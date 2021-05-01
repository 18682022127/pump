package com.itouch8.pump.core.util.minio;

import com.aliyun.oss.model.ObjectMetadata;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.oss.IFileService;
import com.itouch8.pump.minio.IMinioConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copy Right Information : 深圳云途信息资讯有限公司 <br>
 * Project : 途途物流公共平台 <br>
 * Description : 描述<br>
 * Author : kingdom<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2021-05-01<br>
 */
@Service("MINIOFileService")
public class MINIOFileService implements IFileService {

    @Autowired(required = false)
    private IMinioConfig config;

    public MinioClient getMinioClient() {
        String endpoint = config.getEndPoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessSecret();
        return MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKeyId, accessKeySecret)
                        .build();
    }

    @Override
    public void send(String fileId, String base64) {
        if (null == fileId || null == base64) {
            Throw.throwRuntimeException("文件上传参数错误!");
        }
        try {
            MinioClient minioClient = getMinioClient();
            InputStream is = new ByteArrayInputStream(base64.getBytes());
            ObjectMetadata meta = new ObjectMetadata();
            minioClient.putObject(PutObjectArgs.builder().bucket(config.getBuckName()).object(fileId).stream(is,is.available(),-1).build());
        }catch (Exception ignore){}

    }

    @Override
    public String get(String id) {
        if (null == id) {
            return null;
        }
        StringBuffer out = new StringBuffer();
        try {
            MinioClient minioClient = getMinioClient();
            String bucketName = config.getBuckName();
            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(id)
                            .build())) {
                // Read data from stream
            }
            InputStream in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(id).build());
            byte[] b = new byte[4096];
            for (int n = 0; (n = in.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return out.toString();
    }

    @Override
    public void del(String id) {
        if (null == id) {
            Throw.throwRuntimeException("文件删除参数错误!");
        }
        MinioClient minioClient = getMinioClient();
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(config.getBuckName()).object(id).build());
        } catch (Exception e) {

        }
    }

    @Override
    public void send(String fileId, InputStream is, String contentType) {
        if (null == fileId || null == is) {
            Throw.throwRuntimeException("文件上传参数错误!");
        }
        try {
            MinioClient minioClient = getMinioClient();
            minioClient.putObject(PutObjectArgs.builder().bucket(config.getBuckName()).object(fileId).stream(is,is.available(),-1).contentType(contentType).build());
        } catch (Exception e){
        }
    }

    @Override
    public void get(String fileId, OutputStream os) {
        if (null == fileId) {
            return;
        }
        try {
            MinioClient minioClient = getMinioClient();
            InputStream in = minioClient.getObject(GetObjectArgs.builder().bucket(config.getBuckName()).object(fileId).build());
            IOUtils.copy(in, os);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

}
