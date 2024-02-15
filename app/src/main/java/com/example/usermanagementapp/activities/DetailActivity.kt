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

        val toolbar_detail = binding?.toolbarDetails
        val iv_place_image = binding?.imageView
        val tv_mobile_number = binding?.phone
        val tv_location = binding?.address
        val name = binding?.name
        val dob = binding?.dob
        var detailModel: Model? = null

        if (intent.hasExtra(MainActivity.EXTRA_DETAILS)) {
            detailModel =
                intent.getSerializableExtra(MainActivity.EXTRA_DETAILS) as Model
        }

        if (detailModel != null) {

            setSupportActionBar(toolbar_detail)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = null

            toolbar_detail?.setNavigationOnClickListener {
                onBackPressed()
            }
            iv_place_image?.setImageURI(Uri.parse(detailModel.image))
            tv_mobile_number?.setText( detailModel.mobileNumber)
            tv_location?.setText(detailModel.location)
            name?.setText(detailModel.name)
            dob?.setText( detailModel.date)
        }

    }
}