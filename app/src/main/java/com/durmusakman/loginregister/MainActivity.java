package com.durmusakman.loginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    EditText email, password;
    Button login, register;
    CheckBox   loginState;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sharedPreferences= getSharedPreferences("Userınfo", Context.MODE_PRIVATE);

        email =findViewById(R.id.email);
        password =findViewById(R.id.password);
        loginState =findViewById(R.id.checkbox);
        login =findViewById(R.id.login);
        register =findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String txtEmail= email.getText().toString();
                String txtPassword = password.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("Userınfo",0);
                SharedPreferences.Editor editor =   sharedPreferences.edit();
                editor.putString("deneme",email.getText().toString());
                editor.commit();



                if(TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){

                    Toast.makeText(MainActivity.this, "all field requrid",Toast.LENGTH_SHORT).show();

                }else{

                    login(txtEmail,txtPassword);
                }
            }
        });
        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");
        if(loginStatus.equals("loggedin")){


            startActivity(new Intent(MainActivity.this,AppStartActivity.class));



        }



    }


    private void login(String email,String password){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Giriş yapılıyor");
        progressDialog.show();
        String uRl="https://tork.kumruscooter.com/qr/login.php";
        StringRequest request= new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response,Toast.LENGTH_SHORT).show();

                if(response.equals("giriş başarılı")){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor =sharedPreferences.edit();

                    startActivity(new Intent(MainActivity.this,AppStartActivity.class));
                    if(loginState.isChecked()){
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedin");
                    }else{
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedout");
                    }


                    editor.apply();
                    Intent  intent = new Intent(MainActivity.this,AppStartActivity.class);
                    intent.putExtra("k",email);
                    startActivity(intent);


                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",password);

                return  param;

            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);

    }
}