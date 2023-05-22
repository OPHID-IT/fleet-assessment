package com.ophid.fleetassessment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;


public class DBController extends SQLiteOpenHelper {
    private static final String databasename = "dbPatients.db"; // Dtabasename
    private static final int versioncode =19; //versioncode of the database
    private static final String user_tab="user_tab";
    private static final String user_password="user_password";
    private static final String station_tab="station_tab";
    private static final String employee_number ="employee_number";


    private  Context context;

    public DBController(Context context) {
        super(context, databasename, null, versioncode);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
       database.execSQL("CREATE TABLE IF NOT EXISTS user_credential_tab(" +
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

       //active=1 and deleted=0 and and force_reset=0
        String userSql;
        userSql = "CREATE TABLE IF NOT EXISTS "
                + user_tab + "( user_id INTEGER primary key autoincrement ,"
                + employee_number+ " text,"
                + " active INTEGER,"
                + " deleted INTEGER,"
                + " force_reset INTEGER,"
                + user_password+ " text)";
        database.execSQL(userSql);


    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {


        database.execSQL("CREATE TABLE IF NOT EXISTS user_credential_tab(" +
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

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Verifying User Credentials.....");

        Globals.userCredential=null;
            JSONObject obj= new JSONObject();
            try {
                obj.put("password",password);
                obj.put("employeeNumber",employeeNumber.trim());
                System.out.println(obj.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                String URL = Globals.serverUrlBase + "auth/login";
                progressDialog.show();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                        new Response.Listener<JSONObject>() {
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







}