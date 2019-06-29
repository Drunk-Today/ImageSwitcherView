# ImageSwitcherView
一个更方便的图片切换控件
目前只支持match_parent属性
使用示例：

布局中

    <?xml version="1.0" encoding="utf-8"?>
    <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context="com.sunzm.isv.MainActivity">

        <com.sunzm.isv.lib.ImageSwitcherView
            android:id="@+id/image_switcher_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </android.support.constraint.ConstraintLayout>

代码中
    int[] images = new int[]{R.mipmap.test1, R.mipmap.test2, R.mipmap.test3};
    int curPosition = 0;
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
        
