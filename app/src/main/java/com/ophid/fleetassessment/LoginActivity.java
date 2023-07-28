package com.ophid.fleetassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public Button  btnlogin, btnRegister;
    EditText etPassword, empnumber;
    MySQLiteHelper dbHelper;
    String password, empNumber;
    Spinner spinnerCategories;
    AlertDialog.Builder builder;
    ImageView exitAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
         if(Globals.userCredential!=null) {
            try {


                Intent myIntent=null;
                if(!Globals.userCredential.isSupervisor())
                {
                    Globals.isSupevizor=0;

                    myIntent = new Intent(getApplicationContext(), MainActivity.class);
                }
                else
                {
                    //navigate to the administrator*/

                    Globals.isSupevizor=1;
                    myIntent = new Intent(getApplicationContext(),MainActivity.class);
                }

                startActivity(myIntent);
            }
            catch(Exception e)
            {

            }
        }

        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        builder= new AlertDialog.Builder( this);



        btnlogin = findViewById(R.id.cirLoginButton);
        exitAppButton=findViewById(R.id.exitApp);
        empnumber = findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);

        if (dbHelper == null) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            dbHelper = new MySQLiteHelper(LoginActivity.this);
        }

        exitAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,R.style.my_dialog);
                builder.setMessage("Are you sure you want to exit the application?");
                builder.setTitle("Exit");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    /* class com.ophid.coasheet.Reg.AnonymousClass6 */

                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Reg.this.pbar.setVisibility(View.INVISIBLE);
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    /* class com.ophid.coasheet.Reg.AnonymousClass6 */

                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Reg.this.pbar.setVisibility(View.INVISIBLE);

                    }
                });
                builder.create().show();


            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(empnumber.getText().toString().length()==0)
                {
                    empnumber.setError("Provide Employee Number");
                    empnumber.requestFocus();
                    return;
                }
                if( etPassword.getText().toString().length()==0)
                {
                    etPassword.setError("Provide Password");
                    etPassword.requestFocus();
                    return;
                }

                UserCredential userCredential= dbHelper.loginLocalDb(empnumber.getText().toString(),etPassword.getText().toString());

                if(userCredential== null) {
                    if (isConnected(getApplicationContext())) {
                        //Ranga added below
                        Globals.employeeNumber=empnumber.getText().toString();
                         dbHelper.retrieveUserAccount(empnumber.getText().toString(), etPassword.getText().toString());


                        //Toast.makeText(LoginActivity.this, "first", Toast.LENGTH_SHORT).show();

                    } else {
                        Globals.showMessage(LoginActivity.this, "Getting Remote Credentials", "Make sure that you have internet connection and Try Again!");

                    }
                }
                else
                {



                    Globals.userCredential=userCredential;
                    Globals.employeeNumber=userCredential.getEmployeeNumber();
                    Globals.employeeName=userCredential.getEmployeeName();
                    Intent myIntent=null;


                    if(!userCredential.isSupervisor())
                    {
                        //sync
                        Globals.isSupevizor=0;
                        System.out.println(userCredential.toString());
                        if(userCredential.getDriverOrRider()==1) {

                            myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        }
                        else if(userCredential.getDriverOrRider()==3 && ! userCredential.isSupervisor())
                        {

                            myIntent = new Intent(getApplicationContext(),MainActivity.class);


                        }

                    }
                    else
                    {
                        //navigate to the administrator
                        Globals.isSupevizor=1;
                        myIntent = new Intent(getApplicationContext(),MainActivity.class);
                    }

                    startActivity(myIntent);
                    finish();

                }
            }


        });
    }





    public void showMessage(String title,String message)
    {
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    /*public void loadFragment(Fragment fragment, String tag)
    {
        try {
            FragmentManager fragmentManager =getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);

            ft.addToBackStack(tag);
            ft.replace(R.id.frame, fragment);
            ft.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }


}

