package com.example.algebraiccalc.MathLogic.Polynom;

import com.example.algebraiccalc.MathLogic.Fraction;

import java.math.BigInteger;
import java.util.ArrayList;

public abstract class Polynom {
    protected ArrayList<Fraction> polynom_body_;

    protected boolean derivative_ = false;
    protected Polynom deleteExcessZero(){
        for(int i = polynom_body_.size()-1; i > 0; i--){
            if(polynom_body_.get(i).equals(BigInteger.ZERO)){
                polynom_body_.remove(i);
            }else break;
        }
        return this;
    }
    public void onMul(BigInteger bigInteger){
        this.polynom_body_.stream().allMatch(a->{
            a = a.multiply(new Fraction(bigInteger,BigInteger.ONE));
            return true;
        });
    }
    public abstract Polynom add(Polynom polynom);
    public abstract Polynom substruct(Polynom polynom);
    public abstract Polynom multiply(Polynom polynom);
    public abstract Polynom devide(Polynom polynom);
    public abstract Polynom mod(Polynom polynom);
    public abstract Polynom pow(BigInteger bigInteger);

    public abstract Polynom derivative();

    public abstract Polynom underivative();


    public String toString(String nameOfVar){
        StringBuilder string = new StringBuilder();
        for(int i = polynom_body_.size()-1; i >=0; i--){
            if(i==0){
                string.append(polynom_body_.get(i).toString());
            }
            else{
                if(!polynom_body_.get(i).equals(BigInteger.ZERO))
                    string.append(String.format("%s%s^%s+", polynom_body_.get(i).toString(), nameOfVar, String.valueOf(i)));
            }
        }
        if(derivative_)
            string.append(" + C");
        return string.toString();
    }
    public Fraction get(int position){
        return polynom_body_.get(position);
    }

    public int size(){return polynom_body_.size();}
    public ArrayList<Fraction> getPolynomBody(){return polynom_body_;}
    public void setPolynomBody(ArrayList<Fraction> arrayList){this.polynom_body_ = arrayList;}
    public Polynom addBack(Fraction fraction){this.polynom_body_.add(fraction);return this;}

}
