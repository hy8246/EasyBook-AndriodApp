package edu.wit.mobileapp.easybook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;

public class WelcomeScreen extends AppCompatActivity {
Intent intent;
String userEMail, password;
Button loginBtn, registerBtn;
EditText userName,inputPassword;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        intent = getIntent();
        loginBtn = (Button)findViewById(R.id.login_btn);
        registerBtn = (Button)findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent.setClass(WelcomeScreen.this,Register.class);
                startActivity(intent);
            }
        });
        //Edit dialogOnClickListerner
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
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent = new Intent();
                intent.setClass(WelcomeScreen.this, Register.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userName=(EditText)findViewById(R.id.user_email);
                inputPassword=(EditText)findViewById(R.id.user_password);
                userEMail=userName.getText().toString();
                password=inputPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(userEMail.length()!=0 && password.length()!=0 && userEMail.matches(emailPattern)) {
                    //Webrequest for the authenticated
                    Log.v("CheckNetwork","Network Connection Started");
                    //constructe a url of the parameter;
                    String url_follow = "/signin?email="+userEMail+"&password="+password;
                    Log.v("URL follow",url_follow);
                    Log.v("check email&pw",userEMail+" "+password);
//                    intent.setClass(WelcomeScreen.this,Menu.class);
//                    startActivity(intent);
                    intent.putExtra("email_id",userEMail+"");
                    WebRequest connection = new WebRequest(WelcomeScreen.this,url_follow,userEMail);
                    connection.execute();


                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WelcomeScreen.this);
// set title
                    alertDialogBuilder.setTitle("Error");
// set dialog message
                    alertDialogBuilder.setMessage("Please input valid log in information").setCancelable(true);
                    alertDialogBuilder.setPositiveButton("Close", dialogClickListener );
// create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
// show it
                    alertDialog.show();

                }
            }
        });

    }
}


class WebRequest extends AsyncTask<String, Void, String> {

    private Context context;
    HttpURLConnection connection = null;
    String result;
    URL url=null;
    BufferedReader reader;
    String urlString;
    ProgressDialog run_status;
    private String email;

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
    //Contructor
    public WebRequest(Context context, String url_makeup,String email)
    {
        this.context = context;
        urlString = "http://www.easybook2017.com/andriod"+url_makeup;
        result = "Not Set";
        this.email= email;

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
            Log.v("Error Message",e.getMessage());
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

        if(result.equals("0"))
        {
            //if authenticated get to menu page
            Intent intent = new Intent(context,Menu.class);
            intent.putExtra("email_id",email);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
        //else output dialog
        else if(result.equals("1"))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Error");
            // set dialog message
            alertDialogBuilder.setMessage("E-mail doesn't exist").setCancelable(true);
            alertDialogBuilder.setPositiveButton("Close", dialogClickListener );
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
        else if(result.equals("2"))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Error");
            // set dialog message
            alertDialogBuilder.setMessage("Password Incorrect").setCancelable(true);
            alertDialogBuilder.setPositiveButton("Close", dialogClickListener );
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }


    }



}
