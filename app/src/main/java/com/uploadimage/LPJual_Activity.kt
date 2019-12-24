package com.uploadimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lpjual_.*

class LPJual_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lpjual_)
        //actionbar
        val actionbar = supportActionBar
        actionbar?.title = "Laporan Penjualan"

        //setbackbutton
        actionbar?.setDisplayHomeAsUpEnabled(true)

    }
}
