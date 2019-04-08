package pw.androidthanatos.lib.widget.inputfilter

import android.text.InputFilter
import android.text.Spanned

import java.util.regex.Pattern

/**
 * 功能描述:
 * @className: InputFilterEmoji
 * @author: thanatos
 * @createTime: 2018/6/11
 * @updateTime: 2018/6/11 09:29
 */
class InputFilterEmoji : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        //如果是数字直接返回
        if (isMatch(NUM, source)) {
            return source
        }

        return if (isEmoji(source)) {
            ""
        } else source
    }

    companion object {

        private const val NUM = "^[0-9]*$"

        private fun isMatch(regex: String, input: CharSequence?): Boolean {
            return input != null && input.isNotEmpty() && Pattern.matches(regex, input)
        }

        /**
         * Emoji表情校验
         *
         * @param string
         * @return
         */
        fun isEmoji(string: CharSequence): Boolean {
            //过滤Emoji表情
            val p = Pattern.compile("[^\\u0000-\\uFFFF]")
            //过滤Emoji表情和颜文字
            //val p1 = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\ud83e\\udd00-\\ud83e\\uddff]|[\\u2300-\\u23ff]|[\\u2500-\\u25ff]|[\\u2100-\\u21ff]|[\\u0000-\\u00ff]|[\\u2b00-\\u2bff]|[\\u2d06]|[\\u3030]")
            val m = p.matcher(string)
            return m.find()
        }
    }
}
