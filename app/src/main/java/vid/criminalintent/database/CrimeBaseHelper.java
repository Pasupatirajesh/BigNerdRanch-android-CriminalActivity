package vid.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import vid.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by SSubra27 on 10/30/15.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    } // third parameter is to be initialized if u are using a custom cursor object
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("Create table " + CrimeTable.NAME + "(" +" _id integer primary key autoincrement, "+
                    CrimeTable.Cols.UUID  + ", " + CrimeTable.Cols.TITLE + ", " + CrimeTable.Cols.DATE + ", " + CrimeTable.Cols.SOLVED +", "+CrimeTable.Cols.SUSPECT+", "+CrimeTable.Cols.CALLER +")" );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
