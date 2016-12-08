package ssjk.cafein;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.List;


public class CafeInformation extends Activity {
    /** Called when the activity is first created. */

    private SynchronizedScrollView mScrollView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafeinformation);
        ImageView image = (ImageView) findViewById(R.id.cafepic);

        mScrollView = (SynchronizedScrollView)findViewById(R.id.scroll);

        mScrollView.setAnchorView(findViewById(R.id.anchor));
        mScrollView.setSynchronizedView(findViewById(R.id.header));
        String cafe_name = getIntent().getStringExtra("CafeName");
        String cafe_ename = getIntent().getStringExtra("ENGname").toLowerCase();

        try{
            // 'data/data'에 생성된 db파일 읽어오기
            SQLiteDatabase cafedb = openOrCreateDatabase("cafesd1.db", Context.MODE_PRIVATE, null);
            //MainActivity에서 호출된 카페의 이름과 동일한 db를 모두 불러온다
            String select = String.format("SELECT * FROM Cafein WHERE Cafe_Name='%s'", cafe_name);
            // 쿼리로 db의 커서 획득
            Cursor cur = cafedb.rawQuery(select,null);

            // 처음 레코드로 이동
            cur.moveToFirst();

            // 읽은값 출력
            TextView C_info = (TextView)findViewById(R.id.cafeName);
            C_info.setText(cur.getString(1));

            C_info = (TextView)findViewById(R.id.Note);
            C_info.setText(cur.getString(6));
            C_info = (TextView)findViewById(R.id.Phone_Num);
            C_info.setText(cur.getString(7));
            C_info = (TextView)findViewById(R.id.operHours);
            C_info.setText(cur.getString(8));
            int resID = getResources().getIdentifier(cafe_ename, "drawable", this.getPackageName());
            if(resID != 0) {
                Drawable dr = getResources().getDrawable(resID);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 340, 160, true));
                image.setImageDrawable(d);
            }
            printdrink(cafe_name, "COFFEE", cafedb);
            printdrink(cafe_name, "ICE BLENDED", cafedb);
            printdrink(cafe_name, "TEA", cafedb);
            printdrink(cafe_name, "BEVERAGE", cafedb);
            cafedb.close();


        }catch(Exception e){
            Log.i("_)",""+e.toString());
        }

    }

    public void printdrink (String cafe_name, String drink_kind, SQLiteDatabase cafedb){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        String select = String.format("SELECT * FROM Cafein WHERE Cafe_Name='%s' and Drink_Kind = '%s' ", cafe_name, drink_kind);
        Cursor cur = cafedb.rawQuery(select, null);
        int count = cur.getCount();
        if (count > 0) {
            cur.moveToFirst();

            LinearLayout layout = (LinearLayout) findViewById(R.id.drinklayout);

            TextView Drink_Kind = new TextView(this);
            Drink_Kind.setText(cur.getString(2));
            Drink_Kind.setTextSize(15);
            Drink_Kind.setGravity(Gravity.CENTER);
            Drink_Kind.setHeight(35*height/630);
            Drink_Kind.setTypeface(null, Typeface.BOLD);
            layout.addView(Drink_Kind);

            List<TextView> textList = new ArrayList<TextView>(count);
            for (int i = 0; i < count; i++) {
                LinearLayout innerlayout = new LinearLayout(this);
                innerlayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                innerlayout.setOrientation(LinearLayout.HORIZONTAL);
                layout.addView(innerlayout);

                TextView C_item = new TextView(this);
                C_item.setText(cur.getString(3));
                C_item.setTextSize(15);
                C_item.setGravity(Gravity.CENTER);
                C_item.setWidth(200*width/350);

                TextView C_Hprice = new TextView(this);
                C_Hprice.setText(cur.getString(4));
                C_Hprice.setTextSize(15);
                C_Hprice.setGravity(Gravity.CENTER);
                C_Hprice.setWidth(50*width/350);

                TextView C_Cprice = new TextView(this);
                C_Cprice.setText(cur.getString(5));
                C_Cprice.setTextSize(15);
                C_Cprice.setGravity(Gravity.CENTER);
                C_Cprice.setWidth(50*width/350);

                innerlayout.addView(C_item);
                textList.add(C_item);
                innerlayout.addView(C_Hprice);
                textList.add(C_Hprice);
                innerlayout.addView(C_Cprice);
                textList.add(C_Cprice);
                cur.moveToNext();
            }
        }

    }
}