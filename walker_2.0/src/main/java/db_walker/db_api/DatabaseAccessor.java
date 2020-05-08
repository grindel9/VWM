package db_walker.db_api;

import db_walker.utils.Product;
import db_walker.utils.Range;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Abstract class defining the interface user has to the hidden database.
 */
public abstract class DatabaseAccessor {
    /**
     * Simple constructor.
     */
    public DatabaseAccessor(int requestLimit) {
        // This attribute can be played with
        // it is the "k" in top-k result's databases

        this.requestLimit = requestLimit;
    }

    /**
     * Getter for all valid brands.
     * @return all brands within the database
     */
    public final Set<String> getValidBrands() {
        return this.validBrands;
    }

    /**
     * Getter for all valid countries.
     * @return all countries within the database
     */
    public final Set<String> getValidCountries() {
        return this.validCountries;
    }

    /**
     * Getter for all valid types.
     * @return all valid types within the database
     */
    public final Set<String> getValidTypes() {
        return this.validTypes;
    }

    /**
     * Getter for all valid statuses.
     * @return all valid statuses within the database
     */
    public final Set<String> getValidStatuses() { return this.validStatuses; }

    /**
     * Getter for request limit.
     * This is the 'k' in the top-k interface.
     * @return the top limit value of elements returned from a query
     */
    public final int getRequestLimit(){ return this.requestLimit; }

    /**
     * Getter for minimal product price.
     * This simulates ordering of attributes based on min price.
     * @return min price of product within database
     */
    public abstract int getMinProductPrice();

    /**
     * Getter for maximal product price.
     * This simulates ordering of attributes based on max price.
     * @return max price of product within database
     */
    public abstract int getMaxProductPrice();

    /**
     * Getter for minimal product screen size.
     * This simulates ordering of attributes based on min screen size.
     * @return min screen size of product within database
     */
    public abstract int getMinProductScreenSize();

    /**
     * Getter for maximal product screen size.
     * This simulates ordering of attributes based on max screen size.
     * @return max screen size of product within database
     */
    public abstract int getMaxProductScreenSize();

    /**
     * Add a wanted brand to the query.
     * This simulates a multi-select option.
     * @param brand is the name of brand to match against the db
     */
    public final void addWantedBrand(String brand) {
        this.wantedBrands.add(brand);
    }

    /**
     * Add a wanted country to the query.
     * This simulates a multi-select option.
     * @param country is the name of country to match against the db
     */
    public final void addWantedCountry(String country) {
        this.wantedCountries.add(country);
    }

    /**
     * Select a wanted model.
     * This simulates a text-box interface option.
     * @param model is the model name to be matches against the db (via like '%model%')
     */
    public final void setWantedModel(String model) {
        this.wantedModel = model;
    }

    /**
     * Selects wanted type.
     * This simulates a radio button.
     * @param type is the type selected
     */
    public final void setWantedType(String type) {
        this.wantedType = type;
    }

    /**
     * Selects wanted status.
     * This simulates a radio button.
     * @param status is the type selected
     */
    public final void setWantedStatus(String status) {
        this.wantedStatus = status;
    }

    /**
     * Select a range of screen size.
     * @param r is the range to be matched against product screen size in db (via between x and y)
     */
    public final void setWantedScreenSizeRange(String r) {
        this.wantedScreenSizeRange = r;
    }

    /**
     * Select a range of price.
     * @param r is the range to be matched against product screen size in db (via between x and y)
     */
    public final void setWantedPriceRange(String r) {
        this.wantedPriceRange = r;
    }

    /**
     * Convenience method that check's whether user has set a wanted model.
     * @return true if wanted model was set
     */
    protected final boolean wasWantedModelSet(){
        return this.wantedModel != null;
    }

    /**
     * Convenience method that check's whether user has set a wanted type.
     * @return true if wanted type was set
     */
    protected final boolean wasWantedTypeSet(){
        return this.wantedModel != null;
    }

    /**
     * Convenience method that check's whether user has set a wanted status.
     * @return true if wanted status was set
     */
    protected final boolean wasWantedStatusSet() {
        return this.wantedStatus != null;
    }

    /**
     * Convenience method that check's whether user has set a wanted screen size range.
     * @return true if wanted screen size range was set
     */
    protected final boolean wasWantedScreenSizeSet() {
        return this.wantedScreenSizeRange != null;
    }

    /**
     * Convenience method that check's whether user has set a wanted price range.
     * @return true if wanted price range was set
     */
    protected final boolean wasWantedPriceRangeSet() {
        return this.wantedPriceRange != null;
    }


    /**
     * Reset the entire user-input of query and set query to default.
     */
    public final void resetQuery() {
        this.wantedBrands = new TreeSet<>();
        this.wantedCountries = new TreeSet<>();
        this.wantedModel = null;
        this.wantedType = null;
        this.wantedStatus = null;
        this.wantedPriceRange = null;
        this.wantedScreenSizeRange = null;
    }


    /**
     * Final getter.
     * This will return top-k results based on wanted arguments.
     * @return a set of products matching the query
     * @throws SQLException if request to db was unsuccessful
     */
    public abstract Set<Product> get() throws SQLException;

    /**
     * Getter to check if current query does overflow.
     * @return true if query size is bigger than limit set
     * @throws SQLException if request to db was unsuccessful
     */
    public abstract boolean doesCurrentQueryOverflow() throws SQLException;

    /**
     * Getter to check if current query does overflow.
     * @return true if query size is bigger than limit set
     * @throws SQLException if request to db was unsuccessful
     */
    public abstract boolean doesCurrentQueryUnderflow() throws SQLException;

    // Sets specifying all valid inputs to the API
    protected Set<String> validBrands;
    protected Set<String> validCountries;
    protected Set<String> validTypes;
    protected Set<String> validStatuses;

    // Attributes specifying user query
    protected Set<String> wantedBrands;
    protected Set<String> wantedCountries;
    protected String      wantedModel;
    protected String      wantedType;
    protected String      wantedStatus;
    protected String      wantedScreenSizeRange;
    protected String      wantedPriceRange;
    protected final int   requestLimit;
}
