package com.hoanganh.drugstore.Fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.hoanganh.drugstore.Adapter.ImageTutorialAdapter
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.api.RetrofitClient
import com.hoanganh.drugstore.model.HexColorModel
import com.hoanganh.drugstore.model.ResultHexModel
import com.hoanganh.drugstore.preference.SharedPrefManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_personal_details.*
import kotlinx.android.synthetic.main.fragment_personal_details.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PersonalDetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var viewOfLayout: View

    var navc: NavController? = null
    var a = 0
    private lateinit var photoURI: Uri
    private lateinit var currentPhotoPath: String
    private var text1 = ""
    private var text2 = ""
    private var text3 = ""
  



    companion object {
        private const val requestCode = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_personal_details, container, false)

        setUpViewPager()

        viewOfLayout.btnCamera.setOnClickListener(this)
        viewOfLayout.btnCancel.setOnClickListener(this)
        viewOfLayout.btnDone.setOnClickListener(this)
        viewOfLayout.btnDontSave.setOnClickListener(this)
        viewOfLayout.test1.setOnClickListener(this)
        viewOfLayout.test2.setOnClickListener(this)
        viewOfLayout.test3.setOnClickListener(this)

        viewOfLayout.imvResult.setOnTouchListener(OnTouchListener { v, event ->

            try {
                viewOfLayout.imvResult.isDrawingCacheEnabled = true
                val ibm: Bitmap = Bitmap.createBitmap(viewOfLayout.imvResult.drawingCache)
                viewOfLayout.imvResult.isDrawingCacheEnabled = false
                val pxl = ibm.getPixel(event.x.toInt(), event.y.toInt())
                val hex = "#" + Integer.toHexString(pxl).substring(2)


                when (a) {
                    0 -> {
                        test1.setBackgroundColor((Color.parseColor(hex)))
                        text1 = hex
                    }

                    1 -> {
                        test2.setBackgroundColor((Color.parseColor(hex)))
                        text2 = hex
                    }
                    2 ->{
                        test3.setBackgroundColor((Color.parseColor(hex)))
                        text3 = hex
                    }
                }
                Log.d("text1", text1)
                Log.d("text2", text2)
                Log.d("text3", text3)
                ibm.recycle()

            } catch (ignore: Exception) {
                Log.d("123", ignore.toString())
            }
            true
        })

        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnCamera -> {

                viewOfLayout.viewPagerImage.visibility = View.INVISIBLE
                viewOfLayout.btnCancel.visibility = View.INVISIBLE
                viewOfLayout.btnCamera.visibility = View.INVISIBLE
                viewOfLayout.imvResult.visibility = View.VISIBLE
                viewOfLayout.btnDone.visibility = View.VISIBLE
                viewOfLayout.btnDontSave.visibility = View.VISIBLE
                viewOfLayout.test1.visibility = View.VISIBLE
                viewOfLayout.test2.visibility = View.VISIBLE
                viewOfLayout.test3.visibility = View.VISIBLE

                dispatchTakePictureIntent()
            }

            btnCancel -> navc!!.popBackStack()
            test1 -> a = 0
            test2 -> a = 1
            test3 -> a = 2
            btnDone -> updateHex()
            btnDontSave -> navc!!.navigate(R.id.action_fmPersonDetail_self)




        }


    }

    private fun updateHex(){
        val token = SharedPrefManager.getInstance(requireContext()).getToken()
        val type = SharedPrefManager.getInstance(requireContext()).getType()
        val idUser = SharedPrefManager.getInstance(requireContext()).getID()


        RetrofitClient.getApiService().postHexColor("$type  $token",idUser, HexColorModel(listOf(text1,text2,text3) )).enqueue(object : Callback<ResultHexModel> {
            override fun onResponse(call: Call<ResultHexModel>, response: Response<ResultHexModel>) {
                activity!!.runOnUiThread {
                    if (response.code() == 200) {
                        Log.d("haha", response.body().toString())
                    } else {
                        Toasty.error(context!!, "haha", Toast.LENGTH_SHORT, true).show()
                        Log.d("haha", response.code().toString())
                    }

                }
            }

            override fun onFailure(call: Call<ResultHexModel>, t: Throwable) {
                activity!!.runOnUiThread {
                    Toasty.error(context!!, "onFailure", Toast.LENGTH_SHORT, true).show()
                }
            }
        })

    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val dir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera")
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                dir
        ).apply {

            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {

                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                            requireContext(),
                            "com.hoanganh.drugstore.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestCode)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestCode && resultCode == RESULT_OK) {
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, photoURI);
            rotateImage(bitmap)
        }
    }

    private fun rotateImage(bitmap: Bitmap) {
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(currentPhotoPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        }
        var rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        viewOfLayout.imvResult.setImageBitmap(rotateBitmap)

    }



    private fun setUpViewPager() {
        val imgView = listOf(
                R.drawable.image10,
                R.drawable.image11,
                R.drawable.image12,
                R.drawable.image13,
                R.drawable.image14
        )
        val adapter = ImageTutorialAdapter(context, imgView)
        viewOfLayout.viewPagerImage.adapter = adapter
    }


}