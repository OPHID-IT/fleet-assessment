package com.ophid.fleetassessment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.itextpdf.text.pdf.codec.TIFFConstants;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class VehicleInspectionFragment extends Fragment {
    Boolean longPress = false;
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase db;
    String selectedChecklist="No form selected";
    private  String recordDate;
    private String syncStatus="Not Synced";
    String currentLongPressValue="0";
    private DatePickerDialog picker;
    ToggleButton t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23,t24,t25,t26;
    String t1Value="0",t2Value="0",t3Value="0",t4Value="0",t5Value="0",t6Value="0",t7Value="0",t8Value="0",t9Value="0",t10Value="0",t11Value="0",t12Value="0",t13Value="0",t14Value="0",t15Value="0",t16Value="0",t17Value="0",t18Value="0",t19Value="0",t20Value="0",t21Value="0",t22Value="0",t23Value="0",t24Value="0",t25Value="0",t26Value="0";
    RadioButton radio_quarter, radio_half, radio_three_quarters, radio_full;
    String currentMileageValue="", lastTyreChangeDateValue,lastBatteryChangeDateValue,mileageAtLastTyreChangeValue;
    String radioButtonValue="";
    Bitmap imgValue1,imgValue2,imgValue3,imgValue4;

    String recordId="";
    Button saveButton;

    EditText currMileageEditText,lastTyreChangeDateEditText,lastBatteryChangeDateEditText,mileageAtLastTyreChangeEditText;

    ImageButton fb;
    ImageButton lb;
    ImageButton rb;
    ImageButton bb;

    ImageView fv;
    ImageView lv;
    ImageView rv;
    ImageView bv;

    ImageView imv;
   // private Button btnCaptureImage;
    public String currentPhotoPath;
   // LinearLayout linearLayout;
    Uri imageUri;
    public static Bitmap bm = null;
    private Bitmap photo;
    //public LinearLayout dynamicLayout;
    public ArrayList<Object> tollgateData;
    public ArrayList<Object> tollgateDataStringEncoded;
    public ArrayList<Bitmap> photoArray;
    private String m_Text = "";
    Bitmap[] imagez = new Bitmap[4];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_vehicle_inspection,container,false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_vehicle_inspection, null);

        tollgateData=new ArrayList<Object>();
        tollgateDataStringEncoded=new ArrayList<Object>();

        photoArray = new ArrayList<>();
        currMileageEditText = (EditText) root.findViewById(R.id.current);
        lastTyreChangeDateEditText = (EditText) root.findViewById(R.id.date_tyre_last_changed);
        lastBatteryChangeDateEditText = (EditText) root.findViewById(R.id.date_battery_last_changed);
        mileageAtLastTyreChangeEditText = (EditText) root.findViewById(R.id.mileage_at_last_tyre_change);


        fb=(ImageButton) root.findViewById(R.id.front_button);
        lb=(ImageButton) root.findViewById(R.id.left_button);
        rb=(ImageButton) root.findViewById(R.id.right_button);
        bb=(ImageButton) root.findViewById(R.id.back_button);

        fv=(ImageView) root.findViewById(R.id.front_pic);
        lv=(ImageView) root.findViewById(R.id.left_pic);
        rv=(ImageView) root.findViewById(R.id.right_pic);
        bv=(ImageView) root.findViewById(R.id.back_pic);

        radio_quarter=(RadioButton) root.findViewById(R.id.rbQuarter);
        radio_half=(RadioButton) root.findViewById(R.id.rbHalf);
        radio_three_quarters=(RadioButton) root.findViewById(R.id.rbThreeQuarters);
        radio_full=(RadioButton) root.findViewById(R.id.rbFull);

        t1 = (ToggleButton) root.findViewById(R.id.t1);
        t2 = (ToggleButton) root.findViewById(R.id.t2);
        t3 = (ToggleButton) root.findViewById(R.id.t3);
        t4 = (ToggleButton) root.findViewById(R.id.t4);
        t5 = (ToggleButton) root.findViewById(R.id.t5);
        t6 = (ToggleButton) root.findViewById(R.id.t6);
        t7 = (ToggleButton) root.findViewById(R.id.t7);
        t8 = (ToggleButton) root.findViewById(R.id.t8);
        t9 = (ToggleButton) root.findViewById(R.id.t9);
        t10 = (ToggleButton) root.findViewById(R.id.t10);
        t11 = (ToggleButton) root.findViewById(R.id.t11);
        t12 = (ToggleButton) root.findViewById(R.id.t12);
        t13 = (ToggleButton) root.findViewById(R.id.t13);
        t14 = (ToggleButton) root.findViewById(R.id.t14);
        t15 = (ToggleButton) root.findViewById(R.id.t15);
        t16 = (ToggleButton) root.findViewById(R.id.t16);
        t17 = (ToggleButton) root.findViewById(R.id.t17);
        t18 = (ToggleButton) root.findViewById(R.id.t18);
        t19 = (ToggleButton) root.findViewById(R.id.t19);
        t20 = (ToggleButton) root.findViewById(R.id.t20);
        t21 = (ToggleButton) root.findViewById(R.id.t21);
        t22 = (ToggleButton) root.findViewById(R.id.t22);
        t23 = (ToggleButton) root.findViewById(R.id.t23);
        t24 = (ToggleButton) root.findViewById(R.id.t24);
        t25 = (ToggleButton) root.findViewById(R.id.t25);
        t26 = (ToggleButton) root.findViewById(R.id.t26);

        saveButton=(Button) root.findViewById(R.id.saveButton);
        Globals.vehicleInspectionProgressCount=0;
        Globals.vehicleInspectionDenominator=34;

        if (Globals.vehicleInspectionTableHasData==true)
        {
            populateVehicleInspectionVariables();
        }

        dbHelper = new MySQLiteHelper(getContext());
        db = dbHelper.getWritableDatabase();
        



        t1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t1,1);
                return true;
            }
        });
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
        t9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t9,9);
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
        t19.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t19,19);
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
        t25.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t25,25);
                return true;
            }
        });
        t26.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t26,26);
                return true;
            }
        });


        t1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                t1Value=Globals.stringBooleanToStringNumber(Boolean.toString(t1.isChecked()));
                if(t1.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;

                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2Value=Globals.stringBooleanToStringNumber(Boolean.toString(t2.isChecked()));
                if(t2.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;


                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t3Value=Globals.stringBooleanToStringNumber(Boolean.toString(t3.isChecked()));
                if(t3.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;

                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t4Value=Globals.stringBooleanToStringNumber(Boolean.toString(t4.isChecked()));
                if(t4.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;

                }
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t5Value=Globals.stringBooleanToStringNumber(Boolean.toString(t5.isChecked()));
                if(t5.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t6Value=Globals.stringBooleanToStringNumber(Boolean.toString(t6.isChecked()));
                if(t6.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t7Value=Globals.stringBooleanToStringNumber(Boolean.toString(t7.isChecked()));
                if(t7.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t8Value=Globals.stringBooleanToStringNumber(Boolean.toString(t8.isChecked()));
                if(t8.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t9Value=Globals.stringBooleanToStringNumber(Boolean.toString(t9.isChecked()));
                if(t9.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t10Value=Globals.stringBooleanToStringNumber(Boolean.toString(t10.isChecked()));
                if(t10.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t11Value=Globals.stringBooleanToStringNumber(Boolean.toString(t11.isChecked()));
                if(t11.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t12Value=Globals.stringBooleanToStringNumber(Boolean.toString(t12.isChecked()));
                if(t12.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t13Value=Globals.stringBooleanToStringNumber(Boolean.toString(t13.isChecked()));
                if(t13.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t14Value=Globals.stringBooleanToStringNumber(Boolean.toString(t14.isChecked()));
                if(t14.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t15Value=Globals.stringBooleanToStringNumber(Boolean.toString(t15.isChecked()));
                if(t15.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t16Value=Globals.stringBooleanToStringNumber(Boolean.toString(t16.isChecked()));
                if(t16.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t17Value=Globals.stringBooleanToStringNumber(Boolean.toString(t17.isChecked()));
                if(t17.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t18Value=Globals.stringBooleanToStringNumber(Boolean.toString(t18.isChecked()));
                if(t18.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t19Value=Globals.stringBooleanToStringNumber(Boolean.toString(t19.isChecked()));
                if(t19.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t20Value=Globals.stringBooleanToStringNumber(Boolean.toString(t20.isChecked()));
                if(t20.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t21Value=Globals.stringBooleanToStringNumber(Boolean.toString(t21.isChecked()));
                if(t21.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t22Value=Globals.stringBooleanToStringNumber(Boolean.toString(t22.isChecked()));
                if(t22.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t23Value=Globals.stringBooleanToStringNumber(Boolean.toString(t23.isChecked()));
                if(t23.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t24Value=Globals.stringBooleanToStringNumber(Boolean.toString(t24.isChecked()));
                if(t24.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t25Value=Globals.stringBooleanToStringNumber(Boolean.toString(t25.isChecked()));
                if(t25.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });
        t26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t26Value=Globals.stringBooleanToStringNumber(Boolean.toString(t26.isChecked()));
                if(t26.isChecked()){
                    Globals.vehicleInspectionProgressCount += 1;
                }else
                {
                    Globals.vehicleInspectionProgressCount-=1;
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgValue1 == null || imgValue2 == null || imgValue3 == null || imgValue4 == null) {
                    imagesNotCaptured();
                } else
                {
                                recordId = UUID.randomUUID().toString();
                                currentMileageValue = currMileageEditText.getText().toString();
                                lastTyreChangeDateValue = lastTyreChangeDateEditText.getText().toString();
                                lastBatteryChangeDateValue = lastBatteryChangeDateEditText.getText().toString();
                                mileageAtLastTyreChangeValue = mileageAtLastTyreChangeEditText.getText().toString();

                                if (radio_quarter.isChecked()) {
                                    radioButtonValue = "quarter";
                                } else if (radio_half.isChecked()) {
                                    radioButtonValue = "half";
                                } else if (radio_three_quarters.isChecked()) {
                                    radioButtonValue = "three quarters";
                                } else if (radio_full.isChecked()) {
                                    radioButtonValue = "full";
                                }

                                if (Globals.vehicleInspectionTableHasData == false) {

                                    //Ranga added this to change bitmap to encoded string image
                                    byte[] byteArray;
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                                    imgValue1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byteArray = stream.toByteArray();
                                    String imgValue1StringEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                    imgValue2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byteArray = stream.toByteArray();
                                    String imgValue2StringEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                    imgValue3.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byteArray = stream.toByteArray();
                                    String imgValue3StringEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                    imgValue4.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byteArray = stream.toByteArray();
                                    String imgValue4StringEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);



                                    //Ranga added this to change bitmap to encoded string image ends here


                                    saveVehicleInspection(recordId, Globals.employeeNumber, Globals.vehicleRegNumber, t1Value,
                                            t2Value,
                                            t3Value,
                                            t4Value,
                                            t5Value,
                                            t6Value,
                                            t7Value,
                                            t8Value,
                                            t9Value,
                                            t10Value,
                                            t11Value,
                                            t12Value,
                                            t13Value,
                                            t14Value,
                                            t15Value,
                                            t16Value,
                                            t17Value,
                                            t18Value,
                                            t19Value,
                                            t20Value,
                                            t21Value,
                                            t22Value,
                                            t23Value,
                                            t24Value,
                                            t25Value,
                                            t26Value,
                                            currentMileageValue,
                                            radioButtonValue,
                                            lastTyreChangeDateValue,
                                            mileageAtLastTyreChangeValue,
                                            lastBatteryChangeDateValue,
                                            getBitmapAsByteArray(imgValue1),
                                            getBitmapAsByteArray(imgValue2),
                                            getBitmapAsByteArray(imgValue3),
                                            getBitmapAsByteArray(imgValue4)
                                    );


                                    saveVehicleInspectionStringEncodedImages(recordId, Globals.employeeNumber, Globals.vehicleRegNumber, t1Value,
                                            t2Value,
                                            t3Value,
                                            t4Value,
                                            t5Value,
                                            t6Value,
                                            t7Value,
                                            t8Value,
                                            t9Value,
                                            t10Value,
                                            t11Value,
                                            t12Value,
                                            t13Value,
                                            t14Value,
                                            t15Value,
                                            t16Value,
                                            t17Value,
                                            t18Value,
                                            t19Value,
                                            t20Value,
                                            t21Value,
                                            t22Value,
                                            t23Value,
                                            t24Value,
                                            t25Value,
                                            t26Value,
                                            currentMileageValue,
                                            radioButtonValue,
                                            lastTyreChangeDateValue,
                                            mileageAtLastTyreChangeValue,
                                            lastBatteryChangeDateValue,
                                            imgValue1StringEncoded,
                                            imgValue2StringEncoded,
                                            imgValue3StringEncoded,
                                            imgValue4StringEncoded
                                    );


                                } else if (Globals.vehicleInspectionTableHasData == true) {
                                    updateVehicleInspection(recordId, Globals.employeeNumber, Globals.vehicleRegNumber, t1Value,
                                            t2Value,
                                            t3Value,
                                            t4Value,
                                            t5Value,
                                            t6Value,
                                            t7Value,
                                            t8Value,
                                            t9Value,
                                            t10Value,
                                            t11Value,
                                            t12Value,
                                            t13Value,
                                            t14Value,
                                            t15Value,
                                            t16Value,
                                            t17Value,
                                            t18Value,
                                            t19Value,
                                            t20Value,
                                            t21Value,
                                            t22Value,
                                            t23Value,
                                            t24Value,
                                            t25Value,
                                            t26Value,
                                            currentMileageValue,
                                            radioButtonValue,
                                            lastTyreChangeDateValue,
                                            mileageAtLastTyreChangeValue,
                                            lastBatteryChangeDateValue,
                                            getBitmapAsByteArray(imgValue1),
                                            getBitmapAsByteArray(imgValue2),
                                            getBitmapAsByteArray(imgValue3),
                                            getBitmapAsByteArray(imgValue4)
                                    );
                                }
                }
            }
                });









        lastTyreChangeDateEditText.setOnClickListener(new View.OnClickListener() {
            /* class com.ophid.coasheet.Transact.AnonymousClass2 */

            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                int i = instance.get(5);
                int i2 = instance.get(2);
                int i3 = instance.get(1);
                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    /* class com.ophid.coasheet.Transact.AnonymousClass2.AnonymousClass1 */

                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        //EditText editText = MainActivity.this.txtDateWorked;
                        lastTyreChangeDateEditText.setText(i3 + "/" + (i2 + 1) + "/" + i);
                    }
                }, i3, i2, i);
                picker.show();
            }
        });

        lastBatteryChangeDateEditText.setOnClickListener(new View.OnClickListener() {
            /* class com.ophid.coasheet.Transact.AnonymousClass2 */

            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                int i = instance.get(5);
                int i2 = instance.get(2);
                int i3 = instance.get(1);
                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    /* class com.ophid.coasheet.Transact.AnonymousClass2.AnonymousClass1 */

                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        //EditText editText = MainActivity.this.txtDateWorked;
                        lastBatteryChangeDateEditText.setText(i3 + "/" + (i2 + 1) + "/" + i);
                    }
                }, i3, i2, i);
                picker.show();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    File createTempFile = File.createTempFile("photo", ".jpg", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    String unused = VehicleInspectionFragment.this.currentPhotoPath = createTempFile.getAbsolutePath();
                    //HomeFragment tollgate = HomeFragment.this;
                    imageUri = FileProvider.getUriForFile(getContext(), "com.ophid.fleetassessment.fileprovider", createTempFile);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", imageUri);
                    VehicleInspectionFragment.this.startActivityForResult(intent, 123);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        lb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    File createTempFile = File.createTempFile("photo", ".jpg", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    String unused = VehicleInspectionFragment.this.currentPhotoPath = createTempFile.getAbsolutePath();
                    //HomeFragment tollgate = HomeFragment.this;
                    imageUri = FileProvider.getUriForFile(getContext(), "com.ophid.fleetassessment.fileprovider", createTempFile);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", imageUri);
                    VehicleInspectionFragment.this.startActivityForResult(intent, 456);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        rb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    File createTempFile = File.createTempFile("photo", ".jpg", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    String unused = VehicleInspectionFragment.this.currentPhotoPath = createTempFile.getAbsolutePath();
                    //HomeFragment tollgate = HomeFragment.this;
                    imageUri = FileProvider.getUriForFile(getContext(), "com.ophid.fleetassessment.fileprovider", createTempFile);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", imageUri);
                    VehicleInspectionFragment.this.startActivityForResult(intent, 789);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        bb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    File createTempFile = File.createTempFile("photo", ".jpg", getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    String unused = VehicleInspectionFragment.this.currentPhotoPath = createTempFile.getAbsolutePath();
                    //HomeFragment tollgate = HomeFragment.this;
                    imageUri = FileProvider.getUriForFile(getContext(), "com.ophid.fleetassessment.fileprovider", createTempFile);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", imageUri);
                    VehicleInspectionFragment.this.startActivityForResult(intent, 012);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        return root;
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);




      //  if (i == 123) {
           // final ImageView imageView = new ImageView(getActivity());
           // imageView.setLayoutParams(new LinearLayout.LayoutParams(350, 350));
            //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llayout);
          //  this.dynamicLayout = linearLayout;
           // linearLayout.addView(imageView);
            Bitmap decodeFile = BitmapFactory.decodeFile(this.currentPhotoPath);
            this.photo = decodeFile;
            // photoArray.add(photo);


            Bitmap bitmap = null;
            try {
                bitmap = rotateImageb(decodeFile, this.currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap resizeBitmap = resizeBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4);


            ArrayList<Object> tollgateset = new ArrayList<Object>();
            ArrayList<Object> tollgatesetStringEncoded = new ArrayList<Object>();


        Double amounts;
            String dates;
            String vnumz,enumz,tollgateLocation,transactionNumber;


            UUID token = UUID.randomUUID();
            transactionNumber=token.toString();


            tollgateset.add(resizeBitmap);
            tollgateset.add(transactionNumber);

           // tollgatesetStringEncoded.add(encodedImage);
          //  tollgatesetStringEncoded.add(transactionNumber);

        if(i==123) {
            imv = fv;
            imagez[0] = resizeBitmap;
            imgValue1=resizeBitmap;
            imv.setTag("0");
        }
        else if (i==456) {
            imv = lv;
            imagez[1] = resizeBitmap;
            imgValue2=resizeBitmap;
            imv.setTag("1");
        }
        else if(i==789) {
            imv = rv;
            imagez[2] = resizeBitmap;
            imgValue3=resizeBitmap;
            imv.setTag("2");
        }
        else if(i==012) {
            imv = bv;
            imagez[3] = resizeBitmap;
            imgValue4=resizeBitmap;
            imv.setTag("3");
        }

            //photoArray.add(resizeBitmap);
            tollgateData.add(tollgateset);
            //tollgateDataStringEncoded.add(tollgatesetStringEncoded);

            imv.setImageBitmap(resizeBitmap);
            imv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    VehicleInspectionFragment tollgate = VehicleInspectionFragment.this;
                    tollgate.editDeletePopupMessage(imv, Integer.parseInt((String)imv.getTag()));
                     //Toast x = Toast.makeText(getActivity(),imv.getTag()+"",Toast.LENGTH_LONG);
                    // x.show();
                }
            });
       // }
    }

    public void editDeletePopupMessage(ImageView imageView, int xx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select action to perform");
        builder.setItems(new CharSequence[]{"View image", "Cancel"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {

                   // ArrayList x =(ArrayList<Object>)tollgateData.get(xx);
                    //bm = (Bitmap) x.get(0);
                    bm=(Bitmap) imagez[xx];

                    // bm = (Bitmap) photoArray.get(i);

                    startActivity(new Intent(getActivity(), InspectionGetEnlargedImage.class));
                } else if (i == 1) {
                    dialogInterface.cancel();
                }
            }
        });
        builder.create().show();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static Bitmap rotateImageIfRequired(Bitmap bitmap, Uri uri) throws IOException {
        int attributeInt = new ExifInterface(uri.getPath()).getAttributeInt("Orientation", 1);
        if (attributeInt == 3) {
            return rotateImage(bitmap, 180);
        }
        if (attributeInt == 6) {
            return rotateImage(bitmap, 90);
        }
        if (attributeInt != 8) {
            return bitmap;
        }
        return rotateImage(bitmap, TIFFConstants.TIFFTAG_IMAGEDESCRIPTION);
    }

    private static Bitmap rotateImage(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return createBitmap;
    }

    public static Bitmap rotateImageb(Bitmap bitmap, String str) throws IOException {
        int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 1);
        int i = attributeInt != 3 ? attributeInt != 6 ? attributeInt != 8 ? 0 : TIFFConstants.TIFFTAG_IMAGEDESCRIPTION : 90 : 180;
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) i) / ((float) width), ((float) i2) / ((float) height));
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return createBitmap;
    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
    }

    /*public void inputDialogz(TextView x) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView title = new TextView(getActivity());
        title.setText("Enter comment");
        title.setTextColor(getResources().getColor(R.color.black));
        title.setTextSize(18);
        title.setPadding(15,0,0,0);
        builder.setCustomTitle(title);

        //builder.setMessage("Message");
        //builder.setTitle("Enter comment");


// Set up the input
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                x.setTextColor(Color.WHITE);
                x.setBackgroundColor(Color.RED);
                x.setBackground(getResources().getDrawable(R.drawable.selector_revised_comment));
                x.setSelected(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }*/


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
                    case 1:
                        t1Value=m_Text;
                        break;
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
                    case 9:
                        t9Value=m_Text;
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
                    case 19:
                        t19Value=m_Text;
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
                    case 25:
                        t25Value=m_Text;
                        break;
                    case 26:
                        t26Value=m_Text;
                        break;
                }



                x.setTextColor(Color.WHITE);
                x.setBackgroundColor(Color.RED);
                x.setBackground(getResources().getDrawable(R.drawable.selector_revised_comment));
                x.setSelected(true);
                Globals.vehicleInspectionProgressCount += 1;

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
    public void populateVehicleInspectionVariables()
    {
        dbHelper = new MySQLiteHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist where VehicleNumber= '"+Globals.vehicleRegNumber+"' AND SyncStatus= '" + Globals.vehicleRegNumber +"'",null);
        Cursor cursor = db.rawQuery("SELECT VehicleNumber,ActivityDate,SyncStatus,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23,t24,t25,t26,CurrentMileage,Fuel,DateTyreLastChanged,MileageAtLastTyreChange,DateBatteryLastChanged,FrontImage,LeftImage,RightImage,BackImage from VehicleInspection where VehicleNumber= '"+Globals.vehicleRegNumber+"' AND SyncStatus= 'Not Synced'",null);

        // Cursor cursor = db.rawQuery("SELECT * from VehicleChecklist","VehicleNumber= '"+Globals.vehicleRegNumber+"'",null);


        while(cursor.moveToNext()) {
            //   cursor.getString(cursor.getColumnIndex("VehicleNumber")),
            //      cursor.getString(cursor.getColumnIndex("ActivityDate")),
            Globals.vehicleRegNumber=cursor.getString(cursor.getColumnIndex("VehicleNumber"));
            recordDate=cursor.getString(cursor.getColumnIndex("ActivityDate"));
            syncStatus=cursor.getString(cursor.getColumnIndex("SyncStatus"));

            setButtonStateOnDatabaseRetrieval(t1,cursor.getString(cursor.getColumnIndex("t1")));
            t1Value=cursor.getString(cursor.getColumnIndex("t1"));

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

            setButtonStateOnDatabaseRetrieval(t9,cursor.getString(cursor.getColumnIndex("t9")));
            t9Value=cursor.getString(cursor.getColumnIndex("t9"));

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

            setButtonStateOnDatabaseRetrieval(t19,cursor.getString(cursor.getColumnIndex("t19")));
            t19Value=cursor.getString(cursor.getColumnIndex("t19"));

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

            setButtonStateOnDatabaseRetrieval(t25,cursor.getString(cursor.getColumnIndex("t25")));
            t25Value=cursor.getString(cursor.getColumnIndex("t25"));

            setButtonStateOnDatabaseRetrieval(t26,cursor.getString(cursor.getColumnIndex("t26")));
            t26Value=cursor.getString(cursor.getColumnIndex("t26"));

            currentMileageValue=cursor.getString(cursor.getColumnIndex("CurrentMileage"));;
            currMileageEditText.setText(currentMileageValue);

            lastTyreChangeDateValue=cursor.getString(cursor.getColumnIndex("DateTyreLastChanged"));;
            lastTyreChangeDateEditText.setText(lastTyreChangeDateValue);

            lastBatteryChangeDateValue=cursor.getString(cursor.getColumnIndex("DateBatteryLastChanged"));;
            lastBatteryChangeDateEditText.setText(lastBatteryChangeDateValue);

            mileageAtLastTyreChangeValue=cursor.getString(cursor.getColumnIndex("MileageAtLastTyreChange"));;
            mileageAtLastTyreChangeEditText.setText(mileageAtLastTyreChangeValue);

            radioButtonValue=cursor.getString(cursor.getColumnIndex("Fuel"));

            if(radioButtonValue.equals("quarter"))
            {
                radio_quarter.setChecked(true);
            }else
            if(radioButtonValue.equals("half"))
            {
                radio_half.setChecked(true);
            }
            else
            if(radioButtonValue.equals("three quarters"))
            {
                radio_three_quarters.setChecked(true);
            }else
            if(radioButtonValue.equals("full"))
            {
                radio_full.setChecked(true);
            }
            byte[] bitmapdata;

            // code below converts byte array in database to bitmap for display in image view
            bitmapdata = cursor.getBlob(cursor.getColumnIndex("FrontImage"));
            imgValue1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            bitmapdata = cursor.getBlob(cursor.getColumnIndex("LeftImage"));
            imgValue2 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            bitmapdata = cursor.getBlob(cursor.getColumnIndex("RightImage"));
            imgValue3 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            bitmapdata = cursor.getBlob(cursor.getColumnIndex("BackImage"));
            imgValue4 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            //imgValue1=cursor.getBlob(cursor.getColumnIndex("FrontImage"));
            //imgValue2=cursor.getBlob(cursor.getColumnIndex("LeftImage"));
           // imgValue3=cursor.getBlob(cursor.getColumnIndex("RightImage"));
            //imgValue4=cursor.getBlob(cursor.getColumnIndex("BackImage"));

            fv.setImageBitmap(imgValue1);
            lv.setImageBitmap(imgValue2);
            rv.setImageBitmap(imgValue3);
            bv.setImageBitmap(imgValue4);

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


    public void saveVehicleInspection(String id, String employee_number, String vehicle_number, String t1,String t2,String t3,String t4,String t5,String t6,String t7,String t8,String t9,String t10,String t11,String t12,String t13,String t14,String t15,String t16,String t17,String t18,String t19,String t20,String t21,String t22,String t23,String t24,String t25,String t26,String currentMileageVal, String radioButtonVal,String dateTyreLastChangedVal,String mileageAtLastTyreChangeVal,String dateBatteryLastChangedVal, byte[] fronts, byte[] lefts, byte[] rights, byte[] backs) {

        //Date date_stamp = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        //java.sql.Date date_stamp_sqlDate = new java.sql.Date(date_stamp.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("EmployeeNumber", employee_number);
        contentValues.put("VehicleNumber", vehicle_number);
        contentValues.put("ActivityDate", Globals.todaysDate);
        contentValues.put("t1", t1);
        contentValues.put("t2", t2);
        contentValues.put("t3", t3);
        contentValues.put("t4", t4);
        contentValues.put("t5", t5);
        contentValues.put("t6", t6);
        contentValues.put("t7", t7);
        contentValues.put("t8", t8);
        contentValues.put("t9", t9);
        contentValues.put("t10", t10);
        contentValues.put("t11", t11);
        contentValues.put("t12", t12);
        contentValues.put("t13", t13);
        contentValues.put("t14", t14);
        contentValues.put("t15", t15);
        contentValues.put("t16", t16);
        contentValues.put("t17", t17);
        contentValues.put("t18", t18);
        contentValues.put("t19", t19);
        contentValues.put("t20", t20);
        contentValues.put("t21", t21);
        contentValues.put("t22", t22);
        contentValues.put("t23", t23);
        contentValues.put("t24", t24);
        contentValues.put("t25", t25);
        contentValues.put("t26", t26);
        contentValues.put("CurrentMileage",currentMileageVal);
        contentValues.put("Fuel",radioButtonVal);
        contentValues.put("DateTyreLastChanged",dateTyreLastChangedVal);
        contentValues.put("MileageAtLastTyreChange",mileageAtLastTyreChangeVal);
        contentValues.put("DateBatteryLastChanged",dateBatteryLastChangedVal);
        contentValues.put("FrontImage",fronts);
        contentValues.put("LeftImage",lefts);
        contentValues.put("RightImage",rights);
        contentValues.put("BackImage",backs);


        // contentValues.put("SyncStatus", "PENDING");

        db.insert("VehicleInspection",null,contentValues);
        recordSavedPopupMessage();

    }


    public void saveVehicleInspectionStringEncodedImages(String id, String employee_number, String vehicle_number, String t1,String t2,String t3,String t4,String t5,String t6,String t7,String t8,String t9,String t10,String t11,String t12,String t13,String t14,String t15,String t16,String t17,String t18,String t19,String t20,String t21,String t22,String t23,String t24,String t25,String t26,String currentMileageVal, String radioButtonVal,String dateTyreLastChangedVal,String mileageAtLastTyreChangeVal,String dateBatteryLastChangedVal, String fronts, String lefts, String rights, String backs) {

        //Date date_stamp = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        //java.sql.Date date_stamp_sqlDate = new java.sql.Date(date_stamp.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("EmployeeNumber", employee_number);
        contentValues.put("VehicleNumber", vehicle_number);
        contentValues.put("ActivityDate", Globals.todaysDate);
        contentValues.put("t1", t1);
        contentValues.put("t2", t2);
        contentValues.put("t3", t3);
        contentValues.put("t4", t4);
        contentValues.put("t5", t5);
        contentValues.put("t6", t6);
        contentValues.put("t7", t7);
        contentValues.put("t8", t8);
        contentValues.put("t9", t9);
        contentValues.put("t10", t10);
        contentValues.put("t11", t11);
        contentValues.put("t12", t12);
        contentValues.put("t13", t13);
        contentValues.put("t14", t14);
        contentValues.put("t15", t15);
        contentValues.put("t16", t16);
        contentValues.put("t17", t17);
        contentValues.put("t18", t18);
        contentValues.put("t19", t19);
        contentValues.put("t20", t20);
        contentValues.put("t21", t21);
        contentValues.put("t22", t22);
        contentValues.put("t23", t23);
        contentValues.put("t24", t24);
        contentValues.put("t25", t25);
        contentValues.put("t26", t26);
        contentValues.put("CurrentMileage",currentMileageVal);
        contentValues.put("Fuel",radioButtonVal);
        contentValues.put("DateTyreLastChanged",dateTyreLastChangedVal);
        contentValues.put("MileageAtLastTyreChange",mileageAtLastTyreChangeVal);
        contentValues.put("DateBatteryLastChanged",dateBatteryLastChangedVal);
        contentValues.put("FrontImage",fronts);
        contentValues.put("LeftImage",lefts);
        contentValues.put("RightImage",rights);
        contentValues.put("BackImage",backs);


        // contentValues.put("SyncStatus", "PENDING");

        db.insert("VehicleInspectionStringEncodedImages",null,contentValues);
        recordSavedPopupMessage();

    }
        public void updateVehicleInspection(String id, String employee_number,String vehicle_number, String t1,String t2,String t3,String t4,String t5,String t6,String t7,String t8,String t9,String t10,String t11,String t12,String t13,String t14,String t15,String t16,String t17,String t18,String t19,String t20,String t21,String t22,String t23,String t24,String t25,String t26, String currentMileageVal, String radioButtonVal,String dateTyreLastChangedVal,String mileageAtLastTyreChangeVal,String dateBatteryLastChangedVal, byte[] fronts, byte[] lefts, byte[] rights, byte[] backs) {

        //Date date_stamp = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        //java.sql.Date date_stamp_sqlDate = new java.sql.Date(date_stamp.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("EmployeeNumber", employee_number);
        contentValues.put("VehicleNumber", vehicle_number);
        contentValues.put("ActivityDate", Globals.todaysDate);
        contentValues.put("t1", t1);
        contentValues.put("t2", t2);
        contentValues.put("t3", t3);
        contentValues.put("t4", t4);
        contentValues.put("t5", t5);
        contentValues.put("t6", t6);
        contentValues.put("t7", t7);
        contentValues.put("t8", t8);
        contentValues.put("t9", t9);
        contentValues.put("t10", t10);
        contentValues.put("t11", t11);
        contentValues.put("t12", t12);
        contentValues.put("t13", t13);
        contentValues.put("t14", t14);
        contentValues.put("t15", t15);
        contentValues.put("t16", t16);
        contentValues.put("t17", t17);
        contentValues.put("t18", t18);
        contentValues.put("t19", t19);
        contentValues.put("t20", t20);
        contentValues.put("t21", t21);
        contentValues.put("t22", t22);
        contentValues.put("t23", t23);
        contentValues.put("t24", t24);
        contentValues.put("t25", t25);
        contentValues.put("t26", t26);
        contentValues.put("CurrentMileage",currentMileageVal);
        contentValues.put("Fuel",radioButtonVal);
        contentValues.put("DateTyreLastChanged",dateTyreLastChangedVal);
        contentValues.put("MileageAtLastTyreChange",mileageAtLastTyreChangeVal);
        contentValues.put("DateBatteryLastChanged",dateBatteryLastChangedVal);
        contentValues.put("FrontImage",fronts);
        contentValues.put("LeftImage",lefts);
        contentValues.put("RightImage",rights);
        contentValues.put("BackImage",backs);

        db.update("VehicleInspection", contentValues, "VehicleNumber='" + vehicle_number + "'", null);

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

    public void imagesNotCaptured() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.my_dialog);
        builder.setMessage("Please capture ALL vehicle images before saving");
        builder.setTitle("Images not captured");
        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass6 */

            public void onClick(DialogInterface dialogInterface, int i) {
                //Reg.this.pbar.setVisibility(View.INVISIBLE);

            }
        });
        builder.create().show();
    }
}
