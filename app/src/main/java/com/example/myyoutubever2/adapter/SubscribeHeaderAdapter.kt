package com.example.myyoutubever2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myyoutubever2.R
import com.example.myyoutubever2.database.entity.UserDB
import com.example.myyoutubever2.databinding.AdapterSubscribeHeaderBinding

class SubscribeHeaderAdapter(private val listener: ((UserDB) -> Unit)?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val userList = arrayListOf<UserDB>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HeaderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_subscribe_header, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? HeaderViewHolder)?.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserList(list: ArrayList<UserDB>) {
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(private val binding: AdapterSubscribeHeaderBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(userDB: UserDB) {
            binding.userDB = userDB

            binding.root.setOnClickListener {
                listener?.invoke(userDB)
            }
        }
    }
}