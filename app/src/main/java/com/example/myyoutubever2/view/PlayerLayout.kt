package com.example.myyoutubever2.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.example.myyoutubever2.R.layout.layout_player
import com.example.myyoutubever2.utils.Utils
import kotlinx.android.synthetic.main.layout_player.view.*

class PlayerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val mView: View

    private val mDuration = 300L
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

    private var oldY = 0.0F
    private var mPipMoveState = PIP_MOVE_INIT

    init {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).also {
            mView = it.inflate(layout_player, this)
        }

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
    }

    fun isFullScreen() =
        if(mLayoutState == LAYOUT_STATE_PIP)
            false
        else {
            onPipLayoutAnimator()
            true
        }

    private fun initEvent() {
        mView.goPip.setOnClickListener {
            onPipLayoutAnimator()
        }

        mView.setOnClickListener {  }

        mView.ibPipClose.setOnClickListener {
            onPipCloseAnimator()
        }

        mView.videoPlayer.setOnTouchListener { _, event ->
            if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_FULL) false

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

                    Log.d("asd", "height: $height")

                    onChangeScreenSize(height)

                    oldY = moveY
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if(mView.height != displayHeight) {
                        onPipLayoutAnimator()
                    } else {
                        onChangeScreenSize(displayHeight)
                        mPipMoveState = PIP_MOVE_INIT
                    }

                    performClick()
                }
            }

            true
        }
    }

    private fun getScreenHeightFromWidth(width: Int) = width * 225 / 400
    private fun getScreenWidthFromHeight(height: Int) = height * 400 / 225

    private fun onShowLayoutAnimator() {
        visibility = View.VISIBLE

        ValueAnimator.ofInt(0, displayHeight).apply {
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int
                val paddingValue = padding - (padding * value / (displayHeight))

                setLayoutSize(displayWidth, value)
                setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

                alpha = value.toFloat() / displayHeight
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    mLayoutState = LAYOUT_STATE_FULL
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
        } else if(videoHeight > fullVideoHeight && mView.videoPlayer.height != fullVideoHeight){
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
        val objectAlphaAnim = ObjectAnimator.ofFloat(this, "alpha", mView.alpha, 0f)
        val objectTranslateAnim = ObjectAnimator.ofFloat(this, "translationY", pipVideoHeight.toFloat())

        AnimatorSet().apply {
            play(objectTranslateAnim).with(objectAlphaAnim)
            duration = 100

            addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) { mPipMoveState = PIP_MOVE_RUNNING }
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    mPipMoveState = PIP_MOVE_INIT

                    visibility = View.GONE
                    mView.translationY -= pipVideoHeight.toFloat()
                }
            })

            start()
        }
    }

    private fun setVideoViewSize(width: Int, height: Int) {
        mView.videoPlayer.layoutParams = mView.videoPlayer.layoutParams.apply {
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_PIP) return true

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                oldY = event.rawY
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

                        mView.alpha = 1 - (mView.y - layoutPipY) / (displayHeight - layoutPipY) //내릴수록 투명하게
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
                        if(mView.y > layoutPipY + 30) { //30px 이상 내리면 자동 close 아니면 초기위치
                            onPipCloseAnimator()
                        }else {
                            mView.y = layoutPipY.toFloat()
                            onFullLayoutAnimator()
                        }

                        return true
                    }
                    PIP_MOVE_UP -> {
                        if(mView.height != layoutPipHeight) {
                            onFullLayoutAnimator()
                        } else {
                            onChangeScreenSize(layoutPipHeight)
                            mPipMoveState = PIP_MOVE_INIT
                        }
                        return true
                    }
                }
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