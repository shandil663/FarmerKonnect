package com.example.farmerkonnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final int MSG_TYPE_LEFT = 0;
    private final int MSG_TYPE_RIGHT = 1;

    private List<Message> messageList;
    private FirebaseUser firebaseUser;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_left, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_right, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (message.getImageURL().equals("text")) {
            holder.messageText.setVisibility(View.VISIBLE);
            holder.messageImage.setVisibility(View.GONE);
            holder.messageText.setText(message.getMessageText());
        } else {
            holder.messageText.setVisibility(View.GONE);
            holder.messageImage.setVisibility(View.VISIBLE);
            Picasso.get().load(message.getMessageText()).into(holder.messageImage);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView messageImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.messageTextView);
            messageImage = itemView.findViewById(R.id.messageImageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messageList.get(position).getSenderId().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
