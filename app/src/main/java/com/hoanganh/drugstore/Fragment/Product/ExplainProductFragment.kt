package com.hoanganh.drugstore.Fragment.Product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.extension.CustomEllipsize
import com.hoanganh.drugstore.model.product.Product
import com.hoanganh.drugstore.preference.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_explain_product.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExplainProductFragment(getId: Int) : Fragment() {
    private val args = getId
    lateinit var viewOfLayout: View
    private var token = ""
    private var type = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_explain_product, container, false)
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()

        getDataExplain(args)

        return viewOfLayout
    }

    private fun getDataExplain(args: Int) {

        RetrofitClient.getApiService().getProducts("$type  $token", args).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    activity!!.runOnUiThread {
                        viewOfLayout.txtExplain.text = response.body()!!.manual
                            CustomEllipsize(viewOfLayout.txtExplain, 1, "...")
                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}