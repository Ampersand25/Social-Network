package domain;

import java.util.Objects;

public class Address {
    private String homeAddress;
    private String country;
    private String county;
    private String city;

    public Address(String homeAddress, String country, String county, String city) {
        this.homeAddress = homeAddress;
        this.country = country;
        this.county = county;
        this.city = city;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "home address (street name, street number, building number, floor, etc)=" + getHomeAddress() + "|country=" + getCountry() + "|county=" + getCounty() + "|city=" + getCity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeAddress(), getCountry(), getCounty(), getCity());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Address that)) {
            return false;
        }
        return Objects.equals(getHomeAddress(), that.getHomeAddress()) && Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getCounty(), that.getCounty()) && Objects.equals(getCity(), that.getCity());
    }
}
