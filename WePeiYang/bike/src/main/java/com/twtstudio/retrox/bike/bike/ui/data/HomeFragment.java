package com.twtstudio.retrox.bike.bike.ui.data;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.bike.bikeAuth.BikeAuthActivity;
import com.twtstudio.retrox.bike.bike.ui.record.RecordActivity;
import com.twtstudio.retrox.bike.common.ui.PFragment;
import com.twtstudio.retrox.bike.model.BikeUserInfo;
import com.twtstudio.retrox.bike.utils.BikeStationUtils;
import com.twtstudio.retrox.bike.utils.TimeStampUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jcy on 2016/8/9.
 */

public class HomeFragment extends PFragment<HomePresenter> implements HomeViewController {
    @BindView(R2.id.srl_home)
    SwipeRefreshLayout mSrlHome;
    //自行车卡片
    @BindView(R2.id.leave_station)
    TextView mLeaveStation;
    @BindView(R2.id.leave_time)
    TextView mLeaveTime;
    @BindView(R2.id.arr_station)
    TextView mArrStation;
    @BindView(R2.id.arr_time)
    TextView mArrTime;
    @BindView(R2.id.bike_fee)
    TextView mBikeFeeText;
    @BindView(R2.id.bike_data_chart)
    LineChart mLineChart;
    @BindView(R2.id.bike_user_name)
    TextView mNameText;
    @BindView(R2.id.bike_user_balance)
    TextView mBalanceText;
    @BindView(R2.id.bike_query_record)
    LinearLayout mQueryRecordLayout;

    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<>();
    ArrayList<Entry> yVals = new ArrayList<>();
    private LineDataSet mSet;

    private List<List<String>> mRecentData;
    private static final String TAG = "HomeFragment";

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter(getContext(), this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_bike_data;
    }

    @Override
    protected void preInitView() {
        super.preInitView();
        mSrlHome.setColorSchemeResources(R.color.colorPrimary,R.color.assist_color_1,R.color.assist_color_2);
        mSrlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getBikeUserInfo();
            }
        });
        mSrlHome.post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getBikeUserInfo();
            }
        });
        //mPresenter.getBikeUserInfo();
    }

    @Override
    protected void initView() {
        //Picasso.with(getContext()).load(R.drawable.bulr_2).into(mBackImage);
        mQueryRecordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void setBikeUserInfo(BikeUserInfo bikeUserInfo)  {
        mSrlHome.setRefreshing(false);

        if (bikeUserInfo.status == 0) {
            Intent intent = new Intent(getActivity(), BikeAuthActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        if (bikeUserInfo.record != null) {
            //mSrlHome.setRefreshing(false);
            String dep = BikeStationUtils.getInstance().queryId(bikeUserInfo.record.dep).name;
            mLeaveStation.setText(dep + "-" + bikeUserInfo.record.dep_dev + "号桩 取出");
            mLeaveTime.setText(TimeStampUtils.getDateString(bikeUserInfo.record.dep_time));
            String arr = BikeStationUtils.getInstance().queryId(bikeUserInfo.record.arr).name;
            if (arr.equals("no data")) {
                mArrStation.setText("点位名无法查询");
                mArrTime.setText(TimeStampUtils.getDateString(bikeUserInfo.record.arr_time));
                mBikeFeeText.setText("本次消费: " + bikeUserInfo.record.fee);

            } else {
                mArrStation.setText(arr + "-" + bikeUserInfo.record.arr_dev + "号桩 还入");
                mArrTime.setText(TimeStampUtils.getDateString(bikeUserInfo.record.arr_time));
                mBikeFeeText.setText("本次消费: " + bikeUserInfo.record.fee);
            }
            mNameText.setText("姓名: " + bikeUserInfo.name);
            mBalanceText.setText("余额: " + bikeUserInfo.balance);


            mLineChart.setLogEnabled(false);
            mLineChart.setNoDataText("最近没有骑行数据");
            mLineChart.setTouchEnabled(false);
            mLineChart.setDrawGridBackground(false);
            mLineChart.setDragEnabled(false);
            mLineChart.setDragEnabled(false);
            mLineChart.animateX(100);
            mLineChart.setDrawBorders(false);
            mLineChart.getDescription().setEnabled(false);
            mLineChart.setDoubleTapToZoomEnabled(false);
            mLineChart.setPinchZoom(false);
            mLineChart.setAutoScaleMinMaxEnabled(true);
            //mLineChart.setGridBackgroundColor(Color.BLACK);
            mLineChart.setBorderWidth(3);
//            int[] colors = mLineChart.getLegend().getColors();
//            int[] color = {Color.rgb(145,145,145)};
//            String[] names = {"最近一周骑行时间 (min)"};
            mLineChart.getLegend().setTextColor(Color.rgb(145,145,145));
//            mLineChart.getLegend().setTextColor();
            mLineChart.getLegend().setEnabled(true);
            mLineChart.animateY(1500, Easing.EasingOption.EaseInExpo);

            xVals.clear();
            yVals.clear();

            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.text_secondary_color));
            xAxis.setDrawAxisLine(true);
            xAxis.setAxisLineColor(Color.rgb(39, 173, 97));
            xAxis.setAxisLineWidth(1f);
            xAxis.setDrawLabels(true);
            xAxis.setTextSize(10);
//            xAxis.setTextColor(Color.BLACK);
            YAxis yAxis = mLineChart.getAxisLeft();
            yAxis.setAxisMaxValue(4f);
            yAxis.setStartAtZero(false);
            yAxis.setDrawAxisLine(false);
            yAxis.setDrawGridLines(false);
            yAxis.setDrawLabels(false);
            YAxis rightAxis = mLineChart.getAxisRight();
            rightAxis.setDrawLabels(false);
            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawGridLines(false);

            mRecentData = bikeUserInfo.recent;
            float max = 0;
            float min = 0;
            for (int i = 0; i < mRecentData.size(); i++) {
                List<String> item = mRecentData.get(i);
                Integer val = Integer.valueOf(item.get(1));
                if (val > max) {
                    max = val;
                }
                if (val < min) {
                    min = val;
                }
                xVals.add(String.valueOf(item.get(0)));
                yVals.add(new Entry(i, val));
                //colors.add(ContextCompat.getColor(getContext(), R.color.gpa_primary_color));
            }
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return xVals.get((int)value);
                }
            });
            yAxis.setAxisMaxValue(max);
            yAxis.setAxisMinValue(min);

            mSet = new LineDataSet(yVals, "最近一周骑行时间 (min)");
//            mSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            mSet.setCircleColor(Color.rgb(39, 173, 97));
            mSet.setColor(ContextCompat.getColor(getContext(), R.color.text_secondary_color));
            mSet.setValueTextColor(Color.rgb(145, 145, 145));
            mSet.setValueTextSize(10f);
            mSet.setLineWidth(1f);
            mSet.setValueFormatter(new MyValueFormatter());
            LineData data = new LineData(mSet);
            mLineChart.setData(data);
            mLineChart.setExtraLeftOffset(15);
            mLineChart.setExtraRightOffset(20);
            mLineChart.setExtraTopOffset(30);
            mLineChart.setExtraBottomOffset(15);
        }
    }

    static class MyValueFormatter implements IValueFormatter {


        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            int intValue = (int) value;
            if (intValue == 0) {
                return 0 + "";
            }
            int more = intValue % 60;
            int min = (intValue - more) / 60;
            if (min > 0) {
                return min + "'" + more + "\"";
            } else {
                return more + "\"";
            }        }
    }
}
