package pw.androidthanatos.lib.widget.inputfilter

import android.text.InputFilter
import android.text.Spanned

/**
 * @desc: 禁止输入过滤器
 * @className: InputFilterNoInput
 * @author: thanatos
 * @createTime: 2018/10/19
 */
class InputFilterNoInput : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        return dest.subSequence(dstart, dend)
    }
}
