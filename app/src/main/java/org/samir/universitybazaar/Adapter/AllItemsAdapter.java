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

import org.samir.universitybazaar.Activity.Sale.ItemDetailActivity;
import org.samir.universitybazaar.Activity.Sale.SellDetailActivity;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.ViewHolder> {
    private ArrayList<Object> objs = new ArrayList<>(); //this will hold all our sell objects.
    private Context context; //parent activity of that will use this adapter. In our case it is MySaleItemActivity.
    private String activity_type;

    public AllItemsAdapter(Context context) {
        this.context = context;
    }

    public void setObjs(ArrayList<Object> objs) {
        this.objs = objs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_item, parent, false);
        return new AllItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int objId = 1;
        int objType = 1;

        Class cls = null;
        if (objs.get(position) instanceof Sell) {
            Sell o = (Sell) objs.get(position);
            holder.txtSaleItemTitle.setText(o.getTitle());
            holder.txtSalePrice.setText("$" + o.getPrice());
            holder.txtSaleDate.setText(o.getCreatedDate());
            holder.txtSaleStatus.setText(o.getStatus());
            objType = 1;
            objId = o.get_id();
            cls = SellDetailActivity.class;
        }else {
            Loan o = (Loan) objs.get(position);
            holder.txtSaleItemTitle.setText(o.getTitle());
            holder.txtSalePrice.setText("$"+o.getPrice() + " per day");
            holder.txtSaleDate.setText(o.getCreatedDate());
            holder.txtSaleStatus.setText(o.getStatus());
            objType = 2;
            objId = o.get_id();
            // to do cls
            cls = SellDetailActivity.class;
        }

       

        //clicking on any sale in the list should navigate to the ItemDetailActivity  page for that sale
        int finalObjType = objType;
        int finalObjId = objId;
        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            //pass the sellId the ItemDetailActivity page. This will allow us to retrieve all the details of the clicked detail inside the ItemDetailActivity page.
            intent.putExtra("objId", finalObjId);
            intent.putExtra("objType", finalObjType);

            intent.putExtra(Constants.ACTIVITY_NAME, activity_type);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return objs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
