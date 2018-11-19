package tianchi.com.risksourcecontrol2.model;

import android.graphics.Bitmap;

/**
 * Created by Kevin on 2018/2/5.
 */

public interface OnDownloadFileListener {

    public void downloadSucceed(Bitmap bitmap);

    public void downloadFailed(String msg);
}
