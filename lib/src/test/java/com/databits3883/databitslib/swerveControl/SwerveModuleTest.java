package com.databits3883.databitslib.swerveControl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SwerveModuleTest {
    @Test void angleOffsetTest(){
        assertEquals(0,SwerveModule.mapAngleToNearContinuous(0, 0),0.001);
        assertEquals(0,SwerveModule.mapAngleToNearContinuous(0, Math.PI*2),0.001);
        assertEquals(Math.PI/2,SwerveModule.mapAngleToNearContinuous(Math.PI/4, Math.PI/4 + 4*Math.PI + Math.PI/4),0.001);
        assertEquals(Math.PI/4,SwerveModule.mapAngleToNearContinuous(Math.PI/4, Math.PI/2 + 4*Math.PI - Math.PI/4),0.001);

        assertEquals(Math.PI*4 + Math.PI/2, SwerveModule.mapAngleToNearContinuous(Math.PI*4 + Math.PI/2, Math.PI/2),0.001);
        assertEquals(Math.PI*6 + Math.PI, SwerveModule.mapAngleToNearContinuous(Math.PI*6 + Math.PI, -1*Math.PI),0.001);
        assertEquals(Math.PI*-6 - Math.PI - Math.PI/6, SwerveModule.mapAngleToNearContinuous(Math.PI*-6 - Math.PI, 5*Math.PI/6),0.001);
    }
}
