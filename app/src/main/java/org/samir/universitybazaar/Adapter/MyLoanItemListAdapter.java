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

import org.samir.universitybazaar.Activity.Loan.LoanDetailActivity;
import org.samir.universitybazaar.Activity.Loan.LoanItemActivity;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

public class MyLoanItemListAdapter extends RecyclerView.Adapter<MyLoanItemListAdapter.ViewHolder>  {

    public void setLoans(ArrayList<Loan> loans) {
        this.loans = loans;
    }

    private ArrayList<Loan> loans = new ArrayList<>(); //this will hold all our loan objects.
    private Context context; //parent activity of that will use this adapter.
    private String activity_type;

    public MyLoanItemListAdapter(Context context){
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


        holder.txtSaleItemTitle.setText(loans.get(position).getTitle());
        holder.txtSalePrice.setText("$"+loans.get(position).getPrice()+" per day");
        holder.txtSaleDate.setText(loans.get(position).getCreatedDate());
        holder.txtSaleStatus.setText(loans.get(position).getStatus());

        //clicking on any loan in the list should navigate to the loanDetailActivity page for that loan.
        holder.parent.setOnClickListener(v->{
            Intent intent = new Intent(context, LoanDetailActivity.class);
            //pass the loanId to the loanDetailActivity page. This will allow us to retrieve all the details of the clicked loan inside the loanDetailActivity page.
            intent.putExtra(Constants.LOAN_ID,loans.get(position).get_id());
            intent.putExtra(Constants.ACTIVITY_NAME,activity_type);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return loans.size();
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
