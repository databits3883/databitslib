package com.databits3883.databitslib.sparkmax;

import com.databits3883.databitslib.util.PIDParameters;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

public class DebugSparkMaxPIDController extends SparkMaxPIDController implements Sendable {

    public DebugSparkMaxPIDController(CANSparkMax motor, RelativeEncoder encoder, ControlType type,
            PIDParameters parameters) {
        super(motor, encoder, type, parameters);
        SendableRegistry.addLW(this, "Spark Max PID", m_motor.getDeviceId());
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
