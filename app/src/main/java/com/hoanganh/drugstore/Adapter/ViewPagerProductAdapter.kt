package com.hoanganh.drugstore.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hoanganh.drugstore.Fragment.Product.ContactAndCommentFragment
import com.hoanganh.drugstore.Fragment.Product.ExplainProductFragment
import com.hoanganh.drugstore.Fragment.Product.InformationProductFragment


class ViewPagerProductAdapter(fm: FragmentManager, args: Int) : FragmentPagerAdapter(fm) {
    val getId = args

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                ExplainProductFragment(getId)
            }
            1-> {
                InformationProductFragment(getId)
            }
            2 -> {
                ContactAndCommentFragment(getId)
            }
            else -> {
                ExplainProductFragment(getId)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {return "Explain"}
            1 -> {return "Information"}
            2 -> {return "Contact"}
        }
        return super.getPageTitle(position)
    }
    override fun getCount(): Int {
        return 3
    }


}