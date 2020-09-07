package cn.edu.scujcc.downloadandupload;

import android.os.AsyncTask;
import android.os.Environment;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import cn.edu.scujcc.downloadandupload.api.DownloadListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {//下载功能

    public static final int TYPE_SUCCESS = 0;//下载成功
    public static final int TYPE_FAILED = 1;//下载失败
    public static final int TYPE_PAUSED = 2;//下载暂停
    public static final int TYPE_CANCELED = 3;//取消下载

    private DownloadListener listener;//通过此参数将将下载状态进行回调
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {//后台执行具体的下载逻辑
        InputStream is = null;//输入流
        RandomAccessFile savedFile = null;//保存文件的地址
        File file = null;
        try {
            long downloadedLength = 0;//记录已下载文件的长度
            String downloadUrl = params[0];//通过传入的参数，获取到下载的URL地址
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));//从downloadUrl中截取出下载的文件名
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();//下载到指定目录
            file = new File(directory + fileName);//下载的文件
            if (file.exists()) {//若文件已经存在
                downloadedLength = file.length();//获取已下载的字节数，下面可以设置断点接着下载
            }
            long contentLength = getContentLength(downloadUrl);//获取待下载文件的总长度
            if (contentLength == 0) {//若为0，则失败
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {//若文件长度等于已下载的文件长度，则已经下载完成
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();//创建实例
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")//创建断点继续下载
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();//发送请求并获取服务端数据
            if (response != null) {//不断从网络上获取数据
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);//跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {//读取文件
                    if (isCanceled) {//判断是否取消
                        return TYPE_CANCELED;
                    } else if (isPaused) {//判断是否暂停
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);//计算下载的百分比
                        publishProgress(progress);
                    }
                }
                response.body().close();//得到具体内容
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {//界面上更新下载进度
        int progress = values[0];
        if (progress > lastProgress) {//和上一次下载进度相比，若有变化，则更新
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {//通知最后下载进度
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {//获取文件长度
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            long contentLength = 0;
            if (response.body() != null) {
                contentLength = response.body().contentLength();
                response.body().close();
            }

            return contentLength;
        }
        return 0;
    }
}
