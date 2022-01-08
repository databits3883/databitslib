package com.databits3883.databitslib.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PIDParametersTest {
    @Test void zeroInitialization(){
        PIDParameters p = new PIDParameters();
        assertEquals(0,p.p,"P not initialized to 0");
        assertEquals(0,p.i,"I not initialized to 0");
        assertEquals(0, p.d,"D not initialized to 0");
        assertEquals(0, p.ff,"FF not initialized to 0");
    }

    @Test void threeParameterInitialization(){
        PIDParameters p = new PIDParameters(0.1,0.2,0.3);
        assertEquals(0.1, p.p,"P improperly initialized");
        assertEquals(0.2, p.i,"I improperly initialized");
        assertEquals(0.3, p.d,"D improperly initialized");
        assertEquals(0, p.ff,"FF not initialized to zero when not supplied initial value");
    }

    @Test void fourParameterInitialization(){
        PIDParameters p = new PIDParameters(0.1,0.2,0.3,0.4);
        assertEquals(0.1, p.p,"P improperly initialized");
        assertEquals(0.2, p.i,"I improperly initialized");
        assertEquals(0.3, p.d,"D improperly initialized");
        assertEquals(0.4, p.ff,"FF improperly initialized");
    }
}
