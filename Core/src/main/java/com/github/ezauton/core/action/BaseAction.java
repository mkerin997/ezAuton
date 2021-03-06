package com.github.ezauton.core.action;

import java.util.ArrayList;
import java.util.List;

/**
 * The base implementation of an Action. Most actions are based off of this class, as it provides an easy, base
 * implementation.
 */
public class BaseAction implements Action {

    private List<Runnable> toRun = new ArrayList<>();
    private Runnable runnable;

    /**
     * Create an empty {@link BaseAction} which does not run anything
     */
    public BaseAction() {

    }

    /**
     * Runs the provided {@link Runnable} once.
     * <p>
     * To avoid confusion on whether {@link Runnable}s execute sequentially or in parallel, only one runnable is allowed.
     * To easily create an action with multiple {@link Runnable}s or sub actions, see {@link ActionGroup}
     *
     * @param
     */
    public BaseAction(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run(ActionRunInfo actionRunInfo) throws Exception {
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override
    public final Action onFinish(Runnable onFinish) {
        toRun.add(onFinish);
        return this;
    }

    @Override
    public final List<Runnable> getFinished() {
        return toRun;
    }
}
