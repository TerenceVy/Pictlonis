package com.example.pictionis;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import static com.example.pictionis.Display.colorList;
import static com.example.pictionis.Display.current_brush;
import static com.example.pictionis.Display.pathList;

import com.google.firebase.firestore.FirebaseFirestore;


public class Draw extends AppCompatActivity {
    public static Path path = new Path();
    public static Paint paint_brush = new Paint();
    FirebaseFirestore fStore;
    public static String sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();
        sessionID = getIntent().getStringExtra("SESSION_ID");
        setContentView(R.layout.activity_draw);
    }

    public void pencil(View view){
        paint_brush.setColor(Color.BLACK);
        currentColor(paint_brush.getColor());
    }
    public void eraser(View view){
        pathList.clear();
        colorList.clear();
        path.reset();
    }
    public void blackColor(View view){
        paint_brush.setColor(Color.BLACK);
        currentColor(paint_brush.getColor());
    }
    public void blueColor(View view){
        paint_brush.setColor(Color.BLUE);
        currentColor(paint_brush.getColor());
    }
    public void yellowColor(View view){
        paint_brush.setColor(Color.YELLOW);
        currentColor(paint_brush.getColor());
    }
    public void redColor(View view){
        paint_brush.setColor(Color.RED);
        currentColor(paint_brush.getColor());
    }
    public void currentColor(int c){
        current_brush = c;
        path = new Path();
    }
}