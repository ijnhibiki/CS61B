package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] percolation;
    private int size;
    private WeightedQuickUnionUF checkset;

    private int opened = 0;
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N cannot be negative.");
        }
        percolation = new boolean[N][N];
        size = N;
        checkset = new WeightedQuickUnionUF(size * size + 2);
    }                // create N-by-N grid, with all sites initially blocked
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row > size - 1 || col > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            percolation[row][col] = true;
            opened += 1;
            int currectc = xyTo1D(row, col);
            if (row - 1 >= 0 && isOpen(row - 1, col)) {
                int upc = xyTo1D((row - 1), col);
                checkset.union(currectc, upc);
            }
            if (row + 1 < size && isOpen(row + 1, col)) {
                int downc = xyTo1D((row + 1), col);
                checkset.union(currectc, downc);
            }
            if (col - 1 >= 0 && isOpen(row, col - 1)) {
                int leftc = xyTo1D(row, (col - 1));
                checkset.union(currectc, leftc);
            }
            if (col + 1 < size && isOpen(row, col + 1)) {
                int rightc = xyTo1D(row, (col + 1));
                checkset.union(currectc, rightc);
            }
            if (row == 0) {
                checkset.union(currectc, size * size);
            }
            if (row == size - 1) {
                checkset.union(currectc, size * size + 1);
            }
        }

    }      // open the site (row, col) if it is not open already
    public boolean isOpen(int row, int col) {

        if (row < 0 || col < 0 || row > size - 1 || col > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        return percolation[row][col];
    }  // is the site (row, col) open?

    public boolean isFull(int row, int col) {
        int oneDcoordinate = xyTo1D(row, col);
        if (row == size - 1 && !checkset.connected(size * size, oneDcoordinate)) {
            return false;
        }
        if (checkset.connected(size * size, oneDcoordinate) && isOpen(row, col)) {
            return true;
        }
        return false;

    }  // is the site (row, col) full?
    public int numberOfOpenSites() {
        return opened;
    }           // number of open sites
    public boolean percolates() {
        if (checkset.connected(size * size, size * size + 1)) {
            return true;
        }
        return false;
    }       // does the system percolate?

    private int xyTo1D(int r, int c) {
        return (r * size) + c;
    }

    public static void main(String[] args) {
        Percolation test = new Percolation(3);
        test.open(0, 2);
        test.open(1, 2);
        test.open(2, 2);
        test.open(2, 0);
        System.out.println(test.percolates());
        System.out.println(test.isFull(2, 0));
    }

}
