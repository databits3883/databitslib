package com.databits3883.databitslib.swerveControl;

import java.util.List;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;




public class SwerveDrive implements Sendable {

    SwerveModule[] m_modules;
    SwerveDriveKinematics m_kinematics;
    int m_numModules;

    public SwerveDrive(List<Pair<SwerveModule,Translation2d>> modules){
        m_numModules = modules.size();
        m_modules = new SwerveModule[m_numModules];
        Translation2d[] locations = new Translation2d[m_numModules];
        for(int i=0;i<m_numModules;i++){
            m_modules[i] = modules.get(i).getFirst();
            locations[i] = modules.get(i).getSecond();
        }
        m_kinematics = new SwerveDriveKinematics(locations);
    }

    protected void setModuleStates(SwerveModuleState[] states){
        for(int i=0;i<m_numModules;i++){
            m_modules[i].setState(states[i]);
        }
    }

    public void setChassisSpeed(ChassisSpeeds target){
        SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(target);
        setModuleStates(states);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        // TODO Auto-generated method stub
        
    }
    
}