package pe.geekadvice.zzleep.adapters;

/**
 * Created by gerson on 18/12/16.
 */

public class ZzleepAudio
{
	public Integer id;
	public String name;
	public String icon;
	public String description;
	public Integer status;
	public Double price;
	public Double discount;
	public int points;
	public String preview;
	public int company_id;
	public int sonando;

	public ZzleepAudio(Integer id, String name, String icon, String description, Integer status, Double price, Double discount, int points, String preview, int company_id) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.description = description;
		this.status = status;
		this.price = price;
		this.discount = discount;
		this.points = points;
		this.preview = preview;
		this.company_id = company_id;
		this.sonando = 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getSonando() {
		return sonando;
	}

	public void setSonando(int sonando) {
		this.sonando = sonando;
	}
}
