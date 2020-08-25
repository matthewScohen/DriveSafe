package com.example.matthewcohen.uiprototype;

import java.util.ArrayList;

/**
 * Created by matthewcohen on 2/3/18.
 */

public class PERCLOSList
{
    private int size, openFrames, closedFrames;

    ArrayList<Boolean> list;

    public PERCLOSList(int size)
    {
        list = new ArrayList<Boolean>();
        this.size = size;
    }

    public void add(Boolean value)
    {
        //If eyes are open increment openFrames otherwise increment closedFrames
        if(value)
            openFrames++;
        else
            closedFrames++;
        //If there is 2 minutes of data remove the oldest frame from the beginning and add the newest to the end.
        if(list.size() > size)
        {
            //If the oldest frame was open eyes decrement openFrames otherwise decrement closedFrames
            if(list.get(0))
                openFrames--;
            else
                closedFrames--;
            //Remove oldest value
            list.remove(0);
        }
        list.add(value);
    }

    public double getPERCLOS()
    {
        return ((double)closedFrames) / ((double)(openFrames + closedFrames));
    }

    public int getCurrentDataSize()
    {
        return list.size();
    }

    public int getMaxDataSize()
    {
        return this.size;
    }
}
