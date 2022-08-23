package com.example.pictionis;

import static com.example.pictionis.Display2.colorList;
import static com.example.pictionis.Display2.pathList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



import java.util.Map;

public class Draw2 extends AppCompatActivity {
    public static Path path = new Path();
    public static Paint paint_brush = new Paint();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    public static final String TAG = "TAG";
    Button buttonMessage;
    String xPos;
    String yPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw2);
        String sessionID = getIntent().getStringExtra("SESSION_ID");

        collectionReference = fStore.collection("rooms").document(sessionID).collection("draws");
        pathList.clear();
        colorList.clear();
        getAllDocuments(sessionID);
    }

    private void getAllDocuments(String sessionID){
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.isComplete()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String roomName = document.getId();
                                if(!roomName.isEmpty())
                                {
                                    Map<String,Object> test = document.getData();
                                    for(Map.Entry<String, Object> entry : test.entrySet())
                                    {
                                        String k = entry.getKey();
                                        String v = entry.getValue().toString();
                                        if(k.equals("brush"))
                                            colorList.add(Integer.parseInt(v));
                                        if(k.equals("xPos"))
                                            xPos = v;
                                        if(k.equals("yPos"))
                                            yPos = v;
                                        if(xPos != null && yPos!= null) {
                                            path.moveTo(Float.parseFloat(xPos), Float.parseFloat(yPos));
                                            path.lineTo(Float.parseFloat(xPos), Float.parseFloat(yPos));
                                            pathList.add(path);
                                            xPos = null;
                                            yPos = null;
                                            path = new Path();
                                        }
                                    }
                                }
                            }
                            setContentView(R.layout.activity_draw2);
                            buttonMessage = findViewById(R.id.buttonMessage);

                            buttonMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Draw2.this, Message.class);
                                    intent.putExtra("SESSION_ID", sessionID);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents:", task.getException());
                        }
                        Toast.makeText(Draw2.this, "Showing draw in real time", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void currentColor(int c){
        Display.current_brush = c;
        path = new Path();
    }
}