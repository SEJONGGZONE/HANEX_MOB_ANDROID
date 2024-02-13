package com.gzonesoft.sg623.data;


import com.gzonesoft.sg623.util.StringUtil;

import java.io.File;

/**
 * 분실물 파일 업로드후 리턴정보 저장..
 */
public class UploadFileInfo {

    private int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    /**
     * {
     * "FileName": "BodyPart_b564b942-4bf6-4e87-936b-37a1a7c907d3",
     * "FileFormat": "jpg",
     * "FileSize": 137084,
     * "FileKind": "lfms",
     * "FilePath1": "201907",
     * "FilePath2": "20190709",
     * "FileCreatedTime": "20190709100826"
     * }
     */
    String FileName;
    String FileFormat;
    String FileSize;
    String FileKind;
    String FilePath1;
    String FilePath2;
    String FileCreatedTime;

    String lfms_seq;
    String found_where;
    String item_nm;
    String file_url;

    File uploadFile;

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getFileName() {
        return StringUtil.getNvlStr(FileName).toString();
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileFormat() {
        return StringUtil.getNvlStr(FileFormat).toString();
    }

    public void setFileFormat(String fileFormat) {
        FileFormat = fileFormat;
    }

    public String getFileSize() {
        return StringUtil.getNvlStr(FileSize).toString();
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getFileKind() {
        return StringUtil.getNvlStr(FileKind).toString();
    }

    public void setFileKind(String fileKind) {
        FileKind = fileKind;
    }

    public String getFilePath1() {
        return StringUtil.getNvlStr(FilePath1).toString();
    }

    public void setFilePath1(String filePath1) {
        FilePath1 = filePath1;
    }

    public String getFilePath2() {
        return StringUtil.getNvlStr(FilePath2).toString();
    }

    public void setFilePath2(String filePath2) {
        FilePath2 = filePath2;
    }

    public String getFileCreatedTime() {
        return StringUtil.getNvlStr(FileCreatedTime).toString();
    }

    public void setFileCreatedTime(String fileCreatedTime) {
        FileCreatedTime = fileCreatedTime;
    }

    @Override
    public String toString() {
        return "UploadFileInfo{" +
                "idx=" + idx +
                ", FileName='" + FileName + '\'' +
                ", FileFormat='" + FileFormat + '\'' +
                ", FileSize='" + FileSize + '\'' +
                ", FileKind='" + FileKind + '\'' +
                ", FilePath1='" + FilePath1 + '\'' +
                ", FilePath2='" + FilePath2 + '\'' +
                ", FileCreatedTime='" + FileCreatedTime + '\'' +
                '}';
    }
}


