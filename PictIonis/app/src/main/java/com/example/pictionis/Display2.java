package com.example.pictionis;

import static com.example.pictionis.Draw2.paint_brush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class Display2 extends View {

    public static ArrayList<Path> pathList = new ArrayList<Path>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int current_brush = Color.BLACK;
    private FirebaseFirestore fStore;

    public Display2(Context context) {
        super(context);
        init(context);
    }

    public Display2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public Display2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

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
        paint_brush.setStyle(Paint.Style.STROKE);
        paint_brush.setStrokeCap(Paint.Cap.ROUND);
        paint_brush.setStrokeJoin(Paint.Join.ROUND);
        paint_brush.setStrokeWidth(10f);

        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
