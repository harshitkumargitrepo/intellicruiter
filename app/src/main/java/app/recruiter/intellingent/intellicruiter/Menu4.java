package app.recruiter.intellingent.intellicruiter;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class Menu4 extends Fragment {
    //Background Animation
    RelativeLayout HomeFragment;
    AnimationDrawable fragmentAnimationDrawable;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_menu_2, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 3");
        HomeFragment = (RelativeLayout) view;
        fragmentAnimationDrawable = (AnimationDrawable) HomeFragment.getBackground();
        fragmentAnimationDrawable.setEnterFadeDuration(2500);
        fragmentAnimationDrawable.setExitFadeDuration(2500);
        fragmentAnimationDrawable.start();
        
    }
}