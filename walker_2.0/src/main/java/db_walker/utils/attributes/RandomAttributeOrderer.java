package db_walker.utils.attributes;

import db_walker.db_api.DatabaseAccessor;
import db_walker.utils.Range;

import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public final class RandomAttributeOrderer extends AttributeOrderer {
    public RandomAttributeOrderer(DatabaseAccessor db) {
        super(db);
        this.brandSize = db.getValidBrands().size();
        this.countrySize = db.getValidCountries().size();
        this.typeSize = db.getValidTypes().size();
        this.statusSize = db.getValidStatuses().size();
        this.random = new Random();
        // figure out price tree depth
        int priceDepth = (int) (Math.log(this.maxPrice - this.minPrice + 1) / Math.log(2));
        // figure out screen size depth
        int screenDepth = (int) (Math.log(this.maxScreenSize - this.minScreenSize + 1) / Math.log(2));

        // 4 for brand, country, type, status

        this.nonRangeAttributes = 4;
        this.numOfAttributes = priceDepth + screenDepth + nonRangeAttributes;
    }

    private int getNextPriority() {
        return Math.abs(this.random.nextInt() % this.numOfAttributes);
    }


    @Override
    public Collection<AttributeSetter> getNextOrdering() {
        Set<AttributeSetter> tmp = new TreeSet<>();

        int from = this.minPrice;
        int to = this.maxPrice;
        int priority = this.getNextPriority() % nonRangeAttributes;
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
            priority += 1;
        }
        // also divide screen sizes
        from = this.minScreenSize;
        to = this.maxScreenSize;
        priority = this.getNextPriority() % nonRangeAttributes;
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

        // brand
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidBrand(),
                this.getNextPriority(),
                this.brandSize) {
            @Override
            public void setAttribute() {
                database.addWantedBrand(this.argument);
            }
        });
        // country
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidCountry(),
                this.getNextPriority(),
                this.countrySize) {
            @Override
            public void setAttribute() {
                database.addWantedCountry(this.argument);
            }
        });
        // type
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidType(),
                this.getNextPriority(),
                this.typeSize) {
            @Override
            public void setAttribute() {
                database.setWantedType(this.argument);
            }
        });
        // status
        tmp.add(new AttributeSetter(
                this.attributeRandomizer.getRandomValidStatus(),
                this.getNextPriority(),
                this.statusSize) {
            @Override
            public void setAttribute() {
                database.setWantedStatus(this.argument);
            }
        });


        return tmp;
    }

    /**
     * @return "random"
     */
    @Override
    public String toString() {
        return "random";
    }

    private final int brandSize;
    private final int countrySize;
    private final int typeSize;
    private final int statusSize;
    private final Random random;
    private final int numOfAttributes;
    private final int nonRangeAttributes;
}
