package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hoanganh.drugstore.model.ChangePassUser
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.preference.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_change_pass.*
import kotlinx.android.synthetic.main.fragment_change_pass.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePassFragment : Fragment(),View.OnClickListener {


    var navc: NavController? = null
    private lateinit var viewOfLayout: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewOfLayout =inflater.inflate(R.layout.fragment_change_pass, container, false)
        viewOfLayout.btnCancelChangePass.setOnClickListener(this)
        viewOfLayout.btnConfirmChagnePass.setOnClickListener(this)


        return viewOfLayout
    }

    override fun onClick(v: View?) {
        when(v){
            btnCancelChangePass-> { navc?.navigate(R.id.action_fmChangePass_to_nav_informationUser)}
            btnConfirmChagnePass->{changePass()}
        }
    }



    private fun changePass(){
        val token = SharedPrefManager.getInstance(requireContext()).getToken()
        val type = SharedPrefManager.getInstance(requireContext()).getType()
        val userName = SharedPrefManager.getInstance(requireContext()).getUserName()
        val oldPass = viewOfLayout.edOldPass.text.toString().trim()
        val newPass = viewOfLayout.edNewPassword.text.toString().trim()
        val confirmNewPass = viewOfLayout.edComfirmNewPassword.text.toString().trim()



        if (oldPass.isEmpty()){
            viewOfLayout.edOldPass.error = "Old Password required"
            viewOfLayout.edOldPass.requestFocus()
            return
        }
        if (newPass.isEmpty()){
            viewOfLayout.edNewPassword.error = "Old Password required"
            viewOfLayout.edNewPassword.requestFocus()
            return
        }

        if (newPass.length < 8) {
            edNewPassword.error = "Password  should be  greater than 8 characters "
            edNewPassword.requestFocus()
            return
        }

        if (confirmNewPass != newPass){
            viewOfLayout.edNewPassword.error = "Those passwords didn't match. Try again."
            viewOfLayout.edNewPassword.requestFocus()
            viewOfLayout.edComfirmNewPassword.error = "Those passwords didn't match. Try again."
            viewOfLayout.edComfirmNewPassword.requestFocus()
            return
        }

        RetrofitClient.getApiService().putChangePass("$type  $token",ChangePassUser(userName!!,oldPass,newPass,confirmNewPass)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "Change Password Successfully", Toast.LENGTH_SHORT).show()
                    }
                    SharedPrefManager.getInstance(requireContext()).logOutShare()
                    activity!!.finish()
                } else {
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "Old Password is Wrong", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity!!.runOnUiThread {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

    }


}