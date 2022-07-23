package com.durmusakman.loginregister;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Scanner extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private CodeScannerView mCodeScannerView;
    Date currentTime;
    SimpleDateFormat date1;

    Context context = this;
    RequestQueue requestQueue;

    String qrcode, email,email1;
    String teslim ="-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Batarya ekleme");

        SharedPreferences sharedPreferences = getSharedPreferences("Userınfo",0);

        email = sharedPreferences.getString("deneme",email);









        setContentView(R.layout.activity_scanner);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
        } else {
            startScanning();
        }
    }

    private void startScanning() {


        mCodeScannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, mCodeScannerView);
        mCodeScanner.startPreview();   // this line is very important, as you will not be able to scan your code without this, you will only get blank screen
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrcode = result.toString();
                        Toast.makeText(Scanner.this, qrcode, Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });

        //now if you want to scan again when you click on scanner then do this.

        mCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String uRL = "https://tork.kumruscooter.com/qr/batarya.php";
                StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Scanner.this, response.trim(), Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ;
                        Toast.makeText(Scanner.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("email", email);
                        param.put("qrcode", qrcode);
                        param.put("teslim", teslim);



                        return param;

                    }

                };
                RequestQueue requestQueue = Volley.newRequestQueue(Scanner.this);
                requestQueue.add(request);

                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "İzin verildi", Toast.LENGTH_SHORT).show();
                startScanning();
            } else {
                Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Scanner.this,AppStartActivity.class));
        finish();

    }
}