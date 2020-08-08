package com.aspirebudgetingmobile.aspirebudgeting.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.models.WidgetCategoriesModel;

import java.util.List;

public class WidgetConfigCheckboxAdapter extends RecyclerView.Adapter<WidgetConfigCheckboxAdapter.ViewHolder> {

    private Context context;
    private List<WidgetCategoriesModel> list;

    public WidgetConfigCheckboxAdapter(Context context, List<WidgetCategoriesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_config_checkbox, parent, false);
        return new WidgetConfigCheckboxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetConfigCheckboxAdapter.ViewHolder holder, int position) {
        final WidgetCategoriesModel model = list.get(position);
        setCategoryCheckboxList(holder, model);
        //Properly handle checkbox selection in a recycleview
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(model.getSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                model.setSelected(isChecked);
            }
        } );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setCategoryCheckboxList(ViewHolder holder,  WidgetCategoriesModel model){
            Log.e("WidgetConfigCheckbox", "setCategoryCheckboxList: AddingCategory"+ model.getCategoryName());
            holder.checkBox.setText(model.getCategoryName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.categoryCheckbox);
        }
    }
}
