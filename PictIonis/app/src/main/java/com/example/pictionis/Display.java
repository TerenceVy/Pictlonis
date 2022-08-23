package com.example.pictionis;

import static com.example.pictionis.CreateRoom.getAlphaNumericString;
import static com.example.pictionis.Draw.paint_brush;
import static com.example.pictionis.Draw.path;
import static com.example.pictionis.Draw.sessionID;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Display extends View {

    public static ArrayList<Path> pathList = new ArrayList<Path>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int current_brush = Color.BLACK;
    private FirebaseFirestore fStore;

    public Display(Context context) {
        super(context);
        init(context);
    }

    public Display(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public Display(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos,yPos);
                invalidate();
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos,yPos);
                pathList.add(path);
                colorList.add(current_brush);
                String randomId = getAlphaNumericString();
                fStore = FirebaseFirestore.getInstance();
                DocumentReference documentReference = fStore.collection("rooms").document(sessionID).collection("draws").document(randomId);
                if(!documentReference.getId().isEmpty()) {
                    Map<String, Object> draws = new HashMap<>();
                    draws.put("xPos", xPos);
                    draws.put("yPos", yPos);
                    draws.put("brush", current_brush);
                    documentReference.set(draws);
                }
                invalidate();
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        for(int i = 0; i < pathList.size(); i++) {
            paint_brush.setColor(colorList.get(i));
            canvas.drawPath(pathList.get(i),paint_brush);
            invalidate();
        }
    }

    public void init(Context context)
    {
        paint_brush.setAntiAlias(true);
        paint_brush.setColor(current_brush);
        paint_brush.setStyle(Paint.Style.STROKE);
        paint_brush.setStrokeCap(Paint.Cap.ROUND);
        paint_brush.setStrokeJoin(Paint.Join.ROUND);
        paint_brush.setStrokeWidth(10f);

        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
