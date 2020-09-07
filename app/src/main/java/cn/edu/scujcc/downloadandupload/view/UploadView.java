package cn.edu.scujcc.downloadandupload.view;

/**
 * @author Administrator
 */
public interface UploadView {
    /**
     * 上传成功
     *
     * @param result 返回登录成功的结果
     */
    void uploadSuccess(String result);

    /**
     * 上传失败
     *
     * @param msg 返回登录失败的结果
     */
    void uploadFailure(String msg);

    /**
     * 网络错误
     *
     * @param t 返回失败错误
     */
    void uploadError(Throwable t);

    /**
     * 返回数据
     *
     * @param message 返回message数据
     */
    void getMessage(String message);
}
