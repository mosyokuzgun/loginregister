package com.durmusakman.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppStartActivity extends AppCompatActivity {
    Context context = this;

    public Button button,button1,button2,button3,button4,birak,SahayaBirak, ArizaKayit, AracaYukle, TamirTarama;
    String value,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("İşlem Ekranı");
        setContentView(R.layout.activity_app_start);
        button = findViewById(R.id.button1);
        TamirTarama = findViewById(R.id.TamirTarama);
        button1 = findViewById(R.id.button2);
        button2=findViewById(R.id.button3);
        button3=findViewById(R.id.DepoyaCekme);
        button4 = findViewById(R.id.batarya);
        SahayaBirak = findViewById(R.id.SahayaBirak);
        ArizaKayit = findViewById(R.id.ArizaKayit);
        birak = findViewById(R.id.birak);
        AracaYukle = findViewById(R.id.aracayukle);
        Intent intent = getIntent();
        value = intent.getStringExtra("k");
        SharedPreferences sharedPreferences = getSharedPreferences("Userınfo",MODE_PRIVATE);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this,BataryaKayitlari.class));

            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this,Arackaydi.class));
            }
        });

        AracaYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this,AracaYukle.class));
            }
        });





        ArizaKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this, ArizaKayit.class));
            }
        });




        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this,DepoyaCekme.class));
            }
        });

        SahayaBirak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this,SahayaBirak.class));
            }
        });


        birak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this,BataryaBirak.class));
            }
        });
        TamirTarama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppStartActivity.this,TamirTarama.class));
            }
        });










        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppStartActivity.this,Scanner.class));








                finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getResources().getString(R.string.prefLoginState),"logetout");
                editor.apply();
                startActivity(new Intent(AppStartActivity.this,MainActivity.class));
                finish();







            }
        });





    }

    @Override
    public void onBackPressed() {

    }





}