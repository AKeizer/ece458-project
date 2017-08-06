package com.uwaterlooece458.projectece458;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by akeizer on 8/6/17.
 */
public class FileDisplayAdapter extends RecyclerView.Adapter<FileDisplayAdapter.ViewHolder> {
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mText;
        public ViewHolder(LinearLayout v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.title);
            mText = (TextView) v.findViewById(R.id.text);
        }
    }

    public FileDisplayAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public FileDisplayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_main, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
