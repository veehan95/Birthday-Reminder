package example.com.veehan.birthdayreminder.UserInterface;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;

import example.com.veehan.birthdayreminder.DataBase.Contract;
import example.com.veehan.birthdayreminder.DataBase.Helper;
import example.com.veehan.birthdayreminder.DataBase.Queries;
import example.com.veehan.birthdayreminder.R;

/**
 * Created by Tan Vee Han 1304713 on 28/8/2017.
 * This class is to display only the birthays on today and allows user tomodify it here
 */

public class DisplayBirthdayStar extends AppCompatActivity {
    private ListView listView;
    public final static String EXTRA_ID = "com.example.veehan.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_birthday_star);

        listView = (ListView)findViewById(R.id.list_view) ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);

                Intent intent = new Intent(DisplayBirthdayStar.this, ViewBirthdayActivity.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(Contract.Entry.
                        _ID)));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Calendar calendar = Calendar.getInstance();

        Queries dbq = new Queries(new Helper(getApplicationContext()));

        final String[] columns = {
                Contract.Entry._ID,
                Contract.Entry.COLUMN_NAME_NAME,
                Contract.Entry.COLUMN_NAME_BIRTHDAY,
                Contract.Entry.COLUMN_NAME_BIRTHMONTH,
                Contract.Entry.COLUMN_NAME_BIRTHYEAR
        };

        Cursor cursor = dbq.query(columns, Contract.Entry.COLUMN_NAME_BIRTHMONTH + " =? AND "
                        + Contract.Entry.COLUMN_NAME_BIRTHDAY + "=?",new String[] { Integer.toString(calendar.get(Calendar.MONTH)),Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))
        }, null, null,
                Contract.Entry.COLUMN_NAME_BIRTHMONTH + ", " +
                        Contract.Entry.COLUMN_NAME_BIRTHDAY + ", " +
                        Contract.Entry.COLUMN_NAME_NAME + ", " +
                        Contract.Entry.COLUMN_NAME_BIRTHYEAR + " ASC");

        CursorAdapter adapter = new CursorAdapter(this, cursor,0);

        listView.setAdapter(adapter);
    }

}
