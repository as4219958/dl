package main;

class SparsityPenalty{
	static final double RHO = 0.01;

	public static double[][] calAvgRho(double[][] batchActivations,double[][] avgRho,int minibatch){
		for(int i =0;i<minibatch;i++){
			for(int j =0;j<batchActivations[i].length;j++){
			avgRho[i][j] = 1.0/minibatch*batchActivations[i][j];
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