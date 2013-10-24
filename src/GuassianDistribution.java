import java.util.Random;


public class GuassianDistribution {
	private double mean;
	private double variance;
	
	private static final Random rnd = new Random();
	
	protected void testDouble(double d) {
		if (Double.isNaN(d)) {
			System.err.println("Problem");
			System.err.println(this);
			throw new RuntimeException();
		}
	}
	
	public GuassianDistribution(double mean, double variance) {
		super();
		this.mean = mean;
		this.variance = variance;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		testDouble(mean);
		this.mean = mean;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		testDouble(variance);
		this.variance = variance;
	}
	
	public double calcProbability(double x) {
		double result = 1d / (Math.sqrt(2d*Math.PI*variance))*Math.pow(Math.E, (Math.pow(x-mean, 2))/(2*variance));
		testDouble(result);
		return result;
	}
	
	public double calcRandomPoint() {
		return rnd.nextGaussian() * Math.sqrt(variance) + mean;
	}
	
	@Override
	public String toString() {
		return String.format("mean: %2.3f, variance: %2.3f", mean, variance);
	}
}
