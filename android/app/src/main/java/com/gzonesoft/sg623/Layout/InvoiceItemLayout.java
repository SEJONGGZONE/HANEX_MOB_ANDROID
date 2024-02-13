package com.gzonesoft.sg623.Layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzonesoft.sg623.R;
import com.gzonesoft.sg623.data.DeliveryItemInfo;
import com.gzonesoft.sg623.util.CommonUtil;
import com.ssomai.android.scalablelayout.ScalableLayout;


public class InvoiceItemLayout extends LinearLayout {

    public static Context mContext = null;
    public boolean bLastYn = false;

    private DeliveryItemInfo mCurInfo = null;

    private ScalableLayout scFooter;
    private TextView tvInfoText1, tvInfoText2, tvInfoText3, tvInfoText4;
    private TextView tvSumInfoText1, tvSumInfoText2, tvSumInfoText3, tvSumInfoText4;

    public InvoiceItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public InvoiceItemLayout(Context context, DeliveryItemInfo info) {
        super(context);

        mContext = context;
        mCurInfo = info;

        init(context);


        setData();
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_invoice_item, this, true);

        tvInfoText1 = (TextView) findViewById(R.id.tvInfoText1);
        tvInfoText2 = (TextView) findViewById(R.id.tvInfoText2);
        tvInfoText3 = (TextView) findViewById(R.id.tvInfoText3);
        tvInfoText4 = (TextView) findViewById(R.id.tvInfoText4);

        scFooter = (ScalableLayout) findViewById(R.id.scFooter);
        tvSumInfoText1 = (TextView) findViewById(R.id.tvSumInfoText1);
        tvSumInfoText2 = (TextView) findViewById(R.id.tvSumInfoText2);
        tvSumInfoText3 = (TextView) findViewById(R.id.tvSumInfoText3);
        tvSumInfoText4 = (TextView) findViewById(R.id.tvSumInfoText4);
    }

    private void setData() {

        try {
            if (mCurInfo != null) {
                String htmlString = "";

                htmlString = mCurInfo.getITNAME();
                CommonUtil.with().setHtmlMarqueeText(tvInfoText1, htmlString);

                htmlString = mCurInfo.getWSD_QTY();
                CommonUtil.with().setHtmlMarqueeText(tvInfoText2, htmlString);

                htmlString = mCurInfo.getWSD_DAN();
                CommonUtil.with().setHtmlMarqueeText(tvInfoText3, htmlString);

                htmlString = mCurInfo.getWSD_TOT();
                CommonUtil.with().setHtmlMarqueeText(tvInfoText4, htmlString);

                if (mCurInfo.isbLastYn()) {
                    scFooter.setVisibility(VISIBLE);
                    tvSumInfoText1.setText(mCurInfo.getTOT_QTY());
                    tvSumInfoText2.setText("");
                    tvSumInfoText3.setText(mCurInfo.getTOT_AMOUNT());

                    CommonUtil.with().setHtmlText(tvSumInfoText4, mCurInfo.getFOOTER_INFO());

                } else {
                    scFooter.setVisibility(GONE);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
