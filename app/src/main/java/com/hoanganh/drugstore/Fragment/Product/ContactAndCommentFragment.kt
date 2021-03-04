package com.hoanganh.drugstore.Fragment.Product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanganh.drugstore.Adapter.EvaluateAdapter
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.extension.CustomEllipsize
import com.hoanganh.drugstore.model.product.EvaluateInit
import com.hoanganh.drugstore.model.product.EvaluateRequest
import com.hoanganh.drugstore.model.product.Product
import com.hoanganh.drugstore.preference.SharedPrefManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_contact_and_comment.*
import kotlinx.android.synthetic.main.fragment_contact_and_comment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ContactAndCommentFragment(getId: Int) : Fragment() {
    private val args = getId
    lateinit var viewOfLayout: View
    private var token = ""
    private var type = ""
    private var listCommentProduct = ArrayList<EvaluateRequest>()
    private val createDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_contact_and_comment, container, false)
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        getContactAndComment(args)
        addComment(args)
        return viewOfLayout
    }

    private fun addComment(args: Int) {
        viewOfLayout.txtDateCommentProduct.text = createDate
        viewOfLayout.btnCommentProduct.setOnClickListener {
            when {
                edtCommentProduct.text.toString() == "" -> {
                    Toasty.error(requireContext(), "Pls Enter Text", Toast.LENGTH_SHORT, true).show()
                }
                else -> {
                    val cmt = edtCommentProduct.text.toString()
                    val currentDate = txtDateCommentProduct.text.toString()
                    val user = SharedPrefManager.getInstance(requireContext()).getID()
                    RetrofitClient.getApiService()
                            .addEvaluates("$type $token", EvaluateInit(cmt, args, user))
                            .enqueue(object : Callback<EvaluateRequest> {
                                override fun onResponse(call: Call<EvaluateRequest>, response: Response<EvaluateRequest>) {
                                    if (response.isSuccessful) {
                                        activity!!.runOnUiThread {
                                            val username = response.body()!!.user
                                            listCommentProduct.add(0, EvaluateRequest(cmt, currentDate, args, username))
                                            getAllComment()
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<EvaluateRequest>, t: Throwable) {
                                }
                            }
                            )
                }
            }
        }
    }


    private fun getContactAndComment(args: Int) {
        RetrofitClient.getApiService().getProducts("$type  $token", args).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    activity!!.runOnUiThread {
                        val array: String = response.body()!!.attention
                        val producer: String = array.split(".\n")[4]
                        viewOfLayout.txtProducer.text = producer
                        val distribution: String = array.split(".\n")[5]
                        viewOfLayout.txtDistribution.text = distribution
                        val hotline: String = array.split(".\n")[6]
                        viewOfLayout.txtHotline.text = hotline

                        listCommentProduct = response.body()!!.evaluates as ArrayList<EvaluateRequest>
                        if (listCommentProduct.size > 0) {
                            getAllComment()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {

            }
        })
    }

    private fun getAllComment() {
        val listCommentAdapter = EvaluateAdapter(listCommentProduct)
        viewOfLayout.rcCommentProduct.layoutManager = LinearLayoutManager(context)
        viewOfLayout.rcCommentProduct.adapter = listCommentAdapter

    }

}