package com.example.demo.service;

import com.example.demo.entity.file.Chunk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 大文件处理
 *
 * @author 刘声凤 w_liushengfeng@kingsoft.com
 * @date 2022/3/11 11:23
 */
@Service
@Slf4j
public class BigFileService {
    public String fileUploadPost(Chunk chunk) throws Exception {
        /**
         * 每一个上传块都会包含如下分块信息：
         * chunkNumber: 当前块的次序，第一个块是 1，注意不是从 0 开始的。
         * totalChunks: 文件被分成块的总数。
         * chunkSize: 分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大。
         * currentChunkSize: 当前块的大小，实际大小。
         * totalSize: 文件总大小。
         * identifier: 这个就是每个文件的唯一标示。
         * filename: 文件名。
         * relativePath: 文件夹上传的时候文件的相对路径属性。
         * 一个分块可以被上传多次，当然这肯定不是标准行为，但是在实际上传过程中是可能发生这种事情的，这种重传也是本库的特性之一。
         *
         * 根据响应码认为成功或失败的：
         * 200 文件上传完成
         * 201 文加快上传成功
         * 500 第一块上传失败，取消整个文件上传
         * 507 服务器出错自动重试该文件块上传
         */
        File file = new File("/data/upload", chunk.getFilename());
        //第一个块,则新建文件
        if (chunk.getChunkNumber() == 1 && !file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                return "exception:createFileException";
            }
        }

        //进行写文件操作
        try (
                //将块文件写入文件中
                InputStream fos = chunk.getFile().getInputStream();
                //添加 rwd 解决多线程文件写入失败问题。
                RandomAccessFile raf = new RandomAccessFile(file, "rwd")
        ) {
            int len = -1;
            byte[] buffer = new byte[1024];
            raf.seek((chunk.getChunkNumber() - 1) * 1024 * 1024 * 5);
            while ((len = fos.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("上传失败",e);
            if (chunk.getChunkNumber() == 1) {
                file.delete();
            }
            throw new Exception("上传失败");
        }
        if (chunk.getChunkNumber().equals(chunk.getTotalChunks())) {
            // TODO 向数据库中保存上传信息
            return "over";
        } else {
            return "ok";
        }
    }
}
