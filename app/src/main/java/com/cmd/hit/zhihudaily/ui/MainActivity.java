package com.cmd.hit.zhihudaily.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.model.bean.LatestNews;
import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.ServiceCreator;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;
import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;
import com.cmd.hit.zhihudaily.other.SPUtil;
import com.cmd.hit.zhihudaily.ui.view.ImageBannerFarmLayout;
import com.cmd.hit.zhihudaily.viewModel.MainActivityModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-0775 on 2019/4/26.
 */
//链接MUMU模拟器的命令行，命令：adb connect 127.0.0.1:7555
public class MainActivity extends AppCompatActivity implements ImageBannerFarmLayout.FramLayoutListener {

    //图品资源
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

    //view
    ImageBannerFarmLayout mGroup;

    //resourcess
    private List<Bitmap> bitmapList = new ArrayList<>();

    //model
    private MainActivityModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListener();
        doBusiness();
       /* for(int i = 0; i < imageUrl.length; i++){
            //拿到图片资源
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[i]);
            //将图片添加到list中
            list.add(bitmap);
            //可以再添加一个list存放图片的索引id
        }*/


    }

    private void init(){
        //设置图片完全填充
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ImageBannerFarmLayout.WIDTH = dm.widthPixels;

        //轮播图
        mGroup = findViewById(R.id.image_group);
        //SPUtil
        SPUtil.setContext(this);

        //model
        NewsDao dao = new NewsDao();
        NewsService service = ServiceCreator.getInstance().create(NewsService.class);
        NewsRepository repository = new NewsRepository(dao, service);
        model = new MainActivityModel(repository, this);
    }

    private void setListener(){
        mGroup.setListener(this);//设置点击事件
        model.setOnGetBitmapListener(new PhotoCacheHelper.OnGetBitmapListener() {
            @Override
            public void onGetBitmap(Bitmap bitmap) {
                bitmapList.add(bitmap);
//                mGroup.addBitmaps(bitmapList);
            }
        });

    }

    private void doBusiness(){
        model.getLatestNewsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(latestNews -> Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    for (LatestNews.TopStoriesBean bean : latestNews.getTop_stories()){
                        emitter.onNext(bean.getImage());
                    }
                }))
                .subscribe(s -> {
                    model.loadBitmap(s);
                });

    }

    @Override
    public void clickImageIndex(int pos) {
        Toast.makeText(this,"pos=" + pos, Toast.LENGTH_SHORT).show();
    }
}
