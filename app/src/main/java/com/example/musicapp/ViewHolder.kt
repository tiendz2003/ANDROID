package com.example.musicapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
    val artistTextView: TextView = itemView.findViewById(R.id.tvArtist)
}
