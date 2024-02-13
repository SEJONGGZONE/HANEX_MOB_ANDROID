package com.gzonesoft.sg623.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.eksys.gis.sgmobile.co.Coord;
//import com.eksys.gis.streamingmap.observer.LocationSPref;
//import com.eksys.gis.streamingmap.observer.PathInfo;
//import com.eksys.gis.streamingmap.util.SGMConfig;
//import com.eksys.gis.streamingmap.util.StateDefine;
//import com.eksys.gis.talmap.TMMapPos;
import androidx.recyclerview.widget.RecyclerView;

//import com.gzonesoft.cookzzang.DeliveryItemActivity;
//import com.gzonesoft.cookzzang.DeliveryListActivity;
import com.gzonesoft.sg623.R;
//import com.gzonesoft.cookzzang.Requester;
//import com.gzonesoft.cookzzang.RequesterSession;
//import com.gzonesoft.cookzzang.SendSmsAct;
//import com.gzonesoft.cookzzang.data.DeliveryListInfo;
//import com.gzonesoft.cookzzang.model.DeliveryListManager;
//import com.gzonesoft.cookzzang.util.Common;
import com.gzonesoft.sg623.comm.AppSetting;
import com.gzonesoft.sg623.comm.RequesterSession;
import com.gzonesoft.sg623.data.DeliveryListInfo;
import com.gzonesoft.sg623.ui.DeliveryListActivity;
import com.gzonesoft.sg623.util.Common;
import com.gzonesoft.sg623.util.CommonUtil;
import com.gzonesoft.sg623.util.Loggers;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 배송리스트(업무메인) 리스트 아이템 아답터
 */
public class DeliveryListItemAdapter extends RecyclerView.Adapter<DeliveryListItemAdapter.ViewHolder> {

    public View OldView = null;
    public int prePosition = -1;

    // 점포 오류보고 후 메시지
    private final int UPDATE_LOCATION = 0x0000001;
//    private final int DELIVERY_CANCEL = 0x0000002;
//    private final int DELIVERY_CANCEL_OK = 0x1000003;
//    private final int DELIVERY_CANCEL_FAIL = 0x1000004;

    public ArrayList<DeliveryListInfo> dataset;

    private Context mContext;

    private View.OnClickListener onItemViewClickListener;

    public int curPosition = -1;

    // 애니메이션 효과
    Animation animationStart = null;
    Animation animationStop = null;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener {

        TextView route_index;
        TextView sord_no;
        TextView receptname_txt;
        TextView err_receptname_txt;
        LinearLayout err_position_ll;
        TextView request_time;
        TextView delivery_time;
        TextView plan_time;
        TextView delivery_type_nm;
        TextView delivery_st_nm;

        ImageView done_image;
        View done_image_space;

        ImageView combine_image;
        View combine_space;

        View foot_dummy;

        TextView delivery_address;

        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        LinearLayout area_item_text;
        LinearLayout imageView4_ll;

        ImageView imageView4;
        ImageView imageView5;
        ImageView imageView6;

        TextView textView3;


        public int selectedIndex = -1;

        private View mView;

        public ViewHolder(View v) {
            super(v);
            Loggers.d("--- [ViewHolder]처리 1");

            route_index = (TextView) v.findViewById(R.id.route_index);
            sord_no = (TextView) v.findViewById(R.id.sord_no);
            receptname_txt = (TextView) v.findViewById(R.id.receptname_txt);
            err_receptname_txt = (TextView) v.findViewById(R.id.err_receptname_txt);
            err_position_ll = (LinearLayout) v.findViewById(R.id.err_position_ll);
            request_time = (TextView) v.findViewById(R.id.request_time);
            delivery_time = (TextView) v.findViewById(R.id.delivery_time);
            plan_time = (TextView) v.findViewById(R.id.plan_time);
            delivery_type_nm = (TextView) v.findViewById(R.id.delivery_type_nm);
            delivery_st_nm = (TextView) v.findViewById(R.id.delivery_st_nm);
            done_image = (ImageView) v.findViewById(R.id.done_image);
            done_image_space = (View) v.findViewById(R.id.done_image_space);
            combine_image = (ImageView) v.findViewById(R.id.combine_image);
            combine_space = (View) v.findViewById(R.id.combine_space);
            foot_dummy = (View) v.findViewById(R.id.foot_dummy);
//            delivery_address = (TextView) v.findViewById(R.id.delivery_address);
            imageView1 = (ImageView) v.findViewById(R.id.imageView1);
            imageView2 = (ImageView) v.findViewById(R.id.imageView2);
            imageView3 = (ImageView) v.findViewById(R.id.imageView3);
            area_item_text = (LinearLayout) v.findViewById(R.id.area_item_text);
            imageView4_ll = (LinearLayout) v.findViewById(R.id.imageView4_ll);
            imageView4 = (ImageView) v.findViewById(R.id.imageView4);
            imageView5 = (ImageView) v.findViewById(R.id.imageView5);
            imageView6 = (ImageView) v.findViewById(R.id.imageView6);

            textView3 = (TextView) v.findViewById(R.id.textView3);
            Loggers.d("--- [ViewHolder]처리 2");
        }

        @Override
        public boolean onLongClick(View view) {
//            Log.d("ViewHolder", "[onLongClick]----------------");
            view.setBackgroundColor(Color.parseColor("#6A6160"));
            return true;
        }

        @Override
        public void onClick(View view) {
            Log.d("ViewHolder", "[onClick]---------------- 888 ");
        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
//            Log.d("ViewHolder", "[onDrag]----------------");
            return true;
        }
    }


    /**
     * 리스트 아답타 생성자
     * @param context
     * @param myDataset
     */
    public DeliveryListItemAdapter(Context context, ArrayList<DeliveryListInfo> myDataset) {
        try {
            Loggers.d("--- [DeliveryListItemAdapter]처리 1");

            dataset = myDataset;
            this.mContext = context;

            // 애니메이션리소스 매핑
            animationStart = AnimationUtils.loadAnimation(this.mContext, R.anim.animation_start_speed);
            animationStop = AnimationUtils.loadAnimation(this.mContext, R.anim.animation_stop);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 리스트 아이템 클릭리스너
     * @param onItemViewClickListener
     */
    public void setOnItemViewClickListener(View.OnClickListener onItemViewClickListener) {
        Loggers.d("--- [setOnItemViewClickListener]처리 1");
        this.onItemViewClickListener = onItemViewClickListener;
    }

    /**
     * 새로운 ViewHolder  작성
      * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Loggers.d("--- [onCreateViewHolder]처리 1");

        // 새로 View를 만든다
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deliverylistitem, parent, false);

        // View에 클릭 리스너를 붙인다
        if (onItemViewClickListener != null) {
            v.setOnClickListener(onItemViewClickListener);
        }
        // 데이터와 관련이 없는 레이아웃 조정은 여기서 한다(여기서 만든 레이아웃을 돌려쓰기 위해)
        ViewHolder vh = new ViewHolder(v);

        //Loggers.d(("---------------------------------- onCreateViewHolder ----------------------------------"));


        return vh;
    }

    public int nCnt = 0;
    public void UpdateLocationInfo() {
        try {
            Loggers.d("--- [UpdateLocationInfo]처리 1");

            if (tvDistInfo != null) {
                double curLat = AppSetting.GPS_MANAGER.getGpsLat();
                double curLon = AppSetting.GPS_MANAGER.getGpsLng();

                String htmlString = "";
                //htmlString = "<B>" + info.getDISTANCE_ME() + "</B> <small>주변</small>" ;
                htmlString = String.format("%d", nCnt);
                CommonUtil.with().setHtmlMarqueeText(tvDistInfo, htmlString);
                nCnt++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * 리스트 아이텝 View 안의 데이터 변경
      * @param holder
     * @param position
     */
    public TextView tvDistInfo;
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            // Loggers.d("--- [onBindViewHolder]진입. position=" + position);

            // 현재 인덱스 저장
            curPosition = position;

            int curItemIndex = ((DeliveryListActivity) mContext).curItemIndex;

            if ((dataset.size() < position) || (dataset.size() == 0)) {
                return;
            }
            // 순서확정여부 확인..
            boolean checkRouteYn = ((DeliveryListActivity) DeliveryListActivity.mContext).checkRouteYn();

            DeliveryListInfo info = dataset.get(position);
            String htmlString = "";
            // --------------------------------------------------------------------------------
            // 배차순번..
            // --------------------------------------------------------------------------------
            TextView tvRouteIdx = (TextView) holder.itemView.findViewById(R.id.tvRouteIdx);
            //tvRouteIdx.setText(info.getROUTE_IDX());
            tvRouteIdx.setText(String.format("%d", info.getIdx() + 1));
            // --------------------------------------------------------------------------------
            // 거래처명
            // --------------------------------------------------------------------------------
            TextView tvClname = (TextView) holder.itemView.findViewById(R.id.tvClname);
            //tvClname.setText(info.getCLNAME());
            if (checkRouteYn) { // 확정후
                htmlString = "" +
                        "&nbsp;" +
                        "" + info.getCLNAME() + " - <small>" + info.getORDER_AMT() + " <small>원</small></small>" +
                        " <small>- " + info.getCLCEO() + "" +
                        "";
            } else { // 확정전
                htmlString = "" +
                        "&nbsp;" +
                        //"<small>[" + info.getEPNAME() + "]</small>"+
                        "" + info.getCLNAME() + " - <small>" + info.getORDER_AMT() + " <small>원</small></small>" +
                        //" <small>- " + info.getCLCEO() + ", " +
                        //" - " + info.getDISTANCE_ME() + "</small>" +
                        "";
            }
            CommonUtil.with().setHtmlMarqueeText(tvClname, htmlString);
            // --------------------------------------------------------------------------------
            // 주소정보
            // --------------------------------------------------------------------------------
            TextView tvItemInfo = (TextView) holder.itemView.findViewById(R.id.tvItemInfo);
            htmlString = "<small>" + info.getADDRESS() + "</small>";
            CommonUtil.with().setHtmlMarqueeText(tvItemInfo, htmlString);

            // --------------------------------------------------------------------------------
            //  도착시간
            // --------------------------------------------------------------------------------
            TextView tvExpInfo = (TextView) holder.itemView.findViewById(R.id.tvExpInfo);
            if (checkRouteYn) { // 확정후
                if (info.getINBOUND_DTM().length() > 0) {
                    // 도착시간정보 표시
                    htmlString = "<B>" + info.getINBOUND_DTM().substring(11, 16) + "</B> <small>도착</small>";
                    tvExpInfo.setBackgroundColor(Color.parseColor("#0072C3"));
                    tvExpInfo.setTextColor(Color.parseColor("#FFFFFF"));
                    // 구분선지우기
                    View viewDivide = (View) holder.itemView.findViewById(R.id.viewDivide);
                    viewDivide.setVisibility(View.GONE);
                } else if (info.getEXPECT_TIME().length() > 0) {
                    htmlString = "<B>" + info.getEXPECT_TIME().substring(11, 16) + "</B> <small>도착예정</small>";
                    tvExpInfo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    tvExpInfo.setTextColor(Color.parseColor("#0072C3"));
                } else {
                    htmlString = "- 도착시간 미정 - ";
                    tvExpInfo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    tvExpInfo.setTextColor(Color.parseColor("#0072C3"));
                }
            } else {
                htmlString = "<B>- 출발대기 -</B>";
                tvExpInfo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tvExpInfo.setTextColor(Color.parseColor("#929292"));
            }
            CommonUtil.with().setHtmlMarqueeText(tvExpInfo, htmlString);
            // --------------------------------------------------------------------------------
            // 거리정보
            // --------------------------------------------------------------------------------
            double distance = 0;
            try {
                double curLat = AppSetting.GPS_MANAGER.getGpsLat();
                double curLon = AppSetting.GPS_MANAGER.getGpsLng();
                double targetLat = Double.parseDouble(info.getLAT());
                double targetLon = Double.parseDouble(info.getLON());
                distance = CommonUtil.with().distance(curLat, curLon, targetLat, targetLon);
            } catch (Exception ex) {
                distance = -1;
                ex.printStackTrace();
            }

            tvDistInfo = (TextView) holder.itemView.findViewById(R.id.tvDistInfo);
            //htmlString = "<B>" + info.getDISTANCE_ME() + "</B> <small>주변</small>" ;
            if (distance <= 0) {
                htmlString = "- <B>위치 미등록</B> -";
            } else {
                htmlString = "<B>" + CommonUtil.with().distanceStringOnlyValue(distance / 1000) + "</B>km <small>주변</small>";
            }
            CommonUtil.with().setHtmlMarqueeText(tvDistInfo, htmlString);
            // 200미터이내의 경우...
            if (distance < 200) {
                tvDistInfo.setBackgroundColor(Color.parseColor("#E14D00"));
                tvDistInfo.setTextColor(Color.parseColor("#FFFFFF"));
                tvDistInfo.startAnimation(animationStart);
            } else {
                tvDistInfo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tvDistInfo.setTextColor(Color.parseColor("#0072C3"));
                tvDistInfo.startAnimation(animationStop);
            }


            // 리스트 선택/미선택 상태 보여주기..
            if (position != curItemIndex) {
                holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                // 버튼 감추기.
                LinearLayout prev_item_button_area = (LinearLayout) holder.itemView.findViewById(R.id.add_action_area);
                prev_item_button_area.setVisibility(View.GONE);
                // 하위 텍스트 일반으로 일괄처리..
                Common.setTextTypefaceInView(holder.itemView, R.id.area_item_text, Typeface.NORMAL);
                Common.setTextTypefaceInView(holder.itemView, R.id.delivery_status, Typeface.NORMAL);
                //            // 주소배경처리
                //            LinearLayout prev_delivery_addinfo_area = (LinearLayout) holder.itemView.findViewById(R.id.ll_listitem);
                //            prev_delivery_addinfo_area.setBackgroundColor(Color.parseColor("#F7F7F7"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                // 버튼 보이기
                LinearLayout item_button_area = (LinearLayout) holder.itemView.findViewById(R.id.add_action_area);
                item_button_area.setVisibility(View.VISIBLE);
                // 하위 텍스트 볼드처리..
                Common.setTextTypefaceInView(holder.itemView, R.id.area_item_text, Typeface.BOLD);
                Common.setTextTypefaceInView(holder.itemView, R.id.delivery_status, Typeface.BOLD);
                //            // 주소배경처리
                //            LinearLayout delivery_addinfo_area = (LinearLayout) holder.itemView.findViewById(R.id.ll_listitem);
                //            delivery_addinfo_area.setBackgroundColor(Color.parseColor("#FFFFFF"));

                OldView = holder.itemView;
            }


            // 순서확정여부 확인..테마색상변경..
            String colorString = "";
            if (checkRouteYn) { // 확정후
                colorString = "#0072C3";
                tvRouteIdx.setBackgroundColor(Color.parseColor(colorString));
            } else { // 확정전
                colorString = "#111212";
                tvRouteIdx.setBackgroundColor(Color.parseColor("#E14D00"));
            }
            tvClname.setTextColor(Color.parseColor(colorString));
            tvItemInfo.setTextColor(Color.parseColor(colorString));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 데이터 카운트 반환
     * @return
     */
    @Override
    public int getItemCount() {
        return dataset.size();
    }


    /**
     * 아이템 이동
     * @param fromPosition
     * @param toPosition
     * @return
     */
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * 아이템 삭제
     * @param position 리스트 위치
     */
    public void removeAtPosition(int position) {
        if (position < dataset.size()) {
            // 데이터를 삭제한다
            //dataset.remove(position);
            // 삭제했다고 Adapter 알린다
            //notifyItemRemoved(position);
        }
    }



    /**
     * 서버요청 결과 처리 핸들러..
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                // -------------------------------------------------------------------------------------------------------
                // 화면갱신 핸들러
                // -------------------------------------------------------------------------------------------------------
                case UPDATE_LOCATION:   // 위치정보값 업데이트
                    // 위치정보 업데이트 메소드 호출..
                    UpdateLocationInfo();
                    break;

//                case MSG_CUSTCOORD_ERROR:   // 배송지 위치오류보고 콜백처리후...
//                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
//                    // 배송 목록 재조회
//                    ((DeliveryListActivity) mContext).searchDeliveryMainList();
//                    break;
//                case DELIVERY_CANCEL_OK:
//                    CommonUtil.with().ToastMsg(mContext, "배송취소 보고 완료", Toast.LENGTH_LONG).show();
//                    // 배송 목록 재조회
//                    ((DeliveryListActivity) mContext).searchDeliveryMainList();
//                    break;
//                case DELIVERY_CANCEL_FAIL:
//                    CommonUtil.with().ToastMsg(mContext, "배송취소 보고 실패", Toast.LENGTH_LONG).show();
//                    // 배송 목록 재조회
//                    ((DeliveryListActivity) mContext).searchDeliveryMainList();
//                    break;
                // -------------------------------------------------------------------------------------------------------
                // 공통 핸들러 처리
                // -------------------------------------------------------------------------------------------------------
                case RequesterSession.REQ_ERR_NOT_RESPONSE:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case RequesterSession.REQ_NOK:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case RequesterSession.REQ_OK_MESSAGE:
                    CommonUtil.with().ToastMsg(mContext, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case RequesterSession.REQ_OK:
                    break;
            }
//            mProgress.setVisibility(View.GONE);
        }
    };



}