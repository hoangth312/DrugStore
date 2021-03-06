package com.hoanganh.drugstore.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.model.LoginResponse
import com.hoanganh.drugstore.model.User
import com.hoanganh.drugstore.preference.SharedPrefManager
import com.hoanganh.drugstore.utils.InternetConnection
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), View.OnClickListener {

    var emailUser = ""
    var password = ""
    var dialog: AlertDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
        tvForgotPass.setOnClickListener(this)


    }

    override fun onStart() {

        super.onStart()

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {

                    }


                }).check()
        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            val intent = Intent(applicationContext, ScanBarCodeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

        }

    }


    override fun onClick(v: View?) {
        when (v) {
            btnLogin -> {
           loginAccount()
//              startActivity(Intent(this@MainActivity, ScanBarCodeActivity::class.java))
            }
            btnSignUp -> {
                startActivity(Intent(applicationContext, RegisterActivity::class.java))
            }
            tvForgotPass -> {
                startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java))
            }
        }
    }



    private fun diaLogLoading() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
    }


    private fun loginAccount() {
        emailUser = edEmailUser.text.toString().trim()
        password = edPassword.text.toString().trim()

        if (emailUser.isEmpty()) {
            edEmailUser.error = "User required"
            edEmailUser.requestFocus()
            return
        }
        if (password.isEmpty()) {
            edPassword.error = "Password required"
            edPassword.requestFocus()
            return
        }
        if (InternetConnection.checkConnection(applicationContext)) {
            diaLogLoading()
            dialog!!.show()


            RetrofitClient.getApiService().userLogin(LoginResponse(emailUser, password)).enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    dialog!!.dismiss()
                    runOnUiThread {
                        Toasty.error(applicationContext, t.message.toString(), Toast.LENGTH_SHORT, true).show()
                    }
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    dialog!!.dismiss()
                    if (response.isSuccessful) {
                        SharedPrefManager.getInstance(this@MainActivity).saveUser(response.body()!!.copy())
                        val intent = Intent(this@MainActivity, ScanBarCodeActivity::class.java)
                        startActivity(intent)
                        runOnUiThread {
                            Toasty.success(applicationContext, "Login Success!", Toast.LENGTH_SHORT, true).show()
                        }
                    } else {
                        runOnUiThread {

                            Toasty.error(applicationContext, "User Name or Password is Wrong!", Toast.LENGTH_SHORT, true).show()
                        }

                    }
                }
            })
        } else {
            runOnUiThread {
                Toasty.error(applicationContext, "Internet Connection Not Available", Toast.LENGTH_SHORT, true).show();
            }
        }
    }




}








