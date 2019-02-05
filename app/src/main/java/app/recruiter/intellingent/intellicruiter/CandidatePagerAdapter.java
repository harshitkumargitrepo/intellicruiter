package app.recruiter.intellingent.intellicruiter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CandidatePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CandidatePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CandidateHomeTabFragment tab1 = new CandidateHomeTabFragment();
                return tab1;
            case 1:
                CandidateJobTabFragment tab2 = new CandidateJobTabFragment();
                return tab2;
            case 2:
                CandidateInterviewTabFragment tab3 = new CandidateInterviewTabFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}