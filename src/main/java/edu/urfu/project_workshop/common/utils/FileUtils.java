package edu.urfu.project_workshop.common.utils;

import lombok.val;

import java.io.File;

public class FileUtils {
    public static File newFileWithDirs(String path) {
        val file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
}
