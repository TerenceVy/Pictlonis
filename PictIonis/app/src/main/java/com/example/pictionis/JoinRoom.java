package com.example.pictionis;

import static com.example.pictionis.CreateRoom.getAlphaNumericString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class JoinRoom extends AppCompatActivity {

    EditText pEditTextRoomID;
    private TextView textViewData;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = fStore.collection("rooms");
    public static final String TAG = "TAG";
    String userId;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        pEditTextRoomID = findViewById(R.id.editTextRoomID);
        textViewData = findViewById(R.id.textView5);
        fAuth = FirebaseAuth.getInstance();

        getAllDocuments();
        pEditTextRoomID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),JoinRoom.class));
            }
        });

    }

    private void getAllDocuments(){
        textViewData.setText("");
        RelativeLayout layout = new RelativeLayout(this);
        LinearLayout row = new LinearLayout(getApplicationContext());
        row.setOrientation(LinearLayout.VERTICAL);
        userId = fAuth.getCurrentUser().getUid();

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Button button = new Button(getApplicationContext());
                                    String roomName = document.getId();
                                    button.setText("Room : " + roomName);
                                    button.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View v) {
                                                                      Intent intent = new Intent(JoinRoom.this, Draw2.class);
                                                                      intent.putExtra("SESSION_ID",roomName);
                                                                      joinRoom(roomName);
                                                                      startActivity(intent);
                                                                      Toast.makeText(JoinRoom.this, "Join room :" + roomName , Toast.LENGTH_SHORT).show();
                                                                  }
                                    });
                                    row.addView(button);
                                }
                            EditText editText = new EditText(getApplicationContext());
                            editText.setHint("RoomID");
                            editText.setBackgroundColor(-256);
                            Button button2 = new Button(getApplicationContext());
                            button2.setText("Validate");
                            button2.setBackgroundColor(-256);
                            button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String editText2 = editText.getText().toString();
                                    Intent intent = new Intent(JoinRoom.this, Draw2.class);
                                    intent.putExtra("SESSION_ID",editText2);
                                    joinRoom(editText2);
                                    startActivity(intent);
                                    Toast.makeText(JoinRoom.this, "Join room :" + editText2 , Toast.LENGTH_SHORT).show();
                                }
                            });
                            row.addView(editText);
                            row.addView(button2);

                            layout.addView(row);
                        } else {
                            Log.d(TAG, "Error getting documents:", task.getException());
                        }
                        Toast.makeText(JoinRoom.this, "Load finished", Toast.LENGTH_SHORT).show();
                        setContentView(layout);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
}