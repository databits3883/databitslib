package com.databits3883.databitslib.util;

public class PIDParameters {
    public double p;
    public double i;
    public double d;
    public double ff;

    public PIDParameters(){
        this(0,0,0,0);
    }
    public PIDParameters(double _p,double _i,double _d){
        this(_p,_i,_d,0);
    }
    public PIDParameters(double _p, double _i, double _d, double _ff){
        p = _p;
        i = _i;
        d = _d;
        ff = _ff;
    }
}
