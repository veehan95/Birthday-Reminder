package example.com.veehan.birthdayreminder.UserInterface;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import example.com.veehan.birthdayreminder.DataBase.Contract;
import example.com.veehan.birthdayreminder.Domain.CommonFunctions;
import example.com.veehan.birthdayreminder.R;

/**
 * Created by Tan Vee Han 1304713 on 26/8/2017.
 * This class is to set display for an item in the list
 */

public class CursorAdapter extends android.widget.CursorAdapter {
    private LayoutInflater inflater;
    private CommonFunctions cf;
    private TextView tv_countDown;
    private Calendar calendar;
    private TextView tv_name;
    private  String name;
    private TextView tv_birthday;
    private TextView tv_age;
    private String birthday;
    private String birthmonth;
    private String birthyear;
    private ImageView iv;
    private int birthMonth;

    public CursorAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor){
        cf = new CommonFunctions();
        int age;
        String ageMessage = "";

        calendar = java.util.Calendar.getInstance();
        tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_birthday = (TextView)view.findViewById(R.id.tv_birthday);
        tv_age = (TextView)view.findViewById(R.id.tv_age);
        tv_countDown = (TextView)view.findViewById(R.id.tv_count_down);
        iv = (ImageView)view.findViewById(R.id.iv_count_down);

        // Display informations
        // Name
        name = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_NAME));
        tv_name.setText(name);

        // Birthday
        birthday = cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_BIRTHDAY));
        birthmonth = cf.months[cursor.getInt(cursor.getColumnIndex(Contract.Entry
                .COLUMN_NAME_BIRTHMONTH))];
        birthyear = cursor.getString(cursor.getColumnIndex(Contract.Entry
                .COLUMN_NAME_BIRTHYEAR));
        tv_birthday.setText(birthday + " " + birthmonth + " " + birthyear);

        // Age
        age = calendar.get(java.util.Calendar.YEAR) - Integer.parseInt(birthyear);
        if(Arrays.asList(cf.months).indexOf(birthmonth) > calendar.get(java.util.Calendar
                .MONTH)){
            age -= 1;}
        else if(cf.months[calendar.get(java.util.Calendar.MONTH)] == birthmonth &&
                calendar.get(Calendar.DAY_OF_MONTH) > Integer.parseInt(birthday)){age -= 1;}

        if(age > 0){ageMessage = "Currently " + Integer.toString(age) + " years old";}
        else if(age == 0){
            int temp = Arrays.asList(cf.months).indexOf(birthmonth);
            if(temp > calendar.get(java.util.Calendar.MONTH)){
                temp -= 12;
            }
            int ageMonth = calendar.get(java.util.Calendar.MONTH) - temp;
            if(ageMonth == 0){
                ageMessage = "New born ";}
            else{ageMessage = "Currently " + Integer.toString(ageMonth) + " month old";}
        }
        else{ageMessage = "Not born yet";}
        tv_age.setText(ageMessage);

        // Count down
        birthMonth = Arrays.asList(cf.months).indexOf(birthmonth) + 1;
        String birthdayStr = birthday + "/" + Integer.toString(birthMonth) + "/" +
                Integer.toString(calendar.get(java.util.Calendar.YEAR));
        String todayStr = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + "/" +
                Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" +
                Integer.toString(calendar.get(java.util.Calendar.YEAR));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date birthdayDate = sdf.parse(birthdayStr);
            Date todayDate = sdf.parse(todayStr);

            long difference = birthdayDate.getTime() - todayDate.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            if(differenceDates < 0){
                int daysInYear;
                if(calendar.get(java.util.Calendar.YEAR) % 4 == 0){daysInYear = 366;}
                else{daysInYear = 365;}
                differenceDates += daysInYear;
            }

            if(differenceDates == 0){
                iv.setVisibility(View.VISIBLE) ;
                tv_countDown.setVisibility(View.GONE) ;
                iv.setImageResource(R.drawable.today);
            }
            else {
                iv.setVisibility(View.GONE) ;
                tv_countDown.setVisibility(View.VISIBLE) ;
                String dayDifference = Long.toString(differenceDates);
                tv_countDown.setText(dayDifference + " days left");
            }
        } catch (java.text.ParseException e) {
            Log.e("Date error", Long.toString(cursor.getLong(cursor.getColumnIndex(Contract.
                    Entry._ID))));
        }
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return inflater.inflate(R.layout.list_item, parent, false);
    }
}
