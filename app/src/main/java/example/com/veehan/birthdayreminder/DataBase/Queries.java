package example.com.veehan.birthdayreminder.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;

import example.com.veehan.birthdayreminder.Domain.Birthday;
import example.com.veehan.birthdayreminder.Domain.CommonFunctions;

/**
 * Created by Tan Vee Han 1304713 on 26/8/2017.
 * Queries to perform CRUD to database
 */

public class Queries {
    private Helper helper;
    private CommonFunctions cf;

    public Queries(Helper helper){
        this.helper = helper;
        cf = new CommonFunctions();
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy) {
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.query(
                Contract.Entry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
    }

    // Insert object into table
    public long insert(Birthday birthday){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contract.Entry.COLUMN_NAME_NAME, birthday.getName());
        values.put(Contract.Entry.COLUMN_NAME_BIRTHDAY, birthday.getBirthday());
        values.put(Contract.Entry.COLUMN_NAME_BIRTHMONTH, birthday.getBirthMonth());
        values.put(Contract.Entry.COLUMN_NAME_BIRTHYEAR, birthday.getBirthYear());
        values.put(Contract.Entry.COLUMN_NAME_EMAIL, birthday.getEmail());
        values.put(Contract.Entry.COLUMN_NAME_NOTE, birthday.getNote());

        long id = db.insert(Contract.Entry.TABLE_NAME, null, values);
        birthday.setId(id);
        return id;
    }

    // Update row
    public int update(Birthday birthday){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.COLUMN_NAME_NAME, birthday.getName());
        values.put(Contract.Entry.COLUMN_NAME_BIRTHDAY, birthday.getBirthday());
        values.put(Contract.Entry.COLUMN_NAME_BIRTHMONTH, birthday.getBirthMonth());
        values.put(Contract.Entry.COLUMN_NAME_BIRTHYEAR, birthday.getBirthYear());
        values.put(Contract.Entry.COLUMN_NAME_EMAIL, birthday.getEmail());
        values.put(Contract.Entry.COLUMN_NAME_NOTE, birthday.getNote());

        String selection = Contract.Entry._ID + "=?";
        String[] selectionArgs = {Long.toString(birthday.getId())};

        return db.update(
                Contract.Entry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    // Delete row
    public void delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = Contract.Entry._ID + "=?";
        String selectionArgs[] = {Long.toString(id)};
        db.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
    }

    // Count number of birthday stars on that day
    public int count(String day, String month, String year){
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + Contract.Entry.TABLE_NAME
                + " WHERE " + Contract.Entry.COLUMN_NAME_BIRTHMONTH + " =? AND "
                + Contract.Entry.COLUMN_NAME_BIRTHDAY + "=?",new String[] { Integer.toString(
                        Arrays.asList(cf.months).indexOf(month)), day}).
                getCount();
    }
}
