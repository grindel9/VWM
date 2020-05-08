package db_walker.utils;

import db_walker.walker.RandomWalker;

public class Reporter extends Thread {
    public Reporter(RandomWalker walker, int frequency) {
        this.walker = walker;
        this.frequency = frequency;
    }

    @Override
    public void run() {
        while (this.walker.isWorking()) {
            // sleep for seconds
            try {
                Thread.sleep(this.frequency*1000);
                System.out.println("Already acquired: " + this.walker.currentAcquiredAmount());
            } catch (InterruptedException e) {
                System.out.println("Reporter interrupted.");
            }
        }
    }

    private final RandomWalker walker;
    private final int frequency;
}
