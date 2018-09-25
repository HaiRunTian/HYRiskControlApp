package tianchi.com.risksourcecontrol.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 */

public class InitStakeNum {

    public static List<String> getStationIdArrays(String riskType){
        List<String> _list = new ArrayList<String>();
        switch (riskType) {
            case "A 高边坡类":
                for (int i = 630; i <=810 ; i++) {
                    _list.add("K0+"+i+" 右侧");
                }
                for (int i = 175; i <=345 ; i++) {
                    _list.add("K1+"+i+" 右侧");
                }
                return _list;
            case "B 高填方":
                for (int i = 790; i <=940 ; i++) {
                    _list.add("K0+"+i);
                }
                for (int i = 0; i <=17 ; i++) {
                    _list.add("K1+"+i);
                }
                return _list;
            case "C 低填浅挖":
                for (int i = 60; i <=78 ; i++) {
                    _list.add("K2+"+i);
                }
                for (int i = 969; i <=1000 ; i++) {
                    _list.add("K2+"+i);
                }
                return _list;
            case "D 高陡坡或一般陡坡":
                for (int i = 870; i <=900 ; i++) {
                    _list.add("K1+"+i);
                }
                return _list;
            case "E 软土":
                for (int i = 590; i <=610 ; i++) {
                    _list.add("K0+"+i);
                }
                return _list;
            case "F 高液限土":
                for (int i = 300; i <=500 ; i++) {
                    _list.add("K9+"+i);
                }
                return _list;
            case "G 桥梁":
                _list.add("左：K96+432");
                return _list;
            case "H 隧道":
                break;
            case "I 取弃土场":
                _list.add("ZK142+900");
                return _list;
            case "J 服务区":
                break;
            default:
                break;
        }
        return null;
    }
}
