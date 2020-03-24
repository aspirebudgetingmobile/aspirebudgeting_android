package com.aspirebudgetingmobile.aspirebudgeting.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspirebudgetingmobile.aspirebudgeting.R;

public class Transaction extends Fragment {

    View view;

    public Transaction() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_transaction, container, false);

        return view;
    }

}
