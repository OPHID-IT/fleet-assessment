package com.ophid.fleetassessment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.widget.LinearLayout.LayoutParams;

public class HomeFragment extends Fragment {
    TextView progressIndicator;
    TextView dates;
    TextView selectedForm;
    TextView regnum;
    ResultSet rs;
    Statement st;
    Connection connect;
    //public static String regNumber;
    private MySQLiteHelper dbHelper;
    private ProgressBar progressBar;
    private SearchView searchView;
    public static SQLiteDatabase db;
    String vehicleNumber="";
    String activityDate="";
    String syncStatus="";
    int denominator=0;
    int numerators=0;
    ImageButton btnSync;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Date date_stamp;


    public static GetVehicleChecklistData vehicleChecklistInstance;

   // private String[] allHoursWorkedColumns = {"EmployeeNumber", MySQLiteHelper.HOURSWORKED_TRANSACTION_DATE, MySQLiteHelper.HOURSWORKED_ACTUAL_WORK_DATE, MySQLiteHelper.HOURSWORKED_SYNC_STATUS, MySQLiteHelper.HOURSWORKED_NUMBER_OF_HOURS_WORKED};

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dbHelper = new MySQLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();
        //Cursor cursor = db.rawQuery(sql, new String[] {login, password});
        //Cursor query = db.query("VehicleChecklist", this.allHoursWorkedColumns, null, null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist",null);

        //regNumber="No Vehicle Selected";

        //if(cursor.moveToFirst()) {
       //     regNumber = cursor.getString(cursor.getColumnIndex("VehicleNumber"));
        //}
        //check = cursor.moveToFirst();
        //cursor.close();
        //db.close();
        //return inflater.inflate(R.layout.fragment_home,container,false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        progressIndicator = (TextView) root.findViewById(R.id.pbartextview);
        selectedForm = (TextView) root.findViewById(R.id.processtextview);
        regnum=(TextView) root.findViewById(R.id.regNumTextView);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        searchView = (SearchView) root.findViewById(R.id.searchView);
        dates=(TextView) root.findViewById(R.id.dates);
        btnSync=(ImageButton) root.findViewById(R.id.imageButton);

        numerators=Globals.questionProgressCount;
        denominator=Globals.denominator;
        
        dates.setText(Globals.todaysDate);
        progressIndicator.setText("Progress "+numerators+"/"+denominator);
        progressBar.setMax(denominator);
        progressBar.setProgress(numerators);
        selectedForm.setText(Globals.focusedChecklist);
        //regnum.setText(Globals.vehicleRegNumber);
        regnum.setText(Globals.vehicleRegNumber);


        LinearLayout linearLayout = (LinearLayout)root.findViewById(R.id.homeLinearLayout);

        ArrayList<TextView> tv = new ArrayList<>();
        TextView textview1;
        int i = 0;
       /* while(cursor.moveToNext()) {
            regNumber = cursor.getString(cursor.getColumnIndex("VehicleNumber"));

            textview1 = new TextView(getActivity());
            LinearLayout.LayoutParams textviewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textviewLayoutParams.setMargins(50, 20, 0, 0);
            textview1.setLayoutParams(textviewLayoutParams);
            //textview1.setLayoutParams(new LayoutParams(LayoutParams.,LayoutParams.WRAP_CONTENT));

            textview1.setText("Vehicle number: " + cursor.getString(cursor.getColumnIndex("VehicleNumber")) + " Activity date: "+ cursor.getString(cursor.getColumnIndex("ActivityDate")));
            textview1.setTextColor(Color.BLUE);


            textview1.setId(i);

            //tv.add(textview1);
            linearLayout.addView(textview1);
            i++;
        }*/



        //regnum.setText(regNumber);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String str) {
                return true;
            }
            public boolean onQueryTextSubmit(String str) {
                if (!haveNetworkConnection()) {
                    noConnectionPopupMessage();
                    return true;
                }
                //Reg.this.pbar.setVisibility(View.VISIBLE);
                openVehicleChecklist();
                return true;
            }
        });


        btnSync.setOnClickListener(new View.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass2 */

            public void onClick(View view) {
                postVehicleChecklists();

            }
        });

       /* switch(Globals.focusedChecklist)
        {
            case "Vehicle Checklist":
                denominator = "34";
                //numerators=MainActivity.vehicle_checklist_Completed;

                 break;
            case "Motorcycle Checklist":
                denominator = "21";
                //numerators=MainActivity.motorcycle_checklist_Completed;

                break;
            case "Bicycle Inspection":
                denominator = "22";
                //numerators=MainActivity.bicycle_inspection_Completed;

                break;
            case "Vehicle Inspection":
                denominator = "31";
                //numerators=MainActivity.vehicle_inspection_Completed;

                break;
            case "Motorcycle Inspection":
               // denominator = "39";

                break;
       }*/
      // dates.setText(Globals.todaysDate);
      // progressIndicator.setText("Progress "+numerators+"/"+denominator);
      // progressBar.setMax(Integer.parseInt(denominator));
      // progressBar.setProgress(numerators);
      // selectedForm.setText(MainActivity.focusedChecklist);
       return root;
    }



    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void noConnectionPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please check your connection");
        builder.setTitle("Not connected");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass12 */

            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
    public void searchNotFoundPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Vehicle not found, please make sure the number you entered is correct");
        builder.setTitle("Vehicle not found");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.ApprovalList.AnonymousClass4 */

            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }



    public void successPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Vehicle successfully retrieved");
        builder.setTitle("Success");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.ApprovalList.AnonymousClass4 */

            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }



    public boolean doesRegNumberExistInLocalTableAndIsNotSynced(String regNumber)
    {
         Globals.vehicleChecklistTableHasData=false;
        //Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '"+regNumber+"'",null);
        Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '"+regNumber+"' AND SyncStatus= 'Not Synced'",null);

        if (cursor!=null && cursor.getCount()>0)
        {
            Globals.vehicleChecklistTableHasData = true;
        }
        else if (cursor==null || cursor.getCount()==0) {
            Globals.vehicleChecklistTableHasData = false;
        }

        return Globals.vehicleChecklistTableHasData;
    }


    public void openVehicleChecklist() {
        Globals.vehicleRegNumber = ((searchView.getQuery().toString()).trim()).toUpperCase();
        //assignDatabaseVariables(regNumber);
        if(doesRegNumberExistInLocalTableAndIsNotSynced(Globals.vehicleRegNumber))
        {

            regnum.setText(Globals.vehicleRegNumber);
            successPopupMessage();
        }
        else {

            try {
                Connection connectionclass = new ConnectionHelper().connectionclass();
                connect = connectionclass;
                if (connectionclass != null) {
                    ResultSet executeQuery = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select * from ophid_vehicles WHERE license_number ='" + Globals.vehicleRegNumber + "'");

                    if (!executeQuery.next()) {
                        searchNotFoundPopupMessage();
                        return;
                    } else {
                        executeQuery.beforeFirst();
                        successPopupMessage();
                    }

                    if (executeQuery.next()) {
                        executeQuery.beforeFirst();

                        while (executeQuery.next()) {
                            regnum.setText(executeQuery.getString(1));
                        }
                        return;
                    }

                    return;
                }

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }


    @SuppressLint("Range")
    public void postVehicleChecklists()
    {
        Cursor cursor= getVehicleChecklistsPendingPosting();
        while(cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("t1")).equals("0") ||
                    cursor.getString(cursor.getColumnIndex("t2")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t3")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t4")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t5")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t6")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t7")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t8")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t9")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t10")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t11")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t12")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t13")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t14")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t15")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t16")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t17")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t18")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t19")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t20")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t21")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t22")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t23")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t24")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t25")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t26")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t27")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t28")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t29")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t30")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t31")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t32")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t33")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t34")).equals("0"))
            {
                incompleteChecklistSyncPopupMessage();
                return;
            }

        }

        cursor=getVehicleChecklistsPendingPosting();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
           // String query = " insert into fuel_topup(top_up_code,vehicle_number,driver,speedometer_reading,top_up_reason,top_up_id,service_provider,service_provider_location,payment_method,litres,receipt_number,fuel_cost,where_are_you,top_up_date_time,receipt_image,approval_status)" + " values (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String query = " insert into VehicleChecklists(id,EmployeeNumber,VehicleNumber,ActivityDate,q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,q11,q12,q13,q14,q15,q16,q17,q18,q19,q20,q21,q22,q23,q24,q25,q26,q27,q28,q29,q30,q31,q32,q33,q34)" + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";



            /*******************GET TODAYS DATE STARTS HERE*************************************************/
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            date = dateFormat.format(calendar.getTime());
            date_stamp = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            //java.sql.Date date_stamp_sqlDate = new java.sql.Date(date_stamp.getTime());
            /*******************GET TODAYS DATE ENDS HERE*************************************************/
            //Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(MainActivity.periodStartDate);

            if (!((cursor == null) || (cursor.getCount()==0))) {
                while(cursor.moveToNext()) {
                    //String top_up_id = cursor.getString(cursor.getColumnIndex("top_up_id"));
                    //JSONObject jsonObject = new JSONObject();
                    PreparedStatement preparedStmt = connect.prepareStatement(query);
                    preparedStmt.setString(1, cursor.getString(cursor.getColumnIndex("id")));
                    preparedStmt.setString(2, cursor.getString(cursor.getColumnIndex("EmployeeNumber")));
                    preparedStmt.setString(3, cursor.getString(cursor.getColumnIndex("VehicleNumber")));

                    Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(cursor.getColumnIndex("ActivityDate")));
                    java.sql.Date date_stamp_sqlDate = new java.sql.Date(parse.getTime());
                    preparedStmt.setDate(4, date_stamp_sqlDate);

                    preparedStmt.setString(5, cursor.getString(cursor.getColumnIndex("t1")));
                    preparedStmt.setString(6, cursor.getString(cursor.getColumnIndex("t2")));
                    preparedStmt.setString(7, cursor.getString(cursor.getColumnIndex("t3")));
                    preparedStmt.setString(8, cursor.getString(cursor.getColumnIndex("t4")));
                    preparedStmt.setString(9, cursor.getString(cursor.getColumnIndex("t5")));
                    preparedStmt.setString(10, cursor.getString(cursor.getColumnIndex("t6")));
                    preparedStmt.setString(11, cursor.getString(cursor.getColumnIndex("t7")));
                    preparedStmt.setString(12, cursor.getString(cursor.getColumnIndex("t8")));
                    preparedStmt.setString(13, cursor.getString(cursor.getColumnIndex("t9")));
                    preparedStmt.setString(14, cursor.getString(cursor.getColumnIndex("t10")));
                    preparedStmt.setBytes(15, cursor.getBlob(cursor.getColumnIndex("t11")));
                    preparedStmt.setString(16, cursor.getString(cursor.getColumnIndex("t12")));
                    preparedStmt.setString(17, cursor.getString(cursor.getColumnIndex("t13")));
                    preparedStmt.setString(18, cursor.getString(cursor.getColumnIndex("t14")));
                    preparedStmt.setString(19, cursor.getString(cursor.getColumnIndex("t15")));
                    preparedStmt.setString(20, cursor.getString(cursor.getColumnIndex("t16")));
                    preparedStmt.setString(21, cursor.getString(cursor.getColumnIndex("t17")));
                    preparedStmt.setString(22, cursor.getString(cursor.getColumnIndex("t18")));
                    preparedStmt.setString(23, cursor.getString(cursor.getColumnIndex("t19")));
                    preparedStmt.setString(24, cursor.getString(cursor.getColumnIndex("t20")));
                    preparedStmt.setString(25, cursor.getString(cursor.getColumnIndex("t21")));
                    preparedStmt.setString(26, cursor.getString(cursor.getColumnIndex("t22")));
                    preparedStmt.setString(27, cursor.getString(cursor.getColumnIndex("t23")));
                    preparedStmt.setString(28, cursor.getString(cursor.getColumnIndex("t24")));
                    preparedStmt.setString(29, cursor.getString(cursor.getColumnIndex("t25")));
                    preparedStmt.setString(30, cursor.getString(cursor.getColumnIndex("t26")));
                    preparedStmt.setString(31, cursor.getString(cursor.getColumnIndex("t27")));
                    preparedStmt.setString(32, cursor.getString(cursor.getColumnIndex("t28")));
                    preparedStmt.setString(33, cursor.getString(cursor.getColumnIndex("t29")));
                    preparedStmt.setString(34, cursor.getString(cursor.getColumnIndex("t30")));
                    preparedStmt.setString(35, cursor.getString(cursor.getColumnIndex("t31")));
                    preparedStmt.setString(36, cursor.getString(cursor.getColumnIndex("t32")));
                    preparedStmt.setString(37, cursor.getString(cursor.getColumnIndex("t33")));
                    preparedStmt.setString(38, cursor.getString(cursor.getColumnIndex("t34")));

                    preparedStmt.execute();

                    //String query = " insert into TSUSAIDHoursTab(EMP_NUM,EMP_NAM,SUP_MAIL,MONTH,YEAR,PROJ_NAM,DAY12,DAY13 ,DAY14 ,DAY15 ,DAY16 ,DAY17 ,DAY18 ,DAY19 ,DAY20 ,DAY21,DAY22 ,DAY23 ,DAY24 ,DAY25 ,DAY26 ,DAY27 ,DAY28 ,DAY29 ,DAY30 ,DAY31 ,DAY1,DAY2 ,DAY3 ,DAY4 ,DAY5 ,DAY6 ,DAY7 ,DAY8 ,DAY9 ,DAY10 ,DAY11 ,DOC_STA ,DOC_VER ,ACT_VER)" + " values (?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }
                updateVehicleChecklistStatus();
                connect.close();
            } else
            {
                noVehicleChecklistsPendingPosting();//ConnectionResult = "Check Connection";
            }

        } catch (Exception ex)
        {
            Log.e("error", ex.getMessage());
        }



    }

    public  Cursor getVehicleChecklistsPendingPosting() {
       // SQLiteDatabase Db = this.getWritableDatabase();
        String query = "select * from  VehicleChecklist where SyncStatus='Not Synced'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public boolean updateVehicleChecklistStatus() {
        try {
            //SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("SyncStatus", "Synced");
            db.update("VehicleChecklist", contentValues, "SyncStatus = 'Not Synced'", (String[]) null);
            //writableDatabase.close();
            successSyncPopupMessage();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public void successSyncPopupMessage() {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.my_dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("All completed assessments have been synced successfully");
        builder.setTitle("Success");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass6 */

            public void onClick(DialogInterface dialogInterface, int i) {
                //Reg.this.pbar.setVisibility(View.INVISIBLE);
                ;
            }
        });
        builder.create().show();
    }

    public void incompleteChecklistSyncPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.my_dialog);
        builder.setMessage("Please note you have to complete checklists before syncing");
        builder.setTitle("Incomplete checklist");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass6 */

            public void onClick(DialogInterface dialogInterface, int i) {
                //Reg.this.pbar.setVisibility(View.INVISIBLE);
                ;
            }
        });
        builder.create().show();
    }

    public void noVehicleChecklistsPendingPosting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.my_dialog);
        builder.setMessage("No vehicle checklists pending posting are available");
        builder.setTitle("No checklists to post");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass6 */

            public void onClick(DialogInterface dialogInterface, int i) {
                //Reg.this.pbar.setVisibility(View.INVISIBLE);
                ;
            }
        });
        builder.create().show();
    }
}
