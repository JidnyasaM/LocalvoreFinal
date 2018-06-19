package com.curiosity.jidnyasa.localvore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.curiosity.jidnyasa.localvore.dummy.DummyContent;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProfileFeed} and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {


   // private ArrayList<ProfileFeed> mValues = new ArrayList<ProfileFeed>();

    ArrayList<ProfileFeed> mValues;
    Context mContext;

    public DashboardAdapter(Context context,ArrayList<ProfileFeed> items){
        this.mValues = items;
        this.mContext = context;
    }

    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
       // ProfileFeed pf = mValues.get(position);
        holder.mUIdView.setText(mValues.get(position).recUserName);
        holder.mIdView.setText(mValues.get(position).recName);
        holder.mContentView.setText(mValues.get(position).recDesc);

        //Loading image from Glide library.
       // Glide.with(mContext).load(pf.getUrl()).into(holder.mImageView);
        Glide.with(mContext).load(mValues.get(position).getUrl()).into(holder.mImageView);
        //Glide.with(mContext).load(mValues.get(position).url).into(holder.mImageView);
        //Glide.with(holder.itemView).load(mValues.get(position).url).into(holder.mImageView);

        holder.click4more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(mContext,DisplayRecipeActivity.class);
                intent1.putExtra("RECIPE_NAME", mValues.get(position).recName);
                intent1.putExtra("RECIPE_DETAILS", mValues.get(position).recDesc);
                mContext.startActivity(intent1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUIdView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public final Button click4more;
        public ProfileFeed mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUIdView = (TextView) view.findViewById(R.id.recipeDisplayUserName);
            mIdView = (TextView) view.findViewById(R.id.recDisplayName);
            mContentView = (TextView) view.findViewById(R.id.recDisplayDescription);
            mImageView = (ImageView) view.findViewById(R.id.recipeImageDisplay);
            click4more = (Button) view.findViewById(R.id.more);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
