package cn.edu.scujcc.downloadandupload;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.File;

import cn.edu.scujcc.downloadandupload.activity.MainActivity;
import cn.edu.scujcc.downloadandupload.api.DownloadListener;

/**
 * @author Administrator
 */
public class DownloadService extends Service {

    private DownloadTask downloadTask;
    private String downloadUrl;

    //匿名类
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            //下载成功时，将前台服务通知关闭，创建一个新的通知用于告诉用户下载成功了
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            //下载失败时，将前台服务通知关闭，创建一个新的通知用于告诉用户下载失败
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    };


    private DownloadBinder mBinder = new DownloadBinder();

//    public DownloadService() {
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //触发下载进度的这个通知
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        String description = getString(R.string.channel_description);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, description);
        // 用户可以看到的通知渠道的描述
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = null;
            if (mNotificationManager != null) {
                notificationChannel = mNotificationManager.getNotificationChannel("MyService");
            }
            if (notificationChannel == null) {
                NotificationChannel channel = new NotificationChannel("MyService",
                        "com.example.liyun.testservice", NotificationManager.IMPORTANCE_HIGH);
                //是否在桌面icon右上角展示小红点
                channel.enableLights(true);
                //小红点颜色
                channel.setLightColor(Color.RED);
                //通知显示
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                //是否在久按桌面图标时显示此渠道的通知
                //channel.setShowBadge(true);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }
            }
            mBuilder.setContentTitle(title);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            mBuilder.setContentIntent(pi);
            mBuilder.build();
            if (progress > 0) {
                mBuilder.setContentText(progress + "%");
                // setProgress()方法：在通知上设置进度条，
                // 参数一传入通知最大进度，
                // 参数二传入通知当前进度，
                // 参数三表示是否使用模糊进度条
                mBuilder.setProgress(100, progress, false);
            }
        } else {
            mBuilder.setContentTitle(title);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            mBuilder.setContentIntent(pi);
            mBuilder.build();
            if (progress > 0) {
                mBuilder.setContentText(progress + "%");
                mBuilder.setProgress(100, progress, false);
            }
        }
        return mBuilder.build();
    }

    //为了让DownloadService与活动进行通信，创建了DownloadBinder类
    public class DownloadBinder extends Binder {
        //开始下载
        public void startDownload(String url) {
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                //开启下载
                downloadTask.execute(downloadUrl);
                //成为前台服务，，创建一个持续运行的通知
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }
            //将正在下载的文件删除
            if (downloadUrl != null) {
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
