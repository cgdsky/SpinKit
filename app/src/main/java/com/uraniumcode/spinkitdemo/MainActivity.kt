package com.uraniumcode.spinkitdemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.uraniumcode.spinkit.IconPosition
import com.uraniumcode.spinkit.SpinWheelConfig
import com.uraniumcode.spinkit.SpinWheelItem
import com.uraniumcode.spinkit.SpinWheelView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val showSpinWheelButton = findViewById<Button>(R.id.show_spin_wheel_button)

        showSpinWheelButton.setOnClickListener {
            showSpinWheelButton.visibility = View.GONE
            showSpinWheel()
        }

    }

    private fun spinWheelSettings(): SpinWheelView {
        val spinWheelView = SpinWheelView(this)

        val items = listOf(
            SpinWheelItem(
                R.drawable.coin,
                "10 Coin",
                Color.MAGENTA,
                iconPosition = IconPosition.TOP,
                textColor = Color.WHITE
            ),
            SpinWheelItem(
                R.drawable.coin,
                "50 Coin",
                Color.GREEN,
                iconPosition = IconPosition.TOP,
                textColor = Color.WHITE
            ),
            SpinWheelItem(
                R.drawable.coin,
                "100 Coin",
                Color.CYAN,
                iconPosition = IconPosition.TOP,
                textColor = Color.WHITE
            ),
            SpinWheelItem(
                R.drawable.coin,
                "200 Coin",
                Color.BLUE,
                iconPosition = IconPosition.BOTTOM,
                textColor = Color.WHITE
            ),
            SpinWheelItem(
                R.drawable.coin,
                "250 Coin",
                Color.RED,
                iconPosition = IconPosition.BOTTOM,
                textColor = Color.WHITE
            ),
            SpinWheelItem(null, "Try Again", Color.BLACK, textColor = Color.RED),
        )

        spinWheelView.setItems(items)
        spinWheelView.setCenterIcon(R.drawable.coin)
        spinWheelView.setSpinTypeRandom()

        val customConfig = SpinWheelConfig(
            textSize = 50f,
            iconSize = 60f
        )

        spinWheelView.setSpinWheelConfig(customConfig)
        spinWheelView.setOnSpinEndListener { index, item ->
            Toast.makeText(this, " ${item.label}", Toast.LENGTH_SHORT).show()
        }
        return spinWheelView
    }

    private fun showSpinWheel() {
        val spinWheelView = spinWheelSettings()

        val popupWindow = PopupWindow(
            spinWheelView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        ).apply {
            isFocusable = true
            isOutsideTouchable = true
        }

        popupWindow.showAtLocation(
            findViewById(android.R.id.content),
            android.view.Gravity.CENTER,
            0,
            0
        )
    }
}