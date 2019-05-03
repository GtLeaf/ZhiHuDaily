package com.cmd.hit.zhihudaily.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.ui.view.ImageBarnnerFramLayout;
import com.cmd.hit.zhihudaily.ui.view.ImageBarnnerViewGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by PC-0775 on 2019/4/26.
 */
//链接MUMU模拟器的命令行，命令：adb connect 127.0.0.1:7555
public class MainActivity extends AppCompatActivity implements ImageBarnnerFramLayout.FramLayoutListener {

    private ImageBarnnerFramLayout mGroup;
    private int[] ids = new int[]{
        R.drawable.image_0, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_5,
            R.drawable.image_6
    };
    public String[] imageUrl = new String[]{
        "https://pic4.zhimg.com/v2-8b8c2bffdcf1de2d2a92cf2e01a8db93.jpg",
            "https://pic4.zhimg.com/v2-107b89ac48f7b176242773be48e1eeaf.jpg",
            "https://pic2.zhimg.com/v2-5599a0274655bf331451cb8123647269.jpg",
            "https://pic1.zhimg.com/v2-5b3e7329c52777a9e2002a36ad83a73c.jpg",
            "https://pic4.zhimg.com/v2-0ef521d955bce4c1bc63073215d88a07.jpg"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置图片完全填充
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ImageBarnnerFramLayout.WIDTH = dm.widthPixels;

        mGroup = findViewById(R.id.image_group);
        mGroup.setListener(this);
        List<Bitmap> list = new ArrayList<>();
        for(int i = 0; i < imageUrl.length; i++){

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[i]);
            list.add(bitmap);
        }
        mGroup.addBitmaps(list);
    }

    private Drawable loadImageFromNetwork(String imageUrl)
    {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }
        return drawable ;
    }

    @Override
    public void clickImageIndex(int pos) {
        Toast.makeText(this,"pos=" + pos, Toast.LENGTH_SHORT).show();
    }
}
