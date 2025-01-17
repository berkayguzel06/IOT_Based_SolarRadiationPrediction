package com.example.solarpredictionapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleSensorView extends View {
    private Paint circlePaint;
    private Paint textPaint;
    private String sensorValue = "N/A";

    public CircleSensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setSensorValue(String value) {
        this.sensorValue = value;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(cx, cy) - 16;

        canvas.drawCircle(cx, cy, radius, circlePaint);

        canvas.drawText(sensorValue, cx, cy + 10, textPaint);
    }
}
