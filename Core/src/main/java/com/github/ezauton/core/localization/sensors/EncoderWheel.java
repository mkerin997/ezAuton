package com.github.ezauton.core.localization.sensors;

/**
 * The combination of an encoder and a wheel. This allows to calculate translational distance. An encoder without
 * wheel specifications can only calculate revolutions.
 */
//TODO: Perhaps redundant with Encoders#fixRegEncoder
public class EncoderWheel implements ITranslationalDistanceSensor {

    private final IEncoder encoder;
    private final double wheelDiameter;
    private double multiplier = 1D;
    private double encoderPosMultiplied;
    private double encoderRawPos;


    /**
     * @param encoder       The encoder for measuring revolutions
     * @param wheelDiameter The diameter of the wheel with the encoder (recommended in ft)
     */
    public EncoderWheel(IEncoder encoder, double wheelDiameter) {
        this.encoder = encoder;
        this.wheelDiameter = wheelDiameter;
        encoderPosMultiplied = encoder.getPosition() * getMultiplier();
        encoderRawPos = encoder.getPosition();
    }

    public IEncoder getEncoder() {
        return encoder;
    }

    public double getWheelDiameter() {
        return wheelDiameter;
    }

    public double getMultiplier() {
        return multiplier;
    }

    /**
     * @param multiplier If there are additional gear ratios to consider, this is the multiplier
     *                   (wheel rev / encoder rev)
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * @return velocity (probably in ft/s)
     */
    @Override
    public double getVelocity() {
        return encoder.getVelocity() * Math.PI * wheelDiameter * getMultiplier(); // because minute to second
    }

    /**
     * @return position (probably in ft)
     */
    @Override
    public double getPosition() {
        double tempRawPos = encoder.getPosition();
        encoderPosMultiplied = (tempRawPos - encoderRawPos) * getMultiplier() + encoderPosMultiplied;
        encoderRawPos = tempRawPos;
        return encoderPosMultiplied * Math.PI * wheelDiameter;
    }
}
