package com.example.rakeshyadav.intimeflowchart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class ArrowLine
{
    int start_xy = 110;
    int end_x = 210;
    int end_y = 110;
    int upDown_x = 190;
    int up_y = 100;
    int down_y = 120;
    String text = "L I N E";
    public Bitmap getArrowBitmap(Bitmap bitmap)
    {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setStrokeWidth(3);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawLine(start_xy, start_xy, end_x, end_y, paint);
        canvas.drawLine(upDown_x, up_y, end_x, end_y, paint);
        canvas.drawLine(upDown_x, down_y, end_x, end_y, paint);
        canvas.drawText(text, 140, 125, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}

