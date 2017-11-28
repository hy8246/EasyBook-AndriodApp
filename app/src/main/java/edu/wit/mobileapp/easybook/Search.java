package edu.wit.mobileapp.easybook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

/**
 * Created by haopan on 1/30/17.
 */
public class Search extends AppCompatActivity {
    EditText bookName, aurthorName;
    String iBookName, iAurthorName,email;
    Button search_btn,menu_btn;
    Intent intent;
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.search_page);
        intent= getIntent();
        email = intent.getStringExtra("email_id");
        search_btn=(Button)findViewById(R.id.search_search_btn);
        menu_btn=(Button)findViewById(R.id.search_menu_btn);
        menu_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                intent= new Intent();
                intent.putExtra("email_id",email);
                intent.setClass(Search.this,Menu.class);
                startActivity(intent);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookName = (EditText)findViewById(R.id.search_book_name);
                aurthorName =(EditText)findViewById(R.id.search_book_author);

                iBookName = bookName.getText().toString();
                iAurthorName = aurthorName.getText().toString();

                Log.v("myApp",""+TextUtils.isEmpty(iBookName));
                Log.v("myApp",""+TextUtils.isEmpty(iAurthorName));
                if(!((TextUtils.isEmpty(iBookName)) && (TextUtils.isEmpty(iAurthorName))))
                {
                    intent = new Intent();
                    intent.setClass(Search.this,Search_result.class);
                    intent.putExtra("email_id",email);
                    //put in the name
                    if(!iBookName.equals(""))
                    {
                        intent.putExtra("SearchBookName",iBookName);
                    }
                    if(!iAurthorName.equals(""))
                    {
                        intent.putExtra("SearchAuthorName",iAurthorName);
                    }
                    startActivity(intent);
                }
                else
                {
                    bookName.setError("Name and Author can't be both empty");
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


}
