package example.com.veehan.birthdayreminder.Domain;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import example.com.veehan.birthdayreminder.DataBase.Contract;
import example.com.veehan.birthdayreminder.DataBase.Helper;
import example.com.veehan.birthdayreminder.DataBase.Queries;
import example.com.veehan.birthdayreminder.UserInterface.MainActivity;

/**
 * Created by Tan Vee Han 1304713 on 28/8/2017.
 * This class will upload the data to http://labs.jamesooi.com/uecs3253-asg.php and inform user
 * how many data had been uploaded
 */

public class Cloud extends AsyncTask<Void, Void, JSONObject> {
    private static final String JSON_URL = "http://labs.jamesooi.com/uecs3253-asg.php";
    private Birthday birthday;
    private CommonFunctions cf;
    private MainActivity activity;

    public Cloud(MainActivity activity){
        cf = new CommonFunctions();
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(Void... v) {
        JSONObject response = null;

        try {
            response = postJson();
        } catch (IOException ex) {
            Log.e("IO_EXCEPTION", ex.toString());
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        String msg = "Error";
        try {
            int recordsSynced = Integer.parseInt(response.getString("recordsSynced"));
            if(recordsSynced == 0){
                msg = "No record is uploaded";
            }
            else{
                msg =  recordsSynced + " record(s) is(are) uploaded";
            }
        }
        catch(Exception e){
            Log.e("Error",  e.getMessage());
        }

        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    private JSONObject postJson() throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            JSONArray postData = new JSONArray();



            Queries dbq = new Queries(new Helper(activity.getApplicationContext()));

            final String[] columns = {
                    Contract.Entry._ID,
                    Contract.Entry.COLUMN_NAME_NAME,
                    Contract.Entry.COLUMN_NAME_EMAIL,
                    Contract.Entry.COLUMN_NAME_BIRTHDAY,
                    Contract.Entry.COLUMN_NAME_BIRTHMONTH,
                    Contract.Entry.COLUMN_NAME_BIRTHYEAR,
                    Contract.Entry.COLUMN_NAME_NOTE
            };

            Cursor cursor = dbq.query(columns, null, null, null, null, Contract.Entry
                    .COLUMN_NAME_NAME + " ASC");




            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                JSONObject rowObject = new JSONObject();

                for( int i=0 ;  i< cursor.getColumnCount() ; i++ ){
                    if( cursor.getColumnName(i) != null ){
                        try{
                            rowObject.put("id" ,  cursor.getColumnIndex(Contract.Entry._ID) );
                            rowObject.put("name" ,  cursor.getColumnIndex(Contract.Entry
                                    .COLUMN_NAME_NAME) );
                            int birthMonth = Integer.parseInt(Contract.Entry
                                    .COLUMN_NAME_BIRTHMONTH) + 1;
                            String birthdayStr = Contract.Entry.COLUMN_NAME_BIRTHDAY + "-" +
                                    Integer.toString(birthMonth) + "-" + Contract.Entry
                                    .COLUMN_NAME_BIRTHDAY;
                            rowObject.put("birthDate" , birthdayStr);
                        }
                        catch( Exception e ){
                            Log.d("Error", e.getMessage()  );
                        }
                    }
                }
                postData.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();

            URL url = new URL(JSON_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Starts the query
            os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postData));
            writer.flush();
            writer.close();
            os.close();


            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                is = conn.getInputStream();
                return readInputStream(is);
            } else {
                Log.e("HTTP_ERROR", Integer.toString(responseCode));
                return null;
            }
        } catch (Exception ex) {
            Log.e("EXCEPTION", ex.toString());
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public JSONObject readInputStream(InputStream is)
            throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder builder = new StringBuilder();

        String input;
        while ((input = reader.readLine()) != null)
            builder.append(input);

        return new JSONObject(builder.toString());
    }

    private String getPostDataString(JSONArray data) throws Exception {

        StringBuilder result = new StringBuilder();

        result.append(URLEncoder.encode("data", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(data.toString(), "UTF-8"));

        return result.toString();
    }
}