//package com.example.practice
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//
//class UserDiffCallback : DiffUtil.ItemCallback<User>() {
//
//    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//
//    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
//        return oldItem == newItem
//    }
//}
//
//class UserAdapter2 : ListAdapter<User, UserAdapter2.UserViewHolder>(UserDiffCallback()) {
//
//    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val idTextView: TextView = itemView.findViewById(R.id.tvUserId)
//        val nameTextView: TextView = itemView.findViewById(R.id.tvUserName)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_user, parent, false)
//        return UserViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user = getItem(position)
//        holder.idTextView.text = user.id
//        holder.nameTextView.text = user.name
//    }
//}
