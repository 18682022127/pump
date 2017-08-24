package com.itouch8.pump.web.upload;

import java.util.List;

public class FileForm {

    private List<FileData> fileDatas;

    private String addonData = null;

    public List<FileData> getFileDatas() {
        return fileDatas;
    }

    public void setFileDatas(List<FileData> fileDatas) {
        this.fileDatas = fileDatas;
    }

    public String getAddonData() {
        return addonData;
    }

    public void setAddonData(String addonData) {
        this.addonData = addonData;
    }

}
