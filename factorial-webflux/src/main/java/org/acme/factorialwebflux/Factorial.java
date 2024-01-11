package org.acme.factorialwebflux;

public class Factorial {

	private int input;
	private long output;
	private String timeElapsed;
	private String uuid;
	
	public Factorial(){
	
	}
	 
	public Factorial(int input, long output, String timeElapsed, String uuid) {
	  this.input = input;
	  this.output = output;
	  this.timeElapsed = timeElapsed;
	  this.uuid = uuid;
	} 
	 
	public int getInput() {
	  return input;
	}
	public long getOutput() {
	  return output;
	}
	public String getTimeElapsed() {
	  return timeElapsed;
	}
	public String getUuid() {
	  return uuid;
	}
}
