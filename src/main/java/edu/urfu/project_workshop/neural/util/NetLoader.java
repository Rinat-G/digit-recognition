package edu.urfu.project_workshop.neural.util;

import lombok.val;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.TEMP_DIR;
import static org.deeplearning4j.util.ModelSerializer.restoreMultiLayerNetwork;

@Component
public class NetLoader {

    public MultiLayerNetwork netLoad() throws IOException {
        val model = new File(TEMP_DIR + "/mnist-model.zip");
        if (!model.exists())
            throw new IOException("Can't find the model");
        return restoreMultiLayerNetwork(model);
    }
}
