package com.example.algebraiccalc.MathLogic.Matrix;

import com.example.algebraiccalc.MathLogic.Fraction;
import com.example.algebraiccalc.MathLogic.Polynom.Polynom;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomBuilder;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomOnField;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomOnZ;

import java.util.ArrayList;

public abstract class Matrix {
    protected ArrayList<ArrayList<Fraction>> matrix_body_ = new ArrayList<>();
    public abstract Matrix add(Matrix matrix);
    public abstract Matrix substruct(Matrix matrix);

    protected void addZeroColumn(){
        for(int i=0;i<getCountOfLine();i++)
            matrix_body_.get(i).add(Fraction.ZERO);
    }
    protected void addZeroLine(){
        ArrayList<Fraction> adding = new ArrayList<>();
        for(int i=0;i<getCountOfColumn();i++)
            adding.add(Fraction.ZERO);
        matrix_body_.add(adding);
    }
    protected void toSquare(){
        while(getCountOfLine()<getCountOfColumn())
            addZeroLine();
        while(getCountOfColumn()<getCountOfLine())
            addZeroColumn();
    }
    protected abstract Matrix multyplyRecursive(Matrix matrix);
    public abstract Matrix multiply(Matrix matrix);
    public abstract Polynom det();
    public Integer getCountOfLine(){
        return matrix_body_.size();
    }
    public Integer getCountOfColumn(){
        if(getCountOfLine() == 0)
            return 0;
        return matrix_body_.get(0).size();
    }

    private void transposeThis(){
        for(int i=0;i<getCountOfLine();i++){
            for(int j=0;j<getCountOfColumn();j++){
                if(i!=j){
                    Polynom polynom= get(i,j);
                    set(i,j,get(j,i));
                    set(j,i,polynom);
                }
            }
        }
    }

    public Matrix transpose(){
        transposeThis();
        return this;
    }

    public abstract Matrix split(int fromLine, int fromColumn, int lengthLine,int lengthColumn);
    public abstract Matrix join(Matrix child,int fromLine, int fromColumn);
    public Polynom get(int numLine,int numColumn){
        if(numLine < 0 || numColumn < 0 || numLine > getCountOfLine() || numColumn > getCountOfColumn())
            return new PolynomBuilder().addFraction(Fraction.ZERO).build();
        return new PolynomBuilder().addFraction(this.matrix_body_.get(numLine).get(numColumn)).build();
    }
    public void set(int numLine, int numColumn, Polynom polynom){
        if(numLine < 0 || numColumn < 0 || numLine > getCountOfLine() || numColumn > getCountOfColumn())
            return;
        this.matrix_body_.get(numLine).set(numColumn, polynom.get(0));
    }
    public void set(int numLine, int numColumn, Fraction fraction){
        if(numLine < 0 || numColumn < 0 || numLine > getCountOfLine() || numColumn > getCountOfColumn())
            return;
        this.matrix_body_.get(numLine).set(numColumn, fraction);
    }

    public String toString(){
        StringBuilder ans= new StringBuilder();
        for(int i=0;i < getCountOfLine();i++){
            for(int j=0;j<getCountOfColumn();j++){
                ans.append(matrix_body_.get(i).get(j).toString()).append(" ");
            }
            ans.append("\n");
        }
        return ans.toString();
    }

}
