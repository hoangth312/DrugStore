package com.hoanganh.drugstore.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.utils.InternetConnection
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity(),View.OnClickListener {
    var dialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        btnConfirmForgotPass.setOnClickListener(this)
        btnBackToHome.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v){
            btnConfirmForgotPass -> {
                    sendMail()

            }
            btnBackToHome ->{finish()}
        }
    }

    private fun diaLogLoading() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
    }

    private fun sendMail(){
        var emailUserInput = edEmailForgotPass.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailUserInput).matches()) {
            edEmailForgotPass.error = "Enter a valid email"
            edEmailForgotPass.requestFocus()
            return
        }
        if (InternetConnection.checkConnection(applicationContext)) {
            diaLogLoading()
            dialog!!.show()

        RetrofitClient.getApiService().forgotPassword(emailUserInput).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    dialog!!.dismiss()
                    edEmailForgotPass.visibility = View.INVISIBLE
                    subTitleForgotPass.text = getString(R.string.notification_change_pass_success)
                    subTitleForgotPass.setTextColor(ContextCompat.getColor(this@ForgotPasswordActivity, R.color.red))
                    btnBackToHome.visibility = View.VISIBLE
                    btnConfirmForgotPass.visibility = View.INVISIBLE

                } else {
                    dialog!!.dismiss()
                    runOnUiThread {
                        Toasty.error(this@ForgotPasswordActivity, " Your search did not return any results. Please try again with other information.", Toast.LENGTH_SHORT, true).show();

                    }
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
               runOnUiThread {
                   Toasty.error(this@ForgotPasswordActivity, t.message.toString(), Toast.LENGTH_SHORT, true).show();


                }
            }
        })

    } else {
        runOnUiThread {
            Toasty.error(this@ForgotPasswordActivity, "Internet Connection Not Available", Toast.LENGTH_SHORT, true).show();
            }
        }
    }
}