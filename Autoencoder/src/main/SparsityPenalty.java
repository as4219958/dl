package main;

class SparsityPenalty{
	static final double RHO = 0.05;
	public static double[] calAvgRho(double[] batchActivations,double[] avgRho,int minibatch){
		int len = batchActivations.length;
		for(int i =0;i<len;i++){
			avgRho[i] = 1.0/minibatch*batchActivations[i];
		}
		return avgRho;
	}
	
	public static double[] calPenalty(double[] avgRho,double beta){
		int len = avgRho.length;
		double[] params = new double[len];
		for(int i=0;i<len;i++){
			params[i] = -RHO/avgRho[i] + (1-RHO)/(1-avgRho[i]);
		}
		return params;
	}
}