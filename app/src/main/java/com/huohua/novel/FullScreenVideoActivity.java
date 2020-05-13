package com.huohua.novel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;

import androidx.annotation.Nullable;

public class FullScreenVideoActivity extends Activity {
    private static final String TAG = "FullScreenVideoActivity";
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;
    private String mVerticalCodeId;

    @SuppressWarnings("RedundantCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();

        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getApplicationContext());
        getExtraInfo();
        loadAd(mVerticalCodeId, TTAdConstant.VERTICAL);
    }


    private void getExtraInfo() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mVerticalCodeId = intent.getStringExtra("vertical_rit");
    }

    private boolean mHasShowDownloadActive = false;

    @SuppressWarnings("SameParameterValue")
    public void loadAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(true)
                    .setOrientation(orientation)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();

        //step5:请求广告
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "onError: " + code + ", " + String.valueOf(message));
                // TToast.show(FullScreenVideoActivity.this, message);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                Log.e(TAG, "onFullScreenVideoAdLoad");

                mttFullVideoAd = ad;
                if (mttFullVideoAd != null) {
                    //step6:在获取到广告后展示
                    //该方法直接展示广告
                    mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this);

                    // TToast.show(FullScreenVideoActivity.this, "FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));
                    mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                        @Override
                        public void onAdShow() {
                            // TToast.show(FullScreenVideoActivity.this, "FullVideoAd show");
                        }

                        @Override
                        public void onAdVideoBarClick() {
                            // TToast.show(FullScreenVideoActivity.this, "FullVideoAd bar click");
                        }

                        @Override
                        public void onAdClose() {
                            // TToast.show(FullScreenVideoActivity.this, "FullVideoAd close");
                            FullScreenVideoActivity.this.finish();
                        }

                        @Override
                        public void onVideoComplete() {
                            // TToast.show(FullScreenVideoActivity.this, "FullVideoAd complete");
                            FullScreenVideoActivity.this.finish();
                        }

                        @Override
                        public void onSkippedVideo() {
                            // TToast.show(FullScreenVideoActivity.this, "FullVideoAd skipped");
                            FullScreenVideoActivity.this.finish();
                        }

                    });

                    //展示广告，并传入广告展示的场景
                    //mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this, TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
                    mttFullVideoAd = null;
                } else {
                    // TToast.show(FullScreenVideoActivity.this, "请先加载广告");
                }

                ad.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            // TToast.show(FullScreenVideoActivity.this, "下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        // TToast.show(FullScreenVideoActivity.this, "下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        // TToast.show(FullScreenVideoActivity.this, "下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Log.d("DML", "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                        // TToast.show(FullScreenVideoActivity.this, "下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Log.d("DML", "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                        // TToast.show(FullScreenVideoActivity.this, "安装完成，点击下载区域打开", Toast.LENGTH_LONG);
                    }
                });
            }

            @Override
            public void onFullScreenVideoCached() {
                Log.e(TAG, "onFullScreenVideoCached");
                // TToast.show(FullScreenVideoActivity.this, "FullVideoAd video cached");
            }
        });


    }

    private String getAdType(int type) {
        switch (type) {
            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;
        }

        return "未知类型+type=" + type;
    }
}
