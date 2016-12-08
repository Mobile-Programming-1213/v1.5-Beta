package ssjk.cafein;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;

/**
 * Created by wqe13 on 2016-11-24.
 */

public class SuggestionsDatabase{

    public final static String TABLE_SUGGESTION = "TABLE_SUGGESTION";
    public final static String FIELD_ID = "_id";
    public final static String FIELD_SUGGESTION = "FIELD_SUGGESTION";
    public final static String FIELD_TYPE = "FIELD_TYPE";
    private static String DB_PATH = "/data/data/ssjk.cafein/databases/";

    private static String DB_NAME = "cafesd1.db";

    SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);

    public SuggestionsDatabase(Context context){

    }
/*
    public long insertSuggestion(String text)
    {
        ContentValues values = new ContentValues();
        values.put(FIELD_SUGGESTION, text);
        return db.insert(TABLE_SUGGESTION, null, values);
    }
*/
    public Cursor getSuggestions(String text)
    {
        return db.query(TABLE_SUGGESTION, new String[] {FIELD_ID, FIELD_TYPE, FIELD_SUGGESTION},
                FIELD_SUGGESTION+" LIKE '"+ text +"%'", null, null, null, null);
    }


}