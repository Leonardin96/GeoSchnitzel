package com.example.testapp1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp1.Entities.ScavengerHuntWithPois;
import com.example.testapp1.R;

import java.util.List;

public class scavengerhuntListAdapter extends RecyclerView.Adapter<scavengerhuntListAdapter.ViewHolder> {

    private final List<ScavengerHuntWithPois> huntList;
    private final OnItemListener myOnItemListener;


    public scavengerhuntListAdapter(List<ScavengerHuntWithPois> huntList, OnItemListener onItemListener) {
        this.huntList = huntList;
        this.myOnItemListener = onItemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView scavengerhuntNameTxt;
        private final TextView creatorNameTxt;
        private final ImageButton deleteButton;

        OnItemListener onItemListener;

        public ViewHolder(@NonNull View view, OnItemListener onItemListener) {
            super(view);
            scavengerhuntNameTxt = view.findViewById(R.id.textView_scavengerhuntslist_layout_huntname);
            creatorNameTxt = view.findViewById(R.id.textView_scavengerhuntlist_layout_creatorname);
            deleteButton = view.findViewById(R.id.imageButton_list_delete);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag().toString().equals("imageButton_list_delete")) {
                onItemListener.onDeleteClicked(getAdapterPosition());
            } else {
                onItemListener.onItemClicked(getAdapterPosition());
            }
        }
    }

    @NonNull
    @Override
    public scavengerhuntListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scavengerhunt_list_layout, parent, false);

        return new ViewHolder(itemView, myOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull scavengerhuntListAdapter.ViewHolder holder, int position) {
        String huntName = huntList.get(position).scavengerHunt.scavengerHuntName;
        String creatorName = huntList.get(position).scavengerHunt.creatorName;

        holder.scavengerhuntNameTxt.setText(huntName);
        holder.creatorNameTxt.setText(creatorName);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                myOnItemListener.onItemLongClicked(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {

        return huntList.size();
    }

    public interface OnItemListener {
        void onDeleteClicked(int position);
        void onItemClicked(int position);
        void onItemLongClicked(int position);
    }


}
