package com.aspirebudgetingmobile.aspirebudgeting.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.activities.Home;
import com.aspirebudgetingmobile.aspirebudgeting.interfaces.TransactionCallBack;
import com.aspirebudgetingmobile.aspirebudgeting.utils.BasicUtils;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SheetsManager;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Transaction extends Fragment {

    private EditText amountEditText_transactions, addMemoEditText_transactions;
    private TextView selectDateEditText_transactions, inFlowTextView_transactions,
            outFlowTextView_transactions, approvedTextView_transactions, pendingTextView_transactions;
    private AppCompatSpinner categorySpinner_transactions, accountSpinner_transactions;
    private MaterialCardView addCard_transactions;
    private LinearLayout backgroundLayout;

    private View view;
    private Context context;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private SheetsManager sheetsManager = objectFactory.getSheetsManager();
    private String selectedCategory = "", selectedAccount = "";
    private int selectedTransactionType = 2, selectedApprovalType = 2;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    private String selectedDate = "";
    private ProgressDialog progressDialog;

    public Transaction() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction, container, false);
        context = view.getContext();

        fetchIDs();
        initValues();
        initDatePicker();
        onClickListeners();

        return view;
    }

    private void fetchIDs() {
        amountEditText_transactions = view.findViewById(R.id.amountEditText_transactions);
        addMemoEditText_transactions = view.findViewById(R.id.addMemoEditText_transactions);
        selectDateEditText_transactions = view.findViewById(R.id.selectDateEditText_transactions);
        categorySpinner_transactions = view.findViewById(R.id.categorySpinner_transactions);
        accountSpinner_transactions = view.findViewById(R.id.accountSpinner_transactions);
        inFlowTextView_transactions = view.findViewById(R.id.inFlowTextView_transactions);
        outFlowTextView_transactions = view.findViewById(R.id.outFlowTextView_transactions);
        approvedTextView_transactions = view.findViewById(R.id.approvedTextView_transactions);
        pendingTextView_transactions = view.findViewById(R.id.pendingTextView_transactions);
        addCard_transactions = view.findViewById(R.id.addCard_transactions);
        backgroundLayout = view.findViewById(R.id.backgroundLayout);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
    }

    public void initValues() {
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Select Category");
        categoryList.addAll(sheetsManager.getTransactionCategories());
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner_transactions.setAdapter(categorySpinnerAdapter);

        List<String> accountList = new ArrayList<>();
        accountList.add("Select Account");
        accountList.addAll(sheetsManager.getTransactionAccounts());
        ArrayAdapter<String> accountSpinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, accountList);
        accountSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner_transactions.setAdapter(accountSpinnerAdapter);

        categorySpinner_transactions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        accountSpinner_transactions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAccount = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void onClickListeners() {

        addCard_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedApprovalType == 2 || selectedTransactionType == 2 || selectedDate.isEmpty() ||
                        amountEditText_transactions.getText() == null || amountEditText_transactions.getText().toString().isEmpty() ||
                        addMemoEditText_transactions.getText() == null || addMemoEditText_transactions.getText().toString().isEmpty() ||
                        selectedCategory.equalsIgnoreCase("Select Category") || selectedAccount.equalsIgnoreCase("Select Account")) {
                    Toast.makeText(context, "Kindly fill all information", Toast.LENGTH_SHORT).show();
                } else {
                    BasicUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
                    progressDialog.show();
                    startAppendingTransaction(amountEditText_transactions.getText().toString(), addMemoEditText_transactions.getText().toString());
                }
            }
        });

        selectDateEditText_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
            }
        });

        inFlowTextView_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inFlowTextView_transactions.setBackgroundColor(getResources().getColor(R.color.inflow_green));
                outFlowTextView_transactions.setBackgroundColor(getResources().getColor(R.color.aspireDarkPrimaryColor));
                selectedTransactionType = 0;
            }
        });

        outFlowTextView_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inFlowTextView_transactions.setBackgroundColor(getResources().getColor(R.color.aspireDarkPrimaryColor));
                outFlowTextView_transactions.setBackgroundColor(getResources().getColor(R.color.outflow_red));
                selectedTransactionType = 1;
            }
        });

        approvedTextView_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvedTextView_transactions.setBackgroundColor(getResources().getColor(R.color.inflow_green));
                pendingTextView_transactions.setBackgroundColor(getResources().getColor(R.color.aspireDarkPrimaryColor));
                selectedApprovalType = 0;
            }
        });

        pendingTextView_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvedTextView_transactions.setBackgroundColor(getResources().getColor(R.color.aspireDarkPrimaryColor));
                pendingTextView_transactions.setBackgroundColor(getResources().getColor(R.color.outflow_red));
                selectedApprovalType = 1;
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void startAppendingTransaction(final String amount, final String memo) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                sheetsManager.addTransaction(amount, memo, selectedDate,
                        selectedCategory, selectedAccount, selectedTransactionType, selectedApprovalType
                        , new TransactionCallBack() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Transactions Uploaded !", Toast.LENGTH_SHORT).show();
                                ((Home) context).reloadCards();
                            }

                            @Override
                            public void onError() {
                                progressDialog.dismiss();
                            }
                        });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    private void initDatePicker() {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        selectedDate = sdf.format(myCalendar.getTime());
        selectDateEditText_transactions.setText(selectedDate);
    }
}
