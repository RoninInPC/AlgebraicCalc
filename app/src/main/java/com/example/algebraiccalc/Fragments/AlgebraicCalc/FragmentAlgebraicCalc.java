package com.example.algebraiccalc.Fragments.AlgebraicCalc;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.algebraiccalc.Fragments.Expression;
import com.example.algebraiccalc.MathLogic.CalcExpression;
import com.example.algebraiccalc.R;
import com.example.algebraiccalc.ToolbarCreating;

import java.math.BigInteger;

public class FragmentAlgebraicCalc extends Fragment {

    public static int height_action_bar_;
    private static Expression expression_ = new Expression();

    private static ViewContainer view_container_= null;

    private static ButtonContainer button_container_ = null;

    protected static void ToastMessage(String message, Context context){
        Toast toast = Toast.makeText(context ,message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void ParseError(Pair<BigInteger, CalcExpression.ErrorType> pair, Context context){
        switch(pair.second){
            case WRONG_STAPLES:
                ToastMessage("Wrong staples",context);
                break;
            case WRONG_EXPRESSION:
                ToastMessage("Wrong expression",context);
                break;
            case MOD_IS_ZERO:
                ToastMessage("Mod is zero",context);
                break;
            case EMPTY_MOD:
                ToastMessage("No module",context);
                break;
            case NOT_REVERSED:
                ToastMessage("Not reversed",context);
                break;
            case EMPTY_EXPRESSION:
                ToastMessage("No expression",context);
                break;
            case SUCCESS:
                view_container_.AddTextOnLinearLayout(expression_.GetModExpression());

                expression_.SetExpression(pair.first.toString());
                break;
            default:
                break;
        }
    }

    public static void GetSymbolOnClick(View view,Context context) {
        String adding = button_container_.GetSymbolOnClick(view);
        if(adding.equals("even")) {
            CalcExpression calcExpression = new CalcExpression(expression_);
            Pair<BigInteger, CalcExpression.ErrorType> answer = calcExpression.getAns();
            ParseError(answer,context);
        }

        String hint = button_container_.GetHint();

        Expression.AddLog addLog = expression_.AddingBackSymbols(adding, hint);

        if(adding.equals("×") && !addLog.equals(Expression.AddLog.NO_ADD))
            button_container_.ChangeMulToDegree();
        else
            button_container_.ChangeDegreeToMul();

        if(addLog.equals(Expression.AddLog.NO_CLEAR))
            view_container_.Clear();

        view_container_.SetTextInWritingTextView(expression_.GetModExpression());
    }

    protected static void SetExpression(Expression expression){
        expression_ = expression;
    }
    protected static Expression GetExpression(){
        return expression_;
    }

    protected static void SetButtonContainer(ButtonContainer buttonContainer){button_container_=buttonContainer;}

    protected static ButtonContainer GetButtonConainer(){return button_container_;};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_algebraic_calc, container, false);

        Menu menu = ToolbarCreating.getNavigationView().getMenu();
        ToolbarCreating.iterateMenuItemsToSetChecked(menu, menu.findItem(R.id.algebraic_calc));

        Toolbar toolbar = ToolbarCreating.getToolbar();
        menu = toolbar.getMenu();
        MenuItem item =  menu.findItem(R.id.itemR);
        item.setVisible(false);
        item = menu.findItem(R.id.itemField);
        item.setVisible(true);
        item = menu.findItem(R.id.itemPolynomField);
        item.setVisible(false);

        ToolbarCreating.iterateMenuItemsToSetNewString(menu,menu.findItem(R.id.itemField));

        button_container_ = new ButtonContainer.ButtonContainerBuilder().
                AddButton(view.findViewById(R.id.button0)).
                AddButton(view.findViewById(R.id.button1)).
                AddButton(view.findViewById(R.id.button2)).
                AddButton(view.findViewById(R.id.button3)).
                AddButton(view.findViewById(R.id.button4)).
                AddButton(view.findViewById(R.id.button5)).
                AddButton(view.findViewById(R.id.button6)).
                AddButton(view.findViewById(R.id.button7)).
                AddButton(view.findViewById(R.id.button8)).
                AddButton(view.findViewById(R.id.button9)).
                AddButton(view.findViewById(R.id.buttondel)).
                AddButton(view.findViewById(R.id.buttonmul)).
                AddButton(view.findViewById(R.id.buttonmines)).
                AddButton(view.findViewById(R.id.buttonplus)).
                AddButton(view.findViewById(R.id.buttonbackspace)).
                AddButton(view.findViewById(R.id.buttonc)).
                AddButton(view.findViewById(R.id.buttonstaples)).
                AddButton(view.findViewById(R.id.buttonreverseelement)).
                AddButton(view.findViewById(R.id.buttoneven)).
                AddButton(view.findViewById(R.id.buttonwriting)).
                Build(getActivity());

        view_container_ = new ViewContainer(view.findViewById(R.id.scrollView2),
                view.findViewById(R.id.relativelayout),
                view.findViewById(R.id.textView2),
                view.findViewById(R.id.view),
                getActivity(),
                getActivity().getApplicationContext());

        view_container_.LoadFromMemory();

        if(!view_container_.GetTextFromWritingTextView().equals("(mod )"))
            this.expression_ = Expression.MakeExpression(view_container_.GetTextFromWritingTextView());

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view_container_.SaveInMemory();
    }
    @Override
    public void onStop() {
        super.onStop();
        view_container_.SaveInMemory();
    }

}