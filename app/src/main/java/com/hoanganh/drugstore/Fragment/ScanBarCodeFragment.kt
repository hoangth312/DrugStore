package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.extension.checkRequiredPermissions
import com.hoanganh.drugstore.model.datasearchdrug.SearchDrugsModel
import com.hoanganh.drugstore.preference.SharedPrefManager
import com.hoanganh.drugstore.utils.InternetConnection
import kotlinx.android.synthetic.main.fragment_change_pass.view.*
import kotlinx.android.synthetic.main.fragment_change_pass.view.btnCancelChangePass
import kotlinx.android.synthetic.main.fragment_change_pass.view.btnConfirmChagnePass
import kotlinx.android.synthetic.main.fragment_scan_barcode.*
import kotlinx.android.synthetic.main.fragment_scan_barcode.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class ScanBarCodeFragment : Fragment(), View.OnClickListener {

    private lateinit var viewOfLayout: View
    private lateinit var codeScanner: CodeScanner
    var navc: NavController? = null
   var idDrug by Delegates.notNull<Int>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_scan_barcode, container, false)


        viewOfLayout.btnMap.setOnClickListener(this)
        viewOfLayout.btnSearchDrugs.setOnClickListener(this)
        viewOfLayout.btnTest.setOnClickListener(this)


        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                getDataDrug(it.text)


            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
            tvTutorial.text = "Please scan barcode inside the frame"
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()

    }

    override fun onPause() {

        super.onPause()
        codeScanner.releaseResources()


    }

    @UiThread
    private fun getDataDrug(qrCode: String){
        var token = SharedPrefManager.getInstance(requireContext()).getToken().toString()
        var type = SharedPrefManager.getInstance(requireContext()).getType().toString()
        if (InternetConnection.checkConnection(requireContext())) {
            RetrofitClient.getApiService().getDrugbyCode("$type  $token", qrCode
                  ).enqueue(object : Callback<SearchDrugsModel> {
                override fun onResponse(call: Call<SearchDrugsModel>, response: Response<SearchDrugsModel>) {
                    if (response.isSuccessful) {
                        activity!!.runOnUiThread {
                            idDrug = response.body()!!.id
                            tvTutorial.text = "Click in frame to continue"
                            navc?.navigate(R.id.action_nav_home_to_fmDrugInfo)
                            val action = ScanBarCodeFragmentDirections.actionNavHomeToFmDrugInfo(idDrug)
                            navc?.navigateUp()
                            navc?.navigate(action)
                        }
                    } else {
                        activity!!.runOnUiThread {
                            Toast.makeText(context, "lỗi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<SearchDrugsModel>, t: Throwable) {
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show()
            }
        }
    }




    override fun onClick(v: View?) {
        when (v) {
            btnMap -> {
                if (this.checkRequiredPermissions()) {
                    navc?.navigate(R.id.action_nav_home_to_activityMap)
                }
            }
            btnSearchDrugs -> {navc?.navigate(R.id.action_nav_home_to_fmSearchDugs)}
            btnTest -> {navc?.navigate(R.id.action_nav_home_to_fmPersonDetail)}


        }
    }
}










