package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback

import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_scan_barcode.view.*


class ScanBarCodeFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var codeScanner: CodeScanner
    var navc: NavController ? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        viewOfLayout = inflater.inflate(R.layout.fragment_scan_barcode, container, false)




        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        navc = Navigation.findNavController(view)

        viewOfLayout.btnMap.setOnClickListener{
                navc?.navigate(R.id.action_nav_home_to_fmMap)
        }

        viewOfLayout.btnSearchDrugs.setOnClickListener{
            navc?.navigate(R.id.action_nav_home_to_fmSearchDugs)
        }

        viewOfLayout.btnTest.setOnClickListener{
            navc?.navigate(R.id.action_nav_home_to_fmPersonDetail)
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

}








