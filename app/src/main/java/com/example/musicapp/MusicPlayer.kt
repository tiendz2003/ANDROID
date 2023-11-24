package com.example.musicapp

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class MusicPlayer : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvTime: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvArtist: TextView
    private lateinit var tvDuration: TextView
    private lateinit var seekBarTime: SeekBar
    private lateinit var seekBarVolume: SeekBar
    private lateinit var btnPlay: Button
    private var currentPosition: Int = 0

    private lateinit var musicPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        val song: Songs? = intent.getParcelableExtra("song")

        tvTime = findViewById(R.id.tvTime)
        tvDuration = findViewById(R.id.tvDuration)
        seekBarTime = findViewById(R.id.seekBarTime)
        seekBarVolume = findViewById(R.id.seekBarVolume)
        btnPlay = findViewById(R.id.btnPlay)
        tvTitle = findViewById(R.id.tvTitle)
        tvArtist = findViewById(R.id.tvArtist)

        tvTitle.text = song?.title
        tvArtist.text = song?.artist

        musicPlayer = MediaPlayer()

        try {
            val songPath = song?.path
            if (songPath != null) {
                Log.d("MusicPlayer", "Song path: $songPath")
                musicPlayer.setDataSource(songPath)
                musicPlayer.prepare()
            } else {
                Toast.makeText(this, "Error: Song path is null", Toast.LENGTH_SHORT).show()
                Log.e("MusicPlayer", "Error: Song path is null")
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        musicPlayer.isLooping = true
        musicPlayer.seekTo(0)
        musicPlayer.setVolume(0.5f, 0.5f)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            if (musicPlayer.isPlaying) {
                musicPlayer.stop()
            }
            finish() // Kết thúc Activity khi nút trở lại được bấm
        }

        val duration = millisecondsToString(musicPlayer.duration)
        tvDuration.text = duration
        btnPlay.setOnClickListener(this@MusicPlayer)

        seekBarVolume.progress = 50
        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f
                musicPlayer.setVolume(volume, volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarTime.max = musicPlayer.duration
        seekBarTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicPlayer.seekTo(progress)
                    seekBar?.progress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        val action: String? = intent.action
        if (action == "play") {
            // Nếu action là "play", bắt đầu phát nhạc
            if (!musicPlayer.isPlaying) {
                musicPlayer.start()
                btnPlay.setBackgroundResource(R.drawable.ic_pause)
            }
        }

        Thread {
            while (true) {
                if (musicPlayer.isPlaying) {
                    try {
                        val current = musicPlayer.currentPosition.toDouble()
                        val elapsedTime = millisecondsToString(current.toInt())

                        runOnUiThread {
                            tvTime.text = elapsedTime
                            seekBarTime.progress = current.toInt()
                        }

                        Thread.sleep(1000)
                    } catch (_: InterruptedException) {
                    }
                }
            }
        }.start()
    }

    private fun millisecondsToString(time: Int): String {
        var elapsedTime: String
        val minutes = time / 1000 / 60
        val second = time / 1000 % 60
        elapsedTime = "$minutes:"
        elapsedTime += if (second < 10) "0" else ""
        elapsedTime += second
        return elapsedTime
    }


    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.btnPlay) {
                if (musicPlayer.isPlaying) {
                    musicPlayer.pause()
                    btnPlay.setBackgroundResource(R.drawable.ic_play)
                } else {
                    musicPlayer.start()
                    btnPlay.setBackgroundResource(R.drawable.ic_pause)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Xử lý khi nút back trên thiết bị được bấm
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
        }
        finish()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (musicPlayer.isPlaying) {
                musicPlayer.stop()
            }
            finish()
        }
        return super.onOptionsItemSelected(item)
    }



//    override fun onDestroy() {
//        super.onDestroy()
//        if (musicPlayer.isPlaying) {
//            musicPlayer.stop()
//        }
//        musicPlayer.release()
//    }



}