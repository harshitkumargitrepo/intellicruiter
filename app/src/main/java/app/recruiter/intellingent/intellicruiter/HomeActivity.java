package app.recruiter.intellingent.intellicruiter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("HARSHIT :","HomeActivity :: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    // Stay on the Same screen
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }
}
