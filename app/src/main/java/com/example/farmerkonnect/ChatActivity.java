package com.example.farmerkonnect;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.elevation.SurfaceColors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private Uri imageUri;

    private TextView userNameTextView;
    private EditText messageEditText;
    private ImageButton sendButton, imageButton;
    private RecyclerView messagesRecyclerView;

    private String selectedUserId, selectedUserName;
    private String currentUserId; // Assuming you get this from authentication
    private DatabaseReference chatRef;
    private StorageReference imageStorageRef;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity); // Assuming you have this layout
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            int yourColor = Color.parseColor("#128c7e"); // Example: Teal color
//            int blendedColor = ColorUtils.blendARGB(yourColor, SurfaceColors.SURFACE_2.getColor(this), 0.5f); // Blend with Surface color
            getWindow().setStatusBarColor(yourColor);
        }

        userNameTextView = findViewById(R.id.chat_user_name);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        imageButton = findViewById(R.id.imageButton);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setAdapter(messageAdapter);

        // Get User Information
        Intent intent = getIntent();
        selectedUserId = intent.getStringExtra("selected_user_id");
        selectedUserName = intent.getStringExtra("selected_user_name");
        userNameTextView.setText(selectedUserName);

        // Firebase
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chatRef = FirebaseDatabase.getInstance().getReference().child("chats").child(getChatRoomId());
        imageStorageRef = FirebaseStorage.getInstance().getReference().child("chat_images");
// ... Inside your ChatActivity's onCreate ...
// ... Inside your ChatActivity's onCreate ...

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageEditText.getText().toString();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText, "text"); // For now, only text messages
                }
            }
        });

        // ... Inside your ChatActivity's onCreate ...

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSelector();
            }
        });


        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();  // To avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();

                if (messageList.size() > 0) {
                    messagesRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });

        // ... (Listeners in the next steps) ...
    }

    // ... Inside your ChatActivity ...

    private String getChatRoomId() {
        String id1 = currentUserId;
        String id2 = selectedUserId;

        if (id1.compareTo(id2) > 0) {
            return id1 + "_" + id2;
        } else {
            return id2 + "_" + id1;
        }
    }
// ... Inside your ChatActivity ...

    private void sendMessage(String messageText, String messageType) {
        DatabaseReference messageRef = chatRef.push();

        Message message = new Message(currentUserId, selectedUserId, messageText, messageType, System.currentTimeMillis());
        messageRef.setValue(message);
        messageEditText.setText(""); // Clear the input field
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Start image upload process
            uploadImage();
        }
    }private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    private void uploadImage() {
        if (imageUri != null) {
            final StorageReference fileRef = imageStorageRef.child(System.currentTimeMillis() + ".jpg"); // Unique filename

            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            sendMessage(uri.toString(), "image"); // Send image URL as the message
                        }
                    });
                }
            });
            // Consider adding error handling and potentially a progress indicator here
        }
    }


    // ... Other helper functions to be added ...
}
