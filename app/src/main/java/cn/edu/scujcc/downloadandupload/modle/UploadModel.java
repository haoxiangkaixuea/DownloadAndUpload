package cn.edu.scujcc.downloadandupload.modle;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.edu.scujcc.downloadandupload.interfaces.UploadCallback;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 */
public class UploadModel {
    private static final String TAG = "UploadModel";
    /**
     * 单例模式
     */
    private static volatile UploadModel INSTANCE;

    public UploadModel() {

    }

    public static UploadModel getInstance() {
        if (INSTANCE == null) {
            synchronized (UploadModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UploadModel();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 读取SDCARD文件的内容
     *
     * @param fileName 文件路径
     * @return 返回内容
     */
    public static String readFileInSdCard(String fileName) {
        try {
            if (!TextUtils.isEmpty(fileName)) {
                File file = new File(fileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[fis.available()];
                    int length = -1;
                    while ((length = fis.read(buffer)) != -1) {
                        outStream.write(buffer, 0, length);
                    }
                    outStream.close();
                    fis.close();
                    return outStream.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //通过“addFormDataPart”可以添加多个上传的文件。

    public void upload(String url, File file, UploadCallback uploadCallback) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("application/octet-stream", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(150, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                uploadCallback.onUploadFailure("Failure" + e);
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) {
                uploadCallback.onUploadSuccess("success");
            }
        });
    }
}

