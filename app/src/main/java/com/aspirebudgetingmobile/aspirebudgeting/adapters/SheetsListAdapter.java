package com.aspirebudgetingmobile.aspirebudgeting.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.activities.Home;
import com.aspirebudgetingmobile.aspirebudgeting.models.SheetsListModel;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SheetsListAdapter extends RecyclerView.Adapter<SheetsListAdapter.ViewHolder> {

    private Context context;
    private List<SheetsListModel> list;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private SessionConfig sessionConfig;

    public SheetsListAdapter(Context context, List<SheetsListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sheets_list_card, parent, false);
        sessionConfig = objectFactory.getSessionConfig();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SheetsListModel model = list.get(position);

        holder.name.setText(model.getName());
        holder.nameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionConfig.setSheetId(model.getId());
                context.startActivity(new Intent(context, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        MaterialCardView nameCard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.sheetName_sheetListCard);
            nameCard = itemView.findViewById(R.id.sheetsNameCard);
        }
    }

}
