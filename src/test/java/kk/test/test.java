package kk.test;

import org.junit.Test;

import java.util.HashMap;

public class test {

    @Test
    public void heelo() {
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1",999);
        if(map.get("1") == null){
            map.put("1",2);
        }
        else{
            map.put("1",map.get("1")+1);
        }
        System.out.println(map.get("1"));
    }
}
