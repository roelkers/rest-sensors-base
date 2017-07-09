package de.tub.ise.anwsys.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.SmartMeter;

public interface MeterRepository extends CrudRepository<SmartMeter, String> {
	List<SmartMeter> findById(String id);
}
