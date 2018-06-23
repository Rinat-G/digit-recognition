package edu.urfu.project_workshop.neural.util;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.io.IOException;

public class NetLoader {

    private static final String basePath = System.getProperty("java.io.tmpdir") + "/mnist";

    public static MultiLayerNetwork netLoad() throws IOException {
        File model = new File(basePath + "/minist-model.zip");
        if (!model.exists())
            throw new IOException("Can't find the model");
        return ModelSerializer.restoreMultiLayerNetwork(model);
    }
}
