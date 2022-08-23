package com.example.pictionis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateRoom extends AppCompatActivity {
    Button pBtnCreateRoom,pBtnCreateRoom2;
    EditText pRoomId;
    FirebaseFirestore fStore;
    String userId;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_create_room);
        fAuth = FirebaseAuth.getInstance();

        pBtnCreateRoom = findViewById(R.id.btnCreateRoom);
        pBtnCreateRoom2 = findViewById(R.id.btnCreateRoom2);
        pRoomId = findViewById(R.id.roomId);

        pBtnCreateRoom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String randomId = getAlphaNumericString();
                DocumentReference documentReference = fStore.collection("rooms").document(randomId);
                Intent intent = new Intent(CreateRoom.this, Draw.class);
                intent.putExtra("SESSION_ID",randomId);
                Map<String,Object> room = new HashMap<>();
                documentReference.set(room);
                joinRoom(randomId);

                Toast.makeText(CreateRoom.this, "Room joined" , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        pBtnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomId = pRoomId.getText().toString().trim();
                Intent intent = new Intent(CreateRoom.this, Draw.class);
                intent.putExtra("SESSION_ID",roomId);
                DocumentReference documentReference = fStore.collection("rooms").document(roomId);
                Map<String,Object> room = new HashMap<>();
                documentReference.set(room);
                joinRoom(roomId);

                Toast.makeText(CreateRoom.this, "Room joined" , Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

        });
        }

        private void joinRoom(String roomId)
        {
            fAuth = FirebaseAuth.getInstance();
            userId = fAuth.getCurrentUser().getUid();
            DocumentReference DocuRef = fStore.collection("rooms").document(roomId).collection("users").document(getAlphaNumericString());
            Map<String,Object> user = new HashMap<>();
            user.put("user_id",userId);
            DocuRef.set(user);
        }
    static String getAlphaNumericString()
    {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID.substring(0, 5);
    }
}