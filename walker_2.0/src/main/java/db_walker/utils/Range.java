package db_walker.utils;

/**
 * Class defining a range
 */
public final class Range {
    /**
     * Simple range constructor
     * @param from is the starting point of range
     * @param to is the ending point of range
     */
    public Range(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("'From' needs to be <= than 'to'!");
        }
        this.from = from;
        this.to = to;
    }
    /**
     * Getter to "from" value of range
     * @return from
     */
    public int from() { return this.from; }

    /**
     * Getter to "to" value of range
     * @return to
     */
    public int to(){ return this.to; }

    public final String toSql(){ return this.from + " and " + this.to;}

    @Override
    public String toString() {
        return Integer.toString(this.to - this.from);
    }

    private final int from, to;
}
