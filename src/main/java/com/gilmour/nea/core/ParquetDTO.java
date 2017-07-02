package com.gilmour.nea.core;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ParquetDTO {

    private String filePath;

    private String filename;

    private String uploadCode;

    public ParquetDTO(String filePath, String filename, String uploadCode) {
        this.filePath = filePath;
        this.filename = filename;
        this.uploadCode = uploadCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFilename() {
        return filename;
    }

    public String getUploadCode() {
        return uploadCode;
    }
}
