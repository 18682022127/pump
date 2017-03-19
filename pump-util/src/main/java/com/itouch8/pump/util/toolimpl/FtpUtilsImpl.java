package com.itouch8.pump.util.toolimpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.util.Tool;
import com.itouch8.pump.util.toolimpl.RemoteFileEntry;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;


public class FtpUtilsImpl {

    private static int defaultBufferedSize = 1024 * 100;
    private static final FtpUtilsImpl instance = new FtpUtilsImpl() {};

    private FtpUtilsImpl() {}

    
    public static FtpUtilsImpl getInstance() {
        return instance;
    }

    
    public boolean getFile(String server, int port, String username, String password, String ftpFolder, String ftpFileName, String localFolder, String localFileName) {
        boolean rtnFlag = false;
        FTPClient ftp = null;
        BufferedOutputStream fos = null;

        try {
            ftp = getFtpClient(server, port, username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            // 设置下载目录
            if (!ftp.changeWorkingDirectory(ftpFolder)) {
                Throw.throwRuntimeException("FTP服务器上不存在文件夹 " + ftpFolder);
            }

            // 创建文件夹
            File fileFold = new File(localFolder);
            if (!fileFold.exists()) {
                fileFold.mkdirs();
            }
            // 创建文件
            File file = new File(localFolder, localFileName);
            fos = new BufferedOutputStream(new FileOutputStream(file), defaultBufferedSize);

            // 下载文件
            if (!ftp.retrieveFile(ftpFileName, fos)) {
                fos.close();
                file.delete();
                return false;
            }
            ftp.logout();
            CommonLogger.info(" recv file -------- " + localFolder + "/" + localFileName);

            rtnFlag = true;
        } catch (Exception e) {
            Throw.throwRuntimeException("与FTP服务器通讯出错：" + e.getMessage());
        } finally {
            Tool.IO.closeQuietly(fos);
            closeFtpClient(ftp);
        }
        return rtnFlag;
    }

    
    public InputStream getFile(String server, int port, String username, String password, String ftpFolder, String ftpFileName) {
        FTPClient ftp = null;
        InputStream stream = null;
        try {
            ftp = getFtpClient(server, port, username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            // 设置下载目录
            if (!ftp.changeWorkingDirectory(ftpFolder)) {
                Throw.throwRuntimeException("FTP服务器上不存在文件夹 " + ftpFolder);
            }
            // 下载文件
            stream = ftp.retrieveFileStream(ftpFileName);
            ftp.logout();
        } catch (Exception e) {
            Throw.throwRuntimeException("与FTP服务器通讯出错：" + e.getMessage());
        } finally {
            // closeFtpClient(ftp);
        }
        return stream;
    }

    
    public boolean sendFile(String server, int port, String username, String password, String ftpFolder, String fullPathOfLocalFile, String ftpFileName) {
        boolean rtnFlag = false;
        FTPClient ftp = null;
        BufferedInputStream fis = null;

        try {
            ftp = getFtpClient(server, port, username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            fis = new BufferedInputStream(new FileInputStream(fullPathOfLocalFile), defaultBufferedSize);

            // 设置上传目录
            if (null != ftpFolder && 0 < ftpFolder.trim().length()) {
                // 如果不存在目录，则创建目录
                if (!ftp.changeWorkingDirectory(ftpFolder)) {
                    ftp.makeDirectory(ftpFolder);
                }
                ftp.changeWorkingDirectory(ftpFolder);
            } else {
                Throw.throwRuntimeException("FTP服务器上传文件夹不能为空");
            }

            // 上传文件
            if (!ftp.storeFile(ftpFileName, fis)) {
                return false;
            }
            ftp.logout();
            CommonLogger.info(" store file ------- " + ftpFolder + "/" + ftpFileName);

            rtnFlag = true;
        } catch (Exception e) {
            Throw.throwRuntimeException("与FTP服务器通讯出错：" + e.getMessage());
        } finally {
            Tool.IO.closeQuietly(fis);
            closeFtpClient(ftp);
        }
        return rtnFlag;
    }

    
    public boolean sendFile(String server, int port, String username, String password, String ftpFolder, FileInputStream stream, String ftpFileName) {
        boolean rtnFlag = false;
        FTPClient ftp = null;
        BufferedInputStream fis = null;

        try {
            ftp = getFtpClient(server, port, username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            fis = new BufferedInputStream(stream, defaultBufferedSize);

            // 设置上传目录
            if (null != ftpFolder && 0 < ftpFolder.trim().length()) {
                // 如果不存在目录，则创建目录
                if (!ftp.changeWorkingDirectory(ftpFolder)) {
                    ftp.makeDirectory(ftpFolder);
                }
                ftp.changeWorkingDirectory(ftpFolder);
            } else {
                Throw.throwRuntimeException("FTP服务器上传文件夹不能为空");
            }

            // 上传文件
            if (!ftp.storeFile(ftpFileName, fis)) {
                return false;
            }
            ftp.logout();
            CommonLogger.info(" store file ------- " + ftpFolder + "/" + ftpFileName);

            rtnFlag = true;
        } catch (Exception e) {
            Throw.throwRuntimeException("与FTP服务器通讯出错：" + e.getMessage());
        } finally {
            Tool.IO.closeQuietly(fis);
            closeFtpClient(ftp);
        }
        return rtnFlag;
    }

    
    public boolean getSftpFile(String server, int port, String username, String password, String sftpFolder, String sftpFileName, String localFolder, String localFileName) {
        boolean rtnFlag = false;
        ChannelSftp channel = null;
        BufferedOutputStream fos = null;

        try {
            channel = getChannelSftp(server, port, username, password);
            // 设置下载目录
            try {
                channel.cd(sftpFolder);
            } catch (Exception e) {
                Throw.throwRuntimeException("SFTP服务器上不存在文件夹 " + sftpFolder, e);
            }
            // 创建文件夹
            File fileFold = new File(localFolder);
            if (!fileFold.exists()) {
                fileFold.mkdirs();
            }
            File file = new File(localFolder, localFileName);
            fos = new BufferedOutputStream(new FileOutputStream(file), defaultBufferedSize);

            // 下载文件
            try {
                channel.get(sftpFileName, fos);
                CommonLogger.info(" recv file -------- " + localFolder + "/" + localFileName);
            } catch (Exception e) {
                Throw.throwRuntimeException("SFTP服务器下载文件失败 " + sftpFileName, e);
            }
            rtnFlag = true;
        } catch (Exception e) {
            Throw.throwRuntimeException("与SFTP服务器通讯出错：" + e.getMessage());
        } finally {
            Tool.IO.closeQuietly(fos);
            closeChannelSftp(channel);
        }
        return rtnFlag;
    }

    
    public InputStream getSftpFile(String server, int port, String username, String password, String sftpFolder, String sftpFileName) {
        ChannelSftp channel = null;
        InputStream stream = null;
        try {
            channel = getChannelSftp(server, port, username, password);
            // 设置下载目录
            try {
                channel.cd(sftpFolder);
            } catch (Exception e) {
                Throw.throwRuntimeException("SFTP服务器上不存在文件夹 " + sftpFolder, e);
            }

            // 下载文件
            try {
                stream = channel.get(sftpFileName);
            } catch (Exception e) {
                Throw.throwRuntimeException("SFTP服务器下载文件失败 " + sftpFileName, e);
            }

        } catch (Exception e) {
            Throw.throwRuntimeException("与SFTP服务器通讯出错：" + e.getMessage());
        } finally {
            // closeChannelSftp(channel);
        }
        return stream;
    }

    
    public boolean sendSftpFile(String server, int port, String username, String password, String sftpFolder, String fullPathOfLocalFile, String sftpFileName) {
        boolean rtnFlag = false;
        ChannelSftp channel = null;
        BufferedInputStream fis = null;

        try {
            channel = getChannelSftp(server, port, username, password);
            fis = new BufferedInputStream(new FileInputStream(fullPathOfLocalFile), defaultBufferedSize);

            // 设置上传目录
            if (null != sftpFolder && 0 < sftpFolder.trim().length()) {
                // 如果不存在目录，则创建目录
                mkdirs(channel, sftpFolder, "/");
            } else {
                Throw.throwRuntimeException("SFTP服务器上传文件夹不能为空");
            }

            // 上传文件
            try {
                channel.put(fis, sftpFileName);
                CommonLogger.info(" store file ------- " + sftpFolder + "/" + sftpFileName);
            } catch (Exception e) {
                Throw.throwRuntimeException("上传文件至SFTP服务器失败", e);
            }
            rtnFlag = true;
        } catch (Exception e) {
            Throw.throwRuntimeException("与SFTP服务器通讯出错：" + e);
        } finally {
            Tool.IO.closeQuietly(fis);
            closeChannelSftp(channel);
        }
        return rtnFlag;
    }

    
    public boolean sendSftpFile(String server, int port, String username, String password, String sftpFolder, FileInputStream stream, String sftpFileName) {
        boolean rtnFlag = false;
        ChannelSftp channel = null;
        BufferedInputStream fis = null;

        try {
            channel = getChannelSftp(server, port, username, password);
            fis = new BufferedInputStream(stream, defaultBufferedSize);

            // 设置上传目录
            if (null != sftpFolder && 0 < sftpFolder.trim().length()) {
                // 如果不存在目录，则创建目录
                mkdirs(channel, sftpFolder, "/");
            } else {
                Throw.throwRuntimeException("SFTP服务器上传文件夹不能为空");
            }

            // 上传文件
            try {
                channel.put(fis, sftpFileName);
                CommonLogger.info(" store file ------- " + sftpFolder + "/" + sftpFileName);
            } catch (Exception e) {
                Throw.throwRuntimeException("上传文件至SFTP服务器失败", e);
            }
            rtnFlag = true;
        } catch (Exception e) {
            Throw.throwRuntimeException("与SFTP服务器通讯出错：" + e);
        } finally {
            Tool.IO.closeQuietly(fis);
            closeChannelSftp(channel);
        }
        return rtnFlag;
    }

    
    public List<RemoteFileEntry> listFtpFiles(String server, int port, String username, String password, String path) {
        FTPClient ftp = null;
        FTPFile[] listFiles = null;
        List<RemoteFileEntry> list = null;
        try {
            ftp = getFtpClient(server, port, username, password);
            listFiles = ftp.listFiles(path);
            if (null != listFiles && listFiles.length > 0) {
                list = new ArrayList<RemoteFileEntry>();
                for (FTPFile entry : listFiles) {
                    RemoteFileEntry e = new RemoteFileEntry();
                    e.setDir(entry.isDirectory());
                    e.setFile(entry.isFile());
                    e.setName(entry.getName());
                    e.setSize(entry.getSize());
                    e.setLastModifyTime(Tool.DATE.getDateAndTime(entry.getTimestamp().getTime()));
                    list.add(e);
                }
            }
        } catch (Exception e) {
            Throw.throwRuntimeException("列出---- " + path + " ----下文件时出现异常", e);
        }
        return list;
    }

    
    @SuppressWarnings("unchecked")
    public List<RemoteFileEntry> listSftpFiles(String server, int port, String username, String password, String path) {
        ChannelSftp channel = null;
        Vector<LsEntry> ls = null;
        List<RemoteFileEntry> list = null;
        try {
            channel = getChannelSftp(server, port, username, password);
            try {
                ls = channel.ls(path);
                if (null != ls && !ls.isEmpty()) {
                    list = new ArrayList<RemoteFileEntry>();
                    for (LsEntry lsEntry : ls) {
                        SftpATTRS sttr = lsEntry.getAttrs();
                        RemoteFileEntry entry = new RemoteFileEntry();
                        entry.setDir(sttr.isDir());
                        entry.setFile(!sttr.isDir());
                        entry.setName(lsEntry.getFilename());
                        entry.setSize(sttr.getSize());
                        entry.setLastModifyTime(sttr.getMtimeString());
                        list.add(entry);
                    }
                }

            } catch (Exception e) {
                Throw.throwRuntimeException("列出---- " + path + " ----下文件时出现异常", e);
            }
        } catch (Exception e) {
            Throw.throwRuntimeException("与SFTP服务器通讯出错：" + e);
        } finally {
            closeChannelSftp(channel);
        }
        return list;
    }

    
    public boolean removeFtpFile(String server, int port, String username, String password, String ftpFilePath) {
        FTPClient ftp = null;
        boolean rtnFlag = false;
        try {
            ftp = getFtpClient(server, port, username, password);
            if (ftp.deleteFile(ftpFilePath)) {
                rtnFlag = true;
            }
        } catch (Exception e) {
            Throw.throwRuntimeException("FTP服务器删除" + ftpFilePath + "文件异常", e);
        }
        return rtnFlag;
    }

    
    public boolean removeSftpFile(String server, int port, String username, String password, String ftpFilePath) {
        boolean rtnFlag = false;
        ChannelSftp channel = null;

        try {
            channel = getChannelSftp(server, port, username, password);
            // 删除文件
            try {
                channel.rm(ftpFilePath);
                CommonLogger.info(" delete file ------- " + ftpFilePath);
            } catch (Exception e) {
                CommonLogger.error(" delete file ------- " + ftpFilePath + " failure");
            }
            rtnFlag = true;
        } catch (Exception e) {
            Throw.throwRuntimeException("与SFTP服务器通讯出错：" + e);
        } finally {
            closeChannelSftp(channel);
        }
        return rtnFlag;
    }

    private FTPClient getFtpClient(String server, int port, String username, String password) {
        FTPClient ftp = new FTPClient();
        try {
            CommonLogger.info(" connecting FTP server " + server + " : " + port + " ...");
            // 连接FTP服务器
            ftp.connect(server, port);
            CommonLogger.info(" connected -------- " + server + " : " + port);

            // 登陆FTP服务器
            if (!ftp.login(username, password)) {
                Throw.throwRuntimeException("登录FTP服务器" + server + "失败");
            }
            CommonLogger.info(" user login ------- " + username + " : " + password);
        } catch (Exception e) {
            Throw.throwRuntimeException("连接FTP服务器异常", e);
        }
        return ftp;
    }

    private void closeFtpClient(FTPClient ftp) {
        if (null != ftp && ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException f) {
                Throw.throwRuntimeException("关闭FTP连接发生异常");
            }
        }
    }

    private ChannelSftp getChannelSftp(String server, int port, String username, String password) {
        ChannelSftp channel = null;
        try {

            JSch jsch = new JSch();
            Session session = jsch.getSession(username, server, port);
            if (!Tool.CHECK.isBlank(password)) {
                session.setPassword(password);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            CommonLogger.info(" connecting SFTP server " + server + " : " + port + " ...");
            CommonLogger.info(" user login ------- " + username + " : " + password);
            session.connect();
            CommonLogger.info(" connected -------- " + server + " : " + port);
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            Throw.throwRuntimeException("连接SFTP服务器异常", e);
        }
        return channel;
    }

    private void closeChannelSftp(ChannelSftp channel) {
        if (null != channel && channel.isConnected()) {
            try {
                Session session = channel.getSession();
                channel.disconnect();
                if (null != session && session.isConnected()) {
                    session.disconnect();
                }
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
    }

    private void mkdirs(ChannelSftp channel, String path, String separator) throws Exception {
        if (null != path) {

            try {
                channel.cd(path);
                return;
            } catch (Exception e) {
            }

            String[] rs = Tool.STRING.split(path, separator);
            if (rs != null && rs.length > 0) {
                for (String r : rs) {
                    // 如果不存在目录，则创建目录
                    try {
                        channel.cd(r);
                    } catch (Exception e) {
                        channel.mkdir(r);
                        channel.cd(r);
                    }
                }
            }
        }
    }
}
