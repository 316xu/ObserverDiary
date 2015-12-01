package hust.xujifa.observerdiary.helper

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log

/**
 * Created by xujifa on 2015/11/15.
 */
class GpsTracker: Service,LocationListener{
    override fun onBind(intent: Intent?): IBinder? {
        return null as IBinder
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }


    var context:Context
    var isGpsEnable=false
    var isNetEnable=false
    var canGetLoc=false
    var loc:Location=null as Location
    var longitude:Double=0.0
    var latitude:Double=0.0
    companion object{
        var MIN_DISTANCE_UPDATE=10f
        var MIN_TIME_UPDATE=1000*30*1L
    }
    var locationManager:LocationManager=null as LocationManager
    constructor(context:Context){
        this.context=context
        getLocation()
    }

    fun getLocation():Location{
        locationManager=context.getSystemService(Context.LOCATION_SERVICE)as LocationManager
        isGpsEnable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetEnable=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        Log.d("AAA",isGpsEnable.toString()+","+isNetEnable.toString())

        if(isGpsEnable||isNetEnable){
            canGetLoc=true
            if(isNetEnable){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_UPDATE,MIN_DISTANCE_UPDATE,this);
                if(locationManager!=null){
                    loc=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if(loc!=null){
                        longitude=loc.longitude
                        latitude=loc.latitude
                    }
                }
            }
            else if (isGpsEnable) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE.toFloat(), this)
                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (loc != null) {
                        longitude = loc.getLongitude()
                        latitude = loc.getLatitude()
                    }
                }
            }
        }
        return loc
    }


}
