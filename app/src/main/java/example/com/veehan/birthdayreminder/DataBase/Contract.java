package example.com.veehan.birthdayreminder.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Tan Vee Han 1304713 on 26/8/2017.
 */

public class Contract {
    // Column in database
    public static class Entry implements BaseColumns{
        public static final String TABLE_NAME = "birthday";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BIRTHDAY = "date";
        public static final String COLUMN_NAME_BIRTHMONTH = "month";
        public static final String COLUMN_NAME_BIRTHYEAR = "year";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_NOTE ="note";
    }
}
