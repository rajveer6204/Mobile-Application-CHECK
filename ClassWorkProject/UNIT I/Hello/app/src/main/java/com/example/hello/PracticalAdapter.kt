package com.example.hello

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PracticalAdapter(
    private val itemList: List<PracticalItem>
) : RecyclerView.Adapter<PracticalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvPracticalNum: TextView = itemView.findViewById(R.id.tvPracticalNum)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_practical_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.ivIcon.setImageResource(item.iconResId)
        holder.tvPracticalNum.text = "Practical #${item.id}"
        holder.tvTitle.text = item.title

        holder.itemView.setOnClickListener { context ->
            val intent = Intent(context.context, item.targetActivity)
            context.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
