package com.springboot.biz.recipe.fm;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class FmReThumbnailService {

    public void createThumbnail(File originalFile,File thumbnailFile) throws  Exception{
        BufferedImage originalImage = ImageIO.read(originalFile);

        //썸네일 이미지 설정
        int width = 100;
        int height = 100;


        BufferedImage thumbnailImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnailImage.createGraphics();


        g.drawImage(originalImage,0,0,width,height,null);
        g.dispose();

        ImageIO.write(thumbnailImage,"jpg",thumbnailFile);


    }






}
