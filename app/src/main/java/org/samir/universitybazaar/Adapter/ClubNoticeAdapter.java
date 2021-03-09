package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.Models.Comment;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class ClubNoticeAdapter extends RecyclerView.Adapter<ClubNoticeAdapter.ViewHolder> {
    private ArrayList<ClubNotice> notices; //this will hold all the notices for a particular club.

    private Context context; //activity that owns this adapter. This should be the ClubsActivity

    public ClubNoticeAdapter(Context context){
        this.context = context;
        notices = new ArrayList<>();

    }
    @NonNull
    @Override
    public ClubNoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the item_club_notice layout file. This layout file describes how individual comments should look like.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club_notice,parent,false);
        return new ClubNoticeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubNoticeAdapter.ViewHolder holder, int position) {
        //sets all the fields in the notices such as description and date.
        holder.txtAnnouncement.setText(notices.get(position).getTitle());
        holder.txtCreatedDate.setText(notices.get(position).getCreatedDate());
    }

    @Override
    public int getItemCount() {
        //return how many notices are there for a given club.
        return notices.size();
    }

    public void setNotices(ArrayList<ClubNotice> notices){
        this.notices = notices;
        notifyDataSetChanged(); // refreshes the view anytime the data set changes.
    }

    //helper class for the adapter. This class allows the adapter to access all the elements inside the item_club_notice.xml layout file.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtAnnouncement,txtCreatedDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAnnouncement = itemView.findViewById(R.id.txtAnnouncement);
            txtCreatedDate= itemView.findViewById(R.id.txtCreatedDate);
        }
    }
}