package com.itouch8.pump.core.util.oss;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.itouch8.pump.oss.IOSSConfig;
import com.itouch8.pump.web.upload.IUploadFile;

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

    public static final String FILE_TYPE_BASE64 = "base64";

    public static final String FILE_TYPE_STREAM = "stream";

    @Autowired(required = false)
    private IOSSConfig config;

    public OSSClient getOSSClient() {
        String endpoint = config.getEndPoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessSecret();
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    public void send(String fileId, String base64) {

        OSSClient ossClient = null;
        try {
            ossClient = getOSSClient();
            InputStream is = new ByteArrayInputStream(base64.getBytes());
            ObjectMetadata meta = new ObjectMetadata();
            Map<String, String> userMeta = new HashMap<String, String>();
            userMeta.put("type", FILE_TYPE_BASE64);
            meta.setUserMetadata(userMeta);
            ossClient.putObject(config.getBuckName(), fileId, is, meta);
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void send(String fileId, InputStream is, String contentType) {
        OSSClient ossClient = null;
        try {
            ossClient = getOSSClient();
            ObjectMetadata meta = new ObjectMetadata();
            Map<String, String> userMeta = new HashMap<String, String>();
            userMeta.put("type", FILE_TYPE_STREAM);
            meta.setUserMetadata(userMeta);
            meta.setContentType(contentType);
            ossClient.putObject(config.getBuckName(), fileId, is, meta);
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public String get(String id) {
        StringBuffer out = new StringBuffer();
        OSSClient ossClient = null;
        try {
            ossClient = getOSSClient();
            OSSObject object = ossClient.getObject(config.getBuckName(), id);
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

    public String get(String id, OSSClient ossClient) {
        StringBuffer out = new StringBuffer();
        try {
            ossClient = getOSSClient();
            OSSObject object = ossClient.getObject(config.getBuckName(), id);
            InputStream in = object.getObjectContent();
            byte[] b = new byte[4096];
            for (int n = 0; (n = in.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
        return out.toString();
    }

    @Override
    public void get(String id, OutputStream os) {
        OSSClient ossClient = null;
        try {
            ossClient = getOSSClient();
            OSSObject object = ossClient.getObject(config.getBuckName(), id);
            InputStream objectContent = object.getObjectContent();
            IOUtils.copy(objectContent, os);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    public void get(String id, OutputStream os, OSSClient ossClient) {
        try {
            ossClient = getOSSClient();
            OSSObject object = ossClient.getObject(config.getBuckName(), id);
            InputStream objectContent = object.getObjectContent();
            IOUtils.copy(objectContent, os);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public Object getUserParam(String id, String key, OSSClient ossClient) {
        OSSObject object = ossClient.getObject(config.getBuckName(), id);
        if (null != object) {
            Map<String, String> userMetadata = object.getObjectMetadata().getUserMetadata();
            if (null != userMetadata) {
                return userMetadata.get(key);
            }
        }
        return null;
    }

    @Override
    public void del(String id) {
        OSSClient ossClient = null;
        ossClient = getOSSClient();
        ossClient.deleteObject(config.getBuckName(), id);
        ossClient.shutdown();
    }

    @Override
    public void send(String fileId, InputStream is) {
        send(fileId, is, "image/jpeg");
    }

    @Override
    public List<String> send(IUploadFile[] files) {
        List<String> rs = new ArrayList<String>();
        for (IUploadFile file : files) {
            String fileId = UUID.randomUUID().toString().replaceAll("-", "") + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            send(fileId, file.getInputStream(), file.getContentType());
            rs.add(fileId);
        }
        return rs;
    }

}
