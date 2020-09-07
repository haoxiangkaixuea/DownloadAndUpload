package cn.edu.scujcc.downloadandupload.entity;

/**
 * GSON 获取数据
 *
 * @author Administrator
 */
public class ResponseEntity {
    private String data;
    private String successful;
    private int errorCode;
    private String isSuccess;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSuccessful() {
        return successful;
    }

    public void setSuccessful(String successful) {
        this.successful = successful;
    }


    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }
}
