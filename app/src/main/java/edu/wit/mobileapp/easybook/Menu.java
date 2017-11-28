package edu.wit.mobileapp.easybook;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.moshi.Json;

import android.content.Intent;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by haopan on 1/21/17.
 */
public class Menu extends AppCompatActivity {
    Button search_btn, rent_btn,logout_btn;
    ListView recordTable;
    Intent intent;
    private static RecordItemAdapter adapter;
    ArrayList<RecordItem> recordList;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        if(email==null) {
            email = getIntent().getStringExtra("email_id");
        }
        Log.v("Email Check",email+"");


        search_btn=(Button)findViewById(R.id.search_btn);
        rent_btn=(Button)findViewById(R.id.rent_btn);
        recordTable = (ListView)findViewById(R.id.record_tb);
        logout_btn = (Button)findViewById(R.id.logout_btn);
        //initiate the socket connection
        String follow_url = "/returnrentalrecord?email="+email;
        Log.v("url Chekc", follow_url);
        SendRequest rental_record_request = new SendRequest(Menu.this,follow_url);
        rental_record_request.execute();
        String result = rental_record_request.getResult().toString();
        Log.v("result check",result+"");
        String fixresult = result.replace("\\","");
        fixresult = fixresult.substring(1,fixresult.length());
        Log.v("fixresult: ",fixresult+"");


        recordList = new ArrayList<>();
        recordList.add(new RecordItem("Book Name","Due Date"));
        try {

                JSONArray con = new JSONArray(fixresult);
               int length = con.length();
    //            Log.v("check JSon Array",con.toString());
                for(int i = 0; i<length; i++)
                {
    //        recordList.add(new RecordItem("Apple","10/02/2017"));
    //        recordList.add(new RecordItem("Henry","03/20/2017"));
    //        recordList.add(new RecordItem("John","03/21/2017"));

                    JSONObject jsonObj = con.getJSONObject(i);
                    String bookname = jsonObj.getString("bookname");
                    String date = jsonObj.getString("date");
                    date = date.substring(0,19)+"Z";
                    Log.v("Check BookName", bookname+" and "+date);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    try
                    {
                        Date i_date = formatter.parse(date.replaceAll("Z$", "+0000"));
                        Calendar c = Calendar.getInstance();
                        c.setTime(i_date);
                        c.add(Calendar.DATE,30);
                        i_date = c.getTime();
                        String date_str= i_date.toString();
                        Log.v("Check BookName", bookname+" and "+date_str);
                        recordList.add(new RecordItem(bookname,i_date.toString().substring(0,14)));

                    }catch (Exception e)
                    {
                        Log.v("Error",e.getMessage());
                        Log.v("Error","increase date error");
                    }

                }

            }catch (JSONException e)
                {
                    Log.v("Error:",e.getMessage());
                }



        //set up the adapter and put the content into the list view
        adapter = new RecordItemAdapter(this,0, recordList);
        recordTable.setAdapter(adapter);

        search_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent();
                intent.putExtra("email",email);
                intent.setClass(Menu.this,Search.class);
                startActivity(intent);
            }
        });

        rent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(Menu.this);
                scanIntegrator.initiateScan();

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(Menu.this,WelcomeScreen.class);
                startActivity(intent);
            }
        });

    }



    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };



    String scanContent;
    String backBookName;
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
                scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                //consturct the request of the bookname
                final Intent received = intent;
                String follow_url = "/getBookName?serial="+scanContent;
                SendRequest bookname_request = new SendRequest(Menu.this,follow_url);
                bookname_request.execute();
                backBookName = bookname_request.getResult();

                //Check if the bookname exist
                if(!backBookName.equals("1"))
                {
                    //If the bookname is find
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu.this);
    // set title
                    alertDialogBuilder.setTitle("Information Read");
    // set dialog message
                    alertDialogBuilder.setMessage("Book Name: "+ backBookName).setCancelable(true);
                    alertDialogBuilder.setPositiveButton("Confrim to Rent", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id)
                        {
                            String follow_url = "/rent?email="+email+"&serial="+scanContent+"&bookname="+backBookName.replace(" ","%20");
                            SendRequest rent_conform_request = new SendRequest(Menu.this,follow_url);
                            rent_conform_request.execute();
                            String result_code = rent_conform_request.getResult();
                            Log.v("check rent status",""+result_code);
                            if(result_code.equals("0"))
                            {
                                Toast.makeText(Menu.this,"Rent Process Successful",Toast.LENGTH_SHORT).show();

                            }
                            else if(result_code.equals("1"))
                            {

                                Toast.makeText(Menu.this,"Rental Record Error, Please go see the staff",Toast.LENGTH_LONG).show();
                            }
                            else if(result_code.equals("2"))
                            {
                                Toast.makeText(Menu.this,"Book Rental Status Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", dialogClickListener);
    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
    // show it
                    alertDialog.show();
                }
                else
                {
                    //The book serial not found
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu.this);
                    alertDialogBuilder.setTitle("Book Not Exist");
                    alertDialogBuilder.setNegativeButton("OK",dialogClickListener);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}

