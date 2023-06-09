package com.example.algebraiccalc.MathLogic.Matrix;

import com.example.algebraiccalc.MathLogic.Fraction;
import com.example.algebraiccalc.MathLogic.Polynom.Polynom;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomOnField;

import java.math.BigInteger;
import java.util.ArrayList;

public class MatrixBuilder {

    public enum BuildClass{MATRIX_ON_Z,MATRIX_ON_FIELD,MATRIX_ON_POLYNOM_FIELD};

    private BigInteger mod_biginteger_ = BigInteger.ONE;
    private PolynomOnField mod_polynom_ = PolynomOnField.ONE_TWO;
    private ArrayList<ArrayList<Fraction>> body_fraction_ = new ArrayList<>();
    private ArrayList<ArrayList<Polynom>> body_polynom_ = new ArrayList<>();

    public MatrixBuilder(){};

    public MatrixBuilder addMod(BigInteger mod){
        this.mod_biginteger_= mod;
        return this;
    }
    public MatrixBuilder addMod(PolynomOnField mod){
        this.mod_polynom_ = mod;
        return this;
    }
    public MatrixBuilder addFractionBody(ArrayList<ArrayList<Fraction>> body){
        this.body_fraction_ = body;
        return this;
    }
    public MatrixBuilder addPolynomBody(ArrayList<ArrayList<Polynom>> body){
        this.body_polynom_ = body;
        return this;
    }
    public MatrixBuilder sizeOf(int countOfLine, int countOfColumn, Fraction mainFraction){
        body_fraction_ = new ArrayList<>();
        for(int i=0;i<countOfLine;i++){
            ArrayList<Fraction> part = new ArrayList<>();
            for(int j=0;j<countOfColumn;j++){
                part.add(mainFraction);
            }
            body_fraction_.add(part);
        }
        return this;
    }
    public MatrixBuilder sizeOf(int countOfLine,int countOfColumn,Polynom mainPolynom){
        body_polynom_ = new ArrayList<>();
        for(int i=0;i<countOfLine;i++){
            ArrayList<Polynom> part = new ArrayList<>();
            for(int j=0;j<countOfColumn;j++){
                part.add(mainPolynom);
            }
            body_polynom_.add(part);
        }
        return this;
    }
    public Matrix build(BuildClass buildClass){
        Matrix buildMatrix;
        switch (buildClass){
            case MATRIX_ON_FIELD:
                buildMatrix = new MatrixOnField(body_fraction_,mod_biginteger_,true);
                break;
            case MATRIX_ON_POLYNOM_FIELD:
                buildMatrix = new MatrixOnPolynomField(body_polynom_,mod_polynom_,true);
                break;
            case MATRIX_ON_Z:
            default:
                buildMatrix = new MatrixOnZ(body_fraction_);
                break;

        }
        return buildMatrix;
    }
}
