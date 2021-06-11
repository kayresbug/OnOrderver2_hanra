package com.example.onorderver2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.onorderver2.Fragment.MenuInfoFragment;
import com.example.onorderver2.model.MenuRecyclerItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    LayoutInflater inflater;
    ArrayList<MenuRecyclerItem> items = null;

    MenuInfoFragment infoFragment;
    FragmentManager fragmentManager;

    RecyclerView.LayoutManager layoutManager;
    LinearLayout linearLayout;

    public MenuAdapter(Context context, ArrayList<MenuRecyclerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.from(context).inflate(R.layout.item_menu_recycler, parent, false);
        MenuViewHolder viewHolder = new MenuViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.menuName.setText(items.get(position).getName());

        String menu = "file:///android_asset/";
        menu = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/assets/";
        menu = menu + items.get(position).getPicurl();
        int menuImage = context.getResources().getIdentifier(items.get(position).getPicurl(), "drawable", context.getPackageName());

        Log.d("INTEGER", "onBindViewHolder: " + menuImage);

        Glide.with(context).load(menuImage).into(holder.menuPicUrl);
//        setAutoSizeView(context, items.get(position).picurl, holder.menuPicUrl);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String formattedPrice = decimalFormat.format(Integer.parseInt(items.get(position).price));
        holder.menuPrice.setText(formattedPrice + "원");
        holder.menuDetail.setText(items.get(position).getInfo());
        holder.btnPutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity)context).timerReset();
                ((MenuActivity)context).callInfo(position);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity)context).timerReset();
                ((MenuActivity)context).callInfo(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView menuName;
        TextView menuPrice;
        ImageView menuPicUrl;
        TextView menuDetail;
        ImageView btnPutCart;
        LinearLayout layout;
//        TextView menuCode;
//        TextView menuCtgCode;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            this.menuName = itemView.findViewById(R.id.item_menu_name);
            this.menuPrice = itemView.findViewById(R.id.item_menu_price);
            this.menuPicUrl = itemView.findViewById(R.id.item_menu_picurl);
            this.menuDetail = itemView.findViewById(R.id.item_menu_info);
            this.btnPutCart = itemView.findViewById(R.id.btn_put_cart);
            this.layout = itemView.findViewById(R.id.menu_recycler_item);
        }
    }

    public static void setAutoSizeView(Context context, String imageUrl, View view) {
        final View changeView = view;
        Glide.with(context).load(imageUrl).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        final int imageHeight = resource.getIntrinsicHeight();
                        final int imageWidth = resource.getIntrinsicWidth();

                        ViewTreeObserver vto = changeView.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                changeView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                int width = changeView.getMeasuredWidth();
                                int height = (width * imageHeight) / imageWidth;

                                changeView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                            }
                        });
                    }
                });
    }
}
