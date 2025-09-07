package observer;

/**
 * 具体观察者类 - 天气预报显示
 */
public class ForecastDisplay implements Observer {
    private float currentPressure = 29.92f;
    private float lastPressure;
    private WeatherData weatherData;
    
    public ForecastDisplay(WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.attach(this);
    }
    
    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) subject;
            lastPressure = currentPressure;
            currentPressure = weatherData.getPressure();
            display();
        }
    }
    
    public void display() {
        System.out.print("天气预报: ");
        if (currentPressure > lastPressure) {
            System.out.println("天气改善!");
        } else if (currentPressure == lastPressure) {
            System.out.println("天气不变.");
        } else if (currentPressure < lastPressure) {
            System.out.println("天气变冷,可能有雨!");
        }
    }
}