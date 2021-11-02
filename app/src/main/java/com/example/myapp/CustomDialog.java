package com.example.myapp;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.net.sip.SipSession;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Lifecycle;

import java.security.acl.Group;
import java.util.ArrayList;


public class CustomDialog extends Activity {
    private static ArrayList<Contact_Model> arrayList;

    TextView tv_client;
    String phone_no;
    String contentName;
    ImageButton dialog_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        new LoadContacts().execute();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setFinishOnTouchOutside(false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_custom_dialog);
            initializeContent();

            dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog.this.finish();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }


    }

    private void initializeContent() {
        tv_client = findViewById(R.id.tv_client);
        dialog_ok = findViewById(R.id.imageButton);
        View view = findViewById(R.id.root);


    }


    private class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            arrayList = new ArrayList<>();
            arrayList = readContacts();// Get contacts array list from this
            // method
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            Log.i("numm", arrayList.get(0).getContactNumber().replace("-", ""));


            phone_no = getIntent().getExtras().getString("phone_no");

            for (int i = 0; i < arrayList.size(); i++) {

                if (phone_no.equals(arrayList.get(i).getContactNumber().replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim())) {
                    contentName = arrayList.get(i).getContactName();
                    Log.i("impo", arrayList.get(i).getContactNumber());
                    Log.i("impo", arrayList.get(i).getContactName());
                    Log.i("impo", phone_no);
                    tv_client.setText("" + contentName + " is calling you");
                    return;

                } else {
                    tv_client.setText("" + phone_no + " is calling you");
                }
            }
        }
    }

    public ArrayList<Contact_Model> readContacts() {
        ArrayList<Contact_Model> contactList = new ArrayList<Contact_Model>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC "); // Return

        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
                // data of
                // contacts
                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);// Retrun data cusror represntative to
                // contact ID

                // Strings to get all details
                String displayName = "";
                String nickName = "";
                String mobilePhone = "";


                // Now start the cusrsor
                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// get
                    // the
                    // contact
                    // name
                    do {
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                            nickName = dataCursor.getString(dataCursor
                                    .getColumnIndex("data1")); // Get Nick Name

                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    break;

                            }
                        }


                    } while (dataCursor.moveToNext()); // Now move to next
                    // cursor

                    contactList.add(new Contact_Model(
                            displayName, mobilePhone));// Finally add
                    // items to
                    // array list
                }

            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }
}