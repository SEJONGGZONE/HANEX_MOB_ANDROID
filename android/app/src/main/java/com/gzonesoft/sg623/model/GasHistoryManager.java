package com.gzonesoft.sg623.model;


import android.location.Location;

import com.gzonesoft.sg623.data.GasHistoryInfo;

import java.util.ArrayList;


/**
 * 주유이력 리스트 아이템 관리
 */
public class GasHistoryManager {

    private String tag = "GasHistoryManager";

    private static GasHistoryManager instance = null;

    // 현재 Index
    private int deliveryPosition = 0;
    // 현재 Location
    private Location deliveryLocation = null;
    // 목록
    private ArrayList<GasHistoryInfo> list = new ArrayList<GasHistoryInfo>();
    // 목록 인스턴스얻기
    public static GasHistoryManager getInstance() {
        if (instance == null)
            instance = new GasHistoryManager();

        return instance;
    }
    // 목록 추가
    public void add(GasHistoryInfo info) {
        info.setIdx(list.size());
        list.add(info);
    }
    // 데이타.
    public ArrayList<GasHistoryInfo> values() {
        return list;
    }
    // 지우기
    public void clear() {
        // "java.lang.IndexOutOfBoundsException: Inconsistency detected" 에러관련 보완...지우지말고 새로운 객체로 만들어준다...
        //list.clear();
        list = new ArrayList<GasHistoryInfo>();
    }
    // 지정 Index 데이타 얻기
    public GasHistoryInfo get(int idx) {
        return list.get(idx);
    }


    /**
     * 코드로 정보 얻기..
     * @param geonum
     * @return
     */
    public GasHistoryInfo getInfo(String geonum) {
        GasHistoryInfo info = new GasHistoryInfo();
        try {
            for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
                if (list.get(i).getGEONUM().equals(geonum))
                    info = list.get(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return info;
    }

}
