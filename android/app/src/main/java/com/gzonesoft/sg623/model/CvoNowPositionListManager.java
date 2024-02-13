package com.gzonesoft.sg623.model;

import com.gzonesoft.sg623.data.CvoNowPosition;

import java.util.ArrayList;


public class CvoNowPositionListManager {

    private static CvoNowPositionListManager instance = null;


    private ArrayList<CvoNowPosition> list = new ArrayList<CvoNowPosition>();

    public static CvoNowPositionListManager getInstance() {
        if (instance == null)
            instance = new CvoNowPositionListManager();

        return instance;
    }

    public void add(CvoNowPosition info) {
        info.setIdx(list.size());
        list.add(info);
    }

    public ArrayList<CvoNowPosition> values() {
        return list;
    }



    public void clear() {
        list.clear();
    }

    public CvoNowPosition get(int idx) {
        return list.get(idx);
    }


    /**
     * 기기번호로 정보 얻기
     * @param deviceNo
     * @return
     */
    public CvoNowPosition getCvoNowPositionInfo(String deviceNo) {

        CvoNowPosition retInfo = new CvoNowPosition();

        try {
            for(CvoNowPosition info: list) {
                if (info.getDEVICENO().equals(deviceNo)) {
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

