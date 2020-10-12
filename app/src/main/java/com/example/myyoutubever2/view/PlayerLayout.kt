package com.example.myyoutubever2.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.drm.DrmStore.Playback.STOP
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.lifecycle.*
import com.example.myyoutubever2.R.layout.layout_player
import com.example.myyoutubever2.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_player.view.*
import kotlinx.android.synthetic.main.view_video_player.view.*

class PlayerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner, LifecycleObserver {
    private val mView = inflate(context, layout_player, this)

    private val lifecycleRegistry = LifecycleRegistry(this)

    private val mDuration = 200L
    private val padding = Utils.convertDpToPx(context, 15)

    private val displayWidth = Utils.getDisplayWidth(context)
    private var displayHeight = Utils.getDisplayHeight(context)

    private val fullVideoWidth = displayWidth
    private val fullVideoHeight = getScreenHeightFromWidth(fullVideoWidth)

    private val pipVideoWidth = Utils.convertDpToPx(context , 110)
    private val pipVideoHeight = getScreenHeightFromWidth(pipVideoWidth) //PIP에서 video view 높이

    private val layoutPipHeight = pipVideoHeight + (padding * 2) //PIP에서 layout 높이
    private val layoutPipY = displayHeight - pipVideoHeight - (padding * 2) //PIP에서 screen y Position

    private var mLayoutState = LAYOUT_STATE_FULL

    private var firstY = 0.0F
    private var oldY = 0.0F
    private var mPipMoveState = PIP_MOVE_INIT

    private val videoUrl = "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8"

    init {
        initEvent()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        (mView.layoutParams as? FrameLayout.LayoutParams)?.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun startVideo() {
        if(visibility != View.VISIBLE) {
            setVideoViewSize(fullVideoWidth, fullVideoHeight)

            onShowLayoutAnimator()
        }else {
            onFullLayoutAnimator()
        }

        mView.myVideoPlayer.initVideo(videoUrl)
    }

    fun isFullScreen() =
        if(mLayoutState == LAYOUT_STATE_FULL && visibility == View.VISIBLE) {
            onPipLayoutAnimator()
            true
        } else false

    //라이프사이클에 따라 onStart일 경우 video가 Pause상태 이므로 Controller 표출
    //(visible 상태일 경우에만 수행하게 하여, 액티비티 첫 실행 시 부터 재생되는 문제 없도록 한다.)
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if(visibility == View.VISIBLE) mView.myVideoPlayer.showController()
    }

    //라이프사이클에 따라 onPause일 경우 Player View Release
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
//        mView.myVideoPlayer.releaseVideo()
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

        //풀 화면에서 Player를 터치하여 아래로 내릴 경우 애니메이션 효과를 나타냄
        //클릭이벤트와 스와이프를 구분하기 위한 민감도를 조정해야 할 필요 있음.
        mView.myVideoPlayer.setOnTouchListener { _, event ->
            if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_FULL) {
                return@setOnTouchListener false
            }

            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveY = event.rawY

                    var height = if(moveY < oldY) (mView.height + oldY - moveY).toInt()
                    else (mView.height - (moveY - oldY)).toInt()

                    if(height < layoutPipHeight) height = layoutPipHeight //pip 크기만큼까지만 감소
                    if(height > displayHeight) height = displayHeight //디스플레이 height 까지만 증가

                    onChangeScreenSize(height)

                    oldY = moveY
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if(mView.height != displayHeight) { //하단 스와이프 애니메이션 수행
                        onPipLayoutAnimator()
                    } else { //클릭 이벤트로 처리하기 위해, 풀 화면을 유지하고 Player의 Controller를 보여준다.
                        onChangeScreenSize(displayHeight)
                        mPipMoveState = PIP_MOVE_INIT

                        mView.myVideoPlayer.showController()
                    }

                    performClick()
                }
            }

            true
        }
    }

    private fun getScreenHeightFromWidth(width: Int) = width * 225 / 400
    private fun getScreenWidthFromHeight(height: Int) = height * 400 / 225

    //최초 Player Layout을 그릴 때 호출하는 함수, 애니메이션을 다르게 가져가기 위해 따로 구현.
    private fun onShowLayoutAnimator() {
        visibility = View.VISIBLE

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
        setLayoutSize(displayWidth, height)

        val paddingValue = (padding - (padding * (height - layoutPipHeight) / (displayHeight - layoutPipHeight))).also {
            setPadding(it, it, it, it)
        }

        val videoHeight = height - (paddingValue * 2) //위아래 padding

        if(videoHeight <= fullVideoHeight) {
            val videoWidth = getScreenWidthFromHeight(videoHeight)

            mView.layoutPipContents.alpha = ((videoHeight - fullVideoHeight).toFloat() / (pipVideoHeight - fullVideoHeight).toFloat())

            setVideoViewSize(videoWidth, videoHeight)
        } else if(videoHeight > fullVideoHeight && mView.myVideoPlayer.height != fullVideoHeight){
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
        val minDisplayHeight = layoutPipY
        ValueAnimator.ofInt(mView.y.toInt(), minDisplayHeight).apply {
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int

                onChangeScreenSize(displayHeight - value)
            }

            addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) { mPipMoveState = PIP_MOVE_RUNNING }
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
        val objectAlphaAnim = ObjectAnimator.ofFloat(this, "alpha", mView.alpha, 0f)
        val objectTranslateAnim = ObjectAnimator.ofFloat(this, "translationY", pipVideoHeight.toFloat())

        AnimatorSet().apply {
            play(objectTranslateAnim)//.with(objectAlphaAnim)
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
        mView.myVideoPlayer.layoutParams = mView.myVideoPlayer.layoutParams.apply {
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

    //터치이벤트 감도 개선
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_PIP) return true

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                oldY = event.rawY
                firstY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val moveY = event.rawY

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
                    PIP_MOVE_DOWN -> {
                        when {
                            mView.y > layoutPipY + 20 -> { //20px 이상 내리면 자동 close 아니면 초기위치
                                onPipCloseAnimator()
                            }

                            event.eventTime - event.downTime > 100L -> {
                                mView.y = layoutPipY.toFloat()
                                mPipMoveState = PIP_MOVE_INIT
                            }

                            event.eventTime - event.downTime <= 100L -> {
                                mView.y = layoutPipY.toFloat()
                                onFullLayoutAnimator()
                            }
                        }
                    }
                    PIP_MOVE_UP -> {
                        if(mView.height != layoutPipHeight) {
                            onFullLayoutAnimator()
                        } else {
                            onChangeScreenSize(layoutPipHeight)
                            mPipMoveState = PIP_MOVE_INIT
                        }
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
    }
}