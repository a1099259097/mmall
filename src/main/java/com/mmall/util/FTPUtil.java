package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUsername = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword  = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private Integer port;
    private String username;
    private String password;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public FTPUtil(String ip, Integer port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUsername,ftpPassword);
        logger.info("start connection FTP Service!");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("start connection FTP Service,finish upload, upload result:{}");
        return result;
    }

    private boolean uploadFile(String remote, List<File> fileList) throws IOException {

        boolean upload = false;
        FileInputStream fis = null;

        if (connectionFTPService(this.ip,this.port,this.username,this.password)){
            try {
                ftpClient.changeWorkingDirectory(remote);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalActiveMode();
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
                upload = true;
            } catch (IOException e) {
                logger.error("upload exception",e);
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return upload;
    }

    private boolean connectionFTPService(String ip, Integer port, String username, String password){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            ftpClient.login(username, password);
            isSuccess = true;
        } catch (IOException e) {
            logger.error("服务器连接失败",e);
        }
        return isSuccess;
    }





}
