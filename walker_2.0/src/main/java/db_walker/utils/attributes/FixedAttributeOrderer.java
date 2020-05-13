package db_walker.utils.attributes;

import db_walker.db_api.DatabaseAccessor;
import db_walker.utils.Range;

import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * A fixed ordering class.
 */
public final class FixedAttributeOrderer extends AttributeOrderer {
    /**
     * Primary constructor.
     * @param db is the db on which the orderer operates
     */
    public FixedAttributeOrderer(DatabaseAccessor db) {
        super(db);
        this.brandPriority = db.getValidBrands().size();
        this.countryPriority = db.getValidCountries().size();
        this.typePriority = db.getValidTypes().size();
        this.statusPriority = db.getValidStatuses().size();
        this.random = new Random();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<AttributeSetter> getNextOrdering() {
        Set<AttributeSetter> tmp = new TreeSet<>();
        // brand
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidBrand(),
                this.brandPriority,
                this.brandPriority) {
            @Override
            public void setAttribute() {
                database.addWantedBrand(this.argument);
            }
        });
        // country
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidCountry(),
                this.countryPriority,
                this.countryPriority) {
            @Override
            public void setAttribute() {
                database.addWantedCountry(this.argument);
            }
        });
        // type
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidType(),
                this.typePriority,
                this.typePriority) {
            @Override
            public void setAttribute() {
                database.setWantedType(this.argument);
            }
        });
        // status
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidStatus(),
                this.statusPriority,
                this.statusPriority) {
            @Override
            public void setAttribute() {
                database.setWantedStatus(this.argument);
            }
        });
        // divide price ranges
        int from = this.minPrice;
        int to = this.maxPrice;
        int priority = 2; // starting priority is 2, then we multiply by 2 each cycle
        while (from < to) {
            tmp.add(new AttributeSetter(new Range(from, to).toSql(), priority, 2) {
                @Override
                public void setAttribute() {
                    database.setWantedPriceRange(this.argument);
                }
            });
            if (this.random.nextBoolean())
                from += (to-from+1)/2;
            else
                to -= (to-from+1)/2;
            priority *= 2;
        }
        // also divide screen sizes
        from = this.minScreenSize;
        to = this.maxScreenSize;
        priority = 2; // same principle here
        while (from < to) {
            tmp.add(new AttributeSetter(new Range(from, to).toSql(), priority, 2) {
                @Override
                public void setAttribute() {
                    database.setWantedScreenSizeRange(this.argument);
                }
            });
            if (this.random.nextBoolean())
                from += (to-from+1)/2;
            else
                to -= (to-from+1)/2;
            priority *= 2;
        }

        return tmp;
    }

    /**
     * @return "fixed"
     */
    @Override
    public String toString() {
        return "fixed";
    }

    private final int brandPriority;
    private final int countryPriority;
    private final int typePriority;
    private final int statusPriority;
    private final Random random;
}
