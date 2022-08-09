package com.example.retrofitrecyclerview.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitrecyclerview.Activity.MainActivity
import com.example.retrofitrecyclerview.DataClass.UserInfo
import com.example.retrofitrecyclerview.R

class adapter(var userList: List<UserInfo>, mainActivity: MainActivity) :
    RecyclerView.Adapter<adapter.Tasarim>() {


    fun setData(userList: List<UserInfo>) {
        this.userList = userList
        notifyDataSetChanged()
    }


    class Tasarim(view: View) : RecyclerView.ViewHolder(view) {
        val nameText = view.findViewById<TextView>(R.id.name)
        val surnameText = view.findViewById<TextView>(R.id.surname)
        val emailText = view.findViewById<TextView>(R.id.email)
        val passwordText = view.findViewById<TextView>(R.id.password)


        fun bindItems(item: UserInfo) {


            nameText.text = item.name
            surnameText.text = item.surname
            emailText.text = item.email
            passwordText.text = item.password


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Tasarim {
        val binding = LayoutInflater.from(parent.context)
        val view = binding.inflate(R.layout.item, parent, false)

        return Tasarim(view)
    }

    override fun onBindViewHolder(holder: Tasarim, position: Int) {

        holder.bindItems(userList.get(position))
    }

    override fun getItemCount(): Int {
        return userList.count()
    }

}