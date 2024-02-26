package jpabook.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {

	@Column(length = 10)
	private String city;

	@Column(length = 20)
	private String street;

	@Column(length = 5)
	private String zipCode;

	public String fullAddress() {
		return getCity() + " " + getStreet() + " " + getZipCode();
	}

	public String getCity() {
		return city;
	}

	private void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	private void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	private void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(getCity(), address.getCity()) &&
			Objects.equals(getStreet(), address.getStreet()) &&
			Objects.equals(getZipCode(), address.getZipCode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCity(), getStreet(), getZipCode());
	}
}
