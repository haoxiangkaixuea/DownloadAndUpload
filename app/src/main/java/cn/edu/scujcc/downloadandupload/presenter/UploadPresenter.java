package cn.edu.scujcc.downloadandupload.presenter;

import java.io.File;

import cn.edu.scujcc.downloadandupload.interfaces.UploadCallback;
import cn.edu.scujcc.downloadandupload.modle.UploadModel;
import cn.edu.scujcc.downloadandupload.view.UploadView;
import retrofit2.http.Url;

public class UploadPresenter {
    private static final String TAG = "UploadPresenter";
    UploadModel loginModel = UploadModel.getInstance();
    /**
     * View接口
     */
    private UploadView uploadView;

    public UploadPresenter(UploadView view) {
        this.uploadView = view;
    }

    public void upload(String url, File file) {

        loginModel.upload(url, file, new UploadCallback() {
            @Override
            public void onUploadSuccess(String result) {
                uploadView.uploadSuccess(result);
            }

            @Override
            public void onUploadFailure(String msg) {
                uploadView.uploadFailure(msg);
            }

            @Override
            public void onUploadError(Throwable t) {
                uploadView.uploadError(t);
            }

            @Override
            public void onGtMessage(String message) {
                uploadView.getMessage(message);
            }
        });
    }
}
