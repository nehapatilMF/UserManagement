package com.example.usermanagementapp.activities

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.usermanagementapp.databinding.ActivityDetailBinding
import com.example.usermanagementapp.models.Model
class DetailActivity : AppCompatActivity() {

    private var binding : ActivityDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val toolbarDetail = binding?.toolbarDetails
        val ivPlaceImage = binding?.imageView
        val tvMobileNumber = binding?.phone
        val tvLocation = binding?.address
        val name = binding?.name
        val dob = binding?.dob
        var detailModel: Model? = null

        if (intent.hasExtra(MainActivity.EXTRA_DETAILS)) {
            detailModel =
                intent.getSerializableExtra(MainActivity.EXTRA_DETAILS) as Model
        }

        if (detailModel != null) {

            setSupportActionBar(toolbarDetail)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = null

            toolbarDetail?.setNavigationOnClickListener {
                onBackPressed()
            }
            ivPlaceImage?.setImageURI(Uri.parse(detailModel.image))
            tvMobileNumber?.text = detailModel.mobileNumber
            tvLocation?.text = detailModel.location
            name?.text = detailModel.name
            dob?.text = detailModel.date
        }

    }
}