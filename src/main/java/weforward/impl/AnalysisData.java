package weforward.impl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 1
 * @create 2020/10/29 10:11
 */
public class AnalysisData {

    public String name;

    public Integer value;


    public AnalysisData(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
