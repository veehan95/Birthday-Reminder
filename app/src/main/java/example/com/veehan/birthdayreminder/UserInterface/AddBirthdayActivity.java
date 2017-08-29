package example.com.veehan.birthdayreminder.UserInterface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import example.com.veehan.birthdayreminder.Domain.CommonFunctions;
import example.com.veehan.birthdayreminder.R;

/**
 * Created by Tan Vee Han 1304713 on 26/8/2017.
 * This class is to allow user to create a new birthday object
 */

public class AddBirthdayActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etBirthday;
    private EditText etEmail;
    private EditText etNote;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CommonFunctions cf;
    final long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cf = new CommonFunctions();
        sharedPreferences = getSharedPreferences("SAVE_STATE", MODE_PRIVATE);
        editor = getSharedPreferences("SAVE_STATE", MODE_PRIVATE).edit();

        // Save button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cf.save(AddBirthdayActivity.this, editor, id, etName, etBirthday, etEmail,
                        etNote);
            }
        });

        // Name text field
        etName = (EditText)findViewById(R.id.input_name);
        cf.name(AddBirthdayActivity.this, etName);

        // Birthday text field
        etBirthday = (EditText)findViewById(R.id.input_birthday);
        cf.birthday(AddBirthdayActivity.this, etBirthday);

        // Email text field
        etEmail = (EditText)findViewById(R.id.input_email);
        cf.email(AddBirthdayActivity.this, etEmail);

        // Note text field
        etNote = (EditText)findViewById(R.id.input_note);
        cf.note(AddBirthdayActivity.this, sharedPreferences, editor, etNote);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() { cf.discardSave(AddBirthdayActivity.this, editor, id, etName,
            etBirthday, etEmail, etNote); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cf.discardSave(AddBirthdayActivity.this, editor, id, etName, etBirthday,
                        etEmail, etNote);
                break;
        }
        return true;
    }

}
