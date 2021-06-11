package com.example.onorderver2.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onorderver2.ItemDecoration;
import com.example.onorderver2.MenuActivity;
import com.example.onorderver2.MenuAdapter;
import com.example.onorderver2.R;
import com.example.onorderver2.model.MenuRecyclerItem;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private static String BASE_URL = "http://15.164.232.164:5000/";
    private static ArrayList<MenuRecyclerItem> items;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int position;

    private String mParam1;
    private String mParam2;

    SharedPreferences pref;
    String storeCode;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MenuAdapter adapter;
    View rootView;

    FragmentManager fragmentManager;

    MenuInfoFragment infoFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            items = (ArrayList<MenuRecyclerItem>) getArguments().get("items");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        fragmentManager = getFragmentManager();
        pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        storeCode = pref.getString("storecode", "");

        initViews(items);

//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                infoFragment = new MenuInfoFragment();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("items", items);
//                infoFragment.setArguments(bundle);
//                fragmentManager.beginTransaction().replace(R.id.fragment_menu, infoFragment).commit();
//                Log.d("items", "onCreate: " + bundle.getString("items"));
//            }
//        });

        return rootView;
    }

    private void initViews(ArrayList<MenuRecyclerItem> items) {
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = rootView.findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecoration(getActivity()));
        adapter = new MenuAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity)getContext()).timerReset();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ((MenuActivity)getActivity()).timerReset();
            }
        });
    }
}
