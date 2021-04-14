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
import org.samir.universitybazaar.Activity.Sale.SellDetailActivity;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

public class MySaleItemListAdapter extends RecyclerView.Adapter<MySaleItemListAdapter.ViewHolder>  {

    public void setSells(ArrayList<Sell> sells) {
        this.sells = sells;
    }

    private ArrayList<Sell> sells = new ArrayList<>(); //this will hold all our sell objects.
    private Context context; //parent activity of that will use this adapter. In our case it is MySaleActivity.
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


        holder.txtSaleItemTitle.setText(sells.get(position).getTitle());
        holder.txtSalePrice.setText("$"+sells.get(position).getPrice());
        holder.txtSaleDate.setText(sells.get(position).getCreatedDate());
        holder.txtSaleStatus.setText(sells.get(position).getStatus());

        //clicking on any sale in the list should navigate to the SellDetailActivity page for that sale.
        holder.parent.setOnClickListener(v->{
            Intent intent = new Intent(context, SellDetailActivity.class);
            //pass the sellId to the SellDetailActivity page. This will allow us to retrieve all the details of the clicked sale inside the SellDetailActivity page.
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
        private final TextView txtSalePrice, txtSaleItemTitle, txtSaleDate, txtSaleStatus;
        private MaterialCardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtSaleItemTitle = itemView.findViewById(R.id.txtSaleItemTitle);
            txtSaleDate = itemView.findViewById(R.id.txtSaleDate);
            txtSalePrice = itemView.findViewById(R.id.txtSalePrice);
            txtSaleStatus = itemView.findViewById(R.id.txtSaleStatus);
        }
    }
}
