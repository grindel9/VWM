package db_walker.utils.randomizers;

import java.util.*;

/**
 * Attribute randomizer based on java Random class
 */
public final class JavaRandomizer implements AttributeRandomizer {
    /**
     * Simple Java Randomizer constructor
     * @param validBrands is valid brands
     * @param validTypes is valid types
     * @param validStatuses is valid statuses
     * @param validCountries is valid countries
     */
    public JavaRandomizer(Collection<String> validBrands,
                          Collection<String> validTypes,
                          Collection<String> validStatuses,
                          Collection<String> validCountries) {
        this.validBrands = new ArrayList<>(validBrands);
        this.validTypes = new ArrayList<>(validTypes);
        this.validStatuses = new ArrayList<>(validStatuses);
        this.validCountries = new ArrayList<>(validCountries);
        this.random = new Random();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRandomValidBrand() {
        return validBrands.get(random.nextInt(validBrands.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getValidBrands() {
        return validBrands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRandomValidType() {
        return validTypes.get(random.nextInt(validTypes.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getValidTypes() {
        return validTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRandomValidStatus() {
        return validStatuses.get(random.nextInt(validStatuses.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getValidStatuses() {
        return validStatuses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRandomValidCountry() {
        return validCountries.get(random.nextInt(validCountries.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getValidCountries() {
        return validCountries;
    }

    private final List<String> validBrands;
    private final List<String> validTypes;
    private final List<String> validStatuses;
    private final List<String> validCountries;
    private final Random random;
}
