package com.example.myyoutubever2.view

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
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
) : ConstraintLayout(context, attrs, defStyleAttr), Player.EventListener {
    private val mView = inflate(context, R.layout.view_video_player, this)
    private var player: SimpleExoPlayer? = null

    init {
        initEvent()
    }

    private fun initEvent() {
        mView.videoPlayer.setControllerVisibilityListener { //컨트롤러가 사라지면 컨트롤러를 사용하지 못하게 하므로써 터치이벤트를 수행할 수 있도록 한다.
            if(it != View.VISIBLE) mView.videoPlayer.useController = false
        }
    }

    fun initVideo(thumbnail: String, videoUrl: String) {
        Glide.with(context)
            .load(thumbnail)
            .centerCrop()
            .into(mView.videoThumbnail)

        mView.videoThumbnail.visibility = View.VISIBLE

        if(player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context.applicationContext)
            player!!.addListener(this)
            mView.videoPlayer.player = player
        }

        mView.videoPlayer.controllerAutoShow = false
        mView.videoPlayer.useController = false

        val mediaSource = buildMediaSource(videoUrl)
        player!!.prepare(mediaSource)
        player!!.seekTo(0)
        player!!.playWhenReady = false
    }

    fun releaseVideo() {
        player?.let {
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

    fun hideController() {
        mView.videoPlayer.hideController()
        mView.videoPlayer.useController = false
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

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.d("state", "ready : $playWhenReady ,state : $playbackState")
        if(playWhenReady && playbackState == 3) {
            mView.videoThumbnail.visibility = View.GONE //준비 완료되면 썸네일 이미지 가리기
        }
    }
}