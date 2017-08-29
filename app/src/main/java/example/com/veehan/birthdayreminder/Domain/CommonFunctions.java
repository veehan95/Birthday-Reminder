package example.com.veehan.birthdayreminder.Domain;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

import example.com.veehan.birthdayreminder.DataBase.Helper;
import example.com.veehan.birthdayreminder.DataBase.Queries;
import example.com.veehan.birthdayreminder.R;
import example.com.veehan.birthdayreminder.UserInterface.AddBirthdayActivity;
import example.com.veehan.birthdayreminder.UserInterface.UpdateBirthdayActivity;

/**
 * Created by Tan Vee Han 1304713 on 27/8/2017.
 */

public class CommonFunctions {
    // Constant for month names
    public String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    // Limit the name to be maximum of 25 charcaters
    public void name(final Activity activity, final EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et.length() > 25) {
                    et.setText(et.getText().toString().substring(0, 25));
                    et.setSelection(25);
                    Toast.makeText(activity, "The name must be at most 25 characters!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Dialog for choosing date
    public void birthday(final Activity activity, final EditText et){
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        activity,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                et.setText(String.format("%02d", day) + " " + months[month] + " "
                        + year);
            }
        };
    }

    // Email validation
    public void email(final Activity activity, final EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et.length() > 320) {
                    et.setText(et.getText().toString().substring(0, 320));
                    et.setSelection(320);
                    Toast.makeText(activity, "Please enter a valid email!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Pop out dialog to key in notes
    public void note(final Activity activity, final SharedPreferences sharedPreferences,
                     final SharedPreferences.Editor editor, final EditText et){
        et.setText(sharedPreferences.getString("NOTE", ""));
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = (LayoutInflater.from(activity)).inflate(
                        R.layout.dialog_note, null);

                final EditText etFullNote = (EditText)v.findViewById(R.id.full_note);
                etFullNote.setText(sharedPreferences.getString("NOTE", ""));
                etFullNote.setSelection(sharedPreferences.getString("NOTE", "").length());

                new AlertDialog.Builder(activity)
                        .setTitle("Note")
                        .setView(v)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                editor.putString("NOTE", etFullNote.getText().toString());
                                editor.apply();
                                et.setText(sharedPreferences.getString("NOTE", ""));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {}})
                        .show();
            }
        });
    }

    // Discard changes dialog
    public void discardSave(final Activity activity, final SharedPreferences.Editor editor,
                            final long id, final EditText etName, final EditText etBirthday,
                            final EditText etEmail, final EditText etNote){
        // If data are key in text field, dialog will pop out, else it wont
        if(etName.getText().toString().length() != 0 ||
                etBirthday.getText().toString().length() != 0 ||
                etEmail.getText().toString().length() != 0 ||
                etNote.getText().toString().length() != 0){
            View v = (LayoutInflater.from(activity)).inflate(R.layout.dialog_discard,
                    null);
            final Button button_discard = (Button)v.findViewById(R.id.button_discard);
            button_discard.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){activity.finish();}
            });

            final Button button_save= (Button)v.findViewById(R.id.button_save);
            button_save.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    save(activity, editor, id, etName, etBirthday, etEmail, etNote);
                }
            });
            new AlertDialog.Builder(activity)
                    .setView(v)
                    .show();
        }
        else{activity.finish();}
    }

    // Save data from text field to database
    public void save(Activity activity, SharedPreferences.Editor editor, long id, EditText etName,
                     EditText etBirthday, EditText etEmail, EditText etNote){
        Queries dbq = new Queries(new Helper(activity.getApplicationContext()));

        CommonFunctions cf = new CommonFunctions();
        String name = etName.getText().toString();
        String birthday = etBirthday.getText().toString().substring(0,2);
        int birthmonth = Arrays.asList(cf.months).indexOf(etBirthday.getText().toString()
                .substring(3,etBirthday.getText().toString().length() - 5));
        String birthyear = etBirthday.getText().toString().substring(etBirthday.getText().toString()
                .length() - 4, etBirthday.getText().toString().length());
        String email = etEmail.getText().toString();
        String note = etNote.getText().toString();
        String message = "";

        // Force user to enter a name n birthday to save
        // If email is entered, the email must be valid to save
        if(name.length() == 0){
            message += "Please enter a valid name!";
        }
        else if(birthday.length() == 0){
            message += "Please enter a valid birthday!";
        }
        else if(email.length() != 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() == false) {
            message += "Please enter a valid email!";
        }
        else {
            if(activity instanceof AddBirthdayActivity) {
                Birthday birthdayInfo = new Birthday(name, birthday, Integer.toString(birthmonth),
                        birthyear, email, note);
                dbq.insert(birthdayInfo);
                message = "Item Created";
            }
            if(activity instanceof UpdateBirthdayActivity) {
                Birthday birthdayInfo = new Birthday(id, name, birthday, Integer.
                        toString(birthmonth), birthyear, email, note);
                dbq.update(birthdayInfo);
                message = "Details updated";
            }
            editor.clear();
            editor.commit();
            activity.finish();
        }

        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
    }

}
