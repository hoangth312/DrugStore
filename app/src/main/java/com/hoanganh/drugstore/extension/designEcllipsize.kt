package com.hoanganh.drugstore.extension

import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView


fun CustomEllipsize(tv: TextView, maxLine: Int, ellipsizetext: String) {
    if (tv.tag == null) {
        tv.tag = tv.text
    }
    val vto = tv.viewTreeObserver
    vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val obs = tv.viewTreeObserver
            obs.removeGlobalOnLayoutListener(this)
            if (maxLine + 1 <= 0) {
                val lineEndIndex = tv.layout.getLineEnd(0)
                val text = tv.text.subSequence(0, lineEndIndex - ellipsizetext.length).toString() + "" + ellipsizetext
                tv.text = text
            } else if (tv.lineCount >= maxLine + 1) {
                val lineEndIndex = tv.layout.getLineEnd(maxLine + 1 - 1)
                val text = tv.text.subSequence(0, lineEndIndex - ellipsizetext.length).toString() + "" + ellipsizetext
                tv.text = text
            }
        }
    })
}