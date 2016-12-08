package ssjk.cafein;

/**
 * Created by wqe13 on 2016-11-23.
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.support.v7.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;
import android.widget.Toast;

import java.util.Arrays;


public class BeginSearchActivity extends AppCompatActivity{

    SQLiteDatabase cafedb;
    public String select;
    public Cursor cursor;
    ListView layout;
    CustomListAdapter m_Adapter;
    public SearchView searchView;
    SuggestionsDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        layout = (ListView) findViewById(R.id.searchList);
        m_Adapter = new CustomListAdapter();
        layout.setAdapter(m_Adapter);
        database = new SuggestionsDatabase(this);
        cafedb = openOrCreateDatabase("cafesd1.db", Context.MODE_PRIVATE, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {

                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Toast.makeText(BeginSearchActivity.this,"onSuggestionClick",Toast.LENGTH_LONG).show();
                SQLiteCursor cursor = (SQLiteCursor) searchView.getSuggestionsAdapter().getItem(position);
                int indexColumnSuggestion = cursor.getColumnIndex( SuggestionsDatabase.FIELD_SUGGESTION);
                //searchView.setQuery(cursor.getString(indexColumnSuggestion), false);
                getsearch(cursor.getString(indexColumnSuggestion));

                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String inquery) {
                getsearch(inquery);
                //customAdapter.swapCursor(cursor);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String innewText) {
                String newText = innewText.toUpperCase();
                Cursor cursor = database.getSuggestions(newText);
                if(cursor.getCount() != 0)
                {
                    String[] columns = new String[] {SuggestionsDatabase.FIELD_SUGGESTION, SuggestionsDatabase.FIELD_TYPE };
                    int[] columnTextId = new int[] { android.R.id.text1, android.R.id.text2};

                    SuggestionSimpleCursorAdapter simple = new SuggestionSimpleCursorAdapter(getBaseContext(),
                            android.R.layout.simple_list_item_2, cursor,
                            columns , columnTextId
                            , 0);

                    searchView.setSuggestionsAdapter(simple);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });

        return true;
    }

    public void getsearchEnd(String str1, String str2){
        Intent intent = new Intent(BeginSearchActivity.this, EndSearchActivity.class);
        intent.putExtra("Name", str1);
        intent.putExtra("Type" + 0, str2);
        Log.i("-------------", str2);
        if (j != 0){
            intent.putExtra("Number", j);
            for (int i = 0; i < j; i++){
                intent.putExtra("Type" + i+1, strarray[i]);
            }
        }
        startActivity(intent);
    }

    public void getsearch(String str){
        String query = str.toUpperCase();
        String type = null;
        select = String.format("SELECT * FROM Cafein WHERE Cafe_Name='%s' and Drink_Kind = 'Cafe_Info'", query);
        cursor = cafedb.rawQuery(select, null);
        cursor.moveToFirst();
        String secondtype = null;
        if (cursor.getCount() == 0) {
            select = String.format("SELECT * FROM Cafein WHERE Drink_Name ='%s'", query);
            cursor = cafedb.rawQuery(select, null);
            cursor.moveToFirst();
            if(cursor.getCount() != 0) {
                type = cursor.getString(9);
                Log.i("-------type",type);
                String s = String.format("SELECT * FROM Cafein WHERE Drink_Name='%s' and Drink_Kind_app != '%s'", query, type);
                Cursor c = cafedb.rawQuery(s, null);
                int ccount = c.getCount();
                if (ccount != 0) {
                    c.moveToFirst();
                    int morethanone = ccount;
                    strarray = new String[ccount];
                    for (int i = 0; i < ccount; i++, c.moveToNext()) {
                        if(Arrays.asList(strarray).contains(c.getString(9)) == false) {
                            strarray[j] = c.getString(9);
                            j++;
                        }
                    }
                }
                else{
                    strarray = null;
                    j = 0;
                }
            }
        }else {
            type = "CAFE";
        }
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(BeginSearchActivity.this, "No records found!", Toast.LENGTH_LONG).show();
        } else {
            if (type.equals("CAFE")){
                //m_Adapter.add(cursor.getString(1), type);
                getsearchEnd(cursor.getString(1), type);
            }
            else {
                Toast.makeText(BeginSearchActivity.this, j+1 +"Search found!", Toast.LENGTH_LONG).show();
                //m_Adapter.add(cursor.getString(3), type);
                getsearchEnd(cursor.getString(3), type);
            }
        }
    }

    public int j = 0;
    private String[] strarray;
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
