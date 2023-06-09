package com.example.algebraiccalc.Fragments;

public class ExpressionFunctions {
    public static int GetCountOfStaples(String expression){
        int count = 0;
        for(int i = 0;i < expression.length(); i++){
            Character character = expression.charAt(i);
            if(character.equals('('))
                count++;
            if(character.equals(')'))
                count--;
        }
        return count;
    }
    public static String PopBack(String string){
        if(string.length()>0)
            return string=string.substring(0,string.length()-1);
        else
            return "";
    }
    public static String GetBack(String string){
        if(string.length()==0)
            return "";
        return String.valueOf(string.charAt(string.length()-1));
    }
}
