package example.com.veehan.birthdayreminder.UserInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import example.com.veehan.birthdayreminder.DataBase.Contract;
import example.com.veehan.birthdayreminder.DataBase.Helper;
import example.com.veehan.birthdayreminder.DataBase.Queries;
import example.com.veehan.birthdayreminder.Domain.Birthday;
import example.com.veehan.birthdayreminder.Domain.CommonFunctions;
import example.com.veehan.birthdayreminder.R;

/**
 * Created by Tan Vee Han 1304713 on 24/8/2017
 * This Class allows user to update an Birthday Object.
 * the class will first extract information from the database and pre-fill in the text field if
 * values are found.
 * When back button is pressed, a meaasage will pop out to ask user rather to discard all changes
 * when user press save, the row in database will be updated.
 */

public class UpdateBirthdayActivity extends AppCompatActivity {
    private Birthday birthdayInfo;
    private EditText etName;
    private EditText etBirthday;
    private EditText etEmail;
    private EditText etNote;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CommonFunctions cf;
    private Intent intent;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cf = new CommonFunctions();
        intent = getIntent();
        id = intent.getLongExtra(ViewBirthdayActivity.EXTRA_BIRTHDAY, 0);
        sharedPreferences = getSharedPreferences("SAVE_STATE", MODE_PRIVATE);
        editor = getSharedPreferences("SAVE_STATE", MODE_PRIVATE).edit();

        // Floating button action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cf.save(UpdateBirthdayActivity.this, editor, id, etName, etBirthday, etEmail,
                        etNote);
            }
        });

        // Text field properties setting
        // Name text field
        etName = (EditText)findViewById(R.id.input_name);
        cf.name(UpdateBirthdayActivity.this, etName);

        // Birthday text field
        etBirthday = (EditText)findViewById(R.id.input_birthday);
        cf.birthday(UpdateBirthdayActivity.this, etBirthday);

        // Email text field
        etEmail = (EditText)findViewById(R.id.input_email);
        cf.email(UpdateBirthdayActivity.this, etEmail);

        // Note text field
        etNote = (EditText)findViewById(R.id.input_note);
        cf.note(UpdateBirthdayActivity.this, sharedPreferences, editor, etNote);

        // Extract data from data base
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

        if (cursor.moveToNext()) {
            birthdayInfo = new Birthday(
                    cursor.getLong(cursor.getColumnIndex(Contract.Entry._ID)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_BIRTHDAY)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_BIRTHMONTH)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_BIRTHYEAR)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_NOTE))
            );

            // Pre-fil data to text field
            etName.setText(birthdayInfo.getName());
            etName.setSelection(etName.getText().toString().length());
            etBirthday.setText(birthdayInfo.getBirthday() + " " + cf.months[Integer.parseInt(
                    birthdayInfo.getBirthMonth())] + " " + birthdayInfo.getBirthYear());
            etBirthday.setSelection(etBirthday.getText().toString().length());
            etEmail.setText(birthdayInfo.getEmail());
            etEmail.setSelection(etEmail.getText().toString().length());
            etNote.setText(birthdayInfo.getNote());
            etNote.setSelection(etNote.getText().toString().length());

            setTitle(birthdayInfo.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("id not found", Long.toString(cursor.getLong(cursor.getColumnIndex(Contract.Entry._ID))));
            finish();
        }
    }

    @Override
    public void onBackPressed() { cf.discardSave(UpdateBirthdayActivity.this, editor, id, etName,
            etBirthday, etEmail, etNote); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cf.discardSave(UpdateBirthdayActivity.this, editor, id, etName, etBirthday, etEmail,
                        etNote);
                break;
        }
        return true;
    }

}
