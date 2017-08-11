package pe.geekadvice.zzleep.adapters;

/**
 * Created by gerson on 18/12/16.
 */


import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * [{
 "id": 1,
 "name": "Casa",
 "address": "Av. Las Torres",
 "image": "icon_casa.png",
 "type": "user",
 "lat": -12.32,
 "lng": -9.65,
 "user_id": 1,
 "created_at": "2017-05-21 20:08:20",
 "updated_at": "2017-05-21 20:08:20"
 }]
 */


public class ZzleepPlace
{
	private String name;
	private String address;
	private String latitude;
	private String longitude;
	private Double radio;
	private String type;
	private String image;
	private Integer status;
	private Marker marker;
	private Circle circle;
	private int id;

	public static final String TYPE_USER = "user";

	public ZzleepAudio audio;
	public ZzleepAlarm alarma;



	public ZzleepPlace(String name, String address , String lat, String lng, Double radio, String type, String image) {
		this.name = name;
		this.address = address;
		this.latitude = lat;
		this.longitude = lng;
		this.radio= radio;
		this.type=type;
		this.image=image;
		this.status = 1;
		this.marker=null;
		this.circle=null;
        this.alarma=null;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public Double getRadio() {
		return radio;
	}

	public String getType() {
		return type;
	}

	public String getImage() {
		return image;
	}

	public Integer getStatus() {
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setRadio(Double radio) {
		this.radio = radio;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}
	public Circle getCircle() {
		return circle;
	}

	public ZzleepAudio getAudio() {
		return audio;
	}

	public void setAudio(ZzleepAudio audio) {
		this.audio = audio;
	}

	public ZzleepAlarm getAlarma() {
		return alarma;
	}

	public void setAlarma(ZzleepAlarm alarma) {
		this.alarma = alarma;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	public String getJSON(String tipo, String token) throws JSONException {
		int idAlarma=0;
		int idAudio=0;
		if(alarma!=null){
			if(alarma.id!=null){
				idAlarma=alarma.id;
			}
		}
		if(audio!=null){
			if(audio.id!=null){
				idAudio=audio.id;
			}
		}
		String tmp=("{name:\"" +this.getName()+ "\", address: \""
				+this.getAddress()+ "\", image: \"" +this.getImage()
				+ "\",lat:\""+this.getLatitude()+"\",lng:\""+this.getLongitude()
				+"\" ,range:\""+this.getRadio()+"\",type:\""
				+tipo+ "\",alarma_id:"+idAlarma+",sound_id:"+idAudio+",user_id:\"" +token+"\"}");
		return tmp;
	}

}
