package com.example.algebraiccalc.Fragments.PolynomCalc;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

import com.example.algebraiccalc.R;
import com.example.algebraiccalc.Fragments.AlgebraicCalc.ButtonContainer;

public class ButtonContainerPolynom extends ButtonContainer {
    protected AppCompatButton button_add_polynom = null;

    @Override
    public void ChangeMulToDegree(){
        ChangeMulToDegree(R.id.button_mul_polynom);
    }
    @Override
    public void ChangeDegreeToMul(){
        ChangeDegreeToMul(R.id.button_mul_polynom);
    }

    @Override
    public String GetHint(){
        AppCompatButton button = buttonHashMap.get(R.id.button_writing_polynom);
        return button.getHint().toString();
    }

    @Override
    public String GetSymbolOnClick(View view) {
        String adding;
        switch(view.getId()) {
            case R.id.button_mul_polynom:
            case R.id.button_devide_polynom:
            case R.id.button_plus_polynom:
            case  R.id.button_mines_polynom:
                Button b = (Button)view;
                adding = b.getText().toString();
                break;
            case R.id.button_derivative_polynom:
                adding = "derivative";
                break;
            case R.id.button_underivative_polynom:
                adding = "underivative";
                break;
            case R.id.button_reverse_polynom:
                adding = "reverse";
                break;
            case R.id.button_writing_polynom:
                adding = "button_writing";
                Button button = (Button)view;
                String str = button.getHint().toString();
                if(str.equals("writing expr"))
                    button.setHint("writing mod");
                else
                    button.setHint("writing expr");
                break;
            case R.id.button_ans_polynom:
                adding="even";
                break;
            case R.id.button_back_polynom:
                adding="delete";
                break;
            case R.id.button_num_polynom:
                adding ="num";
                break;
            case R.id.button_clear_polynom:
                adding = "clear";
                break;
            case R.id.button_staples_polynom:
                adding = "staples";
                break;
            case R.id.button_add_polynom:
                adding = "add_polynom";
                break;
            default:
                adding= "default";
                break;
        }
        return adding;
    }
}
