package com.databits3883.databitslib.swerveControl;

import java.util.List;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;





public class SwerveDrive{

    SwerveModule[] m_modules;
    SwerveDriveKinematics m_kinematics;
    int m_numModules;

    SwerveModuleState[] m_lastMeasuredStates;

    public SwerveDrive(List<Pair<SwerveModule,Translation2d>> modules){
        m_numModules = modules.size();
        m_modules = new SwerveModule[m_numModules];
        m_lastMeasuredStates = new SwerveModuleState[m_numModules];
        Translation2d[] locations = new Translation2d[m_numModules];
        for(int i=0;i<m_numModules;i++){
            m_modules[i] = modules.get(i).getFirst();
            locations[i] = modules.get(i).getSecond();
        }
        m_kinematics = new SwerveDriveKinematics(locations);
    }

    /**
     * Set swerve module states directly. Preer using setChassisSpeed instead
     * @param states the swerve module states in the order passed to the constructor
     */
    public void setModuleStates(SwerveModuleState[] states){
        for(int i=0;i<m_numModules;i++){
            m_modules[i].setState(m_modules[i].optimize(states[i]));
        }
    }

    public void setChassisSpeed(ChassisSpeeds target){
        SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(target);
        setModuleStates(states);
    }

    public SwerveDriveKinematics getKinematics(){
        return m_kinematics;
    }

    public SwerveModuleState[] measureCurrentState(){
        for(int i=0;i<m_numModules;i++){
            m_lastMeasuredStates[i] = m_modules[i].measureCurrentState();
        }
        return m_lastMeasuredStates;
    }
    
}