package db_walker;

import db_walker.utils.ArgumentParser;
import db_walker.walker.RandomWalker;
import db_walker.walker.WalkingInterface;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        Map<String, String> parsedArguments = ArgumentParser.parseArguments(Arrays.asList(args));

        // default values
        int timeout = 10;
        int requestLimit = 2;
        Integer wantedSample = null;
        boolean randomOrdering = false;

        for (Map.Entry<String, String> entry : parsedArguments.entrySet()) {
            if (entry.getKey().equals("--timeout"))
                timeout = Integer.parseInt(entry.getValue());
            else if (entry.getKey().equals("--request-limit"))
                requestLimit = Integer.parseInt(entry.getValue());
            else if (entry.getKey().equals("--wanted-sample"))
                wantedSample = Integer.parseInt(entry.getValue());
            else if (entry.getKey().equals("--random-ordering"))
                randomOrdering = true;
            else {
                ArgumentParser.printUsage();
                return;
            }
        }

        // Create the walker
        WalkingInterface rw = new RandomWalker(requestLimit, randomOrdering);
        rw.setTimeout(timeout);
        if (wantedSample != null)
            rw.setWantedSample(wantedSample);

        // Start working
        System.out.printf("Running for %s seconds...\n", timeout);
        rw.startWalking();
        rw.waitForWalkEnd();
        rw.outputResult();
    }
}
