package com.example.onorderver2;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onorderver2.Fragment.MenuFragment;
import com.example.onorderver2.Fragment.MenuInfoFragment;
import com.example.onorderver2.model.CategoryRecyclerItem;
import com.example.onorderver2.model.MenuRecyclerItem;
import com.example.onorderver2.model.OrderRecyclerItem;
import com.example.onorderver2.model.PrintOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity {

    private static String BASE_URL = "http://15.164.232.164:5000/";

    SharedPreferences pref;
    String storeCode;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    JsonArray ctgArray = new JsonArray();

    String prevAuthNum = "";
    String prevAuthDate = "";

    String prevClassification = "";
    String vanTr = "";
    String prevCardNo = "";

    String cardName, company;

    MenuFragment menuFragment;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;
    MenuCategoryAdapter adapter;
    Context mContext;
    ArrayList<ArrayList<MenuRecyclerItem>> menuArray = new ArrayList<>();

    String removePrice = "";

    ArrayList<OrderRecyclerItem> orderItems = new ArrayList<>();
    OrderAdapter orderAdapter;
    RecyclerView orderRecycler;
    static TextView orderTotalPrice;
    TextView tableTimer, table;
    TextView tableNo;
    LinearLayout btnPayment, btnService;

    boolean isOrdered = false;
    int categoryPosition = 0;

    static int totalPrice = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("order");

    String payType = "";

    TaskTimer taskTimer = new TaskTimer();
    static int BASIC = 100;
    static int TIMER_BASIC = 120;
    Context context;
    Timer timer = new Timer();
    TimerTask timerTask;

    private final Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private final int UPDATE = 1;
    private long backKeyPressedTime = 0;
    boolean isFirstOrder = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        storeCode = pref.getString("storecode", "");

        context = this;
        tableTimer = findViewById(R.id.table_timer);
        taskTimer.setTime(BASIC, context);
        taskTimer.execute("");
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Log.d("daon_test", "timer = "+TIMER_BASIC);
//                String timerText = "";
//                TIMER_BASIC--;
//                if (TIMER_BASIC > 60){
//                    timerText = "01:"+String.valueOf(TIMER_BASIC - 60);
//                }else{
//                    timerText = "00:"+String.valueOf(TIMER_BASIC);
//                }
//                tableTimer.setText(String.valueOf(timerText));
//            }
//        };

        table = findViewById(R.id.btn_timer_reset);
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis();
                    return;
                }
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    if (mTimerTask != null) {
                        mTimerTask.cancel();
                    }
                    isFirstOrder = false;
//                    mTimerTask = null;
//                    tableTimer.setText("02:00");
//                    TIMER_BASIC = 120;
                    Toast toast = Toast.makeText(MenuActivity.this, "리셋 되었습니다.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }
            }
        });
        tableNo = findViewById(R.id.table_no);
        tableNo.setText(pref.getString("table", ""));
        fragmentManager = getSupportFragmentManager();


        orderRecycler = findViewById(R.id.order_recyclerview);
        orderTotalPrice = (TextView) findViewById(R.id.order_total_price);
        orderTotalPrice.setText("총 0원");

        btnPayment = findViewById(R.id.btn_payment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOrdered) {
                    if (orderItems.size() > 0) {
                        if (isFirstOrder) {
                            PaymentDialog paymentDialog = new PaymentDialog(context, new PaymentDialogClickListener() {
                                @Override
                                public void onCardClick() {
                                    Log.d("storced", "onCardClick: " + pref.getString("storecode", ""));
                                    setPayment(String.valueOf(totalPrice), "credit");
                                }

                                @Override
                                public void onCashClick() {
                                    sendData();
                                }

                                @Override
                                public void onReOrderClick() {
//                                setPayment("2500", "cancel");
                                    isOrdered = false;
                                }
                            });

                            paymentDialog.setCanceledOnTouchOutside(false);
                            paymentDialog.setCancelable(false);
                            paymentDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            paymentDialog.show();
                            isOrdered = true;
                        }else{
                            for (int i = 0; i < orderItems.size(); i++){
                                if (Integer.parseInt(orderItems.get(i).getMenuNo()) > 7000 && Integer.parseInt(orderItems.get(i).getMenuNo()) < 7013){
                                    isFirstOrder = true;
                                    break;
                                }
                                if (Integer.parseInt(orderItems.get(i).getMenuNo()) > 7045 && Integer.parseInt(orderItems.get(i).getMenuNo()) < 7058){
                                    isFirstOrder = true;
                                    break;
                                }
                                if (Integer.parseInt(orderItems.get(i).getMenuNo()) > 7084 && Integer.parseInt(orderItems.get(i).getMenuNo()) < 7131){
                                    isFirstOrder = true;
                                    break;
                                }
                            }
                            if (isFirstOrder){
                                PaymentDialog paymentDialog = new PaymentDialog(context, new PaymentDialogClickListener() {
                                    @Override
                                    public void onCardClick() {
                                        Log.d("storced", "onCardClick: " + pref.getString("storecode", ""));
                                        setPayment(String.valueOf(totalPrice), "credit");
                                    }

                                    @Override
                                    public void onCashClick() {
                                        sendData();
                                    }

                                    @Override
                                    public void onReOrderClick() {
//                                setPayment("2500", "cancel");
                                        isOrdered = false;
                                    }
                                });

                                paymentDialog.setCanceledOnTouchOutside(false);
                                paymentDialog.setCancelable(false);
                                paymentDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                paymentDialog.show();
                            }else{
                                Toast toast = Toast.makeText(MenuActivity.this, "메인메뉴 주문 후 이용가능합니다.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                    }
                }
            }
        });

        fragmentManager = getSupportFragmentManager();

        mContext = this;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
        interfaceAPI.getCategory(storeCode).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    ctgArray = response.body();
                    ArrayList<CategoryRecyclerItem> ctgItems = new ArrayList<>();
                    for (int j = 0; j < ctgArray.size(); j++) {
                        String obj = ctgArray.get(j).toString();
                        obj = obj.replace("\\", "");
                        obj = obj.substring(2, obj.length() - 2);
                        Log.d("ctg", "onResponse: " + obj);
                        JSONObject strObj = null;
                        try {
                            strObj = new JSONObject(obj);

                            CategoryRecyclerItem item = new CategoryRecyclerItem(
                                    strObj.get("code").toString(),
                                    strObj.get("name").toString());

                            ctgItems.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int j = 0; j < ctgItems.size(); j++) {
                        Log.d("AAAA", "onResponse: " + ctgItems.get(j).getCode());
                    }

                    setList(ctgItems);
                    layoutManager = new LinearLayoutManager(mContext);
                    recyclerView = findViewById(R.id.category_recyclerview);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new MenuCategoryAdapter(mContext, ctgItems);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

//        btnPayment = findViewById(R.id.btn_payment);
//        btnPayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                taskTimer.setTime(BASIC);
//                if (!isOrdered) {
//                    if (orderItems.size() > 0) {
//
//                    }
//                }
//            }
//        });

        btnService = findViewById(R.id.btn_service);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CallActivity.class);
                startActivity(intent);
            }
        });

        BackThread thread = new BackThread();  // 작업스레드 생성
        thread.setDaemon(true);  // 메인스레드와 종료 동기화
        thread.start();
    }

    public void setList(ArrayList<CategoryRecyclerItem> ctgItems) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
        interfaceAPI.getMenu(pref.getString("storecode", "")).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray array = response.body();
                    Log.d("OPOP", "onResponse: " + array);
                    for (int i = 0; i < ctgItems.size(); i++) {
                        ArrayList<MenuRecyclerItem> menuItems = new ArrayList<>();
                        for (int j = 0; j < array.size(); j++) {
                            String obj = array.get(j).toString();
                            obj = obj.replace("\\", "");
                            obj = obj.substring(2, obj.length() - 2);
                            JSONObject strObj = null;

                            try {
                                strObj = new JSONObject(obj);
                                if (strObj.get("ctgcode").toString().equals(ctgItems.get(i).getCode())) {
                                    Log.d("daon_test", "adaf = "+strObj.get("name"));
                                    MenuRecyclerItem item = new MenuRecyclerItem(
                                            strObj.get("name").toString(),
                                            strObj.get("price").toString(),
                                            strObj.get("picurl").toString(),
                                            strObj.get("info").toString(),
                                            strObj.get("code").toString(),
                                            strObj.get("ctgcode").toString()

                                    );
                                    menuItems.add(item);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        menuArray.add(menuItems);
                    }

                    MenuFragment menuFragment = new MenuFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("items", menuArray.get(0));
                    menuFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.fragment_menu, menuFragment).commit();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void callCategory(int position){
        Long tsLong = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        int te = Integer.parseInt(format.format(tsLong));

        if (te < 17 && position == 3) {
            Toast toast = Toast.makeText(mContext, "17:00 부터 주문 가능합니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else if (te > 16 && position == 4) {
            Toast toast = Toast.makeText(mContext, "런치메뉴는 11:00 부터 17:00 까지 주문 가능합니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            categoryPosition = position;
            MenuFragment menuFragment = new MenuFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("items", menuArray.get(position));
            menuFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_menu, menuFragment).commit();
        }



    }

    public void callInfo(int position){
        Long tsLong = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        int te = Integer.parseInt(format.format(tsLong));

        if (te > 15 && categoryPosition == 0) {

            if (position > 2) {
                Toast toast = Toast.makeText(mContext, "11:00 부터 16:00 까지 주문 가능합니다.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else{
                ArrayList<MenuRecyclerItem> arrayList = menuArray.get(categoryPosition);
                Log.d("daon_test", "menu = " + arrayList.get(position).name);
                MenuInfoFragment menuInfoFragment = new MenuInfoFragment(this);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list",  menuArray.get(categoryPosition));
                bundle.putInt("position", position);
                bundle.putBoolean("isFirst", isFirstOrder);
//        bundle.putSerializable("items", (Serializable)(MenuRecyclerItem) arrayList.get(position));
                menuInfoFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_menu, menuInfoFragment).commit();
            }


        }else{
            ArrayList<MenuRecyclerItem> arrayList = menuArray.get(categoryPosition);
            Log.d("daon_test", "menu = " + arrayList.get(position).name);
            MenuInfoFragment menuInfoFragment = new MenuInfoFragment(this);
            Bundle bundle = new Bundle();
            bundle.putSerializable("list",  menuArray.get(categoryPosition));
            bundle.putInt("position", position);
            bundle.putBoolean("isFirst", isFirstOrder);
//        bundle.putSerializable("items", (Serializable)(MenuRecyclerItem) arrayList.get(position));
            menuInfoFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_menu, menuInfoFragment).commit();
        }

    }

    public void callOrderItem(String menuName, String price, String picUrl, String menuNo, int count, String option) {
        OrderRecyclerItem item = new OrderRecyclerItem();
        boolean isCounted = false;

        if (orderItems.size() > 0) {
            for (int i = 0; i < orderItems.size(); i++) {
                if (orderItems.get(i).getName().equals(menuName)) {
                    item.setName(orderItems.get(i).getName()+option);
                    item.setCount(String.valueOf(Integer.parseInt(orderItems.get(i).getCount()) + Integer.parseInt(String.valueOf(count))));
                    item.setPrice(String.valueOf(Integer.parseInt(price) * count));
                    item.setPicUrl(picUrl);
                    item.setMenuNo(menuNo);
                    orderItems.set(i, item);
                    isCounted = true;
                    break;
                }
            }
            if (!isCounted) {
                item.setName(menuName+option);
                item.setCount(String.valueOf(count));
                item.setPrice(price);
                item.setMenuNo(menuNo);
                item.setPicUrl(picUrl);
                orderItems.add(item);
            }
        } else {
            item.setName(menuName+option);
            item.setCount(String.valueOf(count));
            item.setPrice(price);
            item.setMenuNo(menuNo);
            item.setPicUrl(picUrl);
            orderItems.add(item);
        }

        totalPrice = totalPrice + (Integer.parseInt(price) * count);

        orderAdapter = new OrderAdapter(MenuActivity.this, orderItems);
        orderRecycler.setAdapter(orderAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        orderRecycler.setLayoutManager(linearLayoutManager);

        DecimalFormat dFormat = new DecimalFormat("###,###");
        String formattedPrice = dFormat.format(totalPrice);

        orderTotalPrice.setText("총 " + formattedPrice + "원");

        MenuFragment menuFragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("items", menuArray.get(categoryPosition));
        menuFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_menu, menuFragment).commit();
        Log.d("TTT", "callOrderItem: " + totalPrice);
    }

    public void removeOrder(int price) {
        Log.d("TTT", "removeOrder: " + totalPrice);
        totalPrice = totalPrice - price;
        Log.d("ASDF", "removeOrder: " + totalPrice);
        if (totalPrice > 0) {
            DecimalFormat dFormat = new DecimalFormat("###,###");
            String formattedPrice = dFormat.format(totalPrice);
            orderTotalPrice.setText(formattedPrice);
        } else {
            totalPrice = 0;
            orderTotalPrice.setText("총 0원");
            isOrdered = false;
        }
    }

    public void sendData() {
        String order = "";
        Log.d("daon_test", "size = "+orderItems.size());
        for (int i = 0; i < orderItems.size(); i++) {
            String orderName = orderItems.get(i).getName();
            order = orderName + " ##" + orderItems.get(i).getCount() + "개##";
        }
        Log.d("daon_test", "order = "+order);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        String sendTime = format.format(calendar.getTime());
        if (orderItems.size() > 0) {
            Long tsLong = System.currentTimeMillis();
            String ts = tsLong.toString();
            String orderItem = "";
            for (int i = 0; i < orderItems.size(); i++) {
                orderItem = orderItem + orderItems.get(i).getName() + " " + orderItems.get(i).getCount() + " 개" + orderItems.get(i).getPrice() + " " + "\n";
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                interfaceAPI.postOrder(pref.getString("storecode", ""), pref.getString("table", ""), orderItems.get(i).getMenuNo(), orderItems.get(i).getPrice(), orderItems.get(i).getCount(),
                        String.valueOf((Integer.parseInt(orderItems.get(i).getPrice())) * (Integer.parseInt(orderItems.get(i).getCount()))), "Cash", orderItems.get(i).getName(), ts,
                        pref.getString("table", "")+sendTime, "  ", "  ", "  ").enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
            interfaceAPI.payment(pref.getString("storecode", ""), "",
                    "", "", "",
                    "", "O", sendTime, "",
                    "", pref.getString("table", "")+sendTime, "",
                    "", "", "", "",
                    "", "", "", "",
                    "", "", "",
                    "Cash", "", "", "",
                    "", String.valueOf(totalPrice)).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        sendFirebaseOrder(orderItems, "cash");

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            isOrdered = false;
        }
    }

    public void closeMenuInfo() {
        MenuFragment menuFragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("items", menuArray.get(categoryPosition));
        menuFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_menu, menuFragment).commit();
    }

    public void event() {
        if (orderItems.size() > 0) {
            removePrice = String.valueOf(totalPrice);
            orderAdapter.removeDate();
            totalPrice = 0;
            orderTotalPrice.setText("총 0원 ");
            isOrdered = false;
            MenuFragment menuFragment = new MenuFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("items", menuArray.get(0));
            bundle.putInt("position", 0);
            menuFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_menu, menuFragment).commit();
        }
    }

    public void sendFirebaseOrder(ArrayList<OrderRecyclerItem> order, String type) {
//        String type1 = "번 주문";
//        if (!payType.equals("")) {
//            type1 = "번 포장";
//            payType = "";
//        }
//
//        if (type.equals("cash")) {
//            type1 = "번 현금";
//        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String time = format.format(calendar.getTime());
        String time2 = format2.format(calendar.getTime());


//        PrintOrderModel printOrderModel = new PrintOrderModel(pref.getString("table", "") + type1, order, time, "x", "order");
        PrintOrderModel printOrderModel = new PrintOrderModel(pref.getString("table", ""), order, time, "x", "order",
                cardName, prevCardNo, prevAuthDate, prevAuthNum, company, company, vanTr, String.valueOf(totalPrice), type, "x");
        ref.child(pref.getString("storename", "")).child(time2).push().setValue(printOrderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    removePrice = String.valueOf(totalPrice);
                    orderAdapter.removeDate();
                    totalPrice = 0;
                    orderTotalPrice.setText("총 0원 ");
                    isOrdered = false;
                    Toast toast = Toast.makeText(MenuActivity.this, "주문이 전달되었습니다.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
//                    if (mTimerTask == null){
//                        mTimerTask = createTimerTask();
//                        mTimer.schedule(mTimerTask, 0, 60000);
//                    }
//                    timer.schedule(timerTask, 0, 6000);
                } else {
                    Toast toast = Toast.makeText(MenuActivity.this, "다시 시도해 주세요.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });



    }

    public void sendOrder() {
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        String order = "";
        for (int i = 0; i < orderItems.size(); i++) {
            order = order + orderItems.get(i).getName() + " " + orderItems.get(i).getCount() + " 개" + orderItems.get(i).getPrice() + " " + "\n";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
            interfaceAPI.postOrder(pref.getString("storecode", ""), pref.getString("table", ""), orderItems.get(i).getMenuNo(), orderItems.get(i).getPrice(), orderItems.get(i).getCount(),
                    String.valueOf((Integer.parseInt(orderItems.get(i).getPrice())) * (Integer.parseInt(orderItems.get(i).getCount()))), "Card", orderItems.get(i).getName(), ts,
                    prevAuthNum, prevAuthDate, vanTr, prevCardNo).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }



        sendFirebaseOrder(orderItems, "card");
    }

    public void setPayment(String amount, String type) {
        Log.d("daon", "payment = " + amount);
//        amount = "5000";
//        prevAuthNum = "72880465    ";
//        prevAuthDate = "210523";
        int i_amount = Integer.parseInt(amount);
        int tax = (i_amount/100)*10;
        int aamount = (i_amount/100)*90;
        HashMap<String, byte[]> m_hash = new HashMap<String, byte[]>();
        /*고정 사용필드*/
        m_hash.put("TelegramType", "0200".getBytes());                                    // 전문 구분 ,  승인(0200) 취소(0420)
        m_hash.put("DPTID", "AT0298903A".getBytes());                                     // 단말기번호 , 테스트단말번호 DPT0TEST03
        m_hash.put("PosEntry", "S".getBytes());                                           // Pos Entry Mode , 현금영수증 거래 시 키인거래에만 'K'사용
        m_hash.put("PayType", "00".getBytes());                                           // [신용]할부개월수(default '00') [현금]거래자구분
        m_hash.put("TotalAmount", getStrMoneyToTgAmount(amount)); // 총금액
        m_hash.put("Amount", getStrMoneyToTgAmount(String.valueOf(aamount)));      // 공급금액 = 총금액 - 부가세 - 봉사료
        m_hash.put("ServicAmount", getStrMoneyToTgAmount("0"));                           // 봉사료
        m_hash.put("TaxAmount", getStrMoneyToTgAmount(String.valueOf(tax)));                              // 부가세
//        m_hash.put("TaxAmount", getStrMoneytoTgAmount("0"));                              // 부가세
        m_hash.put("FreeAmount", getStrMoneyToTgAmount("0"));                             // 면세 0처리  / 면세 1004원일 경우 총금액 1004원 봉사료(ServiceAmount),부가세(TaxAmount) 0원 공급금액 1004원/ 면세(FreeAmount)  1004원
        m_hash.put("AuthNum", "".getBytes());                                            //원거래 승인번호 , 취소시에만 사용
        m_hash.put("Authdate", "".getBytes());                                           //원거래 승인일자 , 취소시에만 사용
        m_hash.put("Filler", "".getBytes());                                              // 여유필드 - 판매차 필요시에만 입력처리
        m_hash.put("SignTrans", "N".getBytes());                                          // 서명거래 필드, 무서명(N) 50000원 초과시 서명 "N" => "S"변경 필수
        if (Long.parseLong(amount) > 50000)
            m_hash.put("SignTrans", "S".getBytes());                                          // 서명거래 필드, 무서명(N) 50000원 초과시 서명 "N" => "S"변경 필수
        m_hash.put("PlayType", "D".getBytes());                                           // 실행구분,  데몬사용시 고정값(D)
        m_hash.put("CardType", "".getBytes());                                            // 은련선택 여부필드 (현재 사용안함), "" 고정
        m_hash.put("BranchNM", "".getBytes());                                            // 가맹점명 ,관련 개발 필요가맹점만 입력 , 없을시 "" 고정
        m_hash.put("BIZNO", "".getBytes());                                               // 사업자번호 ,KSNET 서버 정의된 가맹정일경우만 사용, 없을 시"" 고정
        m_hash.put("TransType", "".getBytes());                                           // "" 고정
        m_hash.put("AutoClose_Time", "30".getBytes());                                    // 사용자 동작 없을 시 자동 종료 ex)30초 후 종료
        /*선택 사용필드*/
        //m_hash.put("SubBIZNO","".getBytes());                                            // 하위 사업자번호 ,하위사업자 현금영수증 승인 및 취소시 적용
        //m_hash.put("Device_PortName","/dev/bus/usb/001/002".getBytes());                 //리더기 포트 설정 필요 시 UsbDevice 인스턴스의 getDeviceName() 리턴값입력 , 필요없을경우 생략가능
        //m_hash.put("EncryptSign","A!B@C#D4".getBytes());                                 // SignTrans "T"일경우 KSCIC에서 서명 받지않고 해당 사인데이터로 승인진행, 특정업체사용

        ComponentName compName = new ComponentName("ks.kscic_ksr01", "ks.kscic_ksr01.PaymentDlg");

        Intent intent = new Intent(Intent.ACTION_MAIN);

        if (type.equals("credit")) {
            m_hash.put("ReceiptNo", "X".getBytes());  // 현금영수증 거래필드, 신용결제 시 "X", 현금영수증 카드거래시 "", Key-In거래시 "휴대폰번호 등 입력" -> Pos Entry Mode 'K;
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        } else if (type.equals("cancel")) {
            //신용취소 호출 부
            m_hash.put("TelegramType", "0420".getBytes());  // 전문 구분 ,  승인(0200) 취소(0420)
            m_hash.put("ReceiptNo", "X".getBytes());        // 현금영수증 거래필드, 신용결제 시 "X", 현금영수증 카드거래시 "", Key-In거래시 "휴대폰번호 등 입력" -> Pos Entry Mode 'K;
            m_hash.put("AuthNum", prevAuthNum.getBytes());
            m_hash.put("Authdate", prevAuthDate.getBytes());
        } else if (type.equals("cancelNoCard")) {
            //신용 무카드 취소 호출부
            m_hash.put("TelegramType", "0420".getBytes()); // 전문 구분 ,  승인(0200) 취소(0420)
            m_hash.put("ReceiptNo", "X".getBytes());      // 현금영수증 거래필드, 신용결제 시 "X", 현금영수증 카드거래시 "", Key-In거래시 "휴대폰번호 등 입력" -> Pos Entry Mode 'K;
            m_hash.put("VanTr", vanTr.getBytes());        // 거래고유번호 , 무카드 취소일 경우 필수 필드
            m_hash.put("Cardbin", prevCardNo.getBytes());
            m_hash.put("AuthNum", prevAuthNum.getBytes());
            m_hash.put("Authdate", prevAuthDate.getBytes());
        }

        intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(compName);
        intent.putExtra("AdminInfo_Hash", m_hash);
        startActivityForResult(intent, 0);
    }

    public byte[] getStrMoneyToTgAmount(String Money) {
        byte[] TgAmount = null;
        if (Money.length() == 0) {
//            Toast.makeText(MainActivity.this, "테스트 금액으로 승인진행", Toast.LENGTH_SHORT).show();
            return "000000001004".getBytes();
        } else {
            Long longMoney = Long.parseLong(Money.replace(",", ""));
            Money = String.format("%012d", longMoney);

            TgAmount = Money.getBytes();
            return TgAmount;
        }
    }

    public void timerReset() {
        taskTimer.setTime(BASIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            HashMap<String, String> hashMap = (HashMap<String, String>) data.getSerializableExtra("result");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (hashMap != null) {
                prevAuthNum = hashMap.get("AuthNum");
                prevAuthDate = hashMap.get("Authdate");
                prevClassification = hashMap.get("Classification");
                vanTr = hashMap.get("VanTr");
                prevCardNo = hashMap.get("CardNo");

                //KTC 인증용 출력
                Log.d("payment", "recv [Classification]:: " + (hashMap.get("Classification")));
                System.out.println("recv [TelegramType]:: " + (hashMap.get("TelegramType")));
                System.out.println("recv [Dpt_Id]:: " + (hashMap.get("Dpt_Id")));
                System.out.println("recv [Enterprise_Info]:: " + (hashMap.get("Enterprise_Info")));
                System.out.println("recv [Full_Text_Num]:: " + (hashMap.get("Full_Text_Num")));
                System.out.println("recv [Status]:: " + (hashMap.get("Status")));
                System.out.println("recv [CardType]:: " + (hashMap.get("CardType")));              //'N':신용카드 'G':기프트카드 'C':체크카드 'P'선불카드 'P'고운맘 바우처
                System.out.println("recv [Authdate]:: " + (hashMap.get("Authdate")));
                System.out.println("recv [Message1]:: " + (hashMap.get("Message1")));
                System.out.println("recv [Message2]:: " + (hashMap.get("Message2")));
                System.out.println("recv [VanTr]:: " + (hashMap.get("VanTr")));
                System.out.println("recv [AuthNum]:: " + (hashMap.get("AuthNum")));
                System.out.println("recv [FranchiseID]:: " + (hashMap.get("FranchiseID")));
                System.out.println("recv [IssueCode]:: " + (hashMap.get("IssueCode")));
                System.out.println("recv [CardName]:: " + (hashMap.get("CardName")));
                System.out.println("recv [PurchaseCode]:: " + (hashMap.get("PurchaseCode")));
                System.out.println("recv [PurchaseName]:: " + (hashMap.get("PurchaseName")));
                System.out.println("recv [Remain]:: " + (hashMap.get("Remain")));
                System.out.println("recv [point1]:: " + (hashMap.get("point1")));
                System.out.println("recv [point2]:: " + (hashMap.get("point2")));
                System.out.println("recv [point3]:: " + (hashMap.get("point3")));
                System.out.println("recv [notice1]:: " + (hashMap.get("notice1")));
                System.out.println("recv [notice2]:: " + (hashMap.get("notice2")));
                System.out.println("recv [CardNo]:: " + (hashMap.get("CardNo")));

            }

            if (hashMap.get("VanTr") == null) {
                Toast.makeText(this, hashMap.get("Message1"), Toast.LENGTH_LONG).show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                interfaceAPI.payment(pref.getString("storecode", ""), hashMap.get("Classification"),
                        hashMap.get("TelegramType"), hashMap.get("Dpt_Id"), hashMap.get("Enterprise_Info"),
                        hashMap.get("Full_Text_Num"), hashMap.get("Status"), hashMap.get("Authdate"), hashMap.get("Message1"),
                        hashMap.get("Message2"), hashMap.get("AuthNum"), hashMap.get("FranchiseID"),
                        hashMap.get("IssueCode"), hashMap.get("CardName"), hashMap.get("PurchaseCode"), hashMap.get("PurchaseName"),
                        hashMap.get("Remain"), hashMap.get("point1"), hashMap.get("point2"), hashMap.get("point3"),
                        hashMap.get("notice1"), hashMap.get("notice2"), hashMap.get("CardType"),
                        hashMap.get("CardNo"), hashMap.get("SWModelNum"), hashMap.get("ReaderModelNum"), hashMap.get("VanTr"),
                        hashMap.get("Cardbin"), String.valueOf(totalPrice)).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("SUUSUS", "onResponse: " + response.isSuccessful());
                        if (response.isSuccessful()) {
                            Log.d("SUUSUS", "onResponse: ");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                Toast.makeText(this, "성공", Toast.LENGTH_LONG).show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                interfaceAPI.payment(pref.getString("storecode", ""), hashMap.get("Classification"),
                        hashMap.get("TelegramType"), hashMap.get("Dpt_Id"), hashMap.get("Enterprise_Info"),
                        hashMap.get("Full_Text_Num"), hashMap.get("Status"), hashMap.get("Authdate"), hashMap.get("Message1"),
                        hashMap.get("Message2"), hashMap.get("AuthNum"), hashMap.get("FranchiseID"),
                        hashMap.get("IssueCode"), hashMap.get("CardName"), hashMap.get("PurchaseCode"), hashMap.get("PurchaseName"),
                        hashMap.get("Remain"), hashMap.get("point1"), hashMap.get("point2"), hashMap.get("point3"),
                        hashMap.get("notice1"), hashMap.get("notice2"), hashMap.get("CardType"),
                        hashMap.get("CardNo"), hashMap.get("SWModelNum"), hashMap.get("ReaderModelNum"), hashMap.get("VanTr"),
                        hashMap.get("Cardbin"), String.valueOf(totalPrice)).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            sendOrder();
                            prevAuthNum = hashMap.get("AuthNum");
                            prevAuthDate = hashMap.get("Authdate");
                            cardName = hashMap.get("CardName");
                            company = hashMap.get("PurchaseName");

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }

        } else if (resultCode == RESULT_FIRST_USER && data != null) {

        } else {
            Toast.makeText(this, "응답값 리턴 실패", Toast.LENGTH_LONG).show();
        }

        // 수행을 제대로 하지 못한 경우
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "앱 호출 실패", Toast.LENGTH_LONG).show();
        }

        isOrdered = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (taskTimer.getStatus() != AsyncTask.Status.RUNNING) {
            taskTimer = new TaskTimer();
            taskTimer.setTime(BASIC, context);
            taskTimer.execute("");
        }
    }

    class BackThread extends Thread {  // Thread 를 상속받은 작업스레드 생성
        @Override
        public void run() {
            while (true) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//                Log.d("daon_test", String.valueOf(app.getPrinter().IsConnected(Sam4sPrint.DEVTYPE_ETHERNET)));
//                Log.d("daon_test", String.valueOf(app.getPrinter2().IsConnected(Sam4sPrint.DEVTYPE_ETHERNET)));
                String time_ = format2.format(calendar.getTime());
                Log.d("TIME", "run: " + time_);
//                if (time != time_) {
//                    time = time_;
//                    initFirebase();
//                }
//                Log.d("daon_test", "time = " + time);
                try {
                    Thread.sleep(60000);   // 1000ms, 즉 1초 단위로 작업스레드 실행
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_menu);

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = manager.getRunningTasks(1);
        ComponentName name = infos.get(0).topActivity;
        String topActivityName = name.getShortClassName().substring(1);
        Log.d("topName", "onBackPressed: " + topActivityName);
        String fragmentName = fragment.toString().substring(0, fragment.toString().lastIndexOf("{"));
        Log.d("fragName", "onBackPressed: " + fragmentName);

        if (fragmentName.equals("MenuFragment")) {
            Log.d("DPDP", "onBackPressed: ");
            return;
        } else {
//            transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragment_frame, mainFragment).commit();
//            Log.d("gse", "after onBackPressed: " + fragment.toString());
        }
    }

    //실제 메세지를 받아 동작할 핸들러
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case UPDATE:
                    updateStatus();
                    break;
            }
        }
    };

    //타이머 테스크 생성, 동작은 핸들러로 메세지를 넘겨서 동작시킨다
    private TimerTask createTimerTask() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(UPDATE);
            }
        };
        return timerTask;
    }

    //핸들러에서 수행한 동작
    private void updateStatus() {
        Log.d("test", "update");
        String timerText = "";
        if (TIMER_BASIC > 0) {
            TIMER_BASIC--;
            if (TIMER_BASIC > 59) {
                if (TIMER_BASIC - 60 < 10) {
                    timerText = "01:0" + String.valueOf(TIMER_BASIC - 60);
                } else {
                    timerText = "01:" + String.valueOf(TIMER_BASIC - 60);
                }
            } else {
                if (TIMER_BASIC < 10) {
                    timerText = "00:0" + String.valueOf(TIMER_BASIC);
                } else {
                    timerText = "00:" + String.valueOf(TIMER_BASIC);
                }
            }
            tableTimer.setText(String.valueOf(timerText));
        } else {
            ArrayList<OrderRecyclerItem> orderItems1 = new ArrayList<>();
            OrderRecyclerItem item = new OrderRecyclerItem();
            boolean isCounted = false;


            item.setName("시간초과");
            item.setCount("");
            item.setPrice("");
            item.setPicUrl("");
            item.setMenuNo("");
            orderItems1.add(item);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            String time = format.format(calendar.getTime());
            String time2 = format2.format(calendar.getTime());

            PrintOrderModel printOrderModel = new PrintOrderModel(pref.getString("table", ""), orderItems1, time, "x", "service",
                    "", "", "", "", "", "", "", "", "", "x");
            ref.child(pref.getString("storename", "")).child(time2).push().setValue(printOrderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (mTimerTask != null)
                        mTimerTask.cancel();
                }
            });
        }
    }
}
