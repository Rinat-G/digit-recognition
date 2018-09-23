package edu.urfu.project_workshop.controller;

import org.datavec.image.loader.Java2DNativeImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.TEMP_DIR;

public class ImageLoaderTest {


    @Test
    public void loaderCropTest() throws IOException {
        File imgFile = new File(TEMP_DIR + "drawn-2018-8-23-18.41.55.png");
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, true);
//        ImageWritable imageWritable = loader.asWritable(imgFile);

        INDArray imageArray = loader.asMatrix(imgFile);

        Java2DNativeImageLoader java2DNativeImageLoader = new Java2DNativeImageLoader(28, 28, 1);
        BufferedImage bufferedImage = java2DNativeImageLoader.asBufferedImage(imageArray);
        File outputFile = new File(TEMP_DIR + "output/image3.png");
        ImageIO.write(bufferedImage, "png", outputFile);

//        ImageWritable imgWritable =  loader.asWritable(imgFile);
//
//        Frame frame = imgWritable.getFrame();
//
//        BufferedImage bufferedImage = new BufferedImage(200, 200,1);
//        Java2DFrameConverter.copy(frame, bufferedImage);
//        File outputFile = new File(TEMP_DIR + "output/image2.png");
//        ImageIO.write(bufferedImage, "png", outputFile);
    }
}
