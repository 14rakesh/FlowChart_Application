package com.example.rakeshyadav.intimeflowchart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ImageView img;
    private int ovalCount = 0,circleCount = 0, lineCount = 0, rectCount = 0, rhomCount = 0, squareCount = 0;
    static int circleId = 300;
    static int ovalId = 600;
    static int rectangleId = 200;
    static int arrowLineId = 100;
    static int rhombusId = 400;
    static int squareId = 500;

    int views = 100;
    float lastTouchX, lastTouchY, posX, posY, prevX, prevY, dx, dy;
    float angle = 0;
    private AbsoluteLayout absoluteLayout;
    Button addViewCir, addViewLine, addViewRect, addViewRhombus, addViewSquare, addViewOval;
    ImageButton btnZoomIn, btnZoomOut, btnRotate;
    private Context context;
    ArrayList<Integer> arraylist = new ArrayList<>();
    ArrayList<IShape> shapesArray = new ArrayList<>();
    int imageDelId = 0, selectId = 0;
    float scale_xy = 1;
    Circle circle = new Circle();
    Oval oval = new Oval();
    Rectangle rectangle = new Rectangle();
    ArrowLine arrowLine = new ArrowLine();
    Rhombus rhombus = new Rhombus();
    Square square = new Square();
    Bitmap bm;
    String str;
    String result = "";
    String fileName;
    HttpClient httpclient = new DefaultHttpClient();
    String url;
    String str_url = "http://192.168.4.87:8094/ShapesWebService/webapi/myresource";
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        prevX = 50;
        prevY = 100;

        absoluteLayout = (AbsoluteLayout) findViewById(R.id.abs);
        addViewCir = (Button) findViewById(R.id.btnAddCircle);
        addViewOval = (Button) findViewById(R.id.btnAddOval);
        addViewRect = (Button) findViewById(R.id.btnAddrect);
        addViewLine = (Button) findViewById(R.id.btnLine);
        btnRotate = (ImageButton) findViewById(R.id.btnrotate);
        btnZoomIn = (ImageButton) findViewById(R.id.btnzoomin);
        btnZoomOut = (ImageButton) findViewById(R.id.btnzoomout);
        addViewRhombus = (Button) findViewById(R.id.btnAdd_rhombus);
        addViewSquare = (Button) findViewById(R.id.btnAdd_Square);

        addViewCir.setOnClickListener(onClickListener);
        addViewOval.setOnClickListener(onClickListener);
        addViewRect.setOnClickListener(onClickListener);
        addViewRhombus.setOnClickListener(onClickListener);
        addViewSquare.setOnClickListener(onClickListener);
        addViewLine.setOnClickListener(onClickListener);
        btnRotate.setOnClickListener(onClickListener);
        btnZoomIn.setOnClickListener(onClickListener);
        btnZoomOut.setOnClickListener(onClickListener);
        openView(view);
    }

    /**
     * Common click listener for clickable controls
     */
    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btnAddCircle:
                    addCircle();
                    break;
                case R.id.btnAddOval:
                    addOval();
                    break;
                case R.id.btnAddrect:
                    addRectangle();
                    break;
                case R.id.btnAdd_rhombus:
                    addRhombus();
                    break;
                case R.id.btnAdd_Square:
                    addSquare();
                    break;
                case R.id.btnLine:
                    addArrowLine();
                    break;
                case R.id.btnrotate:
                    rotateView(v);
                    break;
                case R.id.btnzoomin:
                    zoomIn(v);
                    break;
                case R.id.btnzoomout:
                    zoomOut(v);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Touch listener for view
     */
    View.OnTouchListener onTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            v.bringToFront();
            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                {
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    imageDelId = v.getId();
                    selectId = v.getId();
                    //v.setBackgroundResource(R.drawable.border_layout);
                    break;
                }
                //Update new postion of X and Y coordinates in arraylist
                case MotionEvent.ACTION_UP:
                   // v.setBackgroundResource(0);
                    for (IShape shape : shapesArray)
                    {
                        if (v.getId() == shape.getIds())
                        {
                            shape.setPrevX((int) prevX);
                            shape.setPrevY((int) prevY);
                        }

                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    dx = event.getX() - lastTouchX;
                    dy = event.getY() - lastTouchY;

                    posX = prevX + dx;
                    posY = prevY + dy;

                    if (posX > 0 && posY > 0 && (posX + v.getWidth()) < absoluteLayout.getWidth()
                            && (posY + v.getHeight()) < absoluteLayout.getHeight())
                    {
                        v.setLayoutParams(new AbsoluteLayout.LayoutParams(v.getMeasuredWidth(),
                                v.getMeasuredHeight(), (int) posX, (int) posY));

                        prevX = posX;
                        prevY = posY;
                    }

                    break;

                }

            }
            return true;
        }
    };
    /**
     * Add Circle view dynamically
     */
    private void addCircle()
    {
        img = new ImageView(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);
        if (circleCount < views)
        {
            circleId = circleId + 1;
            img.setId(circleId);

            arraylist.add(circleId);
            img.setImageBitmap(circle.getCircleBitmap(bm));

            absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 50,100));
            shapesArray.add(new IShape("Circle", img.getId(), (int) (prevX), (int) (prevY), angle, scale_xy));
            angle = 0;
            circleCount++;
            if (circleCount == views)
                addViewCir.setEnabled(false);
        }

        img.setOnTouchListener(onTouchListener);
    }
    /**
     * Add Oval view dynamically
     */
    private void addOval()
    {
        img = new ImageView(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        if (ovalCount < views)
        {
            ovalId = ovalId + 1;
            img.setId(ovalId);

            arraylist.add(ovalId);
            img.setImageBitmap(oval.getOvalBitmap(bm));

            absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 50,100));
            shapesArray.add(new IShape("Oval", img.getId(), (int) (prevX), (int) (prevY), angle, scale_xy));
            angle = 0;
            ovalCount++;
            if (ovalCount == views)
                addViewOval.setEnabled(false);
        }

        img.setOnTouchListener(onTouchListener);
    }
    /**
     * Add Rectangle view dynamically
     */
    private void addRectangle()
    {
        img = new ImageView(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
        if (rectCount < views)
        {
            rectangleId = rectangleId + 1;
            img.setId(rectangleId);

            arraylist.add(rectangleId);
            img.setImageBitmap(rectangle.getRectangleBitmap(bm));

            absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,50,100));
            shapesArray.add(new IShape("Rectangle", img.getId(), (int) (prevX), (int) (prevY),angle, scale_xy));
            angle=0;

            rectCount++;
            if (rectCount == views)
                addViewRect.setEnabled(false);
        }

        img.setOnTouchListener(onTouchListener);
    }

    /**
     * Add Rhombus view dynamically
     */
    private void addRhombus()
    {
        img = new ImageView(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);
        if (rhomCount < views)
        {
            rhombusId = rhombusId + 1;
            img.setId(rhombusId);

            arraylist.add(rhombusId);
            img.setImageBitmap(rhombus.getRhombusBitmap(bm));

            absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,50,100));
            shapesArray.add(new IShape("Rhombus", img.getId(), (int) (prevX), (int) (prevY),angle, scale_xy));
            angle=0;

            rhomCount++;
            if (rhomCount == views)
                addViewRhombus.setEnabled(false);
        }

        img.setOnTouchListener(onTouchListener);
    }

    /**
     * Add Square view dynamically
     */
    private void addSquare()
    {
        img = new ImageView(context);
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        if (squareCount < views)
        {
            squareId = squareId + 1;
            img.setId(squareId);

            arraylist.add(squareId);
            img.setImageBitmap(square.getSquareBitmap(bm));

            absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,50,100));
            shapesArray.add(new IShape("Square", img.getId(), (int) (prevX), (int) (prevY),angle, scale_xy));
            angle=0;

            squareCount++;
            if (squareCount == views)
                addViewSquare.setEnabled(false);
        }

        img.setOnTouchListener(onTouchListener);
    }
    /**
     * Add Line view dynamically
     */
    private void addArrowLine()
    {
        img = new ImageView(context);

        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg4);
        if (lineCount < views)
        {
            arrowLineId = arrowLineId + 1;
            img.setId(arrowLineId);

            arraylist.add(arrowLineId);
            img.setImageBitmap(arrowLine.getArrowBitmap(bm));
            absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 50,100));
            shapesArray.add(new IShape("Arrow", img.getId(), (int) (prevX), (int) (prevY),angle, scale_xy));
            angle = 0;
            lineCount++;
            if (lineCount == views)
                addViewLine.setEnabled(false);
        }
        img.setOnTouchListener(onTouchListener);
    }
    /**
     * Remove particular view
     */
    public void removeView(View view)
    {
        try
        {
            circleCount = circleCount - 1;
            rectCount = rectCount - 1;
            lineCount = lineCount - 1;
            for(IShape remove_shape:shapesArray)
            {
                if (imageDelId == remove_shape.getIds())
                {
                    shapesArray.remove(remove_shape);
                }
            }
            absoluteLayout.removeView(findViewById(imageDelId));
            absoluteLayout.invalidate();
            addViewCir.setEnabled(true);
            addViewRect.setEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Rotated particular view
     */
    public void rotateView(View v)
    {
        try
        {
            img = (ImageView) findViewById(selectId);
            if(img.getId()>=100&&img.getId()<=200)
            {
                angle = angle + 45;
                Matrix matrix = new Matrix();
                img.setScaleType(ImageView.ScaleType.MATRIX);
                matrix.postRotate(angle, img.getDrawable().getBounds().width() / 2, img.getDrawable().getBounds().height() / 2);
                img.setImageMatrix(matrix);
                for (IShape iShape : shapesArray)
                {
                    if (iShape.getIds() == img.getId())
                        iShape.setAngle(angle);
                }

                if (angle == 360)
                    angle = 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Zoom In particular view
     */
    public void zoomIn(View v)
    {
        try
        {
            img = (ImageView) findViewById(selectId);
            img.setScaleX(scale_xy);
            img.setScaleY(scale_xy);
            for (IShape iShape : shapesArray)
            {
                if (iShape.getIds() == img.getId())
                    iShape.setScaleXY(scale_xy);
            }
            if (scale_xy <= 4)
            {
                scale_xy = (float) (scale_xy + 0.3);
            }
            absoluteLayout.setOnTouchListener(onTouchListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Zoom Out particular view
     */
    public void zoomOut(View v)
    {
        try
        {
            img = (ImageView) findViewById(selectId);

            if (scale_xy != 0)
            {
                scale_xy = (float) (scale_xy - 0.3);
            }
            img.setScaleX(scale_xy);
            img.setScaleY(scale_xy);
            absoluteLayout.setOnTouchListener(onTouchListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Save view in arraylist
     */
    public void saveView(View v)
    {
        JSONArray jsonArray = new JSONArray();
        for (int i=0; i < shapesArray.size(); i++)
        {
            jsonArray.put(shapesArray.get(i).getJSONObject());

        }
        str = jsonArray.toString();

        Log.d("JSON", jsonArray.toString());
        try {
            // Save the list of entries to internal storage
            fileName = getIntent().getExtras().getString("fname");
            url = str_url+fileName;
            InternalStorage.writeObject(this, fileName, str);
            Log.e("save", "save in internal storage");


        } catch (IOException e)
        {
            Log.e("Error", e.getMessage());
        }
        new PostTask().execute(str);
//        absoluteLayout.removeAllViews();
//        absoluteLayout.invalidate();
        Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
    }
    /**
     * Open save view
     */
    public void openView(View v)
    {

        fileName = getIntent().getExtras().getString("fname");
        url = str_url+fileName;
        String result = "non";
        try
        {
            result = InternalStorage.readObject(this, fileName);
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        JSONArray jArray;
        new GetTask().execute();

        try
        {
            jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                img = new ImageView(context);
                if((jObj.getString("view")).equals("Circle"))
                {
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);

                    img.setId(Integer.parseInt(jObj.getString("id")));
                    img.setImageBitmap(circle.getCircleBitmap(bm));
                    absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Integer.parseInt(jObj.getString("x")), Integer.parseInt(jObj.getString("y"))));
                    img.setRotation(Integer.parseInt(jObj.getString("angle")));
                    img.setScaleX(scale_xy);
                    img.setScaleY(scale_xy);
                    img.setOnTouchListener(onTouchListener);
                }
                else if((jObj.getString("view")).equals("Oval"))
                {
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

                    img.setId(Integer.parseInt(jObj.getString("id")));
                    img.setImageBitmap(oval.getOvalBitmap(bm));
                    absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Integer.parseInt(jObj.getString("x")), Integer.parseInt(jObj.getString("y"))));
                    img.setRotation(Integer.parseInt(jObj.getString("angle")));
                    img.setScaleX(scale_xy);
                    img.setScaleY(scale_xy);
                    img.setOnTouchListener(onTouchListener);
                }
                else if((jObj.getString("view")).equals("Rectangle"))
                {
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);

                    img.setId(Integer.parseInt(jObj.getString("id")));
                    img.setImageBitmap(rectangle.getRectangleBitmap(bm));
                    absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Integer.parseInt(jObj.getString("x")), Integer.parseInt(jObj.getString("y"))));
                    img.setRotation(Integer.parseInt(jObj.getString("angle")));
                    img.setScaleX(scale_xy);
                    img.setScaleY(scale_xy);
                    img.setOnTouchListener(onTouchListener);
                }
                else if((jObj.getString("view")).equals("Rhombus"))
                {
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);

                    img.setId(Integer.parseInt(jObj.getString("id")));
                    img.setImageBitmap(rhombus.getRhombusBitmap(bm));
                    absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Integer.parseInt(jObj.getString("x")), Integer.parseInt(jObj.getString("y"))));
                    img.setRotation(Integer.parseInt(jObj.getString("angle")));
                    img.setScaleX(scale_xy);
                    img.setScaleY(scale_xy);
                    img.setOnTouchListener(onTouchListener);
                }
                else if((jObj.getString("view")).equals("Square"))
                {
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);

                    img.setId(Integer.parseInt(jObj.getString("id")));
                    img.setImageBitmap(square.getSquareBitmap(bm));
                    absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Integer.parseInt(jObj.getString("x")), Integer.parseInt(jObj.getString("y"))));
                    img.setRotation(Integer.parseInt(jObj.getString("angle")));
                    img.setScaleX(scale_xy);
                    img.setScaleY(scale_xy);
                    img.setOnTouchListener(onTouchListener);
                }
                else if((jObj.getString("view")).equals("Arrow"))
                {
                    bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg4);

                    img.setId(Integer.parseInt(jObj.getString("id")));
                    img.setImageBitmap(arrowLine.getArrowBitmap(bm));
                    absoluteLayout.addView(img, new AbsoluteLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT, Integer.parseInt(jObj.getString("x")), Integer.parseInt(jObj.getString("y"))));
                    img.setRotation(Integer.parseInt(jObj.getString("angle")));
                    img.setScaleX(scale_xy);
                    img.setScaleY(scale_xy);
                    img.setOnTouchListener(onTouchListener);

                }
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        //btnOpen.setEnabled(false);
    }

    class GetTask extends AsyncTask<Void,Void,Void>
    {
        InputStream inputStream = null;
        @Override
        protected Void doInBackground(Void... params) {
            HttpGet httpget = new HttpGet(url);

            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);
                inputStream = response.getEntity().getContent();//execute your request and parse response
                if(inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                }else
                    result = "Did not work!";
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(Void unused) {
            //Toast.makeText(getApplicationContext(), "successfully save", Toast.LENGTH_LONG).show();
        }
    }
    class PostTask extends  AsyncTask<String,Void,Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            String valueIWantToSend = params[0];
            HttpPost httppost = new HttpPost(url);
            try {
                // Add your data
                StringEntity se = new StringEntity(valueIWantToSend);
                httppost.setEntity(se);
                httppost.setHeader("Content-Type", "text/plain");

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Log.e("Post Data","Post Data Successfully on server*******************");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void unused) {
            Toast.makeText(getApplicationContext(), "successfully save", Toast.LENGTH_LONG).show();
        }
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String results = "";
        while((line = bufferedReader.readLine()) != null)
            results += line;

        inputStream.close();
        return results;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.delete_button) {
            removeView(view);
        }
        if(id == R.id.save_button) {
         saveView(view);
       }
        if(id == R.id.open_button) {
            startActivity(new Intent(this,ProjectList.class));
        }
        return super.onOptionsItemSelected(item);
    }
}