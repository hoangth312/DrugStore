package com.hoanganh.drugstore.Fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.model.User
import com.hoanganh.drugstore.model.updateAvatarUser
import com.hoanganh.drugstore.preference.SharedPrefManager
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.fragment_info_user.*
import kotlinx.android.synthetic.main.fragment_info_user.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoUserFragment : Fragment(), View.OnClickListener{

    private lateinit var viewOfLayout:View

    var navc: NavController? = null
    var token: String = ""
    var type: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var userName: String = ""
    var emailUser: String = ""
    var idUser: Int = 0


    var dialog: AlertDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_info_user, container, false)
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        idUser = SharedPrefManager.getInstance(requireContext()).getID()
        setData()
        viewOfLayout.ctPass.setOnClickListener(this)
        viewOfLayout.tvChangeAvatar.setOnClickListener(this)
        diaLogLoading()
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
            tvChangeAvatar -> {
                selectAvatar()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setData(){
        firstName = SharedPrefManager.getInstance(requireContext()).getFirstName()!!
        lastName =  SharedPrefManager.getInstance(requireContext()).getLastName()!!
        userName = SharedPrefManager.getInstance(requireContext()).getUserName()!!
        emailUser = SharedPrefManager.getInstance(requireContext()).getEmail()!!
        viewOfLayout.tvNameSetting.text =  "$firstName $lastName"
        viewOfLayout.tvUserNameSetting.text = userName
        viewOfLayout.tvEmailUserSetting.text = emailUser

    }

    private fun selectAvatar(){


        TedBottomPicker.with(requireActivity())
                .show {
                    uploadFileAvatar(it)


                }
    }

    private fun uploadFileAvatar(it: Uri){

        val avatar = it.toFile()
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), avatar)
        val body = MultipartBody.Part.createFormData("file", avatar.name, requestBody)
        dialog!!.show()
        RetrofitClient.getApiService().postFileAvatar("$type  $token", body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    activity!!.runOnUiThread() {
                        val a = response.body()!!.string()
                        upAvatarUser(a,it)

                    }
                } else {
                    dialog!!.dismiss()
                    activity!!.runOnUiThread() {
                        Toast.makeText(context, "else", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dialog!!.dismiss()
                activity!!.runOnUiThread() {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

private fun upAvatarUser(avatar: String, it: Uri){
    RetrofitClient.getApiService().putAvatarUser("$type  $token", updateAvatarUser(idUser,avatar)).enqueue(object : Callback<Void>{
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.code() == 200) {
                activity!!.runOnUiThread() {
                    dialog!!.dismiss()
                    viewOfLayout.imAvatarSetting.setImageURI(it)
                    SharedPrefManager.getInstance(requireContext()).saveUser(User(emailUser,idUser,token,type,userName,firstName,lastName,avatar))
                    Toast.makeText(context, "Change Avatar Success", Toast.LENGTH_SHORT).show()

                }
            } else {
                dialog!!.dismiss()
                activity!!.runOnUiThread() {
                    Toast.makeText(context, "else1", Toast.LENGTH_SHORT).show()
                }
            }

        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            dialog!!.dismiss()
            activity!!.runOnUiThread() {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        }

    })



}

    private fun diaLogLoading() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
    }

}