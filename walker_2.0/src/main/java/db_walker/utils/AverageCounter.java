package db_walker.utils;

/**
 * Average counter class.
 */
public final class AverageCounter {
    /**
     * A simple constructor.
     */
    public AverageCounter() {
        this.sum = 0;
        this.numberOfElements = 0;
    }

    /**
     * Add an element to be counter to the average
     * @param element is the element to be added
     */
    public void addElement(double element) {
        this.sum += element;
        //System.out.println(this.sum);
        this.numberOfElements += 1;
    }

    /**
     * Getter for current average
     * @return the current average value
     */
    public double getAverage(){ return (double)sum/numberOfElements; }

    private double sum;
    private long numberOfElements;
}
