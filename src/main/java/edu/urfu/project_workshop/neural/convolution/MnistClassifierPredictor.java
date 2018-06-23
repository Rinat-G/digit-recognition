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


    public String predictSingleImage(File file) throws IOException {
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, true);
        INDArray image = loader.asRowVector(file);
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);

        return "Prediction: " + net.predict(image)[0];
    }
}
