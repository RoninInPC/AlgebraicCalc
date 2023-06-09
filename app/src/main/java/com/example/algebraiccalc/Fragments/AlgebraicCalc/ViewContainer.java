package com.example.algebraiccalc.Fragments.AlgebraicCalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.example.algebraiccalc.Fragments.Expression;
import com.example.algebraiccalc.Interfaces.MemoryManipulation;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ViewContainer implements MemoryManipulation {
    protected ArrayList<String> list_string_ = new ArrayList();
    protected NestedScrollView scroll_view_;

    protected LinearLayout linear_layout_;
    protected TextView writing_text_view_;
    protected ConstraintLayout constraint_layout_;

    private int textsize_addtext_;
    private Context context_;
    private SharedPreferences shared_preferences_;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ViewContainer(NestedScrollView scrollView, LinearLayout linearLayout,
                         TextView writingTextView, ConstraintLayout constraintLayout, Context context, Context applicationContext){
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        this.constraint_layout_ = constraintLayout;

        this.scroll_view_ = scrollView;
        this.scroll_view_.setVerticalScrollBarEnabled(false);

        this.linear_layout_ = linearLayout;

        this.writing_text_view_ = writingTextView;
        ViewGroup.LayoutParams layoutParams1 = this.writing_text_view_.getLayoutParams();
        layoutParams1.height = (int)(height*0.35*0.3);
        layoutParams1.width= width;
        textsize_addtext_ = layoutParams1.height/10;
        //this.writing_text_view_.setAutoSizeTextTypeUniformWithConfiguration(layoutParams1.height/10,
                //layoutParams1.height/2,1,TypedValue.COMPLEX_UNIT_DIP);
        this.writing_text_view_.setLayoutParams(layoutParams1);
        this.writing_text_view_.setMovementMethod(new ScrollingMovementMethod());

        this.context_ = context;

        this.shared_preferences_ = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        ViewGroup.LayoutParams layoutParams = this.scroll_view_.getLayoutParams();
        layoutParams.height = (int) (height*0.35);
        layoutParams.width= width;
        this.scroll_view_.setLayoutParams(layoutParams);
        layoutParams = this.constraint_layout_.getLayoutParams();
        layoutParams.height = (int)(height*0.6);
        layoutParams.width= width;
        this.constraint_layout_.setLayoutParams(layoutParams);


        scroll_view_.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                writing_text_view_.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        writing_text_view_.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event)
            {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }
    public TextView MakeTextViewFromText(String text, Context context, int gravity,
                                                MovementMethod movement,int text_size_dip, String parsecolor){
        TextView textView = new TextView(context);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expression expression = FragmentAlgebraicCalc.GetExpression();

                TextView text = (TextView)view;
                if(!expression.GetMod().equals("") && !expression.GetExpression().equals(""))
                    AddTextOnLinearLayout(GetTextFromWritingTextView());
                SetTextInWritingTextView(text.getText().toString());

                FragmentAlgebraicCalc.SetExpression(Expression.MakeExpression(GetTextFromWritingTextView()));

                scroll_view_.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll_view_.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
        textView.setText(text);
        textView.setId(View.generateViewId());
        textView.setGravity(gravity);
        textView.setMovementMethod(movement);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_size_dip);
        textView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.parseColor(parsecolor));

        return textView;
    }
    public TextView MakeMainActivityTextViewFromText(String text, Context context){
        return  MakeTextViewFromText(text, context,Gravity.RIGHT | Gravity.CENTER_VERTICAL ,
                new ScrollingMovementMethod(),20,"#FBF9F9");
    }
    public void AddTextOnLinearLayout(String text){
        if(!list_string_.contains(text)) {
            TextView textView = MakeMainActivityTextViewFromText(text, context_);
            textView.setTextSize(textsize_addtext_);
            list_string_.add(text);
            linear_layout_.addView(textView);
            scroll_view_.post(new Runnable() {
                @Override
                public void run() {
                    scroll_view_.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }
    public void SetTextInWritingTextView(String text){
        writing_text_view_.setText(text);
    }
    public String GetTextFromWritingTextView(){
        return writing_text_view_.getText().toString();
    }
    void Clear(){
        list_string_.clear();
        linear_layout_.removeAllViews();
    }
    @Override
    public void SaveInMemory(){
        SharedPreferences.Editor editor = shared_preferences_.edit();
        Gson gson = new Gson();
        editor.putString("Views", gson.toJson(list_string_));
        editor.putString("writingView", writing_text_view_.getText().toString());
        editor.commit();
    }
    @Override
    public void LoadFromMemory(){
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        String save = shared_preferences_.getString("Views","null");
        if(!save.equals("null")) {
            for (JsonElement jsonElement : parser.parse(save).getAsJsonArray())
                AddTextOnLinearLayout(gson.fromJson(jsonElement, String.class));
        }
        save = shared_preferences_.getString("writingView","null");
        if(!save.equals("null")) {
            writing_text_view_.setText(save);
        }

    }

}
