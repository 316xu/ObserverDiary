package hust.xujifa.observerdiary.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.design.widget.*;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import hust.xujifa.observerdiary.R;

/**
 * Created by xujifa on 2015/11/28.
 */
public class TestPlayer extends View implements ValueAnimator.AnimatorUpdateListener {
    float scale=getResources().getDisplayMetrics().density;
    Paint ring1;
    Paint ring2;
    Paint circle;
    Paint tri;
    Path p;
    int s;
    Handler h=null;
    ValueAnimator v;
    boolean playing=false;
    RectF rect=new RectF((float)(2.5*scale),(float)(2.5f*scale),(float)(61.5*scale),(float)(61.5*scale));
    float x1=18*scale,y1=18*scale,x2=46*scale,y2=46*scale,temp1=32*scale,temp2=32*scale;
    public TestPlayer(Context context) {
        this(context, null);
    }

    public TestPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public void setH(Handler h){
        this.h=h;
    }
    public TestPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ring1=new Paint();
        ring1.setColor(getResources().getColor(R.color.blue3));
        ring1.setStyle(Paint.Style.STROKE);
        ring1.setStrokeWidth(10f);

        circle=new Paint();
        circle.setColor(getResources().getColor(R.color.colorAccent));

        circle.setStyle(Paint.Style.FILL);

        ring2=new Paint();

        ring1=new Paint();
        ring1.setColor(getResources().getColor(R.color.blue3));
        ring2.setColor(getResources().getColor(R.color.blue2));
        ring1.setStyle(Paint.Style.STROKE);
        ring2.setStyle(Paint.Style.STROKE);
        ring1.setStrokeWidth(10f);
        ring2.setStrokeWidth(10f);
        tri=new Paint();
        tri.setColor(getResources().getColor(R.color.red));
        tri.setStyle(Paint.Style.FILL_AND_STROKE);

        p=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float dw=64*scale;
        float dh=64*scale;
        setMeasuredDimension((int) dw, (int) dh);



    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2, 29 * scale, ring1);
        canvas.drawCircle((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2, 28 * scale, circle);
        canvas.drawArc(rect, -90f, s * 360f / 1000, false, ring2);

        p.reset();
        p.moveTo(x1, y1);
        p.lineTo(x2, temp1);
        p.lineTo(x2, temp2);
        p.lineTo(x1, y2);
        p.lineTo(x1, y1);
        canvas.drawPath(p, tri);


    }
    public void start(int a){
        v= ValueAnimator.ofInt(0,1000);
        v.setDuration(a);
        v.addUpdateListener(this);
        playing=true;
        v.start();
    }



    public void stop(){
        playing=false;
        v.cancel();
        ValueAnimator v2=ValueAnimator.ofInt(0,100);
        v2.setDuration(200);
        v2.addUpdateListener(this);
        v2.start();
    }
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int va=(int)animation.getAnimatedValue();
        if(playing){
            s=va;
            if(s<=100){
                temp1=(y1+y2)/2+(y1-y2)/2*s/100;
                temp2=(y1+y2)/2-(y1-y2)/2*s/100;
            }else if(s>900){
                temp1=y1+(y2-y1)/2*(s-900)/100;
                temp2=y2+(y1-y2)/2*(s-900)/100;
            }
            if(va==1000){
                playing=false;
                v.cancel();
                if(h!=null)
                    h.sendEmptyMessage(0);
            }

        }else{
            s=0;
            temp1=y1+(y2-y1)/2*(va)/100;
            temp2=y2+(y1-y2)/2*(va)/100;
        }
        invalidate();
    }
}
