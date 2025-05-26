package com.instarverse.api.v1.vote;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int requestAmount = 50000;
		double feeRate = (double) 100 / 10000;
		double feeDouble = requestAmount * feeRate;
		int platformFee = (int) feeDouble;
		int withdrawalAmount = requestAmount - platformFee;
		
		System.out.println(feeRate);
		System.out.println(feeDouble);
		System.out.println(platformFee);
		System.out.println(withdrawalAmount);
	}

}
