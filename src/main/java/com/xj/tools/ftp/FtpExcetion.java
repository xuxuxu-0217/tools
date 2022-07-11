package com.xj.tools.ftp;

/**
 * @author: xujie
 * @date: 2022/7/7
 **/
public class FtpExcetion extends RuntimeException {

    private static final long serialVersionUID = -8490149159895201756L;

    public FtpExcetion(Throwable e){
        super(e.getMessage(),e);
    }

    public FtpExcetion(String message){
        super(message);
    }

    public FtpExcetion(String message,Throwable e){
        super(message,e);
    }

}
