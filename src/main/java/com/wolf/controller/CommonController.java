package com.wolf.controller;

import com.wolf.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        // file是一个临时文件,需要转存到指定位置,否则本次请求之后临时文件会删除
       log.info("文件上传: {}",file.toString());

       // 原始文件名
        String originalFilename = file.getOriginalFilename();
        int start = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(start); // .jpg

        // 使用UUID重新生成文件名,防止文件名重复
        String fileName = UUID.randomUUID().toString() + suffix; // abc.jpg

        File dir = new File(basePath);
        if (!dir.exists()){
            // 如果目录不存在
            dir.mkdirs();
        }

        try {
            // 将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

       return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        // 输入流读取文件内容
        try {
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));

            // 输出流写回文件内容到浏览器中
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpg");
            byte[] bytes = new byte[1024];
            int length = 0;
            while (( length = inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,length);
                outputStream.flush();
            }
            // 关闭流
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
