package vid.criminalintent;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
/**
 * Created by SSubra27 on 11/16/15.
 */
public class ContactsContracts extends FragmentActivity {

    private SimpleCursorAdapter adapter;

    private static final int CONTACT_REQUEST_CODE = 1;
    private static final int CONTACT_LOADER_ID=78;
    private Crime mCrime;
    private Button mLoadContacts;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactscontracts);
        mCrime = new Crime();
        final Intent pickContent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

//        mLoadContacts = (Button) findViewById(R.id.load_contacts);
//        mLoadContacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(pickContent, CONTACT_REQUEST_CODE);
//
//            }
//        });

        setupCursorAdapter();

        getSupportLoaderManager().initLoader(CONTACT_LOADER_ID, new Bundle(), contactsLoader);

        mListView = (ListView) findViewById(R.id.list_item);
        mListView.setAdapter(adapter);
//        fetchContacts();

    }

    private void setupCursorAdapter() {
        String[] uiBindFrom = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_URI};
        int[] uiBindTo = {R.id.tvName, R.id.tvImage};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_contactscontracts, null, uiBindFrom, uiBindTo, 0);

    }

    private LoaderManager.LoaderCallbacks<Cursor> contactsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projectionFields = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_URI};

            CursorLoader cursorLoader = new CursorLoader(ContactsContracts.this, ContactsContract.Contacts.CONTENT_URI, projectionFields, null, null, null);
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            adapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }
    };





//    public void fetchContacts()
//    {
//        String phoneNumber = "";
//
//
//        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
//        String _ID = ContactsContract.Contacts._ID;
//        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
//        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//
//
//        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
//        String Number =ContactsContract.CommonDataKinds.Phone.NUMBER;
//
//
//        StringBuffer output = new StringBuffer();
//
//        ContentResolver contentResolver = getContentResolver();
//
//        Cursor cursor = contentResolver.query(CONTENT_URI,null,null,null,null);
//        if(cursor.getCount()>0)
//        {
//            while(cursor.moveToNext())
//            {
//                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
//                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
//                int hasPhoneNumber =Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
//                if(hasPhoneNumber>0)
//                {
//                    output.append("\n FirstName:"+ name);
//                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI,null, Phone_CONTACT_ID+"=?", new String[]{contact_id},null);
//                    while(phoneCursor.moveToNext())
//                    {
//                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(Number));
//                        output.append("\n Phone number:"+phoneNumber);
//                    }
//                    phoneCursor.close();
//                    output.append("\n");
//                }
//
//            }
//            cursor.close();
//
//        }
//
//
//
//    }


//    @Override
//    public void onActivityResult(int resultCode, int requestCode, Intent data)
//    {
//
//        if(resultCode!=Activity.RESULT_OK)
//        {
//            return;
//        }
//        if(requestCode==CONTACT_REQUEST_CODE && data!=null)
//        {
//            Uri contactName = data.getData();
//            String[] queryFields = new String[]{
//                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID
//            };
//            // whereArgs is the whereArgs[] for the query
//            // perform your query= the contacts query is like a whereClause in your query
//            Cursor c = getContentResolver().query(contactName, queryFields, null, null, null);
//            try {
//                // Double check u actually got the results u were hoping for
//                /**
//                 * Returns the current position of the cursor in the row set.
//                 * The value is zero-based. When the row set is first returned the cursor
//                 * will be at positon -1, which is before the first row. After the
//                 * last row is returned another call to next() will leave the cursor past
//                 * the last entry, at a position of count().
//                 *
//                 * @return the current cursor position.
//                 */
//                if (c.getCount() == 0) {
//                    return;
//                }
//                //pull out the first column of the first row of data
//                c.moveToFirst();
//                String suspect = c.getString(0);
////                long contactId = c.getLong(1);
////                mCrime.setCaller(contactId);
//                mCrime.setSuspect(suspect);
//                mListView.setAdapter(adapter);
//            } finally {
//                c.close();
//            }
//        }
//    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu)
   {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.menu_criminal,menu);
       return true;
   }

}


