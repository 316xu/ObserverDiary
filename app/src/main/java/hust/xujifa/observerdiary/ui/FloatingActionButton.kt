package hust.xujifa.observerdiary.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.widget.ImageView
import hust.xujifa.observerdiary.R

/**
 * Created by xujifa on 2015/9/29.
 */
class FloatingActionButton  : ImageView {
    internal val scale = resources.displayMetrics.density
    private var backgroundTint: Int
    private var elev: Float
    private val icon: Int
    constructor(context:Context):this(context,null as AttributeSet){

    }
    constructor(context: Context,attributeSet: AttributeSet) : super(context,attributeSet) {
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton)
        backgroundTint = array.getColor(R.styleable.FloatingActionButton_backgroundColor, resources.getColor(R.color.green))
        elev = array.getDimension(R.styleable.FloatingActionButton_fabelevation, 4f)
        icon = array.getResourceId(R.styleable.FloatingActionButton_fab_icon, R.mipmap.ic_launcher)
        setIcon(icon)
        if (Build.VERSION.SDK_INT > 20)
            elevation = elevation
        array.recycle()
        setBg(backgroundTint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setPadding((16 * scale).toInt(), (16 * scale).toInt(), (16 * scale).toInt(), (16 * scale).toInt())
        setMeasuredDimension((56 * scale).toInt(), (56 * scale).toInt())
    }

    private fun setBg(@ColorInt color: Int) {
        val shape = OvalShape()
        val drawable = ShapeDrawable(shape)
        drawable.paint.color = color
        background = drawable
    }

    fun setIcon(@DrawableRes resId: Int) {
        val b = BitmapFactory.decodeResource(resources, resId)
        setImageBitmap(b)
    }
}
