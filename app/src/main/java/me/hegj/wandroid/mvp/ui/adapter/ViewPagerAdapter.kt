package me.hegj.wandroid.mvp.ui.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by hackware on 2016/9/10.
 */

class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<SupportFragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(i: Int): Fragment {
        return fragments[i]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}
