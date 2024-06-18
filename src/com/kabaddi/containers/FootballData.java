package com.kabaddi.containers;

import java.util.List;
public class FootballData {
	
	List<Team> Team;
	private String matchId;
	private float WinHomeProbability;
	private float WinAwayProbability;
	private float WinDrawProbability;
	private float overallWinHomeProbability;
	private float overallWinAwayProbability;
	private float overallWinDrawProbability;
	private int homeContestantWins;
	private int awayContestantWins;
	private int draws;
	
	public List<Team> getTeam() {
		return Team;
	}

	public void setTeam(List<Team> team) {
		Team = team;
	}

	@Override
	public String toString() {
		return "FootballData [Team=" + Team + "]";
	}

	public FootballData(List<Team> team) {
		super();
		Team = team;
	}

	public float getWinHomeProbability() {
		return WinHomeProbability;
	}

	public void setWinHomeProbability(float winHomeProbability) {
		WinHomeProbability = winHomeProbability;
	}

	public float getWinAwayProbability() {
		return WinAwayProbability;
	}

	public void setWinAwayProbability(float winAwayProbability) {
		WinAwayProbability = winAwayProbability;
	}

	public float getWinDrawProbability() {
		return WinDrawProbability;
	}

	public void setWinDrawProbability(float winDrawProbability) {
		WinDrawProbability = winDrawProbability;
	}

	public float getOverallWinHomeProbability() {
		return overallWinHomeProbability;
	}

	public void setOverallWinHomeProbability(float overallWinHomeProbability) {
		this.overallWinHomeProbability = overallWinHomeProbability;
	}

	public float getOverallWinAwayProbability() {
		return overallWinAwayProbability;
	}

	public void setOverallWinAwayProbability(float overallWinAwayProbability) {
		this.overallWinAwayProbability = overallWinAwayProbability;
	}

	public float getOverallWinDrawProbability() {
		return overallWinDrawProbability;
	}

	public void setOverallWinDrawProbability(float overallWinDrawProbability) {
		this.overallWinDrawProbability = overallWinDrawProbability;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public int getHomeContestantWins() {
		return homeContestantWins;
	}

	public void setHomeContestantWins(int homeContestantWins) {
		this.homeContestantWins = homeContestantWins;
	}

	public int getAwayContestantWins() {
		return awayContestantWins;
	}

	public void setAwayContestantWins(int awayContestantWins) {
		this.awayContestantWins = awayContestantWins;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public FootballData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
