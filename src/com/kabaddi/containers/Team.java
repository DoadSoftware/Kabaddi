package com.kabaddi.containers;

import java.util.ArrayList;
import java.util.List;

public class Team {
	
	private String name;
	private String code;
	private String ID;
	private List<Players> TeamPlayer =  new ArrayList<Players>();
	private List<Players> yellowCards =  new ArrayList<Players>();
	private List<Players> RedCards =  new ArrayList<Players>();
	private List<Stats> Stats =  new ArrayList<>();
	private int fouls;
	private int WonFouls;
	private int Tackle;
	private int totalFinalThirdPasses;
	private int possWonAtt3rd;
	private int touches;
	private int wonCorners,totalThrows;
	private int lostCorners,blockedScoringAtt;
	private int duelWon,TotalAccuratePass;
	private double duelWonRate;
	private double possession;
	private double PassingAccuracy;
	private int duelLost;
	private int goal,yellowCard,redCard;
	private int touchesInOppBox;
	private int wonTackle;
	private int dribbles;
	private int totalDribbles;
	private int accuratePasses;
	private int totalPass;
	private int possWonDef3rd;
	private int setPiecesGoals;
	private double shootingAccuracy;
	private int shotOnTarget;
	private int setPiecesAttempts;
	private int successfulFinalThirdPasses;	
	private double expectedGoals;
	private double expectedGoalsontarget;
	private double expectedGoalsConceded;
	private int totalClearance;
	private int effectiveClearance;
	private int homeContestantWins;
	private int awayContestantWins;
	private int draws;
	private int SuccessfulDribbles;
	private int UnsuccessfulDribbles;
	private int left;
	private int center;
	private int right;
	private double totalScorerating;
	private int ballRecovery;
	private int minsPlayed;
	private int unsuccessfulTouch;
	private int totalCross;
	private int interceptionWon;
	private int interception,turnover,saves;
	private int totalYellowCard,totalRedCard,cornerTaken;
	private int totalOffside,ontargetScoringAtt,shotOffTarget,goalsConceded;
	private int shots;
	private int aerialWon;
	private int aerialLost;
	
	
	public List<Players> getYellowCards() {
		return yellowCards;
	}
	public void setYellowCards(List<Players> yellowCards) {
		this.yellowCards = yellowCards;
	}
	public List<Players> getRedCards() {
		return RedCards;
	}
	public void setRedCards(List<Players> redCards) {
		RedCards = redCards;
	}
	public int getRedCard() {
		return redCard;
	}
	public void setRedCard(int redCard) {
		this.redCard = redCard;
	}
	public int getYellowCard() {
		return yellowCard;
	}
	public void setYellowCard(int yellowCard) {
		this.yellowCard = yellowCard;
	}
	public int getTotalThrows() {
		return totalThrows;
	}
	public void setTotalThrows(int totalThrows) {
		this.totalThrows = totalThrows;
	}
	public int getSaves() {
		return saves;
	}
	public void setSaves(int saves) {
		this.saves = saves;
	}
	public int getGoalsConceded() {
		return goalsConceded;
	}
	public void setGoalsConceded(int goalConceded) {
		this.goalsConceded = goalConceded;
	}
	public int getCornerTaken() {
		return cornerTaken;
	}
	public void setCornerTaken(int cornerTaken) {
		this.cornerTaken = cornerTaken;
	}
	public int getShotOffTarget() {
		return shotOffTarget;
	}
	public void setShotOffTarget(int getShotOffTarget) {
		this.shotOffTarget = getShotOffTarget;
	}
	public int getOntargetScoringAtt() {
		return ontargetScoringAtt;
	}
	public void setOntargetScoringAtt(int ontargetScoringAtt) {
		this.ontargetScoringAtt = ontargetScoringAtt;
	}
	public int getTotalOffside() {
		return totalOffside;
	}
	public void setTotalOffside(int totalOffside) {
		this.totalOffside = totalOffside;
	}
	public int getTotalYellowCard() {
		return totalYellowCard;
	}
	public void setTotalYellowCard(int totalYellowCard) {
		this.totalYellowCard = totalYellowCard;
	}
	public int getTotalRedCard() {
		return totalRedCard;
	}
	public void setTotalRedCard(int totalRedCard) {
		this.totalRedCard = totalRedCard;
	}
	public int getTurnover() {
		return turnover;
	}
	public void setTurnover(int turnover) {
		this.turnover = turnover;
	}
	
	public int getBallRecovery() {
		return ballRecovery;
	}
	public void setBallRecovery(int ballRecovery) {
		this.ballRecovery = ballRecovery;
	}
	public int getMinsPlayed() {
		return minsPlayed;
	}
	public void setMinsPlayed(int minsPlayed) {
		this.minsPlayed = minsPlayed;
	}
	public int getUnsuccessfulTouch() {
		return unsuccessfulTouch;
	}
	public void setUnsuccessfulTouch(int unsuccessfulTouch) {
		this.unsuccessfulTouch = unsuccessfulTouch;
	}
	public int getTotalCross() {
		return totalCross;
	}
	public void setTotalCross(int totalCross) {
		this.totalCross = totalCross;
	}
	public int getInterceptionWon() {
		return interceptionWon;
	}
	public void setInterceptionWon(int interceptionWon) {
		this.interceptionWon = interceptionWon;
	}
	public int getInterception() {
		return interception;
	}
	public void setInterception(int interception) {
		this.interception = interception;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public List<Players> getTeamPlayer() {
		return TeamPlayer;
	}
	public void setTeamPlayer(List<Players> teamPlayer) {
		TeamPlayer = teamPlayer;
	}
	public int getFouls() {
		return fouls;
	}
	public void setFouls(int fouls) {
		this.fouls = fouls;
	}
	
	public int getTackle() {
		return Tackle;
	}
	public void setTackle(int tackle) {
		Tackle = tackle;
	}
	
	public int getAccuratePasses() {
		return accuratePasses;
	}
	public void setAccuratePasses(int accuratePasses) {
		this.accuratePasses = accuratePasses;
	}
	public Team() {
		super();
	}
	public Team(String name, String code, String iD) {
		super();
		this.name = name;
		this.code = code;
		ID = iD;
	}
	
	public int getTotalFinalThirdPasses() {
		return totalFinalThirdPasses;
	}
	public void setTotalFinalThirdPasses(int totalFinalThirdPasses) {
		this.totalFinalThirdPasses = totalFinalThirdPasses;
	}
	public int getPossWonAtt3rd() {
		return possWonAtt3rd;
	}
	public void setPossWonAtt3rd(int possWonAtt3rd) {
		this.possWonAtt3rd = possWonAtt3rd;
	}
	public int getTouches() {
		return touches;
	}
	public void setTouches(int touches) {
		this.touches = touches;
	}
	public int getWonCorners() {
		return wonCorners;
	}
	public void setWonCorners(int wonCorners) {
		this.wonCorners = wonCorners;
	}
	public int getDuelWon() {
		return duelWon;
	}
	public void setDuelWon(int duelWon) {
		this.duelWon = duelWon;
	}
	public int getDribbles() {
		return dribbles;
	}
	public void setDribbles(int dribbles) {
		this.dribbles = dribbles;
	}
	public int getLostCorners() {
		return lostCorners;
	}
	public void setLostCorners(int lostCorners) {
		this.lostCorners = lostCorners;
	}
	public int getDuelLost() {
		return duelLost;
	}
	public void setDuelLost(int duelLost) {
		this.duelLost = duelLost;
	}
	public int getTouchesInOppBox() {
		return touchesInOppBox;
	}
	public void setTouchesInOppBox(int touchesInOppBox) {
		this.touchesInOppBox = touchesInOppBox;
	}
	public int getWonTackle() {
		return wonTackle;
	}
	public void setWonTackle(int wonTackle) {
		this.wonTackle = wonTackle;
	}
	public int getTotalDribbles() {
		return totalDribbles;
	}
	public void setTotalDribbles(int totalDribbles) {
		this.totalDribbles = totalDribbles;
	}
	public int getTotalPass() {
		return totalPass;
	}
	public void setTotalPass(int totalPass) {
		this.totalPass = totalPass;
	}
	public int getPossWonDef3rd() {
		return possWonDef3rd;
	}
	public void setPossWonDef3rd(int possWonDef3rd) {
		this.possWonDef3rd = possWonDef3rd;
	}
	public int getSuccessfulFinalThirdPasses() {
		return successfulFinalThirdPasses;
	}
	public void setSuccessfulFinalThirdPasses(int successfulFinalThirdPasses) {
		this.successfulFinalThirdPasses = successfulFinalThirdPasses;
	}
	public int getWonFouls() {
		return WonFouls;
	}
	public void setWonFouls(int wonFouls) {
		WonFouls = wonFouls;
	}
	
	public double getExpectedGoals() {
		return expectedGoals;
	}
	public void setExpectedGoals(int expectedGoals) {
		this.expectedGoals = expectedGoals;
	}
	
	public double getExpectedGoalsontarget() {
		return expectedGoalsontarget;
	}
	public void setExpectedGoalsontarget(double expectedGoalsontarget) {
		this.expectedGoalsontarget = expectedGoalsontarget;
	}
	public void setExpectedGoals(double expectedGoals) {
		this.expectedGoals = expectedGoals;
	}
	public int getTotalClearance() {
		return totalClearance;
	}
	public void setTotalClearance(int totalClearance) {
		this.totalClearance = totalClearance;
	}
	public int getEffectiveClearance() {
		return effectiveClearance;
	}
	public void setEffectiveClearance(int effectiveClearance) {
		this.effectiveClearance = effectiveClearance;
	}
	
	public int getSetPiecesGoals() {
		return setPiecesGoals;
	}
	public void setSetPiecesGoals(int setPiecesGoals) {
		this.setPiecesGoals = setPiecesGoals;
	}
	public double getShootingAccuracy() {
		return shootingAccuracy;
	}
	public void setShootingAccuracy(double shootingAccuracy) {
		this.shootingAccuracy = shootingAccuracy;
	}
	public double getDuelWonRate() {
		return duelWonRate;
	}
	public void setDuelWonRate(double duelWonRate) {
		this.duelWonRate = duelWonRate;
	}
	
	
	public int getShotOnTarget() {
		return shotOnTarget;
	}
	public void setShotOnTarget(int shotOnTarget) {
		this.shotOnTarget = shotOnTarget;
	}
	public int getSetPiecesAttempts() {
		return setPiecesAttempts;
	}
	public void setSetPiecesAttempts(int setPiecesAttempts) {
		this.setPiecesAttempts = setPiecesAttempts;
	}
	
public double getPossession() {
		return possession;
	}
	public void setPossession(double possession) {
		this.possession = possession;
	}
public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
	
public List<Stats> getStats() {
		return Stats;
	}
	public void setStats(List<Stats> stats) {
		Stats = stats;
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
	
	public double getExpectedGoalsConceded() {
		return expectedGoalsConceded;
	}
	public void setExpectedGoalsConceded(double expectedGoalsConceded) {
		this.expectedGoalsConceded = expectedGoalsConceded;
	}
	
	public int getTotalAccuratePass() {
		return TotalAccuratePass;
	}
	public void setTotalAccuratePass(int totalAccuratePass) {
		TotalAccuratePass = totalAccuratePass;
	}
	public double getPassingAccuracy() {
		return PassingAccuracy;
	}
	public void setPassingAccuracy(double passingAccuracy) {
		PassingAccuracy = passingAccuracy;
	}
	public double getTotalScorerating() {
		return totalScorerating;
	}
	public void setTotalScorerating(double totalScorerating) {
		this.totalScorerating = totalScorerating;
	}
	
	public int getSuccessfulDribbles() {
		return SuccessfulDribbles;
	}
	public void setSuccessfulDribbles(int successfulDribbles) {
		SuccessfulDribbles = successfulDribbles;
	}
	
	public int getUnsuccessfulDribbles() {
		return UnsuccessfulDribbles;
	}
	public void setUnsuccessfulDribbles(int unsuccessfulDribbles) {
		UnsuccessfulDribbles = unsuccessfulDribbles;
	}
	
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getCenter() {
		return center;
	}
	public void setCenter(int center) {
		this.center = center;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getBlockedScoringAtt() {
		return blockedScoringAtt;
	}
	public void setBlockedScoringAtt(int blockedScoringAtt) {
		this.blockedScoringAtt = blockedScoringAtt;
	}
	public int getShots() {
		return shots;
	}
	public void setShots(int shots) {
		this.shots = shots;
	}
	public int getAerialWon() {
		return aerialWon;
	}
	public void setAerialWon(int aerialWon) {
		this.aerialWon = aerialWon;
	}
	public int getAerialLost() {
		return aerialLost;
	}
	public void setAerialLost(int aerialLost) {
		this.aerialLost = aerialLost;
	}
	@Override
	public String toString() {
		return "Team [name=" + name + ", code=" + code + ", ID=" + ID + ", TeamPlayer=" + TeamPlayer + ", Stats="
				+ Stats + ", fouls=" + fouls + ", WonFouls=" + WonFouls + ", Tackle=" + Tackle
				+ ", totalFinalThirdPasses=" + totalFinalThirdPasses + ", possWonAtt3rd=" + possWonAtt3rd + ", touches="
				+ touches + ", wonCorners=" + wonCorners + ", lostCorners=" + lostCorners + ", duelWon=" + duelWon
				+ ", TotalAccuratePass=" + TotalAccuratePass + ", duelWonRate=" + duelWonRate + ", possession="
				+ possession + ", PassingAccuracy=" + PassingAccuracy + ", duelLost=" + duelLost + ", goal=" + goal
				+ ", touchesInOppBox=" + touchesInOppBox + ", wonTackle=" + wonTackle + ", dribbles=" + dribbles
				+ ", totalDribbles=" + totalDribbles + ", accuratePasses=" + accuratePasses + ", totalPass=" + totalPass
				+ ", possWonDef3rd=" + possWonDef3rd + ", setPiecesGoals=" + setPiecesGoals + ", shootingAccuracy="
				+ shootingAccuracy + ", shotOnTarget=" + shotOnTarget + ", setPiecesAttempts=" + setPiecesAttempts
				+ ", successfulFinalThirdPasses=" + successfulFinalThirdPasses + ", expectedGoals=" + expectedGoals
				+ ", expectedGoalsontarget=" + expectedGoalsontarget + ", expectedGoalsConceded="
				+ expectedGoalsConceded + ", totalClearance=" + totalClearance + ", effectiveClearance="
				+ effectiveClearance + ", homeContestantWins=" + homeContestantWins + ", awayContestantWins="
				+ awayContestantWins + ", draws=" + draws + ", SuccessfulDribbles=" + SuccessfulDribbles
				+ ", UnsuccessfulDribbles=" + UnsuccessfulDribbles + ", left=" + left + ", center=" + center
				+ ", right=" + right + ", totalScorerating=" + totalScorerating + "]";
	}
}
