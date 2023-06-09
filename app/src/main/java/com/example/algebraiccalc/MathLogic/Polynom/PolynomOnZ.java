package com.example.algebraiccalc.MathLogic.Polynom;

import android.graphics.Matrix;
import android.util.Pair;

import com.example.algebraiccalc.MathLogic.Fraction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PolynomOnZ extends Polynom{

    public static PolynomOnZ ZERO = new PolynomOnZ(new ArrayList<>(List.of(Fraction.ZERO)));
    public static PolynomOnZ ONE = new PolynomOnZ(new ArrayList<>(List.of(Fraction.ONE)));
    public PolynomOnZ(){}
    public PolynomOnZ(PolynomOnZ polynom){
        this.polynom_body_ = polynom.polynom_body_;
    }
    public PolynomOnZ(ArrayList<Fraction> arrayList){
        this.polynom_body_ = arrayList;
    }

    @Override
    public Polynom add(Polynom polynom) {
        ArrayList<Fraction> ans;
        ArrayList<Fraction> adding;
        if(size() <= polynom.size()){
            ans = polynom.polynom_body_;
            adding = polynom_body_;
        }else{
            ans = polynom_body_;
            adding = polynom.polynom_body_;
        }
        for(int i = 0;i < adding.size();i++){
            ans.set(i, ans.get(i).add(adding.get(i)));
        }
        return new PolynomOnZ(ans).deleteExcessZero();
    }

    @Override
    public Polynom substruct(Polynom polynom) {
        ArrayList<Fraction> ans;
        ArrayList<Fraction> adding;
        if(size() <= polynom.size()){
            ans = polynom.polynom_body_;
            adding = polynom_body_;
        }else{
            ans = polynom_body_;
            adding = polynom.polynom_body_;
        }
        for(int i = 0;i < adding.size();i++){
            ans.set(i, ans.get(i).substruct(adding.get(i)));
        }
        return new PolynomOnZ(ans).deleteExcessZero();
    }

    @Override
    public Polynom multiply(Polynom polynom) {
        ArrayList<Fraction> ans = new ArrayList<>(polynom_body_.size()+size());
        for(int i=0;i<polynom_body_.size()+size();i++){
            ans.add(Fraction.ZERO);
        }
        for(int i=0;i< polynom.size();i++){
            for(int j=0;j < size();j++){
                ans.set(i + j, ans.get(i + j).add(polynom.polynom_body_.get(i).multiply(polynom_body_.get(j))));
            }
        }
        return new PolynomOnZ(ans).deleteExcessZero();
    }


    private Pair<Polynom,Polynom> delmod(Polynom dividend, Polynom divider){
         Polynom polynom = new PolynomOnZ(dividend.polynom_body_);
         ArrayList<Fraction> quotient = new ArrayList<>();
         for(int i=0;i<dividend.size()-divider.size()+1;i++){
             quotient.add(Fraction.ZERO);
         }
         while(polynom.size()>=divider.size()) {
             Fraction first_delimoe = polynom.polynom_body_.get(polynom.size() - 1);
             Fraction first_delitel = divider.polynom_body_.get(divider.size() - 1);

             quotient.set(polynom.size()-divider.size(), first_delimoe.devide(first_delitel));
             polynom = dividend.substruct(divider.multiply(new PolynomOnZ(quotient)));
         }

         return new Pair(new PolynomOnZ(quotient),new PolynomOnZ(polynom.polynom_body_));
    }

    @Override
    public Polynom devide(Polynom polynom) {
        return delmod(this,polynom).first;
    }

    @Override
    public Polynom mod(Polynom polynom) {
        return delmod(this,polynom).second;
    }

    @Override
    public Polynom pow(BigInteger bigInteger) {
        Polynom one = this;
        for(BigInteger bigInteger1=BigInteger.ONE;!bigInteger1.equals(bigInteger);bigInteger1 = bigInteger1.add(BigInteger.ONE))
            one = one.multiply(this);
        return one;
    }

    @Override
    public Polynom derivative() {
        ArrayList<Fraction> new_polynom_body_ = polynom_body_;
        for(int i=1;i<new_polynom_body_.size();i++){
            new_polynom_body_.set(i - 1, new_polynom_body_.get(i).multiply(Fraction.valueOf(BigInteger.valueOf(i))));
        }
        PolynomOnZ new_polynom = new PolynomOnZ(new_polynom_body_);
        new_polynom.deleteExcessZero();
        return new_polynom;
    }

    @Override
    public Polynom underivative() {
        derivative_ = true;
        ArrayList<Fraction> new_polynom_body_ = polynom_body_;
        new_polynom_body_.add(Fraction.ZERO);
        for(int i=new_polynom_body_.size()-2;i>=0;i--){
            new_polynom_body_.set(i + 1, new_polynom_body_.get(i).devide(Fraction.valueOf(BigInteger.valueOf(i))));
        }
        PolynomOnZ new_polynom = new PolynomOnZ(new_polynom_body_);
        new_polynom.deleteExcessZero();
        return new_polynom;
    }

}
