package edu.urfu.project_workshop.controller;

import edu.urfu.project_workshop.neural.convolution.MnistClassifierPredictor;
import edu.urfu.project_workshop.neural.util.NetLoader;
import org.apache.commons.io.FilenameUtils;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;

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
                return predictor.predictSingleImage(file);
            } catch (IOException e) {
                return "Ошибка: " + e.getMessage();
            }
        } else {
            return "Файл с именем " + name + "не найден";
        }


    }

}
