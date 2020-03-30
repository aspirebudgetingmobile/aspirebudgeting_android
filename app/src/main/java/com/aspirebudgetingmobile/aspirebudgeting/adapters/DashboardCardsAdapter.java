package com.aspirebudgetingmobile.aspirebudgeting.adapters;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.models.DashboardCardsModel;
import com.google.android.material.card.MaterialCardView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class DashboardCardsAdapter extends RecyclerView.Adapter<DashboardCardsAdapter.ViewHolder> {

    Context context;
    List<DashboardCardsModel> list;
    DecimalFormat df = new DecimalFormat("0.00");
    String currencySymbol =  Currency.getInstance(Locale.getDefault()).getSymbol(Locale.getDefault()) + " ";


    public DashboardCardsAdapter(Context context, List<DashboardCardsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflated_dashboard_card_layout, parent, false);
        df.setRoundingMode(RoundingMode.CEILING);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        DashboardCardsModel model = list.get(position);

        setCollapsedCardValues(holder, model);

        setExpandedCardValues(holder, model);

        holder.collapsedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(holder.expandedCard, new AutoTransition());
                holder.collapsedCard.animate().alpha(0f).start();
                holder.expandedCard.animate().alpha(1f).start();
                holder.collapsedCard.setVisibility(View.GONE);
                holder.expandedCard.setVisibility(View.VISIBLE);
            }
        });

        holder.expandedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(holder.collapsedCard, new AutoTransition());
                holder.collapsedCard.animate().alpha(1f).start();
                holder.expandedCard.animate().alpha(0f).start();
                holder.collapsedCard.setVisibility(View.VISIBLE);
                holder.expandedCard.setVisibility(View.GONE);
            }
        });
    }

    private void setExpandedCardValues(ViewHolder holder, DashboardCardsModel model) {
        holder.cardNameExpandedText.setText(model.getName());

        holder.budgetedExpandedText.setText(getCalculatedValue(model.getBudgetedAmount()));
        holder.availableExpandedText.setText(getCalculatedValue(model.getAvailableAmount()));
        holder.spentExpandedText.setText(getCalculatedValue(model.getSpentAmount()));

        if(holder.categoriesDynamicLayout.getChildCount() > 0){
            holder.categoriesDynamicLayout.removeAllViews();
        }

        for(int i=0; i<model.getCategoryName().size(); i++){
            View view = LayoutInflater.from(context).inflate(R.layout.expanded_categories_data_card, null);

            TextView categoryName, budgetedAmount, availableAmount, spentAmount;

            categoryName = view.findViewById(R.id.categoryName_expandedCategory);
            budgetedAmount = view.findViewById(R.id.budgetedBalanceText_expandedCard);
            availableAmount = view.findViewById(R.id.availableBalanceText_expandedCard);
            spentAmount = view.findViewById(R.id.spentBalanceText_expandedCard);

            categoryName.setText(model.getCategoryName().get(i));
            budgetedAmount.setText(model.getBudgetedAmount().get(i).replace("$", currencySymbol));
            availableAmount.setText(model.getAvailableAmount().get(i).replace("$", currencySymbol));
            spentAmount.setText(model.getSpentAmount().get(i).replace("$", currencySymbol));

            holder.categoriesDynamicLayout.addView(view);
        }

    }

    private void setCollapsedCardValues(ViewHolder holder, DashboardCardsModel model) {
        holder.cardNameCollapsedText.setText(model.getName());

        holder.availableCollapsedText.setText(getCalculatedValue(model.getAvailableAmount()));
        holder.spentCollapsedText.setText(getCalculatedValue(model.getSpentAmount()));

        holder.categoriesAvailableCollapsed.setText(String.format("View %d categories", model.getCategoryName().size()));
    }

    private String getCalculatedValue(List<String> amountList) {
        double value = 0.00;
        for (int i = 0; i < amountList.size(); i++) {
            value += Float.parseFloat(amountList.get(i).replace("$", ""));
        }
        return ( currencySymbol + df.format(value));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView collapsedCard, expandedCard;
        TextView availableCollapsedText, spentCollapsedText, cardNameCollapsedText, categoriesAvailableCollapsed;

        TextView availableExpandedText, spentExpandedText, cardNameExpandedText, budgetedExpandedText;
        LinearLayout categoriesDynamicLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Collapsed Card IDs
            collapsedCard = itemView.findViewById(R.id.collapsedCardView);
            availableCollapsedText = itemView.findViewById(R.id.availableBalanceText);
            spentCollapsedText = itemView.findViewById(R.id.spentBalanceText);
            cardNameCollapsedText = itemView.findViewById(R.id.cardHeaderText_collapsedCard);
            categoriesAvailableCollapsed = itemView.findViewById(R.id.viewCategory_collapsedCard);

            // Expanded Card IDs
            expandedCard = itemView.findViewById(R.id.expandedCardView);
            availableExpandedText = itemView.findViewById(R.id.availableBalanceText_expandedCard);
            spentExpandedText = itemView.findViewById(R.id.spentBalanceText_expandedCard);
            cardNameExpandedText = itemView.findViewById(R.id.cardHeaderText_expandedCard);
            budgetedExpandedText = itemView.findViewById(R.id.budgetedBalanceText_expandedCard);
            categoriesDynamicLayout = itemView.findViewById(R.id.categoriesDynamicLayout_expandedCard);

            // Initial Actions
            collapsedCard.setVisibility(View.VISIBLE);
            expandedCard.setVisibility(View.GONE);
        }
    }
}
