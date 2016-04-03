package vid.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
/**
 * Created by SSubra27 on 9/30/15.
 */
public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_PICKER_DIALOG = "vid.bidnerdranch.criminalIntent.datepicker";
    private static final String TIME_PICKER_DIALOG = "vid.bignerdranch.criminalintent.timepicker";
    private static final String PHOTO_PICKER_DIALOG ="vid.bignerdranch.criminalIntent.photopicker";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO=3;

    private Crime mCrime;
    private EditText mEditText;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private DateFormat mDateFormat;
    private DateFormat mTimeFormat;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private Button mContacts;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Point mPhotoViewSize;
    private Callbacks mCallbacks;

    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }


    public static CrimeFragment newInstance(UUID crimeId)   // Using fragment arguments to get the fragment started . This is done by creating a Bundle object and using it to set the fragment arguments
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);
//        getPermissionToReadUserContacts();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallbacks=null;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_delete, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                Toast.makeText(getActivity(), "Deleting the crime", Toast.LENGTH_SHORT).show();
                UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
                CrimeLab.get(getActivity()).removeCrime(crimeId);
                Intent intent = new Intent(getActivity(), CrimeListActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mEditText = (EditText) view.findViewById(R.id.crime_title);
        mEditText.setText(mCrime.getTitle());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
        mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DATE_PICKER_DIALOG);

            }
        });
        mTimeButton = (Button) view.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getTime());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(manager, TIME_PICKER_DIALOG);
            }


        });
        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mReportButton = (Button) view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/Plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .startChooser();
                //Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("Text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i,getString(R.string.send_report));
//                startActivity(i);
            }
        });
        mSuspectButton = (Button) view.findViewById(R.id.crime_suspect);
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); // because its final got to be declared in general scope rather than in onClick(View)
//        pickContact.addCategory(Intent.CATEGORY_HOME); // This is jus a small piece of dummy code to check if the filter works in preventing the app from crashing.
        mSuspectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        mCallButton = (Button) view.findViewById(R.id.crime_call);
        PackageManager pm = getActivity().getPackageManager();
        if(pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {


            mCallButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//              1 Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//                3String selectClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"=?";
//               2 String[] projection/fields = {ContactsContract.CommonDataKinds.Phone.NUMBER};
//                4String[] selectParams={Long.toString(mCrime.getCaller())};
//                Cursor cursor = getActivity().getContentResolver().query(contentUri,fields,selectClause,selectParams,null);

                    Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{Long.toString(mCrime.getCaller())}, null);
                    if (cursor != null && cursor.getCount() > 0) {
                        try {
                            cursor.moveToFirst();
                            String number = cursor.getString(0);
                            Uri phoneNumber = Uri.parse("tel:97-000-000-0000" + number);

                            Intent intent = new Intent(Intent.ACTION_CALL, phoneNumber);
                            startActivity(intent);
                        } finally {
                            cursor.close();
                        }
                    }
                }
            });
        }

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager(); // PackageManager knows all the components installed on Android including all of its activities.
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
        {
            mSuspectButton.setEnabled(false);
        }

        mContacts=(Button)view.findViewById(R.id.button2);
        mContacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),ContactsContracts.class);
                startActivity(intent);
            }
        });

        mPhotoButton=(ImageButton)view.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile !=null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            mPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(captureImage,REQUEST_PHOTO);
                }
            });
        }
        mPhotoView=(ImageView)view.findViewById(R.id.crime_photo);
        mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout() {
                boolean isFirstPass = (mPhotoViewSize==null);
                mPhotoViewSize = new Point();
                mPhotoViewSize.set(mPhotoView.getMaxWidth(),mPhotoView.getMaxHeight());
                if(isFirstPass){
                    updatePhotoView();
                }
            }
        });
        mPhotoView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (mPhotoFile != null && mPhotoFile.exists()) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
//                    PhotoDialogPickerFragment photo = new PhotoDialogPickerFragment();
                    PhotoDialogPickerFragment photo = PhotoDialogPickerFragment.newInstance(mPhotoFile);
                    photo.show(manager, PHOTO_PICKER_DIALOG);
                }
            }
        });
        return view;
    }

    private void updateCrime()
    {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

//    public void getPermissionToReadUserContacts()
//    {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
//        {
//            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS))
//            {
//
//            }
//
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS_PERMISSIONS_REQUEST);
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CONTACTS_PERMISSIONS_REQUEST) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Read Contacts Permissions granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Read Contacts permissions denied", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    private void updateDate()
    {
        mDateButton.setText(mDateFormat.format(mCrime.getDate()));
    }


    private void updateTime()
    {
        mTimeButton.setText(mTimeFormat.format(mCrime.getTime()));
    }

    private String getCrimeReport()
    {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            updateDate();

        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(date);
            updateCrime();
            updateTime();
        }else if(requestCode== REQUEST_PHOTO)
        {
            updatePhotoView();
            updateCrime();

        }else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData(); // setting type of the intent data to be or Uri so that u will know where to search in the database of contacts
//          Also this is the whereClause in the query
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID
            };
            // whereArgs is the whereArgs[] for the query
            // perform your query= the contacts query is like a whereClause in your query
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                // Double check u actually got the results u were hoping for
                /**
                 * Returns the current position of the cursor in the row set.
                 * The value is zero-based. When the row set is first returned the cursor
                 * will be at positon -1, which is before the first row. After the
                 * last row is returned another call to next() will leave the cursor past
                 * the last entry, at a position of count().
                 *
                 * @return the current cursor position.
                 */
                if (c.getCount() == 0) {
                    return;
                }
                //pull out the first column of the first row of data
                c.moveToFirst();
                String suspect = c.getString(0);
                long contactId = c.getLong(1);
                mCrime.setCaller(contactId);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
                updateCrime();
            } finally {
                c.close();
            }
        }
    }
    private void updatePhotoView()
    {
        if(mPhotoFile==null || !mPhotoFile.exists())
        {
            mPhotoView.setImageDrawable(null);

        } else
        {
            Bitmap bitmap = (mPhotoViewSize == null) ? PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity()) : PictureUtils.getScaledBitmap(mPhotoFile.getPath(), mPhotoViewSize.x, mPhotoViewSize.y);
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setClickable(true);
        }
    }
}

