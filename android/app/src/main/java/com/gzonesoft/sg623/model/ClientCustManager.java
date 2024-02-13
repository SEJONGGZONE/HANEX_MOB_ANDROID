package com.gzonesoft.sg623.model;


import com.gzonesoft.sg623.data.ClientCustInfo;

import java.util.ArrayList;


public class ClientCustManager {

    private static ClientCustManager instance = null;


    private ArrayList<ClientCustInfo> list = new ArrayList<ClientCustInfo>();

    public static ClientCustManager getInstance() {
        if (instance == null)
            instance = new ClientCustManager();

        return instance;
    }

    public void add(ClientCustInfo info) {
        info.setIdx(list.size());
        list.add(info);
    }

    public ArrayList<ClientCustInfo> values() {
        return list;
    }



    public void clear() {
        list.clear();
    }

    public ClientCustInfo get(int idx) {
        return list.get(idx);
    }


    /**
     * 거래처정보 얻기
     * @param code
     * @return
     */
    public ClientCustInfo getClientCustInfo(String code) {

        ClientCustInfo retInfo = new ClientCustInfo();

        try {
            for(ClientCustInfo info: list) {
                if (info.getCLCODE().equals(code)) {
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

