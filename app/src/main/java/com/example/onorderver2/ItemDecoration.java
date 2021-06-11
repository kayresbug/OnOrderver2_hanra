package com.example.onorderver2;

import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private int outerMargin;

    public ItemDecoration(Activity activity) {
        spanCount = 2;
        spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12, activity.getResources().getDisplayMetrics());
        outerMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12, activity.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int maxCount = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int coloumn = position % spanCount;
        int row = position / spanCount;
        int lastRow = (maxCount - 1) / spanCount;

        outRect.left = coloumn * spacing / spanCount;
        outRect.right = spacing - (coloumn + 1) * spacing / spanCount;
        outRect.top = spacing * 2;

        if (row == lastRow) {
            outRect.bottom = outerMargin;
        }
    }
}
