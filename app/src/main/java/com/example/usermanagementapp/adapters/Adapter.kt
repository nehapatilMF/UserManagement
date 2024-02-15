package com.example.usermanagementapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.usermanagementapp.R
import com.example.usermanagementapp.activities.AddStudentActivity
import com.example.usermanagementapp.activities.MainActivity
import com.example.usermanagementapp.database.DatabaseHandler
import com.example.usermanagementapp.models.Model

open class Adapter(
    private val context: Context,
    private var list: ArrayList<Model>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]



        if (holder is MyViewHolder) {
            holder.itemView.findViewById<ImageView>(R.id.imageView).setImageURI(Uri.parse(model.image))
            holder.itemView.findViewById<TextView>(R.id.tvName).text = model.name
            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddStudentActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_DETAILS, list[position])
        activity.startActivityForResult(
            intent,
            requestCode
        )

        notifyItemChanged(position)
    }

    fun removeAt(position: Int) {

        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.delete(list[position])

        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Model)
    }


    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}