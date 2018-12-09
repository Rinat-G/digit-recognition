package edu.urfu.project_workshop.neural.convolution;

import edu.urfu.project_workshop.neural.transformer.ImagePreProcessor;
import edu.urfu.project_workshop.neural.transformer.ImageTransformer;
import edu.urfu.project_workshop.neural.util.NetLoader;
import lombok.val;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class MNISTClassifierPredictor {
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


    public int predictSingleImage(File file) throws IOException {

        val transformed = imageTransformer.transform(file);
        imagePreProcessor.processAndPersistForPreview(transformed);
        val imageRowed = imagePreProcessor.process(transformed);
        return net.predict(imageRowed)[0];
    }

}
