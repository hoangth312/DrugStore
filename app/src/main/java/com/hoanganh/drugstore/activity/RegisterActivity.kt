package com.hoanganh.drugstore.activity

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hoanganh.drugstore.model.RegisterAccount
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(),View.OnClickListener {
    private var role = listOf("ROLE_USER")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnCancelRegister.setOnClickListener(this)
        btnOkRegister.setOnClickListener(this)


    }
    override fun onClick(v: View?) {
        when(v){
            btnCancelRegister ->  finish()


            btnOkRegister -> userSignUp()
        }
    }

    private fun userSignUp() {
        val firstName: String = edFristName.text.toString().trim()
        val lastName: String = edLastName.text.toString().trim()
        val userName: String = edUserNameRegister.text.toString().trim()
        val email: String = edEmailRegister.text.toString().trim()
        val password: String = edPassRegister.text.toString().trim()
        val confirmPassword: String = edConfirmPassRegister.text.toString().trim()

        if (firstName.isEmpty()) {
            edFristName.error = "First Name required"
            edFristName.requestFocus()
            return
        }
        if (lastName.isEmpty()) {
            edLastName.error = "Last Name required"
            edLastName.requestFocus()
            return
        }
        if (userName.isEmpty()) {
            edUserNameRegister.error = "Name required"
            edUserNameRegister.requestFocus()
            return
        }
        if (email.isEmpty()) {
            edEmailRegister.error = "Email entry required"
            edEmailRegister.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmailRegister.error = "Enter a valid email"
            edEmailRegister.requestFocus()
            return
        }
        if (password.isEmpty()) {
            edPassRegister.error = "Password required"
            edPassRegister.requestFocus()
            return

        }
        if (password.length < 8) {
            edPassRegister.error = "Password  should be  greater than 8 characters "
            edPassRegister.requestFocus()
            return
        }
        if (confirmPassword != password) {
            edConfirmPassRegister.error = "Those passwords didn't match. Try again."
            edConfirmPassRegister.requestFocus()
            return

        }
        RetrofitClient
                .getApiService()
                .signUp(RegisterAccount(email,firstName,lastName,password,role,userName))
        .enqueue(object : Callback<RegisterAccount> {
            override fun onResponse(call: Call<RegisterAccount?>, response: Response<RegisterAccount?>) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toasty.success(this@RegisterActivity, "User registered successfully", Toast.LENGTH_SHORT, true).show()
                    }
//
                    finish()
                }
            }

            override fun onFailure(call: Call<RegisterAccount>, t: Throwable) {
                runOnUiThread {
                    Toasty.error(this@RegisterActivity, t.message.toString(), Toast.LENGTH_SHORT, true).show()

                }
                tvNoty.text = "Registration failed"
                tvNoty.visibility = View.VISIBLE
            }
        })
    }



}