package example.com.veehan.birthdayreminder.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tan Vee Han 1304713 on 26/8/2017.
 */

public class Helper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "birthday.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Contract.Entry.TABLE_NAME + " (" +
                    Contract.Entry._ID + " INTEGER PRIMARY KEY," +
                    Contract.Entry.COLUMN_NAME_NAME + " TEXT," +
                    Contract.Entry.COLUMN_NAME_BIRTHDAY + " TEXT," +
                    Contract.Entry.COLUMN_NAME_BIRTHMONTH + " TEXT," +
                    Contract.Entry.COLUMN_NAME_BIRTHYEAR + " TEXT," +
                    Contract.Entry.COLUMN_NAME_EMAIL + " TEXT," +
                    Contract.Entry.COLUMN_NAME_NOTE + " TEXT)";

    public Helper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}
