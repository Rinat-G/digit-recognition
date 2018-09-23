package edu.urfu.project_workshop.controller;

import edu.urfu.project_workshop.common.LargestBlobSquareCropTransform;
import edu.urfu.project_workshop.neural.util.NetLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.TEMP_DIR;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_CCOMP;

public class PredictorTest {

    @Test
    public void predictor() throws IOException {

        MultiLayerNetwork net;
        net = NetLoader.netLoad();

        File file = new File(TEMP_DIR + "drawn-2018-8-23-21.6.23.png");

        ShowImageTransform showImageTransform = new ShowImageTransform("title123", 1000000);
        CropImageTransform cropImageTransform = new CropImageTransform(60);
        BoxImageTransform boxImageTransform = new BoxImageTransform(100, 100);

//        LargestBlobCropTransform largestBlobCropTransform = new LargestBlobCropTransform();
//        LargestBlobCropTransform largestBlobCropTransform = new LargestBlobCropTransform(null, CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, 3, 3, 100, 200, true);
        LargestBlobCropTransform largestBlobCropTransform = new LargestBlobCropTransform(null, CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, 3, 3, 100, 100, false);
        LargestBlobSquareCropTransform largestBlobSquareCropTransform = new LargestBlobSquareCropTransform();

        PipelineImageTransform pipelineImageTransform =
                new PipelineImageTransform(
                        showImageTransform,
                        largestBlobCropTransform,
                        showImageTransform);


        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, pipelineImageTransform);
        INDArray image = loader.asRowVector(file);
//        loader.asWritable();
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(1, 0);
        scaler.transform(image);

        System.out.println("Prediction: " + net.predict(image)[0]);
    }
}
