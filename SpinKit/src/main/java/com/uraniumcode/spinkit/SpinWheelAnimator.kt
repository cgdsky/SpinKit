package com.uraniumcode.spinkit

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd

class SpinWheelAnimator(
    private val onAngleUpdate: (Float) -> Unit,
    private val onPointerShake: (Float) -> Unit,
    private val onSpinEnd: () -> Unit
) {
    private var tickAnimator: ValueAnimator? = null

    fun startSpin(
        fromAngle: Float,
        toAngle: Float,
        duration: Long = 5000L,
        sliceCount: Int,
        anglePerSlice: Float,
        rawPointerAngle: Float
    ) {
        ValueAnimator.ofFloat(fromAngle, toAngle).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()

            var lastTickSlice = -1

            addUpdateListener { anim ->
                val currentAngle = anim.animatedValue as Float
                onAngleUpdate(currentAngle)

                val sliceIdx =
                    calculateSliceIndex(currentAngle, rawPointerAngle, anglePerSlice, sliceCount)

                if (sliceIdx != lastTickSlice) {
                    lastTickSlice = sliceIdx
                    triggerPointerShake()
                }
            }

            doOnEnd {
                tickAnimator?.cancel()
                onSpinEnd()
            }

            start()
        }
    }

    private fun calculateSliceIndex(
        currentAngle: Float,
        rawPointerAngle: Float,
        anglePerSlice: Float,
        sliceCount: Int
    ): Int {
        val normalizedAngle = (currentAngle % 360f + 360f) % 360f
        val wheelAngle = (rawPointerAngle - normalizedAngle + 360f) % 360f
        return (wheelAngle / anglePerSlice).toInt() % sliceCount
    }

    private fun triggerPointerShake() {
        tickAnimator?.cancel()
        tickAnimator = ValueAnimator.ofFloat(-12f, 0f, 12f, 0f).apply {
            duration = 150L
            addUpdateListener { shakeAnim ->
                onPointerShake(shakeAnim.animatedValue as Float)
            }
            start()
        }
    }
}
