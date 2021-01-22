package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_drug_information.view.*

class DrugInformationFragment : DialogFragment() {

    private var args = ""
    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.FullScreenDialogStyle
        );
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_drug_information, container, false)

        args = DrugInformationFragmentArgs.fromBundle(requireArguments()).sendIdDrug
        viewOfLayout.tvPrice.text = args

            return viewOfLayout
        }
    }





