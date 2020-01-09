package com.example.pestpro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<uploadinfo> mUploads;
    private OnItemClickListner mListner;

    public ImageAdapter(Context context, List<uploadinfo> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.row,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {

        uploadinfo uploadCurrent=mUploads.get(i);
        imageViewHolder.mTextView.setText(uploadCurrent.getImageName());
        imageViewHolder.mDate.setText(uploadCurrent.getDate());
        imageViewHolder.mName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageURL())
                .placeholder(R.drawable.loading)
                .into(imageViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView mTextView,mDate,mName;
        public ImageView mImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);


            mTextView =itemView.findViewById(R.id.titleHistory);
            mDate=itemView.findViewById(R.id.dateHistory);
            mName=itemView.findViewById(R.id.nameHistory);
            mImageView=itemView.findViewById(R.id.imageHistory);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListner!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    mListner.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(mContext.getString(R.string.action));
            MenuItem doWhatever=menu.add(Menu.NONE,1,1,"Do Whatever");
            MenuItem delete=menu.add(Menu.NONE,2,2,mContext.getString(R.string.delete));

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListner!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){

                    switch (item.getItemId()){
                        case 1:
                            mListner.OnWhatEverItemClick(position);
                            return true;
                        case 2:
                            mListner.OnDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListner{
        void OnItemClick(int position);

        void OnWhatEverItemClick(int position);

        void OnDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListner listener) {

        mListner=listener;
    }

}