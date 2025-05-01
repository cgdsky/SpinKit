package com.uraniumcode.spinkit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


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


    override fun onDraw(canvas: Canvas) {}
}