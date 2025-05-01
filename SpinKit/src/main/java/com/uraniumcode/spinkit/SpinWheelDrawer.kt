package com.uraniumcode.spinkit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.cos
import kotlin.math.sin

class SpinWheelDrawer(private val context: Context) {
    fun drawRingDot(canvas: Canvas, cx: Float, cy: Float, r: Float, angle: Float, radius: Float, itemsSize: Int, paint: Paint) {
        val rad = Math.toRadians(angle.toDouble())
        val dotRadius = radius * 0.03f
        val dotR = r + 12f
        val x = (cx + dotR * cos(rad)).toFloat()
        val y = (cy + dotR * sin(rad)).toFloat()
        paint.style = Paint.Style.FILL
        paint.color = if ((angle / (360f / itemsSize)).toInt() % 2 == 0) Color.BLACK else Color.RED
        canvas.drawCircle(x, y, dotRadius, paint)
    }

    fun drawExtendedPointer(canvas: Canvas, cx: Float, cy: Float, radius: Float, ringStrokeWidth: Float, pointerUpOffset: Float, pointerShakeOffset: Float, paint: Paint) {
        val pointerWidth = radius * 0.12f
        val pointerDepth = radius * 0.1f
        val ringOffset = ringStrokeWidth / 2f
        val outerY = cy - radius - ringOffset - pointerUpOffset - 30
        val innerY = outerY + pointerDepth
        canvas.save()
        canvas.rotate(pointerShakeOffset, cx, outerY)
        val path = Path().apply {
            moveTo(cx - pointerWidth / 2, outerY)
            lineTo(cx + pointerWidth / 2, outerY)
            lineTo(cx, innerY)
            close()
        }
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    fun drawTextAndIconRotating(
        canvas: Canvas,
        item: SpinWheelItem,
        angle: Float,
        centerX: Float,
        centerY: Float,
        radius: Float,
        config: SpinWheelConfig,
        textPaint: Paint
    ) {
        val textRadius = radius * 0.7f
        val rad = Math.toRadians(angle.toDouble())
        val x = (centerX + textRadius * cos(rad)).toFloat()
        val y = (centerY + textRadius * sin(rad)).toFloat()
        textPaint.textSize = config.textSize
        textPaint.color = item.textColor
        textPaint.typeface = config.textTypeface
        textPaint.textAlign = Paint.Align.CENTER
        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(angle + 90f)
        item.iconRes?.let { resId ->
            val bitmap = BitmapFactory.decodeResource(context.resources, resId)
            val scaled = Bitmap.createScaledBitmap(bitmap, config.iconSize.toInt(), config.iconSize.toInt(), true)
            val iconX = -config.iconSize / 2
            val iconY = when (item.iconPosition) {
                IconPosition.TOP -> -config.iconSize - 35f
                IconPosition.BOTTOM -> 20f
            }
            canvas.drawBitmap(scaled, iconX, iconY, null)
        }
        item.label?.let { canvas.drawText(it, 0f, 0f, textPaint) }
        canvas.restore()
    }

    fun drawCenterIcon(canvas: Canvas, centerX: Float, centerY: Float, centerIcon: android.graphics.Bitmap?, config: SpinWheelConfig) {
        centerIcon?.let {
            val centerSize = config.centerIconSize
            val left = centerX - centerSize / 2
            val top = centerY - centerSize / 2
            val bitmap = Bitmap.createScaledBitmap(it, centerSize.toInt(), centerSize.toInt(), true)
            canvas.drawBitmap(bitmap, left, top, null)
        }
    }
} 