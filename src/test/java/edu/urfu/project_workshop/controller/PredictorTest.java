package edu.urfu.project_workshop.controller;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.datavec.image.data.ImageWritable;
import org.datavec.image.loader.Java2DNativeImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.BoxImageTransform;
import org.datavec.image.transform.LargestBlobCropTransform;
import org.datavec.image.transform.PipelineImageTransform;
import org.datavec.image.transform.ShowImageTransform;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.TEMP_DIR;
import static java.lang.Math.round;

public class PredictorTest {

    @Test
    public void predictor() throws IOException {

//        MultiLayerNetwork net;
//        net = NetLoader.netLoad();

        File file = new File(TEMP_DIR + "drawn-2018-9-27-19.9.48.png");

        ShowImageTransform showImageTransform = new ShowImageTransform("title1", 1000000);


        LargestBlobCropTransform largestBlobCropTransform = new LargestBlobCropTransform();

        PipelineImageTransform pipelineImageTransform =
                new PipelineImageTransform(
                        showImageTransform,
                        largestBlobCropTransform,
                        showImageTransform);

        BufferedImage bufferedImage = ImageIO.read(file);
        Frame frame = new Java2DFrameConverter().getFrame(bufferedImage);
        ImageWritable imageTransformed = pipelineImageTransform.transform(new ImageWritable(frame));

        int maxDimension = imageTransformed.getHeight() > imageTransformed.getWidth()
                ? imageTransformed.getHeight()
                : imageTransformed.getWidth();

        System.out.println("maxDimension" + maxDimension);
        System.out.println("getHeight()" + imageTransformed.getHeight());
        System.out.println("getWidth()" + imageTransformed.getWidth());

        int width = (int) round(maxDimension + (maxDimension * 0.05));
        int height = (int) round(maxDimension + (maxDimension * 0.10));
        imageTransformed = new BoxImageTransform(width, height)
                .borderValue(new opencv_core.Scalar(255.0, 255.0, 255.0, 255.0))
                .transform(imageTransformed);

        BufferedImage bufferedImageTransformed = new Java2DFrameConverter().getBufferedImage(imageTransformed.getFrame());
        File outputFile = new File(TEMP_DIR + "imageTransformed1.png");
        ImageIO.write(bufferedImageTransformed, "png", outputFile);

//        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, pipelineImageTransform);
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1);
        INDArray image = loader.asMatrix(imageTransformed.getFrame());
//
//
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(255, 0);
        scaler.transform(image);

        BufferedImage imageLoaded = new Java2DNativeImageLoader().asBufferedImage(image);
        File outputLoadedFile = new File(TEMP_DIR + "imageLoaded1.png");
        ImageIO.write(imageLoaded, "png", outputLoadedFile);


//        System.out.println("Prediction: " + net.predict(image)[0]);
    }
}
