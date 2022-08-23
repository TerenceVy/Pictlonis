package com.example.pictionis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.UUID;

public class Message extends AppCompatActivity {

    public String roomID;
    public Button buttonSend;
    public EditText editText;
    public TextView listMessage;
    FirebaseUser user;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        roomID = getIntent().getStringExtra("SESSION_ID");

        buttonSend = findViewById(R.id.buttonSend);
        editText = findViewById(R.id.etSendMessage);
        user = FirebaseAuth.getInstance().getCurrentUser();
        getAllMessage();
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(user.getUid(), roomID, msg);
                }
                else {
                    Toast.makeText(Message.this, "Can't send empty message.", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }
        });
    }

    private void getAllMessage() {
        String sessionID = getIntent().getStringExtra("SESSION_ID");
        listMessage = findViewById(R.id.textView3);
        listMessage.setText("");
        collectionReference = fStore.collection("rooms").document(sessionID).collection("messages");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String idMessage = document.getId();
                                if (!idMessage.isEmpty()) {
                                    String message = document.getData().toString();
                                    listMessage.append(message);
                                    listMessage.setTextColor(-16777216);
                                    listMessage.setMovementMethod(new ScrollingMovementMethod());
                                }
                            }
                        } else {
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void sendMessage(String sender, String roomID, String message) {
        DocumentReference documentReference = fStore.collection("rooms").document(roomID).collection("messages").document(getRandomId());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("message", message);
        documentReference.set(hashMap);
    }

    static String getRandomId()
    {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID.substring(0, 5);
    }
}