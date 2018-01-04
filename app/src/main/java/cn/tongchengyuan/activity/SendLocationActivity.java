package cn.tongchengyuan.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import cn.tongchengyuan.util.LogUtil;
import com.same.city.love.R;
import com.same.city.love.databinding.ActivitySendLocationBinding;
import com.style.base.BaseToolbarBtnActivity;

import java.util.List;

public class SendLocationActivity extends BaseToolbarBtnActivity implements OnGetGeoCoderResultListener {
    ActivitySendLocationBinding bd;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;
    private static final int paddingLeft = 0;
    private static final int paddingTop = 0;
    private static final int paddingRight = 0;
    private static final int paddingBottom = 200;
    TextView mTextView;

    private LocationClient mLocClient;
    private MyLocationListener myListener = new MyLocationListener();
    private boolean isFirstLoc = true;
    private MarkerOptions markOps;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private Point mCenterPoint;
    private ReverseGeoCodeResult targetLocation;
    private boolean isSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_send_location);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("选择位置");
        getToolbarRightView().setText("发送");
        Intent intent = getIntent();
        isSelect = intent.getBooleanExtra("select", true);
        if (!isSelect)
            getToolbarRightView().setVisibility(View.GONE);
        mBaiduMap = bd.mapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LogUtil.i("X");
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));
            }
        });
       /* mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                MapStatus mapstatus = mBaiduMap.getMapStatus();
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mBaiduMap.getProjection().fromScreenLocation(mCenterPoint)));
*//*
                LogUtil.i("loaction: " + mapstatus.target.latitude + "," + mapstatus.target.longitude);
                LogUtil.i("screen: " + mapstatus.targetScreen.x + "," + mapstatus.targetScreen.y);*//*
            }
        });*/

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        if (!isSelect) {
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);
            String address = intent.getStringExtra("address");
            moveMapViewToMarkerByLocation(latitude, longitude);
        }

        initLocation();
    }

    private void initLocation() {
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        Log.e(TAG, "onGetGeoCodeResult");
        LatLng latLng = geoCodeResult.getLocation();
        Log.e(TAG, "经纬度:" + latLng.latitude + "--" + latLng.longitude);
        Log.e(TAG, "address:" + geoCodeResult.getAddress());
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        targetLocation = reverseGeoCodeResult;
        LatLng latLng = reverseGeoCodeResult.getLocation();
        Log.e(TAG, "经纬度:" + latLng.latitude + "--" + latLng.longitude);
        String address = reverseGeoCodeResult.getAddress();
        LogUtil.e("address: " + address);
        LogUtil.e("getSematicDescription: " + reverseGeoCodeResult.getSematicDescription());
        LogUtil.e("getBusinessCircle: " + reverseGeoCodeResult.getBusinessCircle());
        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
        for (PoiInfo poiInfo : poiInfos) {
            LogUtil.i("附近:" + poiInfo.address);
        }
    }

    @Override
    protected void onClickTitleRightView() {
        if (targetLocation != null) {
            LatLng latLng = targetLocation.getLocation();
            Intent intent = getIntent();
            intent.putExtra("latitude", latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
            intent.putExtra("address", targetLocation.getAddress());
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || bd.mapView == null) {
                return;
            }
            Log.d(TAG, "经纬度:" + location.getLatitude() + "--" + location.getLongitude());
            Log.d(TAG, "address:" + location.getAddrStr());

            if (isFirstLoc && isSelect) {//选择位置时才自动定位
                isFirstLoc = false;
                moveMapViewToMarkerByLocation(location.getLatitude(), location.getLongitude());
            }
        }

    }

    private void moveMapViewToMarkerByLocation(double latitude, double longitude) {
        MyLocationData locData = new MyLocationData.Builder()
                //.accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latitude).longitude(longitude).build();
        mBaiduMap.setMyLocationData(locData);
        LatLng ll = new LatLng(latitude, longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);

        // 初始化当前MapView中心屏幕坐标，初始化当前地理坐标
        mCenterPoint = mBaiduMap.getMapStatus().targetScreen;
        mCenterPoint.y = (int) (bd.mapView.getY() + bd.mapView.getMeasuredHeight() / 2);
        LogUtil.e("X: " + mCenterPoint.x + ", Y: " + mCenterPoint.y);
        builder.targetScreen(mCenterPoint);
        int w = bd.ivMarker.getMeasuredWidth();
        int h = bd.ivMarker.getMeasuredHeight();
        int x = (int) (mCenterPoint.x - w * 0.5);
        int y = (int) (mCenterPoint.y - h);
        bd.ivMarker.setLeft(x);
        bd.ivMarker.setTop(y);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.animateMapStatus(mapStatusUpdate);
    }

    /**
     * 是否启用缩放手势
     *
     * @param v
     */
    public void setZoomEnable(View v) {
        mUiSettings.setZoomGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用平移手势
     *
     * @param v
     */
    public void setScrollEnable(View v) {
        mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用旋转手势
     *
     * @param v
     */
    public void setRotateEnable(View v) {
        mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用俯视手势
     *
     * @param v
     */
    public void setOverlookEnable(View v) {
        mUiSettings.setOverlookingGesturesEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否启用指南针图层
     *
     * @param v
     */
    public void setCompassEnable(View v) {
        mUiSettings.setCompassEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 是否显示底图默认标注
     *
     * @param v
     */
    public void setMapPoiEnable(View v) {
        mBaiduMap.showMapPoi(((CheckBox) v).isChecked());
    }

    /**
     * 是否禁用所有手势
     *
     * @param v
     */
    public void setAllGestureEnable(View v) {
        mUiSettings.setAllGesturesEnabled(!((CheckBox) v).isChecked());
    }

    /**
     * 设置Padding区域
     *
     * @param v
     */
    public void setPadding(View v) {
        if (((CheckBox) v).isChecked()) {
            mBaiduMap.setViewPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            addView(bd.mapView);
        } else {
            mBaiduMap.setViewPadding(0, 0, 0, 0);
            bd.mapView.removeView(mTextView);
        }
    }

    private void addView(MapView mapView) {
        mTextView = new TextView(this);
        mTextView.setText(getText(R.string.instruction));
        mTextView.setTextSize(15.0f);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.BLACK);
        mTextView.setBackgroundColor(Color.parseColor("#AA00FF00"));

        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
        builder.width(mapView.getWidth());
        builder.height(paddingBottom);
        builder.point(new Point(0, mapView.getHeight()));
        builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);

        mapView.addView(mTextView, builder.build());

    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        bd.mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        bd.mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        bd.mapView.onDestroy();
        super.onDestroy();
    }
}
