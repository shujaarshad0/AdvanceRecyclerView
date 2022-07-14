package com.shuja.recyclerviewdragndropitem;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<ItemModel> getListPerson(){
        List<ItemModel> models = new ArrayList<>();
        for (int i = 0 ; i <= 62 ; i++){
            models.add(new ItemModel(String.valueOf(i), null));
        }
        return models;
    }
}
