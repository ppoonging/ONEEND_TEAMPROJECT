package com.springboot.biz.mj.board;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class MjthumbnailService {
    public void createThumbnail(File originalFile, File thumbnilFile) throws  Exception {
        BufferedImage originalImage = ImageIO.read(originalFile);

        //썸네일 이미지 설정
        int width = 100;
        int height = 100;

        BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnailImage.createGraphics();


        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        ImageIO.write(thumbnailImage, "jpg", thumbnilFile);


    }
    }
