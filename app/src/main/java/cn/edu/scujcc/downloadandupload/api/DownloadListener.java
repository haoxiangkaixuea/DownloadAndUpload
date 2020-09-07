package cn.edu.scujcc.downloadandupload.api;

public interface DownloadListener {

    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();

}