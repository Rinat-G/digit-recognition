package edu.urfu.project_workshop.controller;

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

public class PredictorTest {

    @Test
    public void predictor() throws IOException {

        MultiLayerNetwork net;
        net = NetLoader.netLoad();

        File file = new File(TEMP_DIR + "drawn-2018-8-23-21.6.30.png");

        ShowImageTransform showImageTransform = new ShowImageTransform("title123", 10000);
        CropImageTransform cropImageTransform = new CropImageTransform(60);
        BoxImageTransform boxImageTransform = new BoxImageTransform(100, 100);

        LargestBlobCropTransform largestBlobCropTransform = new LargestBlobCropTransform();

        PipelineImageTransform pipelineImageTransform = new PipelineImageTransform(showImageTransform, largestBlobCropTransform, showImageTransform);

        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, pipelineImageTransform);
        INDArray image = loader.asRowVector(file);
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(1, 0);
        scaler.transform(image);

        System.out.println("Prediction: " + net.predict(image)[0]);
    }
}
