package pw.androidthanatos.lib.widget.inputfilter

import android.text.InputFilter
import android.widget.TextView

/**
 * @desc: 编辑框格式过滤器
 * @className: InputFilterUtil
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午5:04
 */
class InputFilterUtil private constructor() {

    /**
     * 不能输入表情
     * @return
     */
    val emojiFilter: InputFilterEmoji
        get() = InputFilterEmoji()

    /**
     * 不能输入特殊字符
     * @return
     */
    val specialCharFilter: InputFilterSpecialChar
        get() = InputFilterSpecialChar()

    /**
     * 只能输入金钱
     * @return
     */
    val moneyXFilter: InputFilterMoneyX
        get() = InputFilterMoneyX()

    /**
     * 不能输入
     * @return
     */
    val noInputFilter: InputFilterNoInput
        get() = InputFilterNoInput()

    /**
     * 不能输入空格 和换行
     * @return
     */
    val noSpaceFilter: InputFilterNoSpaceEnter
        get() = InputFilterNoSpaceEnter()

    /**
     * 指定长度
     * @param max
     * @return
     */
    fun getLengthFilter(max: Int): InputFilter.LengthFilter {
        return InputFilter.LengthFilter(max)
    }

    companion object {

        private val filters = mutableListOf<InputFilter>()

        /**
         * 唯一对象
         */
        val instance: InputFilterUtil = InputFilterUtil()
    }

    /**
     * 注册过滤器
     */
    fun registerFilter(filter: InputFilter): InputFilterUtil {
        filters.add(filter)
        return this
    }

    /**
     * 将过滤器注册给控件
     */
    fun toTarget(view: TextView){
        view.filters = filters.toTypedArray()
        filters.clear()
    }
}
