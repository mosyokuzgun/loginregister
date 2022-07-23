package com.durmusakman.loginregister;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DepoyaCekme extends AppCompatActivity implements LocationListener {

    private CodeScanner mCodeScanner;
    private CodeScannerView mCodeScannerView;
    public Button ArizaKayit;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    Date currentTime;
    SimpleDateFormat date1;

    Context context = this;
    RequestQueue requestQueue;

    String qrcode;
    String email;
    String lat;
    String provider;
    double konum1;
    double konum2;
    String islem = "Depoya Çekildi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Depoya Çekme");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        SharedPreferences sharedPreferences = getSharedPreferences("Userınfo", 0);

        email = sharedPreferences.getString("deneme", email);


        setContentView(R.layout.activity_depoya_cekme);
        ArizaKayit = findViewById(R.id.ArizaKayit);
        ArizaKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = qrcode;
                //Pass data to 2nd activity
                Intent intent = new Intent(DepoyaCekme.this, ArizaKayitOlustur.class);
                intent.putExtra("name", getName);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
        } else {
            startScanning();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        konum1 = location.getLatitude();
        konum2 = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
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
                        Toast toast = Toast.makeText(DepoyaCekme.this, "KOD " + qrcode, Toast.LENGTH_LONG);

                        toast.setGravity(Gravity.CENTER,0,50);

                        toast.show();

                    }
                });
            }
        });

        //now if you want to scan again when you click on scanner then do this.

        mCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String uRL = "https://tork.kumruscooter.com/qr/depo.php";
                StringRequest request = new StringRequest(Request.Method.POST, uRL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast toast = Toast.makeText(DepoyaCekme.this, response.trim(), Toast.LENGTH_LONG);

                        toast.setGravity(Gravity.CENTER,0,50);

                        toast.show();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ;
                        Toast toast = Toast.makeText(DepoyaCekme.this, "OKUNAMADI", Toast.LENGTH_LONG);

                        toast.setGravity(Gravity.CENTER,0,50);

                        toast.show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("email", email);
                        param.put("qrcode", qrcode);
                        param.put("islem", islem);
                        param.put("lat", String.valueOf(konum1));
                        param.put("lan", String.valueOf(konum2));
                        return param;

                    }

                };
                RequestQueue requestQueue = Volley.newRequestQueue(DepoyaCekme.this);
                requestQueue.add(request);
                finish();
                startActivity(getIntent());
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
        startActivity(new Intent(DepoyaCekme.this,AppStartActivity.class));
        finish();
    }
}