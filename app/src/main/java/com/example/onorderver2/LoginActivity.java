package com.example.onorderver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText loginId, loginPwd, inputTblNo;

    TextView btnLogin;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static String BASE_URL = "http://15.164.232.164:5000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginId = findViewById(R.id.login_id);
        loginPwd = findViewById(R.id.login_password);
        inputTblNo = findViewById(R.id.input_table_no);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        if (!pref.getString("table", "").equals("")) {
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tableNo = inputTblNo.getText().toString();

                if (!tableNo.equals("")) {
//                String strId = loginId.getText().toString();
//                String strPwd = loginPwd.getText().toString();
                    String strId = "shhl";
                    String strPwd = "1234";
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                            .addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.loginService(strId, strPwd, "").enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.d("TEST", "onResponse: " + response.isSuccessful());
                            if (response.isSuccessful()) {
                                if (String.valueOf(response.body().get("StatusCode")).equals("200")) {
                                    String fcm = String.valueOf(response.body().get("Message")).replace("\"","");
                                    editor.putString("id", strId);
                                    editor.putString("fcm", fcm);
                                    editor.putString("table", inputTblNo.getText().toString());
                                    editor.putString("storecode", String.valueOf(response.body().get("StoreCode")).replace("\"",""));
                                    editor.putString("addr", String.valueOf(response.body().get("Addr")).replace("\"", ""));
                                    editor.putString("storename", String.valueOf(response.body().get("Name")).replace("\"", ""));
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("TTT", "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });
    }
}