package vid.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;
/**
 * Created by SSubra27 on 10/9/15.
 */
public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks{

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crime_pager);
//        UUID crimeId=(UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
//        mViewPager=(ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
//
//       mCrimes= CrimeLab.get(this).getCrimes();
//       FragmentManager fragmentManager =getSupportFragmentManager(); // fragment manager is required for u to use the fragmentstatepageradapter which is an unamed instance here meaning not assgined to any variable
//        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
//            @Override
//            public Fragment getItem(int position) {
//                Crime crime = mCrimes.get(position);
//                return CrimeFragment.newInstance(crime.getId()); // Using Fragment arguments method to start an instance of the CrimeFragment
//            }
//
//            @Override
//            public int getCount() {
//                return mCrimes.size();
//            }
//        });
//        for (int i=0; i<mCrimes.size(); i++)
//        {
//            if(mCrimes.get(i).getId().equals(crimeId))
//            {
//                mViewPager.setCurrentItem(i);
//                break;
//            }
//        }

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_crime_pager);
            mViewPager=(ViewPager)  findViewById(R.id.activity_crime_pager_view_pager);
            UUID crimeId =(UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
            mCrimes=CrimeLab.get(this).getCrimes();
            FragmentManager fragmentManager = getSupportFragmentManager();
            mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
                @Override
                public Fragment getItem(int position) {
                   Crime crime =  mCrimes.get(position);
                    return CrimeFragment.newInstance(crime.getId());
                }

                @Override
                public int getCount() {
                    return mCrimes.size();
                }
            });
            for(int i=0; i<mCrimes.size();i++)
            {
                if(mCrimes.get(i).getId().equals(crimeId))
                {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    @Override
    public void onCrimeUpdated(Crime crime)
    {

    }


}
