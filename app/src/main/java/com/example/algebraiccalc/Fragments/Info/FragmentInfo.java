package com.example.algebraiccalc.Fragments.Info;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.algebraiccalc.R;
import com.example.algebraiccalc.ToolbarCreating;


public class FragmentInfo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);


        Menu menu = ToolbarCreating.getNavigationView().getMenu();
        ToolbarCreating.iterateMenuItemsToSetChecked(menu, menu.findItem(R.id.info));

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        TextView textView = view.findViewById(R.id.info_textview);
        textView.setTextSize(width/6/8);
        return view;
    }
}