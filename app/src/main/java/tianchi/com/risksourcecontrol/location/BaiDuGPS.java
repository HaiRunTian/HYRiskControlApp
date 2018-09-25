package tianchi.com.risksourcecontrol.location;

import android.app.Application;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Size2D;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

import tianchi.com.risksourcecontrol.base.AppInitialization;
import tianchi.com.risksourcecontrol.util.LogUtils;

/**
 * Created by Los on 2018-08-23 15:23.
 */

public class BaiDuGPS implements BaseGPS {
    private  LocationService locationService = null;
    private  AppInitialization m_app = null;
    private NavigationPanelView m_NavigationPanel = null;
    private  SensorManager m_SensorManager = null;
    private  PrjCoordSys m_priCoordSys = null;
    private  MapControl m_mapCtrl = null;
    private  Point2D m_pos = null;

    private boolean m_isFirst = true;
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener BaiduListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }

                double lat = location.getLatitude();
                double lng = location.getLongitude();
                Point2Ds m_Point2ds = new Point2Ds();
                m_Point2ds.add(new Point2D(lng, lat));
                Boolean isOk = CoordSysTranslator.forward(m_Point2ds, m_priCoordSys);
                m_pos = m_Point2ds.getItem(0);
                Point _point = m_mapCtrl.getMap().mapToPixel(m_pos);
                m_NavigationPanel.setPoint(_point);
//                LogUtils.i("map scale "+ m_mapCtrl.getMap().getScale());
                if(m_isFirst){
                  /* Rectangle2D _rect =  new Rectangle2D(m_pos, new Size2D(2000,2000));
                    m_mapCtrl.getMap().setViewBounds(_rect);
                    m_mapCtrl.getMap().refresh();*/
                   //
                    double[] scales =  m_mapCtrl.getMap().getVisibleScales();
                    m_mapCtrl.panTo(m_pos,300);
                    boolean result = m_mapCtrl.zoomTo(scales[scales.length-4],1000);
                    m_mapCtrl.getMap().refresh();
//                    LogUtils.i(Double.toString(scales[scales.length-3])+",result "+ result);
                    m_isFirst = false;


                }



               /* if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }*/
//                Log.i("location",sb.toString());
            }
        }
    };


    @Override
    public void onStart() {
        try{
            m_NavigationPanel.setVisibility(View.VISIBLE);
            if(locationService != null && locationService.isStart()) return;
            locationService = m_app.locationService;

            //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
            locationService.registerListener(BaiduListener);
            //注册监听
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            locationService.start();// 定位SDK
        }catch (Exception e){
//            Log.e("GPS:",e.toString());
        }
    }

    @Override
    public void onStop() {
        if(locationService.isStart()){
            locationService.stop();
            m_NavigationPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setApplication(Application app) {
        m_app = (AppInitialization)app;
    }

    @Override
    public void setNavigationPanel(NavigationPanelView service) {
        m_NavigationPanel = service;
    }

    @Override
    public void MoveToGPSLoaction() {
         /*Rectangle2D _rect =  new Rectangle2D(m_pos, new Size2D(2000,2000));
        m_map.setViewBounds(_rect);
       m_map.refresh();*/
    }


    @Override
    public SensorListener getSensorListener() {
        return m_BaiDuSensor;
    }

    @Override
    public void setPrjCoordSys(PrjCoordSys sys) {
        m_priCoordSys = sys;
    }

    @Override
    public void setMap(MapControl map) {
        m_mapCtrl = map;
    }

    public final SensorListener m_BaiDuSensor = new SensorListener() {

        @Override
        public void onSensorChanged(int sensor, float[] values) {
            synchronized (this) {
                m_NavigationPanel.setValue(values);
                if (Math.abs(values[0] - 0.0f) < 1)   return;
                if (m_NavigationPanel != null) {
                    m_NavigationPanel.invalidate();
                }
            }
        }
        public void onAccuracyChanged(int sensor, int accuracy) {
        }
    };
}
