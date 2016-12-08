package ssjk.cafein;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 장소영 on 2016-11-04.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splash);

        try {
            //2초간 대기
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //메인으로 넘어가기
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
