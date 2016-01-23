package com.guideapp.guideapp.UI.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guideapp.guideapp.R;
import com.guideapp.guideapp.UI.listener.RecyclerViewItemClickListener;
import com.guideapp.guideapp.model.Local;

import java.util.List;

/**
 * Created by thales on 6/13/15.
 */
public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewHolder> {
    private Context mContext;
    private Cursor data;
    private RecyclerViewItemClickListener mListener;
    private List<Local> mDataSet;

    public LocalAdapter(Context context, RecyclerViewItemClickListener mListener) {
        mContext = context;
        this.mListener = mListener;
    }

    public LocalAdapter(Context context, RecyclerViewItemClickListener mListener, List<Local> dataSet) {
        mContext = context;
        this.mListener = mListener;
        this.mDataSet = dataSet;
    }

    class LocalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView photoView;
        public final TextView descriptionView;
        public final TextView addressView;
        public final ImageView favoriteView;
        public RatingBar ratingView;

        public LocalViewHolder(View view) {
            super(view);
            photoView = (ImageView) view.findViewById(R.id.local_picture);
            descriptionView = (TextView) view.findViewById(R.id.local_text);
            addressView = (TextView) view.findViewById(R.id.local_address);
            favoriteView = (ImageView) view.findViewById(R.id.local_favorite);
            ratingView = (RatingBar) itemView.findViewById(R.id.ratingBar);

            view.setOnClickListener(this);

            favoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFilterPopup(v);
                }
            });
        }

        private void showFilterPopup(View v) {
            PopupMenu popup = new PopupMenu(mContext, v);
            popup.getMenuInflater().inflate(R.menu.menu_local, popup.getMenu());


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_remove:
                            Toast.makeText(mContext, "action_add", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        }

        public void populate(Cursor data) {
            String description = data.getString(0);
            String photo = data.getString(1);
            String address = data.getString(2);

            descriptionView.setText(description);
            addressView.setText(address);

            Glide.with(mContext.getApplicationContext())
                    .load(photo)
                    .into(photoView);
        }

        public void populate(Local data) {
            descriptionView.setText(data.getDescription());
            addressView.setText(data.getAddress());

            ratingView.setRating(4.3f);

            LayerDrawable stars = (LayerDrawable) ratingView.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(mContext.getResources().getColor(R.color.primary_star), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(mContext.getResources().getColor(R.color.secondary_star), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(mContext.getResources().getColor(R.color.secondary_star), PorterDuff.Mode.SRC_ATOP);
        }

        @Override
        public void onClick(final View view) {
            if (mListener != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mListener.onItemClick(view, getLayoutPosition());
                    }
                }, 200);
            }
        }
    }

    @Override
    public LocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local, parent, false);
        return new LocalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LocalViewHolder holder, int position) {
        holder.populate(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.getCount();
        }
        return mDataSet.size();
    }

    public void swapCursor(Cursor data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Cursor getCursor()
    {
        return this.data;
    }
}