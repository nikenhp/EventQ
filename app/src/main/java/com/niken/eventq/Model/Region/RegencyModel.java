package com.niken.eventq.Model.Region;

import java.util.List;

public class RegencyModel {
    private Boolean status;
    private Integer code;
    private List<Regency> data = null;

    public Boolean getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public List<Regency> getData() {
        return data;
    }
}
