package db_walker.utils.attribute_orderers;

import db_walker.db_api.DatabaseAccessor;
import db_walker.utils.Range;

import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * A fixed ordering class.
 */
public class FixedOrderer extends Orderer {
    /**
     * Primary constructor.
     * @param db is the db on which the orderer operates
     */
    public FixedOrderer(DatabaseAccessor db) {
        super(db);
        this.brandPriority = db.getValidBrands().size();
        this.countryPriority = db.getValidCountries().size();
        this.typePriority = db.getValidTypes().size();
        this.statusPriority = db.getValidStatuses().size();
        // price priority == sum(leaves) of branching
        this.startingPricePriority = this.maxPrice - this.minPrice + 1;
        // same with screen size
        this.startingScreenSizePriority = this.maxScreenSize - this.minScreenSize + 1;
        // create a randomizer
        this.random = new Random();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<AttributeSetter> getNextOrdering() {
        Set<AttributeSetter> tmp = new TreeSet<>();
        // brand
        tmp.add(new AttributeSetter(this.attributeRandomizer.getRandomValidBrand(), this.brandPriority) {
            @Override
            public void setAttribute() {
                database.addWantedBrand(this.argument);
            }
        });
        // country
        tmp.add(new AttributeSetter(this.attributeRandomizer.getRandomValidCountry(), this.countryPriority) {
            @Override
            public void setAttribute() {
                database.addWantedCountry(this.argument);
            }
        });
        // type
        tmp.add(new AttributeSetter(this.attributeRandomizer.getRandomValidType(), this.typePriority) {
            @Override
            public void setAttribute() {
                database.setWantedType(this.argument);
            }
        });
        // status
        tmp.add(new AttributeSetter(this.attributeRandomizer.getRandomValidStatus(), this.statusPriority) {
            @Override
            public void setAttribute() {
                database.setWantedStatus(this.argument);
            }
        });
        // discretely divide price ranges
        int from = this.minPrice;
        int to = this.maxPrice;
        int priority = this.startingPricePriority;
        while (from < to) {
            tmp.add(new AttributeSetter(new Range(from, to).toSql(), priority) {
                @Override
                public void setAttribute() {
                    database.setWantedPriceRange(this.argument);
                }
            });
            if (this.random.nextBoolean())
                from += (to-from+1)/2;
            else
                to -= (to-from+1)/2;
            priority += 1;
        }
        // also discretly divide screen sizes
        from = this.minScreenSize;
        to = this.maxScreenSize;
        priority = this.startingScreenSizePriority;
        while (from < to) {
            tmp.add(new AttributeSetter(new Range(from, to).toSql(), priority) {
                @Override
                public void setAttribute() {
                    database.setWantedScreenSizeRange(this.argument);
                }
            });
            if (this.random.nextBoolean())
                from += (to-from+1)/2;
            else
                to -= (to-from+1)/2;
            priority += 1;
        }

        return tmp;
    }

    private final int brandPriority;
    private final int countryPriority;
    private final int typePriority;
    private final int statusPriority;
    private final int startingPricePriority;
    private final int startingScreenSizePriority;
    private final Random random;
}
