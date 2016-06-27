package org.fluentd.logger.sender;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Calculate exponential delay for reconnecting
 */
public class ConstantDelayReconnector implements Reconnector {

    private double wait = 50; // Default wait to 50 ms

    private static final int MAX_ERROR_HISTORY_SIZE = 100;

    private Deque<Long> errorHistory = new LinkedList<Long>();

    public ConstantDelayReconnector() {
        errorHistory = new LinkedList<Long>();
    }

    public ConstantDelayReconnector(int wait) {
        this.wait = wait;
        errorHistory = new LinkedList<Long>();
    }

    public void addErrorHistory(long timestamp) {
        errorHistory.addLast(timestamp);
        if (errorHistory.size() > MAX_ERROR_HISTORY_SIZE) {
            errorHistory.removeFirst();
        }
    }

    public boolean isErrorHistoryEmpty() {
        return errorHistory.isEmpty();
    }

    public void clearErrorHistory() {
        errorHistory.clear();
    }

    public boolean enableReconnection(long timestamp) {
        return errorHistory.isEmpty() || timestamp - errorHistory.getLast() >= wait;
    }
}
