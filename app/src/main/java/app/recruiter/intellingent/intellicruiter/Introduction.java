package app.recruiter.intellingent.intellicruiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;

public class Introduction extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Down here, we add the xml layouts into sample slide inflater.
        addSlide(OnboardingSlide.newInstance(R.layout.introduction_slide_1));
        addSlide(OnboardingSlide.newInstance(R.layout.introduction_slide_2));
        addSlide(OnboardingSlide.newInstance(R.layout.introduction_slide_3));
        addSlide(OnboardingSlide.newInstance(R.layout.introduction_slide_4));

        showStatusBar(true);

        setDepthAnimation();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadSignInActivity();
        Log.i("HARSHIT :","onDonePressed");
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadSignInActivity();
        Log.i("HARSHIT :","onSkipPressed");
    }


    private void loadSignInActivity(){
        Log.i("HARSHIT :","loadSignInActivity");
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void getStarted(View v){
        Log.i("HARSHIT :","getStarted");
        loadSignInActivity();
    }
}