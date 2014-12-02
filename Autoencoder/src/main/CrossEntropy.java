package main;

import utils.Tools;

public class CrossEntropy {
	public static double cost(double output[],int minibatch,double target[]){
		return cost(output,target,minibatch,false);
	}
	public static double cost(double output[],double target[],int minibatch,boolean useSparsity){
		double sum=0;
		int n = output.length;
		if(useSparsity){
			for(int i=0;i<n;i++){
				sum += target[i]*Tools.ln(output[i])+(1-target[i])*Tools.ln(1-output[i]);
			}
		}
		else{
			for(int i=0;i<n;i++){
				sum += target[i]*Tools.ln(output[i])+(1-target[i])*Tools.ln(1-output[i]);
			}
		}
		return -1.0/minibatch*sum;
	}
	public static double[] delta(double[] output,double[] target){
		double[] delta=new double[output.length];
		for(int i=0;i<output.length;i++){
			delta[i]=output[i]-target[i];
		}
		return delta;
	}
}
