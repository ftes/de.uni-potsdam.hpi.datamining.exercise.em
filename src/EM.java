import java.util.ArrayList;
import java.util.List;


public class EM {
	public static final double MAX_INIT_VARIANCE = 50;
	public static final double MAX_INIT_MEAN = 50;
	public static final double MAX_DELTA = 0.00001;
	
	private static void printSamples(Double[] samples) {
		int columns = 10;
		
		System.out.print("Samples:");
		for (int i=0; i<samples.length; i++) {
			if (i % columns == 0) {
				System.out.println();
			}
			System.out.print(String.format("% 6.2f ", samples[i]));
		}
		System.out.println();
	}
	
	private static double calcLogLikelihood(WeightedGuassianDistribution[] distrs, Double[] samples) {
		double sum = 0;
		for (double sample : samples) {
			double distrSum = 0;
			for (WeightedGuassianDistribution distr : distrs) {
				distrSum += distr.getWeight() * distr.calcProbability(sample);
			}
			sum += Math.log(distrSum);
		}
		return sum;
	}
			
	public static WeightedGuassianDistribution[] 
			perform(Double[] samples, int numberOfUnderLyingDistributions) {
		WeightedGuassianDistribution[] distrs = 
				new WeightedGuassianDistribution[numberOfUnderLyingDistributions];
		
		for (int i=0; i<numberOfUnderLyingDistributions; i++) {
			double weight = 1d / numberOfUnderLyingDistributions;
			double variance = Math.random() * MAX_INIT_VARIANCE;
			double mean = Math.random() * MAX_INIT_MEAN;
			distrs[i] = new WeightedGuassianDistribution(mean, variance, weight);
		}
		
		double delta = Double.MAX_VALUE;
		int step = 0;
		while (delta > MAX_DELTA) {
			double oldLogLikelihood = calcLogLikelihood(distrs, samples);
			step++;
			
			System.out.println(String.format("step %d, delta %.8f", step, delta));
			
			double[] probSums = new double[samples.length];
			for (int i=0; i<samples.length; i++) {
				probSums[i] = 0;
				for (WeightedGuassianDistribution distr : distrs) {
					probSums[i] += distr.calcProbability(samples[i]) * distr.getWeight();
				}
			}
			
			double[][] weights = new double[numberOfUnderLyingDistributions][samples.length];
			double[] weightSums = new double[numberOfUnderLyingDistributions];
			for (int i=0; i<numberOfUnderLyingDistributions; i++) {
				weightSums[i] = 0;
				for (int j=0; j<samples.length; j++) {
					weights[i][j] += (distrs[i].calcProbability(samples[j])*distrs[i].getWeight())/probSums[j];
					weightSums[i] += weights[i][j];
				}
			}

			for (int i=0; i<numberOfUnderLyingDistributions; i++) {
				distrs[i].setWeight(weightSums[i] / samples.length);
				
				double mean = 0;
				for (int j=0; j<samples.length; j++) {
					mean += weights[i][j] * samples[j];
				}
				mean /= weightSums[i];
				distrs[i].setMean(mean);
				
				double variance = 0;
				for (int j=0; j<samples.length; j++) {
					variance += weights[i][j] * Math.pow(samples[j] - mean, 2);
				}
				variance /= weightSums[i];
				distrs[i].setVariance(variance);
				
				System.out.println(distrs[i]);
			}
			
			double newLogLikelihood = calcLogLikelihood(distrs, samples);
			delta = Math.abs(newLogLikelihood - oldLogLikelihood);
			
			System.out.println();
		}
		
		return distrs;
	}
	
	public static void main(String[] args) {
		int[] numberOfSamples = {30, 50};
		int numberOfUnderLyingDistributions = numberOfSamples.length;
		
		List<Double> samples = new ArrayList<>();
		WeightedGuassianDistribution[] startDistrs = 
				new WeightedGuassianDistribution[numberOfUnderLyingDistributions];
		
		int totalNumberOfSamples = 0;
		for (int n : numberOfSamples) {
			totalNumberOfSamples += n;
		}
		
		System.out.println("Start distributions:");
		for (int i=0; i<numberOfUnderLyingDistributions; i++) {
			double mean = Math.random() * MAX_INIT_MEAN;
			double variance = Math.random() * MAX_INIT_VARIANCE;
			double weight = (double) numberOfSamples[i] / totalNumberOfSamples;
			startDistrs[i] = new WeightedGuassianDistribution(mean, variance, weight);
			System.out.println(startDistrs[i]);
			
			for (int j=0; j<numberOfSamples[i]; j++) {
				samples.add(startDistrs[i].calcRandomPoint());
			}
		}
		Double[] samplesArray = samples.toArray(new Double[0]);
		
		printSamples(samplesArray);
		
		System.out.println("------------------------------\n\n");
		
		WeightedGuassianDistribution[] generatedDistrs =
				perform(samples.toArray(new Double[0]), numberOfUnderLyingDistributions);
		
		System.out.println("\n\n--------------------------------");
		printSamples(samplesArray);
		System.out.println("Start distributions:");
		for (WeightedGuassianDistribution distr : startDistrs) {
			System.out.println(distr);
		}
		System.out.println("Generated distributions:");
		for (WeightedGuassianDistribution distr : generatedDistrs) {
			System.out.println(distr);
		}
	}
}
