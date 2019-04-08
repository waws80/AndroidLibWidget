package pw.androidthanatos.lib.widget.inputfilter

import android.text.InputFilter
import android.text.Spanned

/**
 * @desc: 不能输入空格的过滤器
 * @className: InputFilterNoSpace
 * @author: thanatos
 * @createTime: 2018/10/19
 */
class InputFilterNoSpaceEnter : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        return if (source == " " || source == "\n") {
            ""
        } else source
    }
}
