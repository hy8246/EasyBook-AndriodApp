package edu.wit.mobileapp.easybook;

/**
 * Created by haopan on 1/31/17.
 */
public class SearchItem {
    String bookName;
    String authorName;
    boolean avaiable;

    public SearchItem(String bookName, String authorName, boolean avaiable)
    {
        this.bookName=bookName;
        this.authorName=authorName;
        this.avaiable = avaiable;
    }
    public String getBookName()
    {
        return this.bookName;
    }
    public String getAurthorName()
    {
        return this.authorName;
    }
    public boolean getAvaiability(){ return this.avaiable;}
}
