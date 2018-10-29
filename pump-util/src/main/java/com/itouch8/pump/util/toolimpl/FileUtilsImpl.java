package com.itouch8.pump.util.toolimpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.util.Tool;

public abstract class FileUtilsImpl {

    private static final FileUtilsImpl instance = new FileUtilsImpl() {};

    private FileUtilsImpl() {}

    public static FileUtilsImpl getInstance() {
        return instance;
    }

    public void forceDelete(File... file) {
        if (null != file) {
            for (File f : file) {
                try {
                    FileUtils.forceDelete(f);
                } catch (Exception i) {
                }
            }
        }
    }

    public boolean deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deleteRecursively(child);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }

    public void forceMkdir(File directory) {
        if (null != directory) {
            try {
                FileUtils.forceMkdir(directory);
            } catch (Exception i) {
            }
        }
    }

    public String getAppendFilename(String name, String append) {
        String extension = FilenameUtils.getExtension(name);
        if (Tool.CHECK.isBlank(extension)) {
            return name + "-" + append;
        } else {
            return FilenameUtils.removeExtension(name) + "-" + append + "." + extension;
        }
    }

    public String replaceExtension(String name, String extension) {
        return FilenameUtils.removeExtension(name) + "." + extension;
    }

    public static interface IRenameFileCallback {

        public void callback(File newFile);
    }

    public void renameFileThenRecover(File file, IRenameFileCallback callback) {
        renameFileThenRecover(file, getAppendFilename(file.getPath(), "" + System.nanoTime()), callback);
    }

    public void renameFileThenRecover(File file, String newFilename, IRenameFileCallback callback) {
        if (null != file) {
            File tmp = new File(newFilename);
            try {
                file.renameTo(tmp);
                callback.callback(tmp);
            } finally {
                tmp.renameTo(file);
            }
        }
    }

    public Set<File> scanFiles(String basePackage, FileFilter filter) {
        return BaseConfig.getScan().scanFiles(basePackage, filter);
    }

    public InputStream decodeImage(String srcBase64) {
        Tool.STRING.decodeBase64(srcBase64);
        return null;
    }

    /**
     * 图像BASE64转换为具体文件
     * 
     * @param imgBase64
     * @param fileName
     * @return
     */
    public String imageBase64ToFile(String imgBase64) {
        String imgFilePath = null;
        try {
            if (null != imgBase64) {
                if (imgBase64.startsWith("data:")) {
                    imgBase64 = imgBase64.split(",")[1];
                }
                byte[] b = Base64.getDecoder().decode(imgBase64);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }
                imgFilePath = System.getProperty("java.io.tmpdir") + File.separatorChar + Tool.STRING.getUUID() + ".jpg";
                OutputStream out = new FileOutputStream(imgFilePath);
                out.write(b);
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            //ignore
        }
        return imgFilePath;
    }


}
