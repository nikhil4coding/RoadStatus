package com.roadstatus.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.roadstatus.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = RoadStatusFragment.newInstance()
        supportFragmentManager.commitNow {
            replace(
                R.id.rootView,
                fragment,
                RoadStatusFragment.TAG
            )
        }
    }
}