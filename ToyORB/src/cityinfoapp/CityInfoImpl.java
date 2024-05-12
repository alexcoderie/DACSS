package cityinfoapp;

public class CityInfoImpl implements CityInfo {
    @Override
    public String getRoadInfo(int roadID) {
        return "Some information";
    }

    @Override
    public String getTemperature(String city) {
        return "28.0";
    }
}
