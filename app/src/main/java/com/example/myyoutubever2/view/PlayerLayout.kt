package com.example.myyoutubever2.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.example.myyoutubever2.R.layout.layout_player
import com.example.myyoutubever2.utils.Utils
import kotlinx.android.synthetic.main.layout_player.view.*
import kotlinx.android.synthetic.main.view_video_player.view.*
import kotlin.math.abs

class PlayerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner, LifecycleObserver {
    private val mView = inflate(context, layout_player, this)
    private val lifecycleRegistry = LifecycleRegistry(this)

    private val mDuration = 200L
    private val padding = Utils.convertDpToPx(context, 15)

    private val displayWidth
        get() = Utils.getDisplayWidth(context)

    private val displayHeight
        get() = Utils.getDisplayHeight(context)

    private val fullVideoWidth
        get() = Utils.getDisplayWidth(context)

    private val fullVideoHeight
        get() = if(Utils.getOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
            getScreenHeightFromWidth(fullVideoWidth)
        }else {
            Utils.getDisplayHeight(context)
        }

    private val pipVideoWidth = Utils.convertDpToPx(context , 110)
    private val pipVideoHeight = getScreenHeightFromWidth(pipVideoWidth) //PIP에서 video view 높이

    private val layoutPipWidth
        get() = if(Utils.getOrientation(context) == Configuration.ORIENTATION_PORTRAIT) displayWidth
                else displayHeight

    private val layoutPipHeight = pipVideoHeight + (padding * 2) //PIP에서 layout 높이
    private val layoutPipY
        get() = displayHeight - pipVideoHeight - (padding * 2) //PIP에서 screen y Position

    private var mLayoutState = LAYOUT_STATE_FULL

    private var firstY = 0.0F
    private var oldY = 0.0F
    private var touchState = TOUCH_STATE_CLICK //스와이프와 클릭이벤트 처리를 구분하기 위함 (이렇게 말고는 방법이 없었을까..)
    private var mPipMoveState = PIP_MOVE_INIT

    private val videoUrl = "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8"
    private val thumbnail = "http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg"

    init {
        initEvent()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        (mView.layoutParams as? FrameLayout.LayoutParams)?.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun startVideo() {
        if(visibility != View.VISIBLE) {
            visibility = View.VISIBLE
            mView.videoAlpha.alpha = 0F //video 투명도 지정하는 배경 초기화

            setVideoViewSize(fullVideoWidth, fullVideoHeight)

            onShowLayoutAnimator()
        }else {
            onFullLayoutAnimator()
        }

        mView.myVideoPlayer.initVideo(thumbnail, videoUrl)
    }

    fun isFullScreen() =
        if(mLayoutState == LAYOUT_STATE_FULL && visibility == View.VISIBLE) {
            onPipLayoutAnimator()
            true
        } else false

    //세로모드
    fun setPortraitView() {
        if(mLayoutState == LAYOUT_STATE_FULL) {
            setLayoutSize(displayWidth, displayHeight)
            setVideoViewSize(fullVideoWidth, fullVideoHeight)
        }
    }

    //가로모드
    fun setLandscapeView() {
        if(mLayoutState == LAYOUT_STATE_FULL) {
            setLayoutSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setVideoViewSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    //라이프사이클에 따라 onStart일 경우 video가 Pause상태 이므로 Controller 표출
    //(visible 상태일 경우에만 수행하게 하여, 액티비티 첫 실행 시 부터 재생되는 문제 없도록 한다.)
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if(visibility == View.VISIBLE && mLayoutState == LAYOUT_STATE_FULL) mView.myVideoPlayer.showController() //풀 화면일때 포그라운드 전환시 컨트롤 뷰 표출
    }

    //라이프사이클에 따라 onPause일 경우 Player View Release
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mView.myVideoPlayer.stopVideo()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
    
    private fun initEvent() {
        mView.setOnClickListener { }

        mView.ibPipClose.setOnClickListener {
            onPipCloseAnimator()
        }

        mView.ibPipVideoPlay.setOnClickListener {
            mView.myVideoPlayer.changeVideoState()
        }

        mView.myVideoPlayer.setControllerListener(object : VideoPlayer.VideoController {
            override fun goPip() {
                onPipLayoutAnimator()
            }

            override fun videoStateIdle() {
            }

            override fun videoStateBuffering() {
            }

            override fun videoStatePlaying() {
                Glide.with(context)
                    .load(R.drawable.ic_pause_circle)
                    .into(mView.ibPipVideoPlay)
            }

            override fun videoStatePause() {
                Glide.with(context)
                    .load(R.drawable.ic_play_circle)
                    .into(mView.ibPipVideoPlay)
            }

            override fun videoStateEnded() {
            }
        })

        //풀 화면에서 Player를 터치하여 아래로 내릴 경우 애니메이션 효과를 나타냄
        setDefaultVideoTouchEvent()
    }

    private fun setDefaultVideoTouchEvent() {
        mView.myVideoPlayer.setOnTouchListener { _, event ->
            if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_FULL) {
                return@setOnTouchListener false
            }

            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldY = event.rawY
                    firstY = event.rawY
                    touchState = TOUCH_STATE_CLICK
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveY = event.rawY

                    //첫 좌표에서 +- 50 만큼은 클릭 영역으로 구분
                    //mView.height == displayHeight 넣지 않으면 중간에 뚝뚝 끊기는 시점 발생함. 최대 크기일 때만 클릭 영역 지정.
                    if(mView.height == displayHeight && abs(moveY - firstY) < 50) {
                        oldY = moveY
                        return@setOnTouchListener true
                    }

                    touchState = TOUCH_STATE_SWIPE

                    var height = if(moveY < oldY) (mView.height + oldY - moveY).toInt()
                    else (mView.height - (moveY - oldY)).toInt()

                    if(height < layoutPipHeight) height = layoutPipHeight //pip 크기만큼까지만 감소
                    if(height > displayHeight) height = displayHeight //디스플레이 height 까지만 증가

                    if(mView.height != displayHeight && mView.myVideoPlayer.isVisibleController()) mView.myVideoPlayer.hideController()

                    onChangeScreenSize(height)

                    oldY = moveY
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if(mView.height + 50 < displayHeight) { //하단 스와이프 애니메이션 수행
                        onPipLayoutAnimator()
                    } else { //클릭 이벤트로 처리하기 위해, 풀 화면을 유지하고 Player의 Controller를 보여준다.
                        onChangeScreenSize(displayHeight)
                        mPipMoveState = PIP_MOVE_INIT

                        if(touchState == TOUCH_STATE_CLICK) {
                            if(mView.myVideoPlayer.isVisibleController()) {
                                mView.myVideoPlayer.hideController()
                            }else {
                                mView.myVideoPlayer.showController()
                            }
                        }
                    }

                    performClick()
                }
            }

            true
        }
    }

    private fun setLandscapeVideoTouchEvent() {
        mView.myVideoPlayer.setOnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    if(mView.myVideoPlayer.isVisibleController()) {
                        mView.myVideoPlayer.hideController()
                    }else {
                        mView.myVideoPlayer.showController()
                    }

                    performClick()
                }
            }

            true
        }
    }

    private fun getScreenHeightFromWidth(width: Int) = width * 225 / 400

    //최초 Player Layout을 그릴 때 호출하는 함수, 애니메이션을 다르게 가져가기 위해 따로 구현.
    private fun onShowLayoutAnimator() {
        ValueAnimator.ofInt(0, displayHeight).apply {
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int
                val paddingValue = padding - (padding * value / (displayHeight))

                setLayoutSize(displayWidth, value)
                setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

                //ExoPlayer에서 alpha 값을 조정하면 아예 안보이는 문제 있어서 임시적으로 제한
//                alpha = value.toFloat() / displayHeight
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    mLayoutState = LAYOUT_STATE_FULL
                    mView.myVideoPlayer.playVideo() //애니메이션 종료되멘 플레이어 재생, 애니메이션 중에는 썸네일 이미지 보이도록 개선
                }
            })

            duration = mDuration
            start()
        }
    }

    private fun onChangeScreenSize(height: Int) {
        val paddingValue = (padding - (padding * (height - layoutPipHeight) / (displayHeight - layoutPipHeight))).also {
            setPadding(it, it, it, it)
        }

        val rangeWidth = displayWidth - layoutPipWidth
        val rangeVideoWidth = fullVideoWidth - pipVideoWidth
        val videoHeight = height - (paddingValue * 2) //위아래 padding

        val width = (layoutPipWidth + (rangeWidth * (1 - ((height - displayHeight).toFloat() / (layoutPipHeight - displayHeight).toFloat())))).toInt()
        val videoWidth = (pipVideoWidth + (rangeVideoWidth * (1 - ((videoHeight - fullVideoHeight).toFloat() / (pipVideoHeight - fullVideoHeight).toFloat())))).toInt()

        setLayoutSize(width, height)

        if(videoHeight <= fullVideoHeight) {
            mView.layoutPipContents.alpha = ((videoHeight - fullVideoHeight).toFloat() / (pipVideoHeight - fullVideoHeight).toFloat())
            setVideoViewSize(videoWidth, videoHeight)
        }else {
            setVideoViewSize(fullVideoWidth, fullVideoHeight)
        }
    }

    private fun onFullLayoutAnimator() {
        ValueAnimator.ofInt(mView.height, displayHeight).apply {
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int

                onChangeScreenSize(value)
            }

            addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) { mPipMoveState = PIP_MOVE_RUNNING }
                override fun onAnimationEnd(animation: Animator?) {
                    mPipMoveState = PIP_MOVE_INIT
                    mLayoutState = LAYOUT_STATE_FULL
                }
            })

            setDuration(mDuration).start()
        }
    }

    private fun onPipLayoutAnimator() {
        mView.videoPlayer.hideController()
        val minDisplayHeight = layoutPipY

        ValueAnimator.ofInt(mView.y.toInt(), minDisplayHeight).apply {
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int

                onChangeScreenSize(displayHeight - value)
            }

            addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {
                    if(mView.myVideoPlayer.isVisibleController()) mView.myVideoPlayer.hideController()
                    mPipMoveState = PIP_MOVE_RUNNING
                }
                override fun onAnimationEnd(animation: Animator?) {
                    mPipMoveState = PIP_MOVE_INIT
                    mLayoutState = LAYOUT_STATE_PIP
                }
            })

            setDuration(mDuration).start()
        }

    }

    private fun onPipCloseAnimator() {
        //Exoplayer alpha 문제
        val playerAlphaAnim = ObjectAnimator.ofFloat(mView.videoAlpha, "alpha", mView.videoAlpha.alpha, 1f)
        val contentsAlphaAnim = ObjectAnimator.ofFloat(mView.layoutPipContents, "alpha", mView.layoutPipContents.alpha, 0f)
        val objectTranslateAnim = ObjectAnimator.ofFloat(this, "translationY", pipVideoHeight.toFloat())

        AnimatorSet().apply {
            play(objectTranslateAnim).with(playerAlphaAnim).with(contentsAlphaAnim)
            duration = 100

            addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) { mPipMoveState = PIP_MOVE_RUNNING }
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    mPipMoveState = PIP_MOVE_INIT

                    visibility = View.GONE
                    mView.translationY -= pipVideoHeight.toFloat()
                    mView.myVideoPlayer.releaseVideo()
                }
            })

            start()
        }
    }

    private fun setVideoViewSize(width: Int, height: Int) {
        mView.layoutVideoPlayer.layoutParams = mView.layoutVideoPlayer.layoutParams.apply {
            this.width = width
            this.height = height
        }
    }

    private fun setLayoutSize(width: Int, height: Int) {
        mView.layoutParams = mView.layoutParams.apply {
            this.width = width
            this.height = height
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    //PIP 상태에서 터치이벤트를 처리하기 위함
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_PIP) return true

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                oldY = event.rawY
                firstY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val moveY = event.rawY

                if(layoutPipY.toFloat() == mView.y
                    && layoutPipHeight == mView.height
                    && abs(moveY-firstY) < 50) {
                    oldY = moveY
                    return true
                }

                if(mPipMoveState == PIP_MOVE_INIT) { //초기에 아래로 내리는건지, 올리는건지 결정하고 터치이벤트가 끝나기 전까지 하나의 상태만 수행
                    mPipMoveState = if(moveY > oldY) {
                        PIP_MOVE_DOWN
                    }else {
                        PIP_MOVE_UP
                    }
                }

                when(mPipMoveState) {
                    PIP_MOVE_DOWN -> {
                        if(moveY > oldY) { //위아래로 무빙
                            mView.y += moveY - oldY
                        }else {
                            mView.y -= oldY - moveY
                        }

                        if(layoutPipY > mView.y) mView.y = layoutPipY.toFloat() //기준 PIP높이 이상은 올리지 못하도록 설정

                        mView.layoutPipContents.alpha = 1 - (mView.y - layoutPipY) / (displayHeight - layoutPipY) //내릴수록 투명하게
                        mView.videoAlpha.alpha = (mView.y - layoutPipY) / (displayHeight - layoutPipY)
                    }
                    PIP_MOVE_UP -> {
                        var height = if(moveY < oldY) (mView.height + oldY - moveY).toInt()
                        else (mView.height - (moveY - oldY)).toInt()

                        if(height < layoutPipHeight) height = layoutPipHeight //pip 크기만큼까지만 감소
                        if(height > displayHeight) height = displayHeight //디스플레이 height 까지만 증가

                        onChangeScreenSize(height)
                    }
                }

                oldY = moveY
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                when(mPipMoveState) {
                    PIP_MOVE_INIT -> onFullLayoutAnimator()
                    PIP_MOVE_DOWN -> if(mView.y != layoutPipY.toFloat()) {
                        onPipCloseAnimator()
                    }else {
                        mPipMoveState = PIP_MOVE_INIT
                    }
                    PIP_MOVE_UP -> if(mView.height != layoutPipHeight) {
                        onFullLayoutAnimator()
                    }else {
                        mPipMoveState = PIP_MOVE_INIT
                    }
                }
                performClick()
            }
            else -> return super.onTouchEvent(event)
        }


        return super.onTouchEvent(event)
    }

    companion object {
        const val LAYOUT_STATE_FULL = 1
        const val LAYOUT_STATE_PIP = 2

        const val PIP_MOVE_INIT = 0
        const val PIP_MOVE_DOWN = 1
        const val PIP_MOVE_UP = 2
        const val PIP_MOVE_RUNNING = 3 //터치이벤트 막기

        const val TOUCH_STATE_CLICK = 0
        const val TOUCH_STATE_SWIPE = 1
    }
}