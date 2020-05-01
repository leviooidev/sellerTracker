package com.techtaro.sellertracker.Adapter;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techtaro.sellertracker.DBTool.TrackingDBHelper;
import com.techtaro.sellertracker.Model.ClsTrackerList;
import com.techtaro.sellertracker.R;

import java.util.List;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.ViewHolder> {


    private List<ClsTrackerList> mTrackerList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    public TrackingAdapter(List<ClsTrackerList> myDataset, Context context, RecyclerView recyclerView) {
        mTrackerList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public TrackingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cardview_tracker, parent, false);

        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        final String strCode = mTrackerList .get(position).getCode();
        final String strSTime = mTrackerList .get(position).getStime();


        holder.tvCVTrackerListCode.setText(strCode);
        holder.tvCVTrackerListDateTime.setText("Time: "+ strSTime);

        holder.imgCVTrackerListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.delete)
                        .setTitle("Confirm Delete?")
                        //.setMessage("Are you confirm to save?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteScannedCode(strCode);
                                mTrackerList.remove(position);
                                mRecyclerV.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mTrackerList.size());
                                notifyDataSetChanged();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {

        return mTrackerList .size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCVTrackerListCode,tvCVTrackerListDateTime;
        ImageView imgCVTrackerListDelete;

        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCVTrackerListCode = (TextView) itemView.findViewById(R.id.tvCVTrackerListCode);
            tvCVTrackerListDateTime = (TextView) itemView.findViewById(R.id.tvCVTrackerListDateTime);
            imgCVTrackerListDelete = (ImageView) itemView.findViewById(R.id.imgCVTrackerListDelete);

            cardView = itemView.findViewById(R.id.CVTrackerList);
        }
    }

    private void deleteScannedCode(String strScannedCode)
    {


        TrackingDBHelper db=new TrackingDBHelper(mContext);

        //OPEN
        db.openDB();

        //INSERT TEMPDELIVERYZONE
        long result=db.deleteScannedJob(strScannedCode);


        if(result>0)
        {
            Toast.makeText(mContext,"Delete Successful",Toast.LENGTH_LONG).show();
            //notifyItemRemoved(this.getLayoutPosition());
        }
        else
        {
            Toast.makeText(mContext,"Unable to delete",Toast.LENGTH_LONG).show();
        }


        //CLOSE
        db.close();




    }



}
