package com.ophid.fleetassessment;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class InspectionGetEnlargedImageApprovals extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.inspection_gate_enlarged_image);

      //  Bitmap resizeBitmap = resizeBitmap(Tollgate.bm, Tollgate.bm.getWidth() / 4, Tollgate.bm.getHeight() / 4);
        ((ImageView) findViewById(R.id.inspectiongetlargeimage)).setImageBitmap(VehicleInspectionApprovalsFragment.bm);
       // ((ImageView) findViewById(R.id.tollgatelargeimage)).setImageBitmap(resizeBitmap);

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
}
