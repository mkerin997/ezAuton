package com.github.ezauton.wpilib.sensors;

import com.github.ezauton.core.localization.sensors.Compass;
import com.kauailabs.navx.frc.AHRS;

public class Gyros {
    public static Compass fromNavx(AHRS navx) {
        return () -> {
            double angle = -navx.getAngle(); // we want CCW orientation
            double boundedAngle = angle % 360;
            if (boundedAngle < 0) {
                boundedAngle = 360 + boundedAngle;
            }
            return boundedAngle;
        };
    }
}
