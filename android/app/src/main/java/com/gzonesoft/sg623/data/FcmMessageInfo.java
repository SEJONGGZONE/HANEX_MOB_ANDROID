package com.gzonesoft.sg623.data;


import com.gzonesoft.sg623.util.StringUtil;

/**
 * FCM 메시지 관리
 */
public class FcmMessageInfo {

//
//
//    "fcmType": "199",
//    "title": "콜트럭-배차",
//    "body": "신기사차주님, 배차정보가 취소되었습니다.",
//    "btnTitle1": "",
//    "btnTitle2": "",
//    "notiType": "0",
//    "notiSoundUrl": "calltruck_cancel_dispatch_info",
//    "fullScreenYn": "Y",
//    "showTime": "5"
//
//    -- 2020.06.06 신규
//    {
//        "google.original_priority": "high",
//            "google.sent_time": "1591672568504",
//            "google.delivered_priority": "high",
//            "gcm.notification.title": "콜트럭",
//            "btnTitle1": "확인",
//            "google.c.sender.id": "1097782698144",
//            "btnTitle2": "취소",
//            "body": "대구_달서구&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<BR><small><small>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;→<\/small><\/small>_서울용산구<BR><BR><big><b>250,000<\/b><\/big><small>원<\/small><br><small><small>선착불_-_(수)_10,000원<\/small><\/small><br><br><small>당일상(지)_\/_내일착(지)_\/_5톤_\/_카고축_<\/small><BR><BR>10<small><small>초_뒤_자동취소됩니다.<\/small><\/small>",
//            "type": "배차협의",
//            "title": "배차협의",
//            "google.message_id": "0:1591672568563515%4609f91f4609f91f",
//            "collapse_key": "calltruck.trucker",
//            "bgColor": "486DC1",
//            "gcm.notification.e": "1",
//            "google.c.a.e": "1",
//            "google.ttl": "2419200",
//            "txtColor": "FFFFFF",
//            "from": "1097782698144",
//            "gcm.notification.body": "배차문의(협의)가 수신되었습니다."
//    }
//
//        return StringUtil.getNvlStr(msg_cd).toString();

    String fcmType;
    String title;
    String body;
    String btnTitle1;
    String btnTitle2;
    String notiType;
    String notiSoundUrl;
    String fullScreenYn;
    String showTime;
    String refcode1;
    String refcode2;
    String refcode3;

    public String getFcmType() {
        return StringUtil.getNvlStr(fcmType).toString();
    }

    public void setFcmType(String fcmType) {
        this.fcmType = fcmType;
    }

    public String getTitle() {
        return StringUtil.getNvlStr(title).toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return StringUtil.getNvlStr(body).toString();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBtnTitle1() {
        return StringUtil.getNvlStr(btnTitle1).toString();
    }

    public void setBtnTitle1(String btnTitle1) {
        this.btnTitle1 = btnTitle1;
    }

    public String getBtnTitle2() {
        return StringUtil.getNvlStr(btnTitle2).toString();
    }

    public void setBtnTitle2(String btnTitle2) {
        this.btnTitle2 = btnTitle2;
    }

    public String getNotiType() {
        return StringUtil.getNvlStr(notiType).toString();
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public String getNotiSoundUrl() {
        return StringUtil.getNvlStr(notiSoundUrl).toString();
    }

    public void setNotiSoundUrl(String notiSoundUrl) {
        this.notiSoundUrl = notiSoundUrl;
    }

    public String getFullScreenYn() {
        return StringUtil.getNvlStr(fullScreenYn).toString();
    }

    public void setFullScreenYn(String fullScreenYn) {
        this.fullScreenYn = fullScreenYn;
    }

    public String getShowTime() {
        return StringUtil.getNvlStr(showTime).toString();
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getRefcode1() {
        return StringUtil.getNvlStr(refcode1).toString();
    }

    public void setRefcode1(String refcode1) {
        this.refcode1 = refcode1;
    }

    public String getRefcode2() {
        return StringUtil.getNvlStr(refcode2).toString();
    }

    public void setRefcode2(String refcode2) {
        this.refcode2 = refcode2;
    }

    public String getRefcode3() {
        return StringUtil.getNvlStr(refcode3).toString();
    }

    public void setRefcode3(String refcode3) {
        this.refcode3 = refcode3;
    }

    @Override
    public String toString() {
        return "FcmMessageInfo{" +
                "fcmType='" + fcmType + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", btnTitle1='" + btnTitle1 + '\'' +
                ", btnTitle2='" + btnTitle2 + '\'' +
                ", notiType='" + notiType + '\'' +
                ", notiSoundUrl='" + notiSoundUrl + '\'' +
                ", fullScreenYn='" + fullScreenYn + '\'' +
                ", showTime='" + showTime + '\'' +
                ", refcode1='" + refcode1 + '\'' +
                ", refcode2='" + refcode2 + '\'' +
                ", refcode3='" + refcode3 + '\'' +
                '}';
    }
}
