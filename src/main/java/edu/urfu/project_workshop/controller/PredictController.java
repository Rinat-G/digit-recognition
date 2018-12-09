package edu.urfu.project_workshop.controller;

import edu.urfu.project_workshop.common.utils.StringShortener;
import edu.urfu.project_workshop.model.service.ImagePersistService;
import edu.urfu.project_workshop.neural.convolution.MNISTClassifierPredictor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PredictController {

    private ImagePersistService persistService;
    private MNISTClassifierPredictor predictor;

    @Autowired
    public PredictController(final ImagePersistService persistService, final MNISTClassifierPredictor predictor) {
        this.persistService = persistService;
        this.predictor = predictor;
    }

    @CrossOrigin
    @RequestMapping(value = "/predict", method = RequestMethod.POST)
    @ResponseBody
    public String predictPost(@RequestParam("data") String data, @RequestParam("name") String name) {

        System.out.println("/predict controller got data: " + StringShortener.shortenString(data));

        val imageFile = persistService.persistBase64ToFile(data, name);

        try {
            return "" + predictor.predictSingleImage(imageFile);
        } catch (Exception e) {
            return "Got exception: " + e.getMessage();
        }
    }

}
