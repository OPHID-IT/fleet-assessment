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
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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
    LinearLayout linearLayout;


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
        linearLayout = (LinearLayout)root.findViewById(R.id.homeLinearLayout);

        displayUnsyncedRecords();




        //regnum.setText(regNumber);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String str) {
                return true;
            }
            public boolean onQueryTextSubmit(String str) {
               /* if (!haveNetworkConnection()) {
                    noConnectionPopupMessage();
                    return true;
                }*/

                openVehicleChecklist();
                return true;
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass2 */

            public void onClick(View view) {
                postVehicleChecklists();
                postMotorcycleChecklists();
                postVehicleInspections();
                displayUnsyncedRecords();

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



    @SuppressLint("Range")
    public boolean doesRegNumberExistInLocalTableAndIsNotSynced(String regNumber)
    {
         Globals.vehicleChecklistTableHasData=false;
        Cursor cursor=null;
        //Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '"+regNumber+"'",null);

       cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '" + regNumber + "' AND SyncStatus= 'Not Synced'", null);

       if(cursor==null || cursor.getCount()>0)
        {
            cursor = db.rawQuery("SELECT * from MotorcycleChecklist where VehicleNumber= '" + regNumber + "' AND SyncStatus= 'Not Synced'", null);
        }


        if (cursor!=null && cursor.getCount()>0)
        {
            Globals.vehicleChecklistTableHasData = true;
            Globals.vehicleCategory=cursor.getString(cursor.getColumnIndex("category"));
        }
        else if (cursor==null || cursor.getCount()==0) {
            Globals.vehicleChecklistTableHasData = false;
            Globals.vehicleCategory="";
        }

        return Globals.vehicleChecklistTableHasData;
    }

    @SuppressLint("Range")
    public boolean doesRegNumberExistInLocalVehicleInspectionTableAndIsNotSynced(String regNumber)
    {
        Globals.vehicleInspectionTableHasData=false;
        //Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '"+regNumber+"'",null);
        Cursor cursor=null;

            cursor = db.rawQuery("SELECT VehicleNumber, SyncStatus from VehicleInspection where VehicleNumber= '"+regNumber+"' AND SyncStatus= 'Not Synced'",null);
        if(cursor==null || cursor.getCount()>0)
        {
            cursor = db.rawQuery("SELECT VehicleNumber, SyncStatus from MotorcycleInspection where VehicleNumber= '"+regNumber+"' AND SyncStatus= 'Not Synced'",null);

        }


        if (cursor!=null && cursor.getCount()>0)
        {
            Globals.vehicleInspectionTableHasData = true;
            Globals.vehicleCategory=cursor.getString(cursor.getColumnIndex("category"));

        }
        else if (cursor==null || cursor.getCount()==0) {
            Globals.vehicleInspectionTableHasData = false;
            Globals.vehicleCategory="";
        }

        return Globals.vehicleInspectionTableHasData;
    }


    public void openVehicleChecklistORI() {
        Globals.vehicleRegNumber = ((searchView.getQuery().toString()).trim()).toUpperCase();
        //assignDatabaseVariables(regNumber);
        if(doesRegNumberExistInLocalTableAndIsNotSynced(Globals.vehicleRegNumber))
        {
            regnum.setText(Globals.vehicleRegNumber);
                            if(Globals.vehicleCategory.equals("CAR"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(true);
                                nav_itemB.setVisible(true);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(false);

                            }
                            else if(Globals.vehicleCategory.equals("MOTORCYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(true);
                                nav_itemD.setVisible(true);
                                nav_itemE.setVisible(false);
                            }
                            else if(Globals.vehicleCategory.equals("BICYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(true);
                            }
            successPopupMessage();

        } else if(doesRegNumberExistInLocalVehicleInspectionTableAndIsNotSynced(Globals.vehicleRegNumber))
        {
            regnum.setText(Globals.vehicleRegNumber);
                            if(Globals.vehicleCategory.equals("CAR"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(true);
                                nav_itemB.setVisible(true);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(false);

                            }
                            else if(Globals.vehicleCategory.equals("MOTORCYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(true);
                                nav_itemD.setVisible(true);
                                nav_itemE.setVisible(false);
                            }
                            else if(Globals.vehicleCategory.equals("BICYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(true);
                            }

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
                            Globals.vehicleCategory=executeQuery.getString("category");

                            if(Globals.vehicleCategory.equals("CAR"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(true);
                                nav_itemB.setVisible(true);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(false);

                            }
                            else if(Globals.vehicleCategory.equals("MOTORCYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(true);
                                nav_itemD.setVisible(true);
                                nav_itemE.setVisible(false);
                            }
                            else if(Globals.vehicleCategory.equals("BICYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(true);
                            }

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
    public void openVehicleChecklist() {
        Globals.vehicleRegNumber = ((searchView.getQuery().toString()).trim()).toUpperCase();
        //assignDatabaseVariables(regNumber);
        if(doesRegNumberExistInLocalTableAndIsNotSynced(Globals.vehicleRegNumber))
        {
            regnum.setText(Globals.vehicleRegNumber);
            if(Globals.vehicleCategory.equals("CAR"))
            {
                Menu menuNav = MainActivity.getNavigationView().getMenu();
                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                nav_itemA.setVisible(true);
                nav_itemB.setVisible(true);
                nav_itemC.setVisible(false);
                nav_itemD.setVisible(false);
                nav_itemE.setVisible(false);

            }
            else if(Globals.vehicleCategory.equals("MOTORCYCLE"))
            {
                Menu menuNav = MainActivity.getNavigationView().getMenu();
                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                nav_itemA.setVisible(false);
                nav_itemB.setVisible(false);
                nav_itemC.setVisible(true);
                nav_itemD.setVisible(true);
                nav_itemE.setVisible(false);
            }
            else if(Globals.vehicleCategory.equals("BICYCLE"))
            {
                Menu menuNav = MainActivity.getNavigationView().getMenu();
                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                nav_itemA.setVisible(false);
                nav_itemB.setVisible(false);
                nav_itemC.setVisible(false);
                nav_itemD.setVisible(false);
                nav_itemE.setVisible(true);
            }
            successPopupMessage();

        } else if(doesRegNumberExistInLocalVehicleInspectionTableAndIsNotSynced(Globals.vehicleRegNumber))
        {
            regnum.setText(Globals.vehicleRegNumber);
            if(Globals.vehicleCategory.equals("CAR"))
            {
                Menu menuNav = MainActivity.getNavigationView().getMenu();
                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                nav_itemA.setVisible(true);
                nav_itemB.setVisible(true);
                nav_itemC.setVisible(false);
                nav_itemD.setVisible(false);
                nav_itemE.setVisible(false);

            }
            else if(Globals.vehicleCategory.equals("MOTORCYCLE"))
            {
                Menu menuNav = MainActivity.getNavigationView().getMenu();
                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                nav_itemA.setVisible(false);
                nav_itemB.setVisible(false);
                nav_itemC.setVisible(true);
                nav_itemD.setVisible(true);
                nav_itemE.setVisible(false);
            }
            else if(Globals.vehicleCategory.equals("BICYCLE"))
            {
                Menu menuNav = MainActivity.getNavigationView().getMenu();
                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                nav_itemA.setVisible(false);
                nav_itemB.setVisible(false);
                nav_itemC.setVisible(false);
                nav_itemD.setVisible(false);
                nav_itemE.setVisible(true);
            }

            successPopupMessage();
        }
        else {

            Cursor cursor = db.rawQuery("SELECT * from ophid_vehicles where license_number ='" + Globals.vehicleRegNumber + "'",null);


            try {
               // Connection connectionclass = new ConnectionHelper().connectionclass();
              //  connect = connectionclass;
               // if (connectionclass != null) {
                   // ResultSet executeQuery = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select * from ophid_vehicles WHERE license_number ='" + Globals.vehicleRegNumber + "'");

                    if (cursor==null || cursor.getCount()==0) {
                        //This code below ensures that we retrieve vehicles into the vehicles table if not already there
                        Cursor cursorb = db.rawQuery("SELECT * from ophid_vehicles",null);
                                if (cursorb==null || cursorb.getCount()==0)
                                {
                                    Connection connectionclass = new ConnectionHelper().connectionclass();
                                    connect = connectionclass;
                                                if (connectionclass != null) {
                                                    dbHelper.populateLocalVehicles();
                                                    Globals.showMessage(getActivity(), "Success", "Vehicles retrieved successfully, please search for your vehicle once more");

                                                }
                                                else
                                                {
                                                    Globals.showMessage(getActivity(), "Populating vehicles from remote", "The system needs to upload vehicles from remote,make sure that you have internet connection and Try Again!");
                                                    return;
                                                }
                                }else
                                {
                                    searchNotFoundPopupMessage();
                                    return;
                                }
                    } else {
                        //executeQuery.beforeFirst();
                        successPopupMessage();
                    }

                    if (cursor!=null || cursor.getCount()>0) {
                        //cursor.moveToFirst();

                        while (cursor.moveToNext()) {
                            regnum.setText(cursor.getString(cursor.getColumnIndex("license_number")));
                            Globals.vehicleCategory=cursor.getString(cursor.getColumnIndex("category"));

                            if(Globals.vehicleCategory.equals("CAR"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(true);
                                nav_itemB.setVisible(true);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(false);

                            }
                            else if(Globals.vehicleCategory.equals("MOTORCYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(true);
                                nav_itemD.setVisible(true);
                                nav_itemE.setVisible(false);
                            }
                            else if(Globals.vehicleCategory.equals("BICYCLE"))
                            {
                                Menu menuNav = MainActivity.getNavigationView().getMenu();
                                MenuItem nav_itemA = menuNav.findItem(R.id.nav_vehicle_checklist);
                                MenuItem nav_itemB= menuNav.findItem(R.id.nav_vehicle_inspection);
                                MenuItem nav_itemC= menuNav.findItem(R.id.nav_motorcycle_checklist);
                                MenuItem nav_itemD= menuNav.findItem(R.id.nav_motorcycle_inspection);
                                MenuItem nav_itemE= menuNav.findItem(R.id.nav_bicycle_inspection);

                                nav_itemA.setVisible(false);
                                nav_itemB.setVisible(false);
                                nav_itemC.setVisible(false);
                                nav_itemD.setVisible(false);
                                nav_itemE.setVisible(true);
                            }

                        }
                        return;
                    }

                    return;
               // }

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


    @SuppressLint("Range")
    public void postVehicleInspections()
    {
        Cursor cursor= getVehicleInspectionsPendingPosting();
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
                    cursor.getString(cursor.getColumnIndex("CurrentMileage")).equals("")||
                    cursor.getString(cursor.getColumnIndex("Fuel")).equals("")||
                    cursor.getString(cursor.getColumnIndex("DateTyreLastChanged")).equals("")||
                    cursor.getString(cursor.getColumnIndex("MileageAtLastTyreChange")).equals("")||
                    cursor.getString(cursor.getColumnIndex("DateBatteryLastChanged")).equals(""))

            {
                incompleteVehicleInspectionSyncPopupMessage();
                return;
            }
        }


        cursor=getVehicleInspectionsPendingPosting();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            // String query = " insert into fuel_topup(top_up_code,vehicle_number,driver,speedometer_reading,top_up_reason,top_up_id,service_provider,service_provider_location,payment_method,litres,receipt_number,fuel_cost,where_are_you,top_up_date_time,receipt_image,approval_status)" + " values (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String query = " insert into VehicleInspections(id,EmployeeNumber,VehicleNumber,ActivityDate,r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22,r23,r24,r25,r26,CurrentMileage,Fuel,DateTyreLastChanged,MileageAtLastTyreChange,DateBatteryLastChanged,FrontImage,LeftImage,RightImage,BackImage,ApprovalStatus)" + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";



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
                    preparedStmt.setString(31, cursor.getString(cursor.getColumnIndex("CurrentMileage")));
                    preparedStmt.setString(32, cursor.getString(cursor.getColumnIndex("Fuel")));

                    Date parseDateTyreLastChanged = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(cursor.getColumnIndex("DateTyreLastChanged")));
                    java.sql.Date parseDateTyreLastChangedSqlDate = new java.sql.Date(parseDateTyreLastChanged.getTime());
                    preparedStmt.setDate(33, parseDateTyreLastChangedSqlDate);

                    preparedStmt.setString(34, cursor.getString(cursor.getColumnIndex("MileageAtLastTyreChange")));

                    Date parseDateBatteryLastChanged = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(cursor.getColumnIndex("DateBatteryLastChanged")));
                    java.sql.Date parseDateBatteryLastChangedSqlDate = new java.sql.Date(parseDateBatteryLastChanged.getTime());
                    preparedStmt.setDate(35, parseDateBatteryLastChangedSqlDate);


                    preparedStmt.setBytes(36, cursor.getBlob(cursor.getColumnIndex("FrontImage")));
                    preparedStmt.setBytes(37, cursor.getBlob(cursor.getColumnIndex("LeftImage")));
                    preparedStmt.setBytes(38, cursor.getBlob(cursor.getColumnIndex("RightImage")));
                    preparedStmt.setBytes(39, cursor.getBlob(cursor.getColumnIndex("BackImage")));

                    preparedStmt.setString(40, "NOT ACTIONED");

                    preparedStmt.execute();

                    //String query = " insert into TSUSAIDHoursTab(EMP_NUM,EMP_NAM,SUP_MAIL,MONTH,YEAR,PROJ_NAM,DAY12,DAY13 ,DAY14 ,DAY15 ,DAY16 ,DAY17 ,DAY18 ,DAY19 ,DAY20 ,DAY21,DAY22 ,DAY23 ,DAY24 ,DAY25 ,DAY26 ,DAY27 ,DAY28 ,DAY29 ,DAY30 ,DAY31 ,DAY1,DAY2 ,DAY3 ,DAY4 ,DAY5 ,DAY6 ,DAY7 ,DAY8 ,DAY9 ,DAY10 ,DAY11 ,DOC_STA ,DOC_VER ,ACT_VER)" + " values (?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }
                updateVehicleInspectionStatus();
                connect.close();
            } else
            {
                noVehicleInspectionsPendingPosting();//ConnectionResult = "Check Connection";
            }

        } catch (Exception ex)
        {
            Log.e("error", ex.getMessage());
        }



    }

    @SuppressLint("Range")
    public void postMotorcycleChecklists()
    {


        Cursor cursor= getMotorcycleChecklistsPendingPosting();

        while(cursor.moveToNext()) {

            if (cursor.getString(cursor.getColumnIndex("t2")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t3")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t4")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t5")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t6")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t7")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t8")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t10")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t11")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t12")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t13")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t14")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t15")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t16")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t17")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t18")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t20")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t21")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t22")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t23")).equals("0")||
                    cursor.getString(cursor.getColumnIndex("t24")).equals("0"))

            {

                incompleteChecklistSyncPopupMessage();
                return;
            }

        }



        cursor=getMotorcycleChecklistsPendingPosting();

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            // String query = " insert into fuel_topup(top_up_code,vehicle_number,driver,speedometer_reading,top_up_reason,top_up_id,service_provider,service_provider_location,payment_method,litres,receipt_number,fuel_cost,where_are_you,top_up_date_time,receipt_image,approval_status)" + " values (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String query = " insert into MotorcycleChecklists(id,EmployeeNumber,VehicleNumber,ActivityDate,s2,s3,s4,s5,s6,s7,s8,s10,s11,s12,s13,s14,s15,s16,s17,s18,s20,s21,s22,s23,s24)" + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";



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


                    preparedStmt.setString(5, cursor.getString(cursor.getColumnIndex("t2")));
                    preparedStmt.setString(6, cursor.getString(cursor.getColumnIndex("t3")));
                    preparedStmt.setString(7, cursor.getString(cursor.getColumnIndex("t4")));
                    preparedStmt.setString(8, cursor.getString(cursor.getColumnIndex("t5")));
                    preparedStmt.setString(9, cursor.getString(cursor.getColumnIndex("t6")));
                    preparedStmt.setString(10, cursor.getString(cursor.getColumnIndex("t7")));
                    preparedStmt.setString(11, cursor.getString(cursor.getColumnIndex("t8")));

                    preparedStmt.setString(12, cursor.getString(cursor.getColumnIndex("t10")));
                    preparedStmt.setBytes(13, cursor.getBlob(cursor.getColumnIndex("t11")));
                    preparedStmt.setString(14, cursor.getString(cursor.getColumnIndex("t12")));
                    preparedStmt.setString(15, cursor.getString(cursor.getColumnIndex("t13")));
                    preparedStmt.setString(16, cursor.getString(cursor.getColumnIndex("t14")));
                    preparedStmt.setString(17, cursor.getString(cursor.getColumnIndex("t15")));
                    preparedStmt.setString(18, cursor.getString(cursor.getColumnIndex("t16")));
                    preparedStmt.setString(19, cursor.getString(cursor.getColumnIndex("t17")));
                    preparedStmt.setString(20, cursor.getString(cursor.getColumnIndex("t18")));
                    preparedStmt.setString(21, cursor.getString(cursor.getColumnIndex("t20")));
                    preparedStmt.setString(22, cursor.getString(cursor.getColumnIndex("t21")));
                    preparedStmt.setString(23, cursor.getString(cursor.getColumnIndex("t22")));
                    preparedStmt.setString(24, cursor.getString(cursor.getColumnIndex("t23")));
                    preparedStmt.setString(25, cursor.getString(cursor.getColumnIndex("t24")));


                    preparedStmt.execute();

                    //String query = " insert into TSUSAIDHoursTab(EMP_NUM,EMP_NAM,SUP_MAIL,MONTH,YEAR,PROJ_NAM,DAY12,DAY13 ,DAY14 ,DAY15 ,DAY16 ,DAY17 ,DAY18 ,DAY19 ,DAY20 ,DAY21,DAY22 ,DAY23 ,DAY24 ,DAY25 ,DAY26 ,DAY27 ,DAY28 ,DAY29 ,DAY30 ,DAY31 ,DAY1,DAY2 ,DAY3 ,DAY4 ,DAY5 ,DAY6 ,DAY7 ,DAY8 ,DAY9 ,DAY10 ,DAY11 ,DOC_STA ,DOC_VER ,ACT_VER)" + " values (?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                }
                updateMotorcycleChecklistStatus();
                connect.close();
            } else
            {
                noMotorcycleChecklistsPendingPosting();//ConnectionResult = "Check Connection";
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

    public  Cursor getMotorcycleChecklistsPendingPosting() {
        // SQLiteDatabase Db = this.getWritableDatabase();
        String query = "select * from  MotorcycleChecklist where SyncStatus='Not Synced'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public  Cursor getVehicleInspectionsPendingPosting() {
        // SQLiteDatabase Db = this.getWritableDatabase();
        String query = "select * from  VehicleInspection where SyncStatus='Not Synced'";
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

    public boolean updateVehicleInspectionStatus() {
        try {
            //SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("SyncStatus", "Synced");
            db.update("VehicleInspection", contentValues, "SyncStatus = 'Not Synced'", (String[]) null);
            //writableDatabase.close();
            successSyncPopupMessage();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean updateMotorcycleChecklistStatus() {
        try {
            //SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("SyncStatus", "Synced");
            db.update("MotorcycleChecklist", contentValues, "SyncStatus = 'Not Synced'", (String[]) null);
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

    public void incompleteVehicleInspectionSyncPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.my_dialog);
        builder.setMessage("Please note you have to complete inspections before syncing");
        builder.setTitle("Incomplete vehicle inspection");
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
        builder.setMessage("No vehicle checklists pending posting");
        builder.setTitle("");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass6 */

            public void onClick(DialogInterface dialogInterface, int i) {
                //Reg.this.pbar.setVisibility(View.INVISIBLE);
                ;
            }
        });
        builder.create().show();
    }

    public void noVehicleInspectionsPendingPosting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.my_dialog);
        builder.setMessage("No vehicle inspections pending posting");
        builder.setTitle("");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass6 */

            public void onClick(DialogInterface dialogInterface, int i) {
                //Reg.this.pbar.setVisibility(View.INVISIBLE);
                ;
            }
        });
        builder.create().show();
    }

    public void noMotorcycleChecklistsPendingPosting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.my_dialog);
        builder.setMessage("No motorcycle checklists pending posting");
        builder.setTitle("");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass6 */

            public void onClick(DialogInterface dialogInterface, int i) {
                //Reg.this.pbar.setVisibility(View.INVISIBLE);
                ;
            }
        });
        builder.create().show();
    }

    @SuppressLint("Range")
    public void displayUnsyncedRecords()
    {
        //clear all items from the layout or refresh layout
        linearLayout.removeAllViews();

        Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where SyncStatus='Not Synced'",null);
        ArrayList<TextView> tv = new ArrayList<>();
        TextView textview1;
        int i = 0;
        LinearLayout.LayoutParams textviewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        textview1 = new TextView(getActivity());
        textview1.setText("Entries pending syncing");
        textviewLayoutParams.setMargins(50, 20, 0, 0);
        textview1.setTypeface(Typeface.DEFAULT_BOLD);
        textview1.setElevation(2);
        textview1.setLayoutParams(textviewLayoutParams);
        textview1.setTextSize(14);
        linearLayout.addView(textview1);


        if(cursor!=null && cursor.getCount()!=0) {
            textview1 = new TextView(getActivity());
            textview1.setText("Vehicle Checklists");
            textviewLayoutParams.setMargins(50, 30, 0, 0);
            textview1.setLayoutParams(textviewLayoutParams);
            textview1.setTextColor(Color.BLACK);
            linearLayout.addView(textview1);
        }


        while(cursor.moveToNext()) {
            @SuppressLint("Range") String regNumber = cursor.getString(cursor.getColumnIndex("VehicleNumber"));

            textview1 = new TextView(getActivity());
            textviewLayoutParams.setMargins(50, 20, 0, 0);
            textview1.setLayoutParams(textviewLayoutParams);
            //textview1.setLayoutParams(new LayoutParams(LayoutParams.,LayoutParams.WRAP_CONTENT));

            textview1.setText(cursor.getString(cursor.getColumnIndex("VehicleNumber")) + "    Activity date: "+ cursor.getString(cursor.getColumnIndex("ActivityDate")));
            textview1.setTextColor(Color.BLUE);

            textview1.setId(i);

            //tv.add(textview1);
            linearLayout.addView(textview1);
            i++;
        }

        cursor = db.rawQuery("SELECT * from VehicleInspection where SyncStatus='Not Synced'",null);

       //This text view below just adds space between the different types of lists
        textview1 = new TextView(getActivity());
        textview1.setText("");
        textviewLayoutParams.setMargins(50, 10, 0, 0);
        textview1.setLayoutParams(textviewLayoutParams);
        textview1.setTextColor(Color.BLACK);
        linearLayout.addView(textview1);

        if(cursor!=null && cursor.getCount()!=0) {
            textview1 = new TextView(getActivity());
            textview1.setText("Vehicle Inspections");
            textviewLayoutParams.setMargins(50, 0, 0, 0);
            textview1.setLayoutParams(textviewLayoutParams);
            textview1.setTextColor(Color.BLACK);
            linearLayout.addView(textview1);
        }

        while(cursor.moveToNext()) {
            @SuppressLint("Range") String regNumber = cursor.getString(cursor.getColumnIndex("VehicleNumber"));

            textview1 = new TextView(getActivity());
            //LinearLayout.LayoutParams textviewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textviewLayoutParams.setMargins(50, 10, 0, 0);
            textview1.setLayoutParams(textviewLayoutParams);
            //textview1.setLayoutParams(new LayoutParams(LayoutParams.,LayoutParams.WRAP_CONTENT));

            textview1.setText(cursor.getString(cursor.getColumnIndex("VehicleNumber")) + "   Activity date: "+ cursor.getString(cursor.getColumnIndex("ActivityDate")));
            textview1.setTextColor(Color.BLUE);

            textview1.setId(i);

            //tv.add(textview1);
            linearLayout.addView(textview1);
            i++;
        }


        cursor = db.rawQuery("SELECT * from MotorcycleChecklist where SyncStatus='Not Synced'",null);

        //This text view below just adds space between the different types of lists
        textview1 = new TextView(getActivity());
        textview1.setText("");
        textviewLayoutParams.setMargins(50, 10, 0, 0);
        textview1.setLayoutParams(textviewLayoutParams);
        textview1.setTextColor(Color.BLACK);
        linearLayout.addView(textview1);

        if(cursor!=null && cursor.getCount()!=0) {
            textview1 = new TextView(getActivity());
            textview1.setText("Motorcycle Checklists");
            textviewLayoutParams.setMargins(50, 0, 0, 0);
            textview1.setLayoutParams(textviewLayoutParams);
            textview1.setTextColor(Color.BLACK);
            linearLayout.addView(textview1);
        }

        while(cursor.moveToNext()) {
            @SuppressLint("Range") String regNumber = cursor.getString(cursor.getColumnIndex("VehicleNumber"));

            textview1 = new TextView(getActivity());
            //LinearLayout.LayoutParams textviewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textviewLayoutParams.setMargins(50, 10, 0, 0);
            textview1.setLayoutParams(textviewLayoutParams);
            //textview1.setLayoutParams(new LayoutParams(LayoutParams.,LayoutParams.WRAP_CONTENT));

            textview1.setText(cursor.getString(cursor.getColumnIndex("VehicleNumber")) + "   Activity date: "+ cursor.getString(cursor.getColumnIndex("ActivityDate")));
            textview1.setTextColor(Color.BLUE);

            textview1.setId(i);

            //tv.add(textview1);
            linearLayout.addView(textview1);
            i++;
        }

    }
}
