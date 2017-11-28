package edu.wit.mobileapp.easybook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by haopan on 2/2/17.
 */

public class Search_result extends AppCompatActivity {
    Button back_btn, new_search_btn;
    ListView result_tb;
    Intent intent;
    SearchItemAdapter adapter;
    String email;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_page);
        intent = getIntent();
        String bookname = intent.getStringExtra("SearchBookName");
        String authorName = intent.getStringExtra("SearchAuthorName");
        email= intent.getStringExtra("email_id");
        String follow_url="/search?bookname=";
        ArrayList<SearchItem> searchResult= new ArrayList<>();

        if(bookname!= null)
        {
            bookname = bookname.toLowerCase().replace(" ", "%20");
            follow_url= follow_url+bookname;
        }
        follow_url = follow_url+"&authorname=";
        if(authorName!= null)
        {

            authorName = authorName.toLowerCase().replace(" ", "%20");
            follow_url = follow_url+authorName;
        }
        try {

        SendRequest search_request = new SendRequest(Search_result.this, follow_url);
        search_request.execute();
        String result = search_request.getResult();

        Log.v("Check Result",result);
        String fixresult = result.replace("\\","");
        fixresult = fixresult.substring(1,fixresult.length());
        Log.v("fixresult: ",fixresult+"");




            JSONArray con = new JSONArray(fixresult);
            int length = con.length();
            //            Log.v("check JSon Array",con.toString());
            for(int i = 0; i<length; i++)
            {

                JSONObject jsonObj = con.getJSONObject(i);
                String input_bookname = jsonObj.getString("bookname");
                Log.v("Check Book Name",input_bookname);
                String input_authorname = jsonObj.getString("author");
                Log.v("Check Author Name",input_authorname);
                Boolean input_availability = jsonObj.getBoolean("status");
                Log.v("Check status",input_availability.toString());
                searchResult.add(new SearchItem(input_bookname,input_authorname,input_availability));

            }

        }catch (JSONException e)
        {
            Log.v("Error:",e.getMessage());
        }


        adapter = new SearchItemAdapter(this,0,searchResult);
        result_tb=(ListView)findViewById(R.id.result_view);
        result_tb.setAdapter(adapter);

        back_btn=(Button)findViewById(R.id.result_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent();
                intent.putExtra("email_id",email);
                intent.setClass(Search_result.this,Menu.class);
                startActivity(intent);
            }
        });

        new_search_btn=(Button)findViewById(R.id.new_search_btn);
        new_search_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent();
                intent.putExtra("email_id",email);
                intent.setClass(Search_result.this,Search.class);
                startActivity(intent);
            }
        });


    }

}
