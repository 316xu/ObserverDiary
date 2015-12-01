package hust.xujifa.observerdiary.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.util.AttributeSet
import android.view.View
import hust.xujifa.observerdiary.R

/**
 * Created by xujifa on 2015/11/13.
 */
class Circle: View {
    val paint:Paint=Paint()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val dw=100
        val dh=100

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

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }

    var color:Int=0
    constructor(context: Context,attr: AttributeSet?=null):super(context,attr){
        val ta=context.obtainStyledAttributes(attr, R.styleable.Circle)

        color=ta.getColor(R.styleable.Circle_circle_color,0)
        paint.color=color
        paint.style=Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle((right-left).toFloat()/2,(bottom-top).toFloat()/2,
                (right-left).toFloat()/2-(paddingLeft+paddingRight).toFloat()/2,paint)

    }
    fun setcolor(@ColorRes i:Int){
        paint.color=i
        invalidate()
    }
    fun getcolor():Int{
        return color
    }
}