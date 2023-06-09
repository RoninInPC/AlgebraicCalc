package com.example.algebraiccalc;

import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.algebraiccalc.Fragments.AlgebraicCalc.FragmentAlgebraicCalc;
import com.example.algebraiccalc.Fragments.Info.FragmentInfo;
import com.example.algebraiccalc.Fragments.PolynomCalc.FragmentPolynomCalc;
import com.google.android.material.navigation.NavigationView;

public class ToolbarCreating {
    private static Toolbar toolbar_ = null;
    private static DrawerLayout drawer_layout_ = null;
    private static NavigationView navigation_view_ = null;
    private static MenuItem now_menu_item_ = null;

    public ToolbarCreating(Toolbar toolbar, DrawerLayout drawerLayout, NavigationView navigationView, AppCompatActivity activity){
        toolbar_ = toolbar;
        toolbar_.setFitsSystemWindows(true);

        ViewGroup.LayoutParams layoutParams = toolbar_.getLayoutParams();
        layoutParams.height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.06);

        toolbar_.setLayoutParams(layoutParams);
        toolbar_.setTitle("");
        activity.setSupportActionBar(toolbar_);
        toolbar_.bringToFront();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Menu menu = toolbar_.getMenu();
        MenuItem item =  menu.findItem(R.id.itemR);
        item.setVisible(false);
        item = menu.findItem(R.id.itemField);
        item.setVisible(false);
        item = menu.findItem(R.id.itemPolynomField);
        item.setVisible(false);

        drawer_layout_ = drawerLayout;
        navigation_view_ = navigationView;

        ViewGroup.LayoutParams layoutParams1 = navigationView.getLayoutParams();
        layoutParams1.width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels*0.7);

        iterateMenuItems(navigationView.getMenu(),layoutParams1.width/14);

        navigation_view_.setLayoutParams(layoutParams1);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer_layout_, toolbar_,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout_.addDrawerListener(toggle);
        toggle.syncState();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout_.openDrawer(GravityCompat.START);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                iterateMenuItemsToSetNewString(toolbar_.getMenu(),item);
                return true;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                now_menu_item_ = menuItem;
                //iterateMenuItemsToSetChecked(navigation_view_.getMenu());

                Fragment fragment = new Fragment();
                switch(menuItem.getItemId()){
                    case R.id.algebraic_calc:
                        fragment = new FragmentAlgebraicCalc();
                        break;
                    case R.id.info:
                        fragment = new FragmentInfo();
                        break;
                    case R.id.polynom_calc:
                        fragment = new FragmentPolynomCalc();
                        break;
                    default:
                        break;
                }
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
                drawer_layout_.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.bringToFront();

        View fragment1 = activity.findViewById(R.id.frameLayout);
        layoutParams1 = fragment1.getLayoutParams();
        layoutParams1.width= activity.getResources().getDisplayMetrics().widthPixels;
        fragment1.setLayoutParams(layoutParams1);

    }

    protected static void iterateMenuItems(Menu menu, int size) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                iterateMenuItems(item.getSubMenu(),size);
            } else {
                SpannableString spannableString = new SpannableString(item.getTitle().toString());
                spannableString.setSpan(new AbsoluteSizeSpan(size),
                        0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                item.setTitle(spannableString);
            }
        }
    }
    public static void iterateMenuItemsToSetChecked(Menu menu, MenuItem menuItem) {
        if(menuItem == null || menu == null)
            return;
        now_menu_item_ = menuItem;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                iterateMenuItemsToSetChecked(item.getSubMenu(),menuItem);
            } else {
                if(item.getItemId() == menuItem.getItemId())
                    item.setChecked(true);
                else
                    item.setChecked(false);
            }
        }
    }

    private static void ChangeColorInSpan(MenuItem menuItem, String color){
        SpannableString spannableString = new SpannableString(menuItem.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color)),0,spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem.setTitle(spannableString);
    }

    public static void iterateMenuItemsToSetNewString(Menu menu, MenuItem menuItem) {
        if(menuItem == null || menu == null)
            return;
        now_menu_item_ = menuItem;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                iterateMenuItemsToSetNewString(item.getSubMenu(),menuItem);
            } else {
                if(item.getItemId() == menuItem.getItemId()) {
                    ChangeColorInSpan(item,"#FFCA0707");
                }
                else {
                    ChangeColorInSpan(item,"#FFFFFFFF");
                }
            }
        }
    }

    public static NavigationView getNavigationView(){
        return navigation_view_;
    }
    public static Toolbar getToolbar(){
        return toolbar_;
    }
    public static MenuItem getNowMenuItem(){
        return now_menu_item_;
    }
}
