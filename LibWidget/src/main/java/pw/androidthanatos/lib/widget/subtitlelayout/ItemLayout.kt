package pw.androidthanatos.lib.widget.subtitlelayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.transition.Slide
import com.jsb.android.widget.R
import pw.androidthanatos.lib.widget.toolbar.ToolBar
import java.lang.IllegalArgumentException

/**
 * 条目布局
 */
class ItemLayout(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

    companion object {

        /**
         * 图标默认位置
         */
        private const val ICON_DEFAULT_GRAVITY = Gravity.CENTER

        /**
         * 标题默认字体颜色
         */
        private val COLOR_DEFAULT_TITLE = Color.parseColor("#333333")

        /**
         * 副标题默认字体颜色
         */
        private val COLOR_DEFAULT_SUBTITLE = Color.parseColor("#888888")

        /**
         * 信息默认字体颜色
         */
        private val COLOR_DEFAULT_INFO = Color.parseColor("#666666")
    }

    /**
     * 左边图标
     */
    private val mLeftIcon by lazy { ImageView(context) }

    /**
     * 标题
     */
    private val mTitleView by lazy { TextView(context) }

    /**
     * 副标题
     */
    private val mSubTitleView by lazy { TextView(context) }

    /**
     * 右边图标
     */
    private val mRightIcon by lazy { ImageView(context) }

    /**
     * 右边信息区域（右边图标的左边）
     */
    private val mRightInfo by lazy { TextView(context) }

    //attrs=====================================================
    /**
     * 左边图标drawable
     */
    private var mLeftIconDrawable: Drawable? = null
    /**
     * 右边图标drawable
     */
    private var mRightIconDrawable: Drawable? = null

    /**
     * 左边图标位置
     * top 若title存在 则与title水平居中对齐   bottom 若subTitle存在则与subTitle水平居中对齐   center 控件水平居中
     */
    @Slide.GravityFlag
    private var mLeftIconGravity: Int = ICON_DEFAULT_GRAVITY

    /**
     * 左边图标距离父控件左边距离
     */
    private var mLeftIconMarginLeft: Int = 0

    /**
     * 右边图标距离父控件右边的距离
     */
    private var mRightIconMarginRight: Int = 0

    /**
     * 标题距离左边图标的距离，若图标不存在则为距离父控件左边的距离
     */
    private var mTitleMarginLeftIcon: Int = 0

    /**
     * 副标题距离左边图标的距离，若图标不存在则为距离父控件左边的距离
     */
    private var mSubTitleMarginLeftIcon: Int = 0

    /**
     * 信息距离右边图标的距离，若图标不存在则为距离父控件右边的距离
     */
    private var mInfoMarginRightIcon: Int = 0


    /**
     * 标题距离左边的距离，为距离父控件左边的距离
     */
    private var mTitleGoneMarginLeftIcon: Int = 0

    /**
     * 副标题距离左边的距离，为距离父控件左边的距离
     */
    private var mSubTitleGoneMarginLeftIcon: Int = 0

    /**
     * 信息距离右边的距离，为距离父控件右边的距离
     */
    private var mInfoGoneMarginRightIcon: Int = 0

    /**
     * 标题文本
     */
    private var mTitleText: String = ""

    /**
     * 标题文本颜色
     */
    private var mTitleTextColor: Int = COLOR_DEFAULT_TITLE

    /**
     * 标题文本是否加粗
     */
    private var mTitleTextBold: Boolean = false

    /**
     * 标题文本字体大小
     */
    private var mTitleTextSize: Int = 0


    /**
     * 副标题文本
     */
    private var mSubTitleText: String = ""

    /**
     * 副标题文本字体颜色
     */
    private var mSubTitleTextColor: Int = COLOR_DEFAULT_SUBTITLE

    /**
     * 副标题文本字体是否加粗
     */
    private var mSubTitleTextBold: Boolean = false

    /**
     * 副标题文本字体大小
     */
    private var mSubTitleTextSize: Int = 0


    /**
     * 信息文本
     */
    private var mInfoText: String = ""

    /**
     * 信息文本字体颜色
     */
    private var mInfoTextColor: Int = COLOR_DEFAULT_INFO

    /**
     * 信息文本字体是否加粗
     */
    private var mInfoTextBold: Boolean = false

    /**
     * 信息文办字体大小
     */
    private var mInfoTextSize: Int = 0

    private var mSubTitleMarginTitleBottom: Int = 0

    init {
        addViews()
        parseAttrs(attrs)
        initView()
    }

    /**
     * 布局添加view
     */
    private fun addViews(){
        addView(mLeftIcon)
        addView(mTitleView)
        addView(mSubTitleView)
        addView(mRightIcon)
        addView(mRightInfo)
    }

    /**
     * 解析属性
     */
    @SuppressLint("WrongConstant")
    private fun parseAttrs(attrs: AttributeSet?){
        if (attrs == null) return
        val array = context.obtainStyledAttributes(attrs, R.styleable.ItemLayout)

        if (array.hasValue(R.styleable.ItemLayout_leftIcon)){
            mLeftIconDrawable = array.getDrawable(R.styleable.ItemLayout_leftIcon)
        }
        if (array.hasValue(R.styleable.ItemLayout_rightIcon)){
            mRightIconDrawable = array.getDrawable(R.styleable.ItemLayout_rightIcon)
        }

        mLeftIconGravity = getGravity(array.getInt(R.styleable.ItemLayout_leftIconGravity, ICON_DEFAULT_GRAVITY))

        mLeftIconMarginLeft = array.getDimension(R.styleable.ItemLayout_leftIconMarginLeft,
            dp2px(12f).toFloat()).toInt()
        mRightIconMarginRight = array.getDimension(R.styleable.ItemLayout_rightIconMarginRight,
            dp2px(12f).toFloat()).toInt()

        mTitleMarginLeftIcon = array.getDimension(R.styleable.ItemLayout_titleMarginLeftIcon,
            dp2px(10f).toFloat()).toInt()
        mSubTitleMarginLeftIcon = array.getDimension(R.styleable.ItemLayout_subTitleMarginLeftIcon,
            dp2px(10f).toFloat()).toInt()
        mInfoMarginRightIcon = array.getDimension(R.styleable.ItemLayout_infoMarginRightIcon,
            dp2px(10f).toFloat()).toInt()

        mTitleGoneMarginLeftIcon = array.getDimension(R.styleable.ItemLayout_titleGoneMarginLeftIcon,
            dp2px(12f).toFloat()).toInt()
        mSubTitleGoneMarginLeftIcon = array.getDimension(R.styleable.ItemLayout_subTitleGoneMarginLeftIcon,
            dp2px(12f).toFloat()).toInt()
        mInfoGoneMarginRightIcon = array.getDimension(R.styleable.ItemLayout_infoGoneMarginRightIcon,
            dp2px(12f).toFloat()).toInt()

        mTitleText = array.getString(R.styleable.ItemLayout_title)?:""
        mTitleTextColor = array.getColor(R.styleable.ItemLayout_title_color, COLOR_DEFAULT_TITLE)
        mTitleTextBold = array.getBoolean(R.styleable.ItemLayout_title_bold, false)
        mTitleTextSize = array.getDimension(R.styleable.ItemLayout_title_size,
            sp2px(16f).toFloat()).toInt()

        mSubTitleText = array.getString(R.styleable.ItemLayout_subTitle)?:""
        mSubTitleTextColor = array.getColor(R.styleable.ItemLayout_subTitle_color, COLOR_DEFAULT_SUBTITLE)
        mSubTitleTextBold = array.getBoolean(R.styleable.ItemLayout_subTitle_bold, false)
        mSubTitleTextSize = array.getDimension(R.styleable.ItemLayout_subTitle_size,
            sp2px(12f).toFloat()).toInt()

        mInfoText = array.getString(R.styleable.ItemLayout_info)?:""
        mInfoTextColor = array.getColor(R.styleable.ItemLayout_info_color, COLOR_DEFAULT_INFO)
        mInfoTextBold = array.getBoolean(R.styleable.ItemLayout_info_bold, false)
        mInfoTextSize = array.getDimension(R.styleable.ItemLayout_info_size,
            sp2px(14f).toFloat()).toInt()

        mSubTitleMarginTitleBottom = dp2px(8f)

        array.recycle()
    }

    /**
     * 初始化view
     */
    private fun initView(){
        initLeftIcon()
        initRightIcon()
        initTitleView()
        initSubTitleView()
        initInfoView()
    }

    private fun initLeftIcon() {
        mLeftIcon.setImageDrawable(mLeftIconDrawable)
    }

    private fun initRightIcon(){
        mRightIcon.setImageDrawable(mRightIconDrawable)
    }

    private fun initTitleView(){
        mTitleView.text = mTitleText
        mTitleView.textSize = px2sp(mTitleTextSize).toFloat()
        mTitleView.setTextColor(mTitleTextColor)
        mTitleView.paint.isFakeBoldText = mTitleTextBold
        mTitleView.maxLines = 1
        mTitleView.gravity = Gravity.CENTER
    }

    private fun initSubTitleView(){
        mSubTitleView.text = mSubTitleText
        mSubTitleView.textSize = px2sp(mSubTitleTextSize).toFloat()
        mSubTitleView.setTextColor(mSubTitleTextColor)
        mSubTitleView.paint.isFakeBoldText = mSubTitleTextBold
        mSubTitleView.maxLines = 1
        mSubTitleView.gravity = Gravity.CENTER
    }

    private fun initInfoView(){
        mRightInfo.text = mInfoText
        mRightInfo.textSize = px2sp(mInfoTextSize).toFloat()
        mRightInfo.setTextColor(mInfoTextColor)
        mRightInfo.paint.isFakeBoldText = mInfoTextBold
        mRightInfo.maxLines = 1
        mRightInfo.gravity = Gravity.CENTER
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutLeftIcon()
        layoutRightIcon()

        layoutTitleView()
        layoutSubTitleView()
        layoutInfo()

        layoutCustomView()
    }

    /**
     * 布局左边图标
     */
    private fun layoutLeftIcon() {
        if (mLeftIconDrawable == null) return
        val left = paddingLeft + mLeftIconMarginLeft
        val top = if (mLeftIconGravity == ICON_DEFAULT_GRAVITY){
            measuredHeight/2 - mLeftIcon.measuredHeight/2
        }else{
            measuredHeight/2 - mLeftIcon.measuredHeight/2
        }
        val right = left + mLeftIcon.measuredWidth
        val bottom = top + mLeftIcon.measuredHeight
        mLeftIcon.layout(left, top, right, bottom)
    }

    /**
     * 布局右边图标
     */
    private fun layoutRightIcon() {
        if (mRightIconDrawable == null) return
        val right = measuredWidth - paddingRight - mRightIconMarginRight
        val top = measuredHeight/2 - mRightIcon.measuredHeight/2
        val left = right - mRightIcon.measuredWidth
        val bottom = top + mRightIcon.measuredHeight
        mRightIcon.layout(left, top, right, bottom)
    }

    /**
     * 布局标题
     */
    private fun layoutTitleView(){
        if (mTitleText.isEmpty()) return
        val left = if (mLeftIconDrawable == null || mLeftIcon.measuredWidth <= 0){
            mTitleGoneMarginLeftIcon
        }else{
            mLeftIcon.right + mTitleMarginLeftIcon
        }
        val top = if (mSubTitleText.isEmpty()){
            measuredHeight/2 - mTitleView.measuredHeight/2
        }else{
            measuredHeight/2 - (mTitleView.measuredHeight + mSubTitleMarginTitleBottom + mSubTitleView.measuredHeight)/2
        }
        val right = left + mTitleView.measuredWidth
        val bottom = top + mTitleView.measuredHeight

        mTitleView.layout(left, top, right, bottom)
        if (mLeftIconGravity == Gravity.TOP){
            mLeftIcon.top = (bottom - mTitleView.measuredHeight/2 - mLeftIcon.measuredHeight/2)
        }
    }

    /**
     * 布局副标题
     */
    private fun layoutSubTitleView(){
        if (mSubTitleText.isEmpty()) return
        val left = if (mLeftIconDrawable == null || mLeftIcon.measuredWidth <= 0){
            mSubTitleGoneMarginLeftIcon
        }else{
            mLeftIcon.right + mSubTitleMarginLeftIcon
        }
        val top = if (mTitleText.isEmpty()){
            measuredHeight/2 - mSubTitleView.measuredHeight/2
        }else{
            mTitleView.bottom + mSubTitleMarginTitleBottom
        }
        val right = left + mSubTitleView.measuredWidth
        val bottom = top + mSubTitleView.measuredHeight

        mSubTitleView.layout(left, top, right, bottom)
        if (mLeftIconGravity == Gravity.BOTTOM){
            mLeftIcon.top = (bottom - mSubTitleView.measuredHeight/2 - mLeftIcon.measuredHeight/2)
            mLeftIcon.bottom = mLeftIcon.top + mLeftIcon.measuredHeight
        }
    }

    /**
     * 布局信息文本控件
     */
    private fun layoutInfo(){
        if (mInfoText.isEmpty()) return
        val right = if (mRightIconDrawable == null || mRightIcon.measuredWidth <= 0){
            measuredWidth - mInfoGoneMarginRightIcon
        }else{
            mRightIcon.left - mInfoMarginRightIcon
        }
        val left = right - mRightInfo.measuredWidth
        val top = measuredHeight/2 - mRightInfo.measuredHeight/2
        val bottom = top + mRightInfo.measuredHeight
        mRightInfo.layout(left, top, right, bottom)
    }

    private fun layoutCustomView(){
        val child = getChildAt(childCount - 1)
        val right = if (mRightIconDrawable == null || mRightIcon.measuredWidth <= 0){
            measuredWidth - mInfoGoneMarginRightIcon
        }else{
            mRightIcon.left - mInfoMarginRightIcon
        }
        val top = measuredHeight/2 - child.measuredHeight/2
        val left = right - child.measuredWidth
        val bottom = top + child.measuredHeight
        child.layout(left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (childCount > 6) throw IllegalArgumentException("只能添加一个子控件")
        (0 until childCount).forEach {
            measureChild(getChildAt(it), widthMeasureSpec, heightMeasureSpec)
        }

        val minHeight = Math.max((mTitleView.measuredHeight + mSubTitleView.measuredHeight
                + paddingTop + paddingBottom + mSubTitleMarginTitleBottom), dp2px(36f))

        val heightModel = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val height = if (heightModel == MeasureSpec.EXACTLY){
            Math.max(minHeight, heightSize)
        }else{
            Math.max(minHeight, 0)
        }

        setMeasuredDimension(widthSize, height)
    }

    override fun addView(child: View?) {
        if (childCount <= 5){
            super.addView(child)
        }
    }

    override fun addView(child: View?, params: LayoutParams?) {
        if (childCount <= 5){
            super.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (childCount <= 5){
            super.addView(child, index)
        }
    }

    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        if (childCount <= 5){
            super.addView(child, index, params)
        }
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (childCount <= 5){
            super.addView(child, width, height)
        }
    }

    @Deprecated("废弃不使用", ReplaceWith("addView"))
    override fun addViewInLayout(child: View?, index: Int, params: LayoutParams?): Boolean {
        return false
    }

    @Deprecated("废弃不使用", ReplaceWith("addView"))
    override fun addViewInLayout(
        child: View?,
        index: Int,
        params: LayoutParams?,
        preventRequestLayout: Boolean
    ): Boolean {
        return false
    }

    @Deprecated("废弃不使用")
    override fun removeAllViews() {}

    @Deprecated("废弃不使用")
    override fun removeAllViewsInLayout() {}


    /**
     * 当前viewGroup使用的LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ItemLayoutParams(context, attrs)
    }

    private inner class ItemLayoutParams: ViewGroup.MarginLayoutParams{

        constructor(c: Context, attrs: AttributeSet): super(c, attrs)
        constructor(w: Int, h: Int): super(w, h)
    }

    /**
     * 转换属性中的gravity
     */
    @Slide.GravityFlag
    private fun getGravity(value: Int): Int{
        return when (value) {
            0 -> Gravity.CENTER
            1 -> Gravity.TOP
            2 -> Gravity.BOTTOM
            else -> Gravity.CENTER
        }
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


    private fun <T: View> T.click(next: (T) -> Unit){
        this.setOnClickListener(object : ToolBar.NoDoubleOnClickListener(){
            override fun onNoDoubleClick(view: View) {
                next.invoke(this@click)
            }
        })
    }

    /**
     * 条目点击事件
     */
    fun itemClick(next: (ItemLayout) -> Unit): ItemLayout{
        click(next)
        return this
    }

    /**
     * 右边图标点击事件
     */
    fun rightIconClick(next: (ImageView) -> Unit): ItemLayout{
        mRightIcon.click(next)
        return this
    }

    /**
     * 左边图标
     */
    fun leftIconView(next: (ImageView) -> Unit): ItemLayout{
        next.invoke(mLeftIcon)
        return this
    }

    /**
     * 右边图标
     */
    fun rightIconView(next: (ImageView) -> Unit): ItemLayout{
        next.invoke(mRightIcon)
        return this
    }

    /**
     * 标题文本
     */
    fun titleView(next: (TextView) -> Unit): ItemLayout{
        next.invoke(mTitleView)
        return this
    }

    /**
     * 副标题文本
     */
    fun subTitleView(next: (TextView) -> Unit): ItemLayout{
        next.invoke(mSubTitleView)
        return this
    }

    /**
     * 信息文本
     */
    fun infoView(next: (TextView) -> Unit): ItemLayout{
        next.invoke(mRightInfo)
        return this
    }

    /**
     * 添加的控件
     */
    fun customView(next: (View) -> Unit): ItemLayout{
        next.invoke(getChildAt(childCount - 1))
        return this
    }
}