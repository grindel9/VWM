package db_walker.walker;

import db_walker.utils.Product;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

public class WorkCycle {
    /**
     * Simple constructor for Work Cycle
     * @param walker is the Random db_walker.walker upon which the cycle is made
     */
    public WorkCycle(RandomWalker walker) {
        this.walker = walker;
        this.acquiredProducts = new TreeSet<>();
    }

    /**
     * Do one cycle of db sampling
     * @throws SQLException if request to db was unsuccessful
     */
    synchronized public void doOneCycle() throws SQLException {
        // check if we are to end
        if (this.walker.wasWantedSampleSizeSet()) {
            if (acquiredAmount() >= this.walker.wantedSampleSize) {
                System.out.println("Got the wanted sample size!");
                this.walker.overseer.interrupt();
                this.walker.isWorking = false;
            }
        }

        // get a random sample
        Set<Product> sample = this.walker.getRandomSample();

        synchronized (this.acquiredProducts) {
            this.acquiredProducts.addAll(sample);
        }
    }

    /**
     * Getter for acquired products
     * @return products acquired so far
     */
    public Set<Product> acquiredProducts() {
        return this.acquiredProducts;
    }

    /**
     * Get already acquired amount of products
     * @return the size of set of products acquired
     */
    public int acquiredAmount() {
        synchronized (this.acquiredProducts) {
            return this.acquiredProducts.size();
        }
    }

    private final RandomWalker walker;
    private final Set<Product> acquiredProducts;
}
