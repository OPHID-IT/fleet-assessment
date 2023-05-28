package com.ophid.fleetassessment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.view.Gravity;

import androidx.appcompat.app.AlertDialog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Globals {

    public static boolean vehicleChecklistTableHasData;
    public static String todaysDate;
    public static String vehicleRegNumber="No Vehicle Selected";
    public static String focusedChecklist="No form selected";
    public static int questionProgressCount=0;
    public static int denominator=0;
    public static String employeeNumber="NOT SET";
    public static String employeeName="NOT SET";

    public static int vehicleInspectionProgressCount=0;
    public static int vehicleInspectionDenominator=32;
    public static boolean vehicleInspectionTableHasData;

    //THE VARIABLES BELOW ARE FOR SIMBAS CODE

    public static WifiManager wifiManager;
    public static Date topUpDateTime;

    //public static  String serverUrlBase="http://40.123.249.217:3678/ophid/api/";;
    //public static  String apiKey="simmtechnology@gmail.com";
    //public static  String apiPass="bcd#df79GBD11DG";
    public static  String apiKey="dev";
    public static  String apiPass="dev";
    public static  String serverUrlBase="http://40.123.249.217:3680/ophid/api/";;

    //SIMBAS VARIABLES END HERE



    public static boolean integerStringToBoolean(String x)
    {
        boolean returnedValue;
        if(x.equals("1"))
        {
            returnedValue=true;
            questionProgressCount+=1;
        }else
        {
            returnedValue = false;
        }
            return returnedValue;
    }

    public static String stringBooleanToStringNumber(String x)
    {
        String returnedValue="";
        if(x.equals("true"))
        {
            returnedValue="1";

        }else if(x.equals("false"))
        {
            returnedValue = "0";
        }else{
            returnedValue=x;
        }
        return returnedValue;
    }


    //TEXT BELOW IS FOR SIMBA'S LOGIN
    // connect = connectionHelper.connectionclass();
    public  static UserCredential userCredential;

    public static void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }
    public static void showError(Context context, String title, String errorEessage)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.my_dialog);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface
                        .OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        dialog.dismiss();
                    }
                });
        builder.setTitle(title);
        builder.setMessage(errorEessage);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();
    }
    public static void initializeWifi(Context context)
    {
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }
    public static void showMessage(Context context, String message)
    {

    }

    public static AlertDialog.Builder showMessageAndProvokeAction(final Context context, String title, String message)
    {
        // instance of alert dialog to build alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.my_dialog);
        builder.setTitle(title);
        builder.setMessage(message);
        return  builder;
    }
    public static void showMessage(final Context context, String title, String message)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.my_dialog);
        builder.setTitle(title);
        builder.setMessage(message);
        // set the positive button to do some actions
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();

    }
    public static  String formatDate( Date date)
    {
        //SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(date);
        return formattedDate;
    }

}
