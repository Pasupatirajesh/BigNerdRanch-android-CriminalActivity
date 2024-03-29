package vid.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vid.criminalintent.database.CrimeBaseHelper;
import vid.criminalintent.database.CrimeCursorWrapper;
import vid.criminalintent.database.CrimeDbSchema;

/**
 * Created by SSubra27 on 10/1/15.
 */
public class CrimeLab {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static CrimeLab sCrimeLab;

    public static CrimeLab get(Context context)
    {
        if(sCrimeLab==null)
        {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context)
    {
       mContext= context.getApplicationContext();
       mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase(); // having a superclass variable assigned to a subclass object helps in accessing the methods and fields of the superclass and subclass. This is called Dynamic Method Dispatch
    }
    public File getPhotoFile(Crime crime)
    {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir == null)
        {
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFilename());
    }
    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.getSuspect());
        values.put(CrimeDbSchema.CrimeTable.Cols.CALLER, crime.getCaller());
        return values;
    }
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new CrimeCursorWrapper(cursor);
    }
    public List<Crime> getCrimes()
    {
    List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
                cursor.close();
            }
            return crimes;
    }
    public void updateCrime(Crime crime)
    {
        String uuidString=crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, values,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }
    public Crime getCrime(UUID id)
    {
        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ",
                new String[] {id.toString()}
                );
        try {
            if (cursor.getCount() ==0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }
    public void addCrime(Crime c)
        {
            ContentValues values =getContentValues(c);
            mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null,values);
        }
    public void removeCrime(UUID crimeId)
        {
            String whereArgs = crimeId.toString();
            mDatabase.delete(CrimeDbSchema.CrimeTable.NAME, CrimeDbSchema.CrimeTable.Cols.UUID+ "=?", new String[] {whereArgs});

        }

}
