package db_walker.utils;

import db_walker.walker.RandomWalker;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that periodically controls the progress of the walker.
 */
public class Reporter extends Thread implements JSONSerializable {
    public Reporter(RandomWalker walker, double frequency) {
        this.walker = walker;
        this.frequency = frequency;
        this.acquired = new ArrayList<>();
    }

    @Override
    public void run() {
        while (this.walker.isWorking()) {
            // sleep for seconds
            try {
                Thread.sleep((long)(this.frequency*1000));
                int current = this.walker.currentAcquiredAmount();
                this.acquired.add(current);
            } catch (InterruptedException e) {
                System.out.println("Reporter interrupted.");
            }
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print('{');
        // frequency
        writer.printf("\"frequency\":%f", this.frequency);
        writer.print(',');

        // add all acquired
        writer.printf("\"acquired times\":");
        writer.print('[');
        for(int i = 0; i < this.acquired.size(); ++i) {
            if (i != 0)
                writer.printf(",");
            writer.printf(String.valueOf(this.acquired.get(i)));
        }

        writer.print(']');
        writer.print('}');
    }

    private final RandomWalker walker;
    private final double frequency;
    private final List<Integer> acquired;
}
