package com.ophid.fleetassessment;

import static android.content.ContentValues.TAG;
//import com.shockwave.pdfium.PdfDocument;
//import com.shockwave.pdfium.*;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.Fragment;
//import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;


import java.util.List;

public class ApprovalsFragment extends Fragment implements OnPageChangeListener,OnLoadCompleteListener{
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
    private ArrayList<Object> columnArrayList=null;
    private ArrayList<Object> rowArrayList=null;

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
    public static final String SAMPLE_FILE = "GFG.pdf";
    PDFView pdfView;
    //com.github.barteksc.pdfviewer.PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    Button btnGeneratePdf;
    int pageHeight = 1120;
    int pagewidth = 792;

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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_approvals, null);
        pdfView= (PDFView)root.findViewById(R.id.pdfView);
        btnGeneratePdf=(Button)root.findViewById(R.id.idBtnGeneratePDF);
        searchView = (SearchView) root.findViewById(R.id.searchView);




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String str) {
                return true;
            }
            public boolean onQueryTextSubmit(String str) {
                if (!haveNetworkConnection()) {
                    noConnectionPopupMessage();
                    return true;
                }

                return true;
            }
        });

        btnGeneratePdf.setOnClickListener(new View.OnClickListener() {
            /* class com.ophid.coasheet.Reg.AnonymousClass2 */

            public void onClick(View view) {

                generatePDF();
                displayFromAsset(SAMPLE_FILE);
            }
        });

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



    public  Cursor getVehicleChecklistsPendingPosting() {
       // SQLiteDatabase Db = this.getWritableDatabase();
        String query = "select * from  VehicleChecklist where SyncStatus='Not Synced'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
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

   private void displayFromAsset(String assetFileName) {
       //pdfFileName = assetFileName;

       File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
       File file = new File(folder, assetFileName);

      // pdfFileName=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+assetFileName;
       //pdfView.fromAsset(SAMPLE_FILE)

            //pdfView.fromAsset(pdfFileName)
       pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(getActivity()))
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        //setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
       // PdfDocument.Meta meta = pdfView.getDocumentMeta();
       // printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

   /* public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }*/


    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();
        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(getActivity(), R.color.purple_200));
       // canvas.drawText("A portal for IT professionals.", 209, 10, title);
        canvas.drawText("Vehicle Inspection Report", 10, 10, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(getActivity(), R.color.purple_200));
        title.setTextSize(25);
        //title.setTextAlign(Paint.Align.CENTER);
        title.setTextAlign(Paint.Align.LEFT);

        //ArrayList<Object> retrievedList = (ArrayList<Object>)populateArrayListForPdf();
       // ArrayList<Object> retrievedListb=(ArrayList<Object>) retrievedList.get(0);

        canvas.drawText("Ranga main, This is sample document which we have created.\n", 10, 30, title);
        canvas.drawText("Ranga, This is sample document which we have created.", 10, 50, title);
        canvas.drawText("Ranga z 2, This is sample document which we have created.", 10, 70, title);

        /* canvas.drawText((String)retrievedListb.get(0), 396, 560, title);
        canvas.drawText((String)retrievedListb.get(1), 396, 560, title);
        canvas.drawText((String)retrievedListb.get(2), 396, 560, title);*/

        pdfDocument.finishPage(myPage);
        //File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(folder, "GFG.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(), "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    public ArrayList<Object> populateArrayListForPdf() {

            try {
                Connection connectionclass = new ConnectionHelper().connectionclass();
                connect = connectionclass;
                if (connectionclass != null) {

                    // ResultSet executeQuery = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select * from ophid_vehicles WHERE license_number ='" + Globals.vehicleRegNumber + "'");
                   // ResultSet executeQuery = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select * from VehicleInspections WHERE vehicle_number ='" + Globals.vehicleRegNumber + "'");
                    ResultSet executeQuery = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select * from VehicleInspections");
                   // ResultSet executeQuery = connect.createStatement(PointerIconCompat.TYPE_WAIT, PointerIconCompat.TYPE_CROSSHAIR).executeQuery("Select id,EmployeeNumber,VehicleNumber from VehicleInspections");

                    ResultSetMetaData rsmd = executeQuery.getMetaData();
                    int column_count = rsmd.getColumnCount();
                    Toast.makeText(getActivity(), "connected", Toast.LENGTH_SHORT).show();

                    if (!executeQuery.next()) {

                        searchNotFoundPopupMessage();
                          //return;
                    } else
                    {
                        //executeQuery.beforeFirst();
                        Toast.makeText(getActivity(), "connected", Toast.LENGTH_SHORT).show();


                        executeQuery.beforeFirst();

                        while (executeQuery.next()) {
                                columnArrayList.add(executeQuery.getString("id"));
                                columnArrayList.add(executeQuery.getString("EmployeeNumber"));
                                columnArrayList.add(executeQuery.getString("VehicleNumber"));
                                columnArrayList.add(executeQuery.getDate("ActivityDate"));
                                columnArrayList.add(executeQuery.getString("r1"));
                                columnArrayList.add(executeQuery.getString("r2"));
                                columnArrayList.add(executeQuery.getString("r3"));
                                columnArrayList.add(executeQuery.getString("r4"));
                                columnArrayList.add(executeQuery.getString("r5"));
                                columnArrayList.add(executeQuery.getString("r6"));
                                columnArrayList.add(executeQuery.getString("r7"));
                                columnArrayList.add(executeQuery.getString("r8"));
                                columnArrayList.add(executeQuery.getString("r9"));
                                columnArrayList.add(executeQuery.getString("r10"));
                                columnArrayList.add(executeQuery.getString("r11"));
                                columnArrayList.add(executeQuery.getString("r12"));
                                columnArrayList.add(executeQuery.getString("r13"));
                                columnArrayList.add(executeQuery.getString("r14"));
                                columnArrayList.add(executeQuery.getString("r15"));
                                columnArrayList.add(executeQuery.getString("r16"));
                                columnArrayList.add(executeQuery.getString("r17"));
                                columnArrayList.add(executeQuery.getString("r18"));
                                columnArrayList.add(executeQuery.getString("r19"));
                                columnArrayList.add(executeQuery.getString("r20"));
                                columnArrayList.add(executeQuery.getString("r21"));
                                columnArrayList.add(executeQuery.getString("r22"));
                                columnArrayList.add(executeQuery.getString("r23"));
                                columnArrayList.add(executeQuery.getString("r24"));
                                columnArrayList.add(executeQuery.getString("r25"));
                                columnArrayList.add(executeQuery.getString("r26"));
                                columnArrayList.add(executeQuery.getString("CurrentMileage"));
                                columnArrayList.add(executeQuery.getString("Fuel"));
                                columnArrayList.add(executeQuery.getDate("DateTyreLastChanged"));
                                columnArrayList.add(executeQuery.getString("MileageAtLastTyreChange"));
                                columnArrayList.add(executeQuery.getDate("DateBatteryLastChanged"));
                                columnArrayList.add(executeQuery.getBlob("FrontImage"));
                                columnArrayList.add(executeQuery.getBlob("LeftImage"));
                                columnArrayList.add(executeQuery.getBlob("RightImage"));
                                columnArrayList.add(executeQuery.getBlob("BackImage"));
                                rowArrayList.add(columnArrayList);
                           // regnum.setText(executeQuery.getString(1));
                        }


                    }

                }

            }
            catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        return rowArrayList;
    }

}
