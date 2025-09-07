package observer;

/**
 * 具体观察者类 - 当前天气状况显示
 */
public class CurrentConditionsDisplay implements Observer {
    private float temperature;
    private float humidity;
    private WeatherData weatherData;
    
    public CurrentConditionsDisplay(WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.attach(this);
    }
    
    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) subject;
            this.temperature = weatherData.getTemperature();
            this.humidity = weatherData.getHumidity();
            display();
        }
    }
    
    public void display() {
        System.out.println("当前天气状况: " + temperature + "°C, " + humidity + "% humidity");
    }
}