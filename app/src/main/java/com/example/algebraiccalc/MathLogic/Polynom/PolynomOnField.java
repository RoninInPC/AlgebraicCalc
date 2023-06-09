package com.example.algebraiccalc.MathLogic.Polynom;

import android.util.Pair;

import com.example.algebraiccalc.MathLogic.Fraction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PolynomOnField extends Polynom{

    protected BigInteger mod_= BigInteger.ZERO;

    public static PolynomOnField ZERO_TWO = new PolynomOnField(new ArrayList<>(List.of(Fraction.ZERO)),BigInteger.valueOf(2));
    public static PolynomOnField ONE_TWO = new PolynomOnField(new ArrayList<>(List.of(Fraction.ONE)),BigInteger.valueOf(2));

    public PolynomOnField(){}
    public PolynomOnField(ArrayList<Fraction> fractions, BigInteger mod){
        this.polynom_body_ = fractions;
        this.mod_ = mod;
    }
    public PolynomOnField(BigInteger mod, ArrayList<BigInteger> arrayList){
        this.polynom_body_ = new ArrayList<>();
        arrayList.stream().allMatch(a->{
           polynom_body_.add(Fraction.valueOf(a));
           return true;
        });
        this.mod_ = mod;
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
            ans.set(i, ans.get(i).onMod(mod_).add(adding.get(i).onMod(mod_)).onMod(mod_));
        }
        return new PolynomOnField(ans, mod_).deleteExcessZero();
    }

    public BigInteger getMod(){
        return this.mod_;
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
            ans.set(i, ans.get(i).onMod(mod_).substruct(adding.get(i).onMod(mod_)).onMod(mod_));
        }
        return new PolynomOnField(ans,mod_).deleteExcessZero();
    }

    @Override
    public Polynom multiply(Polynom polynom) {
        ArrayList<Fraction> ans = new ArrayList<>(polynom_body_.size()+size());
        for(int i=0;i<polynom_body_.size()+size();i++){
            ans.add(Fraction.ZERO);
        }
        for(int i=0;i< polynom.size();i++){
            for(int j=0;j < size();j++){
                ans.set(i + j, ans.get(i + j).add(polynom.polynom_body_.get(i).onMod(mod_).
                        multiply(polynom_body_.get(j).onMod(mod_)).onMod(mod_)));
            }
        }
        return new PolynomOnField(ans,mod_).deleteExcessZero();
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

            quotient.set(polynom.size()-divider.size(), first_delimoe.onMod(mod_).devide(first_delitel.onMod(mod_)).onMod(mod_));
            polynom = dividend.substruct(divider.multiply(new PolynomOnZ(quotient)));
        }

        return new Pair(new PolynomOnField(quotient, mod_),new PolynomOnField(polynom.polynom_body_, mod_));
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
            new_polynom_body_.set(i - 1, new_polynom_body_.get(i).multiply(Fraction.valueOf(BigInteger.valueOf(i)).onMod(mod_)).onMod(mod_));
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
            new_polynom_body_.set(i + 1, new_polynom_body_.get(i).devide(Fraction.valueOf(BigInteger.valueOf(i)).onMod(mod_)).onMod(mod_));
        }
        PolynomOnZ new_polynom = new PolynomOnZ(new_polynom_body_);
        new_polynom.deleteExcessZero();
        return new_polynom;
    }

    public PolynomOnField setMod(BigInteger mod){
        this.mod_ = mod;
        return this;
    }

    public Fraction getNum(){
        if(polynom_body_.size()<=0)
            return Fraction.ZERO;
        return this.polynom_body_.get(0);
    }


}
