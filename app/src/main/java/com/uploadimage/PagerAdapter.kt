package com.uploadimage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private  val pages = listOf(
        ShowclientFragment(),
        ShowdpFragment()
    )

    override fun getItem(position: Int): Fragment {
        return  pages[position]
    }

    override fun getCount(): Int {
        return  pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return  when(position){
            0 -> "Laporan SPG"
            else -> "Data Produk"
        }
    }
}