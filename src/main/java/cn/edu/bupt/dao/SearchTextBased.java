package cn.edu.bupt.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public abstract class SearchTextBased extends BaseData{
    
    public SearchTextBased() {
        super();
    }

    public SearchTextBased(UUID id) {
        this.id = id;
    }

    @JsonIgnore
    public abstract String getSearchText(); 

}
