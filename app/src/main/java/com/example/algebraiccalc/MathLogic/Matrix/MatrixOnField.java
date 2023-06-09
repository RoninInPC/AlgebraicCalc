package com.example.algebraiccalc.MathLogic.Matrix;

import com.example.algebraiccalc.MathLogic.Fraction;
import com.example.algebraiccalc.MathLogic.Polynom.Polynom;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomOnField;
import com.example.algebraiccalc.MathLogic.Polynom.PolynomOnZ;

import java.math.BigInteger;
import java.util.ArrayList;

public class MatrixOnField extends Matrix{

    BigInteger mod_ = BigInteger.ZERO;

    public MatrixOnField(){ }
    public MatrixOnField(ArrayList<ArrayList<Fraction>> arrayList,BigInteger mod,boolean onModding){
        this.matrix_body_ = arrayList;
        this.mod_ = mod;
        if(onModding)
            this.matrix_body_.stream().allMatch(a-> {
                a.stream().allMatch(b -> {
                    b.onMod(mod);
                    return true;
                });
                return true;
            });
    }
    public MatrixOnField(BigInteger mod, ArrayList<ArrayList<BigInteger>> arrayList){
        this.matrix_body_ = new ArrayList<>();
        arrayList.stream().allMatch(a->{
            ArrayList<Fraction> part = new ArrayList<>();
            a.stream().allMatch(b->{
                part.add(Fraction.valueOf(b).onMod(mod));
                return true;
            });
            this.matrix_body_.add(part);
            return true;
        });
        this.mod_ = mod;
    }

    @Override
    public Matrix add(Matrix matrix) {
        if(this.getCountOfColumn()!=matrix.getCountOfColumn() || this.getCountOfLine()!= matrix.getCountOfLine())
            return  new MatrixOnField();
        ArrayList<ArrayList<Fraction>> body_new = new ArrayList<>();
        for(int i=0;i<getCountOfLine();i++){
            ArrayList<Fraction> part_new = new ArrayList<>();
            for(int j=0;j<getCountOfColumn();j++){
                part_new.add(this.get(i,j).get(0).onMod(mod_).add(matrix.get(i,j).get(0).onMod(mod_)).onMod(mod_));
            }
        }
        return new MatrixOnField(body_new,mod_,false);
    }

    @Override
    public Matrix substruct(Matrix matrix) {
        if(this.getCountOfColumn()!=matrix.getCountOfColumn() || this.getCountOfLine()!= matrix.getCountOfLine())
            return  new MatrixOnField();
        ArrayList<ArrayList<Fraction>> body_new = new ArrayList<>();
        for(int i=0;i<getCountOfLine();i++){
            ArrayList<Fraction> part_new = new ArrayList<>();
            for(int j=0;j<getCountOfColumn();j++){
                part_new.add(this.get(i,j).get(0).onMod(mod_).substruct(matrix.get(i,j).get(0).onMod(mod_)).onMod(mod_));
            }
        }
        return new MatrixOnField(body_new,mod_,false);
    }

    @Override
    protected Matrix multyplyRecursive(Matrix matrix){
        this.toSquare();
        matrix.toSquare();

        int n = getCountOfLine();

        Matrix R = MatrixOnField.sizeOf(n,n).addMod(mod_);

        if (n == 1)
            R.set(0,0,get(0,0).get(0).onMod(mod_).multiply(matrix.get(0,0).get(0).onMod(mod_)));
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
            return new MatrixOnZ();

        int lineCountNew=this.getCountOfLine();
        int columnCountNew=matrix.getCountOfColumn();

        return multyplyRecursive(matrix).split(0,0,lineCountNew,columnCountNew);
    }

    @Override
    public Polynom det() {
        if(getCountOfLine()!=getCountOfColumn()){
            return PolynomOnZ.ZERO;
        }
        Fraction det= Fraction.ONE;
        for(int i=0;i < getCountOfLine()-1; i++){
            Fraction fractionMul = get(i+1,i).get(0).devide(get(i,i).get(0));
            det.multiply(get(i,i).get(0));
            for(int j=0; j < getCountOfColumn();j++){
                set(i+1,j,get(i+1,j).get(0).substruct(get(i,j).get(0).multiply(fractionMul)));
            }
        }
        return new PolynomOnField().setMod(mod_).addBack(det);
    }

    @Override
    public Matrix split(int fromLine, int fromColumn, int lengthLine, int lengthColumn) {
        ArrayList<ArrayList<Fraction>> body_new = new ArrayList<>();
        for (int i = 0; i < lengthLine; i++) {
            ArrayList<Fraction> part_new = new ArrayList<>();
            for (int j = 0; j < lengthColumn; j++) {
                part_new.add(Fraction.ZERO);
            }
            body_new.add(part_new);
        }
        for (int i1 = 0, i2 = fromLine; i1 < lengthLine; i1++, i2++)
            for (int j1 = 0, j2 = fromColumn; j1 < lengthColumn; j1++, j2++)
                body_new.get(i1).set(j1, this.get(i2, j2).get(0));
        return new MatrixOnField(body_new,this.mod_,false);
    }
    @Override
    public Matrix join(Matrix child, int fromLine, int fromColumn) {
        Matrix ans = this;
        for (int i1 = 0, i2 = fromLine; i1 < child.getCountOfLine(); i1++, i2++)
            for (int j1 = 0, j2 = fromColumn; j1 < child.getCountOfColumn(); j1++, j2++)
                ans.set(i2, j2, child.get( i1, j1));
        return ans;
    }

    public static MatrixOnField sizeOf(int countOfLine,int countOfColumn){
        ArrayList<ArrayList<Fraction>> ans=new ArrayList<>();
        for(int i=0;i<countOfLine;i++){
            ArrayList<Fraction> part = new ArrayList<>();
            for(int j=0;j<countOfColumn;j++){
                part.add(Fraction.ZERO);
            }
            ans.add(part);
        }
        return new MatrixOnField(ans,BigInteger.ZERO,false);
    }
    public MatrixOnField addMod(BigInteger mod){
        this.mod_ = mod;
        return this;
    }
}
