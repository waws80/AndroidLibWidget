package pw.androidthanatos.lib.widget.inputfilter

import android.text.InputFilter
import android.text.Spanned

/**
 * @desc: 金钱输入限制
 * @className: InputFilterMoneyX
 * @author: thanatos
 * @createTime: 2018/10/19
 */
class InputFilterMoneyX : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (source.toString().trim { it <= ' ' } == "."
            && dstart == 0 && dend == 0) {
            return "0$source$dest"
        }
        if (dest.toString().contains(".") && dest.length - dest.toString().indexOf(".") > 2) {
            if (dest.length - dstart < 3) {
                return ""
            }
        }
        return null
    }
}
