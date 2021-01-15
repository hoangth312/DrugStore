package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hoanganh.drugstore.R

class DrugInformationFragment : Fragment() {
    lateinit var viewOfLayout: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_drug_information, container, false)
        return viewOfLayout
    }
}
