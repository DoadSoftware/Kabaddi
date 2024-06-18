package com.kabaddi.containers;

public class Players {
	private String Id;
	private int foul;
	private int totalTackle;
	private int totalAccuratePass;
	private String name;
	private int shirtNumber,totalThrows;
	private String position,subPosition;
	private String captain;
	private int totalFinalThirdPasses;
	private int possWonAtt3rd;
	private int touches;
	private int setPiecesGoals;
	private double shootingAccuracy;
	private int wonCorners;
	private int goal;
	private int totalShots,TotalPass;
	private int duelWon;
	private int duelLost;
	private int dribbles;
	private int accuratePasses;
	private double expectedGoals;
	private double PassingAccuracy;
	private double expectedGoalsontarget;
	private double expectedGoalsConceded;
	private int totalClearance;
	private int effectiveClearance;
	private double duelWonRate;
	private int shotOnTarget;
	private int setPiecesAttempts;
	private double totalScorerating;
	private int SuccessfulDribbles;
	private int UnsuccessfulDribbles;
	private int ballRecovery;
	private int minsPlayed,yellowCard;
	private int unsuccessfulTouch;
	private int totalCross,redCard;
	private int interceptionWon;
	private int interception,turnover,wonTackle,wonContest;
	private int saves,shotOffTarget,totalOffside;
	private int chanceCreated,cornerTaken,goalsConceded;
	private int assists,blockedScoringAtt,ontargetScoringAtt;
	private int aerialWon;
	private int aerialLost;
	
	
	public int getRedCard() {
		return redCard;
	}
	public void setRedCard(int redCard) {
		this.redCard = redCard;
	}
	public int getTotalThrows() {
		return totalThrows;
	}
	public void setTotalThrows(int totalThrows) {
		this.totalThrows = totalThrows;
	}
	public int getTotalOffside() {
		return totalOffside;
	}
	public void setTotalOffside(int totalOffside) {
		this.totalOffside = totalOffside;
	}
	public int getOntargetScoringAtt() {
		return ontargetScoringAtt;
	}
	public void setOntargetScoringAtt(int ontargetScoringAtt) {
		this.ontargetScoringAtt = ontargetScoringAtt;
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
	public void setShotOffTarget(int shotOffTarget) {
		this.shotOffTarget = shotOffTarget;
	}
	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	public int getChanceCreated() {
		return chanceCreated;
	}
	
	public int getYellowCard() {
		return yellowCard;
	}
	public void setYellowCard(int yellowCard) {
		this.yellowCard = yellowCard;
	}
	public void setChanceCreated(int chanceCreated) {
		this.chanceCreated = chanceCreated;
	}
	public int getSaves() {
		return saves;
	}
	public void setSaves(int saves) {
		this.saves = saves;
	}
	public int getWonContest() {
		return wonContest;
	}
	public void setWonContest(int wonContest) {
		this.wonContest = wonContest;
	}
	public int getWonTackle() {
		return wonTackle;
	}
	public void setWonTackle(int wonTackle) {
		this.wonTackle = wonTackle;
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
	public int getFoul() {
		return foul;
	}
	public void setFoul(int foul) {
		this.foul = foul;
	}
	public int getTotalTackle() {
		return totalTackle;
	}
	public void setTotalTackle(int totalTackle) {
		this.totalTackle = totalTackle;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTotalAccuratePass() {
		return totalAccuratePass;
	}
	public void setTotalAccuratePass(int totalAccuratePass) {
		this.totalAccuratePass = totalAccuratePass;
	}
	public Players(int foul, int totalTackle, String name) {
		super();
		this.foul = foul;
		this.totalTackle = totalTackle;
		this.name = name;
	}
	
	public Players(int totalTackle, String name) {
		super();
		this.totalTackle = totalTackle;
		this.name = name;
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
	public int getAccuratePasses() {
		return accuratePasses;
	}
	public void setAccuratePasses(int accuratePasses) {
		this.accuratePasses = accuratePasses;
	}
	public Players() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public double getExpectedGoals() {
		return expectedGoals;
	}
	public void setExpectedGoals(double expectedGoals) {
		this.expectedGoals = expectedGoals;
	}
	
	public double getExpectedGoalsontarget() {
		return expectedGoalsontarget;
	}
	public void setExpectedGoalsontarget(double expectedGoalsontarget) {
		this.expectedGoalsontarget = expectedGoalsontarget;
	}
	public Players(int foul, int totalTackle, int totalAccuratePass, String name) {
		super();
		this.foul = foul;
		this.totalTackle = totalTackle;
		this.totalAccuratePass = totalAccuratePass;
		this.name = name;
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
	
	public int getDuelLost() {
		return duelLost;
	}
	public void setDuelLost(int duelLost) {
		this.duelLost = duelLost;
	}
	
	public int getShotOnTarget() {
		return shotOnTarget;
	}
	public void setShootOnTarget(int shotOnTarget) {
		this.shotOnTarget = shotOnTarget;
	}
	public int getSetPiecesAttempts() {
		return setPiecesAttempts;
	}
	public void setSetPiecesAttempts(int setPiecesAttempts) {
		this.setPiecesAttempts = setPiecesAttempts;
	}
	
	public int getTotalShots() {
		return totalShots;
	}
	public void setTotalShots(int totalShots) {
		this.totalShots = totalShots;
	}
	public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
	public void setShotOnTarget(int shotOnTarget) {
		this.shotOnTarget = shotOnTarget;
	}
	
	public int getTotalPass() {
		return TotalPass;
	}
	public void setTotalPass(int totalPass) {
		TotalPass = totalPass;
	}
		
	public double getPassingAccuracy() {
		return PassingAccuracy;
	}
	public void setPassingAccuracy(double passingAccuracy) {
		PassingAccuracy = passingAccuracy;
	}
	public double getExpectedGoalsConceded() {
		return expectedGoalsConceded;
	}
	public void setExpectedGoalsConceded(double expectedGoalsConceded) {
		this.expectedGoalsConceded = expectedGoalsConceded;
	}
	
	public double getTotalScorerating() {
		return totalScorerating;
	}
	public void setTotalScorerating(double totalScorerating) {
		this.totalScorerating = totalScorerating;
	}
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
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
	public int getShirtNumber() {
		return shirtNumber;
	}
	public void setShirtNumber(int shirtNumber) {
		this.shirtNumber = shirtNumber;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getCaptain() {
		return captain;
	}
	public void setCaptain(String captain) {
		this.captain = captain;
	}
	public int getBlockedScoringAtt() {
		return blockedScoringAtt;
	}
	public void setBlockedScoringAtt(int blockedScoringAtt) {
		this.blockedScoringAtt = blockedScoringAtt;
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
	
	public String getSubPosition() {
		return subPosition;
	}
	public void setSubPosition(String subPosition) {
		this.subPosition = subPosition;
	}
	
	public Players(String id, String name, int yellowCard, int redCard) {
		super();
		Id = id;
		this.name = name;
		this.yellowCard = yellowCard;
		this.redCard = redCard;
	}
	@Override
	public String toString() {
		return "Players [Id=" + Id + ", foul=" + foul + ", totalTackle=" + totalTackle + ", totalAccuratePass="
				+ totalAccuratePass + ", name=" + name + ", shirtNumber=" + shirtNumber + ", totalThrows=" + totalThrows
				+ ", position=" + position + ", subPosition=" + subPosition + ", captain=" + captain
				+ ", totalFinalThirdPasses=" + totalFinalThirdPasses + ", possWonAtt3rd=" + possWonAtt3rd + ", touches="
				+ touches + ", setPiecesGoals=" + setPiecesGoals + ", shootingAccuracy=" + shootingAccuracy
				+ ", wonCorners=" + wonCorners + ", goal=" + goal + ", totalShots=" + totalShots + ", TotalPass="
				+ TotalPass + ", duelWon=" + duelWon + ", duelLost=" + duelLost + ", dribbles=" + dribbles
				+ ", accuratePasses=" + accuratePasses + ", expectedGoals=" + expectedGoals + ", PassingAccuracy="
				+ PassingAccuracy + ", expectedGoalsontarget=" + expectedGoalsontarget + ", expectedGoalsConceded="
				+ expectedGoalsConceded + ", totalClearance=" + totalClearance + ", effectiveClearance="
				+ effectiveClearance + ", duelWonRate=" + duelWonRate + ", shotOnTarget=" + shotOnTarget
				+ ", setPiecesAttempts=" + setPiecesAttempts + ", totalScorerating=" + totalScorerating
				+ ", SuccessfulDribbles=" + SuccessfulDribbles + ", UnsuccessfulDribbles=" + UnsuccessfulDribbles
				+ ", ballRecovery=" + ballRecovery + ", minsPlayed=" + minsPlayed + ", yellowCard=" + yellowCard
				+ ", unsuccessfulTouch=" + unsuccessfulTouch + ", totalCross=" + totalCross + ", redCard=" + redCard
				+ ", interceptionWon=" + interceptionWon + ", interception=" + interception + ", turnover=" + turnover
				+ ", wonTackle=" + wonTackle + ", wonContest=" + wonContest + ", saves=" + saves + ", shotOffTarget="
				+ shotOffTarget + ", totalOffside=" + totalOffside + ", chanceCreated=" + chanceCreated
				+ ", cornerTaken=" + cornerTaken + ", goalsConceded=" + goalsConceded + ", assists=" + assists
				+ ", blockedScoringAtt=" + blockedScoringAtt + ", ontargetScoringAtt=" + ontargetScoringAtt
				+ ", aerialWon=" + aerialWon + ", aerialLost=" + aerialLost + "]";
	}
}
