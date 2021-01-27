package com.hoanganh.drugstore.Fragment

import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.camera_demo.CameraCaptureListener
import com.example.camera_demo.ImageAvailableListener
import com.hoanganh.drugstore.Adapter.ImageTutorialAdapter
import com.hoanganh.drugstore.R
import com.hoanganh.drugstore.`interface`.CameraStateCallback
import com.hoanganh.drugstore.utils.CaptureStateCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_personal_details.*
import kotlinx.android.synthetic.main.fragment_personal_details.view.*
import java.nio.ByteBuffer

class PersonalDetailsFragment : Fragment() , TextureView.SurfaceTextureListener
, View.OnClickListener{
    private lateinit var viewOfLayout: View
    private lateinit var cameraId: String
    private lateinit var cameraHandler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var captureRequest: CaptureRequest.Builder
    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraCaptureSession: CameraCaptureSession
    var navc: NavController? = null


    private val stateCallback =
            CameraStateCallback { cameraDevice -> startCameraPreview(cameraDevice) }

    private val captureStateCallback =
            CaptureStateCallback { cameraSession -> updatePreview(cameraSession) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_personal_details, container, false)

        setUpViewPager()

       viewOfLayout.btnCamera.setOnClickListener(this)
       viewOfLayout.btnCancel.setOnClickListener(this)
       viewOfLayout.btnClickShot.setOnClickListener(this)
       viewOfLayout.btnCancelCamera.setOnClickListener(this)
       viewOfLayout.btnOkSaveImage.setOnClickListener(this)
       viewOfLayout.btnDontSaveImage.setOnClickListener(this)

        return viewOfLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
    }
    override fun onResume() {
        super.onResume()
        startCameraThread()
    }
    override fun onClick(v: View?) {
        when (v){
            btnCamera -> {
                viewOfLayout.viewPagerImage.visibility = View.INVISIBLE
                viewOfLayout.textureView.visibility = View.VISIBLE
                viewOfLayout.btnCancel.visibility = View.INVISIBLE
                viewOfLayout.btnCamera.visibility = View.INVISIBLE
                viewOfLayout.btnClickShot.visibility = View.VISIBLE
                viewOfLayout.btnCancelCamera.visibility = View.VISIBLE
                viewOfLayout.textureView.surfaceTextureListener = this

            }
            btnCancel -> navc!!.popBackStack()
            btnClickShot->{
                captureImage()
                viewOfLayout.btnCancelCamera.visibility = View.INVISIBLE
                viewOfLayout.btnClickShot.visibility = View.INVISIBLE
                viewOfLayout.btnOkSaveImage.visibility = View.VISIBLE
                viewOfLayout.btnDontSaveImage.visibility = View.VISIBLE

            }
            btnCancelCamera->{
                viewOfLayout.viewPagerImage.visibility = View.VISIBLE
                viewOfLayout.textureView.visibility = View.INVISIBLE
                viewOfLayout.btnCancel.visibility = View.VISIBLE
                viewOfLayout.btnCamera.visibility = View.VISIBLE
                viewOfLayout.btnClickShot.visibility = View.INVISIBLE
                viewOfLayout.btnCancelCamera.visibility = View.INVISIBLE
            }
            btnOkSaveImage->{}
            btnDontSaveImage->{
                viewOfLayout.btnCancelCamera.visibility = View.VISIBLE
                viewOfLayout.btnClickShot.visibility = View.VISIBLE
                viewOfLayout.btnOkSaveImage.visibility = View.INVISIBLE
                viewOfLayout.btnDontSaveImage.visibility = View.INVISIBLE
                startCameraPreview(cameraDevice)
            }
        }

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

    private fun requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), CAMERA) != PackageManager.PERMISSION_GRANTED
        ) { ActivityCompat.requestPermissions(requireActivity(), arrayOf(CAMERA), 1
        )
        }
    }


    private fun prepareCamera() {
        val cameraManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]
        requestCameraPermission()
        if (ActivityCompat.checkSelfPermission(requireActivity(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraManager.openCamera(cameraId, stateCallback, null)
        }
    }
    private fun startCameraPreview(cameraDevice: CameraDevice) {
        this.cameraDevice = cameraDevice
        val surfaceTexture = viewOfLayout.textureView.surfaceTexture
        val surface = Surface(surfaceTexture)
        captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequest.addTarget(surface)
        cameraDevice.createCaptureSession(listOf(surface), captureStateCallback, null)
    }
    private fun updatePreview(session: CameraCaptureSession) {
        captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        session.setRepeatingRequest(captureRequest.build(), null, cameraHandler)
        cameraCaptureSession = session
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        prepareCamera()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = false
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }
    private fun startCameraThread() {
        handlerThread = HandlerThread("THREAD_NAME")
        handlerThread.start()
        cameraHandler = Handler(handlerThread.looper)
    }

    private fun captureImage() {
        val imageReader = ImageReader.newInstance(400, 400, ImageFormat.JPEG, 1)
        val outputSurfaces =
                mutableListOf<Surface>(imageReader.surface)
        val captureRequest =
                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequest.addTarget(imageReader.surface)
        captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        val imageAvailableListener = ImageAvailableListener { reader ->
            displayImage(reader.acquireLatestImage())
//            startCameraPreview(cameraDevice)
        }
        imageReader.setOnImageAvailableListener(imageAvailableListener, null)
        cameraDevice.createCaptureSession(
                outputSurfaces,
                CaptureStateCallback { cameraSession ->
                    cameraSession.capture(
                            captureRequest.build(),
                            CameraCaptureListener(),
                            cameraHandler
                    )
                },
                cameraHandler
        )
    }

    private fun displayImage(outputImage: Image) {
        val buffer: ByteBuffer = outputImage.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.position(0)
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
        viewOfLayout.imvResult.setImageBitmap(bitmap)
        viewOfLayout.imvResult.rotation = 90f
    }

}