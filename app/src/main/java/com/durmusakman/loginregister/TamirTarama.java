package com.durmusakman.loginregister;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TamirTarama extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private CodeScannerView mCodeScannerView;
    Date currentTime;
    SimpleDateFormat date1;
    Context context = this;
    RequestQueue requestQueue;
    String[] email2;
    String qrcode, email;
    String teslim = "0";
    public Button Beklet;
    String beklet = "7";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Tamir Tara");
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
                        Toast.makeText(TamirTarama.this, qrcode, Toast.LENGTH_SHORT).show();



                    }
                });
            }
        });

        mCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uRL = "https://tork.kumruscooter.com/qr/tamir-tara.php" ;
                StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String id = jsonObject.getString("id");
                            String email = jsonObject.getString("email");
                            String qrcode = jsonObject.getString("qrcode");
                            String arizakodu = jsonObject.getString("arizakodu");
                            String ariza = jsonObject.getString("ariza");
                            String aciklama = jsonObject.getString("aciklama");
                            String geliszamani = jsonObject.getString("geliszamani");

                            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View popupView = inflater.inflate(R.layout.tamir_tarama_popup, null);
                            int width = LinearLayout.LayoutParams.MATCH_PARENT;
                            int height = LinearLayout.LayoutParams.MATCH_PARENT;
                            boolean focusable = true;
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                            TextView idstr = popupView.findViewById(R.id.id);
                            idstr.setText(id);
                            TextView emailstr = popupView.findViewById(R.id.email);
                            emailstr.setText(email);
                            TextView qrcodestr = popupView.findViewById(R.id.qrcode);
                            qrcodestr.setText(qrcode);
                            TextView arizakodustr = popupView.findViewById(R.id.arizakodu);
                            arizakodustr.setText(arizakodu);
                            TextView arizastr = popupView.findViewById(R.id.ariza);
                            arizastr.setText(ariza);
                            TextView aciklamastr = popupView.findViewById(R.id.aciklama);
                            aciklamastr.setText(aciklama);
                            TextView geliszamanistr = popupView.findViewById(R.id.geliszamani);
                            geliszamanistr.setText(geliszamani);




                            Button buttonEdit = popupView.findViewById(R.id.islemebasla);
                            Intent intent = new Intent(TamirTarama.this, TamirIslem.class);

                            buttonEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent.putExtra("qrcode", qrcode.toString());
                                    intent.putExtra("id", id.toString());
                                    startActivity(new Intent(TamirTarama.this,TamirIslem.class));
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            Beklet = popupView.findViewById(R.id.beklet);
                            Beklet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String uRL = "https://tork.kumruscooter.com/qr/tamir-tara.php";
                                    StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(TamirTarama.this, "Beklemeye Alındı", Toast.LENGTH_LONG).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ;
                                            Toast.makeText(TamirTarama.this, "Hata", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            HashMap<String, String> param = new HashMap<>();
                                            param.put("durum", beklet);
                                            param.put("id", id.toString());
                                            return param;
                                        }

                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(TamirTarama.this);
                                    requestQueue.add(request);
                                }
                            });

                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent event) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });

                        }catch (JSONException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ;
                        Toast.makeText(TamirTarama.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("qrcode", qrcode.toString());
                        return param;
                    }

                };
                RequestQueue requestQueue = Volley.newRequestQueue(TamirTarama.this);
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
        startActivity(new Intent(TamirTarama.this,AppStartActivity.class));
        finish();
    }

}