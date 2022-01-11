package com.databits3883.databitslib.sparkmax;
//TODO: add integration testing instructions or automation
import com.databits3883.databitslib.util.PIDParameters;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import com.revrobotics.REVLibError;
import com.revrobotics.SparkMaxAlternateEncoder;



/**
 * Wrapper for basic operation with CANSparkMax motor controllers.
 * 
 * Implements the Sendable interface for configuration and testing over networkTables with ShuffleBoard.
 * 
 */
public class SparkMaxPIDController implements Sendable{
    CANSparkMax m_motor;
    RelativeEncoder m_encoder;
    com.revrobotics.SparkMaxPIDController m_controller;
    PIDParameters m_parameters;
    ControlType m_type;

    double m_setpoint;

    private SparkMaxPIDController(CANSparkMax motor, RelativeEncoder encoder, ControlType type){
        m_motor = motor;
        m_encoder = encoder;
        m_type = type;

        m_controller = m_motor.getPIDController();
        m_controller.setFeedbackDevice(m_encoder);

        SendableRegistry.addLW(this, "Spark Max PID", m_motor.getDeviceId());
    }

    /**
     * Creates a spark max pid controller using the encoder configured in firmware
     * @param motor The motor to control
     * @param type The control type to use
     * @return a PID controller object
     */
    public static SparkMaxPIDController withDefaultEncoder(CANSparkMax motor, ControlType type){
        RelativeEncoder encoder = motor.getEncoder();
        return new SparkMaxPIDController(motor, encoder, type);
    }
    /**
     * Creates a spark max pid controller using a connected encoder
     * @param motor The motor to control
     * @param type The control type to use
     * @param encoderType The encoder to use
     * @param countsPerRev The encoder counts per revolution
     * @return A PID controller object
     */
    public static SparkMaxPIDController withEncoder(CANSparkMax motor, ControlType type, Type encoderType, int countsPerRev){
        RelativeEncoder encoder = motor.getEncoder(encoderType, countsPerRev);
        return new SparkMaxPIDController(motor, encoder, type);
    }

    /**
     * Creates a spark max pid controller using the alternate input controller
     * @param motor The motor to control
     * @param type The control type to use
     * @param countsPerRev The counts per revolution on the external encoder
     * @return a PID controller object
     */
    public static SparkMaxPIDController withAlternateEncoder(CANSparkMax motor, ControlType type, int countsPerRev){
        RelativeEncoder encoder = motor.getAlternateEncoder(SparkMaxAlternateEncoder.Type.kQuadrature, countsPerRev);
        return new SparkMaxPIDController(motor, encoder, type);
    }

    /**
     * Sets the conversion factor for position or velocity depending on preset control type
     * @param conversionFactor the conversion factor from rotations
     * @return any error thrown by setting the value, kNotImplemented for non velocity or position types, 
     * kError if control type is not set
     */
    public REVLibError setConversionFactor(double conversionFactor){
        switch (m_type){
            case kCurrent:
            case kDutyCycle:
            case kVoltage:
                return REVLibError.kInvalid;

            case kPosition:
            case kSmartMotion:
                return m_encoder.setPositionConversionFactor(conversionFactor);
            case kVelocity:
            case kSmartVelocity:
                return m_encoder.setVelocityConversionFactor(conversionFactor);
            default:
                return REVLibError.kError;
        }
    }

    public REVLibError setP(double newP){
        REVLibError e = m_controller.setP(newP);
        if(e == REVLibError.kOk){
            m_parameters.p = newP;            
        }
        return e;
    }
    public REVLibError setI(double newI){
        REVLibError e = m_controller.setI(newI);
        if(e == REVLibError.kOk){
            m_parameters.i = newI;            
        }
        return e;
    }
    public REVLibError setD(double newD){
        REVLibError e = m_controller.setD(newD);
        if(e == REVLibError.kOk){
            m_parameters.d = newD;            
        }
        return e;
    }
    public REVLibError setFF(double newFF){
        REVLibError e = m_controller.setFF(newFF);
        if(e == REVLibError.kOk){
            m_parameters.ff = newFF;            
        }
        return e;
    }
    public REVLibError setSetpoint(double newSetpoint){
        REVLibError e = m_controller.setReference(newSetpoint, m_type);
        if(e==REVLibError.kOk){
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
     * Gets the current value of the controlled signal.
     * @return the value controlled by the PID loop
     */
    public double getSignal(){
        switch (m_type){
            case kCurrent:
                return m_motor.getOutputCurrent();
            case kDutyCycle:
                return m_motor.get();
            case kVoltage:
                return m_motor.getBusVoltage();
            case kPosition:
            case kSmartMotion:
                return m_encoder.getPosition();
            case kVelocity:
            case kSmartVelocity:
                return m_encoder.getPosition();
            default:
                return 0;
        }
    }

    /**
     * Resets the PID controller, clearing the I accumulator
     * @return any error produced in resetting the controller, or kOK if successful
     */
    public REVLibError reset(){
        m_motor.disable();
        REVLibError e = m_controller.setIAccum(0);
        m_controller.setReference(m_setpoint, m_type);
        return e;
    }


    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("p", this::getP, this::setP);
        builder.addDoubleProperty("i", this::getI, this::setI);
        builder.addDoubleProperty("d", this::getD, this::setD);
        builder.addDoubleProperty("f", this::getFF, this::setFF);
        builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
        builder.addDoubleProperty("current valaue", this::getSignal, (a)->{return;});
    }


}
