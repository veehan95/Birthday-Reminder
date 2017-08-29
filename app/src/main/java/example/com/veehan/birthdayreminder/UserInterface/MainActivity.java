package example.com.veehan.birthdayreminder.UserInterface;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import example.com.veehan.birthdayreminder.DataBase.Helper;
import example.com.veehan.birthdayreminder.DataBase.Queries;
import example.com.veehan.birthdayreminder.Domain.Cloud;
import example.com.veehan.birthdayreminder.Domain.CommonFunctions;
import example.com.veehan.birthdayreminder.Domain.Service;
import example.com.veehan.birthdayreminder.R;

/**
 * Created by Tan Vee Han 1304713 on 24/8/2017
 * This Class is the first page and also the home page for this application
 */

public class MainActivity extends AppCompatActivity {

    // Initialization
    private final static java.util.Calendar calendar = java.util.Calendar.getInstance();
    private CommonFunctions cf;
    Queries dbq;
    final public static String EXTRA_BIRTHDAYSTAR = "example.com.veehan.BIRTHDAYSTAR";
    private TextView tv_dayMonth;
    private TextView tv_year;
    private TextView tv_message;
    private ImageButton btn_add;
    private ImageButton btn_view;
    private ImageButton btn_cloud;
    private int year;
    private String month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cf = new CommonFunctions();

        // Initialization
        dbq = new Queries(new Helper(getApplicationContext()));
        year = calendar.get(java.util.Calendar.YEAR);
        month = cf.months[calendar.get(java.util.Calendar.MONTH)];
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Generate date message
        tv_dayMonth = (TextView)findViewById(R.id.tv_dayMonth);
        tv_year = (TextView)findViewById(R.id.tv_year);
        tv_dayMonth.setText(Integer.toString(day) + " " + month);
        tv_year.setText(Integer.toString(year));

        // Timer for notification at every morning 8.00
        Calendar timer = Calendar.getInstance();
        timer.set(Calendar.HOUR_OF_DAY,8);
        timer.set(Calendar.MINUTE,00);
        timer.set(Calendar.SECOND,00);
        Intent notification = new Intent(this, Service.class);
        notification.putExtra(EXTRA_BIRTHDAYSTAR,dbq.count(Integer.toString(day), month,
                Integer.toString(year)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,
                notification,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timer.getTimeInMillis(),AlarmManager.
                INTERVAL_DAY,pendingIntent);

        // Generate message
        tv_message = (TextView)findViewById(R.id.tv_message);
        tv_message.setText(messageGenerator(day, month, year));

        // Add Birthday
        btn_add = (ImageButton)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddBirthdayActivity.class);
                startActivity(intent);
            }
        });

        // View birthday list
        btn_view = (ImageButton)findViewById(R.id.btn_list);
        btn_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), BirthdayListActivity.class);
                startActivity(intent);
            }
        });

        // View cloud
        btn_cloud = (ImageButton)findViewById(R.id.btn_cloud);
        btn_cloud.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vew){
                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.
                        CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()) {
                    new Cloud(MainActivity.this).execute();
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.network_unavailable),
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        int year = calendar.get(java.util.Calendar.YEAR);
        String month = cf.months[calendar.get(java.util.Calendar.MONTH)];
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        tv_message.setText(messageGenerator(day, month, year));
    }

    // Self define function
    public String messageGenerator(int day, String month, int year){
        if(dbq.count(Integer.toString(day), month, Integer.toString(year)) > 1) {
            return "There are a total of "+Integer.toString(dbq.count(Integer.toString(day),
                    month, Integer.toString(year)))+" birthday stars today";
        }
        else if(dbq.count(Integer.toString(day), month, Integer.toString(year)) == 1){
            return "There are only 1 birthday star today";
        }
        else{
            return "There are no birthday star today";
        }
    }
}
