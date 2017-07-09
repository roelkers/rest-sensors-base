package de.tub.ise.anwsys;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import de.tub.ise.anwsys.models.Metric;
import de.tub.ise.anwsys.models.SmartMeter;
import de.tub.ise.anwsys.repos.MeterRepository;
import de.tub.ise.anwsys.repos.MetricRepository;

@SpringBootApplication
@ComponentScan("de.tub.ise.anwsys")
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
    
    @Bean
    CommandLineRunner init(MeterRepository meterRepository, MetricRepository metricRepository){
    	return (evt) -> Arrays.asList(
				"ise1224hi5630,ise1224hi5631,ise1224hi5632".split(","))
				.forEach(
						meterName -> {
							SmartMeter meter = meterRepository.save(new SmartMeter(meterName));
							Metric metric1 = new Metric("MX-11460-01","Current(mA)",meter);
							Metric metric2 = new Metric("MX-11564-01","Voltage(V)",meter);
							meter.getMetrics().put("MX-11460-01", metric1);
							meter.getMetrics().put("MX-11564-01", metric2);
							meterRepository.save(meter);
							metricRepository.save(metric1);
							metricRepository.save(metric2);
							//System.out.println(meter.getMetrics().get("MX-11564-01"));
						});
    			
    }

}
