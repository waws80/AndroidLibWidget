package pw.androidthanatos.lib.widget.labelview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.transition.Slide
import com.jsb.android.widget.R

import java.util.ArrayList

/**
 * @desc: 标签布局
 * @className: LabelView.java
 * @author: thanatos
 * @createTime: 18-12-5 下午4:55
 */
class LabelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) :
    ViewGroup(context, attrs, defStyleAttr) {

    private var mGravity = Gravity.LEFT

    private val lineViews = ArrayList<View>() //存放每行的view

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelView)

        if (typedArray.hasValue(R.styleable.LabelView_gravity)) {
            val gravity = typedArray.getInt(R.styleable.LabelView_gravity, 0)
            mGravity = when (gravity) {
                0 -> Gravity.LEFT
                1 -> Gravity.RIGHT
                2 -> Gravity.CENTER
                else -> Gravity.LEFT
            }
        }
        typedArray.recycle()
    }

    fun setGravity(@Slide.GravityFlag gravity: Int) {
        mGravity = gravity
    }

    fun setLabels(labels: List<View>) {
        removeAllViews()
        for (label in labels) {
            addView(label)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = width - paddingLeft - paddingRight     //获取viewGroup宽度
        var lineHeight = 0
        var lineWidth = 0

        /**
         * 设置子View的位置
         */
        var top = paddingTop
        lineViews.clear()
        /**
         * 获取每行的高度和view
         */
        val cCount = childCount
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LabelViewParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val childMaxWidth = lp.leftMargin + lp.rightMargin + childWidth
            val childMaxHeight = childHeight + lp.bottomMargin + lp.topMargin

            //如果需要换行
            if (lineWidth + childMaxWidth > width) {
                layoutChild(top, width, lineWidth)
                //重置行高行宽
                lineWidth = 0
                top += childMaxHeight
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin
            lineHeight = Math.max(lineHeight, childHeight + lp.bottomMargin + lp.topMargin)
            lineViews.add(child)
        }
        //处理最后一行
        layoutChild(top, width, lineWidth)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val modeWidth = View.MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = View.MeasureSpec.getMode(heightMeasureSpec)

        //wrap_content时需要计算高度
        var width = 0
        var height = 0

        //记录每一行的宽度和高度
        var lineHeight = 0
        var lineWidth = 0

        /**
         * 计算宽高, width, height
         */
        val cCount = childCount
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val lp = child.layoutParams as ViewGroup.MarginLayoutParams

            //子view的宽度
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin

            val childHeight = child.measuredHeight + lp.bottomMargin + lp.topMargin

            if (lineWidth + childWidth > sizeWidth - paddingLeft - paddingRight) {
                //对比得到最大的宽度
                width = Math.max(width, lineWidth)
                //重置行宽
                lineWidth = childWidth
                //记录行高
                height += lineHeight
                lineHeight = childHeight
            } else {    //未换行情况
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }

            //当到最后一个控件时，要高度要加上，宽度要是行宽
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width)
                height += lineHeight
            }
        }

        //当mode为wrap_content时，用计算的宽高;为match_parent时，用父类的宽高
        setMeasuredDimension(
            if (modeWidth == View.MeasureSpec.EXACTLY) sizeWidth else width + paddingRight + paddingLeft,
            if (modeHeight == View.MeasureSpec.EXACTLY) sizeHeight else height + paddingBottom + paddingTop
        )

    }

    /**
     * 测量子view
     * @param top 当前view 距布局顶部的距离
     * @param width 当前布局的可用宽度
     * @param lineWidth 当前行的宽度
     */
    private fun layoutChild(top: Int, width: Int, lineWidth: Int) {
        //布局子view
        var left = paddingLeft
        for (j in lineViews.indices) {
            if (lineViews[j].visibility == View.GONE) {
                continue
            }
            val params = lineViews[j].layoutParams as LabelViewParams
            val lc: Int
            val offset = width - lineWidth
            //设置控件位置
            lc = when (mGravity) {
                Gravity.CENTER -> offset / 2 + left + params.leftMargin / 2
                Gravity.RIGHT -> offset + left + params.leftMargin
                else -> left + params.leftMargin
            }
            val tc = top + params.topMargin
            val rc = lc + lineViews[j].measuredWidth + params.rightMargin
            val rb = tc + lineViews[j].measuredHeight + params.bottomMargin

            lineViews[j].layout(lc, tc, rc, rb)
            left += lineViews[j].measuredWidth + params.leftMargin + params.rightMargin
        }
        lineViews.clear()
    }


    /**
     * 当前viewGroup使用的LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return LabelViewParams(context, attrs)
    }

    class LabelViewParams : ViewGroup.MarginLayoutParams {

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs)

        constructor(width: Int, height: Int) : super(width, height)

        constructor(source: ViewGroup.MarginLayoutParams) : super(source)

        constructor(source: ViewGroup.LayoutParams) : super(source)
    }

    /**
     * dp转px
     * @param dp
     * @return px
     */
    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics).toInt()
    }
}
