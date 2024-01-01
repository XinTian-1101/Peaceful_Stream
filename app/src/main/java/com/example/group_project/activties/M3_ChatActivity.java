package com.example.group_project.activties;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.group_project.R;
import com.example.group_project.adapter.M3_ChatAdapter;
import com.example.group_project.databinding.M3ActivityChatBinding;
import com.example.group_project.model.M3_ChatMessage;
import com.example.group_project.model.M3_User;
import com.example.group_project.network.M3_ApiClient;
import com.example.group_project.network.M3_ApiService;
import com.example.group_project.utilities.Constants;
import com.example.group_project.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class M3_ChatActivity extends M3_BaseActivity {

    private M3ActivityChatBinding binding;
    private M3_User receiverUser;
    private List<M3_ChatMessage> m3ChatMessages;
    private M3_ChatAdapter m3ChatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversionId  = null;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = M3ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();

        ImageView backarrow_menu = findViewById(R.id.back_arrow_chat);
        backarrow_menu.setOnClickListener((View v) -> {
                Intent intent = new Intent(M3_ChatActivity.this, M3_MainActivity.class);
                startActivity(intent);
        });

    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        m3ChatMessages = new ArrayList<>();
        m3ChatAdapter = new M3_ChatAdapter(
                m3ChatMessages,
                getBitmapFromEncodedString(receiverUser.image),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecycleView.setAdapter(m3ChatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage(){
        // Create a HashMap to store the message details
        HashMap<String,Object> message = new HashMap<>();

        // Put various details into the message HashMap
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());

        // Add the message to the "chat" collection in Firestore
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);

        // Check if a conversation ID exists
        if(conversionId != null){
            // If yes, update the conversation with the new message
            updateConversion(binding.inputMessage.getText().toString());
        }else{
            // If no, create a new conversation HashMap
            HashMap<String , Object> conversion = new HashMap<>();

            // Put various details into the conversation HashMap
            conversion.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            conversion.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            conversion.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE));
            conversion.put(Constants.KEY_RECEIVER_ID,receiverUser.id);
            conversion.put(Constants.KEY_RECEIVER_NAME,receiverUser.name);
            conversion.put(Constants.KEY_RECEIVER_IMAGE,receiverUser.image);
            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP,new Date());

            // Add the new conversation to Firestore
            addConversion(conversion);
        }
        if(!isReceiverAvailable){   // Check if the receiver is not available
            try{
                // Create a JSONArray to store the receiver's FCM token
                JSONArray tokens = new JSONArray();
                tokens.put(receiverUser.token);

                // Create a JSONObject to store data for push notification
                JSONObject data = new JSONObject();
                data.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                data.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
                data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());

                // Create a JSONObject to store the overall push notification body
                JSONObject body = new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA,data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                // Send the push notification
                sendNotification(body.toString());

            }catch(Exception exception){
                showToast(exception.getMessage());
            }
        }
        // Clear the input message field
        binding.inputMessage.setText(null);
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String messageBody){
        M3_ApiClient.getClient().create(M3_ApiService.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    try{
                        if(response.body() != null){
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if(responseJson.getInt("failure") == 1){
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    showToast("Notification sent successfully");
                }else{
                    showToast("Error: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                showToast(t.getMessage());
            }
        });
    }
    private void listenAvailabilityOfReceiver(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(
                receiverUser.id
        ).addSnapshotListener(M3_ChatActivity.this, (value, error) -> {
            if(error != null){
                return;
            }
            if(value != null){
                if(value.getLong(Constants.KEY_AVAILABILITY) != null){
                    int availability = Objects.requireNonNull(
                            value.getLong(Constants.KEY_AVAILABILITY)
                    ).intValue();
                    isReceiverAvailable = availability == 1;
                }
                receiverUser.token = value.getString(Constants.KEY_FCM_TOKEN);
                if(receiverUser.image == null){
                    receiverUser.image = value.getString(Constants.KEY_IMAGE);
                    m3ChatAdapter.setReceiverProfileImage(getBitmapFromEncodedString(receiverUser.image));
                    m3ChatAdapter.notifyItemRangeChanged(0, m3ChatMessages.size());
                }
            }
            if(isReceiverAvailable){
                binding.textAvailability.setVisibility(View.VISIBLE);
            }else{
                binding.textAvailability.setVisibility(View.GONE);
            }

        });
    }

//    Listens for changes in the "chat" collection in Firestore.
//    Updates the UI with new chat messages.
    private void listenMessages(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverUser.id)
                .addSnapshotListener(eventListener);

        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }


//    Handles changes in the Firestore documents, specifically when new messages are added.
//    Updates the local list of chat messages and notifies the adapter.
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if(value != null){
            int count = m3ChatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges() ){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    M3_ChatMessage m3ChatMessage = new M3_ChatMessage();
                    m3ChatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    m3ChatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    m3ChatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    m3ChatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    m3ChatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    m3ChatMessages.add(m3ChatMessage);
                }
            }
            Collections.sort(m3ChatMessages,(obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0){
                m3ChatAdapter.notifyDataSetChanged();
            }else{
                m3ChatAdapter.notifyItemRangeInserted(m3ChatMessages.size(), m3ChatMessages.size());
                binding.chatRecycleView.smoothScrollToPosition(m3ChatMessages.size() -1);
            }
            binding.chatRecycleView.setVisibility(View.VISIBLE);
        }
        if(conversionId == null){
            checkForConversion();
        }

    };
    private Bitmap getBitmapFromEncodedString(String encodedImage){
        if(encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }else{
            return null;
        }
    }
    private void loadReceiverDetails(){
        receiverUser = (M3_User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }
    private void setListeners(){
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message){
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }

    private void checkForConversion(){
        if(m3ChatMessages.size() != 0){
            checkForConversionRemotely(
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    preferenceManager.getString(Constants.KEY_USER_ID)
            );
        }
    }
    private void checkForConversionRemotely(String senderId, String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverId)
                .get()
                .addOnCompleteListener(conversationOnCompleteListener);
    }
    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }

}