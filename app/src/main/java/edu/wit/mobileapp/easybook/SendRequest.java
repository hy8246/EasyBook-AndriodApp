package edu.wit.mobileapp.easybook;

/**
 * Created by haopan on 3/29/17.
 */
//parse.json implementation
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public class SendRequest extends AsyncTask<String, Void, String>{

    private Context context;
    HttpURLConnection connection = null;
    String result;
    URL url=null;
    BufferedReader reader;
    String urlString;
    ProgressDialog run_status;

    public SendRequest(Context context, String url_makeup)
    {
        this.context = context;
        urlString = "http://www.easybook2017.com/andriod"+url_makeup;
        result = "Not Set";

    }

    @Override
    protected void onPreExecute()
    {
        run_status= ProgressDialog.show(context,"WebRequesting","One Moment");
        run_status.setCanceledOnTouchOutside(false);
    }



    @Override
    protected String doInBackground(String...param)
    {

        try{
            Log.v("Completed Url",urlString);
            url = new URL(urlString);
            Log.v("webrequest","url setup");

            connection= (HttpURLConnection) url.openConnection();

            Log.v("webrequest","url after setup");

            int status = connection.getResponseCode();
            Log.v("Webrequest status",""+status);
            Log.v("webrequest","urlconnection");

            if(status == 200)
            {
                InputStream stream = connection.getInputStream();
                Log.v("webrequest", "get the input stream");
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                Log.v("webrequest", "buffer set up");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    Log.v("webrequest", "Line content: " + line);
                    buffer.append(line);
                }
                result = buffer.toString();
            }

            Log.i("result in DIBG",result);


            Log.v("webrequest",""+result);
        }
        catch (MalformedURLException e)
        {
            Log.v("webrequest Exception",e.getMessage());
        }
        catch (IOException e)
        {
            Log.v("webrequest Exception","IOException");
        }
        finally
        {
            Log.v("webrequest","start the ending process");
            if(connection!= null)
            {
                connection.disconnect();
                Log.v("webrequest","disconnect connection");
            }
            try
            {
                if(reader != null)
                {
                    reader.close();
                    Log.v("webrequest","close reader");
                }
            }
            catch (IOException e)
            {

            }
        }
        return result;
    }
    @Override
    protected void onPostExecute(String data)
    {
        super.onPostExecute(data);
        result = data.toString();
        run_status.dismiss();

    }

    public String getResult(){
        try{
            result = this.get();

        }catch (Exception e){

        }
        return result;
    }



}



