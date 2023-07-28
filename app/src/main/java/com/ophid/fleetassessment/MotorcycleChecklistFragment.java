package com.ophid.fleetassessment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class MotorcycleChecklistFragment extends Fragment {
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase db;
    private String m_Text = "";
    private  String recordDate;
    private String syncStatus="Not Synced";
   
    ToggleButton t2,t3,t4,t5,t6,t7,t8,t10,t11,t12,t13,t14,t15,t16,t17,t18,t20,t21,t22,t23,t24;
    String t1Value="0",t2Value="0",t3Value="0",t4Value="0",t5Value="0",t6Value="0",t7Value="0",t8Value="0",t9Value="0",t10Value="0",t11Value="0",t12Value="0",t13Value="0",t14Value="0",t15Value="0",t16Value="0",t17Value="0",t18Value="0",t19Value="0",t20Value="0",t21Value="0",t22Value="0",t23Value="0",t24Value="0",t25Value="0",t26Value="0",t27Value="0",t28Value="0",t29Value="0",t30Value="0",t31Value="0",t32Value="0",t33Value="0",t34Value="0";
    String recordId="";

    Button saveButton;
    Boolean longPress = false;

    String selectedChecklist="No form selected";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_vehicle_checklist,container,false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_motorcycle_checklist, null);

        t2 = (ToggleButton) root.findViewById(R.id.t2);
        t3 = (ToggleButton) root.findViewById(R.id.t3);
        t4 = (ToggleButton) root.findViewById(R.id.t4);
        t5 = (ToggleButton) root.findViewById(R.id.t5);
        t6 = (ToggleButton) root.findViewById(R.id.t6);
        t7 = (ToggleButton) root.findViewById(R.id.t7);
        t8 = (ToggleButton) root.findViewById(R.id.t8);

        t10 = (ToggleButton) root.findViewById(R.id.t10);
        t11 = (ToggleButton) root.findViewById(R.id.t11);
        t12 = (ToggleButton) root.findViewById(R.id.t12);
        t13 = (ToggleButton) root.findViewById(R.id.t13);
        t14 = (ToggleButton) root.findViewById(R.id.t14);
        t15 = (ToggleButton) root.findViewById(R.id.t15);
        t16 = (ToggleButton) root.findViewById(R.id.t16);
        t17 = (ToggleButton) root.findViewById(R.id.t17);
        t18 = (ToggleButton) root.findViewById(R.id.t18);

        t20 = (ToggleButton) root.findViewById(R.id.t20);
        t21 = (ToggleButton) root.findViewById(R.id.t21);
        t22 = (ToggleButton) root.findViewById(R.id.t22);
        t23 = (ToggleButton) root.findViewById(R.id.t23);
        t24 = (ToggleButton) root.findViewById(R.id.t24);

        saveButton=(Button) root.findViewById(R.id.saveButton);

        if (Globals.vehicleChecklistTableHasData==true)
        {
            populateMotorcycleCheclistVariables();
        }

        dbHelper = new MySQLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();



        t2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t2,2);

                return true;
            }
        });
        t3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t3,3);
                return true;
            }
        });
        t4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t4,4);
                return true;
            }
        });
        t5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t5,5);
                return true;
            }
        });
        t6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t6,6);
                return true;
            }
        });
        t7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t7,7);
                return true;
            }
        });
        t8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t8,8);
                return true;
            }
        });

        t10.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t10,10);
                return true;
            }
        });
        t11.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t11,11);
                return true;
            }
        });
        t12.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t12,12);
                return true;
            }
        });
        t13.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t13,13);
                return true;
            }
        });
        t14.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t14,14);
                return true;
            }
        });
        t15.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t15,15);
                return true;
            }
        });
        t16.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t16,16);
                return true;
            }
        });
        t17.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t17,17);
                return true;
            }
        });
        t18.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t18,18);
                return true;
            }
        });

        t20.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t20,20);
                return true;
            }
        });
        t21.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t21,21);
                return true;
            }
        });
        t22.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t22,22);
                return true;
            }
        });
        t23.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t23,23);
                return true;
            }
        });
        t24.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t24,24);
                return true;
            }
        });




        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2Value=Globals.stringBooleanToStringNumber(Boolean.toString(t2.isChecked()));
                if(t2.isChecked()){
                    Globals.questionProgressCount += 1;


                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t3Value=Globals.stringBooleanToStringNumber(Boolean.toString(t3.isChecked()));
                if(t3.isChecked()){
                    Globals.questionProgressCount += 1;

                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t4Value=Globals.stringBooleanToStringNumber(Boolean.toString(t4.isChecked()));
                if(t4.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;

                }
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t5Value=Globals.stringBooleanToStringNumber(Boolean.toString(t5.isChecked()));
                if(t5.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t6Value=Globals.stringBooleanToStringNumber(Boolean.toString(t6.isChecked()));
                if(t6.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t7Value=Globals.stringBooleanToStringNumber(Boolean.toString(t7.isChecked()));
                if(t7.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t8Value=Globals.stringBooleanToStringNumber(Boolean.toString(t8.isChecked()));
                if(t8.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });

        t10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t10Value=Globals.stringBooleanToStringNumber(Boolean.toString(t10.isChecked()));
                if(t10.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t11Value=Globals.stringBooleanToStringNumber(Boolean.toString(t11.isChecked()));
                if(t11.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t12Value=Globals.stringBooleanToStringNumber(Boolean.toString(t12.isChecked()));
                if(t12.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t13Value=Globals.stringBooleanToStringNumber(Boolean.toString(t13.isChecked()));
                if(t13.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t14Value=Globals.stringBooleanToStringNumber(Boolean.toString(t14.isChecked()));
                if(t14.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t15Value=Globals.stringBooleanToStringNumber(Boolean.toString(t15.isChecked()));
                if(t15.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t16Value=Globals.stringBooleanToStringNumber(Boolean.toString(t16.isChecked()));
                if(t16.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t17Value=Globals.stringBooleanToStringNumber(Boolean.toString(t17.isChecked()));
                if(t17.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t18Value=Globals.stringBooleanToStringNumber(Boolean.toString(t18.isChecked()));
                if(t18.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });

        t20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t20Value=Globals.stringBooleanToStringNumber(Boolean.toString(t20.isChecked()));
                if(t20.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t21Value=Globals.stringBooleanToStringNumber(Boolean.toString(t21.isChecked()));
                if(t21.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t22Value=Globals.stringBooleanToStringNumber(Boolean.toString(t22.isChecked()));
                if(t22.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t23Value=Globals.stringBooleanToStringNumber(Boolean.toString(t23.isChecked()));
                if(t23.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });
        t24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t24Value=Globals.stringBooleanToStringNumber(Boolean.toString(t24.isChecked()));
                if(t24.isChecked()){
                    Globals.questionProgressCount += 1;
                }else
                {
                    Globals.questionProgressCount-=1;
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recordId = UUID.randomUUID().toString();

                if (Globals.vehicleChecklistTableHasData == false)
                {
                    saveCheckList(recordId,Globals.employeeNumber,Globals.vehicleRegNumber,
                            t2Value,
                            t3Value,
                            t4Value,
                            t5Value,
                            t6Value,
                            t7Value,
                            t8Value,

                            t10Value,
                            t11Value,
                            t12Value,
                            t13Value,
                            t14Value,
                            t15Value,
                            t16Value,
                            t17Value,
                            t18Value,

                            t20Value,
                            t21Value,
                            t22Value,
                            t23Value,
                            t24Value
                           );
                }else if (Globals.vehicleChecklistTableHasData == true)
                {
                    updateCheckList(recordId,Globals.employeeNumber,Globals.vehicleRegNumber,
                            t2Value,
                            t3Value,
                            t4Value,
                            t5Value,
                            t6Value,
                            t7Value,
                            t8Value,

                            t10Value,
                            t11Value,
                            t12Value,
                            t13Value,
                            t14Value,
                            t15Value,
                            t16Value,
                            t17Value,
                            t18Value,

                            t20Value,
                            t21Value,
                            t22Value,
                            t23Value,
                            t24Value
                            );
                }
            }
        });


        return root;
    }

    public void saveCheckList(String id, String employee_number, String vehicle_number, String t2,String t3,String t4,String t5,String t6,String t7,String t8,String t10,String t11,String t12,String t13,String t14,String t15,String t16,String t17,String t18,String t20,String t21,String t22,String t23,String t24) {

        //Date date_stamp = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        //java.sql.Date date_stamp_sqlDate = new java.sql.Date(date_stamp.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("EmployeeNumber", employee_number);
        contentValues.put("VehicleNumber", vehicle_number);
        contentValues.put("ActivityDate", Globals.todaysDate);

        contentValues.put("t2", t2);
        contentValues.put("t3", t3);
        contentValues.put("t4", t4);
        contentValues.put("t5", t5);
        contentValues.put("t6", t6);
        contentValues.put("t7", t7);
        contentValues.put("t8", t8);

        contentValues.put("t10", t10);
        contentValues.put("t11", t11);
        contentValues.put("t12", t12);
        contentValues.put("t13", t13);
        contentValues.put("t14", t14);
        contentValues.put("t15", t15);
        contentValues.put("t16", t16);
        contentValues.put("t17", t17);
        contentValues.put("t18", t18);

        contentValues.put("t20", t20);
        contentValues.put("t21", t21);
        contentValues.put("t22", t22);
        contentValues.put("t23", t23);
        contentValues.put("t24", t24);

        // contentValues.put("SyncStatus", "PENDING");

        db.insert("MotorcycleChecklist",null,contentValues);
        recordSavedPopupMessage();

    }


    public void updateCheckList(String id, String employee_number,String vehicle_number,String t2,String t3,String t4,String t5,String t6,String t7,String t8,String t10,String t11,String t12,String t13,String t14,String t15,String t16,String t17,String t18,String t20,String t21,String t22,String t23,String t24) {

        //Date date_stamp = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        //java.sql.Date date_stamp_sqlDate = new java.sql.Date(date_stamp.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("EmployeeNumber", employee_number);
        contentValues.put("VehicleNumber", vehicle_number);
        contentValues.put("ActivityDate", Globals.todaysDate);

        contentValues.put("t2", t2);
        contentValues.put("t3", t3);
        contentValues.put("t4", t4);
        contentValues.put("t5", t5);
        contentValues.put("t6", t6);
        contentValues.put("t7", t7);
        contentValues.put("t8", t8);

        contentValues.put("t10", t10);
        contentValues.put("t11", t11);
        contentValues.put("t12", t12);
        contentValues.put("t13", t13);
        contentValues.put("t14", t14);
        contentValues.put("t15", t15);
        contentValues.put("t16", t16);
        contentValues.put("t17", t17);
        contentValues.put("t18", t18);

        contentValues.put("t20", t20);
        contentValues.put("t21", t21);
        contentValues.put("t22", t22);
        contentValues.put("t23", t23);
        contentValues.put("t24", t24);

        db.update("MotorcycleChecklist", contentValues, "VehicleNumber='" + vehicle_number + "'", null);

        recordSavedPopupMessage();

    }

    public void recordSavedPopupMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Record saved successfully");
        builder.setTitle("Record Saved");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.ApprovalList.AnonymousClass4 */

            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();


            }
        });
        builder.create().show();
    }

    public void inputDialogz(TextView x, int numz) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView title = new TextView(getActivity());
        title.setText("Enter comment");
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTextSize(16);
        title.setPadding(15,0,0,0);
        builder.setCustomTitle(title);
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();

                switch(numz)
                {
                    case 2:
                        t2Value=m_Text;
                        break;
                    case 3:
                        t3Value=m_Text;
                        break;
                    case 4:
                        t4Value=m_Text;
                        break;
                    case 5:
                        t5Value=m_Text;
                        break;
                    case 6:
                        t6Value=m_Text;
                        break;
                    case 7:
                        t7Value=m_Text;
                        break;
                    case 8:
                        t8Value=m_Text;
                        break;
                    case 10:
                        t10Value=m_Text;
                        break;
                    case 11:
                        t11Value=m_Text;
                        break;
                    case 12:
                        t12Value=m_Text;
                        break;
                    case 13:
                        t13Value=m_Text;
                        break;
                    case 14:
                        t14Value=m_Text;
                        break;
                    case 15:
                        t15Value=m_Text;
                        break;
                    case 16:
                        t16Value=m_Text;
                        break;
                    case 17:
                        t17Value=m_Text;
                        break;
                    case 18:
                        t18Value=m_Text;
                        break;
                    case 20:
                        t20Value=m_Text;
                        break;
                    case 21:
                        t21Value=m_Text;
                        break;
                    case 22:
                        t22Value=m_Text;
                        break;
                    case 23:
                        t23Value=m_Text;
                        break;
                    case 24:
                        t24Value=m_Text;
                        break;

                }



                x.setTextColor(Color.WHITE);
                x.setBackgroundColor(Color.RED);
                x.setBackground(getResources().getDrawable(R.drawable.selector_revised_comment));
                x.setSelected(true);
                Globals.questionProgressCount += 1;

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        //Toast xx=Toast.makeText(this,m_Text,Toast.LENGTH_LONG);


    }

    @SuppressLint("Range")
    public void populateMotorcycleCheclistVariables()
    {
        dbHelper = new MySQLiteHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '"+Globals.vehicleRegNumber+"' AND SyncStatus= '" + Globals.vehicleRegNumber +"'",null);
        Cursor cursor = db.rawQuery("SELECT * from MotorCycleChecklist where VehicleNumber= '"+Globals.vehicleRegNumber+"' AND SyncStatus= 'Not Synced'",null);

        // Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist","VehicleNumber= '"+Globals.vehicleRegNumber+"'",null);


        while(cursor.moveToNext()) {
            //   cursor.getString(cursor.getColumnIndex("VehicleNumber")),
            //      cursor.getString(cursor.getColumnIndex("ActivityDate")),
            Globals.vehicleRegNumber=cursor.getString(cursor.getColumnIndex("VehicleNumber"));
            recordDate=cursor.getString(cursor.getColumnIndex("ActivityDate"));
            syncStatus=cursor.getString(cursor.getColumnIndex("SyncStatus"));


            setButtonStateOnDatabaseRetrieval(t2,cursor.getString(cursor.getColumnIndex("t2")));
            t2Value=cursor.getString(cursor.getColumnIndex("t2"));

            setButtonStateOnDatabaseRetrieval(t3,cursor.getString(cursor.getColumnIndex("t3")));
            t3Value=cursor.getString(cursor.getColumnIndex("t3"));

            setButtonStateOnDatabaseRetrieval(t4,cursor.getString(cursor.getColumnIndex("t4")));
            t4Value=cursor.getString(cursor.getColumnIndex("t4"));

            setButtonStateOnDatabaseRetrieval(t5,cursor.getString(cursor.getColumnIndex("t5")));
            t5Value=cursor.getString(cursor.getColumnIndex("t5"));

            setButtonStateOnDatabaseRetrieval(t6,cursor.getString(cursor.getColumnIndex("t6")));
            t6Value=cursor.getString(cursor.getColumnIndex("t6"));

            setButtonStateOnDatabaseRetrieval(t7,cursor.getString(cursor.getColumnIndex("t7")));
            t7Value=cursor.getString(cursor.getColumnIndex("t7"));

            setButtonStateOnDatabaseRetrieval(t8,cursor.getString(cursor.getColumnIndex("t8")));
            t8Value=cursor.getString(cursor.getColumnIndex("t8"));


            setButtonStateOnDatabaseRetrieval(t10,cursor.getString(cursor.getColumnIndex("t10")));
            t10Value=cursor.getString(cursor.getColumnIndex("t10"));

            setButtonStateOnDatabaseRetrieval(t11,cursor.getString(cursor.getColumnIndex("t11")));
            t11Value=cursor.getString(cursor.getColumnIndex("t11"));

            setButtonStateOnDatabaseRetrieval(t12,cursor.getString(cursor.getColumnIndex("t12")));
            t12Value=cursor.getString(cursor.getColumnIndex("t12"));

            setButtonStateOnDatabaseRetrieval(t13,cursor.getString(cursor.getColumnIndex("t13")));
            t13Value=cursor.getString(cursor.getColumnIndex("t13"));

            setButtonStateOnDatabaseRetrieval(t14,cursor.getString(cursor.getColumnIndex("t14")));
            t14Value=cursor.getString(cursor.getColumnIndex("t14"));

            setButtonStateOnDatabaseRetrieval(t15,cursor.getString(cursor.getColumnIndex("t15")));
            t15Value=cursor.getString(cursor.getColumnIndex("t15"));

            setButtonStateOnDatabaseRetrieval(t16,cursor.getString(cursor.getColumnIndex("t16")));
            t16Value=cursor.getString(cursor.getColumnIndex("t16"));

            setButtonStateOnDatabaseRetrieval(t17,cursor.getString(cursor.getColumnIndex("t17")));
            t17Value=cursor.getString(cursor.getColumnIndex("t17"));

            setButtonStateOnDatabaseRetrieval(t18,cursor.getString(cursor.getColumnIndex("t18")));
            t18Value=cursor.getString(cursor.getColumnIndex("t18"));

            setButtonStateOnDatabaseRetrieval(t20,cursor.getString(cursor.getColumnIndex("t20")));
            t20Value=cursor.getString(cursor.getColumnIndex("t20"));

            setButtonStateOnDatabaseRetrieval(t21,cursor.getString(cursor.getColumnIndex("t21")));
            t21Value=cursor.getString(cursor.getColumnIndex("t21"));

            setButtonStateOnDatabaseRetrieval(t22,cursor.getString(cursor.getColumnIndex("t22")));
            t22Value=cursor.getString(cursor.getColumnIndex("t22"));

            setButtonStateOnDatabaseRetrieval(t23,cursor.getString(cursor.getColumnIndex("t23")));
            t23Value=cursor.getString(cursor.getColumnIndex("t23"));

            setButtonStateOnDatabaseRetrieval(t24,cursor.getString(cursor.getColumnIndex("t24")));
            t24Value=cursor.getString(cursor.getColumnIndex("t24"));

        }
    }

    public void setButtonStateOnDatabaseRetrieval(ToggleButton tb,String val)
    {
        if(val.equals("0")||val.equals("1"))
        {
            tb.setChecked(Globals.integerStringToBoolean(val));
        }else
        {
            tb.setTextColor(Color.WHITE);
            tb.setBackgroundColor(Color.RED);
            tb.setBackground(getResources().getDrawable(R.drawable.selector_revised_comment));
            tb.setSelected(true);
        }
    }
}
