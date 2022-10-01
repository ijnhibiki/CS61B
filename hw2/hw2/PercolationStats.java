package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    double [] threshold;
    int trials;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        this.threshold = new double[T];
        this.trials = T;
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("The input is illegal");
        }

        for (int i = 0; i < T; i ++){
            Percolation test = pf.make(N);
            while (!test.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                test.open(row, col);
            }
            threshold[i] = (double) test.opened / (N * N);
        }
    }  // perform T independent experiments on an N-by-N grid
    public double mean() {
        double result = StdStats.mean(threshold);
        return result;
    }                                           // sample mean of percolation threshold
    public double stddev(){
        double result = StdStats.stddev(threshold);
        return result;
    }                                         // sample standard deviation of percolation threshold
    public double confidenceLow() {
        double result = mean() - ((1.96 * stddev()) / Math.sqrt(this.trials));
        return result;
    }                                  // low endpoint of 95% confidence interval
    public double confidenceHigh() {
        double result = mean() + ((1.96 * stddev()) / Math.sqrt(this.trials));
        return result;
    }                                 // high endpoint of 95% confidence interval

}
