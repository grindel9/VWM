package db_walker;

import db_walker.walker.RandomWalker;
import db_walker.walker.WalkingInterface;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        WalkingInterface rw = RandomWalker.buildWalkerFromArgs(args);
        if (rw == null)
            return;
        rw.startWalking();
        rw.waitForWalkEnd();
        rw.outputResult();
    }
}
