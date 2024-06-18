package com.kabaddi.containers;

public class Stats {
	private String opponent;
	private String result;
	public String getOpponent() {
		return opponent;
	}
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Stats [opponent=" + opponent + ", result=" + result + "]";
	}
	public Stats(String opponent, String result) {
		super();
		this.opponent = opponent;
		this.result = result;
	}
	public Stats() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
