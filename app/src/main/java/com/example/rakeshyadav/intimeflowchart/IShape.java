package com.example.rakeshyadav.intimeflowchart;

import org.json.JSONException;
import org.json.JSONObject;

public class IShape
{
    private int prevX,prevY,ids;
    private String viewName;
    private float angle;
    private float scaleXY;
    IShape(String viewName,int ids,int prevX, int prevY, float angle,float scaleXY)
    {
        this.prevX = prevX;
        this.prevY = prevY;
        this.ids = ids;
        this.viewName = viewName;
        this.angle = angle;
        this.scaleXY = scaleXY;
    }
    public float getScaleXY() {
        return scaleXY;
    }
    public void setScaleXY(float scaleXY) {
        this.scaleXY = scaleXY;
    }
    public String getViewName()
    {
        return viewName;
    }

    public int getPrevX()
    {
        return prevX;
    }
    public void setPrevX(int prevX)
    {
        this.prevX = prevX;
    }
    public int getPrevY()
    {
        return prevY;
    }
    public void setPrevY(int prevY)
    {
        this.prevY = prevY;
    }
    public int getIds()
    {
        return ids;
    }
    public float getAngle()
    {
        return angle;
    }
    public void setAngle(float angle)
    {
        this.angle = angle;
    }
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("view",getViewName());
            obj.put("id",getIds());
            obj.put("x",getPrevX());
            obj.put("y",getPrevY());
            obj.put("angle",getAngle());
            obj.put("scale",getScaleXY());
        } catch (JSONException e) {
            System.out.println("DefaultListItem.toString JSONException: " + e.getMessage());
        }
        return obj;
    }
}

