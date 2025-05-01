package com.uraniumcode.spinkit

import android.graphics.Typeface

data class SpinWheelConfig(
    val textSize: Float = 35f,
    val spinDuration: Long = 4000L,
    val textTypeface: Typeface? = null,
    val labelMargin: Float = 20f,
    val iconSize: Float = 48f,
    val centerIconSize: Float = 100f,
)
