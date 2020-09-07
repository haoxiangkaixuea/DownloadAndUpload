package cn.edu.scujcc.downloadandupload.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import cn.edu.scujcc.downloadandupload.R;
import cn.edu.scujcc.downloadandupload.presenter.UploadPresenter;
import cn.edu.scujcc.downloadandupload.view.UploadView;

import static cn.edu.scujcc.downloadandupload.Util.FileUtils.getPath;
import static cn.edu.scujcc.downloadandupload.Util.FileUtils.getRealPathFromURI;

public class UploadActivity extends AppCompatActivity implements UploadView {
    private static final String TAG = "UploadActivity";
    private static final String URL = "http://172.32.12.243:20527/framework/sysApkAndIos/uploadFile/MobileBank";
    private UploadPresenter uploadPresenter;
    private TextView tvFileName;
    private String urlString = null;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadPresenter = new UploadPresenter(this);
        Button btnUpload = findViewById(R.id.upload);
        tvFileName = findViewById(R.id.filename);
        Button btnSelect = findViewById(R.id.button_select);
        btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            // 设置你要打开的文件type
            intent.setType("application/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        });

        btnUpload.setOnClickListener(v -> {
            uploadPresenter.upload(URL, file);
            Log.d(TAG, "file  " + file);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "返回的数据：" + data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            //使用第三方应用打开
            String uploadFile;
            String path;
            if (uri != null && "file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
                if (path != null) {
                    file = new File(path);
                }
                uploadFile = file.getName();
                tvFileName.setText(uploadFile);
            }
            //4.4以后
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                // 获取文件路径
                path = getPath(this, uri);
                if (path != null) {
                    Log.w(TAG, path);
                    file = new File(path);
                }
                // 获得文件名
                uploadFile = file.getName();
                // 这里是为了选中文件后，编辑框内容变成我选中的文件名
                // 直接 mc_annex.setText(file.getName()); 也行
                tvFileName.setText(uploadFile);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            }
            //4.4以下下系统调用方法
            else {
                path = getRealPathFromURI(this, uri);
            }
        }
    }

    @Override
    public void uploadSuccess(String result) {

        Log.d(TAG, "result  " + result);
    }

    @Override
    public void uploadFailure(String msg) {

        Log.d(TAG, "result  " + msg);
    }

    @Override
    public void uploadError(Throwable t) {

        Log.d(TAG, "result  " + t);
    }

    @Override
    public void getMessage(String message) {

        Log.d(TAG, "result  " + message);
    }
}