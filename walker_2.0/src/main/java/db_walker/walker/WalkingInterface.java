package db_walker.walker;

/**
 * Interface for a random db_walker.walker approach to sampling a database
 */
public interface WalkingInterface {
    /**
     * Start method to start walking
     */
    void startWalking();

    /**
     * Wait for db_walker.walker thread to end
     */
    void waitForWalkEnd();

    /**
     * Set timeout for walking. This sets the time the db_walker.walker can work for
     * @param seconds is the amount of seconds the db_walker.walker can work for
     */
    void setTimeout(int seconds);

    /**
     * Set the wanted sample to stop at
     * @param amount is the sample size wanted
     */
    void setWantedSample(int amount);

    /**
     * Output result of the random walk
     */
    void outputResult();
}
