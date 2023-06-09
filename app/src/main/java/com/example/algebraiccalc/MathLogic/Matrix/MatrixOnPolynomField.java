package com.example.algebraiccalc.MathLogic.Matrix;

import com.example.algebraiccalc.MathLogic.Fraction;
import com.example.algebraiccalc.MathLogic.Polynom.Polynom;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomBuilder;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomOnField;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomOnZ;

import java.math.BigInteger;
import java.util.ArrayList;

public class MatrixOnPolynomField extends Matrix{
    private ArrayList<ArrayList<Polynom>> polynom_body_= new ArrayList<>();
    private PolynomOnField mod_;

    public MatrixOnPolynomField(){};

    public MatrixOnPolynomField(ArrayList<ArrayList<Polynom>> arrayList, PolynomOnField mod, boolean onModding) {
        polynom_body_ = arrayList;
        this.mod_ = mod;
        if (onModding)
            this.polynom_body_.stream().allMatch(a -> {
                a.stream().allMatch(b -> {
                    b.mod(mod);
                    return true;
                });
                return true;
            });
    }
    @Override
    public Matrix add(Matrix matrix) {
        if(this.getCountOfColumn()!=matrix.getCountOfColumn() || this.getCountOfLine()!= matrix.getCountOfLine())
            return  new MatrixOnField();
        ArrayList<ArrayList<Polynom>> body_new = new ArrayList<>();
        for(int i=0;i<getCountOfLine();i++){
            ArrayList<Polynom> part_new = new ArrayList<>();
            for(int j=0;j<getCountOfColumn();j++){
                part_new.add(get(i,j).mod(mod_).add(matrix.get(i,j).mod(mod_)).mod(mod_));
            }
        }
        return new MatrixOnPolynomField(body_new,mod_,false);
    }
    @Override
    public Polynom get(int numLine,int numColumn){
        if(numLine < 0 || numColumn < 0 || numLine > getCountOfLine() || numColumn > getCountOfColumn())
            return PolynomOnField.ZERO_TWO;
        return polynom_body_.get(numLine).get(numColumn);
    }
    @Override
    public void set(int numLine, int numColumn, Polynom polynom){
        if(numLine < 0 || numColumn < 0 || numLine > getCountOfLine() || numColumn > getCountOfColumn())
            return;
        this.polynom_body_.get(numLine).set(numColumn, polynom);
    }
    @Override
    public void set(int numLine, int numColumn, Fraction fraction){
        if(numLine < 0 || numColumn < 0 || numLine > getCountOfLine() || numColumn > getCountOfColumn())
            return;
        this.polynom_body_.get(numLine).set(numColumn, new PolynomBuilder().addFraction(fraction).addMod(getModField()).build());
    }

    @Override
    public Matrix substruct(Matrix matrix) {
        if(this.getCountOfColumn()!=matrix.getCountOfColumn() || this.getCountOfLine()!= matrix.getCountOfLine())
            return  new MatrixOnField();
        ArrayList<ArrayList<Polynom>> body_new = new ArrayList<>();
        for(int i=0;i<getCountOfLine();i++){
            ArrayList<Polynom> part_new = new ArrayList<>();
            for(int j=0;j<getCountOfColumn();j++){
                part_new.add((Polynom) get(i,j).mod(mod_).substruct(matrix.get(i,j).mod(mod_)).mod(mod_));
            }
        }
        return new MatrixOnPolynomField(body_new,mod_,false);
    }

    @Override
    protected void addZeroColumn(){
        for(int i=0;i<getCountOfLine();i++)
            polynom_body_.get(i).add(new PolynomBuilder().addFraction(Fraction.ZERO).addMod(getModField()).build());
    }
    @Override
    protected void addZeroLine(){
        ArrayList<Polynom> adding = new ArrayList<>();
        for(int i=0;i<getCountOfColumn();i++)
            adding.add(new PolynomBuilder().addFraction(Fraction.ZERO).addMod(getModField()).build());
        polynom_body_.add(adding);
    }
    @Override
    protected void toSquare(){
        while(getCountOfLine()<getCountOfColumn())
            addZeroLine();
        while(getCountOfColumn()<getCountOfLine())
            addZeroColumn();
    }
    @Override
    protected Matrix multyplyRecursive(Matrix matrix) {
        this.toSquare();
        matrix.toSquare();

        int n = getCountOfLine();

        Matrix R = new MatrixBuilder().sizeOf(n,n,new PolynomBuilder().addFraction(Fraction.ZERO).addMod(getModField()).build()).build(MatrixBuilder.BuildClass.MATRIX_ON_POLYNOM_FIELD);

        if (n == 1)
            R.set(0,0,get(0,0).mod(mod_).multiply(matrix.get(0,0).mod(mod_)));
        else {
            Matrix A11 = this.split(0,0,n/2,n/2);
            Matrix A12 = this.split(0,n/2,n/2,n/2);
            Matrix A21 = this.split(n/2,0,n/2,n/2);
            Matrix A22 = this.split(n/2,n/2,n/2,n/2);
            Matrix B11 = matrix.split(0,0,n/2,n/2);
            Matrix B12 = matrix.split(0,n/2,n/2,n/2);
            Matrix B21 = matrix.split(n/2,0,n/2,n/2);
            Matrix B22 = matrix.split(n/2,n/2,n/2,n/2);
            Matrix M1 = A11.add(A22).multyplyRecursive(B11.add(B22));
            Matrix M2 = A21.add(A22).multyplyRecursive(B11);
            Matrix M3 = A11.multyplyRecursive(B12.substruct(B22));
            Matrix M4 = A22.multyplyRecursive(B21.substruct(B11));
            Matrix M5 = A11.add(A12).multyplyRecursive(B22);
            Matrix M6 = A21.substruct(A11).multyplyRecursive(B11.add(B12));
            Matrix M7 = A12.substruct(A22).multyplyRecursive(B21.add(B22));
            Matrix C11 = M1.add(M4).substruct(M5).add(M7);
            Matrix C12 = M3.add(M5);
            Matrix C21 = M2.add(M4);
            Matrix C22 = M1.add(M3).substruct(M2).add(M6);

            R = R.join(C11, 0, 0);
            R = R.join(C12, 0, n / 2);
            R = R.join(C21, n / 2, 0);
            R = R.join(C22, n / 2, n / 2);
        }
        return R;
    }

    @Override
    public Matrix multiply(Matrix matrix) {
        if(this.getCountOfColumn()!=matrix.getCountOfLine())
            return new MatrixOnPolynomField();

        int lineCountNew=this.getCountOfLine();
        int columnCountNew=matrix.getCountOfColumn();

        return multyplyRecursive(matrix).split(0,0,lineCountNew,columnCountNew);
    }

    @Override
    public Polynom det() {
        if(getCountOfLine()!=getCountOfColumn()){
            return PolynomOnZ.ZERO;
        }
        Polynom det= new PolynomBuilder().addFraction(Fraction.ONE).addMod(getModField()).build();
        for(int i=0;i < getCountOfLine()-1; i++){
            Polynom polynomMul = get(i+1,i).devide(get(i,i));
            det.multiply(get(i,i));
            for(int j=0; j < getCountOfColumn();j++){
                set(i+1,j,get(i+1,j).substruct(get(i,j).multiply(polynomMul)));
            }
        }
        return det;
    }

    @Override
    public Matrix split(int fromLine, int fromColumn, int lengthLine, int lengthColumn) {
        ArrayList<ArrayList<Polynom>> body_new = new ArrayList<>();
        for (int i = 0; i < lengthLine; i++) {
            ArrayList<Polynom> part_new = new ArrayList<>();
            for (int j = 0; j < lengthColumn; j++) {
                part_new.add(new PolynomBuilder().addFraction(Fraction.ZERO).addMod(getModField()).build());
            }
            body_new.add(part_new);
        }
        for (int i1 = 0, i2 = fromLine; i1 < lengthLine; i1++, i2++)
            for (int j1 = 0, j2 = fromColumn; j1 < lengthColumn; j1++, j2++)
                body_new.get(i1).set(j1, this.get(i2, j2));
        return new MatrixOnPolynomField(body_new,this.mod_,false);
    }
    public BigInteger getModField(){
        return mod_.getMod();
    }
    @Override
    public Matrix join(Matrix child, int fromLine, int fromColumn) {
        Matrix ans = this;
        for (int i1 = 0, i2 = fromLine; i1 < child.getCountOfLine(); i1++, i2++)
            for (int j1 = 0, j2 = fromColumn; j1 < child.getCountOfColumn(); j1++, j2++)
                ans.set(i2, j2, child.get( i1, j1));
        return ans;
    }
}
