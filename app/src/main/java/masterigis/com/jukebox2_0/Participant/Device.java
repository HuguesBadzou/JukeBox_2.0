package masterigis.com.jukebox2_0.Participant;

/**
 * Created by Akretche on 20/01/2016.
 */
import android.net.wifi.p2p.WifiP2pDevice;

//Created by Hugues on 15/01/2016.
public class Device {

    String deviceName;
    WifiP2pDevice device;

    // Constructeur par défaut
    public Device(){

    }

    // Setteur
    public void setdeviceName(String deviceNameA){
        this.deviceName=deviceNameA;
    }

    public void setDevice(WifiP2pDevice deviceA){
        this.device=deviceA;
    }

    // Getteur
    public String getDeviceName(){
        return deviceName;
    }

    public WifiP2pDevice getDevice(){
        return device;
    }

}