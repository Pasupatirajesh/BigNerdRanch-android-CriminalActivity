package vid.criminalintent;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.Context;
import java.util.UUID;

public class CriminalActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID="com.bignerdranch.android.criminalintent.crime_id";

   @Override
    protected Fragment createFragment()
    {
        UUID crimeID = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        return CrimeFragment.newInstance(crimeID);
    }

    public static Intent newIntent(Context packageContext,UUID crimeId)
    {
        Intent intent = new Intent(packageContext,CriminalActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

}

