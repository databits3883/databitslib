package com.databits3883.databitslib.sparkmax;
//TODO: add integration testing instructions or automation
import com.databits3883.databitslib.util.PIDParameters;
import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

/**
 * Wrapper for basic operation with CANSparkMax motor controllers.
 * 
 * Implements the Sendable interface for configuration and testing over networkTables with ShuffleBoard.
 * 
 */
public class SparkMaxPIDController implements Sendable{
    CANSparkMax m_motor;
    CANPIDController m_controller;
    PIDParameters m_parameters;
    ControlType m_type;

    double m_setpoint;

    /**
     * Creates a new SparkMaxPIDController
     * @param motor the configured motor to control
     * @param type the control type for the PID control loop. Currently this best supports kPosition and kVelocity
     * @param parameters the PIDF gains for the control loop
     */
    public SparkMaxPIDController(CANSparkMax motor, ControlType type, PIDParameters parameters){
        m_motor = motor;
        m_parameters = parameters;
        m_controller = m_motor.getPIDController();
        m_controller.setP(m_parameters.p);
        m_controller.setI(m_parameters.i);
        m_controller.setD(m_parameters.d);
        m_controller.setFF(m_parameters.ff);
        
        m_type = type;

        SendableRegistry.addLW(this,"SPark Max PID", m_motor.getDeviceId());
    }

    public CANError setP(double newP){
        CANError e = m_controller.setP(newP);
        if(e == CANError.kOk){
            m_parameters.p = newP;            
        }
        return e;
    }
    public CANError setI(double newI){
        CANError e = m_controller.setI(newI);
        if(e == CANError.kOk){
            m_parameters.i = newI;            
        }
        return e;
    }
    public CANError setD(double newD){
        CANError e = m_controller.setD(newD);
        if(e == CANError.kOk){
            m_parameters.d = newD;            
        }
        return e;
    }
    public CANError setFF(double newFF){
        CANError e = m_controller.setFF(newFF);
        if(e == CANError.kOk){
            m_parameters.ff = newFF;            
        }
        return e;
    }
    public CANError setSetpoint(double newSetpoint){
        CANError e = m_controller.setReference(newSetpoint, m_type);
        if(e==CANError.kOk){
            m_setpoint = newSetpoint;
        }
        return e;
    }

    public double getP(){
        return m_parameters.p;
    }
    public double getI(){
        return m_parameters.i;
    }
    public double getD(){
        return m_parameters.d;
    }
    public double getFF(){
        return m_parameters.ff;
    }
    public double getSetpoint(){
        return m_setpoint;
    }

    /**
     * Resets the PID controller, clearing the I accumulator
     * @return any error produced in resetting the controller, or kOK if successful
     */
    public CANError reset(){
        m_motor.disable();
        CANError e = m_controller.setIAccum(0);
        m_controller.setReference(m_setpoint, m_type);
        return e;
    }


    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("PIDController");
        builder.setSafeState(this::reset);
        builder.addDoubleProperty("p", this::getP, this::setP);
        builder.addDoubleProperty("i", this::getI, this::setI);
        builder.addDoubleProperty("d", this::getD, this::setD);
        builder.addDoubleProperty("f", this::getFF, this::setFF);
        builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);

        
    }


}
