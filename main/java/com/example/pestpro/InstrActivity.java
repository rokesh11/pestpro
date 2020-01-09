package com.example.pestpro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mayuonline.tamilandroidunicodeutil.TamilUtil;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class InstrActivity extends AppCompatActivity {

    Toolbar myToolbar;
    LayoutInflater layoutInflater;
    TextView titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instr);

        myToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar3);
        if(myToolbar!=null) {
            setSupportActionBar(myToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowCustomEnabled(true);
            }

            layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View action_view = layoutInflater.inflate(R.layout.custom_action_bar, null);
            titleBar = action_view.findViewById(R.id.head_bar);
            titleBar.setText(getResources().getString(R.string.instruction));
            if (actionBar != null) {
                actionBar.setCustomView(action_view);
            }
        }
    }
}
