package com.example.rakeshyadav.intimeflowchart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class Rhombus {

    float left_x = 57;
    float top_y = -25;
    float right_x = 135;
    float bottom_y = 55;
    String text = "R H O M B U S";
    final Paint textPaint = new Paint();
    public Bitmap getRhombusBitmap(Bitmap bitmap)
    {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        textPaint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(left_x, top_y, right_x, bottom_y);
        canvas.drawText(text, 20, 85, textPaint);
        canvas.save();
        canvas.rotate(45);
        canvas.drawRect(rectF, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
