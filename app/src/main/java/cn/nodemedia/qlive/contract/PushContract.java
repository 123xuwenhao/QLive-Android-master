package cn.nodemedia.qlive.contract;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import cn.nodemedia.NodeCameraView;
import cn.nodemedia.NodePublisher;
import cn.nodemedia.NodePublisherDelegate;
import cn.nodemedia.qlive.R;
import xyz.tanwb.airship.utils.Log;
import xyz.tanwb.airship.utils.ToastUtils;
import xyz.tanwb.airship.view.BasePresenter;
import xyz.tanwb.airship.view.BaseView;

public interface PushContract {

    interface View extends BaseView {

        NodeCameraView getNodeCameraView();

        void flashChange(boolean onOrOff);
    }

    class Presenter extends BasePresenter<View> implements NodePublisherDelegate {

        private SharedPreferences sp;
        private NodePublisher nodePublisher;

        private boolean isFlashEnable = false;

        @Override
        public void onStart() {

            // 得到我们的存储Preferences值的对象，然后对其进行相应操作
            sp=mContext.getSharedPreferences("data", Context.MODE_PRIVATE);

            String streamURL ="rtmp://47.99.171.180:2935/live/"+sp.getString("streamId", null);

            nodePublisher = new NodePublisher(mContext,"M2FmZTEzMGUwMC00ZTRkNTMyMS1jbi5ub2RlbWVkaWEucWxpdmU=-OTv6MJuhXZKNyWWMkdKJWsVKmLHwWPcPfnRbbWGIIf+8t39TqL/mW2f5O5WdT/W8JJE7ePvkvKaS371xVckAZ/U00dSwPp8ShB8Yic2W1GhwCyq04DYETsrGnkOWrhARH7nzNhd3Eq6sVC1Fr74GCEUHbDSCZnCfhcEnzGU9InRiQJ2PImtHORahN3blAGlHb6LZmdnobw5odvKEeUhbkhxYf8S1Fv4VRnSpDCSS3LZ2U3Mp6MfGDA1ZXPadmgdwaJitIrnWA2zP/yqmlUHjMtTv8PzGcc73Tm5k5q+OMbKCJsPq8KSEpFthncvaGZJ2kS2GHx6V5TqYZglBrTx61g==");
            nodePublisher.setNodePublisherDelegate(this);
            nodePublisher.setOutputUrl(streamURL);
            nodePublisher.setCameraPreview(mView.getNodeCameraView(), 0, true);
            nodePublisher.setVideoParam(1, 15, 500000, 0, false);
            nodePublisher.setKeyFrameInterval(1);
            nodePublisher.setAudioParam(32000, 1, 44100);
            nodePublisher.setDenoiseEnable(true);
            nodePublisher.setHwEnable(true);
            nodePublisher.setBeautyLevel(0);
            nodePublisher.setCryptoKey("");
            nodePublisher.startPreview();
        }

        //开始直播
        public void pushStart() {
            nodePublisher.start();
        }

        //关闭直播
        public void pushStop() {
            nodePublisher.stop();
        }

        //关闭录音
        public void audioStop() {
            nodePublisher.setAudioEnable(false);
        }

        //开启录音
        public void audioStart() {
            nodePublisher.setAudioEnable(true);
        }

        //设置美颜等级
        public void setBeautyLevel(int i){
            nodePublisher.setBeautyLevel(i);
        }

        public int switchCamera() {
            int ret = nodePublisher.switchCamera();
            if(ret > 0) {
                mView.flashChange(false);
            }
            return ret;
        }

        public int switchFlash() {
            boolean flashEnable = !this.isFlashEnable;
            int ret = nodePublisher.setFlashEnable(flashEnable);
            this.isFlashEnable = ret == 1;
            mView.flashChange(this.isFlashEnable);
            return ret;
        }

        @Override
        public void onDestroy() {
            nodePublisher.stopPreview();
            nodePublisher.stop();
            nodePublisher.release();
            super.onDestroy();
        }

        @Override
        public void onEventCallback(NodePublisher nodePublisher, int event, String msg) {
            Log.d("EventCallback:" + event + " msg:" + msg);
            handler.sendEmptyMessage(event);
        }

        private Handler handler = new Handler() {
            // 回调处理
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 2000:
                        ToastUtils.show(mContext, mContext.getString(R.string.toast_2000));
                        break;
                    case 2001:
                       ToastUtils.show(mContext, mContext.getString(R.string.toast_2001));
                        break;
                    case 2002:
                        ToastUtils.show(mContext, mContext.getString(R.string.toast_2002));
                        break;
                    case 2004:
                       ToastUtils.show(mContext, mContext.getString(R.string.toast_2004));
                        break;
                    case 2005:
                        ToastUtils.show(mContext, mContext.getString(R.string.toast_2005));
                        break;
                }
            }
        };
    }
}
