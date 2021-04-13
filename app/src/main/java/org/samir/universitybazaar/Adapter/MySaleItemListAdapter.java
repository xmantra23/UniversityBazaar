package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.samir.universitybazaar.Activity.Posts.PostActivity;
import org.samir.universitybazaar.Activity.Sale.SellDetailActivity;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

public class MySaleItemListAdapter extends RecyclerView.Adapter<MySaleItemListAdapter.ViewHolder>  {

    public void setSells(ArrayList<Sell> sells) {
        this.sells = sells;
    }

    private ArrayList<Sell> sells = new ArrayList<>(); //this will hold all our post objects.
    private Context context; //parent activity of that will use this adapter. In our case it is MyPostActivity.
    private String activity_type;

    public MySaleItemListAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //set the data for all the fields in the item_post.xml layout file.
        holder.txtSaleItemTitle.setText(sells.get(position).getTitle());
        holder.txtSaleUserName.setText(sells.get(position).getCreatorName());
        holder.txtSaleDate.setText(sells.get(position).getCreatedDate());
        holder.txtSaleStatus.setText(sells.get(position).getStatus());

        //clicking on any post in the list should navigate to the PostActivity page for that post.
        holder.parent.setOnClickListener(v->{
            Intent intent = new Intent(context, SellDetailActivity.class);
            //pass the postId to the PostActivity page. This will allow us to retrieve all the details of the clicked post inside the PostActivity page.
            intent.putExtra(Constants.SELL_ID,sells.get(position).get_id());
            intent.putExtra(Constants.ACTIVITY_NAME,activity_type);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sells.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtSaleUserName, txtSaleItemTitle, txtSaleDate, txtSaleStatus;
        private MaterialCardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtSaleItemTitle = itemView.findViewById(R.id.txtSaleItemTitle);
            txtSaleDate = itemView.findViewById(R.id.txtSaleDate);
            txtSaleUserName = itemView.findViewById(R.id.txtSaleUserName);
            txtSaleStatus = itemView.findViewById(R.id.txtSaleStatus);
        }
    }
}
