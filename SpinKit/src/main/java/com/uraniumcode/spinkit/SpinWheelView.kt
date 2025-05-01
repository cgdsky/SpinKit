package com.uraniumcode.spinkit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import androidx.core.graphics.withRotation


class SpinWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var items: List<SpinWheelItem> = emptyList()
    private var centerIcon: Bitmap? = null
    private var spinType: SpinType = SpinType.Random
    private var config: SpinWheelConfig = SpinWheelConfig()
    private var currentAngle: Float = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER }

    private val drawer = SpinWheelDrawer(context)

    private val ringStrokeWidth = 30f
    private var ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = ringStrokeWidth
        color = Color.DKGRAY
        strokeCap = Paint.Cap.ROUND
    }
    private var pointerShakeOffset = 0f
    private val pointerUpOffset = 16f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (items.isEmpty()) return
        val w = width.toFloat()
        val h = height.toFloat()
        val radius = min(w, h) / 2f * 0.9f
        val centerX = w / 2f
        val centerY = h / 2f
        val anglePerSlice = 360f / items.size
        canvas.drawCircle(centerX, centerY, radius + ringPaint.strokeWidth / 2, ringPaint)
        canvas.withRotation(currentAngle, centerX, centerY) {
            items.forEachIndexed { index, item ->
                val startAngle = index * anglePerSlice
                paint.style = Paint.Style.FILL
                paint.color = item.sliceColor
                drawArc(
                    centerX - radius, centerY - radius,
                    centerX + radius, centerY + radius,
                    startAngle, anglePerSlice, true, paint
                )
                drawer.drawRingDot(
                    this,
                    centerX,
                    centerY,
                    radius,
                    startAngle,
                    radius,
                    items.size,
                    paint
                )
                drawer.drawRingDot(
                    this,
                    centerX,
                    centerY,
                    radius,
                    startAngle + anglePerSlice,
                    radius,
                    items.size,
                    paint
                )
                drawer.drawTextAndIconRotating(
                    this,
                    item,
                    startAngle + anglePerSlice / 2,
                    centerX,
                    centerY,
                    radius,
                    config,
                    textPaint
                )
            }
        }
        drawer.drawExtendedPointer(canvas, centerX, centerY, radius, ringStrokeWidth, pointerUpOffset, pointerShakeOffset, paint)
        drawer.drawCenterIcon(canvas, centerX, centerY, centerIcon, config)
    }
}