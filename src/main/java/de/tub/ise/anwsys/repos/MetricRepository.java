package de.tub.ise.anwsys.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.Metric;

public interface MetricRepository extends CrudRepository<Metric, String> {
	List<Metric> findById(String id);
}
