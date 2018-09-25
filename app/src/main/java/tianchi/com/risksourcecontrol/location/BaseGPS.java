package tianchi.com.risksourcecontrol.location;

import android.app.Application;
import android.hardware.SensorListener;

import com.supermap.data.PrjCoordSys;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

/**
 * Created by Los on 2018-08-23 15:21.
 */

public interface BaseGPS {
     void onStart();
     void onStop();
     void setApplication(Application app);
     void setNavigationPanel(NavigationPanelView service);
     void MoveToGPSLoaction();

     void setPrjCoordSys(PrjCoordSys sys);
     void setMap(MapControl map);
     SensorListener getSensorListener();
}
