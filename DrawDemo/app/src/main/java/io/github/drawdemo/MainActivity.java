package io.github.drawdemo;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.drawdemo.databinding.ActivityMainBinding;

/**
 * 三种方式实现原角，xfermode,shader和clipPath。参考https://stackoverflow.com/questions/2459916/how-to-make-an-imageview-with-rounded-corners
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Bitmap xfermodeBitmap = ImageHelper.getBitmapFromResource(MainActivity.this,
                    R.drawable.sample,binding.ivXfermode.getMeasuredWidth(),
                    binding.ivXfermode.getMeasuredHeight());

            binding.ivXfermode.setImageBitmap(
                    ImageHelper.getRoundCornerBitmapByXfermode(xfermodeBitmap,30));

            Bitmap shaderBitmap = ImageHelper.getBitmapFromResource(MainActivity.this,
                    R.drawable.sample,binding.ivShader.getMeasuredWidth(),
                    binding.ivShader.getMeasuredHeight());
            binding.ivShader.setImageBitmap(
                    ImageHelper.getRoundCornerBitmapByXfermode(shaderBitmap,30));

            Bitmap roundImageViewBitmap = ImageHelper.getBitmapFromResource(MainActivity.this,
                    R.drawable.sample,binding.ivClipPath.getMeasuredWidth(),
                    binding.ivClipPath.getMeasuredHeight());

            binding.ivClipPath.setImageBitmap(roundImageViewBitmap);
        }
    }
}
