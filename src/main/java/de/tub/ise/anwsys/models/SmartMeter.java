package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class SmartMeter implements Serializable{

	private static final long serialVersionUID = -1L;
	
	 @OneToMany(fetch = FetchType.EAGER,mappedBy="smartMeter",cascade = CascadeType.ALL)
	 //@JoinColumn(name="id")
	 @MapKey(name="id")
	 @JsonManagedReference
	 private Map<String,Metric> metrics = new HashMap<String,Metric>();

	@Id
	String id;
	
	public SmartMeter(String id) {
		super();
		this.id = id;
	}

	protected SmartMeter() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(HashMap<String, Metric> metrics) {
		this.metrics = metrics;
	}

	
	
}
