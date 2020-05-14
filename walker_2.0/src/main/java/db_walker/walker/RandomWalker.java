package db_walker.walker;

import db_walker.db_api.DatabaseAccessor;
import db_walker.db_api.HiddenDatabaseApi;
import db_walker.utils.*;
import db_walker.utils.attributes.AttributeSetter;
import db_walker.utils.attributes.FixedAttributeOrderer;
import db_walker.utils.attributes.AttributeOrderer;
import db_walker.utils.attributes.RandomAttributeOrderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

/**
 * A random walk approach to sampling database
 */
public final class RandomWalker implements WalkingInterface, JSONSerializable {
    /**
     * A builder of random walker from application arguments.
     * @param args is the application arguments
     * @throws SQLException if request to db was unsuccessful
     */
    public static RandomWalker buildWalkerFromArgs(String[] args) throws SQLException {
        Map<String, String> parsedArguments = ArgumentParser.parseArguments(Arrays.asList(args));

        if (parsedArguments == null)
            return null;

        // default values
        int timeout = 10;
        int requestLimit = 1;
        boolean randomOrdering = false;
        double reportFrequency = 1;
        double C = 0;
        Integer wantedSample = null;
        String outputFileName = null;
        String dbUrl = null;
        String dbName = null;
        String username = null;
        String password = null;
        String outputForProducts = null;

        for (Map.Entry<String, String> entry : parsedArguments.entrySet()) {
            switch (entry.getKey()) {
                case "--timeout":
                    timeout = Integer.parseInt(entry.getValue());
                    break;
                case "--request-limit":
                    requestLimit = Integer.parseInt(entry.getValue());
                    break;
                case "--wanted-sample":
                    wantedSample = Integer.parseInt(entry.getValue());
                    break;
                case "--set-c":
                    C = Double.parseDouble(entry.getValue());
                    break;
                case "--report-frequency":
                    reportFrequency = Double.parseDouble(entry.getValue());
                    break;
                case "--output-stats":
                    outputFileName = entry.getValue();
                    break;
                case "--db-url":
                    dbUrl = entry.getValue();
                    break;
                case "--db-name":
                    dbName = entry.getValue();
                    break;
                case "--username":
                    username = entry.getValue();
                    break;
                case "--password":
                    password = entry.getValue();
                    break;
                case "--get-products":
                    outputForProducts = entry.getValue();
                    break;
                case "--ordering":
                    if (entry.getValue().equals("fixed"))
                        randomOrdering = false;
                    else if (entry.getValue().equals("random"))
                        randomOrdering = true;
                    else {
                        ArgumentParser.printUsage();
                        return null;
                    }
                    break;
                default:
                    ArgumentParser.printUsage();
                    return null;
            }
        }

        if (dbUrl == null || dbName == null || username == null || password == null) {
            System.out.println("One of required arguments has not been inputted!");
            ArgumentParser.printUsage();
            return null;
        }

        RandomWalker rw = new RandomWalker(dbUrl, dbName, username, password, requestLimit, C, reportFrequency, randomOrdering);

        rw.setTimeout(timeout);
        if (wantedSample != null)
            rw.setWantedSample(wantedSample);
        if (outputFileName != null)
            rw.setOutputStatistics(outputFileName);
        if (outputForProducts != null)
            rw.setOutputProductsFile(outputForProducts);

        return rw;
    }

    /**
     * Constructor for random db_walker.walker
     * @param dbUrl is the url for the database
     * @param dbName is the name of the database
     * @param username is the username to login as
     * @param password is the password to use for login
     * @param requestLimit is the request limit from the db
     * @param C is the acceptance/skew factor
     * @param reportFrequency is the frequency of reporting in seconds
     * @param randomOrdering specifies whether to order attributes "randomly" or not
     * @throws SQLException if request to db was unsuccessful
     */
    public RandomWalker(
            String dbUrl,
            String dbName,
            String username,
            String password,
            int requestLimit,
            double C,
            double reportFrequency,
            boolean randomOrdering
    ) throws SQLException {
        this.timeout = 10;
        this.isWorking = false;
        this.wantedSampleSize = -1;
        this.workCycle = new WorkCycle(this, C);
        this.reporter = new Reporter(this, reportFrequency);
        this.averageDepthCounter = new AverageCounter();
        this.averageBranchingCounter = new AverageCounter();
        this.requestLimit = requestLimit;
        this.database = new HiddenDatabaseApi(dbUrl, dbName, username, password, requestLimit);

        if (randomOrdering)
            this.attributeOrderer = new RandomAttributeOrderer(this.database);
        else
            this.attributeOrderer = new FixedAttributeOrderer(this.database);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startWalking() {
        // check if walk already started
            if (isWorking) {
                System.out.println("Walker already walking!");
                return;
            }

        // create db_walker.walker thread
        this.walker = new Thread() {
            @Override
            public void run() {
                super.run();
                isWorking = true;
                System.out.println("Starting to work!");
                while (true) {
                        // check if overseer stopped our work
                        if (!isWorking) {
                            System.out.println("Working ending.");
                            break;
                        }
                        // do actual work
                    try {
                        workCycle.doOneCycle();
                    } catch (SQLException throwables) {
                        System.out.println("Error accessing the database during work!");
                        throwables.printStackTrace();
                        isWorking = false;
                        overseer.interrupt();
                        break;
                    }
                }
            }
        };

        this.walker.start();
        startWalkOverseer();
        this.reporter.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitForWalkEnd() {
        try {
            this.overseer.join();
            this.walker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimeout(int seconds) {
            if (isWorking) {
                System.out.println("Can't set a new timeout for already working db_walker.walker.");
                return;
            }
            if (seconds < 1)
                throw new IllegalArgumentException("Timeout needs to be greater than 0!");
            this.timeout = seconds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWantedSample(int amount) {
            if (isWorking) {
                System.out.println("Can't set a new wanted range for already working db_walker.walker.");
                return;
            }
            if (amount < 0)
                throw new IllegalArgumentException("Size of DB needs to be non-negative value!");

            this.wantedSampleSize = amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOutputStatistics(String fileName) {
        this.outputStatisticsFile = fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOutputProductsFile(String filename) {
        this.outputProductsFile = filename;
    }

    /**
     * Walk overseer, that will set the working flag to false when time comes
     */
    private void startWalkOverseer() {
        this.overseer = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    System.out.println("Starting overseer.");
                    // sleep for required time
                    Thread.sleep(timeout*1000);
                } catch (InterruptedException e) {
                    System.out.println("Overseer got interrupted!");
                } finally {
                    isWorking = false;
                    System.out.println("Working overseer stopping work.");
                }
            }
        };

        this.overseer.start();
    }

    /**
     * <pre>
     * Get a random sample from the db
     *
     * This approach has been largely based on:
     * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.96.1181&rep=rep1&type=pdf
     * Major credit to writers of the paper.
     *
     * 1.)
     * Early termination (repeat query if we have already gone "nowhere")
     * Very simple implementation, we just check whether our query is an underflow, and if so, we repeat.
     *
     * 2.)
     * Ordering of attributes while "traversing" the tree>
     * This is by default done in fixed order, since our attributes are categorical or numerical, keeping in mind
     * that numerical can be transformed into categorical very easily using discrete ranges.
     * This is done in the "pre-processing", so we don't need to find this out every time we want to
     * produce a "random" db sample.
     * Can be set to be ordered randomly with appropriate methods of this class before the run is started.
     *
     * 3.)
     * Boosting acceptance probabilities by a scale factor
     * As proposed in the paper, it is preferable to not accept each valid returned tuple, since the distribution
     * of elements in the database may be unbalance, it is a good approach to take this into account.
     * Thus, let's call the acceptance of a tuple t as a(t).
     * In this, algorithm, we calculate a(t) as:
     *
     *   avgBranchingThisQuery/currentDepth
     * -------------------------------------
     *   avgBranchingTotal^(avgDepthTotal-C)
     *
     * where C is a value which if we increase, acceptance decreases, but skew increases, and on the other hand, if
     * we decrease C, acceptance decreases but skew increases.
     *
     *
     * Notes:
     *  > Traversing price ranges or screen sizes is done in binary fashion, meaning that we start
     *  > from Range(minPrice, maxPrice), and we "randomly" increase minPrice by (max - min)/2
     *  > or decrease maxPrice by that amount.
     *  > Same is done with screen size.
     * </pre>
     *
     * @return a "random" set of products from the db
     */
    public Set<Product> getRandomSample(double C, int recursionDepth) throws SQLException {
        // Prevention for infinite recursion
        if (recursionDepth == 50) {
            return new HashSet<>();
        }
        this.database.resetQuery();
        Collection<AttributeSetter> randomWalk = this.attributeOrderer.getNextOrdering();
        double currentDepth = 0;
        double currentChance = 1;
        AverageCounter averageBranch = new AverageCounter();
        for (AttributeSetter setter : randomWalk) {
            currentDepth += 1;
            currentChance *= setter.levelOfBranching();
            averageBranch.addElement(setter.levelOfBranching());

            setter.setAttribute();

            if (isUnderflow())
                return getRandomSample(C, recursionDepth+1);
            else if (!isOverflow()) {

                this.averageDepthCounter.addElement(currentDepth);
                this.averageBranchingCounter.addElement((double)setter.levelOfBranching());

                double randomNumber = Math.random();
                double denominator = Math.pow(this.averageBranchingCounter.getAverage(), this.averageDepthCounter.getAverage() - C);
                double bias = Math.pow(averageBranch.getAverage(), currentDepth) / denominator;

                if (randomNumber < bias) {
                    return this.database.get();
                }

                this.unAccepted += 1;

                return this.getRandomSample(C, recursionDepth + 1);
            }
        }

        return new HashSet<>();
    }

    /**
     * Check whether current query is underflow
     * @return true if result.size == 0
     * @throws SQLException if request to db was unsuccessful
     */
    private boolean isUnderflow() throws SQLException {
        return this.database.doesCurrentQueryUnderflow();
    }

    /**
     * Check whether current query is overflow
     * @return if query result.size >= db request limit
     * @throws SQLException if request to db was unsuccessful
     */
    private boolean isOverflow() throws SQLException {
        return this.database.doesCurrentQueryOverflow();
    }

    /**
     * Check whether was wanted range set or not
     * @return boolean depending on if wanted range was specified or not
     */
    protected boolean wasWantedSampleSizeSet(){
        return wantedSampleSize != -1;
    }

    /**
     * Getter for whether the worker is still working.
     * @return true if worker is still working
     */
    public boolean isWorking(){ return isWorking; }

    /**
     * Convenience wrapper to check whether output file was set.
     * @return true if output file name was set
     */
    private boolean wasOutputStatisticsSet(){ return this.outputStatisticsFile != null; }

    /**
     * Getter for current acquired amount.
     * @return the number of products already acquired
     */
    public int currentAcquiredAmount(){
        return this.workCycle.acquiredAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void outputStatistics() {
        if (isWorking) {
            System.out.println("Working has not ended yet, can't output result!");
            return;
        }
        //this.workCycle.acquiredProducts().forEach(System.out::println);
        System.out.printf("Got %d samples.\n", this.workCycle.acquiredProducts().size());
        System.out.printf("Average depth: %f\n", this.averageDepthCounter.getAverage());
        System.out.println("Unaccepted: " + this.unAccepted);

        if (wasOutputStatisticsSet()) {
            try {
                toJSON(new PrintWriter(new File(this.outputStatisticsFile)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (outputProductsFile != null) {
            try {
                PrintWriter p = new PrintWriter(new File(this.outputProductsFile));
                p.print("[");
                int i = 0;
                for (Product product : this.workCycle.acquiredProducts()) {
                    if (i != 0)
                        p.println(',');
                    product.toJSON(p);
                    i += 1;
                }
                p.print("]");
                p.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{");

        // writer timer
        writer.printf("\"timeout\":%d,", this.timeout);

        // request limit
        writer.printf("\"request limit\":%d,", this.requestLimit);

        writer.printf("\"ordering\":\"%s\",", this.attributeOrderer.toString());

        // average depth
        writer.printf("\"average depth\":%f,", this.averageDepthCounter.getAverage());

        // average branching
        writer.printf("\"average branching\":%f,", this.averageBranchingCounter.getAverage());

        // writer walker
        writer.printf("\"walker\":");
        this.workCycle.toJSON(writer);
        writer.printf(",");

        //
        writer.printf("\"not accepted\":%d,", this.unAccepted);

        // reporter
        writer.printf("\"reporter\":");
        this.reporter.toJSON(writer);
        writer.printf("}");
        writer.flush();
    }

    protected int timeout, wantedSampleSize;
    protected Thread walker, overseer;
    protected volatile Boolean isWorking;

    private final WorkCycle workCycle;
    private final Reporter reporter;
    private final AverageCounter averageDepthCounter;
    private final AverageCounter averageBranchingCounter;
    private final AttributeOrderer attributeOrderer;
    private final DatabaseAccessor database;
    private final int requestLimit;
    private String outputStatisticsFile, outputProductsFile;
    private long unAccepted = 0;
}
