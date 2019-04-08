package pw.androidthanatos.lib.widget.inputfilter

import android.text.InputFilter
import android.text.Spanned

import java.util.regex.Pattern

/**
 * 功能描述: 输入框特殊字符过滤
 * @className: InputFilterSpecialChar
 * @author: thanatos
 * @createTime: 2018/6/11
 * @updateTime: 2018/6/11 09:29
 */
@Deprecated("暂不使用")
class InputFilterSpecialChar : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(regexStr)
        val matcher = pattern.matcher(source.toString())
        return if (matcher.matches()) {
            ""
        } else {
            matcher.replaceAll("")
        }
    }

}
