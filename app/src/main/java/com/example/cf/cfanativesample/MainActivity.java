package com.example.cf.cfanativesample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clickforce.ad.Listener.AdNativeListener;
import com.clickforce.ad.CFNativeAd;
import com.facebook.ads.AdError;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity  {

    private CFNativeAd nativeAd;
    private LinearLayout adView;
    private Context context = this;

    private LinearLayout adContainer ;
    private LayoutInflater inflater ;

    private TextView nativeAdTitle ;
    private TextView nativeAdContent;
    private TextView nativeAdvertiser ;
    private Button nativeAdButtonText ;
    private ImageView nativeImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adContainer = (LinearLayout)findViewById(R.id.native_ad_container);
        inflater = LayoutInflater.from(this);
        adView = (LinearLayout)inflater.inflate(R.layout.native_ad_layout,adContainer,false);
        adContainer.addView(adView);

        nativeAdTitle = (TextView)adView.findViewById(R.id.native_ad_title);
        nativeAdContent = (TextView)adView.findViewById(R.id.native_ad_content);
        nativeAdvertiser = (TextView)adView.findViewById(R.id.native_ad_advertiser);
        nativeAdButtonText = (Button)adView.findViewById(R.id.native_ad_buttonText);
        nativeImage = (ImageView)adView.findViewById(R.id.native_ad_coverimage);

        nativeAd = new CFNativeAd(this);
        nativeAd.setAdID("5236"); //測試版位
        nativeAd.outputDebugInfo = true;

        nativeAd.setOnNativeListener(new AdNativeListener() {
            @Override
            public void onNativeAdResult(CFNativeAd cfNativeAd) {

                nativeAdTitle.setText(cfNativeAd.getAdTitle());
                nativeAdContent.setText(cfNativeAd.getAdContent());
                nativeAdvertiser.setText(cfNativeAd.getAdvertiser());
                nativeAdButtonText.setText(cfNativeAd.getAdButtonText());

                cfNativeAd.downloadAndDisplayImage(cfNativeAd.getAdCoverImage(),nativeImage);
                cfNativeAd.registerViewForInteraction(adView,nativeAdButtonText);
            }

            @Override
            public void onNativeAdClick() {

            }

            @Override
            public void onNativeAdOnFailed() {

            }

            @Override
            public void onFBNativeAdResult(CFNativeAd cfNativeAd) {

                nativeAdTitle.setText(cfNativeAd.getFBAdTitle());
                nativeAdContent.setText(cfNativeAd.getFBAdBody());
                nativeAdvertiser.setText("Sponsored");
                nativeAdButtonText.setText(cfNativeAd.getFBAdCallToAction());

                downloadAndDisplayImage(cfNativeAd.getFBAdCoverImageURL(),nativeImage);
                cfNativeAd.registerViewForInteraction(adView,nativeAdButtonText);
            }

            @Override
            public void onFBNativeClick() {

            }

            @Override
            public void onFBNativeImpression() {

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
