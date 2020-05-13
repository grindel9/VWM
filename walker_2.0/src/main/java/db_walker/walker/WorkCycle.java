package db_walker.walker;

import db_walker.utils.JSONSerializable;
import db_walker.utils.Product;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

public class WorkCycle implements JSONSerializable {
    /**
     * Simple constructor for Work Cycle
     * @param walker is the Random db_walker.walker upon which the cycle is made
     * @param C is the acceptance/skew factor
     */
    public WorkCycle(RandomWalker walker, double C) {
        this.walker = walker;
        this.acquiredProducts = new TreeSet<>();
        this.C = C;
    }

    /**
     * Do one cycle of db sampling.
     * @throws SQLException if request to db was unsuccessful
     */
    public void doOneCycle() throws SQLException {
        // check if we are to end
        if (this.walker.wasWantedSampleSizeSet()) {
            if (acquiredAmount() >= this.walker.wantedSampleSize) {
                System.out.println("Got the wanted sample size!");
                this.walker.overseer.interrupt();
                this.walker.isWorking = false;
            }
        }

        // get a random sample
        Set<Product> sample = this.walker.getRandomSample(this.C, 0);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{");
        writer.printf("\"C\":%f", this.C);
        writer.printf(",");
        writer.printf("\"acquired amount\":%d", this.acquiredAmount());
        writer.printf("}");
    }

    private final RandomWalker walker;
    private final Set<Product> acquiredProducts;
    private final double C;
}
