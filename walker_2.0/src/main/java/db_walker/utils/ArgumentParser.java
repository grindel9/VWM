package db_walker.utils;

import java.util.*;

public class ArgumentParser {
    public static void printUsage() {
        System.out.println("Usage: java -jar app.jar " +
                "[--timeout value(seconds)] " +
                "[--request-limit value]" +
                "[--wanted-sample value]" +
                "Defaults: timeout: 10, request limit: 5, wanted sample: None" +
                "");
    }

    public static Map<String, String> parseArguments(List<String> args) {
        List<String> validKeys = new ArrayList<>(Arrays.asList("--timeout", "--request-limit", "--wanted-sample"));

        if (args.size()%2 != 0) {
            printUsage();
            return null;
        }

        Map<String, String> returnValues = new HashMap<>();
        for (int i = 0; i < args.size(); i += 2) {
            String key = args.get(i);
            String value = args.get(i+1);
            // invalid input
            if (!validKeys.contains(key)) {
                System.out.println("Invalid option: " + key);
                printUsage();
                return null;
            }

            returnValues.put(key, value);
        }

        return returnValues;
    }
}
