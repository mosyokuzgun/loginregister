package com.durmusakman.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class BataryaKayitlari extends AppCompatActivity {
    String komut = "bilgileriver";


ListView Lw;
Button show;
String deneme = "showarray";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batarya_kayitlari);
        Lw =(ListView) findViewById(R.id.deneme);
        show = findViewById(R.id.show);
        BataryaKaydi();


        getSupportActionBar().setTitle("Veri tabanı işlemleri");
        BataryaKaydi();
        Lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long ı) {
                String secilen = (String) Lw.getItemAtPosition(i);
                String [] s = secilen.split("-");
                String id = s[0];
                String email = s[1];
                String qrcode = s[2];
                String aliszaman = s[3];

                kayıtsil(id);
            }
        });





    }


    public void kayıtsil  ( final String  id){
        final String url = "https://tork.kumruscooter.com/qr/delete.php";
        RequestQueue istek = Volley.newRequestQueue(this);
        StringRequest gonder = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(BataryaKayitlari.this, response.trim(), Toast.LENGTH_LONG).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ;
                Toast.makeText(BataryaKayitlari.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("id", id);


                return param;

            }

        };
        istek.add(gonder);



    }

    public void BataryaKaydi (){
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://tork.kumruscooter.com/qr/show.php";
                StringRequest istek = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response){
                                JSONObject veri_json;
                                try {
                                    String u, p,d,id;
                                    veri_json = new JSONObject(response);
                                    JSONArray Jarray = veri_json.getJSONArray("showarray");
                                    final ArrayList<String> arraylistim = new ArrayList<String>();
                                    final ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1, arraylistim);

                                    for (int i = 0; i < Jarray.length(); i++) {

                                        JSONObject Jasonobject = Jarray.getJSONObject(i);
                                        id=Jasonobject.getString("id");
                                        u = Jasonobject.getString("email");
                                        p= Jasonobject.getString("qrcode");
                                        d= Jasonobject.getString("aliszaman");


                                        arraylistim.add(id + "-" + u + "-" + p + "-" + d);
                                        Lw.setAdapter(adaptor);
                                        adaptor.notifyDataSetChanged();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(BataryaKayitlari.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BataryaKayitlari.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }){

                    protected Map<String ,String> getParams() throws com.android.volley.AuthFailureError{
                        Map<String, String> params = new HashMap<String,String>();
                        params.put("komut",komut);
                        return  params;
                    }
                };
                istek.setShouldCache(false);
                AppController.getInstance().addToRequestQueue(istek);


            }
        });









    }
}