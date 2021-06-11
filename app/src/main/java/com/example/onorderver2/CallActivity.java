package com.example.onorderver2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onorderver2.model.OrderRecyclerItem;
import com.example.onorderver2.model.PrintOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallActivity extends Activity {

    LinearLayout btnWater, btnGrill, btnPlate, btnBowl, btnChild, btnScoop, btnOk, btnCancel;

    ImageView btnClose;

    SharedPreferences pref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("order");

    String popToast = "요청이 전달 되었습니다.";

    boolean isCheckedCall = false;
    String serviceText;

    static String BASE_URL = "http://15.164.232.164:5000/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        pref = getSharedPreferences("pref", MODE_PRIVATE);

        btnWater = findViewById(R.id.btn_request_water);
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnWater.setBackgroundResource(R.drawable.square_border_call_checked);
                if (!isCheckedCall) {
                    isCheckedCall = true;
                    serviceText = "물 요청";

                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL).addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.postService(pref.getString("storecode", ""), pref.getString("table", ""), serviceText, ts).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            sendFirebaseOrder(serviceText);
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

            }
        });

        btnGrill = findViewById(R.id.btn_request_side);
        btnGrill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckedCall) {
                    isCheckedCall = true;
                    serviceText = "반찬 요청";

                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL).addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.postService(pref.getString("storecode", ""), pref.getString("table", ""), serviceText, ts).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            sendFirebaseOrder(serviceText);

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        btnChild = findViewById(R.id.btn_request_plate);
        btnChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckedCall) {
                    isCheckedCall = true;
                    serviceText = "접시 요청";

                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL).addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.postService(pref.getString("storecode", ""), pref.getString("table", ""), serviceText, ts).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            sendFirebaseOrder(serviceText);
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        btnPlate = findViewById(R.id.btn_request_cup);
        btnPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckedCall) {
                    isCheckedCall = true;
                    serviceText = "물컵 요청";

                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL).addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.postService(pref.getString("storecode", ""), pref.getString("table", ""), serviceText, ts).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            sendFirebaseOrder(serviceText);
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        btnBowl = findViewById(R.id.btn_request_fork);
        btnBowl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckedCall) {
                    isCheckedCall = true;
                    serviceText = "포크 요청";

                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL).addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.postService(pref.getString("storecode", ""), pref.getString("table", ""), serviceText, ts).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            sendFirebaseOrder(serviceText);
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        btnScoop = findViewById(R.id.btn_request_server);
        btnScoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckedCall) {
                    isCheckedCall = true;
                    serviceText = "직원 요청";

                    Long tsLong = System.currentTimeMillis();
                    String ts = tsLong.toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL).addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.postService(pref.getString("storecode", ""), pref.getString("table", ""), serviceText, ts).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            sendFirebaseOrder(serviceText);
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        btnClose = findViewById(R.id.btn_close_call);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOk = findViewById(R.id.btn_service_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceText = "";
                if (isCheckedCall) {
                    serviceText = serviceText + "직원호출";
                }

                Long tsLong = System.currentTimeMillis();
                String ts = tsLong.toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL).addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                interfaceAPI.postService(pref.getString("storecode", ""), pref.getString("table", ""), serviceText, ts).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        sendFirebaseOrder(serviceText);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                finish();
            }
        });

        btnCancel = findViewById(R.id.btn_service_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendFirebaseOrder(String order) {
        ArrayList<OrderRecyclerItem> orderItems = new ArrayList<>();
        OrderRecyclerItem item = new OrderRecyclerItem();
        boolean isCounted = false;


                    item.setName(order);
                    item.setCount("");
                    item.setPrice("");
                    item.setPicUrl("");
                    item.setMenuNo("");
                    orderItems.add(item);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd",  Locale.getDefault());

        String time = format.format(calendar.getTime());
        String time2 = format2.format(calendar.getTime());

        PrintOrderModel printOrderModel = new PrintOrderModel(pref.getString("table", ""), orderItems, time, "x", "service",
                "", "", "", "", "", "", "", "", "", "x");
        ref.child(pref.getString("storename", "")).child(time2).push().setValue(printOrderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast toast = Toast.makeText(CallActivity.this, popToast, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        });
    }


}
