package com.ophid.fleetassessment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.view.PointerIconCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private MySQLiteHelper dbHelper;
    String periodMark="Mark";
    Connection connectionclass;
    private static final String DATABASE_NAME = "Fleet_Assessment.db";
   // private static final String DATABASE_NAME = "/data/data/org.ophid.logbk/databases/dbPatients.db";


    private static final String TABLE_VEHICLE_CHECKLIST = "create table VehicleChecklist(id text, EmployeeNumber text not null,VehicleNumber text not null,ActivityDate text not null,SyncStatus text DEFAULT 'Not Synced' not null,t1 text not null,t2 text not null,t3 text not null,t4 text not null,t5 text not null,t6 text not null,t7 text not null,t8 text not null,t9 text not null,t10 text not null,t11 text not null,t12 text not null,t13 text not null,t14 text not null,t15 text not null,t16 text not null,t17 text not null,t18 text not null,t19 text not null,t20 text not null,t21 text not null,t22 text not null,t23 text not null,t24 text not null,t25 text not null,t26 text not null,t27 text not null,t28 text not null,t29 text not null,t30 text not null,t31 text not null,t32 text not null,t33 text not null,t34 text not null);";
    private static final int DATABASE_VERSION = 1;
    private static final String user_tab="user_tab";
    private static final String user_password="user_password";
    private static final String employee_number ="employee_number";
    private static final String station_tab="station_tab";



    private  Context context;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
        this.context=context;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(TABLE_VEHICLE_CHECKLIST);

        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS user_credential_tab(" +
                " user_id INTEGER primary key autoincrement , " +
                " employee_number text, " +
                " employee_name text, " +
                " email text, " +
                " supervisor_name text, " +
                " driver_or_rider INTEGER, " +
                " district_name text, " +
                " district_id INTEGER, " +
                " supervisor boolean, " +
                " password text)");

        String userSql;
        userSql = "CREATE TABLE IF NOT EXISTS "
                + user_tab + "( user_id INTEGER primary key autoincrement ,"
                + employee_number+ " text,"
                + " active INTEGER,"
                + " deleted INTEGER,"
                + " force_reset INTEGER,"
                + user_password+ " text)";
        sQLiteDatabase.execSQL(userSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        onCreate(db);
    }


    @SuppressLint("Range")
    public UserCredential loginLocalDb(String employeeNumber, String password) {
        UserCredential userCredential = null;
        //try local details
        SQLiteDatabase Db = this.getWritableDatabase();
        Cursor cursor = Db.rawQuery("Select * from user_credential_tab where  upper(employee_number)='" + employeeNumber.toUpperCase() + "' and trim(password)='" + password.trim() + "'", null);
        if (cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                userCredential = new UserCredential();
                userCredential.setEmployeeName(cursor.getString(cursor.getColumnIndex("employee_name")));
                userCredential.setEmployeeNumber(cursor.getString(cursor.getColumnIndex("employee_number")));
                userCredential.setEmployeeNumber(cursor.getString(cursor.getColumnIndex("employee_number")));
                userCredential.setDistrictId(cursor.getInt(cursor.getColumnIndex("district_id")));
                userCredential.setDistrictName(cursor.getString(cursor.getColumnIndex("district_name")));
                userCredential.setDriverOrRider(cursor.getInt(cursor.getColumnIndex("driver_or_rider")));
                userCredential.setSupervisor(cursor.getInt(cursor.getColumnIndex("supervisor"))>0?true:false);
                //userCredential.setSupervisorEmail(cursor.getString(cursor.getColumnIndex("supervisor_email")));
                ;
            } finally {
                cursor.close();
                Db.close();
            }
        }
        return userCredential;

    }
    public void retrieveUserAccount(final String employeeNumber, final String password) {
        //try to get the record from the remote

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Verifying User Credentials.....");

        Globals.userCredential=null;

        try {
               connectionclass = new ConnectionHelper().connectionclass();
            if (connectionclass != null) {
                ResultSet executeQuery = connectionclass.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select * from ophid_employee_table WHERE emp_num ='" + employeeNumber.trim() + "'");

                boolean isPasswordCorrect= PasswordHash.verifyUserPassword(password,getActualPassword(employeeNumber.trim()),getSalt(employeeNumber.trim()));
               // Toast.makeText(context, password, Toast.LENGTH_SHORT).show();

                if (!executeQuery.next()) {

                    Globals.showMessage(context, "Login" ,"Sorry, it appears no account has been created for you on this device");
                    //searchNotFoundPopupMessage();
                    return;
                } else {

                    if(!isPasswordCorrect)
                    {
                        Globals.showMessage(context, "Login" ,"Sorry, the password you entred is incorrect");
                        //searchNotFoundPopupMessage();
                        return;
                    }

                    executeQuery.beforeFirst();
                        while (executeQuery.next()) {
                            UserCredential userCredential = null;
                            userCredential = new UserCredential();
                            userCredential.setEmployeeName(executeQuery.getString("emp_nam"));
                            userCredential.setEmployeeNumber(executeQuery.getString("emp_num"));
                            userCredential.setPassword(password);
                            userCredential.setDistrictId(executeQuery.getInt("emp_office"));
                            userCredential.setEmail(executeQuery.getString("emp_mail"));
                            userCredential.setDistrictName(executeQuery.getString("emp_office"));
                            userCredential.setDriverOrRider(executeQuery.getInt("driver_or_rider"));
                            userCredential.setSupervisorEmail(executeQuery.getString("emp_sup_mail"));
                            userCredential.setSupervisor(executeQuery.getBoolean("sup_nam"));
                            Globals.userCredential = userCredential;
                            commitUserCredentialToDb(userCredential);
                            Globals.showMessage(context, "Login", "User Credentials Successfully retrieved. You can now login");
                        }
                   // successPopupMessage();
                }


                return;
            }

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }












        JSONObject obj= new JSONObject();
        try {
            obj.put("password",password);
            obj.put("employeeNumber",employeeNumber.trim());
            System.out.println(obj.toString());
            System.out.println("im here");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            String URL = Globals.serverUrlBase + "auth/login";
            progressDialog.show();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, obj,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject obj) {
                           
                            progressDialog.dismiss();
                            System.out.println(obj.toString());

                            try {
                                UserCredential userCredential=null;

                                if (obj != null) {
                                    userCredential = new UserCredential();
                                    userCredential.setEmployeeName(obj.getString("employeeName"));
                                    userCredential.setEmployeeNumber(obj.getString("employeeNumber"));
                                    userCredential.setPassword(password);
                                    userCredential.setDistrictId(obj.getInt("districtId"));
                                    userCredential.setEmail(obj.getString("email"));
                                    userCredential.setDistrictName(obj.getString("districtName"));
                                    userCredential.setDriverOrRider(obj.getInt("driverOrRider"));
                                    userCredential.setSupervisorEmail(obj.getString("supervisorEmail"));
                                    userCredential.setSupervisor(obj.getBoolean("supervisor"));
                                    Globals.userCredential=userCredential;
                                    commitUserCredentialToDb(userCredential);
                                    Globals.showMessage(context, "Login", "User Credentials Successfully retrieved. You can now login");
                                      }

                                else
                                {
                                    Globals.showMessage(context, "Login", "User Credentials Successfully retrieved. You can now login");

                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    if (error.networkResponse != null) {
                        try {

                            String message= new String(error.networkResponse.data,"utf-8");
                            JSONObject jsonObject= new JSONObject(message);
                            System.out.println(jsonObject.toString());
                            Globals.showError(context,"Login",jsonObject.getString("message"));
                        } catch (Exception e) {

                            Globals.showMessage(context, "Login" ,"Sorry, it appears no account has been created for you on this device");

                        }
                    }

                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    String credentials = Globals.apiKey.trim() + ":" + Globals.apiPass.trim();
                    String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean commitUserCredentialToDb(UserCredential userCredential) {
        refreshTable("user_credential_tab");
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("employee_name",userCredential.getEmployeeName());
            cv.put("employee_number",userCredential.getEmployeeNumber());
            cv.put("district_name", userCredential.getDistrictName());
            cv.put("email",userCredential.getEmail());
            cv.put("driver_or_rider",userCredential.getDriverOrRider());
            cv.put("supervisor",userCredential.isSupervisor()==true?1:0);
            cv.put("district_id",userCredential.getDistrictId());
            cv.put("password",userCredential.getPassword());
            db.insert("user_credential_tab", null, cv);
            db.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }


    public boolean commitPurposeToLocalDB(String purpose)
    {
        //try {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("purpose",purpose);
        db.insert("purposes_tab", null, cv);
        db.close();
        return true;
        //} catch (Exception ex) {
        //  return false;
        // }

    }





    public boolean committAllocatedVehicle(JSONObject vehicle) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            System.out.println(" to persist the vehicle in local db "+vehicle.toString());
            cv.put("handed_over",0);
            cv.put("start_odometer",vehicle.getString("initialMileage"));
            cv.put("employee_number",Globals.userCredential.getEmployeeNumber());
            cv.put("licence_number", vehicle.getString("licenseNumber"));
            cv.put("fuel_tank", vehicle.getString("fuelTank"));
            db.insert("driver_vehicle_allocations", null, cv);
            db.close();
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public boolean commitStationToLocalDB(String trip) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("station_name",trip.replaceAll("/","").replaceAll(" ","").replaceAll(":","").toUpperCase());
            db.insert(station_tab, null, cv);
            db.close();
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public void showMessage(String title,String message)
    {
        Globals.showMessage(context,title,message);
    }

    public boolean isIfUserHasAccount(String employee_number)
    {
        SQLiteDatabase Db= this.getWritableDatabase();
        // Cursor cursor=Db.rawQuery("Select * from "+user_tab +" where  upper(employee_number)='"+employee_number.toUpperCase()+"' AND active=1 and deleted=0 and and force_reset=0'",null);
        Cursor cursor=Db.rawQuery("Select * from "+user_tab +" where  upper(employee_number)='"+employee_number.toUpperCase()+"'",null);
        if(cursor.getCount()>0)
        {
            return true;
        }
        return  false;
    }


    public void  refreshTable(String tblTable){
        SQLiteDatabase Db= this.getWritableDatabase();
        Db.execSQL("DELETE FROM "+tblTable);
        Db.close();
    }


    @SuppressLint("Range")
    public  String getSalt(String employeeNumber) {
        // SQLiteDatabase db = this.getWritableDatabase();
        // String query = "select * from  user_login where employee_number='"+employeeNumber+"'";
        String salt="";
        try {
            ResultSet executeQuery = connectionclass.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("select * from  login_salt where employee_number='"+employeeNumber+"'");
            while (executeQuery.next()) {
                salt = executeQuery.getString("salt");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return salt;
    }

    @SuppressLint("Range")
    public  String getActualPassword(String employeeNumber) {
       // SQLiteDatabase db = this.getWritableDatabase();
       // String query = "select * from  user_login where employee_number='"+employeeNumber+"'";
        String actualPassword="";
        try {
            ResultSet executeQuery = connectionclass.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("select * from  user_login where employee_number='" + employeeNumber + "'");

            while (executeQuery.next()) {
                actualPassword = executeQuery.getString("password");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return actualPassword;
    }
}
