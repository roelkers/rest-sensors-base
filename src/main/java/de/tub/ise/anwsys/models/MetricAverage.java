package de.tub.ise.anwsys.models;

public class MetricAverage {

	private String metricId;
	private double value;
	private int sampleSize;
	
	public MetricAverage(String metricId, double value, int sampleSize) {
		super();
		this.metricId = metricId;
		this.value = value;
		this.sampleSize = sampleSize;
	}

	public String getMetricId() {
		return metricId;
	}

	public void setMetricId(String metricId) {
		this.metricId = metricId;
	}

	

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	
}
