package com.cmd.hit.zhihudaily.other;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by PC-0775 on 2019/5/4.
 */

public class PhotoCacheHelper {
    //内存缓存
    private LruCache<String, Bitmap> mMemoryCache;
    //硬盘缓存
    private DiskLruCache mDiskCache;
    //OkhttpClient
    private OkHttpClient okHttpClient;
    //线程池 用来请求下载图片
    private ExecutorService service;
    //主线程Handler 用来图片下载完成后更新ImageView
    private Handler mHandler;

    private static PhotoCacheHelper photoCacheHelper;

    public static PhotoCacheHelper getInstance(){
        return photoCacheHelper;
    }

    public static void init(Context context){
        SoftReference<Context> softReference = new SoftReference<>(context);
        photoCacheHelper = new PhotoCacheHelper(softReference.get());
    }

    public PhotoCacheHelper(Context context){
        this(context, new Handler(Looper.getMainLooper()));
    }

    public PhotoCacheHelper(Context context, Handler mHandler){
        this.mHandler = mHandler;
        config(context);
    }

    //初始化
    private void config(Context context){
        okHttpClient = new OkHttpClient.Builder().build();

        //构建一定数量的线程池
        service = Executors.newFixedThreadPool(6);

        // 构建内存缓存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        // 构建硬盘缓存实例
        File file = getDiskCacheDir("photo", context);
        if (!file.exists()){
            file.mkdirs();
        }
        try {
            mDiskCache = DiskLruCache.open(file, getAppInfoVersion(context), 1, 10*1024*1024);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 根据传入的文件名，获取缓存文件夹路径
     * @param uniqueName
     * @return
     */
    private File getDiskCacheDir(String uniqueName, Context context){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath+File.separator + uniqueName);
    }

    /**
     * 获取app版本信息
     * @return
     */
    private int getAppInfoVersion(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    /**
     * 将bitmap对象加载到imageVeiw中，如果不在缓存中就开启线程去查询本地
     * @param imageView 图片容器
     * @param url       图片地址
     */
    public void loadBitmap(String url, ImageView imageView){
        //从缓存中获取Bitmap
        Bitmap bitmap = mMemoryCache.get(url);
        //如果不在缓存中
        if(null != bitmap){
            if (imageView != null){
                imageView.setImageBitmap(bitmap);
            }
        }else {
            //开启线程查询
            service.execute(new ImageRunnable(url, imageView));
        }
    }


    //应该改名为getBitmap!!!
    public void getBitmap(String url, int what){
        //从缓存中获取Bitmap
        Bitmap bitmap = mMemoryCache.get(url);
        //如果在缓存中
        if(null != bitmap){
            LogUtil.d("白盒测试", "内存中存在数据");
            sendBitmapByMessage(bitmap, what);
        }else {
            LogUtil.d("白盒测试", "内存中不存在数据");
            //开启线程查询
            service.execute(new ImageRunnable(url, what));
        }
    }

    /**
     * 使用MD5进行加密,获取缓存的key
     * @param key   图片地址
     * @return      MD5加密后的串
     */
    private String hashKeyForDisk(String key){
        String cacheKey;
        //用MessageDigest获取MD5实例
        try {
            //MessageDigest类是什么
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            //进行MD5加密
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 将比特数据进行MD5加密
     * @param bytes
     * @return
     */
    private String bytesToHexString(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes){
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 下载图片
     * @param url           图片地址
     * @param outputStream  输出流
     * @return
     */
    private boolean downloadImage(final String url, OutputStream outputStream){

        Request request = new Request.Builder()
                .url(url)
                .build();
        //Call实例
        Call call = okHttpClient.newCall(request);
        //结果实例Response
        Response response = null;
        //输入缓冲流
        BufferedInputStream in = null;
        //输出缓冲流
        BufferedOutputStream out = null;
        try {
            response = call.execute();
            in = new BufferedInputStream(response.body().byteStream(), 8*1024);
            out = new BufferedOutputStream(outputStream, 8*1024);
            int b;
            LogUtil.d("白盒测试", "网络获取成功");
            while ((b = in.read()) != -1){
                out.write(b);
            }
            LogUtil.d("白盒测试", "将数据写入硬盘");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将Bitmao添加到缓存
     * @param bitmap
     * @param url
     */
    private void addBitmapToMemoryCache(Bitmap bitmap, String url){
        //如果内存缓存中获取不到,将其添加到内存缓存
        if (null == getBitmapFromMemoryCache(url)){
            mMemoryCache.put(url, bitmap);
            LogUtil.d("白盒测试", "将数据加载入内存");
        }
    }

    /**
     * 从内存中获取Bitmap
     * @param url   图片地址
     * @return      图片
     */
    private Bitmap getBitmapFromMemoryCache(String url){
        return mMemoryCache.get(url);
    }

    //取消所有的下载程序
    public void cancelDownloadImage(){
        service.shutdown();
    }

    //将硬盘缓存同步到Journal文件中
    public void flushCache(){
        if (mDiskCache != null){
            try {
                mDiskCache.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void clearDiskMemory(){
        try {
            mDiskCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ImageRunnable implements Runnable{
        private String url;
        private Bitmap bitmap;
        private ImageView imageView;
        private int what;

        ImageRunnable(String url, int what){
            this.url = url;
            this.what = what;
        }

        ImageRunnable(String url, ImageView imageView){
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            FileDescriptor fileDescriptor = null;
            FileInputStream fileInputStream;
            DiskLruCache.Snapshot snapshot;
            String key = hashKeyForDisk(url);
            //查找key对应的硬盘缓存

            try {
                snapshot = mDiskCache.get(key);
                if (null == snapshot){
                    LogUtil.d("白盒测试", "硬盘中不存在数据");
                    // 如果对应的硬盘缓存没找到，就开始网络请求，并且写入缓存
                    DiskLruCache.Editor editor = mDiskCache.edit(key);
                    if (editor != null){
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downloadImage(url, outputStream)){
                            editor.commit();
                        }else {
                            LogUtil.d("白盒测试", "网络获取失败");
                            editor.abort();
                        }
                    }
                    snapshot = mDiskCache.get(key);
                }
                if (snapshot != null){
                    fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                if (fileDescriptor != null){
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //返回bitmap
            if (bitmap != null){
                //将图片添加到内存缓存中
                addBitmapToMemoryCache(bitmap, url);
                if(imageView != null){
                    mHandler.post(() -> imageView.setImageBitmap(bitmap));
                }
                sendBitmapByMessage(bitmap, what);
            }
        }
    }

    /**
     * 将bitmap发送回主线程
     * @param bitmap 图片
     */
    private void sendBitmapByMessage(Bitmap bitmap, int what){
        LogUtil.d("白盒测试", "返回数据");
        if (0 == what){
            return;
        }
        //发送消息
        Message message = Message.obtain();
        message.what = what;
        message.obj = bitmap;
        mHandler.sendMessage(message);
    }
}
