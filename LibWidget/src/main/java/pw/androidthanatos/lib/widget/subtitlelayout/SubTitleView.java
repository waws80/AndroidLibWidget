package pw.androidthanatos.lib.widget.subtitlelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import com.jsb.android.widget.R;

/**
 * @className: SubTitleView.java
 * @author: thanatos
 * @des 二级标题栏 view
 */
public class SubTitleView extends RelativeLayout {

    //文字粗体
    public static final int BOLD = 0;

    //文字正常字体
    public static final int NORMAL = 1;

    @IntDef({BOLD, NORMAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextStyle{}

    /**
     * 左边布局
     */
    private LinearLayout llLeft;

    /**
     * 右边布局
     */
    private LinearLayout llRight;

    //分割线
    private View driver;


    //左边距
    private int leftMargin = 0;

    //右边距
    private int rightMargin = 0;

    //左右图标
    private Drawable leftIcon, rightIcon;

    //左右文本
    private CharSequence leftText, rightText;

    //左右文本大小
    private int leftTextSize, rightTextSize;

    //左右文本颜色
    private int leftTextColor, rightTextColor;

    //左右文本样式
    private int leftTextStyle, rightTextStyle;

    //分割线高度
    private int driverHeight;

    //分割线颜色
    private int driverColor;

    //分割线左右边距
    private int driverLeftMargin, driverRightMargin;

    //左右图标距离文字距离
    private int leftDrawablePadding, rightDrawablePadding;

    //右边提示文字颜色
    private int rightTextHintColor;

    //右边提示文字
    private CharSequence rightTextHint;


    public SubTitleView(Context context) {
        this(context, null);
    }

    public SubTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SubTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(attrs);
        initView();
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SubTitleView);

        leftMargin = (int) array.getDimension(R.styleable.SubTitleView_sub_leftMargin, (int) dp2px(16f));
        rightMargin = (int) array.getDimension(R.styleable.SubTitleView_sub_rightMargin, (int) dp2px(16f));

        leftIcon = array.getDrawable(R.styleable.SubTitleView_sub_leftIcon);
        rightIcon = array.getDrawable(R.styleable.SubTitleView_sub_rightIcon);

        leftText = array.getText(R.styleable.SubTitleView_sub_leftText);
        rightText = array.getText(R.styleable.SubTitleView_sub_rightText);

        leftTextSize = (int) array.getDimension(R.styleable.SubTitleView_sub_leftTextSize,sp2px(14f));
        rightTextSize = (int) array.getDimension(R.styleable.SubTitleView_sub_rightTextSize, sp2px(14f));

        leftTextColor = array.getColor(R.styleable.SubTitleView_sub_leftTextColor, Color.parseColor("#4D4D4D"));
        rightTextColor = array.getColor(R.styleable.SubTitleView_sub_rightTextColor, Color.parseColor("#707070"));

        leftTextStyle = array.getInt(R.styleable.SubTitleView_sub_leftTextStyle, 1);
        rightTextStyle = array.getInt(R.styleable.SubTitleView_sub_rightTextStyle, 1);

        rightTextHint = array.getText(R.styleable.SubTitleView_sub_rightHintText);
        rightTextHintColor = array.getColor(R.styleable.SubTitleView_sub_rightHintTextColor, Color.parseColor("#707070"));

        driverHeight = (int) array.getDimension(R.styleable.SubTitleView_sub_driverHeight,0f);
        driverColor = array.getColor(R.styleable.SubTitleView_sub_driverColor, Color.parseColor("#F5F5F5"));
        driverLeftMargin = (int) array.getDimension(R.styleable.SubTitleView_sub_leftDriverMargin, dp2px(16f));
        driverRightMargin = (int) array.getDimension(R.styleable.SubTitleView_sub_rightDriverMargin,0f);

        leftDrawablePadding = (int) array.getDimension(R.styleable.SubTitleView_sub_leftDrawablePadding,dp2px(2f));
        rightDrawablePadding = (int) array.getDimension(R.styleable.SubTitleView_sub_rightDrawablePadding,dp2px(2f));

        array.recycle();
    }

    private void initView() {
        initLeft();
        initRight();
        initDriver();
    }

    /**
     * 初始化左边布局
     */
    private void initLeft() {
        LayoutParams params = new LayoutParams(-2,-2);
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(-2,-2);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.leftMargin = leftMargin;
        ivParams.rightMargin = leftDrawablePadding;
        llLeft = new LinearLayout(getContext());
        llLeft.setLayoutParams(params);
        llLeft.setOrientation(LinearLayout.HORIZONTAL);
        llLeft.setGravity(Gravity.CENTER);

        TextView textView = new TextView(getContext());
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(ivParams);
        iv.setBackground(leftIcon);
        textView.setText(leftText);
        textView.setTextColor(leftTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
        textView.getPaint().setFakeBoldText(leftTextStyle == 0);
        textView.setMinWidth(30);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        llLeft.addView(iv);
        llLeft.addView(textView);

        addView(llLeft);


    }

    /**
     * 初始化右边布局
     */
    private void initRight(){
        LayoutParams params = new LayoutParams(-2,-2);
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(-2,-2);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.rightMargin = rightMargin;
        ivParams.leftMargin = rightDrawablePadding;
        llRight = new LinearLayout(getContext());
        llRight.setLayoutParams(params);
        llRight.setOrientation(LinearLayout.HORIZONTAL);
        llRight.setGravity(Gravity.CENTER);
        TextView textView = new TextView(getContext());
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(ivParams);
        iv.setBackground(rightIcon);
        if (rightIcon == null){
            iv.setVisibility(GONE);
        }else {
            iv.setVisibility(VISIBLE);
        }
        textView.setText(rightText);
        textView.setTextColor(rightTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        textView.getPaint().setFakeBoldText(rightTextStyle == 0);
        textView.setMinWidth(30);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setHint(rightTextHint);
        textView.setHintTextColor(rightTextHintColor);
        textView.setGravity(Gravity.END);

        llRight.addView(textView);
        llRight.addView(iv);
        addView(llRight);

    }

    /**
     * 初始化分割线
     */
    private void initDriver(){
        LayoutParams params = new LayoutParams(-1,-2);
        params.height = driverHeight;
        params.leftMargin = driverLeftMargin;
        params.rightMargin = driverRightMargin;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        driver = new View(getContext());
        driver.setLayoutParams(params);
        driver.setBackground(new ColorDrawable(driverColor));
        addView(driver);
    }

    /**
     * dp转px
     * @param dp
     * @return
     */
    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param sp
     * @return
     */
    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getContext().getResources().getDisplayMetrics());
    }

    public ImageView getLeftImage(){
        return (ImageView) llLeft.getChildAt(0);
    }

    public TextView getLeftTextView(){
        return (TextView) llLeft.getChildAt(1);
    }

    public ImageView getRightImage(){
        return (ImageView) llRight.getChildAt(1);
    }

    public TextView getRightTextView(){
        return (TextView) llRight.getChildAt(0);
    }

    /**
     * 设置左边图标
     * @param resource
     */
    public SubTitleView setLeftIcon(@DrawableRes int resource){
        getLeftImage().setBackgroundResource(resource);
        return this;
    }

    /**
     * 设置右边图标
     * @param resource
     */
    public SubTitleView setRightIcon(@DrawableRes int resource){
        getRightImage().setBackgroundResource(resource);
        return this;
    }


    /**
     * 设置左边图标
     * @param drawable
     */
    public SubTitleView setLeftIcon(@Nullable Drawable drawable) {
        getLeftImage().setBackground(drawable);
        return this;
    }

    /**
     * 设置右边图标
     * @param drawable
     */
    public SubTitleView setRightIcon(@Nullable Drawable drawable) {
        if (drawable == null){
            getRightImage().setVisibility(GONE);
        }else {
            getRightImage().setVisibility(VISIBLE);
        }
        getRightImage().setBackground(drawable);
        return this;
    }

    /**
     * 设置左边文本
     * @param leftText
     */
    public SubTitleView setLeftText(@Nullable CharSequence leftText) {
        getLeftTextView().setText(leftText);
        return this;
    }

    /**
     * 设置右边文本
     * @param rightText
     */
    public SubTitleView setRightText(@Nullable CharSequence rightText) {
        getRightTextView().setText(rightText);
        return this;
    }

    /**
     * 设置左边文本大小
     * @param textSize
     */
    public SubTitleView setLeftTextSize(int textSize) {
        getLeftTextView().setTextSize(textSize);
        return this;
    }

    /**
     * 设置右边文本大小
     * @param textSize
     */
    public SubTitleView setRightTextSize(int textSize) {
        getRightTextView().setTextSize(textSize);
        return this;
    }

    /**
     * 设置左边文本颜色
     * @param color
     */
    public SubTitleView setLeftTextColor(@ColorInt int color) {
        getLeftTextView().setTextColor(color);
        return this;
    }

    /**
     * 设置右边文本颜色
     * @param color
     */
    public SubTitleView setRightTextColor(@ColorInt int color) {
        getRightTextView().setTextColor(color);
        return this;
    }

    /**
     * 设置左边文本样式
     * @param leftTextStyle
     */
    public SubTitleView setLeftTextStyle(@TextStyle int leftTextStyle) {
        getLeftTextView().getPaint().setFakeBoldText(leftTextStyle == BOLD);
        return this;
    }

    /**
     * 设置右边文本样式
     * @param rightTextStyle
     */
    public SubTitleView setRightTextStyle(@TextStyle int rightTextStyle) {
        getRightTextView().getPaint().setFakeBoldText(rightTextStyle == BOLD);
        return this;
    }

    /**
     * 设置分割线高度
     * @param driverHeight
     */
    public SubTitleView setDriverHeight(@Px int driverHeight) {
        driver.getLayoutParams().height = driverHeight;
        return this;
    }

    /**
     * 设置分割线颜色
     * @param driverColor
     */
    public SubTitleView setDriverColor(@ColorInt int driverColor) {
        driver.setBackground(new ColorDrawable(driverColor));
        return this;
    }

    /**
     * 设置分割线左边距
     * @param driverLeftMargin
     */
    public SubTitleView setDriverLeftMargin(int driverLeftMargin) {
        ((LayoutParams)driver.getLayoutParams())
                .leftMargin = driverLeftMargin;
        return this;
    }

    /**
     * 设置分割线右边距
     * @param driverRightMargin
     */
    public SubTitleView setDriverRightMargin(int driverRightMargin) {
        ((LayoutParams)driver.getLayoutParams())
                .leftMargin = driverRightMargin;
        return this;
    }

    /**
     * 设置左边图标和文本之间的距离
     * @param leftDrawablePadding
     */
    public SubTitleView setLeftDrawablePadding(int leftDrawablePadding) {
        ((LinearLayout.LayoutParams)getLeftImage().getLayoutParams())
                .rightMargin = leftDrawablePadding;
        return this;
    }

    /**
     * 设置右边图标和文本之间的距离
     * @param rightDrawablePadding
     */
    public SubTitleView setRightDrawablePadding(int rightDrawablePadding) {
        ((LinearLayout.LayoutParams)getRightImage().getLayoutParams())
                .leftMargin = rightDrawablePadding;
        return this;
    }

    /**
     * 设置左边布局点击事件
     * @param listener
     */
    public SubTitleView setLeftClickListener(OnClickListener listener){
        llLeft.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置右边布局点击事件
     * @param listener
     */
    public SubTitleView setRightClickListener(OnClickListener listener){
        llRight.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置左边icon点击事件
     * @param listener
     */
    public SubTitleView setLeftIconClickListener(OnClickListener listener){
        getLeftImage().setOnClickListener(listener);
        return this;
    }

    /**
     * 设置右边icon点击事件
     * @param listener
     */
    public SubTitleView setRightIconClickListener(OnClickListener listener){
        getRightImage().setOnClickListener(listener);
        return this;
    }

    /**
     * 设置左边文字点击事件
     * @param listener
     */
    public SubTitleView setLeftTextClickListener(OnClickListener listener){
        getLeftTextView().setOnClickListener(listener);
        return this;
    }

    /**
     * 设置右边文字点击事件
     * @param listener
     */
    public SubTitleView setRightTextClickListener(OnClickListener listener){
        getRightTextView().setOnClickListener(listener);
        return this;
    }
}
