package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
    private int[] values;
    private long globalResult = Integer.MAX_VALUE;

    @Override
    public long minimumPairwiseDistance(int[] values) {
        try {
            this.values = values;
            Thread[] threads = new Thread[4];

            // Creates threads using our inner classes
            threads[0] = new Thread(new LowerLeft());
            threads[1] = new Thread(new BottomRight());
            threads[2] = new Thread(new TopRight());
            threads[3] = new Thread(new Center());

            // Starts all the threads up
            for (int i = 0; i < 4; i++) {
                threads[i].start();
            }

            // Waits for all the threads to finish
            for (int i = 0; i < 4; i++) {
                threads[i].join();
            }

        } catch (InterruptedException IE) {}

        // Returns best result
        return globalResult;

    }

    private synchronized void updateGlobalResult(long givenResult){
        // Replace globalResult if givenResult is smaller
        if (givenResult < this.globalResult) {
            this.globalResult = givenResult;
        }
    }

    // Checks for minimal distance in the range 0 <= j < i < N/2
    class LowerLeft implements Runnable {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = 0; i < values.length/2; ++i) {
                for (int j = 0; j < i; ++j) {
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }

            // Checks to see if minimal distance found is better than current best distance
            updateGlobalResult(localResult);
        }

    }

    // Checks for minimal distance in the range N/2 <= j + N/2 < i < N
    class BottomRight implements Runnable  {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = values.length/2; i < values.length; ++i) {
                for (int j = 0; j < i - values.length/2; ++j) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }

            // Checks to see if minimal distance found is better than current best distance
            updateGlobalResult(localResult);
        }
    }

    // Checks for minimal distance in the range N/2 <= j < i < N
    class TopRight implements Runnable {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int i = values.length/2; i < values.length; ++i) {
                for (int j = values.length/2; j < i; ++j) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }

            // Checks to see if minimal distance found is better than current best distance
            updateGlobalResult(localResult);
        }
    }

    // Checks for minimal distance in the range N/2 <= i <= j + N/2 < N
    class Center implements Runnable {
        public void run() {
            long localResult = Integer.MAX_VALUE;
            for (int j = 0; j < values.length - (values.length/2); ++j) {
                for (int i = values.length/2; i < j + (values.length/2); ++i) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < localResult) {
                        localResult = diff;
                    }
                }
            }

            // Checks to see if minimal distance found is better than current best distance
            updateGlobalResult(localResult);
        }
    }

}


