package com.vasken.house;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

public class Progress  extends SeekBar {

    private int oHeight = 100, oWidth = 20;

    public Progress(Context context) {
            super(context);
    }
    public Progress(Context context, AttributeSet attrs)
    {
            super(context, attrs);
            this.setThumb(null);
    }
    public Progress(Context context, AttributeSet attrs, int defStyle)
    {
            super(context, attrs, defStyle);
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
            int height = View.MeasureSpec.getSize(heightMeasureSpec);
            oHeight = height;
            this.setMeasuredDimension(oWidth, oHeight);

    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
            super.onSizeChanged(h, w, oldw, oldh);
    }
    
    protected void onDraw(Canvas c)
    {
            c.rotate(-90);
            c.translate( -oHeight,0);
            super.onDraw(c);
    }

}
