package db_walker.utils.attributes;

import db_walker.db_api.DatabaseAccessor;
import db_walker.utils.randomizers.AttributeRandomizer;
import db_walker.utils.randomizers.JavaRandomizer;

import java.util.Collection;

/**
 * Abstract class that it's children will implement attribute ordering.
 */
public abstract class AttributeOrderer {
    /**
     * Simple orderer constructor.
     * @param db is the database that the ordering will happen on
     */
    public AttributeOrderer(DatabaseAccessor db) {
        this.database = db;
        this.attributeRandomizer = new JavaRandomizer(
                this.database.getValidBrands(),
                this.database.getValidTypes(),
                this.database.getValidStatuses(),
                this.database.getValidCountries()
        );
        this.minPrice = db.getMinProductPrice();
        this.maxPrice = db.getMaxProductPrice();
        this.minScreenSize = db.getMinProductScreenSize();
        this.maxScreenSize = db.getMaxProductScreenSize();
    }

    /**
     * Get ordered attribute setters.
     * @return a collestion of attributes to be set consecutively
     */
    public abstract Collection<AttributeSetter> getNextOrdering();

    protected final DatabaseAccessor database;
    protected final AttributeRandomizer attributeRandomizer;
    protected final int minPrice, maxPrice, minScreenSize, maxScreenSize;
}
