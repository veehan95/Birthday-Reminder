package example.com.veehan.birthdayreminder.UserInterface;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import example.com.veehan.birthdayreminder.DataBase.Contract;
import example.com.veehan.birthdayreminder.DataBase.Helper;
import example.com.veehan.birthdayreminder.DataBase.Queries;
import example.com.veehan.birthdayreminder.Domain.Birthday;
import example.com.veehan.birthdayreminder.Domain.CommonFunctions;
import example.com.veehan.birthdayreminder.R;

/**
 * Created by Tan Vee Han 1304713 on 24/8/2017
 * This Class is to view full details of on birthday
 * When enter this class, information will be extracted from database, then placed in a Birthday
 * object and display on the screen
 */

public class ViewBirthdayActivity extends AppCompatActivity {
    final public static String EXTRA_BIRTHDAY = "example.com.veehan._ID";
    private CommonFunctions cf;
    private Birthday birthdayInfo;
    private TextView tv_name;
    private TextView tv_birthday;
    private TextView tv_email;
    private TextView tv_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cf = new CommonFunctions();

        // Text views
        tv_name = (TextView)findViewById(R.id.input_name);
        tv_birthday = (TextView)findViewById(R.id.input_birthday);
        tv_email = (TextView)findViewById(R.id.input_email);
        tv_note = (TextView)findViewById(R.id.input_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onResume() {
        super.onResume();
        long id;

        Intent intent = getIntent();
        id = intent.getLongExtra(BirthdayListActivity.EXTRA_ID, 0);

        // Get information from database
        Queries dbq = new Queries(new Helper(getApplicationContext()));
        final String[] columns = {
                Contract.Entry._ID,
                Contract.Entry.COLUMN_NAME_NAME,
                Contract.Entry.COLUMN_NAME_EMAIL,
                Contract.Entry.COLUMN_NAME_BIRTHDAY,
                Contract.Entry.COLUMN_NAME_BIRTHMONTH,
                Contract.Entry.COLUMN_NAME_BIRTHYEAR,
                Contract.Entry.COLUMN_NAME_NOTE
        };

        String selection = Contract.Entry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        Cursor cursor = dbq.query(columns, selection, selectionArgs, null, null, null);

        if(cursor.moveToNext()) {
            birthdayInfo = new Birthday(
                    cursor.getLong(cursor.getColumnIndex(Contract.Entry._ID)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_BIRTHDAY)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_BIRTHMONTH)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_BIRTHYEAR)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_NOTE))
            );
            setTitle(birthdayInfo.getName());

            // Display informations on the screen
            tv_name.setText(birthdayInfo.getName());
            tv_birthday.setText(birthdayInfo.getBirthday() + " " + cf.months[Integer.parseInt(
                    birthdayInfo.getBirthMonth())] + " " + birthdayInfo.getBirthYear());
            if(birthdayInfo.getEmail().length() == 0){tv_email.setText("-");}
            else{tv_email.setText(birthdayInfo.getEmail());}
            if(birthdayInfo.getNote().length() == 0){tv_note.setText("-");}
            else{tv_note.setText(birthdayInfo.getNote());}

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else {
            Log.e("id not found", Long.toString(cursor.getLong(cursor.getColumnIndex(Contract.
                    Entry._ID))));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_view_birthday, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_delete:
                Queries dbq = new Queries(new Helper(getApplicationContext()));
                dbq.delete(birthdayInfo.getId());
                finish();
                return true;
            case R.id.action_update:
                Intent intent = new Intent();
                intent.setClass(ViewBirthdayActivity.this, UpdateBirthdayActivity.class);
                intent.putExtra(EXTRA_BIRTHDAY,birthdayInfo.getId());
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
