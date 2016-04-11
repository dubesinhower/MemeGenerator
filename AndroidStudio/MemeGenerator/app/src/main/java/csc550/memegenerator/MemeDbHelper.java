package csc550.memegenerator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import csc550.memegenerator.MemeContract.MemeEntry;

/**
 * Created by Christopher on 4/10/2016.
 */
public class MemeDbHelper extends SQLiteOpenHelper {
    public static final int DATEBASE_VERSION = 1;
    public static final String DATABASE_NAME = "memes.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MemeEntry.TABLE_NAME + " (" +
                    MemeEntry._ID + " INTEGER PRIMARY KEY," +
                    MemeEntry.COLUMN_NAME_DISPLAY_NAME + TEXT_TYPE + COMMA_SEP +
                    MemeEntry.COLUMN_NAME_GENERATOR_NAME + TEXT_TYPE + COMMA_SEP +
                    MemeEntry.COLUMN_NAME_INSTANCE_URL + TEXT_TYPE + COMMA_SEP +
                    MemeEntry.COLUMN_NAME_IS_MINE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MemeEntry.TABLE_NAME;

    public MemeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATEBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
