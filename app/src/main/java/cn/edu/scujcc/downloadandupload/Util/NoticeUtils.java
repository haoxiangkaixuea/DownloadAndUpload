package cn.edu.scujcc.downloadandupload.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import cn.edu.scujcc.downloadandupload.R;
import cn.edu.scujcc.downloadandupload.activity.MainActivity;
import cn.edu.scujcc.downloadandupload.activity.UploadActivity;

import static cn.edu.scujcc.downloadandupload.activity.MainActivity.SEND_NOTICE;

/**
 * @author Administrator
 */
public class NoticeUtils {
    public void sendNotice(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 通知渠道组的id.
            String group = "my_group_01";
            // 用户可见的通知渠道组名称.
            CharSequence name = context.getString(R.string.group_name);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(group, name));
            }
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "my_channel_01";
            // 用户可以看到的通知渠道的名字.
            CharSequence name1 = context.getString(R.string.channel_name);
            // 用户可以看到的通知渠道的描述
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name1, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationManager中创建该通知渠道
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 为该通知设置一个id
            int notifyId = 1;
            // 通知渠道的id
            String chandalId = "my_channel_01";

            Intent intentNotice = new Intent(context, UploadActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intentNotice, 0);

            //创建通知首先要创建一个NotificationManager来对通知进行管理，通过getSystemService获取到
            //里面需要穿一个字符串，一般传Context.NOTIFICATION_SERVICE
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //接下来要用Builder来构造Notification对象，这里我们使用NotificationCompat类来构造创建Notification对象
            //确保我们的程序字啊所有android系统版本都能运行
            Notification notification = new NotificationCompat.Builder(context, "default")
                    //通知的标题
                    .setContentTitle("This is content Title")
                    //通知的内容
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("method int androidx.appcompat.widget." +
                            "DropDownListView.lookForSelectablePosition(int, boolean) \" +\n" +
                            "  \"would have incorrectly overridden the package-private " +
                            "method in android.widget.ListView<"))
                    //显示大图
                    // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture
                    //(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))
                    //发出通知的时间
                    .setWhen(System.currentTimeMillis())
                    //通知的小标题
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    //通知点开后的大标题
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    //启动通知活动
                    .setContentIntent(pi)
                    //设置取消通知，方法一
                    .setAutoCancel(true)
                    //设置通知的重要程度
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setChannelId(chandalId)
                    //通知是发出震动
                    .setVibrate(new long[]{0, 1000, 1000, 1000})
                    .build();
            //notify让通知显示出来
            if (manager != null) {
                manager.notify(SEND_NOTICE, notification);
            }
        } else {
            // Create a notification and set the notification channel.
            // 发布通知
            //实现通知的点击效果，使用PendingIntent来启动一个通知活动
            Intent intentNotice = new Intent(context, UploadActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intentNotice, 0);

            //创建通知首先要创建一个NotificationManager来对通知进行管理，通过getSystemService获取到
            //里面需要穿一个字符串，一般传Context.NOTIFICATION_SERVICE
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //接下来要用Builder来构造Notification对象，这里我们使用NotificationCompat类来构造创建Notification对象
            //确保我们的程序字啊所有android系统版本都能运行
            Notification notification = new NotificationCompat.Builder(context, "default")
                    //通知的标题
                    .setContentTitle("This is content Title")
                    //通知的内容
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("method int androidx.appcompat.widget." +
                            "DropDownListView.lookForSelectablePosition(int, boolean) \" +\n" +
                            "  \"would have incorrectly overridden the package-private " +
                            "method in android.widget.ListView<"))
                    //显示大图
                    // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture
                    //(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))
                    //发出通知的时间
                    .setWhen(System.currentTimeMillis())
                    //通知的小标题
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    //通知点开后的大标题
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    //启动通知活动
                    .setContentIntent(pi)
                    //设置取消通知，方法一
                    .setAutoCancel(true)
                    //设置通知的重要程度
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    //通知是发出震动
                    .setVibrate(new long[]{0, 1000, 1000, 1000})
                    .build();
            //notify让通知显示出来
            assert manager != null;
            manager.notify(SEND_NOTICE, notification);
        }
    }
}
