package edu.urfu.project_workshop.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Deprecated
@Controller
public class UploadController {
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public @ResponseBody
    String provideUploadInfo() {
        return "Вы можете использовать этот URL для загрузки  файлов методом POST ";

    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(
//            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();
//            String fileBaseName = FilenameUtils.getBaseName(originalFilename);
//            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String fileName = FilenameUtils.getName(originalFilename);

            try {
                byte[] bytes = file.getBytes();

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(fileName)));
                stream.write(bytes);
                stream.close();
                return "Загружен файл: " + fileName;

            } catch (Exception e) {
                return "Не удалось загрузить файл " + fileName + ": " + e.getMessage();
            }
        } else {
            return "Не удалось загрузить файл  т.к. он пустой.";
        }
    }


}
