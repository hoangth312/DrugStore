package com.hoanganh.drugstore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.models.SlideModel

import com.hoanganh.drugstore.Adapter.CommentAdapter
import com.hoanganh.drugstore.Adapter.ImageTutorialAdapter

import com.hoanganh.drugstore.Adapter.NoteAdapter
import com.hoanganh.drugstore.Adapter.ViewPagerAdapter
import com.hoanganh.drugstore.Model.Clinics
import com.hoanganh.drugstore.Model.Comment

import com.hoanganh.drugstore.R
import kotlinx.android.synthetic.main.fragment_information_of_clinic.*
import kotlinx.android.synthetic.main.fragment_information_of_clinic.view.*
import me.relex.circleindicator.CircleIndicator3
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InformationOfClinicFragment : Fragment() {
    lateinit var viewOfLayout: View
    private val listNote = ArrayList<Clinics>()
    private val listCommentClinics = ArrayList<Comment>()
    private val listImage = ArrayList<SlideModel>()
    private val now = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_information_of_clinic, container, false)
        setupView()
        setData()
        setDataComment()
        setDataBanner()
        return viewOfLayout

    }

    private fun setDataBanner() {
        listImage.add(SlideModel(R.drawable.pharmacy))
        listImage.add(SlideModel(R.drawable.salonpas))
        slider.setImageList(listImage, true)
//       listImage.add(0, R.drawable.pharmacy)
//        listImage.add(1, R.drawable.salonpas)
//        val pagerAdaper = ViewPagerAdapter(listImage)
//        viewOfLayout.viewPagerImageClinic.adapter = pagerAdaper
//        viewOfLayout.viewPagerImageClinic.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        viewOfLayout.circleIn.setViewPager(viewPagerImageClinic)

    }


    private fun setDataComment() {
        viewOfLayout.txtDateCommentClinic.text = now
        viewOfLayout.reviewCommentClinic.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> rating }
        viewOfLayout.btnCommentClinic.setOnClickListener {
            if (edtCommentClinic.text.toString() == "") {
                Toast.makeText(context, "Pls Enter Text", Toast.LENGTH_SHORT).show()
            } else if (reviewCommentClinic.rating.toDouble() == 0.0) {
                Toast.makeText(context, "Pls Enter Rating for you", Toast.LENGTH_SHORT).show()
            } else {
                val cmt = edtCommentClinic.text.toString()
                val currentDate = txtDateCommentClinic.text.toString()
                val rateValue = reviewCommentClinic.rating
                listCommentClinics.add(0, Comment(cmt, "", rateValue, currentDate))
                val listCommentAdapter = CommentAdapter(listCommentClinics)
                viewOfLayout.reCommentClinic.layoutManager = LinearLayoutManager(context)
                viewOfLayout.reCommentClinic.adapter = listCommentAdapter

            }

        }

    }

    private fun setupView() {
        viewOfLayout.txtDetailClinic.text = "Cơ sở có 5 phòng khám, Tim Mạch, Lao Phổi, Răng Hàm Mặt, Cấp Cứu, AAAAAAA"
        viewOfLayout.reNoteClinic.layoutManager = LinearLayoutManager(context)
        val noteAdapter = NoteAdapter(listNote)
//        viewOfLayout.reNoteClinic.setItemViewCacheSize(3)
        viewOfLayout.reNoteClinic.adapter = noteAdapter
    }

    private fun setData() {
        listNote.add(Clinics("VIP room has wifi, fruit, air purification system, private toilet, special bed"))
        listNote.add(Clinics("Fully equipped clinic, doctors with long experience in treating diseases"))
    }

}




