package com.team2502.ezauton.test.purepursuit;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team2502.ezauton.actuators.IVelocityMotor;
import com.team2502.ezauton.actuators.RampUpSimulatedMotor;
import com.team2502.ezauton.command.PPCommand;
import com.team2502.ezauton.helper.EzVoltagePPBuilder;
import com.team2502.ezauton.helper.Paths;
import com.team2502.ezauton.localization.estimators.TankRobotEncoderEncoderEstimator;
import com.team2502.ezauton.localization.sensors.EncoderWheel;
import com.team2502.ezauton.localization.sensors.Encoders;
import com.team2502.ezauton.localization.sensors.IEncoder;
import com.team2502.ezauton.pathplanning.PP_PathGenerator;
import com.team2502.ezauton.pathplanning.Path;
import com.team2502.ezauton.pathplanning.purepursuit.ILookahead;
import com.team2502.ezauton.pathplanning.purepursuit.LookaheadBounds;
import com.team2502.ezauton.pathplanning.purepursuit.PPWaypoint;
import com.team2502.ezauton.pathplanning.purepursuit.PurePursuitMovementStrategy;
import com.team2502.ezauton.robot.ITankRobotConstants;
import com.team2502.ezauton.robot.implemented.TankRobotTransLocDriveable;
import edu.wpi.first.wpilibj.command.Command;

public class PPExamples
{


    /**
     * Uses encoders
     * Minimal use of helper methods
     */
    public void exampleNoHelpers()
    {

        TalonSRX leftTalon = new TalonSRX(1);
        TalonSRX rightTalon = new TalonSRX(2);

        // (x, y, speed, acceleration, deceleration)
        PPWaypoint waypoint1 = PPWaypoint.simple2D(0, 0, 0, 3, -3);
        PPWaypoint waypoint2 = PPWaypoint.simple2D(0, 6, 5, 3, -3);
        PPWaypoint waypoint3 = PPWaypoint.simple2D(0, 12, 0, 3, -3);

        PP_PathGenerator pathGenerator = new PP_PathGenerator(waypoint1, waypoint2, waypoint3);

        // dt = 0.05
        Path path = pathGenerator.generate(0.05);

        PurePursuitMovementStrategy ppMoveStrat = new PurePursuitMovementStrategy(path, 0.1D);

        IVelocityMotor leftMotor = velocity -> leftTalon.set(ControlMode.Velocity, velocity * Encoders.CTRE_MAG_ENCODER);
        IVelocityMotor rightMotor = velocity -> rightTalon.set(ControlMode.Velocity, velocity * Encoders.CTRE_MAG_ENCODER);

        IEncoder leftEncoder = Encoders.fromTalon(leftTalon, Encoders.CTRE_MAG_ENCODER);
        EncoderWheel leftEncoderWheel = new EncoderWheel(leftEncoder, 3);

        IEncoder rightEncoder = Encoders.fromTalon(rightTalon, Encoders.CTRE_MAG_ENCODER);
        EncoderWheel rightEncoderWheel = new EncoderWheel(rightEncoder, 3);

        // The lateral wheel distance between wheels
        ITankRobotConstants constants = () -> 20;

        TankRobotEncoderEncoderEstimator locEstimator = new TankRobotEncoderEncoderEstimator(leftEncoderWheel, rightEncoderWheel, constants);

        ILookahead lookahead = new LookaheadBounds(1, 5, 2, 10, locEstimator);

        TankRobotTransLocDriveable tankRobotTransLocDriveable = new TankRobotTransLocDriveable(leftMotor, rightMotor, locEstimator, locEstimator, constants);
        Command commmand = new PPCommand(ppMoveStrat, locEstimator, lookahead, tankRobotTransLocDriveable, locEstimator).buildWPI();
    }

    public void exampleVoltage()
    {
        TalonSRX leftTalon = new TalonSRX(1);
        TalonSRX rightTalon = new TalonSRX(2);

        PurePursuitMovementStrategy ppMoveStrat = new PurePursuitMovementStrategy(Paths.STRAIGHT_12FT, 0.1D);

        // max speed of robot in feet. This can be any unit; however, units must be consistent across entire use of PP.
        double maxRobotSpeed = 16;

        // We need to limit acceleration for voltage drive because the motor will always need to run within its bounds to
        // get accurate localization
        // we need accel per 20ms because that is how often a command in WPILib is called
        double maxAccelPerSecond = 3D;
        double maxAccelPer20ms = 3 / 50D;

        RampUpSimulatedMotor leftMotor = RampUpSimulatedMotor.fromVolt(voltage -> leftTalon.set(ControlMode.PercentOutput, voltage), maxRobotSpeed, maxAccelPer20ms);
        RampUpSimulatedMotor rightMotor = RampUpSimulatedMotor.fromVolt(voltage -> rightTalon.set(ControlMode.PercentOutput, voltage), maxRobotSpeed, maxAccelPer20ms);

        ITankRobotConstants constants = () -> 5;

        TankRobotEncoderEncoderEstimator locEstimator = new TankRobotEncoderEncoderEstimator(leftMotor, rightMotor, constants);

        ILookahead lookahead = new LookaheadBounds(1, 5, 2, 10, locEstimator);

        TankRobotTransLocDriveable tankRobotTransLocDriveable = new TankRobotTransLocDriveable(leftMotor, rightMotor, locEstimator, locEstimator, constants);
        Command commmand = new PPCommand(ppMoveStrat, locEstimator, lookahead, tankRobotTransLocDriveable, locEstimator).buildWPI();
    }

    public void simpleExample()
    {
        Command command = new EzVoltagePPBuilder()
                .addLeft(new TalonSRX(1))
                .addRight(new TalonSRX(2))
                .addLateralWheelDist(1)
                .addSpeedPair(16, 1)
                .addSpeedPair(1, 0.1)
                .build(Paths.STRAIGHT_12FT, 0.5);
    }
}
