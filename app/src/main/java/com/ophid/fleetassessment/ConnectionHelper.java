package com.ophid.fleetassessment;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    String database;
    String instance;
    String ip;
    String pass;
    String port;
    String uname;

    public Connection connectionclass() {
        ip = "102.133.227.49";
       uname = "coaapp";
        pass = "@Andromeda2023";
        port = "14575";

       // ip="40.123.249.217";
       // uname="payer";
      //  pass="P@y3r@2020";
      //  port="16432";

        this.database = "OphidLogBook";
        this.instance = "MSSQLSERVER";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            return DriverManager.getConnection("jdbc:jtds:sqlserver://" + this.ip + ":" + this.port + ";databasename=" + this.database + ";user=" + this.uname + ";password=" + this.pass + ";");
        } catch (Exception e) {
            Log.e("Error ", e.getMessage());
            return null;
        }
    }
}
