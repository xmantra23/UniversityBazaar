package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Activity.Messages.InboxMessageActivity;
import org.samir.universitybazaar.Activity.Messages.SentMessageDetailsActivity;
import org.samir.universitybazaar.Models.Message;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

public class SentMessagesAdapter extends  RecyclerView.Adapter<SentMessagesAdapter.ViewHolder>{
    private ArrayList<Message> messages = new ArrayList<>();
    private Context context;

    public SentMessagesAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message,parent,false);
        return new SentMessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //initialize the fields in the item_sent_message.xml layout file
        String subject = messages.get(position).getSubject();
        String receiverName = messages.get(position).getReceiverName();
        String messageDate = messages.get(position).getMessageDate();
        holder.txtSubject.setText(subject);
        holder.txtReceiverName.setText("Sent To: " + receiverName);
        holder.txtSentDate.setText(messageDate);

        //handle clicking on the individual message on the sent messages page.
        holder.parent.setOnClickListener(v->{
            Intent intent = new Intent(context, SentMessageDetailsActivity.class);
            intent.putExtra(Constants.MESSAGE,messages.get(position)); //passing the message object to the next activity.
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(ArrayList<Message> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout parent;
        private TextView txtSubject, txtReceiverName,txtSentDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtSubject = itemView.findViewById(R.id.txtSubject);
            txtReceiverName = itemView.findViewById(R.id.txtReceiverName);
            txtSentDate = itemView.findViewById(R.id.txtSentDate);

        }
    }

}
