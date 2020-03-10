package com.example.firebaseauthentication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

// ViewPageAdapter will deal FragmentManager
// FragmentManager is an Interface interacting with Fragment's object

class ViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        val fragment: Fragment? = if (position == 0)
            SignInFragment()
        else if (position == 1)
            SignUpFragment()
        else
            null

        return fragment!!
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        val title: String? = if (position == 0)
            "Sign In"
        else if (position == 1)
            "Sign Up"
        else
            null
        return title
    }
}