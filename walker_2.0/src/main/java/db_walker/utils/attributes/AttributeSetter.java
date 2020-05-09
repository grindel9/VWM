package db_walker.utils.attributes;

/**
 * Class that represents an attribute setter for the database.
 */
public abstract class AttributeSetter implements Comparable<AttributeSetter> {
    /**
     * Primary constructor.
     * @param argument is the argument that will be sent to the db
     * @param priority is the priority of the argument, lower value == earlier execution
     * @param branchingLevel is the level of branching at this level in tree
     */
    public AttributeSetter(String argument, int priority, int branchingLevel) {
        this.argument = argument;
        this.priority = priority;
        this.branchingLevel = branchingLevel;
    }

    /**
     * Getter for priority value
     * @return value of priority
     */
    public final int priority(){ return this.priority; }

    /**
     * Getter for level of branching
     * @return value of branching at this level of attribute
     */
    public final int levelOfBranching() { return this.branchingLevel; }

    /**
     * This method will set the attribute inside the databse.
     */
    public abstract void setAttribute();

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(AttributeSetter a) {
        if (this.priority < a.priority)
            return -1;
        else if (this.priority() == a.priority())
            return this.argument.compareTo(a.argument);
        return 1;
    }

    protected final String argument;
    protected final int priority;
    protected final int branchingLevel;
}
