package com.ophid.fleetassessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MotorcycleChecklistFragment extends Fragment {

    Boolean longPress = false;

    String selectedChecklist="No form selected";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_vehicle_checklist,container,false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_motorcycle_checklist, null);

        final ToggleButton t2 = (ToggleButton) root.findViewById(R.id.t2);
        final ToggleButton t3 = (ToggleButton) root.findViewById(R.id.t3);
        final ToggleButton t4 = (ToggleButton) root.findViewById(R.id.t4);
        final ToggleButton t5 = (ToggleButton) root.findViewById(R.id.t5);
        final ToggleButton t6 = (ToggleButton) root.findViewById(R.id.t6);
        final ToggleButton t7 = (ToggleButton) root.findViewById(R.id.t7);
        final ToggleButton t8 = (ToggleButton) root.findViewById(R.id.t8);

        final ToggleButton t10 = (ToggleButton) root.findViewById(R.id.t10);
        final ToggleButton t11 = (ToggleButton) root.findViewById(R.id.t11);
        final ToggleButton t12 = (ToggleButton) root.findViewById(R.id.t12);
        final ToggleButton t13 = (ToggleButton) root.findViewById(R.id.t13);
        final ToggleButton t14 = (ToggleButton) root.findViewById(R.id.t14);
        final ToggleButton t15 = (ToggleButton) root.findViewById(R.id.t15);
        final ToggleButton t16 = (ToggleButton) root.findViewById(R.id.t16);
        final ToggleButton t17 = (ToggleButton) root.findViewById(R.id.t17);
        final ToggleButton t18 = (ToggleButton) root.findViewById(R.id.t18);

        final ToggleButton t20 = (ToggleButton) root.findViewById(R.id.t20);
        final ToggleButton t21 = (ToggleButton) root.findViewById(R.id.t21);
        final ToggleButton t22 = (ToggleButton) root.findViewById(R.id.t22);
        final ToggleButton t23 = (ToggleButton) root.findViewById(R.id.t23);
        final ToggleButton t24 = (ToggleButton) root.findViewById(R.id.t24);


     /*   btnStartStop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                if (longPress) {
                    longPress = false;
                } else {
                    longPress = true;
                }
                return true;
            }
        });

        btnStartStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!longPress) {
                    //Do stuff
                } else {
                    Toast.makeText(getContext(), "Button is locked!\nLong press button to unlock it", Toast.LENGTH_SHORT).show();
                }

            }
        });*/






        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t2.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t3.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t4.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t5.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t6.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t7.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t8.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });

        t10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t10.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t11.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t12.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t13.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t14.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t15.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t16.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t17.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t18.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });

        t20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t20.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t21.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t22.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t23.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });
        t24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t24.isChecked()){
                    MainActivity.motorcycle_checklist_Completed += 1;
                }else
                {
                    MainActivity.motorcycle_checklist_Completed-=1;
                }
            }
        });


        return root;
    }

}
