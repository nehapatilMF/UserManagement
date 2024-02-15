package com.example.usermanagementapp.utils
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

object ImageUtils {

    private const val GALLERY = 1
    private const val CAMERA = 2
    private const val IMAGE_DIRECTORY = "StudentsImages"

    fun showImagePickerDialog(activity: Activity, listener: (Bitmap) -> Unit) {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery(activity, listener)
                1 -> takePhotoFromCamera(activity, listener)
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery(activity: Activity, listener: (Bitmap) -> Unit) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera(activity: Activity, listener: (Bitmap) -> Unit) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(intent, CAMERA)
    }

    fun handleImageResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context): Bitmap? {
        if (resultCode == Activity.RESULT_OK) {
            return when (requestCode) {
                GALLERY -> {
                    handleGalleryResult(data, context)
                }
                CAMERA -> {
                    handleCameraResult(data)
                }
                else -> null
            }
        }
        return null
    }

    private fun handleGalleryResult(data: Intent?, context: Context): Bitmap? {
        val contentURI = data?.data
        return try {
            MediaStore.Images.Media.getBitmap(context.contentResolver, contentURI)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun handleCameraResult(data: Intent?): Bitmap? {
        return data?.extras?.get("data") as? Bitmap
    }

    fun saveImageToInternalStorage(bitmap: Bitmap, context: Context): String {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }
}
