package com.example.rakeshyadav.intimeflowchart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;


public class Oval {
    float left_x = 5;
    float top_y = 5;
    float right_x = 150;
    float bottom_y = 80;
    String text = "O V A L";

    final Paint textPaint = new Paint();
    public Bitmap getOvalBitmap(Bitmap bitmap)
    {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        textPaint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF ovalBounds = new RectF(left_x, top_y, right_x, bottom_y);
        canvas.drawOval(ovalBounds, paint);
        canvas.drawText(text,58,47,textPaint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
