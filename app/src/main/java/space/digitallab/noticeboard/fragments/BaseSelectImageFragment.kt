package space.digitallab.noticeboard.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import space.digitallab.noticeboard.R

open class BaseAdsFragment: Fragment(), InterAdsClose {

    lateinit var adView: AdView
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterAd()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun initAds(){

        MobileAds.initialize(activity as Activity)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun loadInterAd(){

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(context as Activity, getString(R.string.ad_inter_id), adRequest, object : InterstitialAdLoadCallback(){

            override fun onAdLoaded(ad: InterstitialAd) {
                mInterstitialAd = ad

            }
        })
    }

    fun showInterAd(){

        if(mInterstitialAd != null){

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback(){

                override fun onAdDismissedFullScreenContent() {

                    onClose()

                }

                override fun onAdFailedToShowFullScreenContent(ad: AdError) {

                    onClose()

                }
            }
            mInterstitialAd?.show(activity as Activity)

        }else{

            onClose()

        }
    }

    override fun onClose() {}
}