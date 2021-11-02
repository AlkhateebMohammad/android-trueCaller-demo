package com.example.myapp;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.app.Activity;
import android.widget.TextView;

import com.example.myapp.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static ArrayList<Contact_Model> arrayList;

    TextView tv_client;
    String contentName = "";
    String phone_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new LoadContacts().execute();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | PixelFormat.TRANSLUCENT
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);


        //phone_no = getIntent().getStringExtra("phone_no");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        // Log.i("impo", phone_no);

                        //  tv_client.setText(phone_no);

                        popup();
                    }
                },
                2000);


        // Log.i("impo", phone_no);
    }


    void popup() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.activity_custom_dialog, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


        ImageButton btnDismiss = popupView.findViewById(R.id.imageButton);

        tv_client = popupView.findViewById(R.id.tv_client);


        Log.i("impo", String.valueOf(arrayList.size()));
        //Log.i("impo", phone_no);
        //

        for (int i = 0; i < arrayList.size(); i++) {

            if (phone_no.equals(arrayList.get(i).getContactNumber().replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim())) {
                contentName = arrayList.get(i).getContactName();
                Log.i("impo", arrayList.get(i).getContactNumber());
                Log.i("impo", arrayList.get(i).getContactName());
                Log.i("impo", phone_no);
                tv_client.setText("" + contentName + " is calling you");


            }

        }

        if (tv_client.getText().equals("")) {
            tv_client.setText(phone_no + " is calling you");
        }


        //tv_client.setText(phone_no + arrayList.get(0).getContactName());

        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                System.exit(0);
            }
        });


        popupWindow.showAtLocation(popupView, 0, 0, 800);

        popupView.setOnTouchListener(new OnTouchListener() {
            int orgX, orgY;
            int offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        orgX = (int) event.getX();
                        orgY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX = (int) event.getRawX() - orgX;
                        offsetY = (int) event.getRawY() - orgY;
                        popupWindow.update(offsetX, offsetY, -1, -1, true);
                        break;
                }
                return true;
            }
        });
    }

    private class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            arrayList = new ArrayList<>();
            arrayList = utils.readContacts(MainActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (getIntent().getStringExtra("phone_no") != null) {
                phone_no = getIntent().getStringExtra("phone_no");
            } else {
                phone_no = "Welcome";
            }
            //popup();
            super.onPostExecute(aVoid);
        }
    }

}
