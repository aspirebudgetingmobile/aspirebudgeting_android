package com.aspirebudgetingmobile.aspirebudgeting.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.models.AccountBalanceModel;

import java.util.List;

public class AccountBalanceAdapter extends RecyclerView.Adapter<AccountBalanceAdapter.ViewHolder> {

    Context context;
    List<AccountBalanceModel> list;

    public AccountBalanceAdapter(Context context, List<AccountBalanceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_balance_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccountBalanceModel model = list.get(position);

        holder.name.setText(model.getAccountName());
        if (model.getAmount().contains("-")){
            holder.amount.setText(model.getAmount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.negativeAmountRed));
        } else {
            holder.amount.setText(model.getAmount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.positiveAmountGreen));
        }
        if (model.getLastUpdatedOn() == null){
            holder.lastUpdatedOn.setVisibility(View.GONE);
        } else{
            holder.lastUpdatedOn.setText(model.getLastUpdatedOn());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount, lastUpdatedOn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.accountNameText_accountBalance);
            amount = itemView.findViewById(R.id.amountTextView_accountBalance);
            lastUpdatedOn = itemView.findViewById(R.id.lastUpdatedOnText_accountBalance);
        }
    }
}
