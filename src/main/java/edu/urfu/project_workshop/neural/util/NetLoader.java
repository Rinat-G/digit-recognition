package edu.urfu.project_workshop.neural.util;

import lombok.val;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.deeplearning4j.util.ModelSerializer.restoreMultiLayerNetwork;

@Component
public class NetLoader {

    public MultiLayerNetwork netLoad() throws IOException {
        val modelStream = getClass().getResourceAsStream("/model/mnist-model.zip");
        return restoreMultiLayerNetwork(modelStream);
    }

}
