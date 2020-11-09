package com.example.myyoutubever2

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.OrientationEventListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myyoutubever2.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var orientationEventListener: OrientationEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(playerLayout)

        click.setOnClickListener {
            playerLayout.startVideo()
        }

        playerLayout.setChangeOrientation {
            requestedOrientation = if(Utils.getOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
                setOrientationPortraitListener()
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }else {
                setOrientationLandscapeListener()
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }

        /*
        * 화면 회전에 따라 돌아간 각도가 반환되는 함수.
        * 시계방향으로 돌아갈수록 +1씩 증가하여 거꾸로 들면 180 원래대로 돌아오면 360 값이 반환된다.
        * 유튜브처럼 전체화면 누르면 강제로 가로방향으로 전환하지만 현재 핸드폰의 회전 상태는 0도
        * orientation 값이 90이나 270에 가까워졌을때 현재 핸드폰을 가로로 돌렸다고 판단 할 수 있음.
        * 여유있게 +-10도를 계산하여 80~100 or 260~280 구간에 orientation이 진입하면 SCREEN_ORIENTATION_SENSOR 로 변경
        * 구글 검색 키워드 : android landscape like youtube
        * */

    }

    private fun setOrientationPortraitListener() {
        setOrientationEventListener(0, 360)
    }

    private fun setOrientationLandscapeListener() {
        setOrientationEventListener(90, 270)
    }

    private fun setOrientationEventListener(leftAngle: Int, rightAngle: Int) {
        orientationEventListener?.disable()
        orientationEventListener = object: OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI) {
            override fun onOrientationChanged(orientation: Int) {
                val epsilon = 10

                Log.d("asd","$orientation");

                if(epsilonCheck(orientation, leftAngle, epsilon) ||
                    epsilonCheck(orientation, rightAngle, epsilon)){
                    orientationEventListener!!.disable()
                    orientationEventListener = null

                    Handler().postDelayed({
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                    },500)
                }
            }

            private fun epsilonCheck(orientation: Int, angle: Int, epsilon: Int): Boolean {
                //Log.d("asd","${angleCheck(angle - epsilon)} : ${angleCheck(angle + epsilon)}")

                return orientation > angle - epsilon && orientation < angle + epsilon
            }
        }

        orientationEventListener!!.enable()
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener?.enable()
    }

    override fun onPause() {
        super.onPause()
        orientationEventListener?.disable()
    }

    private fun showStatusBar() {
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    private fun doFullScreen() {
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when(newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> { //세로 전환
                showStatusBar()
                playerLayout.setPortraitView()
            }
            Configuration.ORIENTATION_LANDSCAPE -> { //가로 전환
                doFullScreen()
                playerLayout.setLandscapeView()
            }
        }
    }

    override fun onBackPressed() {
        if(!playerLayout.isFullScreen()) {
            super.onBackPressed()
        }
    }
}