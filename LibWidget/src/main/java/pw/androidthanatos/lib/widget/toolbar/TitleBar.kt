package pw.androidthanatos.lib.widget.toolbar

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.jsb.android.widget.R


/**
 *  @desc: 标题栏  建议使用[ToolBar]
 *  @className: TitleBar.kt
 *  @author: thanatos
 *  @createTime: 18-12-11 下午6:10
 */
@Deprecated("建议使用ToolBar")
class TitleBar : AppBarLayout {

    /**
     * 是否只使用AppBar　不添加子布局
     */
    private var mOnlyParent = false

    /**
     * 子布局，真正的标题栏
     */
    private var mChild: RelativeLayout? = null

    /**
     * 标题栏左边控件
     */
    private var mLeftMenuView: TextView? = null

    /**
     * 标题栏标题布局
     */
    private var mTitleView: TextView? = null

    /**
     * 标题栏右边菜单按钮
     */
    private var mRightMenuView: TextView? = null

    /**
     * 标题栏右边扩展布局
     */
    private var mRightActionView: LinearLayout? = null

    /**
     * 标题栏左边扩展布局
     */
    private var mLeftActionView: LinearLayout? = null

    //attr
    private var mLeftMenuDrawable: Drawable? = null

    private var mRightMenuDrawable: Drawable? = null

    private var mTitleText: CharSequence? = null

    private var mTitleTextColor: Int = Color.parseColor("#3C3C3C")

    private var mTitleTextBold: Boolean = true

    private var mImmerseStatusBar: Boolean = false

    private var mDriverHeight = 2

    private var mDriverPaint = Paint(Paint.ANTI_ALIAS_FLAG)



    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet? = null) : super(context, attrs){
        initEvaluation(attrs)
        initAttrs(attrs)
        setStatusBar()
    }


    /**
     * 初始化z轴高度
     */
    private fun initEvaluation(attrs: AttributeSet?) {
        if (attrs == null) return
        val sysType = context.obtainStyledAttributes(attrs, com.google.android.material.R.styleable.AppBarLayout)
        //设置z轴高度为０
        if (!sysType.hasValue(com.google.android.material.R.styleable.AppBarLayout_elevation)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val dur = resources.getInteger(com.google.android.material.R.integer.app_bar_elevation_anim_duration)
                val sla = StateListAnimator()
                sla.addState(intArrayOf(16842766, com.google.android.material.R.attr.state_liftable, -com.google.android.material.R.attr.state_lifted),
                        ObjectAnimator.ofFloat(this, "elevation", *floatArrayOf(0.0f)).setDuration(dur.toLong()))
                sla.addState(intArrayOf(16842766), ObjectAnimator.ofFloat(this, "elevation", *floatArrayOf(0f)).setDuration(dur.toLong()))
                sla.addState(IntArray(0), ObjectAnimator.ofFloat(this, "elevation", *floatArrayOf(0.0f)).setDuration(0L))
                stateListAnimator = sla
            }
        }
        sysType.recycle()
    }

    /**
     * 初始化属性
     */
    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val type = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        if (type.hasValue(R.styleable.TitleBar_title_only_appbar)){
            mOnlyParent = type.getBoolean(R.styleable.TitleBar_title_only_appbar, false)
        }
        if (type.hasValue(R.styleable.TitleBar_title_left_drawable)){
            mLeftMenuDrawable = type.getDrawable(R.styleable.TitleBar_title_left_drawable)
        }
        if (type.hasValue(R.styleable.TitleBar_title_right_drawable)){
            mRightMenuDrawable = type.getDrawable(R.styleable.TitleBar_title_right_drawable)
        }
        if (type.hasValue(R.styleable.TitleBar_title_text)){
            mTitleText = type.getText(R.styleable.TitleBar_title_text)
        }
        if (type.hasValue(R.styleable.TitleBar_title_text_color)){
            mTitleTextColor = type.getColor(R.styleable.TitleBar_title_text_color, mTitleTextColor)
        }
        if (type.hasValue(R.styleable.TitleBar_title_text_bold)){
            mTitleTextBold = type.getBoolean(R.styleable.TitleBar_title_text_bold, false)
        }
        if (type.hasValue(R.styleable.TitleBar_title_statusBar_immerse)){
            mImmerseStatusBar = type.getBoolean(R.styleable.TitleBar_title_statusBar_immerse, false)
        }
        if (type.hasValue(R.styleable.TitleBar_title_driver_height)){
            mDriverHeight = type.getDimension(R.styleable.TitleBar_title_driver_height, mDriverHeight.toFloat()).toInt()
        }
        if (type.hasValue(R.styleable.TitleBar_title_driver_color)){
            mDriverPaint.color = type.getColor(R.styleable.TitleBar_title_driver_color, Color.parseColor("#ECECEC"))
        }else{
            mDriverPaint.color = Color.parseColor("#ECECEC")
        }
        type.recycle()

        if (!mOnlyParent) {
            initChild()
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rect = Rect()
        rect.left = 0
        rect.top = measuredHeight - mDriverHeight
        rect.right = measuredWidth
        rect.bottom = measuredHeight
        canvas.drawRect(rect, mDriverPaint)
    }

    /**
     * 初始化子view
     */
    private fun initChild(){
        val rect = Rect()
        if (context is Activity){
            (context as Activity).window.windowManager.defaultDisplay.getRectSize(rect)
        }
        val childParams = AppBarLayout.LayoutParams(-1, -1)
        mChild = RelativeLayout(context)
        mChild?.minimumHeight = dp2px(48f)
        addView(mChild, childParams)

        initChildLayout()
    }

    /**
     * 初始化子view内部布局
     */
    private fun initChildLayout(){
        initLeftView()
        initTitleView()
        initRightView()
        initLeftActionView()
        initRightActionView()
    }

    /**
     * 初始化左边控件
     */
    private fun initLeftView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.ALIGN_LEFT)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        params.leftMargin = dp2px(4f)
        mLeftMenuView = TextView(mChild?.context)
        mLeftMenuView?.id = R.id.titleBar_left_view
        mLeftMenuView?.layoutParams = params
        mLeftMenuView?.compoundDrawablePadding = dp2px(8f)
        mLeftMenuView?.setPadding(dp2px(10f),0,dp2px(10f),0)
        mChild?.addView(mLeftMenuView)
        mLeftMenuDrawable?.apply {
            setBounds(0,0, intrinsicWidth, intrinsicHeight)
            mLeftMenuView?.setCompoundDrawables(this, null, null, null)
        }
    }

    /**
     * 初始化标题布局
     */
    private fun initTitleView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        mTitleView = TextView(mChild?.context)
        mTitleView?.layoutParams = params
        mTitleView?.maxWidth = dp2px(180f)
        mTitleView?.setTextColor(mTitleTextColor)
        mTitleView?.textSize = 18f
        mTitleView?.maxLines = 1
        mTitleView?.ellipsize = TextUtils.TruncateAt.END
        mChild?.addView(mTitleView)

        mTitleView?.apply {
            text = mTitleText
            setTextColor(mTitleTextColor)
            paint.isFakeBoldText = mTitleTextBold
        }
    }

    /**
     * 初始化右边控件
     */
    private fun initRightView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        params.rightMargin = dp2px(4f)
        mRightMenuView = TextView(mChild?.context)
        mRightMenuView?.id = R.id.titleBar_right_view
        mRightMenuView?.layoutParams = params
        mRightMenuView?.compoundDrawablePadding = dp2px(8f)
        mRightMenuView?.setPadding(dp2px(10f),0,dp2px(10f),0)
        mChild?.addView(mRightMenuView)

        mRightMenuDrawable?.apply {
            setBounds(0,0, intrinsicWidth, intrinsicHeight)
            mRightMenuView?.setCompoundDrawables(null, null, this, null)
        }
    }

    /**
     * 初始化左边扩展布局
     */
    private fun initLeftActionView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        params.addRule(RelativeLayout.RIGHT_OF, mLeftMenuView?.id!!)
        mLeftActionView = LinearLayout(mChild?.context)
        mLeftActionView?.orientation = LinearLayout.HORIZONTAL
        mLeftActionView?.layoutParams = params
        mChild?.addView(mLeftActionView)
    }

    /**
     * 初始化右边扩展布局
     */
    private fun initRightActionView() {
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        params.addRule(RelativeLayout.LEFT_OF, mRightMenuView?.id!!)
        mRightActionView = LinearLayout(mChild?.context)
        mRightActionView?.orientation = LinearLayout.HORIZONTAL
        mRightActionView?.layoutParams = params
        mChild?.addView(mRightActionView)
    }

    /**
     * 设置状态栏是否为沉浸式
     */
    private fun setStatusBar(){
        if (mImmerseStatusBar) {
            mChild?.setPadding(0,getStatusBarHeight(),0,0)
            if ((context is Activity)) {
                setImmerseLayout((context as Activity).window)
            }

        }
    }

    private fun setImmerseLayout(window: Window, @ColorInt color: Int = Color.TRANSPARENT, light: Boolean = true) {// view为标题栏
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
        }

        val vg = window.findViewById<ViewGroup>(android.R.id.content)
        val statusBarView = View(window.context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (color == Color.TRANSPARENT){
                statusBarView.setBackgroundColor(Color.argb(112,0,0,0))
            }else{
                statusBarView.setBackgroundColor(color)
            }
        }else{
            statusBarView.setBackgroundColor(color)
        }

        vg.addView(statusBarView, ViewGroup.LayoutParams(-1, getStatusBarHeight()))
    }


    /**
     * dp2px
     */
    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
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
     * 左边按钮设置
     */
    fun leftMenuView(action: (TextView)->Unit): TitleBar {
        action.invoke(mLeftMenuView!!)
        return this
    }

    /**
     * 右边按钮设置
     */
    fun rightMenuView(action: (TextView)->Unit): TitleBar {
        action.invoke(mRightMenuView!!)
        return this
    }

    /**
     * 标题栏设置
     */
    fun titleView(action: (TextView)->Unit): TitleBar {
        action.invoke(mTitleView!!)
        return this
    }

    /**
     * 左边扩展布局设置
     */
    fun leftActionView(action: (LinearLayout)->Unit): TitleBar {
        action.invoke(mLeftActionView!!)
        return this
    }

    /**
     * 右边扩展布局设置
     */
    fun rightActionView(action: (LinearLayout)->Unit): TitleBar {
        action.invoke(mRightActionView!!)
        return this
    }

    /**
     * 设置标题栏文本
     */
    fun setTitle(title: CharSequence): TitleBar {
        mTitleView?.text = title
        return this
    }

    /**
     * 设置标题栏文本颜色
     */
    fun setTitleColor(@ColorInt color: Int): TitleBar {
        mTitleView?.setTextColor(color)
        return this
    }

    /**
     * 设置标题栏是否文字加粗
     */
    fun setTitleBold(bold: Boolean): TitleBar {
        mTitleView?.paint?.isFakeBoldText = bold
        return this
    }

    /**
     * 设置左边菜单栏图标
     */
    fun setLeftMenuDrawable(@DrawableRes res: Int): TitleBar {
        mLeftMenuView?.apply {
            val drawable = resources.getDrawable(res)
            drawable.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            setCompoundDrawables(drawable, null, null, null)
        }
        return this
    }

    /**
     * 设置右边菜单栏图标
     */
    fun setRightMenuDrawable(@DrawableRes res: Int): TitleBar {
        mRightMenuView?.apply {
            val drawable = resources.getDrawable(res)
            drawable.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            setCompoundDrawables(null, null, drawable, null)
        }
        return this
    }

    /**
     * 设置是否为沉浸式状态栏
     */
    @JvmOverloads
    fun setStatusBarImmerse(immerse: Boolean, color: Int = Color.TRANSPARENT): TitleBar {
        if (immerse){
            mChild?.setPadding(0,getStatusBarHeight(),0,0)
            if (context is Activity){
                setImmerseLayout((context as Activity).window, color)
            }
        }

        return this
    }

    /**
     * 左边菜单栏点击事件
     */
    fun leftMenuClick(click:(TextView)->Unit): TitleBar {
        mLeftMenuView?.setOnClickListener {
            click.invoke(mLeftMenuView!!)
        }
        return this
    }

    /**
     * 右边菜单栏点击事件
     */
    fun rightMenuClick(click:(TextView)->Unit): TitleBar {
        mRightMenuView?.setOnClickListener {
            click.invoke(mRightMenuView!!)
        }
        return this
    }

    /**
     * 标题栏点击事件
     */
    fun titleViewClick(click:(TextView)->Unit): TitleBar {
        mTitleView?.setOnClickListener { click.invoke(mTitleView!!) }
        return this
    }

}