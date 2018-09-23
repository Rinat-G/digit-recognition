package edu.urfu.project_workshop.controller;

import edu.urfu.project_workshop.neural.convolution.MnistClassifierPredictor;
import edu.urfu.project_workshop.neural.util.NetLoader;
import org.apache.commons.io.IOUtils;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static edu.urfu.project_workshop.common.Constants.TEMP_DIR;

@Controller
public class PredictController {

    @RequestMapping(value = "/predict", method = RequestMethod.GET)
    @ResponseBody
    public String predictNumber(
            @RequestParam("name") String name
    ) {
        MultiLayerNetwork network;
        try {
            network = NetLoader.netLoad();
        } catch (IOException e) {
            return "Не удалось загрузить модель: " + e.getMessage();
        }

        MnistClassifierPredictor predictor = new MnistClassifierPredictor(network);
        File file = new File(name);
        if (file.exists()) {
            try {
                return "" + predictor.predictSingleImage(file);
            } catch (IOException e) {
                return "Ошибка: " + e.getMessage();
            }
        } else {
            return "Файл с именем " + name + "не найден";
        }


    }

    @CrossOrigin
    @RequestMapping(value = "/predict", method = RequestMethod.POST)
    @ResponseBody
    public String predictPost(
            @RequestParam("data") String data,
            @RequestParam("name") String name) {
        System.out.println("На /predict контроллер получены данные: " + data);

        String base64Image = data.split(",")[1];
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

        File tempFolder = new File(TEMP_DIR);

        if (!tempFolder.exists() || !tempFolder.isDirectory()) {
            tempFolder.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(TEMP_DIR + name + ".png");
             ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            IOUtils.copy(bis, fos);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(TEMP_DIR + name + ".png");


        MultiLayerNetwork network;
        try {
            network = NetLoader.netLoad();
        } catch (IOException e) {
            return "Не удалось загрузить модель: " + e.getMessage();
        }

        MnistClassifierPredictor predictor = new MnistClassifierPredictor(network);
        File file = new File(TEMP_DIR + name + ".png");

        try {
            return "" + predictor.predictSingleImage(file);
        } catch (IOException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

}
