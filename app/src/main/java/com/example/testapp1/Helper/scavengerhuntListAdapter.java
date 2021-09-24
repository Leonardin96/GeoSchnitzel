package com.example.testapp1.Helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp1.Entities.ScavengerHuntWithPois;
import com.example.testapp1.R;

import java.util.ArrayList;
import java.util.List;

public class scavengerhuntListAdapter extends RecyclerView.Adapter<scavengerhuntListAdapter.ViewHolder> {

    private List<ScavengerHuntWithPois> huntList;
    private OnItemListener myOnItemListener;


    public scavengerhuntListAdapter(List<ScavengerHuntWithPois> huntList, OnItemListener onItemListener) {
        this.huntList = huntList;
        this.myOnItemListener = onItemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView scavengerhuntNameTxt;
        private TextView creatorNameTxt;

        OnItemListener onItemListener;

        public ViewHolder(@NonNull View view, OnItemListener onItemListener) {
            super(view);
            scavengerhuntNameTxt = view.findViewById(R.id.textView_scavengerhuntlist_layout_huntname);
            creatorNameTxt = view.findViewById(R.id.textView_scavengerhuntlist_layout_creatorname);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClicked(getAdapterPosition());
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
    }

    @Override
    public int getItemCount() {

        return huntList.size();
    }

    public interface OnItemListener {
        void onItemClicked(int position);
    }


}
