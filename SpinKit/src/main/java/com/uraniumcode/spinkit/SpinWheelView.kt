package com.uraniumcode.spinkit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withRotation
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.sqrt

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
    private var pointerShakeOffset: Float = 0f
    private val pointerUpOffset = 16f
    private var animator: SpinWheelAnimator? = null
    private var onSpinEndListener: ((Int, SpinWheelItem) -> Unit)? = null
    private var lastTouchAngle = 0.0
    private val gestureDetector: GestureDetector

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER }
    private val ringStrokeWidth = 30f
    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = ringStrokeWidth
        color = Color.DKGRAY
        strokeCap = Paint.Cap.ROUND
    }

    private var wheelBitmap: Bitmap? = null
    private var wheelCanvas: Canvas? = null
    private val drawer = SpinWheelDrawer(context)

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?, e2: MotionEvent,
                velocityX: Float, velocityY: Float
            ): Boolean {
                val velocity = sqrt(velocityX * velocityX + velocityY * velocityY)
                if (velocity > 1500) spin()
                return true
            }
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rebuildWheelCache()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (items.isEmpty()) return

        val w = width.toFloat()
        val h = height.toFloat()
        val centerX = w / 2f
        val centerY = h / 2f
        val radius = min(w, h) / 2f * 0.9f

        canvas.drawCircle(centerX, centerY, radius + ringStrokeWidth / 2, ringPaint)

        wheelBitmap?.let {
            canvas.withRotation(currentAngle, centerX, centerY) {
                drawBitmap(it, 0f, 0f, null)
            }
        }

        drawer.drawExtendedPointer(
            canvas,
            centerX,
            centerY,
            radius,
            ringStrokeWidth,
            pointerUpOffset,
            pointerShakeOffset,
            paint
        )

        drawer.drawCenterIcon(canvas, centerX, centerY, centerIcon, config)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        val centerX = width / 2f
        val centerY = height / 2f
        val angle = Math.toDegrees(atan2((event.y - centerY).toDouble(), (event.x - centerX).toDouble()))
        when (event.action) {
            MotionEvent.ACTION_DOWN -> lastTouchAngle = angle
            MotionEvent.ACTION_MOVE -> {
                val delta = angle - lastTouchAngle
                currentAngle += delta.toFloat()
                lastTouchAngle = angle
                invalidate()
            }
            MotionEvent.ACTION_UP -> performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun setItems(items: List<SpinWheelItem>) {
        this.items = items
        rebuildWheelCache()
        invalidate()
    }

    fun setCenterIcon(resId: Int) {
        centerIcon = BitmapFactory.decodeResource(resources, resId)
        invalidate()
    }

    fun setSpinTypeRandom() { spinType = SpinType.Random }
    fun setSpinTypeTargeted(index: Int) { spinType = SpinType.Targeted(index) }
    fun setSpinWheelConfig(config: SpinWheelConfig) {
        this.config = config
        rebuildWheelCache()
        invalidate()
    }

    fun setOnSpinEndListener(listener: (Int, SpinWheelItem) -> Unit) {
        onSpinEndListener = listener
    }

    fun spin() {
        if (items.isEmpty()) return
        val sliceCount = items.size
        val anglePerSlice = 360f / sliceCount
        val rawPointerAngle = 268f
        val targetIndex = when (spinType) {
            is SpinType.Random -> items.indices.random()
            is SpinType.Targeted -> (spinType as SpinType.Targeted).index
        }
        val desired = targetIndex * anglePerSlice
        val normalized = (rawPointerAngle - desired + 360f) % 360f
        val spins = 5
        val fromAngle = currentAngle
        val toAngle = 360f * spins + normalized

        animator = SpinWheelAnimator(
            onAngleUpdate = { angle -> currentAngle = angle; postInvalidateOnAnimation() },
            onPointerShake = { offset -> pointerShakeOffset = offset; postInvalidateOnAnimation() },
            onSpinEnd = { currentAngle = normalized; postInvalidateOnAnimation(); handleSpinEnd() }
        )
        animator?.startSpin(fromAngle, toAngle, duration = 5000L, sliceCount, anglePerSlice, rawPointerAngle)
    }

    private fun handleSpinEnd() {
        if (items.isEmpty()) return
        val normalized = (currentAngle % 360f + 360f) % 360f
        val sliceCount = items.size
        val anglePerSlice = 360f / sliceCount
        val rawPointerAngle = 270f
        val wheelAngle = (rawPointerAngle - normalized + 360f) % 360f
        val winningIndex = (wheelAngle / anglePerSlice).toInt() % sliceCount
        onSpinEndListener?.invoke(winningIndex, items[winningIndex])
    }

    private fun rebuildWheelCache() {
        if (width == 0 || height == 0 || items.isEmpty()) return
        val w = width.toFloat()
        val h = height.toFloat()
        val centerX = w / 2f
        val centerY = h / 2f
        val radius = min(w, h) / 2f * 0.9f
        val anglePerSlice = 360f / items.size

        wheelBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        wheelCanvas = Canvas(wheelBitmap!!)
        val drawer = SpinWheelDrawer(context)

        items.forEachIndexed { index, item ->
            val startAngle = index * anglePerSlice
            paint.style = Paint.Style.FILL
            paint.color = item.sliceColor
            wheelCanvas!!.drawArc(
                centerX - radius, centerY - radius,
                centerX + radius, centerY + radius,
                startAngle, anglePerSlice, true, paint
            )
            drawer.drawRingDot(wheelCanvas!!, centerX, centerY, radius, startAngle, radius, items.size, paint)
            drawer.drawRingDot(wheelCanvas!!, centerX, centerY, radius, startAngle + anglePerSlice, radius, items.size, paint)
            drawer.drawTextAndIconRotating(wheelCanvas!!, item, startAngle + anglePerSlice / 2, centerX, centerY, radius, config, textPaint)
        }
    }
}
