package com.ophid.fleetassessment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.UUID;

public class VehicleInspectionFragment extends Fragment {
    Boolean longPress = false;
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase db;
    String selectedChecklist="No form selected";
    private String m_Text = "";
    private  String recordDate;
    private String syncStatus="Not Synced";
    String currentLongPressValue="0";

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
    public ArrayList<Bitmap> photoArray;
    private String m_Text = "";
    Bitmap[] imagez = new Bitmap[4];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_vehicle_inspection,container,false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_vehicle_inspection, null);
        tollgateData=new ArrayList<Object>();
        photoArray = new ArrayList<>();

        final ToggleButton t1 = (ToggleButton) root.findViewById(R.id.t1);
        final ToggleButton t2 = (ToggleButton) root.findViewById(R.id.t2);
        final ToggleButton t3 = (ToggleButton) root.findViewById(R.id.t3);
        final ToggleButton t4 = (ToggleButton) root.findViewById(R.id.t4);
        final ToggleButton t5 = (ToggleButton) root.findViewById(R.id.t5);
        final ToggleButton t6 = (ToggleButton) root.findViewById(R.id.t6);
        final ToggleButton t7 = (ToggleButton) root.findViewById(R.id.t7);
        final ToggleButton t8 = (ToggleButton) root.findViewById(R.id.t8);
        final ToggleButton t9 = (ToggleButton) root.findViewById(R.id.t9);
        final ToggleButton t10 = (ToggleButton) root.findViewById(R.id.t10);
        final ToggleButton t11 = (ToggleButton) root.findViewById(R.id.t11);
        final ToggleButton t12 = (ToggleButton) root.findViewById(R.id.t12);
        final ToggleButton t13 = (ToggleButton) root.findViewById(R.id.t13);
        final ToggleButton t14 = (ToggleButton) root.findViewById(R.id.t14);
        final ToggleButton t15 = (ToggleButton) root.findViewById(R.id.t15);
        final ToggleButton t16 = (ToggleButton) root.findViewById(R.id.t16);
        final ToggleButton t17 = (ToggleButton) root.findViewById(R.id.t17);
        final ToggleButton t18 = (ToggleButton) root.findViewById(R.id.t18);
        final ToggleButton t19 = (ToggleButton) root.findViewById(R.id.t19);
        final ToggleButton t20 = (ToggleButton) root.findViewById(R.id.t20);
        final ToggleButton t21 = (ToggleButton) root.findViewById(R.id.t21);
        final ToggleButton t22 = (ToggleButton) root.findViewById(R.id.t22);
        final ToggleButton t23 = (ToggleButton) root.findViewById(R.id.t23);
        final ToggleButton t24 = (ToggleButton) root.findViewById(R.id.t24);
        final ToggleButton t25 = (ToggleButton) root.findViewById(R.id.t25);
        final ToggleButton t26 = (ToggleButton) root.findViewById(R.id.t26);

        fb=(ImageButton) root.findViewById(R.id.front_button);
        lb=(ImageButton) root.findViewById(R.id.left_button);
        rb=(ImageButton) root.findViewById(R.id.right_button);
        bb=(ImageButton) root.findViewById(R.id.back_button);

        fv=(ImageView) root.findViewById(R.id.front_pic);
        lv=(ImageView) root.findViewById(R.id.left_pic);
        rv=(ImageView) root.findViewById(R.id.right_pic);
        bv=(ImageView) root.findViewById(R.id.back_pic);


        t1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t1);
                return true;
            }
        });
        t2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t2);
                return true;
            }
        });
        t3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t3);
                return true;
            }
        });
        t4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t4);
                return true;
            }
        });
        t5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t5);
                return true;
            }
        });
        t6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t6);
                return true;
            }
        });
        t7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t7);
                return true;
            }
        });
        t8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t8);
                return true;
            }
        });
        t9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t9);
                return true;
            }
        });
        t10.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t10);
                return true;
            }
        });
        t11.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t11);
                return true;
            }
        });
        t12.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t12);
                return true;
            }
        });
        t13.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t13);
                return true;
            }
        });
        t14.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t14);
                return true;
            }
        });
        t15.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t15);
                return true;
            }
        });
        t16.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t16);
                return true;
            }
        });
        t17.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t17);
                return true;
            }
        });
        t18.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t18);
                return true;
            }
        });
        t19.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t19);
                return true;
            }
        });
        t20.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t20);
                return true;
            }
        });
        t21.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t21);
                return true;
            }
        });
        t22.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t22);
                return true;
            }
        });
        t23.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t23);
                return true;
            }
        });
        t24.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t24);
                return true;
            }
        });
        t25.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t25);
                return true;
            }
        });
        t26.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputDialogz(t26);
                return true;
            }
        });
        //imageViewArray = new ArrayList<>();
       // btnCaptureImage= (Button)root.findViewById(R.id.imageCaptureButton);
        //linearLayout = (LinearLayout)root.findViewById(R.id.llayout);
        /*btnCaptureImage.setOnClickListener(new View.OnClickListener() {
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
        });*/

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
            Double amounts;
            String dates;
            String vnumz,enumz,tollgateLocation,transactionNumber;


            UUID token = UUID.randomUUID();
            transactionNumber=token.toString();


            tollgateset.add(resizeBitmap);
            tollgateset.add(transactionNumber);

        if(i==123) {
            imv = fv;
            imagez[0] = resizeBitmap;
            imv.setTag("0");
        }
        else if (i==456) {
            imv = lv;
            imagez[1] = resizeBitmap;
            imv.setTag("1");
        }
        else if(i==789) {
            imv = rv;
            imagez[2] = resizeBitmap;
            imv.setTag("2");
        }
        else if(i==012) {
            imv = bv;
            imagez[3] = resizeBitmap;
            imv.setTag("3");
        }

            //photoArray.add(resizeBitmap);
            tollgateData.add(tollgateset);

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

    public void inputDialogz(TextView x) {
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
    }
}
