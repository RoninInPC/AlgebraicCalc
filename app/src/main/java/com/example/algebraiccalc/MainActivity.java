package com.example.algebraiccalc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;

import com.example.algebraiccalc.Fragments.AlgebraicCalc.FragmentAlgebraicCalc;
import com.example.algebraiccalc.Fragments.Info.FragmentInfo;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ToolbarCreating toolbar_creating_ = null;
    private DefaultFragmentLoading default_fragment_loading_ = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_creating_ = new ToolbarCreating(
                findViewById(R.id.tlbr),
                findViewById(R.id.cl),
                findViewById(R.id.nav_view),
                this);
        default_fragment_loading_ = new DefaultFragmentLoading(getApplicationContext());

        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        if(displayMetrics.widthPixels< displayMetrics.heightPixels)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        default_fragment_loading_.LoadFromMemory();

        Fragment fragment = default_fragment_loading_.getFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        default_fragment_loading_.SaveInMemory();
    }
    @Override
    protected void onStop() {
        super.onStop();
        default_fragment_loading_.SaveInMemory();
    }
}