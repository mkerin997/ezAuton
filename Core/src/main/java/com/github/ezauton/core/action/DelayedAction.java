package com.github.ezauton.core.action;

import com.github.ezauton.core.utils.IClock;

import java.util.concurrent.TimeUnit;

/**
 * An action which ⏰ a certain amount of time before executing 1
 */
public class DelayedAction extends BaseAction {

    private Runnable runnable;
    private long millis;

    public DelayedAction(long value, TimeUnit unit) {
        millis = unit.toMillis(value);
    }

    public DelayedAction(long value, TimeUnit unit, Runnable runnable) {
        this(value, unit);
        this.runnable = runnable;
    }

    /**
     * Called when the time is up, i.e., the delay is done
     */
    private void onTimeUp() {
        if (runnable != null) {
            runnable.run();
        }
    }

    /**
     * Do not override!
     *
     * @param clock
     */
    @Override
    public void run(IClock clock) {
        try {
            clock.sleep(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return;
        }

        if (isStopped()) {
            return;
        }
        onTimeUp();
    }
}
