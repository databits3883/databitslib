package com.databits3883.databitslib.sparkmax;
//TODO: add integration testing instructions or automation
import com.databits3883.databitslib.util.PIDParameters;
import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANEncoder;
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
    CANEncoder m_encoder;
    CANPIDController m_controller;
    PIDParameters m_parameters;
    ControlType m_type;

    double m_setpoint;



    private SparkMaxPIDController(CANSparkMax motor, CANEncoder encoder, ControlType type){
        m_motor = motor;
        m_encoder = encoder;
        m_type = type;

        m_controller = m_motor.getPIDController();
        m_controller.setFeedbackDevice(m_encoder);

        SendableRegistry.addLW(this, "Spark Max PID", m_motor.getDeviceId());
    }

    public static SparkMaxPIDController withDefaultEncoder(CANSparkMax motor, ControlType type){
        CANEncoder encoder = motor.getEncoder();
        return new SparkMaxPIDController(motor, encoder, type);
    }

    public static SparkMaxPIDController withAlternateEncoder(CANSparkMax motor, ControlType type, int countsPerRev){
        CANEncoder encoder = motor.getAlternateEncoder(AlternateEncoderType.kQuadrature, countsPerRev);
        return new SparkMaxPIDController(motor, encoder, type);
    }

    /**
     * Sets the conversion factor for position or velocity depending on preset control type
     * @param conversionFactor the conversion factor from rotations
     * @return any error thrown by setting the value, kNotImplemented for non velocity or position types, 
     * kError if control type is not set
     */
    public CANError setConversionFactor(double conversionFactor){
        switch (m_type){
            case kCurrent:
            case kDutyCycle:
            case kVoltage:
                return CANError.kNotImplmented;

            case kPosition:
            case kSmartMotion:
                return m_encoder.setPositionConversionFactor(conversionFactor);
            case kVelocity:
            case kSmartVelocity:
                return m_encoder.setVelocityConversionFactor(conversionFactor);
            default:
                return CANError.kError;
        }
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
