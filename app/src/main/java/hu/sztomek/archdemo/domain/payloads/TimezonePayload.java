package hu.sztomek.archdemo.domain.payloads;

import hu.sztomek.archdemo.domain.common.Payload;

public class TimezonePayload implements Payload {

    private String name;
    private String city;
    private int difference;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimezonePayload that = (TimezonePayload) o;

        if (difference != that.difference) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return city != null ? city.equals(that.city) : that.city == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + difference;
        return result;
    }

    @Override
    public String toString() {
        return "TimezonePayload{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", difference=" + difference +
                '}';
    }
}
