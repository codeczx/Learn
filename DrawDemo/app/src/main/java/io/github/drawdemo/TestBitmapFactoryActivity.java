package io.github.drawdemo;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import io.github.drawdemo.databinding.ActivityTestBitmapFactoryBinding;

public class TestBitmapFactoryActivity extends AppCompatActivity {

    private static final String TAG = "TestBitmapFactoryActivi";
    private ActivityTestBitmapFactoryBinding binding;
    private String url = "https://upload-images.jianshu.io/upload_images/2223621-af3759178eb3d07d.jpg";
    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_bitmap_factory);

        new Thread(() -> {
            Bitmap bitmap = ImageHelper.getBitmapFromNetwork(url,300,300);
            Message msg = Message.obtain();
            msg.obj = bitmap;
            mHandler.sendMessage(msg);
        }).start();
    }

    private static class MyHandler extends Handler{

        private WeakReference<TestBitmapFactoryActivity> activityWeakReference;

        MyHandler(TestBitmapFactoryActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TestBitmapFactoryActivity activity = activityWeakReference.get();
            if(activity!=null){
                activity.binding.iv.setImageBitmap((Bitmap) msg.obj);
            }
        }
    }
}
