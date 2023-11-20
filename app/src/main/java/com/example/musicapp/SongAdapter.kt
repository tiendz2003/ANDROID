package com.example.musicapp



import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(private val context: Context, private val songList: List<Songs>) :
    RecyclerView.Adapter<ViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_song, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.titleTextView.text = song.title
        holder.artistTextView.text = song.artist
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(song)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }
    interface OnItemClickListener {
        fun onItemClick(song: Songs)
    }
}

