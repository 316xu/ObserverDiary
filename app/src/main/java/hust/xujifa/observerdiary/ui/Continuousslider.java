package hust.xujifa.observerdiary.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import hust.xujifa.observerdiary.R;

/**
 * Created by xujifa on 2015/9/27.
 */
public class Continuousslider extends ViewGroup {
    Line line;
    Circle circle;
    Handler h;
    boolean flag=false;

    public Continuousslider(Context context) {
        super(context);
        init();
    }
    public void setHandler(Handler h){
        flag=true;
        this.h=h;
    }
    public Continuousslider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    void init(){
        line=new Line(getContext());
        circle=new Circle(getContext());
        addView(line);
        addView(circle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desirew=500,desireh=100;
        int widthmode=MeasureSpec.getMode(widthMeasureSpec);
        int widthsize=MeasureSpec.getSize(widthMeasureSpec);
        int heightmode=MeasureSpec.getMode(heightMeasureSpec);
        int heightsize=MeasureSpec.getSize(heightMeasureSpec);
        int width=0,height=0;
        if(widthmode==MeasureSpec.EXACTLY) width=widthsize;
        else if(widthmode==MeasureSpec.AT_MOST) width=Math.min(desirew,widthsize);
        else if(widthmode==MeasureSpec.UNSPECIFIED){width=desirew;};
        if(heightmode==MeasureSpec.EXACTLY) height=heightsize;
        else if(heightmode==MeasureSpec.AT_MOST) height=Math.min(desireh,heightsize);
        else if(heightmode==MeasureSpec.UNSPECIFIED)height=desireh;
        setMeasuredDimension(width, height);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());
        float ww = (float) w - xpad;
        float hh = (float) h - ypad;
        circle.layout(0, 0, (int) ww, (int) hh);
        line.layout(0, 0, (int) ww, (int) hh);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                circle.scalel();
                circle.startAnimation(200);
                moveto(event.getX());
                return true;
            case MotionEvent.ACTION_UP:
                circle.scales();
                circle.startAnimation(200);
                moveto(event.getX());
                return true;
            case MotionEvent.ACTION_MOVE:
                moveto(event.getX());
                return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    void moveto(float x){
        line.moveto(x);
        circle.moveto(x);
    }
    class Line extends View {
        Paint paint1;
        Paint paint2;
        float x;
        public Line(Context context) {
            super(context);
            paint1=new Paint();
            paint2=new Paint();
            paint1.setColor(getResources().getColor(R.color.gray));
            paint1.setAlpha(80);
            paint2.setAlpha(80);
            paint2.setColor(getResources().getColor(R.color.blue));
            paint1.setStyle(Paint.Style.FILL);
            paint2.setStyle(Paint.Style.FILL);
            x=getLeft()+55;
        }
        public void moveto(float x){
            this.x=x;
            invalidate();
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(x<=getLeft()+55)x=getLeft()+55;
            if(x>getRight()-55)x=getRight()-55;
            canvas.drawRect(getLeft() + 55, (getTop()+getBottom())/2-3, x,(getTop()+getBottom())/2+3, paint2);
            canvas.drawRect(x, (getTop()+getBottom())/2-3, getRight() - 55,(getTop()+getBottom())/2+3, paint1);
            if(flag) h.sendEmptyMessage((int)((x-55)/(getRight()-getLeft()-110)*1000));
        }
    }
    class Circle extends View implements ValueAnimator.AnimatorUpdateListener{
        Paint paint;
        Paint bg;
        float x;
        int radius;
        int radius2;
        boolean temp;
        ValueAnimator animator;
        public void moveto(float x){
            this.x=x;
            invalidate();
        }

        public Circle(Context context) {
            super(context);
            paint=new Paint();
            paint.setColor(getResources().getColor(R.color.blue));
            bg=new Paint();

            bg.setStyle(Paint.Style.FILL);
            bg.setColor(getResources().getColor(R.color.blue));
            bg.setAlpha(26);

            paint.setStyle(Paint.Style.FILL);
            x=getLeft()+20;
            radius=15;
            radius2=15;
            animator=new ValueAnimator();
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(x>getRight()-30)x=getRight()-30;
            else if(x<=getLeft()+30) {
                x = getLeft() + 30;
                paint.setColor(getResources().getColor(R.color.gray));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                bg.setColor(getResources().getColor(R.color.gray));
                bg.setAlpha(80);
            }else{
                paint.setColor(getResources().getColor(R.color.blue));
                paint.setStyle(Paint.Style.FILL);
                bg.setColor(getResources().getColor(R.color.blue));
                bg.setAlpha(80);

            }
            canvas.drawCircle(x, (getTop()+getBottom())/2 , radius2, bg);

            canvas.drawCircle(x, (getTop()+getBottom())/2 , radius, paint);


        }

        public void scalel() {
            temp=true;
        }
        public void scales() {
            temp=false;
        }
        public void startAnimation(int duration){
            ValueAnimator v=ValueAnimator.ofInt(0,100);
            v.setStartDelay(0);
            v.setDuration(duration);
            v.addUpdateListener(this);
            v.start();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int test = (int) animation.getAnimatedValue();
            if(temp){
                radius=15+(15*test)/100;
                radius2=15+(35*test)/100;
            }else{
                radius2=50-(35*test)/100;
                radius=30-(15*test)/100;
            }
            invalidate();
        }
    }
}
