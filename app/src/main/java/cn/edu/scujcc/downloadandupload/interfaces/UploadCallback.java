package cn.edu.scujcc.downloadandupload.interfaces;

/**
 * @author Administrator
 */
public interface UploadCallback {
    /**
     * 上传成功
     *
     * @param result 返回登录成功的结果
     */
    void onUploadSuccess(String result);

    /**
     * 上传失败
     *
     * @param msg 返回登录失败的结果
     */
    void onUploadFailure(String msg);

    /**
     * 网络错误
     *
     * @param t 返回失败错误
     */
    void onUploadError(Throwable t);

    /**
     * 返回数据
     *
     * @param message 返回message数据
     */
    void onGtMessage(String message);
}
