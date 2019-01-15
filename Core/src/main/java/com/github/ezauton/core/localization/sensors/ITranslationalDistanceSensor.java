package com.github.ezauton.core.localization.sensors;

/**
 * Like an encoder but for translational distance instead of rotations. An example of a ITranslationalDistanceSensor is an {@link EncoderWheel}.
 */
public interface ITranslationalDistanceSensor
{
    /**
     * Get the translational position
     * @return
     */
    double getPosition();

    /**
     * Get the <strong>translational</strong> velocity (should be VELOCITY so positive <i>and</i> negative)
     * @return
     */
    double getVelocity();
}
