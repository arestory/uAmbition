package uambition.ares.ywq.uambition.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	private MemoryCache memoryCache = new MemoryCache();
	private AbstractFileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	private ExecutorService executorService;
	private int defaultImageId;

	public ImageLoader(Context context, int defaultImageId) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		this.defaultImageId = defaultImageId;

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ImageView imageView = (ImageView) msg.obj;
			Bitmap b = memoryCache.get(imageViews.get(imageView));
			if (b != null)
				imageView.setImageBitmap(b);
			else {
				imageView.setImageResource(defaultImageId);
			}
		}
	};

	/**
	 * 
	 * @param url
	 *            图片的URL或者文件的SD卡路径
	 * @param imageView
	 *            ImageView
	 * @param size
	 *            压缩图片最小的一条边到<size*2;小于100，则显示100*2
	 * @since type 0 表示url图片文件的网址，
	 * @since 1表示url是图片文件在SD卡上的绝对路径，
	 * @since 2表示url是视频文件在SD卡上的绝对路径，显示缩略小图
	 * @since 3，表示url是视频文件在SD卡上的绝对路径，显示大图
	 */
	public void DisplayImage(String url, ImageView imageView, int size, int type) {
		Log.i("xpf", "url="+url);
		imageViews.put(imageView, url);
		// 先从内存缓存中查找

		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {

			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView, size, type);
		}
	}

	private void queuePhoto(String url, ImageView imageView, int size, int type) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, size, type);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url, int size, int type) {
		Bitmap b = null;
		if (type == 0) {
			File f = fileCache.getFile(url);

			// 先从文件缓存中查找是否有

			if (f != null && f.exists()) {
				b = decodeFile(f, size, type);
			}

			if (b != null) {
				return b;
			}
			// 最后从指定的url中下载图片
			try {
				Bitmap bitmap = null;
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f, size, type);
				return bitmap;
			} catch (Exception ex) {
				Log.e("ImageLoader", "ImageLoader downLoad Image catch Exception... " + ex.getMessage());
				return null;
			}
		} else if (type == 1) {// SD卡上的图片文件
			File f2 = new File(url);
			if (f2 != null && f2.exists()) {
				b = decodeFile(f2, size, type);
			}
		} else if (type == 2) {// 视频文件小图
			b = ThumbnailUtils.createVideoThumbnail(url, Images.Thumbnails.MICRO_KIND);
		} else if (type == 3) {// 视频文件 大图
			b = ThumbnailUtils.createVideoThumbnail(url, Images.Thumbnails.MINI_KIND);
		}
		return b;
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	public Bitmap decodeFile(File f, int size, int type) {
		try {
			if (size < 100) {
				size = 100;
			}
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = size;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			if (type == 1) {// 从SD卡读图片时旋转
				// 旋转图片
				int degrees = readPictureDegree(f.getAbsolutePath());
				if (degrees != 0 && bitmap != null) {
					Matrix m = new Matrix();
					m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				}
			}
			return bitmap;
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public Bitmap decodeRes(Context context, int resid, int size) {
		if (size < 100) {
			size = 100;
		}
		// decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resid, o);

		// Find the correct scale value. It should be the power of 2.
		final int REQUIRED_SIZE = size;
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resid, o2);
		memoryCache.put(resid + "", bitmap);
		return bitmap;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public int size;
		public int type;

		public PhotoToLoad(String u, ImageView i, int size, int type) {
			url = u;
			imageView = i;
			this.size = size;
			this.type = type;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			Bitmap bmp = getBitmap(photoToLoad.url, photoToLoad.size, photoToLoad.type);
			memoryCache.put(photoToLoad.url, bmp);
			Message message = handler.obtainMessage();
			message.obj = photoToLoad.imageView;
			handler.sendMessage(message);
		}
	}

	// /**
	// * 防止图片错位
	// *
	// * @param photoToLoad
	// * @return
	// */
	// boolean imageViewReused(PhotoToLoad photoToLoad) {
	// String tag = imageViews.get(photoToLoad.imageView);
	// if (tag == null || !tag.equals(photoToLoad.url))
	// return true;
	// return false;
	// }

	// // 用于在UI线程中更新界面
	// class BitmapDisplayer implements Runnable {
	// Bitmap bitmap;
	// PhotoToLoad photoToLoad;
	//
	// public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
	// bitmap = b;
	// photoToLoad = p;
	// }
	//
	// public void run() {
	// if (imageViewReused(photoToLoad))
	// return;
	// if (bitmap != null)
	// photoToLoad.imageView.setImageBitmap(bitmap);
	//
	// }
	// }

	public void clearCache() {
		memoryCache.clear();
	}

	public void deleteFiles() {
		fileCache.clear();
	}

	public void remove(String url) {
		if (url != null) {
			memoryCache.remove(url);
		}
	}

	public boolean isPic(String fileName) {
		boolean flag = false;
		if (fileName == null)
			return false;
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if ("jpg".equalsIgnoreCase(fileExt) || "png".equalsIgnoreCase(fileExt) || "gif".equalsIgnoreCase(fileExt) || "jpeg".equalsIgnoreCase(fileExt)) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	private static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			Log.e("", "CopyStream catch Exception...");
		}
	}

	/**
	 * 获取图片的旋转角度
	 * 
	 * @Title: readPictureDegree
	 * @param path
	 * @return int
	 * @date 2013-11-27 上午9:22:33
	 */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {

			ExifInterface exifInterface = new ExifInterface(path);
			// 得到照片的信息，ExifInterface.TAG_MAKE拍照设备型号,ExifInterface.TAG_MODEL拍照设备品牌，ExifInterface.TAG_ORIENTATION照片旋转角度
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				degree = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 执行后台操作
	 * 
	 * @param arg0
	 */
	public void doInBackgroud(Runnable arg0) {
		executorService.submit(arg0);
	}

	private Set<String> urls = new HashSet<String>();

	// 对外界开放的回调接口
	public interface BigImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(String url, Bitmap bmp);
	}

	// 下载大图片 ，带回调接口
	public Bitmap loadBigPic(String url, int size,BigImageCallback bigImageCallback) {
		Bitmap bitmap=null;
		if (url != null) {
			// 先从内存缓存中查找
		   bitmap = memoryCache.get(url);
			if (bitmap != null) {
				bigImageCallback.imageLoaded(url, bitmap);
			} else {
				// 从文件缓存中读取
				// 若没有的话则开启新线程加载图片
				executorService.submit(new LoadPic(url,size, bigImageCallback));
			}
			
		}
		return bitmap;
	}

	// 下载图片，保存到cache中
	class LoadPic implements Runnable {
		String url;
		BigImageCallback bigImageCallback;
		int size;

		LoadPic(String url, int size,BigImageCallback bigImageCallback) {
			this.url = url;
			this.bigImageCallback = bigImageCallback;
			this.size=size;
		}

		@Override
		public void run() {
			File f = fileCache.getFile(url);
			// 先从文件缓存中查找是否有
			Bitmap b = null;
			if (f != null && f.exists()) {
				b = decodeFile(f, size, 0);
			} else if (f != null && !f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			if (b != null) {
				memoryCache.put(url, b);
				bigImageCallback.imageLoaded(url, b);
				return;
			}
			if (urls.contains(url)) {
				// 在下载队列中，不下载
				return;
			} else {
				// 没有下载，添加到下载队列
				urls.add(url);
			}
			// 最后从指定的url中下载图片
			try {
				Bitmap bitmap = null;
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f, size, 0);
				if (bitmap != null) {
					memoryCache.put(url, bitmap);
				}
				urls.remove(url);
				bigImageCallback.imageLoaded(url, bitmap);
			} catch (Exception ex) {
				// memoryCache.put(url, GlobalVariables.wrong_pic);
				// bigImageCallback.imageLoaded(url);
				urls.remove(url);
				bigImageCallback.imageLoaded(url, null);
			}
		}
	}
	 public static Bitmap loadImage(String url) {  
	        Bitmap bitmap = null;  
	        HttpClient client = new DefaultHttpClient();  
	        HttpResponse response = null;  
	        InputStream inputStream = null;  
	        try {  
	            response = client.execute(new HttpGet(url));  
	            HttpEntity entity = response.getEntity();  
	            inputStream = entity.getContent();  
	            bitmap = BitmapFactory.decodeStream(inputStream);  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return bitmap;  
	    }  
}