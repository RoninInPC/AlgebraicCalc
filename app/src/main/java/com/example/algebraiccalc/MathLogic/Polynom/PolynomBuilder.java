package com.example.algebraiccalc.MathLogic.Polynom;

import com.example.algebraiccalc.MathLogic.Fraction;

import java.math.BigInteger;
import java.util.ArrayList;

public class PolynomBuilder {
    private ArrayList<Fraction> polynom_body_ = new ArrayList<>();
    private BigInteger mod_= null;
    public PolynomBuilder(){};
    public PolynomBuilder addFraction(Fraction fraction){
        polynom_body_.add(fraction);
        return this;
    }
    public PolynomBuilder setArrFraction(ArrayList<Fraction> arrayList){
        polynom_body_ = arrayList;
        return this;
    }
    public PolynomBuilder addMod(BigInteger mod){
        mod_ = mod;
        return this;
    }
    public Polynom build(){
        if(mod_==null)
            return new PolynomOnZ(polynom_body_);
        return new PolynomOnField(polynom_body_,mod_);
    }
}
