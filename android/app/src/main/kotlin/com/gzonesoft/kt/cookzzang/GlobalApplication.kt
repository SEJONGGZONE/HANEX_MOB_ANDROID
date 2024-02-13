package com.gzonesoft.kt.cookzzang

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.kakao.sdk.common.Constants
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption

class GlobalApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "f8ec2fc929669ed1eacc2847664021f5")

        if (NaviClient.instance.isKakaoNaviInstalled(applicationContext)) {
            var keyHash = Utility.getKeyHash(this)
            Log.i("TAG", "카카오내비 앱으로 길 안내 가능")
            Log.i("TAG", keyHash)
            // CUJ6inhGdlq8j0gnO6Fai13oSX4=
            //launchNavi()
        } else {
            Log.i("TAG", "카카오내비 미설치")
        }

    }

}

class KakaoService {
    fun launchNavi(act: Activity, context: Context, title:String, xPos: String, yPos: String) {
        // 카카오내비 앱으로 길 안내
        if (NaviClient.instance.isKakaoNaviInstalled(context)) {
            // 카카오내비 앱으로 길 안내 - WGS84
            act.startActivity(
                NaviClient.instance.navigateIntent(
                    Location(title, xPos, yPos),
                    NaviOption(coordType = CoordType.WGS84)
                )
            )
        } else {
            // 카카오내비 설치 페이지로 이동
            act.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    // Uri.parse(Constants.WEB_NAVI_INSTALL)
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }
}
