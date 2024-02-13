package com.gzonesoft.sg623.model;

import com.gzonesoft.sg623.data.ItemInfo;

import java.util.ArrayList;


public class ItemListManager {

    private static ItemListManager instance = null;


    private ArrayList<ItemInfo> list = new ArrayList<ItemInfo>();

    public static ItemListManager getInstance() {
        if (instance == null)
            instance = new ItemListManager();

        return instance;
    }

    public void add(ItemInfo info) {
        info.setIdx(list.size());
        list.add(info);
    }

    public ArrayList<ItemInfo> values() {
        return list;
    }



    public void clear() {
        list.clear();
    }

    public ItemInfo get(int idx) {
        return list.get(idx);
    }


    /**
     * 상품코드로 상품정보 얻기
     * @param code
     * @return
     */
    public ItemInfo getItemInfo(String code) {

        ItemInfo retInfo = new ItemInfo();

        try {
            for(ItemInfo info: list) {
                if (info.getITCODE().equals(code)) {
                    retInfo = info;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return retInfo;
        }

    }
}

