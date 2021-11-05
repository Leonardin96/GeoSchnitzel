package com.example.testapp1.Helper;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // If it isn't the last item in the RecyclerView set make space
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() -1) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
