package com.ophid.fleetassessment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;

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

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;


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
                populateVehicleListFromRemote(regNumber);

                return true;
            }
        });

        return root;
    }



    public void populateVehicleListFromRemote(String regNumber) {

        try {


            Connection connectionclass = new ConnectionHelper().connectionclass();
            connect = connectionclass;
            if (connectionclass != null) {
                Toast.makeText(getActivity(), "connected", Toast.LENGTH_SHORT).show();
                ResultSet rs = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select id,EmployeeNumber,VehicleNumber,SyncedDateTimeStamp from VehicleInspections where VehicleNumber= '"+ regNumber +"'");
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

                        //LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
                       // llc = new LinearLayout(getActivity());
                       // layout.setOrientation(LinearLayout.VERTICAL);
                        Button btnAddARoom = new Button(getActivity());
                        btnAddARoom.setAllCaps(false);
                        btnAddARoom.setElevation(20);
                        btnAddARoom.setText("Employee#:" +employeeNumber +  "   Upload date: "+dateRecordSynced);
                        //btnAddARoom.setLayoutParams(params);
                        llc.addView(btnAddARoom);


                    }
                }else
                {
                    selectedRegNum.setText("No Vehicle Selected");
                    searchNotFoundPopupMessage();
                }
                // populateVehicleInspectionVariables();



            }

        }
        catch (Exception e) {
            Log.e("error", e.getMessage());
        }

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
        builder.setMessage("No records pending approval found for "+ regNumber);
        builder.setTitle("No records found");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.ApprovalList.AnonymousClass4 */
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}