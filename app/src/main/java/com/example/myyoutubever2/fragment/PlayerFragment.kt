package com.example.myyoutubever2.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myyoutubever2.R
import com.example.myyoutubever2.database.entity.VideoDB
import com.example.myyoutubever2.utils.Utils
import com.example.myyoutubever2.view.VideoPlayer
import com.example.myyoutubever2.viewmodel.MainViewModel
import com.example.myyoutubever2.viewmodel.PlayerFragViewModel
import kotlinx.android.synthetic.main.fragment_player.view.*
import kotlinx.android.synthetic.main.view_video_player.view.*
import kotlin.math.abs

class PlayerFragment : Fragment() {
    private var orientationEventListener: OrientationEventListener? = null

    private val mDuration = 200L
    private var padding = 0
    private var displayWidth = 0
    private var displayHeight= 0
    private var fullVideoWidth= 0
    private var fullVideoHeight= 0
    private var pipVideoWidth = 0
    private var pipVideoHeight = 0
    private var layoutPipWidth = 0
    private var layoutPipHeight = 0
    private var layoutPipY = 0
    private var pipMarginBottom = 0

    private var mLayoutState = LAYOUT_STATE_FULL

    private var firstY = 0.0F
    private var oldY = 0.0F
    private var touchState = TOUCH_STATE_CLICK //스와이프와 클릭이벤트 처리를 구분하기 위함 (이렇게 말고는 방법이 없었을까..)
    private var mPipMoveState = PIP_MOVE_INIT

    private lateinit var videoDB: VideoDB
    private lateinit var mView: View
    private lateinit var viewModel: PlayerFragViewModel

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoDB = it.getSerializable(PARAM_VIDEO) as VideoDB
        }

    }

    override fun onStart() {
        super.onStart()
        orientationEventListener?.enable()
    }

    override fun onPause() {
        super.onPause()
        orientationEventListener?.disable()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mView = view

        val videoRecommendFragment = VideoRecommendFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentVideoContents, videoRecommendFragment)
            .commit()

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[PlayerFragViewModel::class.java]
        viewModel.setRecommendVideo(videoDB)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        initSize()
        initEvent()
        startVideo(videoDB)

        setStatusBarChange(Utils.getOrientation(context!!))
    }

    private fun initSize() {
        padding = 0 //Utils.convertDpToPx(context!!, 10)
        displayWidth = Utils.getDisplayWidth(context!!)
        displayHeight = Utils.getDisplayHeight(context!!) + 50
        fullVideoWidth = Utils.getDisplayWidth(context!!)
        fullVideoHeight = if(Utils.getOrientation(context!!) == Configuration.ORIENTATION_PORTRAIT) {
            Utils.getScreenHeightFromWidth(fullVideoWidth)
        }else {
            Utils.getDisplayHeight(context!!)
        }

        pipVideoWidth = Utils.convertDpToPx(context!! , 110)
        pipVideoHeight = Utils.getScreenHeightFromWidth(pipVideoWidth) //PIP에서 video view 높이
        pipMarginBottom = Utils.convertDpToPx(context!!, 48)

        layoutPipWidth= if(Utils.getOrientation(context!!) == Configuration.ORIENTATION_PORTRAIT) displayWidth
        else displayHeight

        layoutPipHeight = pipVideoHeight + (padding * 2) //PIP에서 layout 높이
        layoutPipY = displayHeight - pipVideoHeight - (padding * 2) //PIP에서 screen y Position
    }

    private fun setStatusBarChange(orientation: Int) {
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            showStatusBar()
        }else {
            if(mLayoutState == LAYOUT_STATE_FULL) doFullScreen()
            else showStatusBar()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        initSize()
        setStatusBarChange(newConfig.orientation)

        when(newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> { //세로 전환
                setPortraitView()
            }

            Configuration.ORIENTATION_LANDSCAPE -> { //가로 전환
                setLandscapeView()
            }
        }
    }

    private fun setPortraitView() {
        if(mLayoutState == LAYOUT_STATE_FULL) {
            setLayoutSize(displayWidth, displayHeight)
            setVideoViewSize(fullVideoWidth, fullVideoHeight)
        }
    }

    private fun setLandscapeView() {
        mView.post {
            if(mLayoutState == LAYOUT_STATE_FULL) {
                setLayoutSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setVideoViewSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    //비디오 정보 + 추천 비디오 정보를 한번에 넘겨주기
    private fun startVideo(videoDB: VideoDB) {
        mView.videoAlpha.alpha = 0F //video 투명도 지정하는 배경 초기화

        setVideoViewSize(fullVideoWidth, fullVideoHeight)
        onShowLayoutAnimator()

        mView.myVideoPlayer.initVideo(videoDB.thumbnailPath, videoDB.videoPath)
    }

    fun isFullScreen() =
        if(mLayoutState == LAYOUT_STATE_FULL && mView.visibility == View.VISIBLE) {
            onPipLayoutAnimator()
            true
        } else false

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

            override fun changeOrientation() {
                activity?.let {
                    it.requestedOrientation = if(Utils.getOrientation(it) == Configuration.ORIENTATION_LANDSCAPE) {
                        setOrientationPortraitListener()
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                    }else {
                        setOrientationLandscapeListener()
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    }

                    mView.myVideoPlayer.hideController()
                }
            }

            override fun videoStateIdle() {
            }

            override fun videoStateBuffering() {
            }

            override fun videoStatePlaying() {
                Glide.with(context!!)
                    .load(R.drawable.ic_pause_circle)
                    .into(mView.ibPipVideoPlay)
            }

            override fun videoStatePause() {
                Glide.with(context!!)
                    .load(R.drawable.ic_play_circle)
                    .into(mView.ibPipVideoPlay)
            }

            override fun videoStateEnded() {
            }
        })

        setTouchEventFullMode()
        setTouchEventPipMode()
    }

    private fun setTouchEventFullMode() {
        mView.myVideoPlayer.setOnTouchListener { _, event ->
            if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_FULL) {
                return@setOnTouchListener false
            }

            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(Utils.getOrientation(context!!) == Configuration.ORIENTATION_LANDSCAPE &&
                            event.rawY < 200) {
                        touchState = TOUCH_STATE_NONE
                    }else {
                        oldY = event.rawY
                        firstY = event.rawY
                        touchState = TOUCH_STATE_CLICK
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if(touchState == TOUCH_STATE_NONE) return@setOnTouchListener false

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
                    if(touchState == TOUCH_STATE_NONE) {
                        touchState = PIP_MOVE_INIT
                        return@setOnTouchListener false
                    }

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

                    mView.performClick()
                }
            }

            true
        }
    }

    private fun setTouchEventPipMode() {
        mView.setOnTouchListener { _, event ->
            if(mPipMoveState == PIP_MOVE_RUNNING || mLayoutState != LAYOUT_STATE_PIP) return@setOnTouchListener true

            when(event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldY = event.rawY
                    firstY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val newY = event.rawY

                    if(mView.translationY == 0F
                        && layoutPipHeight == mView.height
                        && abs(newY-firstY) < 50) {
                        oldY = newY
                        return@setOnTouchListener true
                    }

                    if(mPipMoveState == PIP_MOVE_INIT) { //초기에 아래로 내리는건지, 올리는건지 결정하고 터치이벤트가 끝나기 전까지 하나의 상태만 수행
                        mPipMoveState = if(newY > oldY) {
                            PIP_MOVE_DOWN
                        }else {
                            PIP_MOVE_UP
                        }
                    }

                    when(mPipMoveState) {
                        PIP_MOVE_DOWN -> {

                            val transY = mView.translationY
                            val moveY = newY - oldY

                            if(transY + moveY < 0) {
                                mView.translationY = 0F
                            }else {
                                mView.translationY += moveY
                            }

                            mView.layoutPipContents.alpha = 1 - (mView.translationY) / (mView.height) //내릴수록 투명하게
                            mView.videoAlpha.alpha = (mView.translationY) / (mView.height)
                        }
                        PIP_MOVE_UP -> {
                            var height = if(newY < oldY) (mView.height + oldY - newY).toInt()
                            else (mView.height - (newY - oldY)).toInt()

                            if(height < layoutPipHeight) height = layoutPipHeight //pip 크기만큼까지만 감소
                            if(height > displayHeight) height = displayHeight //디스플레이 height 까지만 증가

                            onChangeScreenSize(height)
                        }
                    }

                    oldY = newY
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    when(mPipMoveState) {
                        PIP_MOVE_INIT -> onFullLayoutAnimator()
                        PIP_MOVE_DOWN -> if(mView.translationY > 20) {
                            onPipCloseAnimator()
                        }else {
                            mPipMoveState = PIP_MOVE_INIT
                            mView.translationY = 0F
                        }
                        PIP_MOVE_UP -> if(mView.height != layoutPipHeight) {
                            onFullLayoutAnimator()
                        }else {
                            mPipMoveState = PIP_MOVE_INIT
                        }
                    }
                }
                else -> return@setOnTouchListener true
            }
            mView.performClick()
            return@setOnTouchListener true
        }
    }

    //최초 Player Layout을 그릴 때 호출하는 함수, 애니메이션을 다르게 가져가기 위해 따로 구현.
    private fun onShowLayoutAnimator() {
        ValueAnimator.ofInt(0, displayHeight).apply {
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int
                val paddingValue = padding - (padding * value / (displayHeight))

                setLayoutSize(displayWidth, value)
                mView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue)

                val margin = getMarginBottom(value)
                mainViewModel.bottomTabAnimation(margin)
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
            mView.setPadding(it, it, it, it)
        }

        val rangeWidth = displayWidth - layoutPipWidth
        val rangeVideoWidth = fullVideoWidth - pipVideoWidth
        val videoHeight = height - (paddingValue * 2) //위아래 padding

        val width = (layoutPipWidth + (rangeWidth * (1 - ((height - displayHeight).toFloat() / (layoutPipHeight - displayHeight).toFloat())))).toInt()
        val videoWidth = (pipVideoWidth + (rangeVideoWidth * (1 - ((videoHeight - fullVideoHeight).toFloat() / (pipVideoHeight - fullVideoHeight).toFloat())))).toInt()

        setLayoutSize(width, height)

        val margin = getMarginBottom(height)
        mainViewModel.bottomTabAnimation(margin)

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

                    if(Utils.getOrientation(context!!) == Configuration.ORIENTATION_LANDSCAPE) doFullScreen()

                }
            })

            setDuration(mDuration).start()
        }
    }

    private fun onPipLayoutAnimator() {
        mView.videoPlayer.hideController()

        ValueAnimator.ofInt(mView.height, displayHeight - layoutPipY).apply {
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int

                onChangeScreenSize(value)
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

                    setStatusBarChange(Utils.getOrientation(context!!))
                }
            })

            setDuration(mDuration).start()
        }

    }

    private fun onPipCloseAnimator() {
        //Exoplayer alpha 문제
        val playerContentsAlphaAnim = ObjectAnimator.ofFloat(mView.layoutPipContents,
            "alpha",
            mView.layoutPipContents.alpha,
            0f)
        val playerAlphaAnim = ObjectAnimator.ofFloat(mView.videoAlpha,
            "alpha",
            mView.videoAlpha.alpha,
            1f)
        val objectTranslateAnim = ObjectAnimator.ofFloat(mView,
            "translationY",
            mView.height.toFloat())

        AnimatorSet().apply {
            play(objectTranslateAnim).with(playerAlphaAnim).with(playerContentsAlphaAnim)
            duration = 100

            addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) { mPipMoveState = PIP_MOVE_RUNNING }
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    mPipMoveState = PIP_MOVE_INIT

                    mView.visibility = View.GONE
                    mView.translationY -= pipVideoHeight.toFloat()
                    mView.myVideoPlayer.releaseVideo()
                }
            })

            start()
        }
    }

    private fun setOrientationPortraitListener() {
        setOrientationEventListener(0, 360)
    }

    private fun setOrientationLandscapeListener() {
        setOrientationEventListener(90, 270)
    }

    /*
     * 화면 회전에 따라 돌아간 각도가 반환되는 함수.
     * 시계방향으로 돌아갈수록 +1씩 증가하여 거꾸로 들면 180 원래대로 돌아오면 360 값이 반환된다.
     * 유튜브처럼 전체화면 누르면 강제로 가로방향으로 전환하지만 현재 핸드폰의 회전 상태는 0도
     * orientation 값이 90이나 270에 가까워졌을때 현재 핸드폰을 가로로 돌렸다고 판단 할 수 있음.
     * 여유있게 +-10도를 계산하여 80~100 or 260~280 구간에 orientation이 진입하면 SCREEN_ORIENTATION_SENSOR 로 변경
     * 구글 검색 키워드 : android landscape like youtube
 * */
    private fun setOrientationEventListener(leftAngle: Int, rightAngle: Int) {
        orientationEventListener?.disable()
        orientationEventListener = object: OrientationEventListener(context!!, SensorManager.SENSOR_DELAY_UI) {
            override fun onOrientationChanged(orientation: Int) {
                val epsilon = 10

                if(epsilonCheck(orientation, leftAngle, epsilon) ||
                    epsilonCheck(orientation, rightAngle, epsilon)){
                    orientationEventListener!!.disable()
                    orientationEventListener = null

                    Handler().postDelayed({
                        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                    },500)
                }
            }

            private fun epsilonCheck(orientation: Int, angle: Int, epsilon: Int): Boolean {
                return orientation > angle - epsilon && orientation < angle + epsilon
            }
        }

        orientationEventListener!!.enable()
    }


    private fun showStatusBar() {
        activity?.let {
            val decorView = it.window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    private fun doFullScreen() {
        activity?.let {
            val decorView = it.window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
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

    private fun getMarginBottom(height: Int): Float {
        return pipMarginBottom * (displayHeight - height) / (displayHeight - pipVideoHeight).toFloat()
//        val margin = pipMarginBottom * (displayHeight - height) / (displayHeight - pipVideoHeight)
//
//        val layoutParams = mView.layoutParams as FrameLayout.LayoutParams
//        layoutParams.bottomMargin = margin
//        mView.layoutParams = layoutParams
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
        const val TOUCH_STATE_NONE = 2 //터치이벤트 무


        private const val PARAM_VIDEO = "param_video"
        @JvmStatic
        fun newInstance(param1: VideoDB) =
            PlayerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PARAM_VIDEO, param1)
                }
            }

    }
}