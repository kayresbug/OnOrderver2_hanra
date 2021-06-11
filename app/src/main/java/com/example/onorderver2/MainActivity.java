package com.example.onorderver2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;

    TextView btnRecyclerView, btnRecyclerViewTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecyclerViewTest = findViewById(R.id.recyclerview_test);
        btnRecyclerViewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
