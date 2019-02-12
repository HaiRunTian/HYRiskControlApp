package tianchi.com.risksourcecontrol2.location;

import android.app.Application;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;

import com.supermap.data.Point2D;
import com.supermap.data.PrjCoordSys;
import com.supermap.mapping.MapControl;

/**
 * Created by Los on 2018-08-24 8:54.
 */

public class OriginalGPS implements BaseGPS {
    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void setApplication(Application app) {

    }

    @Override
    public void setNavigationPanel(NavigationPanelView service) {

    }

    @Override
    public void MoveToGPSLoaction() {

    }

    @Override
    public void setPrjCoordSys(PrjCoordSys sys) {

    }

    @Override
    public void setMap(MapControl map) {

    }

    @Override
    public Point2D getPoint2D() {
        return null;
    }

    @Override
    public SensorEventListener getSensorListener() {
        return null;
    }
}
