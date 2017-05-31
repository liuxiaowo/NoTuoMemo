package com.example.memo;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class MemoApplication extends Application {
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
		.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
		.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
		.discCacheFileCount(60)// 缓存文件的最大个数
		.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
		.build();
		ImageLoader.getInstance().init(config);
	}

}
