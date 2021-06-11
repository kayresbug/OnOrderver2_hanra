package com.example.onorderver2.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.onorderver2.MenuActivity;
import com.example.onorderver2.R;
import com.example.onorderver2.model.MenuRecyclerItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MenuInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int position;

    private String mParam1;
    private String mParam2;

    Context context;
    ArrayList<MenuRecyclerItem> items = null;

    ImageView menuImage, btnPlus, btnMinus, btnClose, btnOptionPlus, btnOptionMinus, btnOption2Plus, btnOption2Minus, btnOption3Plus, btnOption3Minus, btnOption4Plus, btnOption4Minus;

    TextView menuName, menuInfo, menuPrice, menuCount, btnAddCart, optionCount, sub1MenuCount, sub2MenuCount,sub3MenuCount, sub4MenuCount;

    String price;

    LinearLayout optionLayout, optionLayout2, optionLayout3, optionLayoutTitle;

    View rootView;
    FragmentManager fragmentManager;
    RadioButton radioButton1, radioButton2, radioButton3;
    boolean isFirst = false;
    public MenuInfoFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            items = (ArrayList<MenuRecyclerItem>) getArguments().get("list");
            position = (int) getArguments().get("position");
            isFirst = getArguments().getBoolean("isFirst");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu_info, container, false);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String basePrice = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()));

        radioButton1 = rootView.findViewById(R.id.radio1);
        radioButton2 = rootView.findViewById(R.id.radio2);
        radioButton3 = rootView.findViewById(R.id.radio3);

        btnClose = rootView.findViewById(R.id.btn_close_info);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity)getActivity()).closeMenuInfo();
            }
        });

        menuName = rootView.findViewById(R.id.menu_info_name);
        menuName.setText(items.get(position).getName());
        menuInfo = rootView.findViewById(R.id.menu_info_detail);
        menuInfo.setText(items.get(position).getInfo());
        menuPrice = rootView.findViewById(R.id.menu_info_price);
        menuPrice.setText(basePrice);
        menuImage = rootView.findViewById(R.id.menu_info_image);

        sub1MenuCount = rootView.findViewById(R.id.menu_option_count);
        sub2MenuCount = rootView.findViewById(R.id.menu_option2_count);
        sub3MenuCount = rootView.findViewById(R.id.menu_option3_count);
        sub4MenuCount = rootView.findViewById(R.id.menu_option4_count);
        menuCount = rootView.findViewById(R.id.menu_count);
        optionCount = rootView.findViewById(R.id.menuinfofragment_text_count2);
        optionLayout = rootView.findViewById(R.id.menuinfofragment_layout_option1);
        optionLayout3 = rootView.findViewById(R.id.menuinfofragment_layout_option3);
        optionLayoutTitle = rootView.findViewById(R.id.menuinfofragment_layout_option1_title);
        if (!items.get(position).getInfo().contains("맵기단계")){
            optionLayout.setVisibility(View.GONE);
            optionCount.setText("01");
        }
        if (items.get(position).getName().contains("한라원특선 A")){
//            optionLayout.setVisibility(View.VISIBLE);
            optionLayout3.setVisibility(View.VISIBLE);
            optionLayoutTitle.setVisibility(View.GONE);
            optionCount.setText("01");
            radioButton1.setText("된장찌개");
            radioButton2.setText("김치찌개");
            radioButton3.setVisibility(View.GONE);
        }

//        if (items.get(position).getName().contains("한라원특선 B")){
//            optionLayout.setVisibility(View.VISIBLE);
//            optionLayoutTitle.setVisibility(View.GONE);
//            optionCount.setText("01");
//            radioButton1.setText("된장찌개");
//            radioButton2.setText("김치찌개");
//            radioButton3.setVisibility(View.GONE);
//        }

        optionLayout2 = rootView.findViewById(R.id.menuinfofragment_layout_option2);
        if (items.get(position).name.contains("한라원특선 B") || items.get(position).name.contains("한라원특선 C")) {
            optionLayout2.setVisibility(View.GONE);
        }

        int imageUrl = context.getResources().getIdentifier(items.get(position).getPicurl(), "drawable", context.getPackageName());
        Glide.with(context).load(imageUrl).into(menuImage);

        if (items.get(position).getInfo().contains("2인분 이상") || items.get(position).name.contains("한라원특선 A")) {
            menuCount.setText("2");
            String moreThanTwo = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()) * 2);
            menuPrice.setText(moreThanTwo);
        }

        btnPlus = rootView.findViewById(R.id.btn_menu_plus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(menuCount.getText().toString());
                if (items.get(position).name.contains("한라원특선 A")) {
                    if (count < 4) {
                        count++;
                        menuCount.setText(String.valueOf(count));
                        String formattedPrice = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()) * count);
                        menuPrice.setText(formattedPrice);
                    }
                }else{
                    count++;
                    menuCount.setText(String.valueOf(count));
                    String formattedPrice = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()) * count);
                    menuPrice.setText(formattedPrice);
                }
            }
        });

        btnMinus = rootView.findViewById(R.id.btn_menu_minus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(menuCount.getText().toString());
                if (items.get(position).getInfo().contains("2인분 이상") || items.get(position).name.contains("한라원특선 A")) {
                    if (items.get(position).getName().contains("LA양념갈비")) {
                        if (isFirst) {
                            if (count > 1) {
                                count--;
                                menuCount.setText(String.valueOf(count));
                                String formattedPrice = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()) * count);
                                menuPrice.setText(formattedPrice);
                            }
                        }else {
                            if (count > 2) {
                                count--;
                                menuCount.setText(String.valueOf(count));
                                String formattedPrice = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()) * count);
                                menuPrice.setText(formattedPrice);
                            }
                        }
                    }else {
                        if (count > 2) {
                            count--;
                            menuCount.setText(String.valueOf(count));
                            String formattedPrice = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()) * count);
                            menuPrice.setText(formattedPrice);
                        }
                    }
                } else {
                    if (count > 1) {
                        count--;
                        menuCount.setText(String.valueOf(count));
                        String formattedPrice = decimalFormat.format(Integer.parseInt(items.get(position).getPrice()) * count);
                        menuPrice.setText(formattedPrice);
                    }
                }
            }
        });

        btnOptionMinus = rootView.findViewById(R.id.btn_menu_option_minus);
        btnOptionMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(sub1MenuCount.getText().toString());
                if (count < 1){
                }else{
                    count--;
                    sub1MenuCount.setText(String.valueOf(count));

                }
            }
        });
        btnOptionPlus = rootView.findViewById(R.id.btn_menu_option_plus);
        btnOptionPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countOption1 = Integer.parseInt(sub1MenuCount.getText().toString());
                int countOption2 = Integer.parseInt(sub2MenuCount.getText().toString());
                int countOption3 = Integer.parseInt(sub3MenuCount.getText().toString());
                int countOption4 = Integer.parseInt(sub4MenuCount.getText().toString());
                int countMain = Integer.parseInt(menuCount.getText().toString());
                if (countOption1+countOption2+countOption3+countOption4 < countMain) {
                    countOption1++;
                    sub1MenuCount.setText(String.valueOf(countOption1));
                }

            }
        });

        btnOption2Minus = rootView.findViewById(R.id.btn_menu_option2_minus);
        btnOption2Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(sub2MenuCount.getText().toString());
                if (count < 1){
                }else{
                    count--;
                    sub2MenuCount.setText(String.valueOf(count));

                }
            }
        });
        btnOption2Plus = rootView.findViewById(R.id.btn_menu_option2_plus);
        btnOption2Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countOption1 = Integer.parseInt(sub1MenuCount.getText().toString());
                int countOption2 = Integer.parseInt(sub2MenuCount.getText().toString());
                int countOption3 = Integer.parseInt(sub3MenuCount.getText().toString());
                int countOption4 = Integer.parseInt(sub4MenuCount.getText().toString());
                int countMain = Integer.parseInt(menuCount.getText().toString());
                if (countOption1+countOption2+countOption3+countOption4 < countMain) {
                    countOption2++;
                    sub2MenuCount.setText(String.valueOf(countOption2));
                }

            }
        });

        btnOption3Minus = rootView.findViewById(R.id.btn_menu_option3_minus);
        btnOption3Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(sub3MenuCount.getText().toString());
                if (count < 1){
                }else{
                    count--;
                    sub3MenuCount.setText(String.valueOf(count));

                }
            }
        });
        btnOption3Plus = rootView.findViewById(R.id.btn_menu_option3_plus);
        btnOption3Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countOption1 = Integer.parseInt(sub1MenuCount.getText().toString());
                int countOption2 = Integer.parseInt(sub2MenuCount.getText().toString());
                int countOption3 = Integer.parseInt(sub3MenuCount.getText().toString());
                int countOption4 = Integer.parseInt(sub4MenuCount.getText().toString());
                int countMain = Integer.parseInt(menuCount.getText().toString());
                if (countOption1+countOption2+countOption3+countOption4 < countMain) {
                    countOption3++;
                    sub3MenuCount.setText(String.valueOf(countOption3));
                }

            }
        });

        btnOption4Minus = rootView.findViewById(R.id.btn_menu_option4_minus);
        btnOption4Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(sub4MenuCount.getText().toString());
                if (count < 1){
                }else{
                    count--;
                    sub4MenuCount.setText(String.valueOf(count));

                }
            }
        });
        btnOption4Plus = rootView.findViewById(R.id.btn_menu_option4_plus);
        btnOption4Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countOption1 = Integer.parseInt(sub1MenuCount.getText().toString());
                int countOption2 = Integer.parseInt(sub2MenuCount.getText().toString());
                int countOption3 = Integer.parseInt(sub3MenuCount.getText().toString());
                int countOption4 = Integer.parseInt(sub4MenuCount.getText().toString());
                int countMain = Integer.parseInt(menuCount.getText().toString());
                if (countOption1+countOption2+countOption3+countOption4 < countMain) {
                    countOption4++;
                    sub4MenuCount.setText(String.valueOf(countOption4));
                }

            }
        });

        btnAddCart = rootView.findViewById(R.id.btn_add_cart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = "";
                if (items.get(position).getInfo().contains("맵기단계")){
                    if (radioButton1.isChecked()){
                        option = "(1단계)";
                    }else if (radioButton2.isChecked()){
                        option = "(2단계)";
                    }else if (radioButton3.isChecked()){
                        option = "(3단계)";
                    }
                    ((MenuActivity) getActivity()).callOrderItem(
                            items.get(position).getName(),
                            String.valueOf(Integer.parseInt(items.get(position).getPrice())),
                            items.get(position).getPicurl(),
                            items.get(position).getCode(),
                            Integer.parseInt(menuCount.getText().toString()),
                            option
                    );
                }else {
                    if (items.get(position).getName().contains("한라원특선 A")){
                        int countOption1 = Integer.parseInt(sub1MenuCount.getText().toString());
                        int countOption2 = Integer.parseInt(sub2MenuCount.getText().toString());
                        int countOption3 = Integer.parseInt(sub3MenuCount.getText().toString());
                        int countOption4 = Integer.parseInt(sub4MenuCount.getText().toString());
                        int countMain = Integer.parseInt(menuCount.getText().toString());
                        if (countMain != countOption1 + countOption2 + countOption3 + countOption4){
                            Toast toast = Toast.makeText(getContext(), "옵션 수량이 다릅니다.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else{
                            String strOption1 = "";
//                            if (radioButton1.isChecked()){
//                                strOption1 = "\n    된장찌개 \n    물냉면 : "+countOption1+"\n    비빔냉면 : "+countOption2;
//
//                            }else if (radioButton2.isChecked()){
//                                strOption1 = "\n    김치찌개 \n    물냉면 : "+countOption1+"\n    비빔냉면 : "+countOption2;
//                            }
                            if (countOption1 > 0){
                                strOption1 = strOption1 + "\n  물  냉  면 : "+countOption1;
                            }
                            if (countOption2 > 0){
                                strOption1 = strOption1 + "\n  비빔냉면 : "+countOption2;
                            }
                            if (countOption3 > 0){
                                strOption1 = strOption1 + "\n  김치찌개 : "+countOption3;
                            }
                            if (countOption4 > 0){
                                strOption1 = strOption1 + "\n  된장찌개 : "+countOption4;
                            }


                            ((MenuActivity) getActivity()).callOrderItem(
                                    items.get(position).getName()+strOption1,
                                    String.valueOf(Integer.parseInt(items.get(position).getPrice())),
                                    items.get(position).getPicurl(),
                                    items.get(position).getCode(),
                                    Integer.parseInt(menuCount.getText().toString()),
                                    option
                            );
                        }
                    }else if (items.get(position).getName().contains("한라원특선 B")) {
                        int countOption1 = Integer.parseInt(sub1MenuCount.getText().toString());
                        int countOption2 = Integer.parseInt(sub2MenuCount.getText().toString());
                        int countMain = Integer.parseInt(menuCount.getText().toString());
                        if (countMain-1 != countOption1 + countOption2) {

                        } else {
                            String strOption1 = "";
                            if (radioButton1.isChecked()) {
                                strOption1 = "\n    된장찌개";

                            } else if (radioButton2.isChecked()) {
                                strOption1 = "\n    김치찌개";
                            }


                            ((MenuActivity) getActivity()).callOrderItem(
                                    items.get(position).getName(),
                                    String.valueOf(Integer.parseInt(items.get(position).getPrice())),
                                    items.get(position).getPicurl(),
                                    items.get(position).getCode(),
                                    Integer.parseInt(menuCount.getText().toString()),
                                    option
                            );
                        }
                    } else {
                        ((MenuActivity) getActivity()).callOrderItem(
                                items.get(position).getName(),
                                String.valueOf(Integer.parseInt(items.get(position).getPrice())),
                                items.get(position).getPicurl(),
                                items.get(position).getCode(),
                                Integer.parseInt(menuCount.getText().toString()),
                                option
                        );
                    }
                }
            }

        });

        return rootView;
    }
}
