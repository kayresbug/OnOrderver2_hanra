package com.example.onorderver2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onorderver2.model.CategoryRecyclerItem;

import java.util.ArrayList;

public class MenuCategoryAdapter extends RecyclerView.Adapter<MenuCategoryAdapter.CategoryViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<CategoryRecyclerItem> items = null;

    public MenuCategoryAdapter(Context context, ArrayList<CategoryRecyclerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.item_category_recylcer, parent, false);
        CategoryViewHolder viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.ctgName.setText(items.get(position).getCtgName());
        holder.ctgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MenuActivity)context).callCategory(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView ctgName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ctgName = itemView.findViewById(R.id.category_name);
        }
    }

}
