package de.tub.ise.anwsys.models;

public class MetricAverage {

	private String metricName;
	
	private double value;
	private int sampleSize;
	
	public MetricAverage(String metricName, double value, int sampleSize) {
		super();
		this.metricName = metricName;
		this.value = value;
		this.sampleSize = sampleSize;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricId(String metricId) {
		this.metricName = metricId;
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
