package com.example.musicapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListMusicActivity : AppCompatActivity() {
    private val requestPermission = 123
    private lateinit var songArrayList: ArrayList<Songs>
    private lateinit var rvSongs: RecyclerView
    private lateinit var songsAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)
        supportActionBar?.hide()
        // Ánh xạ ListView
        rvSongs = findViewById(R.id.rvSongs)
        val layoutManager = LinearLayoutManager(this)
        rvSongs.layoutManager = layoutManager

        // Khởi tạo ArrayList
        songArrayList = ArrayList()

        // Khởi tạo adapter và gán cho ListView
        songsAdapter = SongAdapter(this, songArrayList)
        rvSongs.adapter = songsAdapter

        // Loại bỏ padding nếu có
        rvSongs.setPadding(0, 0, 0, 0)


        // Trong phương thức onCreate
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
        {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                requestPermission
            )
        } else {

            // Nếu đã có quyền, thực hiện các công việc khác
            getSongs()
        }

        // Xử lý sự kiện khi nhấn vào một bài hát trong ListView
        songsAdapter.setOnItemClickListener(object : SongAdapter.OnItemClickListener {
            override fun onItemClick(song: Songs) {
                val openMusicPlayer = Intent(this@ListMusicActivity, MusicPlayer::class.java)
                openMusicPlayer.putExtra("song", song)
                openMusicPlayer.action = "play"
                startActivity(openMusicPlayer)
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestPermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu đã có quyền, lấy danh sách bài hát
                getSongs()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getSongs() {
        val contentResolver = contentResolver
        val songUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor: Cursor? = contentResolver.query(songUri, null, null, null, null)

        if (songCursor != null && songCursor.moveToFirst()) {
            val indexTitle: Int = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val indexArtist: Int = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val indexData: Int = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)

            do {
                val title: String = songCursor.getString(indexTitle) ?: ""
                val artist: String = songCursor.getString(indexArtist) ?: ""
                val path: String = songCursor.getString(indexData) ?: ""
                songArrayList.add(Songs(title, artist, path))
            } while (songCursor.moveToNext())
        }

        songsAdapter.notifyDataSetChanged()
    }




}