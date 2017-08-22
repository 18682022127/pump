package com.itouch8.pump.web.upload;

import java.util.List;

public class FileForm {

    private IUploadFile[] files;

    private List<FileData> fileDatas;

    public IUploadFile[] getFiles() {
        return files;
    }

    public void setFiles(IUploadFile[] files) {
        this.files = files;
    }

    public List<FileData> getFileDatas() {
        return fileDatas;
    }

    public void setFileDatas(List<FileData> fileDatas) {
        this.fileDatas = fileDatas;
    }

}
