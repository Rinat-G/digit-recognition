package edu.urfu.project_workshop.neural.util;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.TEMP_DIR;

public class NetLoader {

//    private static final String basePath = System.getProperty("user.home") + "/mnist";

    public static MultiLayerNetwork netLoad() throws IOException {
        File model = new File(TEMP_DIR + "/mnist-model.zip");
        if (!model.exists())
            throw new IOException("Can't find the model");
        return ModelSerializer.restoreMultiLayerNetwork(model);
    }
}
