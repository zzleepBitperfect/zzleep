package pe.geekadvice.zzleep.adapters;

/**
 * Created by gerson on 18/12/16.
 */

public class ZzleepAlarm
{
	public String name;
	public String icon;
	public Integer status;
	public Number price;
	public String video;
	public String audio;
	public Integer id;

	public ZzleepAlarm() {

	}

	public ZzleepAlarm(String name,String icon,Integer status,Number price,String video, String audio) {
		this.name = name;
		this.icon = icon;
		this.status = status;
		this.price = price;
		this.video = video;
		this.audio = audio;
		this.id=-1;
	}
}
