package edu.wit.mobileapp.easybook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by haopan on 1/31/17.
 */
public class Register extends AppCompatActivity {

Button register_btn, register_menu_btn;
EditText email, password, confirmPW, FName, LName, phone, iD;
String i_email, i_password, i_confirmPW, i_FName, i_LName, i_phone,i_id,i_gender;
RadioGroup gender;
    RadioButton selected_gender;
Intent intent;
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.register_page);
        intent = getIntent();
        register_btn=(Button)findViewById(R.id.register_btn);

        email=(EditText)findViewById(R.id.reg_email);

        password=(EditText)findViewById(R.id.reg_PW);

        confirmPW=(EditText)findViewById(R.id.reg_CPW);

        FName=(EditText)findViewById(R.id.reg_FN);

        LName=(EditText)findViewById(R.id.reg_LN);

        gender= (RadioGroup)findViewById(R.id.reg_gender);

        phone=(EditText)findViewById(R.id.reg_phone);

        iD = (EditText)findViewById(R.id.reg_iid);

        register_menu_btn=(Button)findViewById(R.id.register_return_btn);

        register_menu_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                intent = new Intent();
                intent.setClass(Register.this, WelcomeScreen.class);
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean ready=true;
                i_LName=LName.getText().toString();
                i_FName=FName.getText().toString();
                i_password=password.getText().toString();
                i_confirmPW=confirmPW.getText().toString();
                i_email=email.getText().toString();
                i_email.toLowerCase();
                i_id= iD.getText().toString();
                int selected_id = gender.getCheckedRadioButtonId();
                selected_gender = (RadioButton)findViewById(selected_id);
                try {
                    i_gender = selected_gender.getText().toString();
                }catch (Exception e)
                {
                    Toast.makeText(Register.this, "Please Select the Gender",Toast.LENGTH_SHORT);
                }
//                Log.v("Check Gender",i_gender);
                if(TextUtils.isEmpty(i_phone))
                {
                    i_phone="0000000000";
                }
                else
                {
                    i_phone = phone.getText().toString();
                }

                if(!(i_password.equals(i_confirmPW)))
                {
                    ready=false;
                }
                if(i_password.length()<6)
                {
                    ready=false;
                    Toast.makeText(Register.this,"Password Required 6 digit with Letter and number",Toast.LENGTH_SHORT);
                }
                else
                {
                   if(Pattern.matches("[a_zA_Z]",i_password))
                   {
                       ready=false;
                       Toast.makeText(Register.this,"Need to be letter with number",Toast.LENGTH_SHORT);
                   }
                    else
                   {

                   }
                }

                    Log.v("Check Ready",ready+"");
                if (ready)
                {
                    String follow_url = "/signup?email="+i_email+"&password="+i_password
                            +"&firstname="+i_FName+"&lastname="+i_LName+"&gender="+i_gender+"&iid="+i_id
                            +"&phone="+i_phone;
                    RegisterRequest signup_request = new RegisterRequest(Register.this,follow_url);
                    signup_request.execute();
                    String result_code = signup_request.getResult();
                    Log.v("result_code",result_code);


                    if(result_code.equals("0")) {
                        Toast.makeText(Register.this,"Register Successful",
                                Toast.LENGTH_LONG).show();
                        intent = new Intent(getBaseContext(), Register.class);
                        intent.setClass(Register.this, WelcomeScreen.class);
                        startActivity(intent);
                    }
                    else if (result_code.equals("1"))
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
// set title
                        alertDialogBuilder.setTitle("Error");
// set dialog message
                        alertDialogBuilder.setMessage("The E-mail has registered.");

                        alertDialogBuilder.setPositiveButton("Close", dialogClickListener );
// create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
// show it
                        alertDialog.show();
                    }
                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
// set title
                    alertDialogBuilder.setTitle("Error");
// set dialog message
                    alertDialogBuilder.setMessage("Please fill all the require field");
                    alertDialogBuilder.setPositiveButton("Close", dialogClickListener );
// create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
// show it
                    alertDialog.show();

                }


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

    public class RegisterRequest extends AsyncTask<String, Void, String> {

        private Context context;
        HttpURLConnection connection = null;
        String result;
        URL url=null;
        BufferedReader reader;
        String urlString;
        ProgressDialog run_status;

        public RegisterRequest(Context context, String url_makeup)
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

                Log.v("webrequest Exception","IOException: "+e.getMessage());
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

}
