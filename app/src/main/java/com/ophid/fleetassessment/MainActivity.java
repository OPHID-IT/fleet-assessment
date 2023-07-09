package com.ophid.fleetassessment;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    static int vehicle_checklist_Completed=0;
    static int motorcycle_checklist_Completed=0;
    static int bicycle_inspection_Completed=0;
    static int vehicle_inspection_Completed=0;
    static int motorcycle_inspection_Completed=0;
    //static String focusedChecklist="No form selected";
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase db;
    public static Fragment vehicleChecklistfragment;
    public static Fragment vehicleInspectionfragment;
    public static Fragment approvalsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Globals.todaysDate = dateFormat.format(calendar.getTime());

        dbHelper = new MySQLiteHelper(MainActivity.this);
        db = dbHelper.getWritableDatabase();

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

      //  if(savedInstanceState==null) {
       //     getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VehicleChecklistFragment()).commit();
       //     navigationView.setCheckedItem(R.id.nav_vehicle_checklist);
      //  }

        //  if(savedInstanceState==null) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        //     navigationView.setCheckedItem(R.id.nav_vehicle_checklist);
        //  }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.nav_vehicle_checklist:
                    if(Globals.vehicleRegNumber.equals("No Vehicle Selected"))
                    {
                        noVehicleSelected();

                    }else {
                        if (doesRegNumberExistInLocalTableAndIsNotSynced(Globals.vehicleRegNumber)) {
                            vehicleAlreadyExistsAndIsNotSyncedPopupMessage();
                        } else {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VehicleChecklistFragment()).commit();
                        }
                    }
                    break;
                case R.id.nav_motorcycle_checklist:
                    // getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new MotorcycleChecklistFragment()).commit();
                    if(Globals.vehicleRegNumber.equals("No Vehicle Selected"))
                    {
                        noVehicleSelected();

                    }else {
                                    Fragment motorcycleChecklistFragment = getSupportFragmentManager().findFragmentByTag("Motorcycle_checklist_tag");
                                    if (motorcycleChecklistFragment == null) {
                                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new MotorcycleChecklistFragment(), "Motorcycle_checklist_tag").addToBackStack("Motorcycle_checklist_tag").commit();
                                        Globals.focusedChecklist = "Motorcycle Checklist";
                                    } else {
                                        getSupportFragmentManager().popBackStackImmediate("Motorcycle_checklist_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_container, motorcycleChecklistFragment, "Motorcycle_checklist_tag");
                                        transaction.commit();
                                        Globals.focusedChecklist = "Motorcycle Checklist";
                                    }
                    }
                    break;
                case R.id.nav_bicycle_inspection:
                    //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new BicycleInspectionFragment()).commit();
                    if(Globals.vehicleRegNumber.equals("No Vehicle Selected"))
                    {
                        noVehicleSelected();

                    }else {

                        Fragment bicycleInspectionFragment = getSupportFragmentManager().findFragmentByTag("Bicycle_Inspection_tag");
                        if (bicycleInspectionFragment == null) {
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new BicycleInspectionFragment(), "Bicycle_Inspection_tag").addToBackStack("Bicycle_Inspection_tag").commit();
                            Globals.focusedChecklist = "Bicycle Inspection";
                        } else {
                            getSupportFragmentManager().popBackStackImmediate("Bicycle_Inspection_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, bicycleInspectionFragment, "Bicycle_Inspection_tag");
                            transaction.commit();
                            Globals.focusedChecklist = "Bicycle Inspection";
                        }
                    }
                    break;
                case R.id.nav_vehicle_inspection:
                    //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new VehicleInspectionFragment()).commit();

                    if(Globals.vehicleRegNumber.equals("No Vehicle Selected"))
                    {
                        noVehicleSelected();

                    }else {
                        if (doesRegNumberExistInLocalVehicleInspectionTableAndIsNotSynced(Globals.vehicleRegNumber)) {
                            vehicleInspectionAlreadyExistsAndIsNotSyncedPopupMessage();
                        } else {

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VehicleInspectionFragment()).commit();
                        }
                    }
                    break;
                case R.id.nav_motorcycle_inspection:
                    //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new MotorcycleInspectionFragment()).commit();
                    if(Globals.vehicleRegNumber.equals("No Vehicle Selected"))
                    {
                        noVehicleSelected();

                    }else {
                        Fragment motorcycleInspectionFragment = getSupportFragmentManager().findFragmentByTag("Motorcycle_Inspection_tag");
                        if (motorcycleInspectionFragment == null) {
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new MotorcycleInspectionFragment(), "Motorcycle_Inspection_tag").addToBackStack("Motorcycle_Inspection_tag").commit();
                            Globals.focusedChecklist = "Motorcycle Inspection";
                        } else {
                            getSupportFragmentManager().popBackStackImmediate("Motorcycle_Inspection_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, motorcycleInspectionFragment, "Motorcycle_Inspection_tag");
                            transaction.commit();
                            Globals.focusedChecklist = "Motorcycle Inspection";
                        }
                    }
                    break;
                case R.id.nav_approvals:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VehicleInspectionApprovalsFragment()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VehicleInspectionApprovalListFragment()).commit();

                    break;
                case R.id.nav_home:
                    //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

                    Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("Home_tag");
                    if (homeFragment == null) {
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomeFragment(), "Home_tag").addToBackStack("Home_tag").commit();
                    } else {
                        getSupportFragmentManager().popBackStackImmediate("Home_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, homeFragment, "Home_tag");
                        transaction.commit();
                    }

                    break;
                case R.id.nav_logout:
                    //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

                    while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    MainActivity.this.finish();
                    Intent myIntent=null;
                    Globals.userCredential=null;
                    myIntent = new Intent(this, LoginActivity.class);
                    startActivity(myIntent);
                    //return  true;

                    break;
            }







        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public boolean doesRegNumberExistInLocalVehicleInspectionTableAndIsNotSynced(String regNumber)
    {
        Globals.vehicleInspectionTableHasData=false;
        //Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '"+regNumber+"'",null);
        Cursor cursor = db.rawQuery("SELECT VehicleNumber, SyncStatus from VehicleInspection where VehicleNumber= '"+regNumber+"' AND SyncStatus= 'Not Synced'",null);

        if (cursor!=null && cursor.getCount()>0)
        {
            Globals.vehicleInspectionTableHasData = true;
        }
        else if (cursor==null || cursor.getCount()==0) {
            Globals.vehicleInspectionTableHasData = false;
        }

        return Globals.vehicleInspectionTableHasData;
    }

    public void vehicleAlreadyExistsAndIsNotSyncedPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please note you have the same vehicle not yet synced on your device");
        builder.setTitle("Vehicle not synced");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.ApprovalList.AnonymousClass4 */

            public void onClick(DialogInterface dialogInterface, int i)

            {
                MainActivity.vehicleChecklistfragment = getSupportFragmentManager().findFragmentByTag("vehicle_checklist_tag");
                if (MainActivity.vehicleChecklistfragment == null) {
                    Globals.focusedChecklist="Vehicle Checklist";
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new VehicleChecklistFragment(),"vehicle_checklist_tag").addToBackStack("vehicle_checklist_tag").commit();

                } else {
                    Globals.focusedChecklist="Vehicle Checklist";
                    getSupportFragmentManager().popBackStackImmediate("vehicle_checklist_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, MainActivity.vehicleChecklistfragment,"vehicle_checklist_tag");
                    transaction.commit();


                }

                // return;
            }
        });
        builder.create().show();
    }


    public void vehicleInspectionAlreadyExistsAndIsNotSyncedPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please note you have the same vehicle not yet synced on your device");
        builder.setTitle("Vehicle not synced");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.ApprovalList.AnonymousClass4 */

            public void onClick(DialogInterface dialogInterface, int i)
            {
                MainActivity.vehicleInspectionfragment = getSupportFragmentManager().findFragmentByTag("vehicle_inspection_tag");
                if (MainActivity.vehicleInspectionfragment == null) {
                    Globals.focusedChecklist="Vehicle Inspection";
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new VehicleInspectionFragment(),"vehicle_inspection_tag").addToBackStack("vehicle_inspection_tag").commit();

                } else {
                    Globals.focusedChecklist="Vehicle Inspection";
                    getSupportFragmentManager().popBackStackImmediate("vehicle_inspection_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, MainActivity.vehicleInspectionfragment,"vehicle_inspection_tag");
                    transaction.commit();


                }

                // return;
            }
        });
        builder.create().show();
    }
    @Override
    public void onBackPressed() {
        //if (drawer.isDrawerOpen(GravityCompat.START)){
        //    drawer.closeDrawer(GravityCompat.START);
       // }else {
           // super.onBackPressed();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                Toast.makeText(this, Globals.questionProgressCount+"", Toast.LENGTH_SHORT).show();

        //  }
    }

    public void noVehicleSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.my_dialog);
        builder.setMessage("Please select a vehicle before you proceed");
        builder.setTitle("No vehicle selected");
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