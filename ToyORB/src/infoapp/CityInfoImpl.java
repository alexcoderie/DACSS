package infoapp;

public class CityInfoImpl implements CityInfo {
    @Override
    public String getRoadInfo(int roadID) {
        return "Some information";
    }

    @Override
    public float getTemperature(String city) {
        return 28.0f;
    }
}
