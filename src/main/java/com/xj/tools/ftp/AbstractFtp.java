package com.xj.tools.ftp;

import org.apache.commons.net.ftp.FTPFile;

import java.io.Closeable;
import java.io.File;
import java.util.List;

/**
 * 抽象FTP类， 用于定义FTP通用方法
 * @author: xujie
 * @date: 2022/7/7
 **/
public abstract class AbstractFtp implements Closeable {

    protected FtpConfig ftpConfig;

    public AbstractFtp(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    /**
     * 连接超时,重新进行连接
     * @return
     */
    public abstract AbstractFtp reconnectIfTimeout();

    /**
     * 切换到指定目录
     * @param directory 目录路径
     * @return 切换是否成功
     */
    public abstract boolean cd(String directory);

    /**
     * 切换到上级目录
     * @return
     */
    public boolean  toParent(){
        return cd("..");
    }

    /**
     * 显示用户所处工作路径
     * @return
     */
    public abstract String pwd();

    /**
     * 创建目录
     * @param directory 目录路径
     * @return
     */
    public abstract boolean mkdir(String directory);

    /**
     * 判断文件或目录是否存在
     * @param path
     * @return
     */
    public boolean exist(String path){
        return false;
    }

    /**
     * 遍历某个路径下所有文件和目录,不会进行递归遍历
     * @param path 路径
     * @return 文件和目录列表
     */
    public abstract List<FTPFile> ls(String path);

    /**
     * 删除指定目录下的指定文件
     * @param filePath
     * @return 是否删除成功
     */
    public abstract boolean delFile(String filePath);

    /**
     * 删除文件夹以及文件夹下的所有文件
     * @param directoryPath 文件夹路径
     * @return 是否删除成功
     */
    public abstract  boolean delDirectory(String directoryPath);

    /**
     * 文件上传
     * 1.目标文件名destPath
     * 2.destPath为目录,则文件名将与file文件名相同
     * @param destPath 服务端路径,可以为{@code null} 或者相对路或绝对路径
     * @param file 需要上传的文件
     * @return 是否上传成功
     */
    public abstract boolean upload(String destPath, File file);

    /**
     * 下载文件
     * @param path 文件路径
     * @param outFile 输出文件或目录
     * @return
     */
    public abstract boolean download(String path, File outFile);
}
