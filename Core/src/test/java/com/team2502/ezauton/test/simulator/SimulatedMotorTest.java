package com.team2502.ezauton.test.simulator;

import com.team2502.ezauton.actuators.implementations.BaseSimulatedMotor;
import com.team2502.ezauton.utils.SimulatedClock;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SimulatedMotorTest
{
    @Test
    public void testMotor()
    {
        SimulatedClock clock = new SimulatedClock();
        BaseSimulatedMotor motor = new BaseSimulatedMotor(clock);

        Assert.assertEquals(0, motor.getPosition(), 1E-6);
        motor.runVelocity(1);

        clock.addTime(TimeUnit.SECONDS, 1);
        Assert.assertEquals(1, motor.getPosition(), 1E-6);

        clock.addTime(TimeUnit.SECONDS, 1);
        Assert.assertEquals(2, motor.getPosition(), 1E-6);
        motor.runVelocity(10);
        Assert.assertEquals(2, motor.getPosition(), 1E-6);

        clock.addTime(TimeUnit.SECONDS, 1);
        Assert.assertEquals(12, motor.getPosition(), 1E-6);

    }
}
