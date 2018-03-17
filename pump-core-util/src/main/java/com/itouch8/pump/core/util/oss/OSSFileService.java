package com.itouch8.pump.core.util.oss;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.itouch8.pump.oss.IOSSConfig;

/**
 * Copy Right Information : 深圳云途信息资讯有限公司 <br>
 * Project : 途途物流公共平台 <br>
 * Description : 描述<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-01-24<br>
 */
@Service("OSSFileService")
public class OSSFileService implements IFileService {

    @Autowired(required = false)
    private IOSSConfig config;

    @Override
    public void send(String fileId, String base64) {

        String endpoint = config.getEndPoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessSecret();
        String bucketName = config.getBuckName();
        OSSClient ossClient = null;

        try {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            InputStream is = new ByteArrayInputStream(base64.getBytes());
            ObjectMetadata meta = new ObjectMetadata();
            ossClient.putObject(bucketName, fileId, is, meta);
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public String get(String id) {
        StringBuffer out = new StringBuffer();
        OSSClient ossClient = null;
        try {
            String endpoint = config.getEndPoint();
            String accessKeyId = config.getAccessKeyId();
            String accessKeySecret = config.getAccessSecret();
            String bucketName = config.getBuckName();
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            OSSObject object = ossClient.getObject(bucketName, id);
            InputStream in = object.getObjectContent();
            byte[] b = new byte[4096];
            for (int n = 0; (n = in.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException ignore) {
            ignore.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return out.toString();
    }

    @Override
    public void del(String id) {
        OSSClient ossClient = null;
        String endpoint = config.getEndPoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessSecret();
        String bucketName = config.getBuckName();
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, id);
        ossClient.shutdown();
    }

    @Override
    public void send(String fileId, InputStream s, String contentType) {
        String endpoint = config.getEndPoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessSecret();
        String bucketName = config.getBuckName();
        OSSClient ossClient = null;

        try {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            ObjectMetadata meta = new ObjectMetadata();
            ossClient.putObject(bucketName, fileId, s, meta);
        } finally {
            ossClient.shutdown();
        }
    }

}
