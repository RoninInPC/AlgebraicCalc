package com.example.algebraiccalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.Fragment;

import com.example.algebraiccalc.Fragments.AlgebraicCalc.FragmentAlgebraicCalc;
import com.example.algebraiccalc.Fragments.Info.FragmentInfo;
import com.example.algebraiccalc.Fragments.PolynomCalc.FragmentPolynomCalc;
import com.example.algebraiccalc.Interfaces.MemoryManipulation;

public class DefaultFragmentLoading implements MemoryManipulation{

    private Fragment load_fragment_;
    private SharedPreferences shared_preferences_ = null;

    private String string_save_ = "FragmentId";

    public DefaultFragmentLoading(Context applicationContext){
        shared_preferences_ = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }
    @Override
    public void SaveInMemory(){
        SharedPreferences.Editor editor = shared_preferences_.edit();
        editor.putInt(string_save_, ToolbarCreating.getNowMenuItem().getItemId());
        editor.commit();
    }
    @Override
    public void LoadFromMemory(){
        int idFragment = shared_preferences_.getInt(string_save_,-100);
        switch (idFragment){
            case R.id.info:
                load_fragment_ = new FragmentInfo();
                break;
            case R.id.polynom_calc:
                load_fragment_ = new FragmentPolynomCalc();
                break;
            case R.id.algebraic_calc:
            default:
                load_fragment_ = new FragmentAlgebraicCalc();
                break;
        }
    }
    public Fragment getFragment(){
        return load_fragment_;
    }
}
