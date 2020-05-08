package db_walker.walker;

import db_walker.db_api.DatabaseAccessor;
import db_walker.db_api.HiddenDatabaseApi;
import db_walker.utils.Product;
import db_walker.utils.Reporter;
import db_walker.utils.attribute_orderers.AttributeSetter;
import db_walker.utils.randomizers.AttributeRandomizer;
import db_walker.utils.randomizers.JavaRandomizer;
import db_walker.utils.attribute_orderers.FixedOrderer;
import db_walker.utils.attribute_orderers.Orderer;
import db_walker.utils.attribute_orderers.RandomOrderer;

import java.sql.SQLException;
import java.util.*;

/**
 * A random walk approach to sampling database
 */
public final class RandomWalker implements WalkingInterface{
    /**
     * Constructor for random db_walker.walker
     * @param requestLimit is the request limit from the db
     * @param randomOrdering specifies whether we want to randomize attribute ordering
     * @throws SQLException if request to db was unsuccessful
     */
    public RandomWalker(int requestLimit, boolean randomOrdering) throws SQLException {
        this.timeout = 10;
        this.isWorking = false;
        this.wantedSampleSize = -1;
        this.workCycle = new WorkCycle(this);
        this.reporter = new Reporter(this, 5);
        this.database = new HiddenDatabaseApi(requestLimit);

        if (randomOrdering)
            this.attributeOrderer = new RandomOrderer(this.database);
        else
            this.attributeOrderer = new FixedOrderer(this.database);

        this.averageDepth = 0;
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
     * Get a random sample from the db
     *
     *
     * 3 Points need to be kept in mind according to:
     * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.96.1181&rep=rep1&type=pdf
     *
     * 1.) Early termination (repeat query if we have already gone "nowhere")
     *
     * 2.) Ordering of attributes while "traversing" the tree
     *     If the attributes in the DB were only boolean, it would be preferable to have random ordering
     *     However, since our DB is from the most part categorical, traversing from smallest to largest
     *     domains is preferable. Thus we need to sort the attributes based on the number of their valid
     *     values.
     *     This is done in the "pre-processing", so we don't need to find this out every time we want to
     *     produce a "random" db sample.
     *
     * 3.)  Boosting acceptance probabilities by a scale factor // NEEDS TO BE DESCRIBED MORE
     *
     * Notes:
     *      > Traversing price ranges or screen sizes is done in binary fashion, meaning that we start
     *      > from Range(minPrice, maxPrice), and we "randomly" increase minPrice by avg(minPrice, MaxPrice)
     *      > or decrease maxPrice by avg(minPrice, maxPrice).
     *      > Same is done with screen size.
     *      > Since the database interface allows us to get min and max values of those attributes, we can
     *      > determine the maximum depth of this "tree" and take it into account when choosing which attribute
     *      > is to be traversed first.
     *
     * @return a "random" set of products from the db
     */
    public Set<Product> getRandomSample() throws SQLException {
        this.database.resetQuery();
        Collection<AttributeSetter> randomWalk = this.attributeOrderer.getNextOrdering();
        int depth = 1;
        double currentChance = 1.0;
        for (AttributeSetter setter : randomWalk) {
            currentChance *= 1.0/setter.levelOfBranching();
            setter.setAttribute();
            if (isUnderflow())
                return getRandomSample();
            else if (!isOverflow())
                return this.database.get();
            depth += 1;
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
    public void outputResult() {
        if (isWorking) {
            System.out.println("Working has not ended yet, can't output result!");
            return;
        }
        //this.workCycle.acquiredProducts().forEach(System.out::println);
        System.out.printf("Got %d samples.\n", this.workCycle.acquiredProducts().size());
    }

    protected int timeout, wantedSampleSize;
    protected Thread walker, overseer;
    protected volatile Boolean isWorking;

    private final WorkCycle workCycle;
    private final Reporter reporter;
    private final Orderer attributeOrderer;
    private final DatabaseAccessor database;
    private int averageDepth;
}
