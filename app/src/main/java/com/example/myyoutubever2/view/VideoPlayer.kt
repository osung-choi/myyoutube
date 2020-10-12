package com.example.myyoutubever2.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myyoutubever2.R
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.view_video_player.view.*

class VideoPlayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val mView = inflate(context, R.layout.view_video_player, this)
    private var player: SimpleExoPlayer? = null

    //재생중인 곡 저장하기 위한 변수
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    fun initVideo(videoUrl: String) {
        if(player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context.applicationContext)
            mView.videoPlayer.player = player
        }

        val mediaSource = buildMediaSource(videoUrl)
        player!!.prepare(mediaSource)
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.playWhenReady = playWhenReady
    }

    fun releaseVideo() {
        player?.let {
            playWhenReady = it.playWhenReady
            currentWindow = it.currentWindowIndex
            playbackPosition = it.currentPosition
            it.release()
            player = null
        }
    }

    fun stopVideo() {
        player?.let {
            it.playWhenReady = false
            it.stop()
        }
    }

    fun playVideo() {
        player?.let {
            it.playWhenReady = true
        }
    }

    fun setPipMode(pipMode: Boolean) {
        mView.videoPlayer.controllerAutoShow = pipMode
    }

    private fun buildMediaSource(url: String) : MediaSource {
        var userAgent:String = Util.getUserAgent(context, "project_name")
        val uri = Uri.parse(url)

        return uri.lastPathSegment!!.run {
            if(contains(".m3u8"))
                HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
            else
                ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
        }
    }
}