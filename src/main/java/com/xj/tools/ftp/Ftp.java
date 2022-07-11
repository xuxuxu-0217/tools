package com.xj.tools.ftp;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.ftp.FtpException;
import com.xj.tools.utils.ArrayUtils;
import com.xj.tools.utils.StringUtils;
import org.apache.commons.net.ftp.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: xujie
 * @date: 2022/7/7
 **/
public class Ftp extends AbstractFtp {


    private FTPClient client;
    private FtpWorkMode ftpWorkMode;

    /**
     * 执行完操作是否返回会当前目录
     */
    private boolean backToPwd;

    /**
     * 构造,匿名登录
     * @param host 域名或id
     * @param port 端口
     */
    public Ftp(String host, int port) {
        this(host, port, "anonymous", "");
    }

    /**
     * 构造
     * @param host 域名或ip
     * @param port 端口
     * @param user 用户名
     * @param password 密码
     */
    public Ftp(String host, int port, String user, String password){
        this(host, port, user, password, StandardCharsets.UTF_8);
    }

    /**
     * 构造
     * @param host 域名或ip
     * @param port 端口
     * @param user 用户名
     * @param password 密码
     * @param charset 编码
     */
    public Ftp(String host, int port, String user, String password, Charset charset){
        this(host, port, user, password, charset, null);
    }

    /**
     * 构造
     * @param host 域名或ip
     * @param port 端口
     * @param user 用户名
     * @param password 密码
     * @param charset 编码
     * @param workMode 模式
     */
    public Ftp(String host, int port, String user, String password, Charset charset, FtpWorkMode workMode){
        this(new FtpConfig(host, port, user, password, charset), workMode);
    }

    /**
     * 构造
     * @param config FTP配置
     * @param workMode 模式
     */
    public Ftp(FtpConfig config, FtpWorkMode workMode){
        super(config);
        this.ftpWorkMode = workMode;
        this.init();
    }

    /**
     * 初始化连接
     * @return this
     */
    public Ftp init(){
        return this.init(this.ftpConfig, this.ftpWorkMode);
    }

    /**
     * 初始化连接
     * @param host 域名或ip
     * @param port 端口
     * @param user 用户
     * @param password 密码
     * @return
     */
    public Ftp init(String host, int port, String user, String password){
        return this.init(host, port, user, password, null);
    }

    /**
     * 初始化连接
     * @param host 域名或ip
     * @param port 端口
     * @param user 用户
     * @param password 密码
     * @param workMode 模式
     * @return this
     */
    public Ftp init(String host, int port, String user, String password, FtpWorkMode workMode){
        return this.init(new FtpConfig(host, port, user, password, this.ftpConfig.getCharset()), workMode);
    }

    /**
     * 初始化连接
     * @param config FTP配置
     * @param workMode 模式
     * @return this
     */
    public Ftp init(FtpConfig config, FtpWorkMode workMode){
        final FTPClient client = new FTPClient();
        client.setControlEncoding(config.getCharset().toString());
        client.setConnectTimeout((int) config.getConnectionTimeout());
        try {
            //连接ftp服务器
            client.connect(config.getHost(),config.getPort());
            client.setSoTimeout((int) config.getSocketTimeout());
            //登录ftp服务器
            client.login(config.getHost(),config.getPassword());
        }catch (IOException e){
            throw new FtpExcetion(e);
        }
        //是否登录服务器成功
        final int replayCode = client.getReplyCode();
        if (false == FTPReply.isPositiveCompletion(replayCode)){
            try {
                client.disconnect();
            }catch (IOException e){
                // ignore
            }
            throw new FtpExcetion("login failed for user ["+ config.getUser() +"], reply code is: ["+ replayCode +"]");
        }
        this.client = client;
        if (workMode != null){
            setFtpWorkMode(workMode);
        }
        return this;
    }

    /**
     * 设置FTP连接模式，可选主动模式和被动模式
     *
     * @param ftpWorkMode 模式枚举
     * @return this
     */
    public Ftp setFtpWorkMode(FtpWorkMode ftpWorkMode){
        this.ftpWorkMode = ftpWorkMode;
        switch (ftpWorkMode){
            case PORT:
                this.client.enterLocalActiveMode();
                break;
            case PASV:
                this.client.enterLocalPassiveMode();
                break;
        }
        return this;
    }

    /**
     * 设置执行完操作是否返回当前目录
     * @param backToPwd 执行完成操作是否返回当前目录
     * @return this
     */
    public Ftp setBackToPwd(boolean backToPwd){
        this.backToPwd = backToPwd;
        return this;
    }

    /**
     * 连接超时,重新进行连接
     * 当连接超时时,client.isConnected()仍然返回true,无法判断是否超时,因此通过pwd命令判断超时
     * @return
     */
    @Override
    public AbstractFtp reconnectIfTimeout() {
        String pwd = null;
        try {
            pwd = pwd();
        }catch (FtpExcetion e){
            //ignore
        }
        if (pwd == null){
            return this.init();
        }
        return this;
    }

    /**
     * 切换目录
     *
     * @param directory 目录路径
     * @return 是否成功
     */
    @Override
    public boolean cd(String directory) {
        if (StringUtils.isBlank(directory)){
            return false;
        }
        try {
            return client.changeWorkingDirectory(directory);
        }catch (IOException e){
            throw new FtpExcetion(e);
        }
    }

    /**
     * 打印当前工作目录
     * @return
     */
    @Override
    public String pwd() {
        try {
            return client.printWorkingDirectory();
        }catch (Exception e){
            throw new FtpExcetion(e);
        }
    }

    /**
     * 创建目录
     * @param directory 目录路径
     * @return
     */
    @Override
    public boolean mkdir(String directory) {
        try {
            return this.client.makeDirectory(directory);
        }catch (IOException e){
            throw new FtpExcetion(e);
        }
    }

    /**
     * 遍历某个路径下所有文件和目录,不会进行递归遍历
     * @param path 路径
     * @return
     */
    @Override
    public List<FTPFile> ls(String path) {
        return lsFiles(path, ftpFile -> ftpFile != null && !ftpFile.isDirectory());
    }

    /**
     * 遍历某个目录下所有文件和目录,不会递归遍历
     * 此方法过滤"."和".."两种目录
     * @param path 目录
     * @param filter 过滤器
     * @return 文件或路径
     */
    public List<FTPFile> lsFiles(String path, FTPFileFilter filter){
        final FTPFile[] ftpFiles = lsFiles(path);
        if (ArrayUtils.isEmpty(ftpFiles)){
            return Collections.emptyList();
        }

        final List<FTPFile> result = new ArrayList<>(ftpFiles.length - 2 <= 0 ? ftpFiles.length : ftpFiles.length - 2);
        for (FTPFile ftpFile : ftpFiles){
            if (filter.accept(ftpFile)){
                result.add(ftpFile);
            }
        }
        return result;
    }

    public FTPFile[] lsFiles(String path){
        String pwd = null;
        if (StringUtils.isNotBlank(path)){
            //获取工作路径
            pwd = pwd();
            cd(path);
        }
        FTPFile[] ftpFiles;
        try {
            ftpFiles = this.client.listFiles();
        }catch (IOException e){
            throw new FtpExcetion(e);
        }finally {
            //返回原目录
            cd(pwd);
        }
        return ftpFiles;

    }

    /**
     * 获取目录状态
     * @param path 路径
     * @return 状态码
     */
    public int stat(String path){
        try {
            return this.client.stat(path);
        }catch (IOException e){
            throw new FtpExcetion(e);
        }
    }

    /**
     * 判断ftp服务器文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public boolean existFile(String path) {
        FTPFile[] ftpFileArr;
        try {
            ftpFileArr = client.listFiles(path);
        } catch (IOException e) {
            throw new FtpException(e);
        }
        return ArrayUtil.isNotEmpty(ftpFileArr);
    }

    @Override
    public boolean delFile(String filePath) {
        final String pwd = pwd();
        boolean isSuccess;
        try {
            FTPFile ftpFile = client.mlistFile(filePath);
            if (ftpFile == null){
                return false;
            }
            if (ftpFile.isDirectory()){
                return false;
            }
            isSuccess =  client.deleteFile(filePath);
        } catch (IOException e) {
            throw new FtpExcetion(e);
        } finally {
            cd(pwd);
        }
        return isSuccess;
    }

    @Override
    public boolean delDirectory(String directoryPath) {
        return false;
    }

    @Override
    public boolean upload(String destPath, File file) {
        return false;
    }

    @Override
    public boolean download(String path, File outFile) {
        return false;
    }

    @Override
    public void close() throws IOException {

    }
}
