package com.example.cf.cfanativesample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clickforce.ad.Listener.AdNativeListener;
import com.clickforce.ad.CFNativeAd;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private CFNativeAd nativeAd;

    private Context context = this;
    private LinearLayout adView;
    private LinearLayout adContainer ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nativeAd = new CFNativeAd(this);
        nativeAd.setAdID("8384"); //測試版位 FB廣告版位:5236
        nativeAd.outputDebugInfo = true;

        nativeAd.setOnNativeListener(new AdNativeListener() {
            @Override
            public void onNativeAdResult(CFNativeAd cfNativeAd) {
                adContainer = (LinearLayout)findViewById(R.id.native_ad_container);
                LayoutInflater inflater = LayoutInflater.from(context);
                adView = (LinearLayout)inflater.inflate(R.layout.native_ad_layout,adContainer,false);
                adContainer.addView(adView);

                TextView nativeAdTitle = (TextView)adView.findViewById(R.id.native_ad_title);
                TextView nativeAdContent = (TextView)adView.findViewById(R.id.native_ad_content);
                TextView nativeAdvertiser = (TextView)adView.findViewById(R.id.native_ad_advertiser);
                Button nativeAdButtonText = (Button)adView.findViewById(R.id.native_ad_buttonText);
                ImageView nativeImage = (ImageView)adView.findViewById(R.id.native_ad_coverimage);


                nativeAdTitle.setText(cfNativeAd.getAdTitle());
                nativeAdContent.setText(cfNativeAd.getAdContent());
                nativeAdvertiser.setText(cfNativeAd.getAdvertiser());
                nativeAdButtonText.setText(cfNativeAd.getAdButtonText());

                cfNativeAd.downloadAndDisplayImage(cfNativeAd.getAdCoverImage(),nativeImage);

                List<View> clickListView = new ArrayList<>();
                clickListView.add(nativeAdTitle);
                clickListView.add(nativeAdButtonText);
                cfNativeAd.registerViewForInteraction(clickListView);
                nativeAd.setActiveViewLog(adContainer);

            }

            @Override
            public void onNativeAdClick() {

            }

            @Override
            public void onNativeAdOnFailed() {
                Log.d("error","NO Error");
            }

            @Override
            public void onFBNativeAdResult(CFNativeAd cfNativeAd) {
                adContainer = (LinearLayout)findViewById(R.id.native_ad_container);
                LayoutInflater inflater = LayoutInflater.from(context);
                adView = (LinearLayout)inflater.inflate(R.layout.native_ad_layout,adContainer,false);
                adContainer.addView(adView);

                TextView nativeAdTitle = (TextView)adView.findViewById(R.id.native_ad_title);
                TextView nativeAdContent = (TextView)adView.findViewById(R.id.native_ad_content);
                TextView nativeAdvertiser = (TextView)adView.findViewById(R.id.native_ad_advertiser);
                Button nativeAdButtonText = (Button)adView.findViewById(R.id.native_ad_buttonText);
                AdIconView adIconView = (AdIconView)adView.findViewById(R.id.native_ad_icon);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);

                nativeAdTitle.setText(cfNativeAd.getFBAdvertiserName());
                nativeAdContent.setText(cfNativeAd.getFBAdBody());
                nativeAdvertiser.setText("Sponsored");
                nativeAdButtonText.setText(cfNativeAd.getFBAdCallToAction());

                LinearLayout adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
                adChoicesContainer.addView(cfNativeAd.getFBAdChoicesView());

                //Set Click
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdContent);
                clickableViews.add(nativeAdButtonText);

                cfNativeAd.setFbRegisterViewForInteraction(adView,nativeAdMedia,adIconView,clickableViews);
            }

            @Override
            public void onFBNativeClick() {
                Log.d("FB","onFBNativeClick");
            }

            @Override
            public void onFBNativeImpression() {
                Log.d("FB","onFBNativeImpression");
            }

            @Override
            public void onFBNativeError(AdError adError) {
                Log.d("FB error",adError.getErrorMessage());
            }
        });



    }


    public void downloadAndDisplayImage(final String coverImg, final ImageView imageView){

        AsyncTask<String, Void, Bitmap> task = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Bitmap doInBackground(String... params) {

                return getBitmapFromURL(coverImg);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                imageView.setImageBitmap(bitmap);
            }

        };
        task.execute();
    }

    //讀取網路圖片，型態為Bitmap
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
