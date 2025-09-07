package observer;

/**
 * 增强版天气数据 - 使用异步通知
 */
public class EnhancedWeatherData extends AsyncSubject {
    private float temperature;
    private float humidity;
    private float pressure;
    
    // 获取温度
    public float getTemperature() {
        return temperature;
    }
    
    // 获取湿度
    public float getHumidity() {
        return humidity;
    }
    
    // 获取气压
    public float getPressure() {
        return pressure;
    }
    
    // 当气象数据更新时调用此方法
    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }
    
    // 气象数据改变时通知观察者
    private void measurementsChanged() {
        // 异步通知观察者
        notifyObserversAsync();
    }
}