package com.example.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

// ViewPageAdapter will deal FragmentManager
// FragmentManager is an Interface interacting with Fragment's object

class ViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        val fragment: Fragment? = if (position == 0)
            FragmentChat()
        else if (position == 1)
            FragmentStatus()
        else if (position == 2)
            FragmentCall()
        else
            null

        return fragment!!
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        val title: String? = if (position == 0)
            "CHATS"
        else if (position == 1)
            "STATUS"
        else if (position == 2)
            "CALLS"
        else
            null

        return title
    }
}