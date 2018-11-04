package edu.urfu.project_workshop.neural.transformer;

import edu.urfu.project_workshop.model.service.ImagePersistService;
import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.LOADED_DIR;

@Component
public class ImagePreProcessor {
    private ImagePersistService persistService;

    @Autowired
    public ImagePreProcessor(ImagePersistService persistService) {
        this.persistService = persistService;
    }

    public INDArray process(File inputFile) throws IOException {
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, false);
        INDArray imageRowed = loader.asRowVector(inputFile);
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(1, 0); //reverse for white-black images because model trained on black white data set
        scaler.transform(imageRowed);
        return imageRowed;

    }

    public void processAndPersistForPreview(File inputFile) throws IOException {
        NativeImageLoader loader = new NativeImageLoader(28, 28, 1, false);
        INDArray imageMatrix = loader.asMatrix(inputFile);
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(255, 0);
        scaler.transform(imageMatrix);
        persistService.persistNDArrayToFile(imageMatrix, inputFile.getName(), LOADED_DIR);
    }
}
