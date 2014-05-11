package businessobjects;

public class Adress {
	private String street;
	private int postcode;
	private String city;
	private String country;
	
	public Adress(String street, int postcode, String city, String country) {
		this.street = street;
		this.postcode = postcode;
		this.city = city;
		this.country = country;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getPostcode() {
		return postcode;
	}
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
