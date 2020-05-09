package db_walker.utils;

import java.util.*;

public class ArgumentParser {
    public static void printUsage() {
        System.out.println(
                "Usage: java -jar app.jar --db-url <url> --db-name <name> --username <username> --password <password>\n" +
                "   [-h/--help]\n" +
                "   [--timeout value(seconds)]\n" +
                "   [--request-limit value]\n" +
                "   [--wanted-sample value]\n" +
                "   [--set-c value]\n" +
                "   [--report-frequency value]\n" +
                "   [--output-stats file_name]\n" +
                "   [--ordering fixed/random]\n" +
                "   [--get-products file_name]" +
                "------------------------\n" +
                "Defaults: timeout: 30\n" +
                "          request limit: 1\n" +
                "          wanted sample: None\n" +
                "          C: 0\n" +
                "          report frequency: 1\n" +
                "          output: None\n" +
                "          ordering: fixed\n" +
                "");
    }

    public static Map<String, String> parseArguments(List<String> args) {
        List<String> validKeys = new ArrayList<>(Arrays.asList(
                "--db-url",
                "--db-name",
                "--username",
                "--password",
                "--timeout",
                "--request-limit",
                "--wanted-sample",
                "--set-c",
                "--report-frequency",
                "--output-stats",
                "--ordering",
                "--get-products")
        );

        Map<String, String> returnValues = new HashMap<>();
        for (int i = 0; i < args.size(); i += 2) {
            String key = args.get(i);
            if (key.equals("-h") || key.equals("--help")) {
                printUsage();
                return null;
            }
            if (i+1 == args.size()) {
                System.out.println("Invalid option input.");
                printUsage();
                return null;
            }
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
