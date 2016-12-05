/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util.component.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.log4j.Logger;

/**
 * 七牛云附件基类处理器
 *
 * @author alfred.yx
 * @version $ID: v 0.1 下午3:56 alfred.yx Exp $
 */
public abstract class QiniuBaseHandler {

    private static Logger logger = Logger.getLogger(QiniuBaseHandler.class);
    protected Auth auth;

    public QiniuBaseHandler(Auth auth) {
        this.auth = auth;
    }

    // 设置指定上传策略
    public String getCallbackUpToken(String bucket, String callbackUrl, String callbackBody) {
        if (logger.isInfoEnabled()) {
            logger.info("在" + bucket + "中生成新的附件上传CallbackUpToken");
        }
        return auth.uploadToken(bucket, null, 3600,
                new StringMap().put("callbackUrl", callbackUrl).put("callbackBody", callbackBody).put("callbackFetchKey", 1));
    }

    // 获取七牛私有资源临时下载授权凭证
    public abstract String getSignedUrl(String domainUrl, String url);

    /**
     * 删除七牛云文件
     *
     * @param fileName
     */
    public boolean deleteFile(String bucket, String fileName) {
        BucketManager bucketManager = new BucketManager(auth);
        try {
            if (logger.isInfoEnabled()) {
                logger.info("在" + bucket + "中删除文件" + fileName);
            }
            bucketManager.delete(bucket, fileName);
        } catch (QiniuException e) {
            logger.error("在" + bucket + "中删除文件" + fileName + "失败");
            return false;
        }
        return true;
    }

    public boolean uploadFile(byte[] file, String fileName, String token) {
        UploadManager uploadManager = new UploadManager();
        try {
            Response res = uploadManager.put(file, fileName, token);
            logger.info(res.toString());
            logger.info(res.bodyString());
            return true;
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时简单状态信息
            logger.error(r.toString());
            try {
                // 响应的文本信息
                logger.error(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
            return false;
        }
    }

    public boolean isValidCallback(String originAuthorization, String callbackUrl, String callbackBody, String callbackContentType) {
        return auth.isValidCallback(originAuthorization, callbackUrl, callbackBody.getBytes(), callbackContentType);
    }
}
