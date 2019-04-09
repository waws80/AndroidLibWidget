package pw.androidthanatos.lib.widget.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import com.jsb.android.widget.R
import java.util.*

/**
 *  @desc:
 *  @className: ToolBar.kt
 *  @author: thanatos
 *  @createTime: 2019/2/12
 *  @updateTime: 2019/2/12 14:41
 */
class ToolBar: ViewGroup {

    companion object {

        //标题居中
        const val CENTER = 0

        //标题居左
        const val LEFT = 1
    }

    /**
     * 标题位置类型
     */
    @IntDef(CENTER, LEFT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TitleType

    //左边菜单按钮
    private val mLeftView by lazy { ImageView(context) }

    //右边菜单按钮
    private val mRightView by lazy { ImageView(context) }

    //标题
    private val mTitleView by lazy { TextView(context) }

    //左边行为扩展布局
    private val mLeftAction by lazy { RelativeLayout(context) }

    //右边行为扩展布局
    private val mRightAction by lazy { RelativeLayout(context) }

    //下划线控件
    private val mDriverView by lazy { ImageView(context) }


    //左边菜单图标
    private var mLeftDrawable: Drawable? = null
    //右边菜单图标
    private var mRightDrawable: Drawable? = null

    //标题文本
    private var mTitle: String = ""
    //标题颜色
    private var mTitleColor: Int = Color.parseColor("#333333")
    //标题是否加粗
    private var mTitleBold: Boolean = true
    //标题位置
    @TitleType
    private var mTitleLoc = CENTER

    //是否沉浸式状态栏
    private var mImmerseStatusBar: Boolean = false

    //底部下划线高度
    private var mDriverHeight: Int = 2
    //底部下划线颜色
    private var mDriverColor: Int = Color.parseColor("#F5F5F5")


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs){
        parseAttrs(attrs)
        addView(mLeftView)
        addView(mRightView)
        addView(mTitleView)
        addView(mLeftAction)
        addView(mRightAction)
        addView(mDriverView)
        initLeftView()
        initRightView()
        initTitleView()
        initDriverVIew()
    }

    /**
     * 解析属性
     */
    private fun parseAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val array = context.obtainStyledAttributes(attrs, R.styleable.ToolBar)

        if (array.hasValue(R.styleable.ToolBar_left_drawable)){
            mLeftDrawable = array.getDrawable(R.styleable.ToolBar_left_drawable)
        }

        if (array.hasValue(R.styleable.ToolBar_right_drawable)){
            mRightDrawable = array.getDrawable(R.styleable.ToolBar_right_drawable)
        }

        mImmerseStatusBar = array.getBoolean(R.styleable.ToolBar_statusBar_immerse, false)

        mDriverHeight = array.getDimension(R.styleable.ToolBar_driver_height, mDriverHeight.toFloat()).toInt()

        mDriverColor = array.getColor(R.styleable.ToolBar_driver_color, mDriverColor)

        mTitle = array.getString(R.styleable.ToolBar_text)?:""
        mTitleColor = array.getColor(R.styleable.ToolBar_text_color, mTitleColor)
        mTitleBold = array.getBoolean(R.styleable.ToolBar_text_bold, mTitleBold)
        mTitleLoc = array.getInt(R.styleable.ToolBar_text_loc, mTitleLoc)

        array.recycle()
    }

    private fun initLeftView() {
        mLeftView.setImageDrawable(mLeftDrawable)

    }

    private fun initRightView(){
        mRightView.setImageDrawable(mRightDrawable)
    }

    private fun initTitleView(){
        mTitleView.text = mTitle
        mTitleView.setTextColor(mTitleColor)
        mTitleView.paint.isFakeBoldText = mTitleBold
        mTitleView.textSize = 18f
        mTitleView.maxWidth = dp2px(160f)
        mTitleView.minWidth = dp2px(120f)
        mTitleView.maxLines = 1
        mTitleView.gravity = Gravity.CENTER
        mTitleView.ellipsize = TextUtils.TruncateAt.END
    }

    private fun initDriverVIew(){
        mDriverView.setBackgroundColor(mDriverColor)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val h = measuredHeight
        val w = measuredWidth
        layoutLeftView(h)
        layoutLeftAction(h)
        layoutRightView(w, h)
        layoutRightAction(w, h)
        layoutTitleView(w, h)
        layoutDriverView(w, h)
    }

    private fun layoutLeftView(parentH: Int) {
        val h = mLeftView.measuredHeight
        val w = mLeftView.measuredWidth
        val left = dp2px(12f)
        val top = (parentH/2 - h/2)
        val right = left + w
        val bottom = parentH - top
        mLeftView.layout(left, top, right, bottom)
    }

    private fun layoutLeftAction(parentH: Int){
        val h = mLeftAction.measuredHeight
        val w = mLeftAction.measuredWidth
        val left = if (mLeftView.measuredWidth <= 0) dp2px(12f) else mLeftView.right + dp2px(10f)
        val top = (parentH/2 - h/2)
        val right = left + w
        val bottom = parentH - top
        mLeftAction.layout(left, top, right, bottom)
    }

    private fun layoutRightView(parentW: Int, parentH: Int){
        val h = mRightView.measuredHeight
        val w = mRightView.measuredWidth
        val left = parentW - dp2px(12f) - w
        val top = (parentH/2 - h/2)
        val right = left + w
        val bottom = parentH - top
        mRightView.layout(left, top, right, bottom)
    }

    private fun layoutRightAction(parentW: Int, parentH: Int){
        val h = mRightAction.measuredHeight
        val w = mRightAction.measuredWidth
        val left = (if (mRightView.measuredWidth <= 0) parentW - dp2px(12f) - w else mRightView.left - dp2px(10f) - w )
        val top = (parentH/2 - h/2)
        val right = left + w
        val bottom = parentH - top
        mRightAction.layout(left, top, right, bottom)
    }

    private fun layoutTitleView(parentW: Int, parentH: Int){
        val h = mTitleView.measuredHeight
        val w = mTitleView.measuredWidth
        val left = if (mTitleLoc == CENTER) { parentW/2 - w/2 } else {
            if (mLeftAction.measuredWidth <=0){
                mLeftView.right + dp2px(10f)
            }else{
                mLeftAction.right + dp2px(10f)
            }
        }

        val top = (parentH/2 - h/2)
        val right = left + w
        val bottom = parentH - top
        mTitleView.layout(left, top, right, bottom)
    }

    private fun layoutDriverView(parentW: Int, parentH: Int){
        val left = 0
        val top = parentH - mDriverHeight
        mDriverView.layout(left, top, parentW, parentH)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = View.MeasureSpec.getMode(heightMeasureSpec)
        val height = if (modeHeight == View.MeasureSpec.EXACTLY) sizeHeight else dp2px(56f)
        setMeasuredDimension(sizeWidth, height)
        (0 until childCount).forEach {
            measureChild(getChildAt(it), widthMeasureSpec, heightMeasureSpec)
        }
    }


    /**
     * 当前viewGroup使用的LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ToolBarViewParams(context, attrs)
    }

    private inner class ToolBarViewParams: ViewGroup.MarginLayoutParams{

        constructor(c: Context, attrs: AttributeSet): super(c, attrs)
        constructor(w: Int, h: Int): super(w, h)
    }

    private fun setImmerseLayout(window: Window, @ColorInt color: Int = Color.TRANSPARENT, light: Boolean = false) {// view为标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && light){
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }else{
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT

            val vg = window.findViewById<ViewGroup>(android.R.id.content)
            val statusBarView = View(window.context)
            val maskView = View(window.context)
            if (color == Color.TRANSPARENT){
                statusBarView.setBackgroundColor(Color.argb(60,0,0,0))
            }else{
                statusBarView.setBackgroundColor(color)
                maskView.setBackgroundColor(Color.argb(60,0,0,0))
            }
            vg.addView(statusBarView, ViewGroup.LayoutParams(-1, getStatusBarHeight()))
            vg.addView(maskView, ViewGroup.LayoutParams(-1, getStatusBarHeight()))
        }


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isClickable = true
        isFocusable = true
        setImmerseStatusBar()
    }


    /**
     * 获取状态栏高度
     * @return 状态栏高度
     */
    private fun getStatusBarHeight(): Int {
        // 获得状态栏高度
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * dp2px
     */
    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
    }

    /**
     * sp转px
     * @param sp
     * @return
     */
    private fun sp2px(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics).toInt()
    }


    /**
     * px转dp
     */
    private fun px2dp(px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * px转sp
     */
    private fun px2sp(px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    abstract class NoDoubleOnClickListener : View.OnClickListener{

        override fun onClick(v: View) {
            val currentTime = Calendar.getInstance().timeInMillis
            if (currentTime - lastClickTime > delay) {
                lastClickTime = currentTime
                onNoDoubleClick(v)
            }
        }

        abstract fun onNoDoubleClick(view: View)

        companion object {

            private var lastClickTime = 0L

            private const val delay = 500L
        }
    }

    private fun View.click(next: (View) -> Unit){
        setOnClickListener(object : NoDoubleOnClickListener(){
            override fun onNoDoubleClick(view: View) {
                next.invoke(this@click)
            }
        })
    }


    //attrs========================================================

    fun setImmerseStatusBar(){
        if (context is Activity && mImmerseStatusBar){
            setImmerseLayout((context as Activity).window)
            (layoutParams as ViewGroup.MarginLayoutParams).topMargin = getStatusBarHeight()
        }
    }

    fun leftMenuClick(next: (View)->Unit): ToolBar{
        mLeftView.click { next.invoke(it) }
        return this
    }

    fun rightMenuClick(next: (View)->Unit): ToolBar{
        mRightView.click { next.invoke(it) }
        return this
    }

    fun leftMenu(next: (ImageView) -> Unit): ToolBar{
        next.invoke(mLeftView)
        return this
    }

    fun rightMenu(next: (ImageView) -> Unit): ToolBar{
        next.invoke(mRightView)
        return this
    }

    fun titleView(next: (TextView) -> Unit): ToolBar{
        next.invoke(mTitleView)
        return this
    }

    fun leftAction(next: (RelativeLayout) -> Unit): ToolBar{
        next.invoke(mLeftAction)
        return this
    }

    fun rightAction(next: (RelativeLayout) -> Unit): ToolBar{
        next.invoke(mRightAction)
        return this
    }
}