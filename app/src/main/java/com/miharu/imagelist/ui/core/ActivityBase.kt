package com.miharu.imagelist.ui.core

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

abstract class ActivityBase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutView())
        setupContent()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)

                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    // applicationContext.hideKeyboard(view)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    /** Implementation :
     * binding = ExBinding.inflate(layoutInflater)
     * return binding.root
     */
    abstract fun getLayoutView(): View

    /**
     * Observe data, set button action etc.
     */
    abstract fun setupContent()
}