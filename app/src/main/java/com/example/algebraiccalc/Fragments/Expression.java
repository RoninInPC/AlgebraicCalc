package com.example.algebraiccalc.Fragments;

import java.util.regex.Pattern;

public class Expression {
    private String expression_ = "";
    private int count_of_staples_ = 0;
    private String mod_ = "";
    public enum AddLog{ADD_NUMBER,ADD_OPERATION,ADD_STAPLE,DELETE,REVERSE,CLEAR,NO_ADD,NO_CLEAR};

    private enum ClearLog{CLEAR_EXPRESSION,CLEAR_MOD};

    private AddLog NeedClear(ClearLog clearLog){
        if(expression_.equals("") && mod_.equals(""))
            return AddLog.NO_CLEAR;
        if(clearLog.equals(ClearLog.CLEAR_EXPRESSION)){
            if(expression_.equals("")){
                return NeedClear(ClearLog.CLEAR_MOD);
            }else{
                count_of_staples_ = 0;
                expression_="";
                return AddLog.CLEAR;
            }
        }else{
            if(mod_.equals("")){
                return NeedClear(ClearLog.CLEAR_EXPRESSION);
            }else{
                mod_="";
                return AddLog.CLEAR;
            }
        }
    }

    private AddLog AddingInExpressionBackSymbol(String adding){
        String back = ExpressionFunctions.GetBack(expression_);
        if(Pattern.matches("[0123456789]",adding)) {
            String c = expression_.length()>=3? String.valueOf(expression_.charAt(expression_.length()-3)):"n";
            if((c.equals("^") && !Pattern.matches("[\\×\\/\\-\\^\\+]",back)) || back.equals(")")){
                expression_ +="×";
            }
            Character character = !expression_.isEmpty()?expression_.charAt(0):' ';

            if(character.equals('0') && back.equals("0"))
                expression_ = ExpressionFunctions.PopBack(expression_);
            if(expression_.length()>2){
                character = expression_.charAt(expression_.length()-2);
                if(Pattern.matches("[\\×\\/\\-\\^\\+]",String.valueOf(character)) && back.equals("0"))
                    expression_= ExpressionFunctions.PopBack(expression_);
            }
            expression_ += adding;
            return AddLog.ADD_NUMBER;
        }
        if(Pattern.matches("[\\×\\/\\-\\^\\+]",adding) || adding.equals("Xn")){
            if(back.equals("×") && adding.equals("Xn")){
                expression_ = ExpressionFunctions.PopBack(expression_);
                expression_ +="^";
                return AddLog.ADD_OPERATION;
            }
            if(Pattern.matches("[0123456789]",back) || back.equals(")") || (back.equals("(") && adding.equals("-"))){
                expression_ +=adding;
                return AddLog.ADD_OPERATION;
            }
        }
        if(adding.equals("delete")){
            if(back.equals("("))
                count_of_staples_--;
            if(back.equals(")"))
                count_of_staples_++;
            expression_ = ExpressionFunctions.PopBack(expression_);
            return AddLog.DELETE;
        }
        if(adding.equals("staples")){
            if((Pattern.matches("[0123456789]",back) || back.equals(")")) && count_of_staples_ > 0){
                expression_ +=")";
                count_of_staples_--;
                return AddLog.ADD_STAPLE;
            }else
            if(Pattern.matches("[0123456789]",back) || back.equals("(") ||
                    Pattern.matches("[\\×\\/\\-\\^\\+]",back) || count_of_staples_==0){
                if(Pattern.matches("[0123456789]",back) || back.equals(")"))
                    expression_+="×";
                expression_ +="(";
                count_of_staples_++;
                return AddLog.ADD_STAPLE;
            }
        }
        if(adding.equals("reverse")){
            if(Pattern.matches("[0123456789]",back) || back.equals(")")) {
                expression_ += "^(-1)";
                return AddLog.REVERSE;
            }
        }
        if(adding.equals("clear")){
            return NeedClear(ClearLog.CLEAR_EXPRESSION);
        }
        return AddLog.NO_ADD;
    }

    private AddLog AddingInModBackSymbol(String adding){

        if(Pattern.matches("[0123456789]",adding)){

            Character character = !mod_.isEmpty()?mod_.charAt(0):' ';
            if(character.equals('0'))
                mod_ = ExpressionFunctions.PopBack(mod_);

            mod_ += adding;
            return AddLog.ADD_NUMBER;
        }
        if(adding.equals("clear")){
            return NeedClear(ClearLog.CLEAR_MOD);
        }
        if(adding.equals("delete")){
            mod_ = ExpressionFunctions.PopBack(mod_);
            return AddLog.DELETE;
        }
        return AddLog.NO_ADD;
    }
    public AddLog AddingBackSymbols(String adding, String hint){
        if(hint.equals("writing expr")){
            return AddingInExpressionBackSymbol(adding);
        }
        else{
            return AddingInModBackSymbol(adding);
        }
    }
    public String GetExpression(){
        return expression_;
    }
    public void SetExpression(String expression){
        this.expression_ = expression;
        count_of_staples_ = ExpressionFunctions.GetCountOfStaples(expression);
    }
    public String GetMod(){
        return mod_;
    }
    public void SetMod(String mod){
        this.mod_ = mod;
    }
    public String GetModExpression(){
        return String.format("%s (mod %s)", expression_, mod_);
    }

    public int GetCountOfStaples() {
        return count_of_staples_;
    }

    public void SetCountOfStaples(int count_of_staples_) {
        this.count_of_staples_ = count_of_staples_;
    }
    public static Expression MakeExpression(String fromTextView){
        Expression expr = new Expression();
        if(fromTextView==null)return expr;
        if(fromTextView.length()==0)return expr;
        String splitting[] =  fromTextView.split("m");
        expr.SetExpression(splitting[0].substring(0,splitting[0].length()-2));
        expr.SetMod(splitting[1].replaceAll("[mod() ]",""));
        return expr;

    }

}
