package ru.javaops.masterjava.matrix;

import ru.javaops.masterjava.util.Pair;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {
    private static final long EXECUTION_TIMEOUT = 50;

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final CompletionService<Pair<Integer, Integer[]>> completionService = new ExecutorCompletionService<>(executor);

        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixA.length; i++) {
            completionService.submit(concurrentMultiplyCallable(i, matrixA, matrixB));
        }

        Future<Pair<Integer, Integer[]>> resultFuture;
        while ((resultFuture = completionService.poll(EXECUTION_TIMEOUT, TimeUnit.MILLISECONDS)) != null) {
            final Pair<Integer, Integer[]> result = resultFuture.get();
            final Integer left = result.getLeft();
            final Integer[] right = result.getRight();
            matrixC[left] = Arrays.stream(right).mapToInt(Integer::intValue).toArray();
        }

        return matrixC;
    }

    private static Callable<Pair<Integer, Integer[]>> concurrentMultiplyCallable(
        final int rowNum,
        final int[][] matrixA,
        final int[][] matrixB
    ) {
        return () -> {
            final int[] rowA = matrixA[rowNum];
            final int length = rowA.length;
            final Integer[] outputRow = new Integer[length];

            final int[] rowB = new int[length];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    rowB[j] = matrixB[j][i];
                }

                int sum = 0;
                for (int k = 0; k < length; k++) {
                    sum += rowA[k] * rowB[k];
                }
                outputRow[i] = sum;
            }

            return new Pair<>(rowNum, outputRow);
        };
    }

    public static int[][] singleThreadMultiply(final int[][] matrixA, final int[][] matrixB) {
        final int rows = matrixA.length;
        final int columns = matrixB.length;
        final int[][] matrixC = new int[rows][columns];

        final int[] rowB = new int[columns];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                rowB[j] = matrixB[j][i];
            }

            for (int j = 0; j < rows; j++) {
                int[] rowA = matrixA[j];
                int sum = 0;
                for (int k = 0; k < columns; k++) {
                    sum += rowA[k] * rowB[k];
                }
                matrixC[j][i] = sum;
            }
        }

        return matrixC;
    }

    private static final int THREAD_NUMBER = 10;

    public static int[][] forkJoinMultiply(int[][] matrixA, int[][] matrixB) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_NUMBER);
        final Object invoke = forkJoinPool.invoke(new MultiplyTask(matrixA, matrixB));
        return (int[][]) invoke;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static class MultiplyTask extends RecursiveTask<int[][]> {
        private static final int THRESHOLD = 300;

        private final int[][] matrixA, matrixB;

        private final int size;

        public MultiplyTask(int[][] matrixA, int[][] matrixB) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.size = matrixA.length;
        }

        @Override
        protected int[][] compute() {
            if (size > THRESHOLD) {
                final int pivot = matrixA.length/2;
                final int[][] left = Arrays.copyOfRange(matrixA, 0, pivot);
                final int[][] right = Arrays.copyOfRange(matrixA, pivot, matrixA.length);
                final ForkJoinTask<int[][]> leftTask = ForkJoinTask.getPool().submit(new MultiplyTask(left, matrixB));
                final ForkJoinTask<int[][]> rightTask = ForkJoinTask.getPool().submit(new MultiplyTask(right, matrixB));

                return merge(leftTask.join(), rightTask.join());
            } else {
                return multiply();
            }
        }

        private int[][] multiply() {
            return MatrixUtil.singleThreadMultiply(matrixA, matrixB);
        }

        private int[][] merge(int[][] left, int[][] right) {
            return Stream.concat(Arrays.stream(left), Arrays.stream(right))
                .toArray((size -> (int[][]) Array.newInstance(left.getClass().getComponentType(), size)));
        }
    }
}
