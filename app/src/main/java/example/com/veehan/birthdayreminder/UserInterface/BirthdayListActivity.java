package example.com.veehan.birthdayreminder.UserInterface;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import example.com.veehan.birthdayreminder.DataBase.Contract;
import example.com.veehan.birthdayreminder.DataBase.Helper;
import example.com.veehan.birthdayreminder.DataBase.Queries;
import example.com.veehan.birthdayreminder.R;

/**
 * Created by Tan Vee Han 1304713 on 26/8/2017.
 * This class contains functions that are used by multiple classes
 */

public class BirthdayListActivity extends AppCompatActivity {
    private ListView listView;
    public final static String EXTRA_ID = "com.example.veehan.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.list_view) ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);

                Intent intent = new Intent(BirthdayListActivity.this, ViewBirthdayActivity.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(Contract.Entry.
                        _ID)));
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume(){
        super.onResume();

        Queries dbq = new Queries(new Helper(getApplicationContext()));

        final String[] columns = {
                Contract.Entry._ID,
                Contract.Entry.COLUMN_NAME_NAME,
                Contract.Entry.COLUMN_NAME_BIRTHDAY,
                Contract.Entry.COLUMN_NAME_BIRTHMONTH,
                Contract.Entry.COLUMN_NAME_BIRTHYEAR
        };

        Cursor cursor = dbq.query(columns, null, null, null, null,
                Contract.Entry.COLUMN_NAME_BIRTHMONTH + ", " +
                        Contract.Entry.COLUMN_NAME_BIRTHDAY + ", " +
                        Contract.Entry.COLUMN_NAME_NAME + ", " +
                        Contract.Entry.COLUMN_NAME_BIRTHYEAR + " ASC");

        CursorAdapter adapter = new CursorAdapter(this, cursor,0);

        listView.setAdapter(adapter);
    }

}
