package com.databits3883.databitslib.swerveControl;

import com.databits3883.databitslib.sparkmax.SparkMaxPIDController;
import com.revrobotics.CANError;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

public class SwerveModule implements Sendable{

    SparkMaxPIDController m_velocityController;
    SparkMaxPIDController m_rotationController;

    SwerveModuleState currentState;

    public SwerveModule(SparkMaxPIDController velocityController, SparkMaxPIDController rotationController, String name){
        m_velocityController = velocityController;
        m_rotationController = rotationController;
        SendableRegistry.addLW(this, name);
    }

    public void setState(SwerveModuleState newState){
        m_velocityController.setSetpoint(newState.speedMetersPerSecond);
        double newAngle = newState.angle.getRadians();
        double currentAngle = m_rotationController.getSignal();
        m_rotationController.setSetpoint(mapAngleToNearContinuous(currentAngle, newAngle));

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

    /**
     * Set the conversion factor for wheel angle control to account for gearing
     * @param factor the factor converting encoder rotations to wheel rotations
     * @return any Error raised in setting the factor
     */
    public CANError setAngleConversionFactor(double factor){
        return m_rotationController.setConversionFactor(factor* 2*Math.PI);
    }

    /**
     * Set the conversion factor for wheel velocity control to account for gearing
     * @param factor the factor converting encoder rotations to wheel rotations
     * @return any Error raised in setting the factor
     */
    public CANError setVelocityConversionFactor(double factor){
        return m_velocityController.setConversionFactor(factor);
    }
    
}
