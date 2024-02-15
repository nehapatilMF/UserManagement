package com.example.usermanagementapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usermanagementapp.adapters.Adapter
import com.example.usermanagementapp.database.DatabaseHandler
import com.example.usermanagementapp.databinding.ActivityMainBinding
import com.example.usermanagementapp.models.Model
import com.example.usermanagementapp.utils.SwipeToDeleteCallback
import com.example.usermanagementapp.utils.SwipeToEditCallback

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val fabAdd = binding?.fabAddHappyPlace
        fabAdd?.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStudentActivity::class.java)

            startActivityForResult(intent, ADD_ACTIVITY_REQUEST_CODE)
        }

        getListFromLocalDB()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    private fun getListFromLocalDB() {
        val rvList = binding?.rvList
        val tvNoRecordsAvailable =binding?.tvNoRecordsAvailable

        val dbHandler = DatabaseHandler(this)

        val getList = dbHandler.getStudentsList()

        if (getList.size > 0) {
            rvList?.visibility = View.VISIBLE
            tvNoRecordsAvailable?.visibility = View.GONE
            setupRecyclerView(getList)
        } else {
            rvList?.visibility = View.GONE
            tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(happyPlacesList: ArrayList<Model>) {
        val rvList = binding?.rvList

        rvList?.layoutManager = LinearLayoutManager(this)
        rvList?.setHasFixedSize(true)

        val placesAdapter = Adapter(this, happyPlacesList)
        rvList?.adapter = placesAdapter

        placesAdapter.setOnClickListener(object :
            Adapter.OnClickListener {
            override fun onClick(position: Int, model: Model) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_DETAILS, model) // Passing the complete serializable data class to the detail activity using intent.
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvList?.adapter as Adapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rvList)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rvList?.adapter as Adapter
                adapter.removeAt(viewHolder.adapterPosition)

                getListFromLocalDB() }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rvList)
    }

    companion object{
        private const val ADD_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_DETAILS = "extra_details"
    }

}