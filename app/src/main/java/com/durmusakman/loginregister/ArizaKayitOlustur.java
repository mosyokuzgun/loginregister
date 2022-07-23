
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



public class ArizaKayitOlustur extends AppCompatActivity {


    String email;
    EditText qrcode, arizakodu, aciklama, diger, testDataList;
    Button register;
    String[] ariza = { "Direksiyon", "Fren", "Gidon", "İzmir", "Diğer",  };
    TextView textView;
    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    ArrayList<String> dataList = new ArrayList<>();
    String durum ="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Arıza Kaydı Oluştur");
        SharedPreferences sharedPreferences = getSharedPreferences("Userınfo",0);
        email = sharedPreferences.getString("deneme",email);
        setContentView(R.layout.activity_ariza_kayit_olustur);
        qrcode=findViewById(R.id.qrcode);
        Intent intent = getIntent();
        String qrcodestr = intent.getStringExtra("name");
        qrcode.setText(qrcodestr);

        textView = findViewById(R.id.textView);
        selectedLanguage = new boolean[ariza.length];

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArizaKayitOlustur.this);
                builder.setTitle("Arıza");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(ariza, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            langList.add(i);
                            dataList.add(ariza[i]);
                            Collections.sort(langList);
                        } else {
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < langList.size(); j++) {
                            stringBuilder.append(ariza[langList.get(j)]);
                            if (j != langList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        textView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            selectedLanguage[j] = false;
                            langList.clear();
                            dataList.clear();
                            textView.setText("");
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
                String txtariza = dataList.toString();
                String txtarizakodu = arizakodu.getText().toString();
                String txtaciklama= aciklama.getText().toString();

                if(TextUtils.isEmpty(txtqrcode) || dataList.isEmpty() || TextUtils.isEmpty(txtaciklama)){

                        Toast.makeText(ArizaKayitOlustur.this,"All fiels reqired",Toast.LENGTH_SHORT).show();

                    }
                    else{
                     registerNewAccount(txtqrcode,txtariza,txtarizakodu,txtaciklama);
                    }

        }
        });
    }

    private void registerNewAccount(final String qrcode,final String ariza,final String arizakodu,final String aciklama){
        final ProgressDialog progressDialog = new ProgressDialog(ArizaKayitOlustur.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Yeni Hesap Oluşturuldu");
        progressDialog.show();
        String uRl="https://tork.kumruscooter.com/qr/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(response.equals("başarılı şekilde kayıt oldunuz")){

                    progressDialog.dismiss();
                    Toast.makeText(ArizaKayitOlustur.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ArizaKayitOlustur.this,MainActivity.class));
                    finish();
                }
                else{

                    progressDialog.dismiss();
                    Toast.makeText(ArizaKayitOlustur.this, response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ArizaKayitOlustur.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("qrcode",qrcode);
                param.put("ariza",dataList.toString());
                param.put("arizakodu",arizakodu);
                param.put("aciklama",aciklama);
                param.put("email", email);
                param.put("durum", durum);


                return param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(ArizaKayitOlustur.this).addToRequestQueue(request);



    }
}