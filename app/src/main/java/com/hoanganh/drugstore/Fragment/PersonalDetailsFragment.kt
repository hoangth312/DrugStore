package com.hoanganh.drugstore.Fragment



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hoanganh.drugstore.Adapter.ImageTutorialAdapter
import com.hoanganh.drugstore.R

import kotlinx.android.synthetic.main.fragment_personal_details.view.*

class PersonalDetailsFragment() : Fragment() {
      private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_personal_details, container, false)
        setUpViewPager()
        return viewOfLayout
    }


    private fun setUpViewPager() {
        val imgView = listOf<Int>(R.drawable.image10,R.drawable.image11,R.drawable.image12,R.drawable.image13,R.drawable.image14 )
        val adapter = ImageTutorialAdapter(context, imgView  )
        viewOfLayout.viewPagerImage.adapter = adapter
        }

}