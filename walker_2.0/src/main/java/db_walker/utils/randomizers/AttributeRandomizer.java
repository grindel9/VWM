package db_walker.utils.randomizers;

import java.util.List;

/**
 * Randomizer interface to get attributes of products
 */
public interface AttributeRandomizer {
    /**
     * Getter for random valid brand
     * @return random valid brand
     */
    String getRandomValidBrand();

    /**
     * Getter for all valid brands
     * @return valid brands list
     */
    List<String> getValidBrands();

    /**
     * Getter for random valid type
     * @return random valid type
     */
    String getRandomValidType();

    /**
     * Getter for all valid types
     * @return valid types list
     */
    List<String> getValidTypes();

    /**
     * Getter for random valid status
     * @return random valid status
     */
    String getRandomValidStatus();

    /**
     * Getter for all valid statuses
     * @return valid statuses array
     */
    List<String> getValidStatuses();

    /**
     * Getter for random valid country
     * @return random valid country
     */
    String getRandomValidCountry();

    /**
     * Getter for all valid countries
     * @return valid countries array
     */
    List<String> getValidCountries();
}
