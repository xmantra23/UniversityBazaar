package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Activity.Messages.InboxMessageActivity;
import org.samir.universitybazaar.Activity.Messages.NewMessageActivity;
import org.samir.universitybazaar.Database.MessageDAO;
import org.samir.universitybazaar.Models.Message;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * adapter class for displaying messages in the inbox
 */
public class InboxMessageAdapter extends RecyclerView.Adapter<InboxMessageAdapter.ViewHolder> {
    private ArrayList<Message> messages = new ArrayList<>();
    private Context context;

    public InboxMessageAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox_message,parent,false);
        return new InboxMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //initialize the fields in the item_inbox_message.xml layout file
        String subject = messages.get(position).getSubject();
        String senderName = messages.get(position).getSenderName();
        String messageDate = messages.get(position).getMessageDate();
        holder.txtSubject.setText(subject);

        //if the message has not been read then make the subject bold.
        if(messages.get(position).getReadStatus() == 0){
            holder.txtSubject.setTypeface(null, Typeface.BOLD);
        }else{
            holder.txtSubject.setTypeface(null, Typeface.NORMAL);
        }
        holder.txtSenderName.setText("Sent By: " + senderName);
        holder.txtReceivedDate.setText(messageDate);

        //handle clicking on the individual message on the inbox page.
        holder.parent.setOnClickListener(v->{
            //user clicked on this message so update the read status to 1 (message read) in the database.
            MessageDAO db = new MessageDAO(context);
            int messageId = messages.get(position).get_id();
            if(db.markMessageRead(messageId)){
                //success in updating read status.
                //changing locally too so that it is locally updated as soon as we click the item and we don't need to reload from the database
                //to see the changes.
                messages.get(position).setReadStatus(1);
                holder.txtSubject.setTypeface(null,Typeface.NORMAL);
            }

            //show the details of the clicked on message.
            Intent intent = new Intent(context, InboxMessageActivity.class);
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
        private TextView txtSubject, txtSenderName,txtReceivedDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtSubject = itemView.findViewById(R.id.txtSubject);
            txtSenderName = itemView.findViewById(R.id.txtSenderName);
            txtReceivedDate = itemView.findViewById(R.id.txtReceivedDate);

        }
    }
}
