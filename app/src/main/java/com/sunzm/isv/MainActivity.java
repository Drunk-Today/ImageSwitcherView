package com.sunzm.isv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sunzm.isv.lib.ImageSwitcherView;

public class MainActivity extends AppCompatActivity {

    private ImageSwitcherView mImageSwitcherView;

    private int[] images = new int[]{R.mipmap.test1, R.mipmap.test2, R.mipmap.test3};
    private int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageSwitcherView = findViewById(R.id.image_switcher_view);

        /** 设置动画时长 */
        mImageSwitcherView.setAnimatorDuration(500);
        /** 设置初始图片，第一次设置图片资源时，不执行动画 */
        mImageSwitcherView.setImageResource(images[curPosition]);

        mImageSwitcherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curPosition = (curPosition + 1) % images.length;
                mImageSwitcherView.setImageResource(images[curPosition]);
            }
        });
    }
}