package com.sergiocruz.fingerprint

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class FingerPrintView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ImageView(context, attrs) {

    private var icon: Drawable? = null
    private var state = State.OFF

    // Keep in sync with attrs.
    enum class State {
        ON,
        OFF,
        ERROR,
        SUCCESS,
    }

    private var padding: Int = 0

    init {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            throw AssertionError("API 21 required.")
        }

        padding = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()

        contentDescription = context.getString(R.string.fingerprint)

        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.FingerPrintView)
        val state = typedArray.getInteger(R.styleable.FingerPrintView_state, -1)
        if (state != -1) setState(State.values()[state], false)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        val contentWidth = width - padding
        icon?.setBounds(padding / 2, padding / 2, contentWidth / 2, contentWidth / 2)
        super.onDraw(canvas)
    }

    @JvmOverloads
    fun setState(state: State, animate: Boolean = true) {

        @DrawableRes
        val resId = getDrawable(this.state, state, animate)
        if (resId == 0) {
            setImageDrawable(null)
        } else {
            val icon: Drawable?
            if (animate) {
                icon = AnimatedVectorDrawableCompat.create(context, resId)
            } else {
                icon = VectorDrawableCompat.create(resources, resId, context.theme)
            }
            this.icon = icon

            setImageDrawable(icon)

            setBackgroundResource(R.drawable.circle_background)

            try {
                if (icon is AnimatedVectorDrawableCompat?) {
                    icon?.start()
                }
            } catch (e: Exception) {
            }
            if (state == State.ERROR) {
                postDelayed({ setState(State.OFF, true) }, 4000)
            }
        }

        this.state = state
    }

    @DrawableRes
    private fun getDrawable(currentState: State, newState: State, animate: Boolean): Int {

        return when (currentState) {
            State.ON ->
                when (newState) {
                    State.ON -> if (animate) R.drawable.swirl_fingerprint_draw_on_animation else R.drawable.swirl_fingerprint
                    State.OFF -> if (animate) R.drawable.swirl_fingerprint_draw_off_animation else R.drawable.swirl_fingerprint_draw_off
                    State.ERROR -> if (animate) R.drawable.swirl_fingerprint_fp_to_error_state_animation else R.drawable.swirl_error
                    State.SUCCESS -> TODO()
                }
            State.OFF ->
                when (newState) {
                    State.ON -> if (animate) R.drawable.swirl_fingerprint_draw_on_animation else R.drawable.swirl_fingerprint
                    State.OFF -> R.drawable.swirl_fingerprint_draw_off
                    State.ERROR -> if (animate) R.drawable.swirl_error_draw_on_animation else R.drawable.swirl_error
                    State.SUCCESS -> TODO()
                }
            State.ERROR ->
                when (newState) {
                    State.ON -> if (animate) R.drawable.swirl_fingerprint_error_state_to_fp_animation else R.drawable.swirl_fingerprint
                    State.OFF -> if (animate) R.drawable.swirl_fingerprint_draw_off_animation else R.drawable.swirl_fingerprint_draw_off
                    State.ERROR -> if (animate) R.drawable.swirl_fingerprint_fp_to_error_state_animation else R.drawable.swirl_error
                    State.SUCCESS -> TODO()
                }
            State.SUCCESS -> 0
        }


//        when (newState) {
//            State.ON -> {
//                if (animate) {
//                    if (currentState == State.OFF) {
//                        return R.drawable.swirl_fingerprint_draw_on_animation
//                    } else if (currentState == State.ERROR) {
//                        return R.drawable.swirl_fingerprint_error_state_to_fp_animation
//                    }
//                }
//                return R.drawable.swirl_fingerprint
//            }
//            State.OFF -> {
//                if (animate) {
//                    if (currentState == State.ON) {
//                        return R.drawable.swirl_fingerprint_draw_off_animation
//                    } else if (currentState == State.ERROR) {
//                        return R.drawable.swirl_error_draw_off_animation
//                    }
//                }
//                return R.drawable.swirl_fingerprint_draw_off
//            }
//            State.ERROR -> {
//                if (animate) {
//                    if (currentState == State.ON) {
//                        return R.drawable.swirl_fingerprint_fp_to_error_state_animation
//                    } else if (currentState == State.OFF) {
//                        return R.drawable.swirl_error_draw_on_animation
//                    }
//                }
//                return R.drawable.swirl_error
//            }
//            State.SUCCESS -> TODO()
//        }
    }
}
