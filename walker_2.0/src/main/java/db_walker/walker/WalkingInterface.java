package db_walker.walker;

import java.io.FileNotFoundException;

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
     * Set the output file name.
     * Is none by default.
     * @param fileName is the name of the file to be created for output
     */
    void setOutputStatistics(String fileName);

    /**
     * Specify a file name to which we want to output products. Products are in JSON format.
     * @param filename is the name of the file to output to
     */
    void setOutputProductsFile(String filename);

    /**
     * Output result of the random walk
     */
    void outputStatistics();
}
