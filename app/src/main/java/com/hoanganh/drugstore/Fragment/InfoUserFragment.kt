package com.hoanganh.drugstore.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.preference.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_info_user.*
import kotlinx.android.synthetic.main.fragment_info_user.view.*

class InfoUserFragment : Fragment(), View.OnClickListener{

    private lateinit var viewOfLayout:View

    var navc: NavController? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_info_user, container, false)
        setData()
        viewOfLayout.ctPass.setOnClickListener(this)
        return viewOfLayout
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
    }

    override fun onClick(v: View?) {
        when (v){
            ctPass -> {
                navc?.navigate(R.id.action_nav_informationUser_to_fmChangePass)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setData(){
        viewOfLayout.tvNameSetting.text = SharedPrefManager.getInstance(requireContext()).getFirstName() +" "+ SharedPrefManager.getInstance(requireContext()).getLastName()
        viewOfLayout.tvUserNameSetting.text = SharedPrefManager.getInstance(requireContext()).getUserName()
        viewOfLayout.tvEmailUserSetting.text = SharedPrefManager.getInstance(requireContext()).getEmail()

    }

}