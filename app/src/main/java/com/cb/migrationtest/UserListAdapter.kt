package com.cb.migrationtest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserListAdapter(
    private var modelList: ArrayList<UserEntity>,
    val onClick: (UserEntity) -> Unit,
    val onDeleteClick: (UserEntity) -> Unit
) : RecyclerView.Adapter<UserListViewHolder>() {
    private val TAG = "UserListAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_single_user, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: UserListViewHolder, position: Int) {
        try {
            val userEntity = modelList[position]
            viewHolder.firstName.text = userEntity.firstName
            viewHolder.lastName.text = userEntity.lastName
            viewHolder.mainLayout.setOnClickListener { onClick(userEntity) }
            viewHolder.delete.setOnClickListener { onDeleteClick(userEntity) }
        } catch (e: Exception) {
            Log.d(TAG, "onBindViewHolder: $e")
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}


class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var mainLayout: LinearLayout = view.findViewById(R.id.main_layout)
    var firstName: TextView = view.findViewById(R.id.first_name)
    var lastName: TextView = view.findViewById(R.id.last_name)
    var delete: ImageView = view.findViewById(R.id.delete)
}