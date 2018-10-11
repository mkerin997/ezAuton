package org.github.ezauton.ezauton.action;

import org.github.ezauton.ezauton.localization.Updateable;

import java.util.concurrent.TimeUnit;

/**
 * Describes an action that ends after a certain amount of time has elapsed
 */
public class TimedPeriodicAction extends PeriodicAction
{

    private long durationMillis;

    /**
     * @param periodUnit
     * @param period       How long to (repeatedly) run the runnables for
     * @param durationUnit
     * @param duration     The timeunit that period is
     */
    public TimedPeriodicAction(TimeUnit periodUnit, long period, TimeUnit durationUnit, long duration, Updateable... updateables)
    {
        super(periodUnit, period, updateables);
        durationMillis = durationUnit.toMillis(duration);
    }

    @Override
    protected boolean isFinished()
    {
        return this.stopwatch != null && this.stopwatch.read() > durationMillis;
    }

}