
public class WeightedGuassianDistribution extends GuassianDistribution {
	private double weight;

	public WeightedGuassianDistribution(double mean, double variance, double weight) {
		super(mean, variance);
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		testDouble(weight);
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return String.format("%s, weight: %.3f", super.toString(), weight);
	}
}
