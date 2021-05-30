package com.lab2a.execution;

import com.lab2a.utils.exception.ItersExceededException;
import com.lab2a.utils.exception.LabException;
import com.lab2a.utils.exception.TimeExceededException;

import java.util.ArrayList;
import java.util.List;


public class Perceptron {

    private final boolean timeLimited, itersLimited;

    double deadline;
    int max_iters;

    private double w1;
    private double w2;
    private final double sigma, P;
    private final List<double[]> points = new ArrayList<>();
    private final List<Boolean> pointIsMoreP = new ArrayList<>();
    private boolean trained = false;

    private double time;

    private int iters;

    public Perceptron(double w1, double w2, double sigma, double P) {

        this.timeLimited = false;
        this.itersLimited = false;

        this.w1 = w1;
        this.w2 = w2;
        this.sigma = sigma;
        this.P = P;

    }

    public Perceptron(double w1, double w2, double sigma, double P, int max_iters) {

        this.timeLimited = false;
        this.itersLimited = true;

        this.max_iters = max_iters;

        this.w1 = w1;
        this.w2 = w2;
        this.sigma = sigma;
        this.P = P;

    }

    public Perceptron(double w1, double w2, double sigma, double P, double deadline) {

        this.timeLimited = true;
        this.itersLimited = false;

        this.deadline = deadline;

        this.w1 = w1;
        this.w2 = w2;
        this.sigma = sigma;
        this.P = P;

    }

    public Perceptron(double w1, double w2, double sigma, double P, double deadline, int max_iters) {

        this.timeLimited = true;
        this.itersLimited = true;

        this.deadline = deadline;
        this.max_iters = max_iters;

        this.w1 = w1;
        this.w2 = w2;
        this.sigma = sigma;
        this.P = P;

    }

    private void correctWeights(double delta, double x1, double x2) {

        this.w1 = this.w1 + delta * x1 * this.sigma;
        this.w2 = this.w2 + delta * x2 * this.sigma;

    }

    public void addPoint(double x1, double x2, boolean isMoreP) {

        this.points.add(new double[]{x1, x2});
        this.pointIsMoreP.add(isMoreP);

    }

    public void train() throws LabException {

        double time0 = System.nanoTime();

        this.iters = 0;

        while (!this.trained) {

            boolean noMistakes = true;

            for (int i = 0; i < this.points.size(); i++) {

                double y = this.points.get(i)[0] * this.w1 + this.points.get(i)[1] * this.w2;
                if (y > this.P != this.pointIsMoreP.get(i)) {
                    double delta = this.P - y;
                    this.correctWeights(delta, this.points.get(i)[0], this.points.get(i)[1]);
                    noMistakes = false;
                }

            }

            this.time = System.nanoTime() - time0;

            this.iters++;

            if (this.timeLimited && this.time >= this.deadline) throw new TimeExceededException();

            if (this.itersLimited && this.iters >= this.max_iters) throw new ItersExceededException();

            if (noMistakes) {
                this.trained = true;
            }

        }

    }

    public boolean isPointMoreThanP(double x1, double x2) {

        return (this.w1 * x1 + this.w2 * x2 > this.P);

    }

    public boolean isTrained() {

        return this.trained;

    }

    public double getSigma() {

        return this.sigma;

    }

    public double getTime() {

        return this.time;

    }

    public int getIters() {

        return this.iters;

    }

}
