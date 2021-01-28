package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class FastDFSClient {
    /***
     * 初始化tracker信息
     */
    static {
        try {
            //获取tracker的配置文件fdfs_client.conf的位置
            String filePath = new ClassPathResource("fdfs_client.conf").getPath();
            //加载tracker配置信息
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 文件上传
     */
    public static String[] upload(FastDFSFile fastDFSFile){
        //附加参数
        NameValuePair[] meta_list = new NameValuePair[2];
        meta_list[0]=new NameValuePair("地址","深圳");
        meta_list[1]=new NameValuePair("电话","110");
        try {
            StorageClient storageClient = getStorageClient();
            String[] strings = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 文件下载
     */
    public static InputStream download(String groupName, String remoteFileName){
        try {
            StorageClient storageClient = getStorageClient();
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 文件删除
     */
    public static int delete(String groupName, String remoteFileName){
        try {
            StorageClient storageClient = getStorageClient();
            int i = storageClient.delete_file(groupName, remoteFileName);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * 优化方法：获取Storage
     */
    public static StorageClient getStorageClient(){
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer connection = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(connection,null);
            return storageClient;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 基于SSM实现文件上传
     */
    public Result upload(HttpServletResponse response, HttpServletRequest request){
        //文件上传核心类
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断请求中是否有文件
        if(commonsMultipartResolver.isMultipart(request)){
            //解析上传的文件
           MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
           //获取请求中的所有文件
            Iterator<String> fileNames = multiRequest.getFileNames();
            while (fileNames.hasNext()){
                //将解析的文件转换成spring支持的文件类型MultipartFile
                MultipartFile file = multiRequest.getFile(fileNames.next());
                if(file!=null){
                    try {
                        //设置文件上传地址,创建空文件后进行写入
                        File localFile = new File("F:\\f\\" + file.getOriginalFilename());
                        file.transferTo(localFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new Result(true,StatusCode.OK,"上传成功");
    }
}