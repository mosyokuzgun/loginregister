
package com.durmusakman.loginregister;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class TamirIslem extends AppCompatActivity {

    String email;
    EditText qrcode;
    EditText arizakodu;
    EditText aciklama;
    String id;
    EditText idstr;
    Button register;
    String[] ariza = { "Direksiyon", "Fren", "Gidon", "İzmir", "Diğer",  };
    TextView name, number;
    TextView yapilanislem;
    TextView kullanilan;
    boolean[] yapilansecilen;
    boolean[] kullanilansecilen;
    ArrayList<Integer> kullanilanList2 = new ArrayList<>();
    ArrayList<String> kullanilanList = new ArrayList<>();
    ArrayList<Integer> yapilanList2 = new ArrayList<>();
    ArrayList<String> yapilanList = new ArrayList<>();
    String durum ="1";
    String[] yapilanArray = {"Javxa", "C++", "Kotlin", "C", "Python", "Javascript"};
    String[] kullanilanArray = {"Java", "C++", "Kotlin", "C", "Python", "Javascript"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Tamir İşlem");
        SharedPreferences sharedPreferences = getSharedPreferences("Userınfo",0);
        email = sharedPreferences.getString("deneme",email);
        setContentView(R.layout.tamir_islem);
        qrcode=findViewById(R.id.qrcode);

        Intent intent = getIntent();
        String qrcodestr = intent.getStringExtra("qrcode");
        String idstr = intent.getStringExtra("id");
        id=getIntent().getStringExtra("id");
        qrcode.setText(qrcodestr);


        yapilanislem = findViewById(R.id.yapilanislem);
        yapilansecilen = new boolean[yapilanArray.length];
        yapilanislem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder pencere = new AlertDialog.Builder(TamirIslem.this);
                pencere.setTitle("Select Language");
                pencere.setCancelable(false);
                pencere.setMultiChoiceItems(yapilanArray, yapilansecilen, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            yapilanList2.add(i);
                            yapilanList.add(yapilanArray[i]);
                            Collections.sort(yapilanList2);
                        } else {
                            yapilanList2.remove(Integer.valueOf(i));
                        }
                    }
                });

                pencere.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < yapilanList2.size(); j++) {
                            stringBuilder.append(yapilanArray[yapilanList2.get(j)]);
                            if (j != yapilanList2.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        yapilanislem.setText(stringBuilder.toString());
                    }
                });

                pencere.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                pencere.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < yapilansecilen.length; j++) {
                            yapilansecilen[j] = false;
                            yapilanList2.clear();
                            yapilanList.clear();
                            yapilanislem.setText("");
                        }
                    }
                });
                pencere.show();
            }
        });


        kullanilan = findViewById(R.id.kullanilan);
        kullanilansecilen = new boolean[kullanilanArray.length];
        kullanilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TamirIslem.this);
                builder.setTitle("Select Language");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(kullanilanArray, kullanilansecilen, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            kullanilanList2.add(i);
                            kullanilanList.add(kullanilanArray[i]);
                            Collections.sort(kullanilanList2);
                        } else {
                            kullanilanList2.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < kullanilanList2.size(); j++) {
                            stringBuilder.append(kullanilanArray[kullanilanList2.get(j)]);
                            if (j != kullanilanList2.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        kullanilan.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < kullanilansecilen.length; j++) {
                            kullanilansecilen[j] = false;
                            kullanilanList2.clear();
                            kullanilanList.clear();
                            kullanilan.setText("");
                        }
                    }
                });
                builder.show();
            }
        });






        arizakodu =findViewById(R.id.arizakodu);
        aciklama =findViewById(R.id.aciklama);
        register =findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String txtqrcode = qrcode.getText().toString();
                String txtariza = yapilanList.toString();
                String txtarizakodu = kullanilanList.toString();
                String txtaciklama= aciklama.getText().toString();

                if(TextUtils.isEmpty(txtqrcode) || yapilanList.isEmpty() || TextUtils.isEmpty(txtaciklama)){

                    Toast.makeText(TamirIslem.this,"All fiels reqired",Toast.LENGTH_SHORT).show();

                }
                else{
                    registerNewAccount(txtqrcode,txtariza,txtarizakodu,txtaciklama);
                }

            }
        });
    }

    private void registerNewAccount(final String qrcode,final String ariza,final String arizakodu,final String aciklama){
        final ProgressDialog progressDialog = new ProgressDialog(TamirIslem.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Yeni Hesap Oluşturuldu");
        progressDialog.show();
        String uRl="https://tork.kumruscooter.com/qr/tamir-islem.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(response.equals("başarılı şekilde kayıt oldunuz")){

                    progressDialog.dismiss();
                    Toast.makeText(TamirIslem.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TamirIslem.this,MainActivity.class));
                    finish();
                }
                else{

                    progressDialog.dismiss();
                    Toast.makeText(TamirIslem.this, response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TamirIslem.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("qrcode",qrcode);
                param.put("yapilan",yapilanList.toString());
                param.put("kullanilan",kullanilanList.toString());
                param.put("aciklama",aciklama);
                param.put("email", email);
                param.put("durum", durum);
                param.put("id", id);


                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(TamirIslem.this).addToRequestQueue(request);



    }
}