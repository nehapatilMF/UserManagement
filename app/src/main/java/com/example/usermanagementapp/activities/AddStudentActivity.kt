package com.example.usermanagementapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.usermanagementapp.R
import com.example.usermanagementapp.database.DatabaseHandler
import com.example.usermanagementapp.databinding.ActivityAddStudentBinding
import com.example.usermanagementapp.models.Model
import com.example.usermanagementapp.utils.DateUtils
import com.example.usermanagementapp.utils.ImageUtils


@Suppress("DEPRECATION")
class AddStudentActivity : AppCompatActivity(), View.OnClickListener {
    private var binding : ActivityAddStudentBinding? = null
    private var saveImageToInternalStorage: String? = null
    private var details: Model? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.title = " Students Details"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(MainActivity.EXTRA_DETAILS)) {
            details =
                    intent.getSerializableExtra(MainActivity.EXTRA_DETAILS) as Model
        }

        if (details != null) {
            supportActionBar?.title = "Edit Students Details"
            binding?.etName?.setText(details?.name)
            binding?.etMobileNumber?.setText(details?.mobileNumber)
            binding?.etDate?.setText(details?.date)
            binding?.etLocation?.setText(details?.location)
            saveImageToInternalStorage = Uri.parse(details!!.image).toString()
            Glide.with(this)
                .load(saveImageToInternalStorage)
                .into(binding?.imageView!!)

            binding?.btnSave?.text = "UPDATE"
        }


        binding?.etDate?.setOnClickListener(this)
        binding?.tvAddImage?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.et_date -> {
                DateUtils.showDatePicker(this@AddStudentActivity) { _, year, month, dayOfMonth ->
                    binding?.etDate?.setText("$dayOfMonth.${month + 1}.$year")
                }
            }

            R.id.tv_add_image -> {
                ImageUtils.showImagePickerDialog(this@AddStudentActivity) { bitmap ->
                    saveImageToInternalStorage(bitmap)
                }
            }

            R.id.btn_save -> {
                when {
                    binding?.etName?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
                    }
                    binding?.etMobileNumber?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding?.etLocation?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please select location", Toast.LENGTH_SHORT)
                            .show()
                    }
                    saveImageToInternalStorage == null -> {
                        Toast.makeText(this, "Please add image", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val model = Model(
                            if (details == null) 0 else details!!.id,
                            binding?.etName?.text.toString(),
                            saveImageToInternalStorage.toString(),
                            binding?.etMobileNumber?.text.toString(),
                            binding?.etDate?.text.toString(),
                            binding?.etLocation?.text.toString(),
                        )

                        val dbHandler = DatabaseHandler(this)

                        if (details == null) {
                            val addStudent = dbHandler.addStudent(model)

                            if (addStudent > 0) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        } else {
                            val updateStudent = dbHandler.update(model)

                            if (updateStudent > 0) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }

        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) {
        saveImageToInternalStorage = ImageUtils.saveImageToInternalStorage(bitmap, this)
        Log.e("Saved Image : ", "Path :: $saveImageToInternalStorage")
        binding?.imageView?.setImageBitmap(bitmap)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bitmap = ImageUtils.handleImageResult(requestCode, resultCode, data, this)
        bitmap?.let { saveImageToInternalStorage(it) }
    }

}