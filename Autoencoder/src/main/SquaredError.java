package main;
import utils.Tools;


public class SquaredError {
	public static double cost(double output[],double target[]){
		double sum=0;
		int n = output.length;
		for(int i=0;i<n;i++){
			sum += Math.pow(target[i]-output[i],2);
		}
		return 1.0/n*sum;
	}
	public static double[] delta(double[] output,double[] target,double[] z){
		double[] delta=new double[output.length];
		double[] dz = Tools.arrayDf(z);
		for(int i=0;i<output.length;i++){
			delta[i]=(output[i]-target[i])*dz[i];
		}
		return delta;
	}
}
