# SpinKit

A customizable and animated spin wheel component for Android.

## ✨ Features

- 🎨 Customizable slices: color, icon, label, text color, icon position (top/bottom)  
- 🧲 Center icon support  
- 🎯 Animated spinning with random or targeted result  
- ✋ Gesture support: spin by fling or drag  
- 📞 Callback for spin result  
- 🔌 Easy integration and configuration  
- 🎮  Demo usage included  


![](https://cagdaskaya.com.tr/video/spinkit.gif)

---

## 📦 Installation

Using **JitPack**:

1. Add the JitPack repository to your **root** `build.gradle`:
    ```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
    ```

2. Add the dependency to your **module** (`app`) `build.gradle`:
    ```groovy
    implementation 'com.github.cgdsky:SpinKit:1.0.0'
    ```

---

## 🚀 Usage

### 1. Show `SpinWheelView` in a Popup (Programmatic Example)

```kotlin
val showSpinWheelButton = findViewById<Button>(R.id.show_spin_wheel_button)
showSpinWheelButton.setOnClickListener {
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
            SpinWheelItem(null, "Try Again", Color.BLACK, textColor = Color.RED)
        // ... add more items
    )

    spinWheelView.setItems(items)
    spinWheelView.setCenterIcon(R.drawable.coin)
    spinWheelView.setSpinTypeRandom()
    spinWheelView.setSpinWheelConfig(
        SpinWheelConfig(
            textSize = 50f,
            iconSize = 60f
        )
    )
    spinWheelView.setOnSpinEndListener { index, item ->
        Toast.makeText(this, "You won: ${item.label}", Toast.LENGTH_SHORT).show()
    }

    val popupWindow = PopupWindow(
        spinWheelView,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    )
    popupWindow.isFocusable = true
    popupWindow.isOutsideTouchable = true
    popupWindow.showAtLocation(
        findViewById(android.R.id.content),
        Gravity.CENTER, 0, 0
    )
}
```
---

### 2. XML Usage (Add SpinWheelView in XML Layout)

XML Layout (e.g. activity_main.xml):
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.uraniumcode.spinkit.SpinWheelView
        android:id="@+id/spinWheelView"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <Button
        android:id="@+id/spin_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Spin"
        app:layout_constraintTop_toBottomOf="@id/spinWheelView"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>
</androidx.constraintlayout.widget.ConstraintLayout>
```
Activity Code:
```kotlin
val spinWheelView = findViewById<SpinWheelView>(R.id.spinWheelView)

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
            SpinWheelItem(null, "Try Again", Color.BLACK, textColor = Color.RED)
    // ... add more items
)

spinWheelView.setItems(items)
spinWheelView.setCenterIcon(R.drawable.coin)
spinWheelView.setSpinTypeRandom()
spinWheelView.setSpinWheelConfig(
    SpinWheelConfig(
        textSize = 50f,
        iconSize = 60f
    )
)
spinWheelView.setOnSpinEndListener { index, item ->
    Toast.makeText(this, "You won: ${item.label}", Toast.LENGTH_SHORT).show()
}

val spinButton = findViewById<Button>(R.id.spin_button)
spinButton.setOnClickListener {
    spinWheelView.spin()
}
```

## ⚙️ Customization

- **Slices:** Customize the following attributes for each slice:
  - `sliceColor`: The color of the slice (e.g., `Color.RED`, `Color.GREEN`).
  - `iconRes`: The resource ID for the icon to display in the slice (e.g., `R.drawable.my_icon`).
  - `label`: The label/text displayed on the slice (e.g., `"10 Coins"`).
  - `textColor`: The color of the text (e.g., `Color.WHITE`, `Color.BLACK`).
  - `iconPosition`: The position of the icon relative to the text (`IconPosition.TOP` or `IconPosition.BOTTOM`).

- **Center Icon:** You can set a central icon for the spin wheel using:
  - `setCenterIcon(R.drawable.my_icon)`

- **Spin Type:**
  - **Random Spin:** To make the wheel spin randomly and stop at a random slice:
    ```kotlin
    spinWheelView.setSpinTypeRandom()
    ```
  - **Targeted Spin:** To make the wheel stop at a specific slice:
    ```kotlin
    spinWheelView.setSpinTypeTargeted(index) // index is the position of the targeted slice
    ```

- **Configurable Style:** Use `SpinWheelConfig` to configure:
  - `textSize`: The size of the text on the slices (e.g., `50f`).
  - `iconSize`: The size of the icons on the slices (e.g., `60f`).
  - `spinDuration`: The duration of the spin animation in milliseconds (e.g., `1000`).

- **Gestures:** 
  - Users can spin the wheel by either flinging it (fast spin starts the spinning action) or dragging it manually (slow spin will only animate rotation without triggering a result).

- **Result Listener:** Use `setOnSpinEndListener` to get the result once the spin ends:
  ```kotlin
  spinWheelView.setOnSpinEndListener { index, item ->
      Toast.makeText(this, "You won: ${item.label}", Toast.LENGTH_SHORT).show()
  }

🎮  Demo
The sample app under the app module demonstrates:

- Programmatic creation via PopupWindow

- All customizable options

📝 License  

This project is licensed under the MIT License.

🙌 Contributions  

Issues and pull requests are welcome. Feel free to fork and customize!

