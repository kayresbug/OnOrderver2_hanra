package com.example.onorderver2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onorderver2.model.OrderRecyclerItem;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<OrderRecyclerItem> items;
    private Context context;

    public OrderAdapter(Context context, ArrayList<OrderRecyclerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_cart_order, parent, false);

        OrderViewHolder viewHolder = new OrderViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.orderMenuName.setText(items.get(position).getName() + "  X");
        Log.d("count", "onBindViewHolder: " + items.get(position).getPrice());
        holder.orderCount.setText(items.get(position).getCount());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SIZE ::: ", "onClick: " + items.size());
                Log.d("COUNT ::: ", "onClick: " + items.get(position).getCount());
                Log.d("ITEM", "onClick: " + items.get(position).getPrice());
                MenuActivity menuActivity = new MenuActivity();
                menuActivity.removeOrder((Integer.parseInt(items.get(position).getPrice())) * Integer.parseInt(items.get(position).getCount()));
                items.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderMenuName;
        TextView orderCount;
        LinearLayout layout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderMenuName = itemView.findViewById(R.id.item_cart_name);
            orderCount = itemView.findViewById(R.id.item_cart_count);
            layout = itemView.findViewById(R.id.order_layout);
        }
    }

    public void removeDate() {
        items.clear();
        notifyDataSetChanged();
    }
}
