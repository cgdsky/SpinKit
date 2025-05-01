package com.uraniumcode.spinkit

import android.graphics.Color


enum class IconPosition {
    TOP, BOTTOM
}

data class SpinWheelItem(
    val iconRes: Int?,
    val label: String?,
    val sliceColor: Int = Color.BLACK,
    val textColor: Int = Color.WHITE,
    val iconPosition: IconPosition = IconPosition.TOP
)

