package com.example.algebraiccalc.Fragments.AlgebraicCalc;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

import com.example.algebraiccalc.R;

import java.util.HashMap;
import java.util.Map;

public class ButtonContainer{
    protected Map<Integer,AppCompatButton> buttonHashMap = new HashMap<>();

    public void ChangeMulToDegree(int rid){
        AppCompatButton button = buttonHashMap.get(rid);
        String text = (String) button.getText().toString();
        if(text.equals("×")){
            SpannableString string = new SpannableString("Xn");
            string.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            string.setSpan(new RelativeSizeSpan(0.5f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //Html.fromHtml("X<sup><small>n<small></sup>")
            button.setText(string);
        }
    }
    public void ChangeMulToDegree(){
        ChangeMulToDegree(R.id.buttonmul);
    }

    public void ChangeDegreeToMul(int rid){
        AppCompatButton button = buttonHashMap.get(rid);
        String text = button.getText().toString();
        if(text.equals("Xn")){
            button.setText("×");
        }
    }
    public void ChangeDegreeToMul(){
        ChangeDegreeToMul(R.id.buttonmul);
    }

    public String GetHint(){
        AppCompatButton button = buttonHashMap.get(R.id.buttonwriting);
        return button.getHint().toString();
    }

    public String GetSymbolOnClick(View view){
        String adding;
        switch(view.getId()){
            case R.id.button0:
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
            case R.id.buttondel:
            case R.id.buttonmul:
            case R.id.buttonmines:
            case R.id.buttonplus:
                Button b = (Button)view;
                adding = b.getText().toString();
                break;
            case R.id.buttonbackspace:
                adding = "delete";
                break;
            case R.id.buttonc:
                adding = "clear";
                break;
            case R.id.buttonstaples:
                adding = "staples";
                break;
            case R.id.buttonreverseelement:
                adding = "reverse";
                break;
            case R.id.buttoneven:
                adding = "even";
                break;
            case R.id.buttonwriting:
                adding = "buttonwriting";
                Button button = (Button)view;
                String str = button.getHint().toString();
                if(str.equals("writing expr"))
                    button.setHint("writing mod");
                else
                    button.setHint("writing expr");
                break;
            default:
                adding = "default";
                break;
        }
        return adding;
    }
    static class ButtonContainerBuilder{
        private static ButtonContainer container = new ButtonContainer();

        public ButtonContainerBuilder AddButton(AppCompatButton appCompatButton){
            container.buttonHashMap.put(appCompatButton.getId(),appCompatButton);
            return this;
        }
        public ButtonContainer Build(Context context){
            float weight = Resources.getSystem().getDisplayMetrics().widthPixels;
            float height = Resources.getSystem().getDisplayMetrics().heightPixels;
            float size;
            if(weight - height * 0.6 < 0)
                size = weight;
            else
                size = (float) (height * 0.6);

            container.buttonHashMap.forEach((integer, appCompatButton) -> {
                ViewGroup.LayoutParams layoutParams1 = appCompatButton.getLayoutParams();

                layoutParams1.height = (int) (size / 6);
                layoutParams1.width = (int) (size / 6);
                appCompatButton.setLayoutParams(layoutParams1);
                //appCompatButton.getLayoutParams().width;
                if(integer.equals(R.id.buttonwriting))
                    appCompatButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            layoutParams1.width / 4);
                else
                    appCompatButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            layoutParams1.width / 2);
                appCompatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentAlgebraicCalc.GetSymbolOnClick(view,context);
                    }
                });
            });

            AppCompatButton button = container.buttonHashMap.get(R.id.buttonreverseelement);
            SpannableString string = new SpannableString("X-1");
            string.setSpan(new SuperscriptSpan(), 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            string.setSpan(new RelativeSizeSpan(0.5f), 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            button.setText(string);
            return this.container;
        }
    }
}
