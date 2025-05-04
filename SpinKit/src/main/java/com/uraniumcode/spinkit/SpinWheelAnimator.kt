package com.uraniumcode.spinkit

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd

class SpinWheelAnimator(
    private val onAngleUpdate: (Float) -> Unit,
    private val onPointerShake: (Float) -> Unit,
    private val onSpinEnd: () -> Unit
) {
    fun startSpin(
        fromAngle: Float,
        toAngle: Float,
        duration: Long = 5000L,
        sliceCount: Int,
        anglePerSlice: Float,
        rawPointerAngle: Float
    ) {
        val animator = ValueAnimator.ofFloat(fromAngle, toAngle).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
            var lastTickSlice = -1
            addUpdateListener { anim ->
                val currentAngle = anim.animatedValue as Float
                onAngleUpdate(currentAngle)
                val nowNorm = (currentAngle % 360f + 360f) % 360f
                val wheelAngle0 = (rawPointerAngle - nowNorm + 360f) % 360f
                val sliceIdx = (wheelAngle0 / anglePerSlice).toInt() % sliceCount
                if (sliceIdx != lastTickSlice) {
                    lastTickSlice = sliceIdx
                    ValueAnimator.ofFloat(-12f, 0f, 12f, 0f).apply {
                        this.duration = 150
                        addUpdateListener { sh ->
                            onPointerShake(sh.animatedValue as Float)
                        }
                    }.start()
                }
            }
            doOnEnd {
                onSpinEnd()
            }
        }
        animator.start()
    }
} 