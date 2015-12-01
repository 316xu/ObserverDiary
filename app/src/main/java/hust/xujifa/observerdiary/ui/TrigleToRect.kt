package hust.xujifa.observerdiary.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by xujifa on 2015/11/18.
 */
class TrigleToRect: View,ValueAnimator.AnimatorUpdateListener {

    var paint= Paint()
    var flag=false
    var y1=0f
    var y2=0f
    constructor(context: Context,attr:AttributeSet?=null):super(context,attr){
        paint.color= Color.RED
        paint.style= Paint.Style.FILL_AND_STROKE
        paint.setStrokeWidth(4f)
        paint.setAntiAlias(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val dw=160
        val dh=160

        val wm= View.MeasureSpec.getMode(widthMeasureSpec)
        val hm= View.MeasureSpec.getMode(heightMeasureSpec)
        val ws= View.MeasureSpec.getSize(widthMeasureSpec)
        val hs= View.MeasureSpec.getSize(widthMeasureSpec)
        var width:Int
        var height:Int


        when(wm){
            View.MeasureSpec.EXACTLY->width=ws
            View.MeasureSpec.AT_MOST->width=Math.min(dw,ws)
            else->width=dw
        }
        when(hm){
            View.MeasureSpec.EXACTLY->height=hs
            View.MeasureSpec.AT_MOST->height=Math.min(dh,hs)
            else->height=dh
        }
        setMeasuredDimension(width+paddingLeft+paddingRight,height+paddingTop+paddingBottom)
        y1=(bottom-top).toFloat()/2
        y2=(bottom-top).toFloat()/2

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

            canvas.drawLine(0f,0f,0f,(bottom-top).toFloat(),paint)
            canvas.drawLine(0f,0f,(right-left).toFloat(),y1,paint)
            canvas.drawLine(0f,(bottom-top).toFloat(),(right-left).toFloat(),y2,paint)
            canvas.drawLine((right-left).toFloat(),y1.toFloat(),(right-left).toFloat(),y2,paint)
    }
    fun change(){
        val v=ValueAnimator.ofInt(0,100)
        v.setDuration(300)

        v.addUpdateListener(this)
        v.start()

        flag=!flag;

    }
    override fun onAnimationUpdate(animation: ValueAnimator) {
        val i=animation.animatedValue as Int
        if(flag){
            y1=(bottom-top).toFloat()/2+(top-bottom).toFloat()/2*i/100
            y2=(bottom-top).toFloat()/2-(top-bottom).toFloat()/2*i/100
        }else{
            y1=-(top-bottom).toFloat()/2*i/100
            y2=bottom-top+(top-bottom).toFloat()/2*i/100

        }
        invalidate()
    }

}