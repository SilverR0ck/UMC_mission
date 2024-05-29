package com.example.flo

import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer?=null
    private var gson: Gson= Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB : SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songRepeatIv.setOnClickListener {
            val currentColor = binding.songRepeatIv.colorFilter
            if(currentColor == null){
                binding.songRepeatIv.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
            }
            else{
                binding.songRepeatIv.clearColorFilter()
            }
        }
        binding.songRandomIv.setOnClickListener {
            val currentColor = binding.songRandomIv.colorFilter
            if(currentColor == null){
                binding.songRandomIv.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
            }
            else{
                binding.songRandomIv.clearColorFilter()
            }
        }

    }




    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        Log.d("now Song ID", songs[nowPos].id.toString())
        startTimer()
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()

    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song : Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second*1000/song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        setPlayerStatus(song.isPlaying)

    }

    private fun setPlayerStatus(isPlaying: Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){ // Play 버튼이 눌렸을 때, Play이미지를 보이게 하고, Pause이미지는 뷰와 공간을 없애도록 GONE사용
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    inner class Timer(private  val playTime : Int, var isPlaying : Boolean = true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f
        override fun run() {
            super.run()
            try{
                while (true){
                    if(second>=playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
                        }
                        if(mills%1000==0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second%60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }
    // 사용자가 포커스를 읽었을 때 음악을 중지
    override fun onPause(){
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress*song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터

        val songJson = gson.toJson(song) // song 객체를 json 포멧으로 자동으로 변환
        editor.putString("songData", songJson)
        editor.apply()
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }


}