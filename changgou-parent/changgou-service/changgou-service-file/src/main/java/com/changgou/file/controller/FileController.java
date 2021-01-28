package com.changgou.file.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSClient;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;

@RestController
@CrossOrigin
@RequestMapping
public class FileController {
    /**
     * 文件上传测试
     */
    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file")MultipartFile file)throws Exception{
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename())
        );
        String[] upload = FastDFSClient.upload(fastDFSFile);
        return "http://192.168.211.132:8080"+"/"+upload[0]+"/"+upload[1];
    }
    @GetMapping("/download/{groupName}/{remoteFileName}")
    public Result download(@PathVariable(value = "groupName") String groupName,@PathVariable(value = "remoteFileName") String remoteFileName) throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient();
        InputStream inputStream = fastDFSClient.download(groupName, remoteFileName);
        FileOutputStream fileOutputStream = new FileOutputStream("D:/2.png");
        byte[] bytes=new byte[1024];
        while ((inputStream.read(bytes)) !=-1){
            fileOutputStream.write(bytes);
        }
        fileOutputStream.close();
        inputStream.close();
        return new Result(true, StatusCode.OK,"下载成功");
    }
}
