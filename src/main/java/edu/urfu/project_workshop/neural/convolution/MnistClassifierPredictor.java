package edu.urfu.project_workshop.neural.convolution;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;
import java.io.IOException;

public class MnistClassifierPredictor {
    private MultiLayerNetwork net;

    public MnistClassifierPredictor(MultiLayerNetwork net) {

        this.net = net;
    }


    public int predictSingleImage(File file) throws IOException {

        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, true);
        INDArray image = loader.asRowVector(file);
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(1, 0); //reverse for white-black images because model trained on black white date set
        scaler.transform(image);

        return net.predict(image)[0];
    }
}
