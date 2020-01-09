package com.example.pestpro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    Toolbar myToolbar;
    LayoutInflater layoutInflater;
    TextView titleBar;

    LinearLayout selectLang;
    TextView selectedLang;
    int checkedItem=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);

        myToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar4);
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
            titleBar.setText(getResources().getString(R.string.Settings));
            if (actionBar != null) {
                actionBar.setCustomView(action_view);
            }
        }

        selectLang=findViewById(R.id.change_lang);
        selectedLang=findViewById(R.id.selected_lang);

        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");

        if (language != null) {
            if(language.equals("ta"))
            {
                selectedLang.setText(getResources().getString(R.string.lang2));
                checkedItem=0;
            }else if(language.equals("hi"))
            {
                selectedLang.setText(getResources().getString(R.string.lang3));
                checkedItem=1;
            }else
            {
                selectedLang.setText(getResources().getString(R.string.lang1));
                checkedItem=2;
            }
        }

        selectLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showChangeLanguageDialog();
            }
        });

    }

    private void showChangeLanguageDialog() {

        final String[] listItems={"Tamil","Hindi","English"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(SettingsActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i==0){
                    setLocale("ta");
                    recreate();
                }
                else if(i==1){
                    setLocale("hi");
                    recreate();
                }
                else if(i==2){
                    setLocale("en");
                    recreate();
                }
                else
                {
                    setLocale("en");
                    recreate();
                }

                dialog.dismiss();

                Intent startOver=new Intent(SettingsActivity.this,MainActivity.class);
                startOver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startOver.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startOver);
                finish();
            }
        });

        AlertDialog mDialog=mBuilder.create();
        mDialog.show();

    }

    private void setLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config =new Configuration();
        config.locale=(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }

    public void loadLocale()
    {
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);
    }

}

