package com.example.algebraiccalc.MathLogic;

import com.example.algebraiccalc.MathLogic.Fraction;

import org.antlr.v4.runtime.misc.Triple;

import java.math.BigInteger;

public class Evklid {
    protected static Triple<BigInteger,BigInteger,BigInteger> AdvancedEvklid(BigInteger element, BigInteger mod){
        if(mod.equals(BigInteger.ZERO)){
            return new Triple<>(element,BigInteger.ONE,BigInteger.ZERO);
        }else{
            Triple<BigInteger,BigInteger,BigInteger> triple = AdvancedEvklid(mod,element.mod(mod));

            if(!triple.a.equals(BigInteger.ONE))
                return new Triple<>(BigInteger.ZERO,BigInteger.ZERO,BigInteger.ZERO);

            return new Triple<>(triple.a,triple.c,triple.b.subtract(element.divide(mod).multiply(triple.c)));
        }
    }

    public static Fraction DoFractionOnMod(Fraction fraction, BigInteger mod){
        BigInteger numerator = fraction.getNumerator();
        BigInteger denominator = fraction.getDenominator();
        return new Fraction(numerator.mod(mod).multiply(ReverseElement(denominator,mod).mod(mod)).mod(mod),BigInteger.ONE);
    }

    public static BigInteger FromFractionToIntegerOnMod(Fraction fraction,BigInteger mod){
        return DoFractionOnMod(fraction, mod).getNumerator();
    }

    public static BigInteger ReverseElement(BigInteger element, BigInteger mod){
        Triple<BigInteger,BigInteger,BigInteger> triple = AdvancedEvklid(element,mod);
        return triple.b;
    }

    public static BigInteger GCD(BigInteger a, BigInteger b){
        return AdvancedEvklid(a,b).a;
    }
    public static BigInteger LCM(BigInteger a, BigInteger b){
        return a.multiply(b).divide(GCD(a,b));
    }

}
