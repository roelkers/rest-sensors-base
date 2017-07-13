package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.sql.Date;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Measurement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@GeneratedValue
	@Id
	String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonManagedReference
	private Metric metric;
	
	private long timeMillis;
	
	private double value;
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Measurement(){
		
	}

	public Measurement(long timeMillis, double value) {
		super();
		
		this.timeMillis = timeMillis;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Measurement [id=" + id + ", timeMillis=" + timeMillis + ", value=" + value + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public Measurement(Metric metric, long timeMillis, double value) {
		super();
		this.metric = metric;
		this.timeMillis = timeMillis;
		this.value = value;
	}

	public long getTimeMillis() {
		return timeMillis;
	}

	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
	}
	
}
