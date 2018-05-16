package com.example.playenglish.bean;

/**
 * Created by 解奕鹏 on 2018/5/9.
 */

public class Word {
    private String key;
    private String psEnglish;
    private String dict_pron;

    public String getDict_pron() {
        return dict_pron;
    }

    public void setDict_pron(String dict_pron) {
        this.dict_pron = dict_pron;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPsEnglish() {
        return psEnglish;
    }

    public void setPs(String psEnglish) {
        this.psEnglish = psEnglish;
    }

    @Override
    public String toString() {
        return "key"+key+",ps"+psEnglish;
    }
}
