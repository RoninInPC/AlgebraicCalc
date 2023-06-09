package com.example.algebraiccalc.MathLogic;

import android.util.Pair;

import com.example.algebraiccalc.Fragments.Expression;
import com.example.algebraiccalc.Fragments.ExpressionFunctions;

import org.antlr.v4.runtime.misc.Triple;
import org.apache.commons.lang3.math.NumberUtils;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import kotlin.jvm.functions.Function2;

public class CalcExpression {
    private String expression_ = "";
    private String mod_ = "";
    private int count_of_staples_ = 0;
    private boolean lost_reverse_ = false;

    private boolean mod_is_zero_ = false;
    private boolean is_no_parsable = false;
    public enum ErrorType {NOT_REVERSED,EMPTY_MOD,EMPTY_EXPRESSION,SUCCESS,WRONG_EXPRESSION,MOD_IS_ZERO,WRONG_STAPLES};
    private static class Term{
        public String name;
        public enum Type{CONST,OPEN_STAPLES,CLOSE_STAPLES,OPERATION,DEFAULT};
        public Type type;
        public Term(String name,Type type){
            this.name =name;
            this.type =type;};
        public Term(String name){
            this.name = name;
            type = Type.DEFAULT;

            if(NumberUtils.isParsable(this.name))
                type = Type.CONST;
            if(this.name.equals("("))
                type = Type.OPEN_STAPLES;
            if(this.name.equals(")"))
                type = Type.CLOSE_STAPLES;
            if(Pattern.matches("[\\×\\/\\-\\^\\+]", this.name))
                type = Type.OPERATION;
        }
    }

    Map<String,Integer> priority_map_ = Map.of("+",10,"-",10,"×",20,
            "/",20,"^",30,"unari_minus",10, "reverse_minus",10);

    Map<String, Function2<Stack<BigInteger>,BigInteger,BigInteger>> function_map_ =
            Map.of("+",(a, c)->a.push(a.pop().mod(c).add(a.pop().mod(c))),
                    "-",(a, c)->a.push(BigInteger.ZERO.subtract(a.pop().mod(c)).add(a.pop().mod(c))),
                    "×",(a, c)->a.push(a.pop().mod(c).multiply(a.pop().mod(c))),
                    "/",(a, c)->a.push(ReverseElement(a.pop(),c).multiply(a.pop().mod(c))),
                    "^", (a,c)->a.push(Pow(a.pop(),a.pop(),c)),
                    "unari_minus",(a,c)->a.push(a.pop().mod(c).subtract(c).abs()),
                    "reverse_minus",(a,c)->a.push(BigInteger.ZERO.subtract(a.pop().mod(c)))
                    );
    Map<String,Integer> count_of_arguments =
            Map.of("+",2,
                    "-",2,
                    "×",2,
                    "/",2,
                    "^",2,
                    "unari_minus",1,
                    "reverse_minus",1);
    private  BigInteger ReverseElement(BigInteger element, BigInteger mod){
        Triple<BigInteger,BigInteger,BigInteger> triple = Evklid.AdvancedEvklid(element,mod);
        if(triple.b.equals(BigInteger.ZERO))
            lost_reverse_ = true;
        return triple.b;
    }
    private BigInteger Pow(BigInteger element1, BigInteger element2,BigInteger mod){
        if(element1.toString().contains("-")){
            return ReverseElement(element2,mod).modPow(element1.abs(),mod);
        }
        return element2.modPow(element1,mod);
    }

    private boolean IsNoParsable(Stack<BigInteger> stacking, String operation){
        boolean bool = stacking.size()<count_of_arguments.get(operation);
        is_no_parsable = bool;
        return bool;
    }
    public CalcExpression(Expression expression){
        this.expression_ = expression.GetExpression();
        this.mod_ = expression.GetMod();
        this.count_of_staples_ = expression.GetCountOfStaples();
    }
    public CalcExpression(String expression, String mod){
        this.expression_ = expression;
        this.count_of_staples_ = ExpressionFunctions.GetCountOfStaples(expression);
        this.mod_ = mod;
    }
    public Pair<BigInteger, ErrorType> getAns(){

        if(expression_.equals("")){
            return new Pair(BigInteger.ZERO, ErrorType.EMPTY_EXPRESSION);
        }

        if(mod_.equals("")){
            return  new Pair(BigInteger.ZERO, ErrorType.EMPTY_MOD);
        }
        if(count_of_staples_!=0){
            return new Pair(BigInteger.ZERO,ErrorType.WRONG_STAPLES);
        }
        BigInteger ans = GetValue(MakePostfixTerms(GetTerms()));
        ErrorType ansError = ErrorType.SUCCESS;
        if(is_no_parsable)
           ansError = ErrorType.WRONG_EXPRESSION;
        if(lost_reverse_)
            ansError = ErrorType.NOT_REVERSED;
        if(mod_is_zero_)
            ansError = ErrorType.MOD_IS_ZERO;

        return new Pair(ans, ansError);
    }
    protected ArrayList<Term> GetTerms(){
        ArrayList<Term> array_terms = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(expression_,"×/-^+()",true);
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (!token.isEmpty()) {
                if(token.equals("-")){
                    String power = array_terms.size() >= 2? array_terms.get(array_terms.size()-2).name:"";

                    if(power.equals("^")){
                        array_terms.add(new Term("reverse_minus", Term.Type.OPERATION));
                        continue;
                    }

                    Term.Type type = array_terms.isEmpty()? Term.Type.DEFAULT: array_terms.get(array_terms.size()-1).type;

                    if(type== Term.Type.OPERATION || type==Term.Type.OPEN_STAPLES || type== Term.Type.DEFAULT){
                        array_terms.add(new Term("unari_minus", Term.Type.OPERATION));
                        continue;
                    }
                }
                array_terms.add(new Term(token));
            }
        }
        return array_terms;
    }
    protected ArrayList<Term> MakePostfixTerms(ArrayList<Term> array_terms){
        ArrayList<Term> postfix_terms = new ArrayList<>();
        Stack<Term> stacking = new Stack<>();
        array_terms.stream().allMatch(s-> {
            switch (s.type) {
                case CONST:
                    postfix_terms.add(s);
                    break;
                case OPERATION:
                    while(stacking.size() > 0 && ((stacking.peek().type == Term.Type.OPERATION) &&
                            priority_map_.get(stacking.peek().name) >= priority_map_.get(s.name))){
                        postfix_terms.add(stacking.pop());
                    }
                    stacking.push(s);
                    break;
                case CLOSE_STAPLES:
                    while(stacking.peek().type != Term.Type.OPEN_STAPLES) {
                        postfix_terms.add(stacking.pop());
                    }
                    stacking.pop();
                    break;
                case OPEN_STAPLES:
                    stacking.push(s);
                    break;
                case DEFAULT:
                default:
                    break;
            }
            return true;
        });
        while (stacking.size() > 0){
            postfix_terms.add(stacking.pop());
        }
        return postfix_terms;
    }
    protected BigInteger GetValue(ArrayList<Term> postfix_terms){
        Stack<BigInteger> stacking = new Stack<>();
        BigInteger module = NumberUtils.isParsable(mod_)?new BigInteger(mod_):new BigInteger("10");
        if(module.equals(BigInteger.ZERO))
            mod_is_zero_ = true;

        postfix_terms.stream().allMatch( s->{
            switch(s.type){
                case CONST:
                    if(NumberUtils.isParsable(s.name)){
                        stacking.push(new BigInteger(s.name));
                    }
                    break;
                case OPERATION:
                    if(lost_reverse_ || IsNoParsable(stacking,s.name) || mod_is_zero_)
                        return false;
                    function_map_.get(s.name).invoke(stacking,module);
                    break;
            }
            return true;
        });
        if(is_no_parsable || mod_is_zero_ || lost_reverse_)
            return BigInteger.ZERO;
        return stacking.peek().mod(module);
    }
}
