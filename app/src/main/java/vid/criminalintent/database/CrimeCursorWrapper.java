package vid.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import vid.criminalintent.Crime;

/**
 * Created by SSubra27 on 11/2/15.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }
    public Crime getCrime()
    {
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title =  getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date =     getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved =   getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT));
        long number =    getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.CALLER));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);
        crime.setCaller(number);

        return crime;
    }
}
