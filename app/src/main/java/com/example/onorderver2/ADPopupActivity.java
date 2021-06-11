package com.example.onorderver2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ADPopupActivity extends AppCompatActivity {

    LinearLayout popupLayout;
    ImageView popupImage;
    private Download download;

    boolean isTrue = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_popup);
        download = new Download();
        download.start();
        Log.d("ADADADAD", "onCreate: ");
        popupLayout = findViewById(R.id.layout_ad_pop);
        popupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTrue = false;
                finish();
            }
        });

        popupImage = findViewById(R.id.image_ad);
        popupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTrue = false;
                finish();
            }
        });
    }

    public class Download extends Thread {
        int count = 0;

        @Override
        public void run() {
            while (isTrue) {
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (count == 15) {
                    (ADPopupActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            popupImage.setImageResource(R.drawable.shhl_ad1);
                        }
                    });
                } else if (count == 25) {
                    (ADPopupActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            popupImage.setImageResource(R.drawable.shhl_ad2);
                        }
                    });
                } else if (count == 35) {
                    count = 0;
                    (ADPopupActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            popupImage.setImageResource(R.drawable.shhl_ad3);
                        }
                    });
                }
            }
        }
    }
}
