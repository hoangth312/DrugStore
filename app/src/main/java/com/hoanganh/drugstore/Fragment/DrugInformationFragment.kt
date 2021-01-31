package com.hoanganh.drugstore.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoanganh.drugstore.Adapter.SimilarProductAdapter
import com.hoanganh.drugstore.Adapter.ViewPagerProductAdapter
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.model.Like
import com.hoanganh.drugstore.model.product.LikeRequest
import com.hoanganh.drugstore.model.product.Product
import com.hoanganh.drugstore.preference.SharedPrefManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_drug_info.view.*
import kotlinx.android.synthetic.main.fragment_drug_information.*
import kotlinx.android.synthetic.main.fragment_drug_information.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class DrugInformationFragment : DialogFragment() {
    private var isLiked: Boolean = false
    private var totalLike by Delegates.notNull<Int>()
    private var idEvaluate by Delegates.notNull<Int>()
    private var idProduct by Delegates.notNull<Int>()
    private var args by Delegates.notNull<Int>()
    private lateinit var viewOfLayout: View
    private var categoryName = ""
    private var productList: ArrayList<Product> = ArrayList()
    private var token = ""
    private var type = ""
    var similarAdapter: SimilarProductAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.FullScreenDialogStyle
        );
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_drug_information, container, false)
        token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        args = DrugInformationFragmentArgs.fromBundle(requireArguments()).sendIdDrug
        getData()
        handleClickLikeButton()
//        getLikeSharePreference()
        return viewOfLayout
    }

//    private fun getLikeSharePreference() {
//        val sharedPreferences = requireActivity().getSharedPreferences("Like", Context.MODE_PRIVATE)
//        isLiked = sharedPreferences.getBoolean("saveLike", false)
//        checkLike(isLiked)
//    }
//
//    private fun saveLikeSharePreference(isLiked: Boolean) {
//        val sharedPreferences = requireActivity().getSharedPreferences("Like", Context.MODE_PRIVATE)
//        val status = sharedPreferences.edit()
//        status.putBoolean("saveLike", isLiked)
//        status.apply()
//    }

//    private fun checkLike(isLiked: Boolean) {
//        when(isLiked){
//            true -> viewOfLayout.imgUnlike.setImageResource(R.drawable.ic_like)
//            false -> viewOfLayout.imgUnlike.setImageResource(R.drawable.ic_unlike)
//        }
//    }

    private fun handleClickLikeButton() {
        viewOfLayout.imgUnlike.setOnClickListener {
            val user = SharedPrefManager.getInstance(requireContext()).getID()
            if (!isLiked) {
                isLiked = true
                viewOfLayout.imgUnlike.setImageResource(R.drawable.ic_like)
                viewOfLayout.txtNumberLike.text = (totalLike + 1).toString()
                addLike(user, true)
 //               saveLikeSharePreference(true)
                Log.e("ptest", "onLikeClick true")

            } else {
                isLiked = false
                viewOfLayout.imgUnlike.setImageResource(R.drawable.ic_unlike)
                viewOfLayout.txtNumberLike.text = totalLike.toString()
                updateLike(false, idEvaluate)
 //               saveLikeSharePreference(false)
                Log.e("ptest", "onLikeClick false")
            }
        }
    }


    private fun addLike(user: Int, isLike: Boolean) {
        RetrofitClient
                .getApiService()
                .addLike("$type  $token", Like(likes = isLike, args, user))
                .enqueue(object : Callback<LikeRequest> {
                    override fun onResponse(call: Call<LikeRequest>, response: Response<LikeRequest>) {
                        activity!!.runOnUiThread {
                            idEvaluate = response.body()!!.id

                        }
                    }

                    override fun onFailure(call: Call<LikeRequest>, t: Throwable) {
                    }
                })
    }

    private fun updateLike(isLike: Boolean, idEvaluate: Int) {
        RetrofitClient
                .getApiService()
                .updateLike("$type  $token", LikeRequest(idEvaluate, likes = isLike))
                .enqueue(object : Callback<LikeRequest> {
                    override fun onResponse(call: Call<LikeRequest>, response: Response<LikeRequest>) {
                        activity!!.runOnUiThread() {
                        }
                    }

                    override fun onFailure(call: Call<LikeRequest>, t: Throwable) {
                    }
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager(args)

    }

    private fun viewPager(args: Int) {
        viewOfLayout.viewPagerProduct.adapter = ViewPagerProductAdapter(childFragmentManager, args)
        viewOfLayout.tabLayout.setupWithViewPager(viewPagerProduct)

    }

    private fun getData() {
        RetrofitClient.getApiService().getProducts("$type  $token", args).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.code() == 200) {
                    activity!!.runOnUiThread {
                        viewOfLayout.txtNameMedicine.text = response.body()!!.vietnameseName
                        viewOfLayout.textView2.text = response.body()!!.description
                        val listImageProduct = response.body()!!.imageProducts
                        Picasso.get().load(listImageProduct.get(0)).into(viewOfLayout.imgMedicine)
                        viewOfLayout.tvPrice.text = response.body()!!.price.toString() + " VND(" + response.body()!!.priceTax.toString() + " VNĐ)"
                        viewOfLayout.txtNumberLike.text = response.body()!!.likeTotal.toString()
                        totalLike = response.body()!!.likeTotal
                        viewOfLayout.txtCommentPd.text = response.body()!!.commentTotal.toString()
                        categoryName = response.body()!!.categoryName
                        getListProduct(categoryName)

                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {

            }
        })

    }


    private fun getListProduct(categoryName: String) {
        RetrofitClient.getApiService().getListSP("$type  $token", categoryName).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    productList = response.body() as ArrayList<Product>
                    activity!!.runOnUiThread {
                        if (productList.size == 0) {
                            Toast.makeText(context, "Khong co san pham lien quan", Toast.LENGTH_SHORT).show()
                        }
                        setupView()
                    }
                } else
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "Loi", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {

            }
        })
    }

    private fun setupView() {
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewOfLayout.reSimilarProduct.layoutManager = linearLayoutManager
        similarAdapter = SimilarProductAdapter(productList, onItemClick = this::onCellClickListener)
        viewOfLayout.reSimilarProduct.adapter = similarAdapter

    }

    private fun onCellClickListener(products: Product) {
          val args = products.id
            getDataId(args)
    }

    private fun getDataId(args: Int) {
        RetrofitClient.getApiService().getProducts("$type  $token", args).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.code() == 200) {
                    activity!!.runOnUiThread {
                        viewOfLayout.txtNameMedicine.text = response.body()!!.vietnameseName
                        viewOfLayout.textView2.text = response.body()!!.description
                        val listImageProduct = response.body()!!.imageProducts
                        Picasso.get().load(listImageProduct.get(0)).into(viewOfLayout.imgMedicine)
                        viewOfLayout.tvPrice.text = response.body()!!.price.toString() + " VND(" + response.body()!!.priceTax.toString() + " VNĐ)"
                        viewOfLayout.txtNumberLike.text = response.body()!!.likeTotal.toString()
                        totalLike = response.body()!!.likeTotal
                        viewOfLayout.txtCommentPd.text = response.body()!!.commentTotal.toString()
                        categoryName = response.body()!!.categoryName
                        getListProduct(categoryName)

                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {

            }
        })

    }
}





