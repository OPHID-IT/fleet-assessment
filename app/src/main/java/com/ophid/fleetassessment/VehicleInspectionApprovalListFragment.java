package com.ophid.fleetassessment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;


public class VehicleInspectionApprovalListFragment extends Fragment {
    private Connection connect;
    String recordId,regNumber,dateRecordSynced,employeeNumber;
    private SearchView searchView;
    TextView selectedRegNum;
    LinearLayout llc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_approvals_list, container, false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_approvals_list, null);
        searchView = (SearchView) root.findViewById(R.id.searchView);
        selectedRegNum = (TextView) root.findViewById(R.id.regNumTextView);
        llc=(LinearLayout)root.findViewById(R.id.homeLinearLayout);

        if(!Globals.vehicleRegNumber.equals("No Vehicle Selected"))
        {
            //populateVehicleListFromRemote(Globals.vehicleRegNumber);
            llc.removeAllViews();
            AsyncTaskPopulateVehicleListFromRemote runner = new AsyncTaskPopulateVehicleListFromRemote();
            runner.execute(Globals.vehicleRegNumber);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String str) {
                return true;
            }
            public boolean onQueryTextSubmit(String str) {
                if (!haveNetworkConnection()) {
                    noConnectionPopupMessage();
                    return true;
                }
                regNumber=((searchView.getQuery().toString()).trim()).toUpperCase();
              //  populateVehicleListFromRemote(regNumber);
                llc.removeAllViews();
                AsyncTaskPopulateVehicleListFromRemote runner = new AsyncTaskPopulateVehicleListFromRemote();
                runner.execute(regNumber);
                //This value below will be used to refresh the search query next time this fragment comes into focus
                Globals.vehicleRegNumber=regNumber;

                return true;
            }
        });

        return root;
    }



    /*public void populateVehicleListFromRemote(String regNumber) {

        try {


            Connection connectionclass = new ConnectionHelper().connectionclass();
            connect = connectionclass;
            if (connectionclass != null) {
                Toast.makeText(getActivity(), "connected", Toast.LENGTH_SHORT).show();
                ResultSet rs = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select id,EmployeeNumber,VehicleNumber,SyncedDateTimeStamp,ApprovalStatus from VehicleInspections where VehicleNumber= '"+ regNumber +"' AND ApprovalStatus='NOT ACTIONED'");
                llc.removeAllViews();

                if(rs.next()) {
                    selectedRegNum.setText(regNumber);
                    rs.beforeFirst();

                    while (rs.next()) {
                        recordId = rs.getString("id");
                        employeeNumber = rs.getString("EmployeeNumber");
                        //regNumber=rs.getString("VehicleNumber");
                        dateRecordSynced = DateFormat.format("yyyy-MM-dd HH:mm:ss", rs.getDate("SyncedDateTimeStamp")).toString();

                        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        dateRecordSynced = simpleDateFormat.format(rs.getTimestamp("SyncedDateTimeStamp")).toString();

                        Button btnAddVehicle = new Button(getActivity());
                        btnAddVehicle.setAllCaps(false);
                        btnAddVehicle.setText("Employee#:" +employeeNumber +  "   Upload date: "+dateRecordSynced);
                        btnAddVehicle.setText(recordId);
                        btnAddVehicle.setTag(recordId);

                        //btnAddARoom.setLayoutParams(params);

                        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                   Globals.vehicleInspectionRecordId=btnAddVehicle.getTag().toString();
                                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VehicleInspectionApprovalsFragment()).commit();
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new VehicleInspectionApprovalsFragment()).commit();

                            }

                        });

                        llc.addView(btnAddVehicle);


                    }
                }else
                {
                    selectedRegNum.setText("No Vehicle Selected");
                    searchNotFoundPopupMessage(regNumber);
                }
                // populateVehicleInspectionVariables();



            }

        }
        catch (Exception e) {
            Log.e("error", e.getMessage());
        }

    }*/

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

    public void searchNotFoundPopupMessage(String reg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("No records pending approval found for "+ reg);
        builder.setTitle("No records found");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.ApprovalList.AnonymousClass4 */
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }


    private class AsyncTaskPopulateVehicleListFromRemote extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Retriving values..."); // Calls onProgressUpdate()
            regNumber=params[0];
            Globals.vehicleRegNumber=params[0];
            try {


                Connection connectionclass = new ConnectionHelper().connectionclass();
                connect = connectionclass;
                if (connectionclass != null) {
                   // Toast.makeText(getActivity(), "connected", Toast.LENGTH_SHORT).show();
                    ResultSet rs = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select id,EmployeeNumber,VehicleNumber,SyncedDateTimeStamp,ApprovalStatus from VehicleInspections where VehicleNumber= '"+ regNumber +"' AND ApprovalStatus='NOT ACTIONED'");
                    //llc.removeAllViews();

                    if(rs.next()) {

                        rs.beforeFirst();

                        while (rs.next()) {
                            recordId = rs.getString("id");
                            employeeNumber = rs.getString("EmployeeNumber");
                            //regNumber=rs.getString("VehicleNumber");
                            dateRecordSynced = DateFormat.format("yyyy-MM-dd HH:mm:ss", rs.getDate("SyncedDateTimeStamp")).toString();

                            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                            dateRecordSynced = simpleDateFormat.format(rs.getTimestamp("SyncedDateTimeStamp")).toString();

                            Button btnAddVehicle = new Button(getActivity());
                            btnAddVehicle.setAllCaps(false);
                            btnAddVehicle.setText("Employee#:" +employeeNumber +  "   Upload date: "+dateRecordSynced);
                             //btnAddVehicle.setText(recordId);
                             btnAddVehicle.setTag(recordId);

                            //btnAddARoom.setLayoutParams(params);
                            btnAddVehicle.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v)
                                                        {
                                                            //Globals.vehicleInspectionRecordId=recordId;
                                                            Globals.vehicleInspectionRecordId=btnAddVehicle.getTag().toString();
                                                            //Toast.makeText(getActivity(), Globals.vehicleInspectionRecordId, Toast.LENGTH_SHORT).show();
                                                            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VehicleInspectionApprovalsFragment()).commit();
                                                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new VehicleInspectionApprovalsFragment()).commit();

                                                        }

                                                    });

                                          getActivity().runOnUiThread(new Runnable() {
                                             @Override
                                             public void run()
                                             {

                                                                llc.addView(btnAddVehicle);

                                            }
                                        });

                        }

                    }else
                    {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // This wont run without the handler as long as its inside the async
                                searchNotFoundPopupMessage(params[0]);
                                regNumber="No Vehicle Selected";
                                Globals.vehicleRegNumber="No Vehicle Selected";
                            }
                        });

                    }
                    // populateVehicleInspectionVariables();



                }

            }
            catch (Exception e) {
                Log.e("error", e.getMessage());
            }


            return regNumber;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            selectedRegNum.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Retreiving values");
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }


}