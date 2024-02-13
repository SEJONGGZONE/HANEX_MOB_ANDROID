package com.gzonesoft.sg623.model;


import android.location.Location;

import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * 배송리스트(업무메인) 리스트 아이템 관리
 */
public class DeliveryListManager {

    private String tag = "DeliveryListManager";

    private static DeliveryListManager instance = null;

    // 현재 배송지 Index
    private int deliveryPosition = 0;
    // 현재 배송지 Location
    private Location deliveryLocation = null;
    // 배송지 목록
    private ArrayList<DeliveryListInfo> list = new ArrayList<DeliveryListInfo>();
    // 배송지 목록 인스턴스얻기
    public static DeliveryListManager getInstance() {
        if (instance == null)
            instance = new DeliveryListManager();

        return instance;
    }
    // 배송지 목록 추가
    public void add(DeliveryListInfo info) {
        info.setIdx(list.size());
        list.add(info);
    }
    // 데이타.
    public ArrayList<DeliveryListInfo> values() {
        return list;
    }
    // 지우기
    public void clear() {
        // "java.lang.IndexOutOfBoundsException: Inconsistency detected" 에러관련 보완...지우지말고 새로운 객체로 만들어준다...
        //list.clear();
        list = new ArrayList<DeliveryListInfo>();
    }

    // 거리순 정렬 준비..
    public class DistanceComparator implements Comparator<DeliveryListInfo> {
        public int compare(DeliveryListInfo p1, DeliveryListInfo p2) {
            //return p1.getDISTANCE_ME_VALUE().compareTo(p2.getDISTANCE_ME_VALUE());
            if (p1.getDISTANCE_ME_VALUE() > p2.getDISTANCE_ME_VALUE()) {
                return 1;
            } else if (p1.getDISTANCE_ME_VALUE() < p2.getDISTANCE_ME_VALUE()) {
                return -1;
            }
            return 0;
        }
    }

    // 거리순 정렬
    public void sortByDistance() {
        Collections.sort(list, new DistanceComparator());
    }



    // 지정 Index 배송지 데이타 얻기
    public DeliveryListInfo get(int idx) {
        return list.get(idx);
    }
    // 현재 배송지 Index 얻기
    public int getDeliveryPosition() {
        return deliveryPosition;
    }
    // 현재 배송지 Index 세팅 + 배송지 Location 정보도 함께저장..
    public void setDeliveryPosition(int deliveryPosition) {

        // 현재 배송지 Location 저장
        setCurDeliveryLocation(deliveryPosition);
        // 현재 배송지 Index 저장
        this.deliveryPosition = deliveryPosition;
    }
    // 현재 배송지 Location 얻기
    public Location getDeliveryLocation() {
        return deliveryLocation;
    }

    // 지정 배송지Index를 현재 배송위치정보로 저장
    private void setCurDeliveryLocation(int position) {

//        // 범위를 넘는 버그 확인 - 조치함.
//        if (list.size() <= position) return;
//
//        // 0-1.위치정보가 있는경우만 해당..
//        String slat = StringUtil.getNvlStr(list.get(position).getLATITUDE()).toString();
//        String slng = StringUtil.getNvlStr(list.get(position).getLONGITUDE()).toString();
//
//        if (!TextUtils.isEmpty(slat) && !TextUtils.isEmpty(slng))
//        {
//            // 0-2.배송위치 저장(Location)
//            Location location = new Location("deliverylocation");
//            location.setLongitude(Double.parseDouble(slng));
//            location.setLatitude(Double.parseDouble(slat));
//            // 현재 배송지 위치 세팅
//            setDeliveryLocation(location);
//        } else {
//            // 현재 배송지 위치 세팅
//            setDeliveryLocation(null);
//        }
    }
    // 현재 배송지 위치 세팅
    public void setDeliveryLocation(Location deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }


    // 다음배송지 목록위치(NEXT_ROUTE_FLAG 기준) - 현재 미사용
    public int getNextDeliveryPosition() {
        boolean bFind = false;
        int nFindPosition = 0;
//        for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
//            DeliveryListInfo info = list.get(i);
//            if (!bFind) {
//                if ("Y".equals(info.getNEXT_ROUTE_FLAG())) {
//                    nFindPosition = i;
//                    bFind = true;
//                }
//            }
//        }
        return nFindPosition;
    }

    /**
     * 거래처코드로 배송지정보 얻기..
     * @param clcode
     * @return
     */
    public DeliveryListInfo getInfo(String clcode) {
        DeliveryListInfo info = new DeliveryListInfo();
        try {
            for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
                if (list.get(i).getCLCODE().equals(clcode))
                    info = list.get(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return info;
    }

    // 다음 배송지중 위치정보가 있는 배송지의 위치를 리턴(기준위치없이 처음부터 수행)
    public int getNextPosWithPositionInfo() {
        boolean bFind = false;
        int nFindPosition = 0;
//        for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
//            DeliveryListInfo info = list.get(i);
//            if (!bFind) {
//                // 0.위치정보가 있는경우만 해당..
//                String slat = StringUtil.getNvlStr(info.getLATITUDE()).toString();
//                String slng = StringUtil.getNvlStr(info.getLONGITUDE()).toString();
//                if (
//                        (!"".equals(slat)) && (!"".equals(slng))
//                        ||
//                        (!"null".equals(slat)) && (!"null".equals(slng))
//                    ) {
//                    // 1.배송상태가 배송전인경우..
//                    if ("배송전".equals(info.getAPP_DELIVERY_ST_NM())) {
//                        nFindPosition = i;
//                        bFind = true;
//                    }
//                }
//            }
//        }
//        if (!bFind) nFindPosition = -1;
        return nFindPosition;
    }

    // 다음 배송지중 위치정보가 있는 배송지의 위치를 리턴(기준위치없이 처음부터 수행 + 지정위치제외) - 현재 배송지를 완료하지 않은 상태에서 다음배송지를 미리가져올경우 사용
    public int getNextPosWithPositionAnother(int selectPosition) {
        boolean bFind = false;
        int nFindPosition = -1;
//        for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
//            DeliveryListInfo info = list.get(i);
//            if (!bFind) {
//                // 0.위치정보가 있는경우만 해당..
//                String slat = StringUtil.getNvlStr(info.getLATITUDE()).toString();
//                String slng = StringUtil.getNvlStr(info.getLONGITUDE()).toString();
//                if (
//                        (!"".equals(slat)) && (!"".equals(slng))
//                                ||
//                                (!"null".equals(slat)) && (!"null".equals(slng))
//                        ) {
//                    // 1.배송상태가 배송전인경우..
//                    if ("배송전".equals(info.getAPP_DELIVERY_ST_NM())) {
//                        // 2.지정위치 제외..
//                        if (i != selectPosition) {
//                            nFindPosition = i;
//                            bFind = true;
//                        }
//                    }
//                }
//            }
//        }
        return nFindPosition;
    }

    // 다음 배송지, SKPI건 찾기...
    // 1.위치정보가 있는 ..
    // 2.배송완료일시가 없는 ..
    // 3.'SKIP'보고를 한 .. SKIP_YN = 'Y'
    // 4.현재 배송건 제외 ..
    public int getNextSkipPosWithPositionAnother(int selectPosition) {
        boolean bFind = false;
        int nFindPosition = -1;
//        for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
//            DeliveryListInfo info = list.get(i);
//            if (!bFind) {
//                // 1.위치정보가 있는경우만 해당..
//                String slat = StringUtil.getNvlStr(info.getLATITUDE()).toString();
//                String slng = StringUtil.getNvlStr(info.getLONGITUDE()).toString();
//                if (
//                        (!"".equals(slat)) && (!"".equals(slng))
//                                ||
//                                (!"null".equals(slat)) && (!"null".equals(slng))
//                        ) {
//                    // 2.배송완료일시가 없는 ..
//                    if (TextUtils.isEmpty(info.getAPP_SIGN_TIME())) {
//                        // 3.'SKIP'보고를 한 .. SKIP_YN = 'Y'
//                        if ("Y".equals(info.getAPP_SKIP_YN())) {
//                            // 4.현재 배송건 제외 ..
//                            if (i != selectPosition) {
//                                nFindPosition = i;
//                                bFind = true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
        return nFindPosition;
    }


    // 기준위치에서 다음 배송지중 위치정보가 있는 배송지의 위치를 리턴
    public int getNextPosWithPositionInfo(int startPosition) {
        boolean bFind = false;
        int nFindPosition = 0;
//        for (int i = startPosition+1; i < list.size(); i++) {  // 리스트 Loop..
//            DeliveryListInfo info = list.get(i);
//            if (!bFind) {
//                // 위치정보가 있는경우만 해당..
//                String slat = StringUtil.getNvlStr(info.getLATITUDE()).toString();
//                String slng = StringUtil.getNvlStr(info.getLONGITUDE()).toString();
//                if (
//                        (!"".equals(slat)) && (!"".equals(slng))
//                                ||
//                                (!"null".equals(slat)) && (!"null".equals(slng))
//                        ) {
//                    nFindPosition = i;
//                    bFind = true;
//                }
//            }
//        }
        return nFindPosition;
    }

    // 지정위치의 배송정보를 리턴해준다.
    public String getPopMsgDeliveryInfo(int position) {
        if ((position < 0) || (position >= list.size()) ) return "";
        DeliveryListInfo info = list.get(position);
        String retStr = "";
//        if (TextUtils.isEmpty(info.getSORD_NO())) {
//            //retStr = info.getRCPTNAME_TXT() + "님" + info.getDELIVERY_TIME() + "요청";
//            String timeInfo = "------------------------------------------------" +
//                    "\n배송순번 : " + info.getROUTE_IDX() +
//                    "\n수취인 : " + info.getRCPTNAME_TXT() +
//                    "\n배송요청시간 : " + info.getDELIVERY_TIME() +
//                    "\n상태 : " + info.getDELIVERY_TYPE_NM() + "/" + info.getAPP_DELIVERY_ST_NM() +
//                    "\n------------------------------------------------\n";
//            retStr = timeInfo;
//        } else {
//            //retStr = "[" + info.getROUTE_IDX() + "-" + info.getSORD_NO() + "]" + info.getRCPTNAME_TXT() + "님" + info.getDELIVERY_TIME() + "요청";
//
//            String timeInfo = "------------------------------------------------" +
//                    "\n배송순번 : " + info.getROUTE_IDX() + "-" + info.getSORD_NO() +
//                    "\n수취인 : " + info.getRCPTNAME_TXT() +
//                    "\n배송요청시간 : " + info.getDELIVERY_TIME() +
//                    "\n상태 : " + info.getDELIVERY_TYPE_NM() + "/" + info.getAPP_DELIVERY_ST_NM() +
//                    "\n------------------------------------------------\n";
//            retStr = timeInfo;
//        }
        return retStr;
    }

    // 지정위치의 배송지정보를 리턴해준다.(지도에서 마커 클릭)
    public String getPopMsgPositionInfo(int position) {
        if ((position < 0) || (position >= list.size()) ) return "";
        DeliveryListInfo info = list.get(position);
        String retStr = "";
//        if (TextUtils.isEmpty(info.getSORD_NO())) {
//            //retStr = info.getRCPTNAME_TXT() + "님" + info.getDELIVERY_TIME() + "요청";
//            String timeInfo = "------------------------------------------------" +
//                    "\n배송순번 : " + info.getROUTE_IDX() +
//                    "\n수취인 : " + info.getRCPTNAME_TXT() +
//                    "\n배송요청시간 : " + info.getDELIVERY_TIME() +
//                    "\n상태 : " + info.getDELIVERY_TYPE_NM() + "/" + info.getAPP_DELIVERY_ST_NM() +
//                    "\n주소 : " + info.getADDR() +
//                    "\n------------------------------------------------\n";
//            retStr = timeInfo;
//        } else {
//            //retStr = "[" + info.getROUTE_IDX() + "-" + info.getSORD_NO() + "]" + info.getRCPTNAME_TXT() + "님" + info.getDELIVERY_TIME() + "요청";
//
//            String timeInfo = "------------------------------------------------" +
//                    "\n배송순번 : " + info.getROUTE_IDX() + "-" + info.getSORD_NO() +
//                    "\n수취인 : " + info.getRCPTNAME_TXT() +
//                    "\n배송요청시간 : " + info.getDELIVERY_TIME() +
//                    "\n상태 : " + info.getDELIVERY_TYPE_NM() + "/" + info.getAPP_DELIVERY_ST_NM() +
//                    "\n주소 : " + info.getADDR() +
//                    "\n------------------------------------------------\n";
//            retStr = timeInfo;
//        }
        return retStr;
    }

    // ------------------------------------------------------------------------------------
    // 배송지 출발정보가 있는 건의 최종 배송정보를리턴..
    // ------------------------------------------------------------------------------------
    public DeliveryListInfo getLastDeptInfo() {
        boolean bFind = false;
        DeliveryListInfo retInfo = null;
//        int nFindPosition = -1;
//        for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
//            DeliveryListInfo info = list.get(i);
//            // 배송지 출발정보 확인...
//            if (!TextUtils.isEmpty(info.getAPP_DEP_TIME())) {
//                nFindPosition = i;
//            }
//        }
//        if (nFindPosition > -1) {
//            // 출발정보가 있는 배송지가 있다.
//            retInfo = list.get(nFindPosition);
//        }
        return retInfo;
    }


    // ------------------------------------------------------------------------------------
    // 배송지점 반경거리 확인
    // ------------------------------------------------------------------------------------
    public boolean checkGeoFenceDelivery(Location targetLocation, int chkDistance) {
        boolean bGeofenceYN = false;
        // 배송지점위치 얻기
        Location srcLocation= getDeliveryLocation();
        if (srcLocation != null) {
            Loggers.d2(this, ">>>>> 배송지점 반경거리 확인");
            bGeofenceYN = CommonUtil.with().checkGeofence(srcLocation, targetLocation, chkDistance);
        } else {
            Loggers.d("---- 배송지점 위치정보 없음");
            bGeofenceYN = false;
        }
        return bGeofenceYN;
    }



    // ------------------------------------------------------------------------------------
    // 주문번호기준 배송정보를 리턴 - 배송취소 관련 로직 추가
    // ------------------------------------------------------------------------------------
    public DeliveryListInfo getDeliveryCancleInfoByOrderNo(String orderNo) {

        DeliveryListInfo retInfo = null;
//        int nFindPosition = -1;
//        for (int i = 0; i < list.size(); i++) {  // 리스트 Loop..
//
//            // 아직 찾기전...
//            if (nFindPosition == -1) {
//                // 배송목록 얻기
//                DeliveryListInfo info = list.get(i);
//                // 0.위치정보가 있는경우만 해당..
//                String slat = StringUtil.getNvlStr(info.getLATITUDE()).toString();
//                String slng = StringUtil.getNvlStr(info.getLONGITUDE()).toString();
//                if (
//                        (!"".equals(slat)) && (!"".equals(slng))
//                                ||
//                                (!"null".equals(slat)) && (!"null".equals(slng))
//                        ) {
//                    // 1.배송상태가 배송전인경우..
//                    if ("배송전".equals(info.getAPP_DELIVERY_ST_NM())) {
//                        nFindPosition = i;  // 다음배송지의 대상건의 취소시 운송실적 인정하는 Rule 협의에 따라 이후건은 무시하기 위한 로직...
//                        // 배송지 주문번호 확인...
//                        if (orderNo.equals(String.valueOf(info.getORD_NO()))) {
//                            retInfo = info;
//                        }
//                    }
//                }
//            }
//        }
        return retInfo;
    }

    /**
     * 메뉴얼건 생성여부 확인 메소드
     * @param position
     * @return
     */
    public String checkMenualOrderYn(int position, boolean vanStartYn) {

        String sRet = "N";

//        // 1번째 배송건일경우 ...
//        if (position == 0) {
//            // 점포 출발 여부에 따라 건수인정 여부를 판단
//
//            // 점포출발 확인
//            if (vanStartYn) {
//                // 건수인정 함 : 점포출발 함
//                sRet = "Y";
//            } else {
//                // 건수인정 하지않음 : 점포출발 하지 않음
//                sRet = "N";
//            }
//        }
//        // 2번째 배송건 이상의 경우 ...
//        else {
//
//            // 해당 배송건의 바로 이전 주문정보 확인
//            DeliveryListInfo prevInfo = list.get(position-1);
//
//            // 배송전의 상태인경우 - 건수인정 안함
//            if ("배송전".equals(prevInfo.getAPP_DELIVERY_ST_NM())) {
//                // 건수인정 하지않음
//                sRet = "N";
//            } else {
//                // 배송전이 아닐경우(정상배송-정시/조기/지연, 미배송, 배송취소)
//
//                if ("미배송".equals(prevInfo.getAPP_DELIVERY_ST_NM())) {
//                    // 건수인정 하지않음
//                    sRet = "N";
//                } else if ("배송취소".equals(prevInfo.getAPP_DELIVERY_ST_NM())) {
//                    // 건수인정 하지않음
//                    sRet = "N";
//                } else {
//                    // 건수인정 함. 정상배송-정시/조기/지연 의 경우..
//                    sRet = "Y";
//                }
//            }
//        }
        return sRet;
    }

}
