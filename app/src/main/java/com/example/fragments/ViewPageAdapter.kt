package com.example.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return ExampleFragment.newInstance(position + 1)
    }
}