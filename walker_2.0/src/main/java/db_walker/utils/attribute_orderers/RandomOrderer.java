package db_walker.utils.attribute_orderers;

import db_walker.db_api.DatabaseAccessor;

import java.util.Collection;

public class RandomOrderer extends Orderer {
    public RandomOrderer(DatabaseAccessor db) {
        super(db);
    }

    @Override
    public Collection<AttributeSetter> getNextOrdering() {
        return null;
    }
}
