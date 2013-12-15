package com.example.find;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConvert;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.myfirstandroid.R;

public class McNearbyActivity extends Activity {
	private BMapManager mcMapMan = null;
	private MapView mcMapView = null;
	private static double latitude;
	private static double longitude;
	MKSearch mMKSearch = null;
	GeoPoint point = null;
	GeoPoint converted_point = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mcMapMan = new BMapManager(getApplication());
		mcMapMan.init("nxkTmMiVH1fLQvaXsYeP6rsq", null);
		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错

		setContentView(R.layout.activity_map_mc_nearby);
		mcMapView = (MapView) findViewById(R.id.bmapsView);

		// 定位
		get_user_loc();
		// 展示地图
		show_map();
		// 在地图上标明我的位置
		show_user_loc();
		// search poi
		search_poi();
		
		MKMapViewListener mapViewListener = new MKMapViewListener() {    
			  
		    @Override    
		    public void onMapMoveFinish() {    
		        // 此处可以实现地图移动完成事件的状态监听    
		    }    
		                           
		    @Override    
		    public void onClickMapPoi(MapPoi poi) {    
		    	/*if (poi.hasCaterDetails == true){  
					mMKSearch.poiDetailSearch(info.uid);  
					// 其中，poi 对象可由常规poi检索返回  
					}    */
		    }    
		  
		    @Override  
		    public void onGetCurrentMap(Bitmap b) {  
		        //用MapView.getCurrentMap()发起截图后，在此处理截图结果.    
		    }  
		  
		    @Override  
		    public void onMapAnimationFinish() {  
		    /** 
		     *  地图完成带动画的操作（如: animationTo()）后，此回调被触发 
		     */  
		    }  
		  
		    @Override  
		    public void onMapLoadFinish() {  
		        //地图初始化完成时，此回调被触发.   
		    }       
		};    
		mcMapView.regMapViewListener(mcMapMan, mapViewListener);  //注册监听  

	}

	private void get_user_loc() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 100, 10,
				new TestLocationListener());
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		} else {
			Toast.makeText(getApplicationContext(), "位置获取失败，请检查GPS或网络连接是否正常",
					Toast.LENGTH_LONG).show();
		}
	}

	private void show_map() {

		mcMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		MapController mMapController = mcMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
		converted_point = CoordinateConvert.fromWgs84ToBaidu(point);
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(converted_point);// 设置地图中心点
		mMapController.setZoom(18);// 设置地图zoom级别
	}

	private void show_user_loc() {
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mcMapView);
		LocationData locationData = new LocationData();
		locationData.latitude = latitude;
		locationData.longitude = longitude;
		locationData.direction = 2.0f;
		myLocationOverlay.setData(locationData);
		mcMapView.getOverlays().add(myLocationOverlay);
		mcMapView.refresh();
		mcMapView.getController().animateTo(converted_point);

	}

	private void search_poi() {
		mMKSearch = new MKSearch();
		mMKSearch.init(mcMapMan, new MySearchListener());// 注意，MKSearchListener只支持一个，以最后一次设置为准

		mMKSearch.poiSearchNearBy("kfc", converted_point, 5000);
	}

	@Override
	protected void onDestroy() {
		mcMapView.destroy();
		if (mcMapMan != null) {
			mcMapMan.destroy();
			mcMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mcMapView.onPause();
		if (mcMapMan != null) {
			mcMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mcMapView.onResume();
		if (mcMapMan != null) {
			mcMapMan.start();
		}
		// 在地图上标明我的位置
		show_user_loc();
		// search poi
		search_poi();

		super.onResume();
	}

	protected class TestLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

	protected class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			// 返回地址信息搜索结果
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			// 返回驾乘路线搜索结果
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
			// 返回步行路线搜索结果
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
			// 在此处理短串请求返回结果.
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int type, int error) {
			if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
				Toast.makeText(getApplicationContext(), "抱歉，未找到结果",
						Toast.LENGTH_LONG).show();
				return;
			} else if (error != 0 || res == null) {
				Toast.makeText(getApplicationContext(), "搜索出错啦..",
						Toast.LENGTH_LONG).show();
				return;
			}
			PoiOverlay poiOverlay = new PoiOverlay(McNearbyActivity.this,
					mcMapView);
			poiOverlay.setData(res.getAllPoi());
			mcMapView.getOverlays().clear();
			mcMapView.getOverlays().add(poiOverlay);
			mcMapView.refresh();
			for (MKPoiInfo info : res.getAllPoi()) {
				if (info.pt != null) {
					mcMapView.getController().animateTo(info.pt);
					break;
				}
			}

		}
	}
}
