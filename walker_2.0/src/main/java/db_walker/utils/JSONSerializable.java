package db_walker.utils;

import java.io.PrintWriter;

/**
 * Interface to for json serialization.
 */
public interface JSONSerializable {
    /**
     * Method to write to method.
     * @param writer is the writer to which we are writing
     */
    void toJSON(PrintWriter writer);
}
