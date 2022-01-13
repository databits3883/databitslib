package com.databits3883.databitslib.swerveControl;

import com.databits3883.databitslib.sparkmax.SparkMaxPIDController;
import com.revrobotics.REVLibError;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

/** Controlls a swerve module with a controller for wheel angle and velocity */
public class SwerveModule{

    SparkMaxPIDController m_velocityController;
    SparkMaxPIDController m_rotationController;

    SwerveModuleState currentState;

    public SwerveModule(SparkMaxPIDController velocityController, SparkMaxPIDController rotationController){
        m_velocityController = velocityController;
        m_rotationController = rotationController;
    }

    /**
     * Sets the target state of the swerve module
     * @param newState the state to target
     */
    public void setState(SwerveModuleState newState){
        m_velocityController.setSetpoint(newState.speedMetersPerSecond);
        double newAngle = newState.angle.getRadians();
        double currentAngle = m_rotationController.getSignal();
        m_rotationController.setSetpoint(mapAngleToNearContinuous(currentAngle, newAngle));

        currentState = newState;

    }

    /**
     * Optimize a module state to minimize rotational disance from this module's current position
     * @param state the desired state
     * @return A new state equivalent to the input, possibly with the speed inverted and the angle rotated half a rotation
     */
    public SwerveModuleState optimize(SwerveModuleState state){
        return SwerveModuleState.optimize(state, new Rotation2d(m_rotationController.getSetpoint()));
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

    /**
     * Set the conversion factor for wheel angle control to account for gearing
     * @param factor the factor converting encoder rotations to wheel rotations
     * @return any Error raised in setting the factor
     */
    public REVLibError setAngleConversionFactor(double factor){
        return m_rotationController.setConversionFactor(factor* 2*Math.PI);
    }

    /**
     * Set the conversion factor for wheel velocity control to account for gearing
     * @param factor the factor converting encoder rotations to wheel rotations
     * @return any Error raised in setting the factor
     */
    public REVLibError setVelocityConversionFactor(double factor){
        return m_velocityController.setConversionFactor(factor);
    }

    public REVLibError setWheelAngle(Rotation2d angle){
        return m_rotationController.setEncoderPosition(angle.getRadians());
    }
    
}
