package com.example.groupxproject.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.groupxproject.Interface.ItemClickListner;
import com.example.groupxproject.R;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView post_title;
    public TextView post_date;
    public TextView post_loc;
    public TextView post_desc;
    public ImageView post_image;
    public RelativeLayout btnExpand1;
    public ExpandableLinearLayout expandableLinearLayout;

    ItemClickListner itemClickListner;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public ItemViewHolder(View itemView, boolean isExpandable)  {
        super(itemView);

        if(isExpandable)
        {
         post_title = (TextView) itemView.findViewById(R.id.post_title);
         post_date = (TextView)itemView.findViewById(R.id.post_date);
         post_loc = (TextView)itemView.findViewById(R.id.post_loc);
         post_desc = (TextView)itemView.findViewById(R.id.post_desc);
         post_image = (ImageView) itemView.findViewById(R.id.post_image);
         //btnExpand1 = (RelativeLayout)itemView.findViewById(R.id.btnExpand1);
         //expandableLinearLayout = (ExpandableLinearLayout)itemView.findViewById(R.id.expandableLayout);
        }

        else {

            post_image = (ImageView) itemView.findViewById(R.id.post_image);
            post_title = (TextView) itemView.findViewById(R.id.post_title);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListner.onClick(view,getAdapterPosition());
            }
        });
    }
}
