package com.durmusakman.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {



    EditText userName, email, password, mobile;
    RadioGroup radioGroup;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName=findViewById(R.id.username);
        email =findViewById(R.id.email);
        password =findViewById(R.id.password);
        mobile =findViewById(R.id.mobile);
        radioGroup =findViewById(R.id.radiogp);
        register =findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUserName = userName.getText().toString();
                String txtEmail= email.getText().toString();
                String txtPassword = password.getText().toString();
                String txtMobile= mobile.getText().toString();
               if(TextUtils.isEmpty(txtUserName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)|| TextUtils.isEmpty(txtMobile)){

                   Toast.makeText(RegisterActivity.this,"All fiels reqired",Toast.LENGTH_SHORT).show();

               }
               else{
                   int genderId =radioGroup.getCheckedRadioButtonId();
                   RadioButton selected_Gender = radioGroup.findViewById(genderId);
                    if(selected_Gender == null){
                        Toast.makeText(RegisterActivity.this,"Lütfen Cinsiyet seçiniz", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        String selectGender = selected_Gender.getText().toString();
                        registerNewAccount(txtUserName,txtEmail,txtPassword,txtMobile,selectGender);
                    }
               }

            }
        });
    }

    private void registerNewAccount(final String username,final String email,final String password,final String mobile,final String gender){
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
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
                    Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }
                else{

                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("username",username);
                param.put("email",email);
                param.put("password",password);
                param.put("mobile",mobile);
                param.put("gender",gender);
                return  param;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);



    }
}