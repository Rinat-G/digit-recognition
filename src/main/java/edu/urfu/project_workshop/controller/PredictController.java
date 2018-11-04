package edu.urfu.project_workshop.controller;

import edu.urfu.project_workshop.model.service.ImagePersistService;
import edu.urfu.project_workshop.neural.convolution.MNISTClassifierPredictor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@Controller
public class PredictController {

    private ImagePersistService persistService;
    private MNISTClassifierPredictor predictor;

    @Autowired
    public PredictController(final ImagePersistService persistService, final MNISTClassifierPredictor predictor) {
        this.persistService = persistService;
        this.predictor = predictor;
    }


    @RequestMapping(value = "/predict", method = RequestMethod.GET)
    @ResponseBody
    public String predictNumber(
            @RequestParam("name") String name
    ) {
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
    public String predictPost(@RequestParam("data") String data, @RequestParam("name") String name) {

        System.out.println("На /predict контроллер получены данные: " + data);

        val imageFile = persistService.persistBase64ToFile(data, name);

        try {
            return "" + predictor.predictSingleImage(imageFile);
        } catch (IOException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

}
