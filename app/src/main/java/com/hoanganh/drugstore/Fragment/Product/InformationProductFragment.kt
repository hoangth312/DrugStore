package com.hoanganh.drugstore.Fragment.Product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.model.product.Product
import com.hoanganh.drugstore.preference.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_detail_product.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationProductFragment(getId: Int) : Fragment() {
    private val args = getId
    private var token = ""
    private var type = ""
    lateinit var viewOfLayout: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_detail_product, container, false)
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        getInformation(args)
        return viewOfLayout
    }

    private fun getInformation(args: Int) {
        RetrofitClient.getApiService().getProducts("$type  $token", args).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    activity!!.runOnUiThread {
                        val array : String = response.body()!!.attention
                        val a : String = array.split(".\n")[0]
                        viewOfLayout.txtAttention.text = a
                        val b : String = array.split(".\n")[1]
                        viewOfLayout.txtIngredient.text = b
                        val c : String = array.split(".\n")[2]
                        viewOfLayout.txtNoteProduct.text = c
                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}
