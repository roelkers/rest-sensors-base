package de.tub.ise.anwsys.repos;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.Measurement;
import de.tub.ise.anwsys.models.Metric;

public interface MeasurementRepository extends CrudRepository<Measurement, String>{
	
	List<Measurement> findByTimeMillisBetweenAndMetric(long timeMillisA,long timeMillisB, Metric metric);
}
