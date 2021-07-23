package com.hyh.springboot.bean;

public class Dht11 {
    private String temperature;
    private String humidity;
    private String smog;
    private String infrared;
    
    public String getInfrared() {
		return infrared;
	}
	public void setInfrared(String infrared) {
		this.infrared = infrared;
	}
	private String time;

    public Dht11(String temperature, String humidity, String smog,String infrared,String time)
    {
        this.temperature=temperature;
        this.humidity=humidity;
        this.smog=smog;
        this.infrared=infrared;
        this.time=time;
        
    }
    public Dht11(){}

    public String getSmog() {
        return smog;
    }

    public void setSmog(String smog) {
        this.smog = smog;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "{" +
                "temperature='" + temperature + '\'' +                
                ", humidity='" + humidity + '\'' +
                ", smog='" + smog + '\'' +
                "infrared='" + infrared + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
