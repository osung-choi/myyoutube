package com.example.myyoutubever2.view

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myyoutubever2.R
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.view_video_player.view.*

class VideoPlayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val mView = inflate(context, R.layout.view_video_player, this)
    private var player: SimpleExoPlayer? = null

    //재생중인 곡 저장하기 위한 변수 (현재는 활용을 못하는 중인데 활용 가능한 부분 있는지 검토)
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    init {
        initEvent()
    }

    private fun initEvent() {
        mView.videoPlayer.setControllerVisibilityListener { //컨트롤러가 사라지면 컨트롤러를 사용하지 못하게 하므로써 터치이벤트를 수행할 수 있도록 한다.
            if(it != View.VISIBLE) mView.videoPlayer.useController = false
        }
    }

    fun initVideo(videoUrl: String) {
        if(player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context.applicationContext)
            mView.videoPlayer.player = player
            mView.videoPlayer.controllerAutoShow = false
            mView.videoPlayer.useController = false
        }

        val mediaSource = buildMediaSource(videoUrl)
        player!!.prepare(mediaSource)
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.playWhenReady = false
    }

    fun releaseVideo() {
        player?.let {
            playWhenReady = it.playWhenReady
            currentWindow = it.currentWindowIndex
            playbackPosition = it.currentPosition
            mView.videoPlayer.player = null
            it.release()
            player = null
        }
    }

    fun stopVideo() {
        player?.let {
            it.playWhenReady = false
        }
    }

    fun playVideo() {
        player?.let {
            it.playWhenReady = true
        }
    }

    fun showController() {
        mView.videoPlayer.useController = true
        mView.videoPlayer.showController()
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