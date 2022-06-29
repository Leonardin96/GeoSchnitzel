package com.example.testapp1.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp1.Entities.PointOfInterest;
import com.example.testapp1.Callbacks.ItemMoveCallback;
import com.example.testapp1.R;

import java.util.Collections;
import java.util.List;


public class poiEditingListAdapter extends RecyclerView.Adapter<poiEditingListAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private final List<PointOfInterest> data;
    private ClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View rowView;
        private final TextView mTitle;
        private final ImageButton deleteBtn;
        ClickListener listener;

        public MyViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);

            rowView = itemView;
            mTitle = itemView.findViewById(R.id.textView_edit_pois_layout_poi_name);
            deleteBtn = itemView.findViewById(R.id.imageButton_poi_delete);
            this.listener = clickListener;

            itemView.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag().toString().equals("imageButton_poi_delete")) {
                listener.onDeleteClicked(getAdapterPosition());
            } else {
                listener.onItemClicked(getAdapterPosition());
            }

        }
    }

    public poiEditingListAdapter(List<PointOfInterest> data, ClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_pois_list_layout, parent, false);
        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText(data.get(position).poiName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundResource(R.drawable.recyclerview_item_background_interaction);
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundResource(R.drawable.recyclerview_item_background);
    }

    public List<PointOfInterest> getData() {
        return data;
    }

    public interface ClickListener {
        void onDeleteClicked(int position);
        void onItemClicked(int position);
    }


}
