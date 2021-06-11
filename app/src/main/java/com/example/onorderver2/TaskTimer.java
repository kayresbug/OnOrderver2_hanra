package com.example.onorderver2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class TaskTimer extends AsyncTask<String, String, String> {

    private static final String RESULT_SUCCESS = "1";
    private static final String RESULT_FAIL = "0";

    private static final int TEXT_COLOR_NORMAL = 0xFF000000;
    private static final int TEXT_COLOR_FINISHED = 0xFFFF0000;

    private Context context;
    private int time = 1;

    public void setTime(int time, Context context) {
        this.time = time;
        this.context = context;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    protected String doInBackground(String... strings) {
        while (time > 0) {
            try {
                Thread.sleep(1000);
                time--;
                publishProgress();
            } catch (InterruptedException e) {
                return RESULT_FAIL;
            }
        }

        return RESULT_SUCCESS;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if (RESULT_SUCCESS.equals(result)) {
            ((MenuActivity)context).event();
            Intent intent = new Intent(context, ADPopupActivity.class);
            context.startActivity(intent);
        }
//        super.onPostExecute(result);
    }
}
