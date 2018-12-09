package edu.urfu.project_workshop.neural.convolution;

import edu.urfu.project_workshop.neural.transformer.ImagePreProcessor;
import edu.urfu.project_workshop.neural.transformer.ImageTransformer;
import edu.urfu.project_workshop.neural.util.NetLoader;
import lombok.val;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Service
public class MNISTClassifierPredictor {
    private final double RIGHT_ANSWER_THRESHOLD = 0.5;

    private MultiLayerNetwork net;
    private ImageTransformer imageTransformer;
    private ImagePreProcessor imagePreProcessor;

    @Autowired
    public MNISTClassifierPredictor(final NetLoader netLoader,
                                    final ImageTransformer imageTransformer,
                                    final ImagePreProcessor imagePreProcessor) throws IOException {

        try {
            this.net = netLoader.netLoad();
            this.imageTransformer = imageTransformer;
            this.imagePreProcessor = imagePreProcessor;
        } catch (IOException e) {
            throw new IOException("Can't load model", e);
        }
    }


    public String predictSingleImage(File file) throws IOException {

        val transformed = imageTransformer.transform(file);
        imagePreProcessor.processAndPersistForPreview(transformed);
        val imageRowed = imagePreProcessor.process(transformed);
        System.out.println(net.output(imageRowed));
        System.out.println(Arrays.toString(net.predict(imageRowed)));
//        return net.predict(imageRowed)[0];

        return predict(imageRowed);
    }

    private String predict(INDArray image) {
        val results = net.output(image).toDoubleVector();
        double max = 0d;
        int maxIndex = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i] > max) {
                max = results[i];
                maxIndex = i;
            }
        }
        if (max > RIGHT_ANSWER_THRESHOLD) {
            return String.valueOf(maxIndex);
        }
        return "Число определено не точно";

    }

}
