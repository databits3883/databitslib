package com.databits3883.databitslib.swerveControl;

import com.databits3883.databitslib.sparkmax.SparkMaxPIDController;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class SwerveModule implements Sendable{

    SparkMaxPIDController velocityController;
    SparkMaxPIDController rotationController;

    SwerveModuleState currentState;

    public SwerveModule(int velocityChannel, int rotationChannel, double velocityGearRation, double angleGearRatio){
        //TODO: controller implementation

        rotationController.setConversionFactor(2*Math.PI*angleGearRatio);
    }

    public void setState(SwerveModuleState newState){
        velocityController.setSetpoint(newState.speedMetersPerSecond);
        double newAngle = newState.angle.getRadians();
        double currentAngle = rotationController.getSignal();
        rotationController.setSetpoint(mapAngleToNearContinuous(currentAngle, newAngle));

        currentState = newState;

    }

    public static double mapAngleToNearContinuous(double currentAngle, double newAngle){
        long completedRotations = Math.round(currentAngle / (Math.PI*2));
        double offsetAngle = newAngle%(Math.PI*2) + (2*Math.PI*completedRotations);
        if(Math.abs(currentAngle - offsetAngle) < Math.PI){
            return offsetAngle;
        }else if(offsetAngle > currentAngle){
            return offsetAngle - Math.PI*2;
        }else{
            return offsetAngle + Math.PI*2;
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        // TODO Auto-generated method stub
        
    }
    
}
