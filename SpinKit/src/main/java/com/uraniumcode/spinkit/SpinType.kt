package com.uraniumcode.spinkit

sealed class SpinType {
    data object Random : SpinType()
    data class Targeted(val index: Int) : SpinType()
}