package edu.wit.mobileapp.easybook;

import java.util.Date;

/**
 * Created by haopan on 3/3/17.
 */

public class RecordItem {



    String name;
    String return_day;
    public RecordItem(String name, String return_day)
    {
        this.name = name;
        this.return_day= return_day;
    }
    public String getBookName()
    {
        return this.name;
    }
    public String getDay_to_return()
    {
        return this.return_day.toString();
    }
}
