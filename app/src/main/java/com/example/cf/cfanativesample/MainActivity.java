package com.example.cf.cfanativesample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clickforce.ad.Listener.AdNativeListener;
import com.clickforce.ad.NativeAd;

public class MainActivity extends AppCompatActivity  {

    private NativeAd nativeAd;
    private LinearLayout adView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nativeAd = new NativeAd(this);
        nativeAd.setAdID("5016");
        nativeAd.outputDebugInfo = true;

        nativeAd.setOnNativeListener(new AdNativeListener() {
            @Override
            public void onNativeAdResult(NativeAd nativeAd) {

                LinearLayout adContainer = (LinearLayout)findViewById(R.id.native_ad_container);
                LayoutInflater inflater = LayoutInflater.from(context);
                adView = (LinearLayout)inflater.inflate(R.layout.native_ad_layout,adContainer,false);
                adContainer.addView(adView);

                TextView nativeAdTitle = (TextView)adView.findViewById(R.id.native_ad_title);
                TextView nativeAdContent = (TextView)adView.findViewById(R.id.native_ad_content);
                TextView nativeAdvertiser = (TextView)adView.findViewById(R.id.native_ad_advertiser);
                Button nativeAdButtonText = (Button)adView.findViewById(R.id.native_ad_buttonText);
                ImageView nativeImage = (ImageView)adView.findViewById(R.id.native_ad_coverimage);

                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdContent.setText(nativeAd.getAdContent());
                nativeAdvertiser.setText(nativeAd.getAdvertiser());
                nativeAdButtonText.setText(nativeAd.getAdButtonText());

                nativeAd.downloadAndDisplayImage(nativeAd.getAdCoverImage(),nativeImage);
                nativeAd.registerViewForInteraction(adView);
            }

            @Override
            public void onNativeAdClick() {

            }

            @Override
            public void onNativeAdOnFailed() {

            }

            @Override
            public void getFBNativeID(String s) {

            }
        });
    }
}
