package com.ophid.fleetassessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return inflater.inflate(R.layout.fragment_home,container,false);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        final TextView t1 = (TextView) root.findViewById(R.id.pbartextview);
        return root;
    }
}
