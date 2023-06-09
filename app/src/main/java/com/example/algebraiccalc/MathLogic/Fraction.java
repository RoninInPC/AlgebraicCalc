package com.example.algebraiccalc.MathLogic;

import java.math.BigInteger;

public class Fraction {
    private BigInteger numerator_ = BigInteger.ZERO;

    public BigInteger getNumerator() {
        return numerator_;
    }

    public void setNumerator(BigInteger numerator_) {
        this.numerator_ = numerator_;
    }

    public BigInteger getDenominator() {
        return denominator_;
    }

    public void setDenominator(BigInteger denominator_) {
        this.denominator_ = denominator_;
    }

    private BigInteger denominator_ = BigInteger.ONE;

    public static Fraction ZERO = new Fraction(BigInteger.ZERO,BigInteger.ONE);
    public static Fraction ONE = new Fraction(BigInteger.ONE,BigInteger.ONE);
    public Fraction(){};
    public Fraction(BigInteger numerator, BigInteger denominator){
        this.numerator_ = numerator;
        this.denominator_ = denominator;
        optimizeThis();
    }
    private Fraction(BigInteger numerator){
        this.numerator_ = numerator;
        this.denominator_ = BigInteger.ONE;
    }

    private void optimizeThis(){
        if(numerator_.equals(BigInteger.ZERO)){
            denominator_ = BigInteger.ONE;
            return;
        }
        BigInteger gcd = Evklid.GCD(numerator_,denominator_);
        denominator_ = denominator_.divide(gcd);
        numerator_ = numerator_.divide(gcd);
    }
    private Fraction optimize(){
        optimizeThis();
        return this;
    }

    public Fraction add(Fraction adding){
        if(adding.denominator_.equals(BigInteger.ZERO) || this.denominator_.equals(BigInteger.ZERO))
            return new Fraction();
        BigInteger denominator_new = Evklid.LCM(this.denominator_,adding.denominator_);
        BigInteger numerator_new = BigInteger.ZERO.add(this.numerator_.multiply(denominator_new.divide(this.denominator_)))
                .add(adding.numerator_.multiply(denominator_new.divide(adding.denominator_)));
        return new Fraction(numerator_new,denominator_new).optimize();
    }
    public Fraction substruct(Fraction subtrahend){
        subtrahend.numerator_ = BigInteger.ZERO.subtract(subtrahend.numerator_);
        return this.add(subtrahend);
    }
    public Fraction multiply(Fraction factor){
        BigInteger numerator_new = this.numerator_.multiply(factor.numerator_);
        BigInteger denominator_new = this.denominator_.multiply(factor.denominator_);
        return new Fraction(numerator_new,denominator_new).optimize();
    }
    public Fraction devide(Fraction quotient){
        Fraction factor_new = new Fraction(quotient.denominator_,quotient.numerator_);
        return this.multiply(factor_new).optimize();
    }
    public Fraction onMod(BigInteger mod){
        return Evklid.DoFractionOnMod(this,mod);
    }

    public static Fraction valueOf(BigInteger bigInteger){
        return new Fraction(bigInteger);
    }
    public String toString(){
        if(denominator_.equals(BigInteger.ONE))
            return numerator_.toString();
        else
            return numerator_.toString()+"/"+denominator_.toString();
    }
}
