package io.yoba.homework3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children

class MainActivity : AppCompatActivity() {
    private val topCustomViewGroup by lazy {
        findViewById<CustomViewGroup>(R.id.top_custom_view_group)
    }

    private val bottomCustomViewGroup by lazy {
        findViewById<CustomViewGroup>(R.id.bottom_custom_view_group)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (topCustomViewGroup.children + bottomCustomViewGroup.children).forEach { chip ->
            chip.setOnClickListener {
                val parentViewGroup = it.parent as CustomViewGroup

                val oppositeViewGroup = if (parentViewGroup == topCustomViewGroup) {
                    bottomCustomViewGroup
                } else {
                    topCustomViewGroup
                }

                parentViewGroup.removeView(it)
                oppositeViewGroup.addView(it)
            }
        }
    }
}
