package com.kabaddi.broadcaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import com.kabaddi.containers.FootballData;
import com.kabaddi.containers.Scene;
import com.kabaddi.containers.ScoreBug;
import com.kabaddi.model.*;
import com.kabaddi.service.KabaddiService;
import com.kabaddi.util.KabaddiFunctions;
import com.kabaddi.util.KabaddiUtil;
import com.opencsv.exceptions.CsvException;

import net.sf.json.JSONArray;

public class Kabaddi extends Scene{
	
	public String session_selected_broadcaster = KabaddiUtil.KABADDI;
	
	public ScoreBug scorebug = new ScoreBug(); 
	public String which_graphics_onscreen = "";
	public String which_LBand_onscreen = "",which_Right_LBand_onscreen = "",which_Bottom_LBand_onscreen = "";
	public boolean is_infobar = false;
	public String flag_path = "IMAGE*/Default/Essentials/Flag_Id/";
	public String Lt_flag_path = "IMAGE*/Default/Essentials/Flags/";
	
	public String logo_path = "IMAGE*/Default/Essentials/Badges/";
	public String logo_bw_path = "IMAGE*/Default/Essentials/BadgesBW/";
	public String logo_outline_path = "IMAGE*/Default/Essentials/BadgesOutline/";
	public String logo2_path = "IMAGE*/Default/Design/";
	private String colors_path = "C:\\Images\\Super_Cup\\Colours\\";
	private String photos_path = "C:\\Images\\Super_Cup\\Photos\\";
	private String image_path = "C:\\Sports\\Football\\Statistic\\Match_Data\\";
	private String status;
	private String slashOrDash = "-";
	private String formation = "",top_stats_value="";
	private int count = 0,TeamId=0,which_side=1;
	public static List<String> penalties;
	public static List<String> penaltiesremove;
	public static LeaderBoard leaderBoard;
	public String vtp = "";
	public Player player;
	public static FootballData data = new FootballData();
	
	public Kabaddi() {
		super();
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ScoreBug updateScoreBug(PrintWriter print_writer, List<Scene> scenes, Match match,KabaddiService footballService) throws InterruptedException, MalformedURLException, IOException, CsvException
	{
		if(scorebug.isScorebug_on_screen() == true) {
			scorebug = populateScoreBug(print_writer, true,scorebug, scenes.get(0).getScene_path(), match, session_selected_broadcaster);
			scorebug = populateExtraTime(print_writer, true,scorebug, null, match, session_selected_broadcaster);
		}
		return scorebug;
	}
	public Object ProcessGraphicOption(PrintWriter print_writer, String whatToProcess,Match match,Clock clock, KabaddiService footballService, List<Scene> scenes, 
			String valueToProcess) throws InterruptedException, NumberFormatException, MalformedURLException, IOException, CsvException, JAXBException, SAXException, ParserConfigurationException{
		
		if (which_graphics_onscreen == "PENALTY")
		{
			int iHomeCont = 0, iAwayCont = 0;
			penalties.add(valueToProcess.split(",")[1]);
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$Seperator$AllScoreGrp$txt_HomeScore*GEOM*TEXT SET " + match.getHomePenaltiesHits() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$Seperator$AllScoreGrp$txt_AwayScore*GEOM*TEXT SET " + match.getAwayPenaltiesHits() + "\0");
			
			for(String pen : penalties)
			{	
				if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");

					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");

					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");

					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}
			}
			if(match.getHomePenaltiesHits() == 0 && match.getAwayPenaltiesHits() == 0) {
				penalties = new ArrayList<String>();
				penaltiesremove = new ArrayList<String>();
			}
		} else {
			if(penalties == null) {
				penalties = new ArrayList<String>();
				penaltiesremove = new ArrayList<String>();
			}
			if(match.getHomePenaltiesHits() == 0 && match.getAwayPenaltiesHits() == 0) {
				penalties = new ArrayList<String>();
				penaltiesremove = new ArrayList<String>();
			}
			int iHomeCont = 0, iAwayCont = 0;
			penalties.add(valueToProcess.split(",")[1]);
			if((match.getHomePenaltiesHits()+match.getHomePenaltiesMisses()) != 0 && (match.getAwayPenaltiesHits()+match.getAwayPenaltiesHits()) != 0) {
				if(((match.getHomePenaltiesHits()+match.getHomePenaltiesMisses())%5) == 0 && ((match.getAwayPenaltiesHits()+match.getAwayPenaltiesMisses())%5) == 0) {
					if(match.getHomePenaltiesHits() == match.getAwayPenaltiesHits()) {
						penalties = new ArrayList<String>();
//						for(int p=1;p<=5;p++) {
//							print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + p + "$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
//							print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + p + "$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
//						}
					}
				}
			}
			
			//print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tScore " + match.getHomePenaltiesHits() + "-" + match.getAwayPenaltiesHits() + ";");

			for(String pen : penalties)
			{
				//System.out.println("ELSE LOOP - " + iHomeCont);
				if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");

					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
					
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");

					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
			
					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}
			}
		}
		
		switch (whatToProcess.toUpperCase()) {
		
		case "POPULATE-SCOREBUG": case "POPULATE-SCOREBUG_STATS": case "POPULATE-EXTRA_TIME": case "POPULATE-EXTRA_TIME_BOTH": case "POPULATE-RED_CARD": 
		case "POPULATE-EXTRA_TIME_HALF": case "POPULATE-SCOREBUG-CARD": case "POPULATE-SCOREBUG-SUBS": case "POPULATE-SUBS_CHANGE_ON": case "POPULATE-SCOREBUG-PROMO":
		case "POPULATE-SCOREBUG_TEAM_STATS": case "POPULATE-SCOREBUG_PLAYER_STATS": case "POPULATE-HEADTOHEAD": case "POPULATE-SCOREBUG_STATS_API":
			
		case "POPULATE-FF-MATCHID": case "POPULATE-FF-PROMO": case "POPULATE-FF-PLAYINGXI": case "POPULATE-FF-MATCHSTATS": case "POPULATE-DOUBLE_PROMO": 
		case "POPULATE-FF-TEAMS": case "POPULATE-POINTS_TABLE": case "POPULATE-FIXTURES": case "POPULATE-POINTS_TABLE2": case "POPULATE-PLAYOFFS": 
		case "POPULATE-ROAD-TO-FINAL": case "POPULATE-L3-MATCHSTATUS": case "POPULATE-ATTACKING_ZONE": case "POPULATE-FF-PLAYER_TOUCH_MAP":
		case "POPULATE-FF-TOURNAMENT_STATS": case "POPULATE-FF-TEAM_COMPARISON": case "POPULATE-FF-HEADTOHEAD": case "POPULATE-FF-FIXTUREANDRESULT":
		case "POPULATE-FF-TEAM_TOUCH":
			
		case "POPULATE-HOMESUB": case "POPULATE-AWAYXI": case "POPULATE-AWAYSUB": case "POPULATE-QUAIFIERS": case "POPULATE-FF-CHETTRI":
		case "POPULATE-PENALTY": case "POPULATE-CHANGE_PENALTY": 
		case "POPULATE-LOF-LINEUP": case "POPULATE-LOF-VERTICAL_FLIPPER": case "POPULATE-LOF-LEADERBOARD": case "POPULATE-VERTICAL_CHANGE_ON":
		case "POPULATE-CHETTRI_CHANGE_ON": case "POPULATE-LOF-AVG_FORMATION":
			
		case "POPULATE-L3-BUG-DB": case "POPULATE-HIGHLIGHT_SCOREBUG": 
		
		case "POPULATE-LT-PROMO": case "POPULATE-L3-TEAMFIXTURE": case "POPULATE-L3-SCOREUPDATE": case "POPULATE-LT-MATCHID": 
		case "POPULATE-L3-NAMESUPER": case "POPULATE-L3-NAMESUPER-PLAYER": case "POPULATE-L3-NAMESUPER-CARD": case "POPULATE-L3-SUBSTITUTE": 
		case "POPULATE-OFFICIALS": case "POPULATE-L3-HEATMAP": case "POPULATE-L3-TOP_STATS": case "POPULATE-L3-STAFF": case "POPULATE-LT-RESULT":
		case "POPULATE-LT-PLAYER_STATS":
		
			switch(whatToProcess.toUpperCase()) {
			case "POPULATE-SCOREBUG_STATS": case "POPULATE-EXTRA_TIME": case "POPULATE-EXTRA_TIME_BOTH": case "POPULATE-RED_CARD": case "POPULATE-SCOREBUG-CARD":
			case "POPULATE-SCOREBUG-SUBS": case "POPULATE-SUBS_CHANGE_ON": case "POPULATE-EXTRA_TIME_HALF": case "POPULATE-SCOREBUG-PROMO":
			case "POPULATE-SCOREBUG_TEAM_STATS": case "POPULATE-SCOREBUG_PLAYER_STATS": case "POPULATE-HEADTOHEAD": case "POPULATE-SCOREBUG_STATS_API":
			
			case "POPULATE-HOMESUB": case "POPULATE-AWAYXI": case "POPULATE-AWAYSUB":
			case "POPULATE-CHANGE_PENALTY": case "POPULATE-VERTICAL_CHANGE_ON": case "POPULATE-CHETTRI_CHANGE_ON":
				break;
			case "POPULATE-SCOREBUG":
				scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
				break;
			case "POPULATE-FF-MATCHID": case "POPULATE-FF-PROMO": case "POPULATE-FF-PLAYINGXI": case "POPULATE-FF-MATCHSTATS": case "POPULATE-DOUBLE_PROMO": 
			case "POPULATE-FF-TEAMS": case "POPULATE-POINTS_TABLE": case "POPULATE-FIXTURES": case "POPULATE-POINTS_TABLE2": case "POPULATE-PLAYOFFS": 
			case "POPULATE-ROAD-TO-FINAL": case "POPULATE-L3-MATCHSTATUS": case "POPULATE-ATTACKING_ZONE": case "POPULATE-FF-PLAYER_TOUCH_MAP":
			case "POPULATE-FF-TOURNAMENT_STATS": case "POPULATE-FF-TEAM_COMPARISON": case "POPULATE-FF-HEADTOHEAD": case "POPULATE-FF-FIXTUREANDRESULT":
			case "POPULATE-FF-TEAM_TOUCH":

				scenes.get(1).scene_load(print_writer,session_selected_broadcaster);
				print_writer.println("-1 RENDERER*STAGE SHOW 0.0\0");
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Reset START \0");
				break;
			case "POPULATE-LOF-LINEUP": case "POPULATE-LOF-LEADERBOARD": case "POPULATE-L3-TOP_STATS": case "POPULATE-LOF-AVG_FORMATION":
				if(which_graphics_onscreen == "" && which_graphics_onscreen.isEmpty()) {
					scenes.get(2).setScene_path(valueToProcess.split(",")[1]);
					scenes.get(2).scene_load(print_writer,session_selected_broadcaster);
					print_writer.println("-1 RENDERER*STAGE SHOW 0.0\0");
				}
				break;
			default:
				scenes.get(2).setScene_path(valueToProcess.split(",")[1]);
				scenes.get(2).scene_load(print_writer,session_selected_broadcaster);
				print_writer.println("-1 RENDERER*STAGE SHOW 0.0\0");
				break;
			}
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-SCOREBUG":
				populateScoreBug(print_writer, false,scorebug, valueToProcess.split(",")[1],match, session_selected_broadcaster);
				break;
			case "POPULATE-SCOREBUG-PROMO":
				scorebug.setScorebug_promo(valueToProcess.split(",")[1]);
				populateScoreBugPromo(print_writer, false,scorebug,Integer.valueOf(valueToProcess.split(",")[1]),footballService.getTeams(),
						footballService.getFixtures(),footballService.getGrounds(),match , session_selected_broadcaster);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Promo_In START \0");
				break;
			case "POPULATE-SCOREBUG_TEAM_STATS":
				if(scorebug.getLast_scorebug_team_stat() != null && !scorebug.getLast_scorebug_team_stat().trim().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*TeamStats_Out START \0");
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_team_stat(valueToProcess.split(",")[1]);
					populateScoreBugTeamStats(print_writer, false, scorebug, Integer.valueOf(valueToProcess.split(",")[1]), 
							footballService.getTeams(), footballService.getTeamStats(), match, session_selected_broadcaster);
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*TeamStats_In START \0");
				}else {
					scorebug.setScorebug_team_stat(valueToProcess.split(",")[1]);
					populateScoreBugTeamStats(print_writer, false, scorebug, Integer.valueOf(valueToProcess.split(",")[1]), 
							footballService.getTeams(), footballService.getTeamStats(), match, session_selected_broadcaster);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*TeamStats_In START \0");
				}
				break;
			case "POPULATE-SCOREBUG_PLAYER_STATS":
				if(scorebug.getLast_scorebug_player_stat() != null && !scorebug.getLast_scorebug_player_stat().trim().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatsImage_Out START \0");
					TimeUnit.MILLISECONDS.sleep(800);
					scorebug.setScorebug_player_stat(valueToProcess.split(",")[4]);
					populateScoreBugPlayerStats(print_writer, false, scorebug, Integer.valueOf(valueToProcess.split(",")[1]), valueToProcess.split(",")[2],
							Integer.valueOf(valueToProcess.split(",")[4]), valueToProcess.split(",")[3], footballService.getTeams(), footballService.getAllPlayer(), 
							footballService.getPlayerStats(), match, session_selected_broadcaster);
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatsImage_In START \0");
				}else {
					scorebug.setScorebug_player_stat(valueToProcess.split(",")[4]);
					populateScoreBugPlayerStats(print_writer, false, scorebug, Integer.valueOf(valueToProcess.split(",")[1]), valueToProcess.split(",")[2],
							Integer.valueOf(valueToProcess.split(",")[4]), valueToProcess.split(",")[3], footballService.getTeams(), footballService.getAllPlayer(), 
							footballService.getPlayerStats(), match, session_selected_broadcaster);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatsImage_In START \0");
				}
				break;
				
			case "POPULATE-SCOREBUG-CARD":	
				if(scorebug.getLast_scorebug_card_goal() != null && !scorebug.getLast_scorebug_card_goal().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Cards_Out START \0");
					TimeUnit.MILLISECONDS.sleep(500);
					
					scorebug.setScorebug_card_goal(valueToProcess.split(",")[2]);
					populateScorebugCard(print_writer, scorebug, Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[3]), 
							match, session_selected_broadcaster);
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Cards_In START \0");
				}else {
					scorebug.setScorebug_card_goal(valueToProcess.split(",")[2]);
					populateScorebugCard(print_writer, scorebug, Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[3]), 
							match, session_selected_broadcaster);
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Cards_In START \0");
				}
				break;
			case "POPULATE-SCOREBUG-SUBS":
				scorebug.setScorebug_subs(valueToProcess.split(",")[2]);
				populateScorebugSubs(print_writer, scorebug, Integer.valueOf(valueToProcess.split(",")[1]), footballService.getAllPlayer(), match, 
						session_selected_broadcaster);
				TimeUnit.MILLISECONDS.sleep(500);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Substitutes$Subtitutes_In START \0");
				break;
			case "POPULATE-RED_CARD":
				populateRedcard(print_writer, false,scorebug,Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[2]),
						match,session_selected_broadcaster);
				break;
			case "POPULATE-EXTRA_TIME_HALF":
				populateETONE_TWO(print_writer, false,scorebug,match,session_selected_broadcaster);
				break;
			case "POPULATE-EXTRA_TIME":
				populateExtraTime(print_writer, false,scorebug,valueToProcess.split(",")[1],match,session_selected_broadcaster);
				break;
			case "POPULATE-EXTRA_TIME_BOTH":
				populateExtraTimeBoth(print_writer, false,scorebug,valueToProcess.split(",")[1],match,session_selected_broadcaster);
				break;
			case "POPULATE-L3-TEAMFIXTURE":
				populateTeamFixture(print_writer,valueToProcess.split(",")[1],Integer.valueOf(valueToProcess.split(",")[2]), match, 
						session_selected_broadcaster,footballService.getTeams(),footballService.getFixtures());
				break;
			case "POPULATE-FF-MATCHID":
				populateMatchId(print_writer,valueToProcess.split(",")[1], match, session_selected_broadcaster,footballService.getVariousTexts());
				break;
			case "POPULATE-FF-HEADTOHEAD":
				populateFF_H2H(print_writer,valueToProcess.split(",")[1], match, session_selected_broadcaster);
				break;
			case "POPULATE-PLAYOFFS":
				populatePlayoffs(print_writer, valueToProcess.split(",")[1],footballService.getPlayoffs(),footballService.getTeams(),footballService.getVariousTexts(),session_selected_broadcaster,match);
				break;
			case "POPULATE-FF-TEAMS":
				populateFFTeams(print_writer,valueToProcess.split(",")[1],footballService.getTeams(), match, session_selected_broadcaster);
				break;
			case "POPULATE-LT-PLAYER_STATS":
				populateLTPlayerStats(print_writer, valueToProcess.split(",")[1] ,Integer.valueOf(valueToProcess.split(",")[2]),footballService.getTeams(),
						KabaddiFunctions.processAllPlayerStats(footballService), match , session_selected_broadcaster);
				break;
			case "POPULATE-LT-PROMO":
				populateLTMatchPromoSingle(print_writer, valueToProcess.split(",")[1] ,Integer.valueOf(valueToProcess.split(",")[2]),footballService.getTeams(),
						footballService.getFixtures(),footballService.getGrounds(),match , session_selected_broadcaster);
				break;
			case "POPULATE-FF-PROMO":
				populateMatchPromoSingle(print_writer, valueToProcess.split(",")[1] ,Integer.valueOf(valueToProcess.split(",")[2]),footballService.getTeams(),
						footballService.getFixtures(),footballService.getGrounds(),match , session_selected_broadcaster);
				break;
			case "POPULATE-PENALTY":
				populateLtPenalty(print_writer, valueToProcess.split(",")[1],valueToProcess, footballService,match,clock, session_selected_broadcaster);
				break;
			case "POPULATE-CHANGE_PENALTY":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change_Out START \0");
				TimeUnit.MILLISECONDS.sleep(800);
				populateLtPenaltyChange(print_writer, match,session_selected_broadcaster);
				TimeUnit.MILLISECONDS.sleep(800);
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change_In START \0");
				break;
			case "POPULATE-L3-NAMESUPER":
				//System.out.println("Value1 : " + valueToProcess.split(",")[1] + "Value2 : " + valueToProcess.split(",")[2]);
				for(NameSuper ns : footballService.getNameSupers()) {
					  if(ns.getNamesuperId() == Integer.valueOf(valueToProcess.split(",")[2])) {
						  populateNameSuper(print_writer, valueToProcess.split(",")[1], ns, match, session_selected_broadcaster);
					  }
					}
				break;
			case "POPULATE-L3-NAMESUPER-PLAYER":
				populateNameSuperPlayer(print_writer, valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]), valueToProcess.split(",")[3], Integer.valueOf(valueToProcess.split(",")[4]), match, session_selected_broadcaster);
				break;
			case "POPULATE-L3-NAMESUPER-CARD":
				populateNameSuperCard(print_writer, valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]), valueToProcess.split(",")[3], Integer.valueOf(valueToProcess.split(",")[4]), match, session_selected_broadcaster);
				break;
			case "POPULATE-L3-STAFF":
				for(Staff st : footballService.getStaffs()) {
					  if(st.getStaffId() == Integer.valueOf(valueToProcess.split(",")[2])) {
						  populateStaff(print_writer, valueToProcess.split(",")[1], st,footballService.getTeams(), match, session_selected_broadcaster);
					  }
					}
				break;
			case "POPULATE-L3-SUBSTITUTE":
				populateSubstitute(print_writer, valueToProcess.split(",")[1],Integer.valueOf(valueToProcess.split(",")[2]),valueToProcess.split(",")[3],
						footballService.getAllPlayer(),footballService.getTeams(), match, session_selected_broadcaster);
				break;
			case "POPULATE-LOF-LINEUP":
				if(which_graphics_onscreen.equalsIgnoreCase("LOF_LINEUP")) {
					which_side = 2;
				}else {
					which_side = 1;
				}
				TeamId = Integer.valueOf(valueToProcess.split(",")[2]);
				populateLofLineUp(print_writer, valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]),footballService.getFormations(),
						footballService.getTeams(),footballService.getVariousTexts(),match, session_selected_broadcaster);
				
				if(which_side == 1) {
					print_writer.println("-1 RENDERER PREVIEW SCENE*" + valueToProcess.split(",")[1] + " C:/Temp/Preview.png In 2.000 \0");
				}else {
					print_writer.println("-1 RENDERER PREVIEW SCENE*" + valueToProcess.split(",")[1] + " C:/Temp/Preview.png Change 2.000 \0");
				}
				break;
				
			case "POPULATE-LOF-LEADERBOARD":
				if(which_graphics_onscreen.equalsIgnoreCase("LOF_LEADERBOARD")) {
					which_side = 2;
				}else {
					which_side = 1;
				}
				TeamId = Integer.valueOf(valueToProcess.split(",")[3]);
				top_stats_value = valueToProcess.split(",")[4];
				for(LeaderBoard leaderboard : KabaddiFunctions.processAllLeaderBoards(footballService)) {
					if(leaderboard.getLeaderboardId() == Integer.valueOf(valueToProcess.split(",")[2])) {
						leaderBoard = leaderboard;
						populateLofLeaderBoard(print_writer, valueToProcess.split(",")[1], leaderboard, footballService.getTeams(), 
								match, session_selected_broadcaster);
					}
				}
				
				if(valueToProcess.split(",")[4].equalsIgnoreCase("with_photo")) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$Out$Select_EventLogo*FUNCTION*Omo*vis_con SET 0 \0");
					String pos_perview = "";
					pos_perview = "Side" + which_side + "$Anim_Highlight$" + Integer.valueOf(valueToProcess.split(",")[3]) + "In 1.000";
					
					for(int i=5;i>=Integer.valueOf(valueToProcess.split(",")[3]);i--) {
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Side" + which_side + "$Position_Change$" + i + "$In START \0");
						pos_perview = pos_perview + " Side" + which_side + "$Position_Change$" + i + "$In 1.000";
					}
					if(which_side == 1) {
						print_writer.println("-1 RENDERER PREVIEW SCENE*" + valueToProcess.split(",")[1] + " C:/Temp/Preview.png In 2.000 " + pos_perview + "\0");
					}else {
						print_writer.println("-1 RENDERER PREVIEW SCENE*" + valueToProcess.split(",")[1] + " C:/Temp/Preview.png Change 2.000 " + pos_perview + "\0");
					}
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$Out$Select_EventLogo*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					if(which_side == 1) {
						print_writer.println("-1 RENDERER PREVIEW SCENE*" + valueToProcess.split(",")[1] + " C:/Temp/Preview.png In 2.000 \0");
					}else {
						print_writer.println("-1 RENDERER PREVIEW SCENE*" + valueToProcess.split(",")[1] + " C:/Temp/Preview.png Change 2.000 \0");
					}
				}
				AnimateInLeaderBoardPlayer(print_writer,valueToProcess.split(",")[4],Integer.valueOf(valueToProcess.split(",")[3]));
				break;
			case "POPULATE-CHETTRI_CHANGE_ON":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change START \0");
				break;
				
			case "POPULATE-LOF-VERTICAL_FLIPPER": case "POPULATE-VERTICAL_CHANGE_ON":
				if(whatToProcess.equalsIgnoreCase("POPULATE-LOF-VERTICAL_FLIPPER")) {
					TeamId = Integer.valueOf(valueToProcess.split(",")[2]);
					populateLofVerticalFlipper(print_writer,valueToProcess.split(",")[1], 1, Integer.valueOf(valueToProcess.split(",")[2]),footballService.getFormations(),
							footballService.getTeams(),match, session_selected_broadcaster);
					print_writer.println("-1 RENDERER PREVIEW SCENE*" + "/Default/VerticalFlipper" + " C:/Temp/Preview.png In 2.200 \0");
					count = count + 1;
				}else {
					if((formation.split("-").length == 3 && count == 4)||(formation.split("-").length == 4 && count == 5)||(formation.split("-").length == 5 && count == 6)) {
						this.status = KabaddiUtil.END;
						return status;
					}
					else {
						populateLofVerticalFlipper(print_writer,"/Default/VerticalFlipper", 2, 0,footballService.getFormations(),footballService.getTeams(),
								match, session_selected_broadcaster);
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change START \0");
						
						TimeUnit.MILLISECONDS.sleep(2200);
						
						populateLofVerticalFlipper(print_writer,"/Default/VerticalFlipper", 1, 0,footballService.getFormations(),footballService.getTeams(),
								match, session_selected_broadcaster);
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
						count = count + 1;
					}
				}
				break;
			case "POPULATE-FF-FIXTUREANDRESULT":
				populateFF_FixtureAndResult(print_writer, valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]),
						KabaddiFunctions.processAllFixtures(footballService),match, session_selected_broadcaster);
				break;
			case "POPULATE-FF-PLAYINGXI":
				populatePlayingXI(print_writer, valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]),valueToProcess.split(",")[3],
						footballService.getFormations(), footballService.getTeams(),footballService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-HOMESUB":
				print_writer.println("-1 RENDERER PREVIEW SCENE*" + "/Default/FullFrames" + " C:/Temp/Preview.png FF_In 0.020 LineUp$Team1$DataIn 2.520 LineUp$Team1$Change 2.100 \0");
				break;
			case "POPULATE-AWAYXI":
				print_writer.println("-1 RENDERER PREVIEW SCENE*" + "/Default/FullFrames" + " C:/Temp/Preview.png FF_In 0.020 LineUp$Team1$DataIn 0.000 LineUp$Team2$DataIn 2.520 \0");
				break;
			case "POPULATE-AWAYSUB":
				print_writer.println("-1 RENDERER PREVIEW SCENE*" + "/Default/FullFrames" + " C:/Temp/Preview.png FF_In 0.020 LineUp$Team2$DataIn 2.520 LineUp$Team2$Change 2.100 \0");
				break;	
			case "POPULATE-L3-HEATMAP":
				populateHeatMapPeakDistance(print_writer, valueToProcess.split(",")[1], Integer.valueOf(valueToProcess.split(",")[2]),valueToProcess.split(",")[3] ,
						Integer.valueOf(valueToProcess.split(",")[4]),footballService.getAllPlayer(),match, session_selected_broadcaster);
				break;
			case "POPULATE-L3-SCOREUPDATE":
				populateScoreUpdate(print_writer, valueToProcess.split(",")[1], footballService,match,clock, session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCHID":
				populateLtMatchId(print_writer, valueToProcess.split(",")[1], footballService,match,clock, session_selected_broadcaster);
				break;
			case "POPULATE-FF-TOURNAMENT_STATS":
				populateTournamentStats(print_writer, valueToProcess.split(",")[1], match, session_selected_broadcaster);
				break;
				
			case "POPULATE-OFFICIALS":
				populateOfficials(print_writer, valueToProcess.split(",")[1],footballService.getOfficials(),match, session_selected_broadcaster);
				break;
			case "POPULATE-HIGHLIGHT_SCOREBUG":
				populateBugHighlight(print_writer,valueToProcess.split(",")[1],match,clock, session_selected_broadcaster);
				break;
			 case "POPULATE-L3-BUG-DB":
				 for(Bugs bug : footballService.getBugs()) {
					  if(bug.getBugId() == Integer.valueOf(valueToProcess.split(",")[2])) {
							populateBug(print_writer,valueToProcess.split(",")[1],bug,match,clock, session_selected_broadcaster);
					  }
				}
				break;
			case "POPULATE-FF-MATCHSTATS":
				populateMatchStats(print_writer,valueToProcess.split(",")[1], footballService,match,clock, session_selected_broadcaster);
				break;
			case "POPULATE-DOUBLE_PROMO":
				populateMatchDoublePromo(print_writer, valueToProcess.split(",")[1],valueToProcess.split(",")[2], match,footballService.getFixtures(),
						footballService.getTeams(),footballService.getGrounds(), session_selected_broadcaster);
				break;
			case "POPULATE-LT-RESULT":
				populateMatchResult(print_writer, valueToProcess.split(",")[1],Integer.valueOf(valueToProcess.split(",")[2]),footballService.getFixtures(),footballService.getTeams(),footballService.getGrounds(),
						session_selected_broadcaster,match);
				break;
			case "POPULATE-FIXTURES":
				populateFixtures(print_writer, valueToProcess.split(",")[1],valueToProcess.split(",")[2],valueToProcess.split(",")[3],footballService.getFixtures(),footballService.getTeams(),footballService.getGrounds(),
						session_selected_broadcaster,match);
				break;
			case "POPULATE-QUAIFIERS":
				populateQulifiers(print_writer, valueToProcess.split(",")[1],session_selected_broadcaster,match);
				break;
			case "POPULATE-POINTS_TABLE2":
				LeagueTable league_table1 = null;
				LeagueTable league_table2 = null;
				if(valueToProcess.split(",")[2].equalsIgnoreCase("SemiFinal1")) {
					if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableA" + ".XML").exists()) {
						league_table1 = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
								new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableA" + ".XML"));
					}
					if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableB" + ".XML").exists()) {
						league_table2 = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
								new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableB" + ".XML"));
					}
					
				}else if(valueToProcess.split(",")[2].equalsIgnoreCase("SemiFinal2")) {
					if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableC" + ".XML").exists()) {
						league_table1 = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
								new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableC" + ".XML"));
					}
					if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableD" + ".XML").exists()) {
						league_table2 = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
								new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTableD" + ".XML"));
					}
				}
				populatePointsTableGrp(print_writer, valueToProcess.split(",")[1],valueToProcess.split(",")[2],league_table1.getLeagueTeams(),league_table2.getLeagueTeams(),
						footballService.getTeams(),session_selected_broadcaster,match);
				break;
			case "POPULATE-ROAD-TO-FINAL":
				LeagueTable league_table3 = null;
				LeagueTable league_table4 = null;
				
							
				if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTable" + match.getHomeTeam().getTeamGroup() + ".XML").exists()) {
					league_table3 = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
							new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTable" + match.getHomeTeam().getTeamGroup() + ".XML"));
				}
				if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTable" + match.getAwayTeam().getTeamGroup() + ".XML").exists()) {
					league_table4 = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
							new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + "LeagueTable" + match.getAwayTeam().getTeamGroup() + ".XML"));
				}
				
				populateRoadToFinal(print_writer, valueToProcess.split(",")[1],league_table3.getLeagueTeams(),league_table4.getLeagueTeams(),
						footballService.getTeams(),session_selected_broadcaster,match);
				break;	
			case "POPULATE-POINTS_TABLE":
				LeagueTable league_table = null;
				
				if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + valueToProcess.split(",")[2] + ".XML").exists()) {
					league_table = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
							new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.LEAGUE_TABLE_DIRECTORY + valueToProcess.split(",")[2] + ".XML"));
				}
				
				populatePointsTable(print_writer, valueToProcess.split(",")[1],valueToProcess.split(",")[2],league_table.getLeagueTeams(),footballService.getTeams(),
						session_selected_broadcaster,match);
				break;
			}
		case "TEAMFIXTURE_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(footballService.getTeams()).toString();
		case "NAMESUPER_GRAPHICS-OPTIONS": 
			return JSONArray.fromObject(footballService.getNameSupers()).toString();
		case "BUG_DB_GRAPHICS-OPTIONS":case "DB_GRAPHICS":
			return JSONArray.fromObject(footballService.getBugs()).toString();
		case "STAFF_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(footballService.getStaffs()).toString();
		case "PROMO_GRAPHICS-OPTIONS": case "LTPROMO_GRAPHICS-OPTIONS": case "SCOREBUGPROMO_GRAPHICS-OPTIONS": case "RESULT_PROMO_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(KabaddiFunctions.processAllFixtures(footballService)).toString();
		case "LT_PLAYER_STATS_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(KabaddiFunctions.processAllPlayerStats(footballService)).toString();
		case "LEADERBOARD_GRAPHICS-OPTIONS":
			return JSONArray.fromObject(KabaddiFunctions.processAllLeaderBoards(footballService)).toString();
			
		case "ANIMATE-IN-SCOREBUG": case "ANIMATE-IN-SPONSOR": case "ANIMATE-IN-SUBS_CHANGE_ON": case "ANIMATE-IN-HIGHLIGHT_SCOREBUG": case "ANIMATE-IN-BUG-DB":
		case "ANIMATE-IN-MATCHID": case "ANIMATE-IN-PROMO": case "ANIMATE-IN-PLAYINGXI": case "ANIMATE-IN-HOMESUB": case "ANIMATE-IN-AWAYXI": case "ANIMATE-IN-AWAYSUB":
		case "ANIMATE-IN-MATCHSTATUS": case "ANIMATE-IN-MATCHSTATS": case "ANIMATE-IN-DOUBLE_PROMO": case "ANIMATE-IN-FF_TEAMS": case "ANIMATE-IN-POINTS_TABLE":
		case "ANIMATE-IN-FIXTURES": case "ANIMATE-IN-QUAIFIERS": case "ANIMATE-IN-POINTS_TABLE2": case "ANIMATE-IN-PLAYOFFS":
		case "ANIMATE-IN-SCOREUPDATE": case "ANIMATE-IN-LT_MATCHID": case "ANIMATE-IN-NAMESUPER_CARD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-NAMESUPERDB":
		case "ANIMATE-IN-SUBSTITUTE": case "ANIMATE-IN-OFFICIALS": case "ANIMATE-IN-HEATMAP": case "ANIMATE-IN-TOP_STATS": case "ANIMATE-IN-STAFF":
		case "ANIMATE-IN-PENALTY": case "ANIMATE-IN-LTPROMO": case "ANIMATE-IN-RESULT": case "ANIMATE-IN-ROAD-TO-FINAL": case "ANIMATE-IN-TEAMFIXTURE":
		case "ANIMATE-IN-LOF_LINEUP": case "ANIMATE-IN-VERTICAL_FLIPPER": case "ANIMATE-IN-LOF_LEADERBOARD": case "ANIMATE-IN-TOURNAMENT_STATS":
		case "ANIMATE-IN-TEAM_COMPARISON": case "ANIMATE-IN-CHETTRI": case "ANIMATE-IN-FF_HEADTOHEAD": case "ANIMATE-IN-FIXTUREANDRESULT": 
		case "ANIMATE-IN-TEAM_TOUCH": case "ANIMATE-IN-AVG_FORMATION": case "ANIMATE-IN-LT_PLAYER_STATS":
		
		case "ANIMATE-ATTACKING_ZONE": case "ANIMATE-IN-PLAYER_TOUCH_MAP":
			
		case "ANIMATE-SUB_CHANGE_ON": case "ANIMATE-IN-FLAG":
		case "CLEAR-ALL": 
		case "ANIMATE-OUT-SCOREBUG": case "ANIMATE-OUT-EXTRA_TIME": case "ANIMATE-OUT-SCOREBUG_STAT": case"ANIMATE-OUT-RED_CARD":
		case "ANIMATE-OUT": 
			
			switch (whatToProcess.toUpperCase()) {
			case "ANIMATE-IN-SCOREBUG":
				AnimateInGraphics(print_writer, "SCOREBUG");
				is_infobar = true;
				scorebug.setScorebug_on_screen(true);
				scorebug.setScorebug_sponsor_on_screen(false);
				scorebug.setScorebug_ET_on_screen(false);
				scorebug.setScorebug_flag_on_screen(false);
				break;
			case "ANIMATE-IN-TEST":
				AnimateInGraphics(print_writer, "TEST");
				which_graphics_onscreen = "TEST";
				break;
			case "ANIMATE-IN-HIGHLIGHT_SCOREBUG":
				AnimateInGraphics(print_writer, "HIGHLIGHT_SCOREBUG");
				which_graphics_onscreen = "HIGHLIGHT_SCOREBUG";
				break;
			case "ANIMATE-IN-BUG-DB":
				AnimateInGraphics(print_writer, "BUG-DB");
				which_graphics_onscreen = "BUG-DB";
				break;
			case "ANIMATE-IN-QUAIFIERS":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
				which_graphics_onscreen = "QUAIFIERS";
				break;
			case "ANIMATE-SUB_CHANGE_ON":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In CONTINUE \0");
				break;
			case "ANIMATE-IN-PLAYOFFS":
				AnimateInGraphics(print_writer, "PLAYOFFS");
				which_graphics_onscreen = "PLAYOFFS";
				break;
			case "ANIMATE-IN-SPONSOR":
				if(scorebug.isScorebug_flag_on_screen() == false) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Sponsor_In START \0");
					scorebug.setScorebug_flag_on_screen(true);
				}
				else if(scorebug.isScorebug_flag_on_screen() == true) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Sponsor_Out START \0");
					scorebug.setScorebug_flag_on_screen(false);
				}
				break;
			case "ANIMATE-IN-FLAG":
				if(scorebug.isScorebug_sponsor_on_screen() == false) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flag_In START \0");
					scorebug.setScorebug_sponsor_on_screen(true);
				}
				else if(scorebug.isScorebug_sponsor_on_screen() == true) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flag_Out START \0");
					scorebug.setScorebug_sponsor_on_screen(false);
				}
				break;
			case "ANIMATE-IN-SUBS_CHANGE_ON":
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Substitutes$Change START \0");
				break;
			case "ANIMATE-IN-FF_TEAMS":
				AnimateInGraphics(print_writer, "FF_TEAMS");
				which_graphics_onscreen = "FF_TEAMS";
				break;
			case "ANIMATE-IN-MATCHID":
				AnimateInGraphics(print_writer, "MATCHID");
				which_graphics_onscreen = "MATCHID";
				break;
			case "ANIMATE-IN-FF_HEADTOHEAD":
				AnimateInGraphics(print_writer, "FF_HEADTOHEAD");
				which_graphics_onscreen = "FF_HEADTOHEAD";
				break;
			case "ANIMATE-IN-FIXTURES":
				AnimateInGraphics(print_writer, "FIXTURES");
				which_graphics_onscreen = "FIXTURES";
				break;
			case "ANIMATE-IN-POINTS_TABLE": case "ANIMATE-IN-POINTS_TABLE2": case "ANIMATE-IN-ROAD-TO-FINAL":
				AnimateInGraphics(print_writer, "POINTS_TABLE");
				which_graphics_onscreen = "POINTS_TABLE";
				break;
			case "ANIMATE-IN-MATCHSTATUS":
				AnimateInGraphics(print_writer, "MATCHSTATUS");
				which_graphics_onscreen = "MATCHSTATUS";
				break;
			case "ANIMATE-IN-TOURNAMENT_STATS":
				AnimateInGraphics(print_writer, "TOURNAMENT_STATS");
				which_graphics_onscreen = "TOURNAMENT_STATS";
				break;
			case "ANIMATE-IN-TEAM_COMPARISON":
				AnimateInGraphics(print_writer, "TEAM_COMPARISON");
				which_graphics_onscreen = "TEAM_COMPARISON";
				break;
				
			case "ANIMATE-IN-MATCHSTATS":
				AnimateInGraphics(print_writer, "MATCHSTATS");
				which_graphics_onscreen = "MATCHSTATS";
				break;
			case "ANIMATE-IN-PROMO":
				AnimateInGraphics(print_writer, "MATCHSINGLEPROMO");
				which_graphics_onscreen = "MATCHSINGLEPROMO";
				break;
			case "ANIMATE-IN-DOUBLE_PROMO":
				AnimateInGraphics(print_writer, "DOUBLE_PROMO");
				which_graphics_onscreen = "DOUBLE_PROMO";
				break;
			case "ANIMATE-IN-PENALTY":
				AnimateInGraphics(print_writer, "PENALTY");
				which_graphics_onscreen = "PENALTY";
				break;
			case "ANIMATE-IN-TEAMFIXTURE":
				AnimateInGraphics(print_writer, "TEAMFIXTURE");
				which_graphics_onscreen = "TEAMFIXTURE";
				break;
			case "ANIMATE-IN-NAMESUPER_CARD":
				AnimateInGraphics(print_writer, "NAMESUPER_CARD");
				which_graphics_onscreen = "NAMESUPER_CARD";
				break;
			case "ANIMATE-IN-NAMESUPER":
				AnimateInGraphics(print_writer, "NAMESUPER");
				which_graphics_onscreen = "NAMESUPER";
				break;
			case "ANIMATE-IN-STAFF":
				AnimateInGraphics(print_writer, "STAFF");
				which_graphics_onscreen = "STAFF";
				break;
			case "ANIMATE-IN-NAMESUPERDB":
				AnimateInGraphics(print_writer, "NAMESUPERDB");
				which_graphics_onscreen = "NAMESUPERDB";
				break;
			case "ANIMATE-IN-OFFICIALS":
				AnimateInGraphics(print_writer, "OFFICIALS");
				which_graphics_onscreen = "OFFICIALS";
				break;
			case "ANIMATE-IN-RESULT":
				AnimateInGraphics(print_writer, "RESULT");
				which_graphics_onscreen = "RESULT";
				break;
			case "ANIMATE-IN-SUBSTITUTE":
				AnimateInGraphics(print_writer, "SUBSTITUTE");
				which_graphics_onscreen = "SUBSTITUTE";
				break;
			case "ANIMATE-IN-HOMESUB":
				AnimateInGraphics(print_writer, "HOMESUB");
				which_graphics_onscreen = "PLAYINGXI";
				break;
			case "ANIMATE-IN-AWAYXI":
				AnimateInGraphics(print_writer, "AWAYXI");
				which_graphics_onscreen = "PLAYINGXI";
				break;
			case "ANIMATE-IN-AWAYSUB":
				AnimateInGraphics(print_writer, "AWAYSUB");
				which_graphics_onscreen = "PLAYINGXI";
				break;
			case "ANIMATE-IN-PLAYINGXI":
				AnimateInGraphics(print_writer, "PLAYINGXI");
				which_graphics_onscreen = "PLAYINGXI";
				break;
			case "ANIMATE-IN-FIXTUREANDRESULT":
				AnimateInGraphics(print_writer, "FIXTUREANDRESULT");
				which_graphics_onscreen = "FIXTUREANDRESULT";
				break;
			case "ANIMATE-IN-TEAM_TOUCH":
				AnimateInGraphics(print_writer, "TEAM_TOUCH");
				which_graphics_onscreen = "TEAM_TOUCH";
				break;
			case "ANIMATE-IN-LOF_LINEUP":
				if(which_side == 1) {
					AnimateInGraphics(print_writer, "LOF_LINEUP");
				}else {
					ChangeOnGraphics(print_writer, "LOF_LINEUP");
					TimeUnit.MILLISECONDS.sleep(2000);
					which_side = 1;
					populateLofLineUp(print_writer, "/Default/Lof_LineUp", TeamId, footballService.getFormations(), footballService.getTeams(),
							footballService.getVariousTexts(), match, session_selected_broadcaster);
					
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
				}
				which_graphics_onscreen = "LOF_LINEUP";
				break;
			case "ANIMATE-IN-VERTICAL_FLIPPER":
				AnimateInGraphics(print_writer, "VERTICAL_FLIPPER");
				which_graphics_onscreen = "VERTICAL_FLIPPER";
				break;
			case "ANIMATE-IN-CHETTRI":
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
				which_graphics_onscreen = "CHETTRI";
				break;
			case"ANIMATE-ATTACKING_ZONE":
				AnimateInGraphics(print_writer, "ATTACKING_ZONE");
				which_graphics_onscreen = "ATTACKING_ZONE";
				break;
			case "ANIMATE-IN-PLAYER_TOUCH_MAP":
				AnimateInGraphics(print_writer, "PLAYER_TOUCH_MAP");
				which_graphics_onscreen = "PLAYER_TOUCH_MAP";
				break;
			case "ANIMATE-IN-LOF_LEADERBOARD":
				if(which_side == 1) {
					AnimateInGraphics(print_writer, "LOF_LEADERBOARD");
				}else {
					ChangeOnGraphics(print_writer, "LOF_LEADERBOARD");
					TimeUnit.MILLISECONDS.sleep(2000);
					which_side = 1;
					populateLofLeaderBoard(print_writer, "/Default/Lof_LeaderBoard", leaderBoard, footballService.getTeams(), 
							match, session_selected_broadcaster);
					
					TimeUnit.MILLISECONDS.sleep(500);
					AnimateInLeaderBoardPlayer(print_writer,top_stats_value,TeamId);
					TimeUnit.MILLISECONDS.sleep(1000);
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
				}
				
				which_graphics_onscreen = "LOF_LEADERBOARD";
				break;
			case "ANIMATE-IN-SCOREUPDATE":
				if(is_infobar == true && scorebug.isScorebug_on_screen() == true) {
					AnimateOutGraphics(print_writer, "SCOREBUG");
					scorebug.setScorebug_on_screen(false);
					TimeUnit.MILLISECONDS.sleep(500);
				}
				AnimateInGraphics(print_writer, "SCOREUPDATE");
				TimeUnit.MILLISECONDS.sleep(500);
				if(match.getHomeTeamScore() > 0 || match.getAwayTeamScore() > 0) {
					if(match.getHomeTeamScore() > 4 || match.getAwayTeamScore() > 4) {
						//processAnimation(session_socket, "Scorer3Line_In", "START", session_selected_broadcaster, 2);
					}else if(match.getHomeTeamScore() > 2 || match.getAwayTeamScore() > 2) {
						//processAnimation(session_socket, "Scorer2Line_In", "START", session_selected_broadcaster, 2);
					}else {
						//processAnimation(session_socket, "Scorer1Line_In", "START", session_selected_broadcaster, 2);
					}
				}
				which_graphics_onscreen = "SCOREUPDATE";
				break;
			case "ANIMATE-IN-LTPROMO":
				AnimateInGraphics(print_writer, "LTPROMO");
				which_graphics_onscreen = "LTPROMO";
				break;
			case "ANIMATE-IN-LT_PLAYER_STATS":
				AnimateInGraphics(print_writer, "LT_PLAYER_STATS");
				which_graphics_onscreen = "LT_PLAYER_STATS";
				break;
			case "ANIMATE-IN-LT_MATCHID":
				AnimateInGraphics(print_writer, "LT_MATCHID");
				which_graphics_onscreen = "LT_MATCHID";
				break;
			case "CLEAR-ALL":
				print_writer.println("-1 SCENE CLEANUP\0");
				print_writer.println("-1 IMAGE CLEANUP\0");
				print_writer.println("-1 GEOM CLEANUP\0");
				print_writer.println("-1 FONT CLEANUP\0");

				print_writer.println("-1 IMAGE INFO\0");
				print_writer.println("-1 RENDERER SET_OBJECT SCENE*" + valueToProcess.split(",")[0] + "\0");

				print_writer.println("-1 RENDERER INITIALIZE\0");
				print_writer.println("-1 RENDERER*SCENE_DATA INITIALIZE\0");
				print_writer.println("-1 RENDERER*UPDATE SET 0\0");
				print_writer.println("-1 RENDERER*STAGE SHOW 0.0\0");

				print_writer.println("-1 RENDERER*UPDATE SET 1\0");

				print_writer.println("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/ScoreBug-Single\0");

				print_writer.println("-1 RENDERER*FRONT_LAYER INITIALIZE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*UPDATE SET 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE SHOW 0.0\0");

				print_writer.println("-1 RENDERER*FRONT_LAYER*UPDATE SET 1\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*/Default/FullFrames\0");
	           	
				print_writer.println("-1 RENDERER*BACK_LAYER INITIALIZE\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*UPDATE SET 0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE SHOW 0.0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Reset START \0");
               
				print_writer.println("-1 RENDERER*BACK_LAYER*UPDATE SET 1\0");
	               
				print_writer.println("-1 SCENE CLEANUP\0");
				print_writer.println("-1 IMAGE CLEANUP\0");
				print_writer.println("-1 GEOM CLEANUP\0");
				print_writer.println("-1 FONT CLEANUP\0");
				which_graphics_onscreen = "";
				which_Right_LBand_onscreen ="";
				which_Bottom_LBand_onscreen="";
				is_infobar = false;
				scorebug.setScorebug_on_screen(false);
				count = 0;
				this.status = "";
				break;
			
			case "ANIMATE-OUT-SCOREBUG":
				if(is_infobar == true) {
					AnimateOutGraphics(print_writer, "SCOREBUG");
					is_infobar = false;
					scorebug.setScorebug_on_screen(false);
					scorebug.setScorebug_sponsor_on_screen(false);
					scorebug.setScorebug_ET_on_screen(false);
				}
				break;
			case "ANIMATE-OUT-SCOREBUG_STAT":
				if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Stats_Out START \0");
					scorebug.setLast_scorebug_stat("");scorebug.setScorebug_stat("");
				}
				else if(scorebug.getLast_scorebug_card_goal() != null && !scorebug.getLast_scorebug_card_goal().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Cards_Out START \0");
					scorebug.setLast_scorebug_card_goal("");scorebug.setScorebug_card_goal("");
				}
				else if(scorebug.getLast_scorebug_promo() != null && !scorebug.getLast_scorebug_promo().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Promo_Out START \0");
					scorebug.setLast_scorebug_promo("");scorebug.setScorebug_promo("");
				}
				else if(scorebug.getLast_scorebug_subs() != null && !scorebug.getLast_scorebug_subs().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Substitutes$Subtitutes_Out START \0");
					scorebug.setLast_scorebug_subs("");scorebug.setScorebug_subs("");
				}
				else if(scorebug.getLast_scorebug_team_stat() != null && !scorebug.getLast_scorebug_team_stat().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*TeamStats_Out START \0");
					scorebug.setLast_scorebug_team_stat("");scorebug.setScorebug_team_stat("");
				}
				else if(scorebug.getLast_scorebug_player_stat() != null && !scorebug.getLast_scorebug_player_stat().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatsImage_Out START \0");
					scorebug.setLast_scorebug_player_stat("");scorebug.setScorebug_player_stat("");
				}
				else if(scorebug.getLast_scorebug_headTohead_stat() != null && !scorebug.getLast_scorebug_headTohead_stat().isEmpty()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*HeadToHead_Out START \0");
					scorebug.setLast_scorebug_headTohead_stat("");scorebug.setScorebug_headTohead_stat("");
				}
				break;
			case"ANIMATE-OUT-RED_CARD":	
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*RedCards_Out START \0");
				break;
			case "ANIMATE-OUT-EXTRA_TIME":
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*AddedMin_Out START \0");
				break;
			case "ANIMATE-OUT":
				switch(which_graphics_onscreen) {
				case "SCOREUPDATE":
					AnimateOutGraphics(print_writer, which_graphics_onscreen);
					if(is_infobar == true && scorebug.isScorebug_on_screen() == false) {
						TimeUnit.MILLISECONDS.sleep(500);
						AnimateInGraphics(print_writer, "SCOREBUG");
						scorebug.setScorebug_on_screen(true);
					}
					which_graphics_onscreen = "";
					break;
				case "MATCHID": case "PLAYINGXI": case "LT_MATCHID": case "NAMESUPER_CARD": case "NAMESUPER": case "NAMESUPERDB": 
				case "SUBSTITUTE": case "MATCHSINGLEPROMO": case "MATCHSTATUS": case "OFFICIALS": case "HEATMAP": case "MATCHSTATS": case "TOP_STATS":
				case "STAFF": case "PENALTY": case "DOUBLE_PROMO": case "FF_TEAMS": case "POINTS_TABLE": case "FIXTURES": case "QUAIFIERS": case "LTPROMO":
				case "PLAYOFFS": case "RESULT": case "ROAD-TO-FINAL": case "HIGHLIGHT_SCOREBUG": case "BUG-DB": case "TEAMFIXTURE": case "LOF_LINEUP":
				case "VERTICAL_FLIPPER": case "LOF_LEADERBOARD": case "TOURNAMENT_STATS": case "TEAM_COMPARISON": case "CHETTRI": case "FF_HEADTOHEAD":
				case "ATTACKING_ZONE": case "FIXTUREANDRESULT": case "PLAYER_TOUCH_MAP": case "TEAM_TOUCH": case "AVG_FORMATION": case "LT_PLAYER_STATS":
					AnimateOutGraphics(print_writer, which_graphics_onscreen);
					which_graphics_onscreen = "";
					count = 0;
					this.status = "";
					break;
				}
				break;
			}
			break;
		}
		return null;
	}
	
	public void populateAttacking(PrintWriter print_writer, String viz_scene, Match match, FootballData data, Integer TeamIndex) {
		
		double total_left = 0,total_center = 0, total_right = 0;
		int total = (data.getTeam().get(TeamIndex).getLeft() + data.getTeam().get(TeamIndex).getCenter() + data.getTeam().get(TeamIndex).getRight());
		total_left = ((double)data.getTeam().get(TeamIndex).getLeft() / total)*100;
		total_center = ((double)data.getTeam().get(TeamIndex).getCenter() / total)*100;
		total_right = ((double)data.getTeam().get(TeamIndex).getRight() / total)*100;
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FF_Required$HashTag*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$GroundOut$TouchData$DotGrp$TouchDot1*ACTIVE SET 0 \0");
		
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$LogoGrp$img_Badges*GEOM*TEXTURE*IMAGE SET " 
   				 + data.getTeam().get(TeamIndex).getCode().toUpperCase() + "\0");
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$txt_Header*GEOM*TEXT SET " 
   				 +data.getTeam().get(TeamIndex).getName().toUpperCase()+ "\0");
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " 
   				 +"ATTACKING ZONE"+ "\0");
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + "" + "\0");
   		
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$GroundOut$ZoneValues$Zone1$txt_Value*GEOM*TEXT SET " 
   				 + Math.round(total_left) + "\0");
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$GroundOut$ZoneValues$Zone2$txt_Value*GEOM*TEXT SET " 
   				 + Math.round(total_center) + "\0");
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$GroundOut$ZoneValues$Zone3$txt_Value*GEOM*TEXT SET " 
   				 + Math.round(total_right) + "\0");
   		
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$GroundOut$ZoneValues$Zone1$BarGrp$geom_BarScale_X"
   				+ "*TRANSFORMATION*SCALING*X SET " + df.format(((double)data.getTeam().get(TeamIndex).getLeft() / total)) + "\0");
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$GroundOut$ZoneValues$Zone2$BarGrp$geom_BarScale_X"
   				+ "*TRANSFORMATION*SCALING*X SET " + df.format(((double)data.getTeam().get(TeamIndex).getCenter() / total)) + "\0");
   		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$AttackingZone$AllData$Data$GroundOut$ZoneValues$Zone3$BarGrp$geom_BarScale_X"
   				+ "*TRANSFORMATION*SCALING*X SET " + df.format(((double)data.getTeam().get(TeamIndex).getRight() / total)) + "\0");
   		
   		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 FF_In 2.000 AttackingZone_In 2.500 \0");
	}
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	
	public void AnimateInGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException, IOException {
		
		switch (whichGraphic) {
		case "SCOREBUG":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Reset START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*In START \0");
			break;
		case "SCOREUPDATE": case "LT_MATCHID": case "NAMESUPER_CARD": case "NAMESUPER": case "NAMESUPERDB": case "SUBSTITUTE": case "OFFICIALS":
		case "HEATMAP": case "TOP_STATS": case "STAFF": case "PENALTY": case "LTPROMO": case "RESULT": case "HIGHLIGHT_SCOREBUG": case "BUG-DB":
		case "TEAMFIXTURE":	case "LOF_LINEUP": case "VERTICAL_FLIPPER": case "LOF_LEADERBOARD": case "AVG_FORMATION": case "LT_PLAYER_STATS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
			break;
		case "MATCHID": case "MATCHSINGLEPROMO":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*MatchId_In START \0");
			break;
		case "FF_HEADTOHEAD":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*HeadToHead_In START \0");
			break;
		case "FIXTURES":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Fixtures_In START \0");
			break;
		case "POINTS_TABLE":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PointsTable_In START \0");
			break;
		case "DOUBLE_PROMO":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*DoubleID_In START \0");
			break;
		case "FF_TEAMS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Groups_In START \0");
			break;
		case "MATCHSTATUS": case "TOURNAMENT_STATS": case "TEAM_COMPARISON":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*MatchStats_In START \0");
			break;
		case "PLAYOFFS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayOffs_In START \0");
			break;
		case "MATCHSTATS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*MatchScorers_In START \0");
			break;
		case "PLAYINGXI":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*LineUp$Team1$DataIn START \0");
			break;
		case "FIXTUREANDRESULT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Results_In START \0");
			break;
		case "HOMESUB":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*LineUp$Team1$Change START \0");
			break;
		case "AWAYXI":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*LineUp$Team1$DataOut START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*LineUp$Team2$DataIn START \0");
			break;
		case "AWAYSUB":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*LineUp$Team2$Change START \0");
			break;
		case "ATTACKING_ZONE":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*AttackingZone_In START\0");
			break;
		case "PLAYER_TOUCH_MAP":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayerTouch_In START\0");
			break;
		case "TEAM_TOUCH":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_In START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*TeamTouch_In START\0");
			break;
		}
	}
	public void AnimateOutGraphics(PrintWriter print_writer, String whichGraphic) throws IOException, InterruptedException {
		
		switch (whichGraphic.toUpperCase()) {
		case "SCOREBUG":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "SCOREUPDATE": case "LT_MATCHID": case "NAMESUPER_CARD": case "NAMESUPER": case "NAMESUPERDB": case "SUBSTITUTE": case "OFFICIALS":
		case "HEATMAP": case "TOP_STATS": case "STAFF": case "PENALTY": case "QUAIFIERS": case "LTPROMO": case "RESULT": case "HIGHLIGHT_SCOREBUG":
		case "BUG-DB": case "TEAMFIXTURE": case "LOF_LINEUP": case "VERTICAL_FLIPPER": case "LOF_LEADERBOARD": case "CHETTRI": case "AVG_FORMATION":
		case "LT_PLAYER_STATS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");
			break;
		case "MATCHID": case "MATCHSINGLEPROMO":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*MatchId_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "FF_HEADTOHEAD":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*HeadToHead_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "ATTACKING_ZONE":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*AttackingZone_Out START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START\0");
			break;
		case "PLAYER_TOUCH_MAP":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayerTouch_Out START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START\0");
			break;
		case "TEAM_TOUCH":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*TeamTouch_Out START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START\0");
			break;
		case "PLAYOFFS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayOffs_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;	
		case "FIXTURES":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Fixtures_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "POINTS_TABLE":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PointsTable_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "DOUBLE_PROMO":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*DoubleID_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "FF_TEAMS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Groups_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "MATCHSTATUS": case "TOURNAMENT_STATS": case "TEAM_COMPARISON":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*MatchStats_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "MATCHSTATS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*MatchScorers_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "PLAYINGXI":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*LineUp$Team2$DataOut START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			TimeUnit.MILLISECONDS.sleep(1500);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Reset START \0");
			break;
		case "FIXTUREANDRESULT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Results_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*FF_Out START \0");
			TimeUnit.MILLISECONDS.sleep(200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		}
	}
	public void ChangeOnGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException, IOException {
		switch (whichGraphic) {
		case "LOF_LINEUP": case "LOF_LEADERBOARD": case "TOP_STATS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Change START \0");
			
			if(whichGraphic.equalsIgnoreCase("LOF_LEADERBOARD")) {
				TimeUnit.MILLISECONDS.sleep(2200);
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Side1 SHOW 0.0 \0");
			}
			break;
			
		}
	}
	public void AnimateInLeaderBoardPlayer(PrintWriter print_writer, String PhotoType, int PlayerNumber) throws InterruptedException, IOException {
		
		if(PhotoType.equalsIgnoreCase("with_photo")) {
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Side" + which_side + "$Anim_Highlight$" + PlayerNumber + "$In START \0");
			
			for(int i=5;i>=Integer.valueOf(PlayerNumber);i--) {
				print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Side" + which_side + "$Position_Change$" + i + "$In START \0");
			}
		}
		
	}
	
	public ScoreBug populateScoreBug(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug,String viz_sence_path,Match match, String selectedbroadcaster) throws IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$Seperator$AllScoreGrp$txt_HomeScore*GEOM*TEXT SET " + 
					match.getHomeTeamScore() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$Seperator$AllScoreGrp$txt_AwayScore*GEOM*TEXT SET " + 
					match.getAwayTeamScore() + "\0");
			
			if(is_this_updating == false) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$TeamGrp1$txt_Name*GEOM*TEXT SET " + 
						match.getHomeTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$TeamGrp2$txt_Name*GEOM*TEXT SET " + 
						match.getAwayTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$TeamGrp1$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$TeamGrp2$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				
			}
		}
		return scorebug;
	}
	public ScoreBug populateScoreBugStats(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug,int Homedata,int Awaydata ,Match match, String selectedbroadcaster) 
			throws MalformedURLException, IOException, CsvException, InterruptedException {
		
		switch(scorebug.getScorebug_stat().toUpperCase()) {
		case KabaddiUtil.YELLOW:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + "YELLOW CARD" + "\0");
			break;
		case KabaddiUtil.RED:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + "RED CARD" + "\0");
			break;
		case KabaddiUtil.OFF_SIDE:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + "OFFSIDES" + "\0");
			break;
		case KabaddiUtil.SHOTS:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + KabaddiUtil.SHOTS + "\0");
			break;
		case KabaddiUtil.POSSESSION:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + KabaddiUtil.POSSESSION + " %" + "\0");
			break;
		case KabaddiUtil.SHOTS_ON_TARGET:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + "SHOTS ON TARGET" + "\0");
			break;
		case KabaddiUtil.CORNERS:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + KabaddiUtil.CORNERS + "\0");
			break;
		case KabaddiUtil.TACKLES:
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_StatHead*GEOM*TEXT SET " + KabaddiUtil.TACKLES + "\0");
			break;
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_HomeStatValue*GEOM*TEXT SET " + 
				Homedata + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsGrp$StatDataGrp$txt_AwayStatValue*GEOM*TEXT SET " + 
				Awaydata + "\0");
		
		scorebug.setLast_scorebug_stat(scorebug.getScorebug_stat().toUpperCase());
		return scorebug;
	}
	public ScoreBug populateRedcard(PrintWriter print_writer,boolean is_this_updating, ScoreBug scorebug,int Homedata,int Awaydata, Match match, String selectedbroadcaster) throws MalformedURLException, IOException, CsvException {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$TeamGrp1$CardGrp$SelectCardNumber*FUNCTION*Omo*vis_con SET " + Homedata + "\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$MainScorePart$TeamGrp2$CardGrp$SelectCardNumber*FUNCTION*Omo*vis_con SET " + Awaydata + "\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*RedCards_In START \0");
		
		return scorebug;
	}
	public ScoreBug populateETONE_TWO(PrintWriter print_writer,boolean is_this_updating, ScoreBug scorebug,Match match, String selectedbroadcaster) throws MalformedURLException, IOException, CsvException {
		
		if(scorebug.isScorebug_ET_on_screen() == false) {
			if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.EXTRA1) || 
					match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.EXTRA2)) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$ExtraPart$TimeIn$txt_ExtraTime*GEOM*TEXT SET " + "ET" + "\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$ExtraPart$TimeIn$txt_ExtraTime*GEOM*TEXT SET " + "" + "\0");
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraTime_In START \0");
			scorebug.setScorebug_ET_on_screen(true);
		}
		else if(scorebug.isScorebug_ET_on_screen() == true) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraTime_Out START \0");
			scorebug.setScorebug_ET_on_screen(false);
		}
		return scorebug;
	}
	public ScoreBug populateExtraTime(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug, String time_value, Match match, String selectedbroadcaster) throws IOException {
		
		if(is_this_updating == false) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$TimePart$TimeIn$TimePosition$InjuryTimeGrp$txt_AddedMinute*GEOM*TEXT SET " + "+" +  time_value + "'" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*AddedMin_In START \0");
		}
		return scorebug;
	}
	public ScoreBug populateExtraTimeBoth(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug,String time_value, Match match, String selectedbroadcaster) throws IOException {
		
		if(is_this_updating == false) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$TimePart$TimeIn$TimePosition$InjuryTimeGrp$txt_AddedMinute*GEOM*TEXT SET " + "+" +  time_value + "'" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*AddedMin_In START \0");
		}
		return scorebug;
	}
	public ScoreBug populateScorebugCard(PrintWriter print_writer,ScoreBug scorebug,int TeamId,int playerId, Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			int l = 200;
			String team_name = "";
			if(TeamId == match.getHomeTeamId()) {
				team_name = match.getHomeTeam().getTeamName1();
				for(Player hs : match.getHomeSquad()) {
					if(playerId == hs.getPlayerId()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_FirstName*GEOM*TEXT SET " + "" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_LastName*GEOM*TEXT SET " + hs.getTicker_name().toUpperCase() + "\0");
						
					}
				}
				for(Player hsub : match.getHomeSubstitutes()) {
					if(playerId == hsub.getPlayerId()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$txt_Number*GEOM*TEXT SET " + hsub.getJersey_number() + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_FirstName*GEOM*TEXT SET " + "" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_LastName*GEOM*TEXT SET " + hsub.getTicker_name().toUpperCase() + "\0");
					}
				}
			}
			else {
				team_name = match.getAwayTeam().getTeamName1();
				for(Player as : match.getAwaySquad()) {
					if(playerId == as.getPlayerId()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_FirstName*GEOM*TEXT SET " + "" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_LastName*GEOM*TEXT SET " + as.getTicker_name().toUpperCase() + "\0");
						
					}
				}
				for(Player asub : match.getAwaySubstitutes()) {
					if(playerId == asub.getPlayerId()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$txt_Number*GEOM*TEXT SET " + asub.getJersey_number() + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_FirstName*GEOM*TEXT SET " + "" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$PlayerName$txt_LastName*GEOM*TEXT SET " + asub.getTicker_name().toUpperCase() + "\0");
					}
				}
			}
			
			switch(scorebug.getScorebug_card_goal().toUpperCase())
			{
			case "YELLOW_CARD":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$SelectCardType*ACTIVE SET 1 \0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$SelectCardType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$txt_TeamName*GEOM*TEXT SET " + team_name + "\0");
				break;
			case "RED_CARD":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$SelectCardType*ACTIVE SET 1 \0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$SelectCardType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$txt_TeamName*GEOM*TEXT SET " + team_name + "\0");
				break;
			case "YELLOW_RED":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$SelectCardType*ACTIVE SET 1 \0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$SelectCardType*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$txt_TeamName*GEOM*TEXT SET " + team_name + "\0");
				break;
			case "PLAYER":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$PlayerGrpAll$SelectCardType*ACTIVE SET 0 \0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$CardsAll$txt_TeamName*GEOM*TEXT SET " + team_name + "\0");
				break;
			}
			scorebug.setLast_scorebug_card_goal(scorebug.getScorebug_card_goal().toUpperCase());
		}
		return scorebug;
	}
	public ScoreBug populateScorebugSubs(PrintWriter print_writer,ScoreBug scorebug,int TeamId,List<Player> plyr, Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			int l = 200;
			List<Event> evnt = new ArrayList<Event>();
			
			for(int i = 0; i<=match.getEvents().size()-1; i++) { 
				if(match.getEvents().get(i).getEventType().equalsIgnoreCase("replace")) {
					if(TeamId ==plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
						if(match.getHomeTeamId() == plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
							evnt.add(match.getEvents().get(i)); 
						}else if(match.getAwayTeamId() == plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
							evnt.add(match.getEvents().get(i)); 
						} 
					} 
				}
			}
			if(match.getHomeTeamId() == TeamId) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$TeamNameGrp$txt_TeamName*GEOM*TEXT SET " + match.getHomeTeam().getTeamName1() + "\0");
			}else if(match.getAwayTeamId() == TeamId) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$TeamNameGrp$txt_TeamName*GEOM*TEXT SET " + match.getAwayTeam().getTeamName1() + "\0");
			}
			switch(scorebug.getScorebug_subs().toUpperCase())
			{
			case "SINGLE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				break;
			case "DOUBLE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$OutPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$OutPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$OutPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$InPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$InPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$InPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				break;
				
			case "TRIPLE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll*FUNCTION*Omo*vis_con SET " + "3" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$OutPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line1$InPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$OutPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$OutPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$OutPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$InPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$InPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line2$InPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line3$OutPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line3$OutPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line3$OutPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line3$InPlayerGrpAll$Player$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line3$InPlayerGrpAll$Player$PlayerNameAll$txt_FirstName*GEOM*TEXT SET " + 
						"" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$SubstitutesAll$Grp1$SubDataAll$PlayerAll$Line3$InPlayerGrpAll$Player$PlayerNameAll$txt_LastName*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getTicker_name().toUpperCase() + "\0");
				
				break;
			}
			
		}
		
		scorebug.setLast_scorebug_subs(scorebug.getScorebug_subs().toUpperCase());
		return scorebug;
	}
	public ScoreBug populateScoreBugTeamStats(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug, int teamId ,List<Team> team, List<TeamStat> teamStats, Match match, String broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			for(TeamStat teamStat : teamStats) {
				if(teamStat.getTeamId() == teamId) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$1$"
							+ "txt_StatHead*GEOM*TEXT SET " + teamStat.getHeadStats1() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$2$"
							+ "txt_StatHead*GEOM*TEXT SET " + teamStat.getHeadStats2() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$3$"
							+ "txt_StatHead*GEOM*TEXT SET " + teamStat.getHeadStats3() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$4$"
							+ "txt_StatHead*GEOM*TEXT SET " + teamStat.getHeadStats4() + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$1$"
							+ "txt_StatValue*GEOM*TEXT SET " + teamStat.getValueStats1() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$2$"
							+ "txt_StatValue*GEOM*TEXT SET " + teamStat.getValueStats2() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$3$"
							+ "txt_StatValue*GEOM*TEXT SET " + teamStat.getValueStats3() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber$4$"
							+ "txt_StatValue*GEOM*TEXT SET " + teamStat.getValueStats4() + "\0");
				}
			}
			
			if(teamId == match.getHomeTeamId()) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$NameGrp$txt_TeamName*GEOM*TEXT SET " + 
						match.getHomeTeam().getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$NameGrp$txt_SubHead*GEOM*TEXT SET " + "" + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$ImageGrp$img_Badges*TEXTURE*IMAGE SET " +
						flag_path + match.getHomeTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$ImageGrp$img_BadgesShadow*TEXTURE*IMAGE SET " +
						flag_path + match.getHomeTeam().getTeamName4() + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber"
						+ "*FUNCTION*Omo*vis_con SET " + "4" + "\0");
				
			}
			else if(teamId == match.getAwayTeamId()) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$NameGrp$txt_TeamName*GEOM*TEXT SET " + 
						match.getAwayTeam().getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$NameGrp$txt_SubHead*GEOM*TEXT SET " + "" + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$ImageGrp$img_Badges*TEXTURE*IMAGE SET " +
						flag_path + match.getAwayTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$ImageGrp$img_BadgesShadow*TEXTURE*IMAGE SET " +
						flag_path + match.getAwayTeam().getTeamName4() + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$TeamStats$SelectStats$AllStats$Select_LineNumber"
						+ "*FUNCTION*Omo*vis_con SET " + "4" + "\0");
			}
		}
		scorebug.setLast_scorebug_team_stat(scorebug.getScorebug_team_stat());
		return scorebug;
	}
	public ScoreBug populateScoreBugPlayerStats(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug,int teamId, String StatType, int playerId , String PhotoType,
			List<Team> team, List<Player> players, List<PlayerStat> playerStats, Match match, String broadcaster) throws InterruptedException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			String cout = "";
			Player player = null;
			player = players.stream().filter(plyr -> plyr.getPlayerId() == playerId).findAny().orElse(null);
			
			data = new FootballData();
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$NameGrp$txt_PlayerName*GEOM*TEXT SET " + 
					player.getTicker_name() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$NameGrp$txt_PlayerNumber*GEOM*TEXT SET " + 
					player.getJersey_number() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$TeamNameGrp$txt_TeamName*GEOM*TEXT SET " + 
					team.get(teamId-1).getTeamName1() + "\0");

			if(teamId == match.getHomeTeamId()) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			}
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$NameGrp$txt_Position*GEOM*TEXT SET " + 
					player.getRole().toUpperCase() + "\0");
			
			if(PhotoType.equalsIgnoreCase("without_photo")) {
				cout = "WithOutImage";
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$Selec_tImage*FUNCTION*Omo*vis_con SET " + "0" + "\0");
			}
			else {
				cout = "With_Image";
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$Selec_tImage*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$DataGrp$StatsWithImage$ImageGrp$img_Player*TEXTURE*IMAGE SET " + photos_path 
						+ player.getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$DataGrp$StatsWithImage$ImageGrp$img_PlayerShadow*TEXTURE*IMAGE SET " + photos_path 
						+ player.getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
			}
			
			if(StatType.equalsIgnoreCase("heatmap")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$HeatMap$GroundAlInl$img_HeatMap"
						+ "*TEXTURE*IMAGE SET "+ image_path + "playerheatmap0_23.jpg" + "\0");
			}
			else if(StatType.equalsIgnoreCase("stats")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats*FUNCTION*Omo*vis_con SET " + "0" + "\0");
				
				com.kabaddi.containers.Players plyr = new com.kabaddi.containers.Players();
				
				for(com.kabaddi.containers.Team teams : data.getTeam()) {
					for(com.kabaddi.containers.Players plyer : teams.getTeamPlayer()) {
						if(player.getPlayerAPIId() != null && plyer.getId() != null && plyer.getId().equalsIgnoreCase(player.getPlayerAPIId())) {
							plyr = plyer;
							break;
						}
					}
				}
				System.out.println(plyr.toString());
				
				switch(player.getRole()) {
				case "Goalkeeper":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$$AllStats$1$"
							+ "txt_StatHead*GEOM*TEXT SET " + "MINUTES PLAYED" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$2$"
							+ "txt_StatHead*GEOM*TEXT SET " + "SAVES" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatHead*GEOM*TEXT SET " + "CLEARANCES" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatHead*GEOM*TEXT SET " + "TOUCHES" + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$1$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getMinsPlayed() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$2$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getSaves() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getTotalClearance() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getTouches() + "\0");
					break;
				case "Defender":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$1$"
							+ "txt_StatHead*GEOM*TEXT SET " + "MINUTES PLAYED" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$$AllStats$2$"
							+ "txt_StatHead*GEOM*TEXT SET " + "CLEARANCES" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatHead*GEOM*TEXT SET " + "INTERCEPTIONS " + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatHead*GEOM*TEXT SET " + "TACKLES" + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$1$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getMinsPlayed() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$2$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getTotalClearance() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getInterception() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getTotalTackle() + "\0");
					break;
				case "MidFielder":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$$AllStats$1$"
							+ "txt_StatHead*GEOM*TEXT SET " + "MINUTES PLAYED" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$2$"
							+ "txt_StatHead*GEOM*TEXT SET " + "PASSES" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatHead*GEOM*TEXT SET " + "DUEL WON" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatHead*GEOM*TEXT SET " + "CHANCES CREATED" + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$1$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getMinsPlayed() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$2$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getTotalPass() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getDuelWon() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getChanceCreated() + "\0");
					break;
				case "Forward":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$1$"
							+ "txt_StatHead*GEOM*TEXT SET " + "MINUTES PLAYED" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$$AllStats$2$"
							+ "txt_StatHead*GEOM*TEXT SET " + "GOALS" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatHead*GEOM*TEXT SET " + "CHANCES CREATED" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatHead*GEOM*TEXT SET " + "SHOT ON TARGET" + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$1$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getMinsPlayed() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$2$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getGoal() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$3$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getChanceCreated() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$DataGrp$StatsWithImage$" + cout + "$SelectStats$PlayerStat$AllStats$4$"
							+ "txt_StatValue*GEOM*TEXT SET " + plyr.getShotOnTarget() + "\0");
					break;
				}
				
			}
		}
		scorebug.setLast_scorebug_player_stat(scorebug.getScorebug_player_stat());
		return scorebug;
	}
	public ScoreBug populateScoreBugPromo(PrintWriter print_writer,boolean is_this_updating,ScoreBug scorebug,int match_number ,List<Team> team,List<Fixture> fix,List<Ground>ground,Match match, String broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			String team_name = "",newDate = "";
			List<String> data_name = new ArrayList<String>();
			
			String[] dateSuffix = {
					"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
					
					"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
					
					"th", "st", "nd", "rd", "th", "th", "th", "th", "th","th",
					
					"th", "st"
			};
			
			for(Team TM : team) {
				if(fix.get(match_number - 1).getHometeamid() == TM.getTeamId()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$lg_Badge1"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + TM.getTeamName4() + "\0");
					TimeUnit.MILLISECONDS.sleep(200);
					team_name = TM.getTeamName4().toUpperCase();
					data_name.add(team_name);
					if(is_this_updating == false) {
						scorebug.setScorebug_name(team_name);
						scorebug.setLast_scorebug_name(team_name);
//						is_this_updating = true;
					}
				}
			}
			
			for(Team TM : team) {
				if(fix.get(match_number - 1).getAwayteamid() == TM.getTeamId()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$lg_Badge2"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + TM.getTeamName4() + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$Match$txt_Match"
							+ "*GEOM*TEXT SET " + team_name + " vs " + TM.getTeamName4().toUpperCase() + "\0");
					TimeUnit.MILLISECONDS.sleep(200);
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_AwayTeam*GEOM*TEXT SET " + TM.getTeamName1().toUpperCase() + "\0");
				}
				
				
			}
			
//			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$txt_Group"
//					+ "*GEOM*TEXT SET " + "" + "\0");
//			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$HeadTimeGrp$txt_Head_Time"
//					+ "*GEOM*TEXT SET " + "HERO CLUB PLAYOFF" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$txt_Group"
					+ "*GEOM*TEXT SET " + fix.get(match_number-1).getGroupName() + "\0");
			String Date = "";
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, +1);
			Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
			if(fix.get(match_number-1).getDate().equalsIgnoreCase(Date)) {
//				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$txt_Group"
//						+ "*GEOM*TEXT SET " + "TOMORROW " + fix.get(match_number-1).getTime() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$HeadTimeGrp$txt_Head_Time"
						+ "*GEOM*TEXT SET " + "TOMORROW " + fix.get(match_number-1).getTime() + "\0");
			}else {
				cal.add(Calendar.DATE, -1);
				Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
				if(fix.get(match_number-1).getDate().equalsIgnoreCase(Date)) {
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$txt_Group"
//							+ "*GEOM*TEXT SET " + "COMING UP" + " AT " + fix.get(match_number-1).getTime() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$HeadTimeGrp$txt_Head_Time"
							+ "*GEOM*TEXT SET " + "COMING UP" + " AT " + fix.get(match_number-1).getTime() + "\0");
				}else {
					newDate = fix.get(match_number-1).getDate().split("-")[0];
					if(Integer.valueOf(newDate) < 10) {
						newDate = newDate.replaceFirst("0", "");
					}
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$TeamsGrpAll$TeamAll$Line1$PromoAll$Promo1$txt_Group"
//							+ "*GEOM*TEXT SET " + newDate + dateSuffix[Integer.valueOf(newDate)] + " " + Month.of(Integer.valueOf(fix.getDate().split("-")[1])) + " AT " + fix.get(match_number-1).getTime() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$AllOut$DataGrp$PromoAll$Grp1$PromoDataAll$HeadTimeGrp$txt_Head_Time"
							+ "*GEOM*TEXT SET " + newDate + dateSuffix[Integer.valueOf(newDate)] + " " + Month.of(Integer.valueOf(fix.get(match_number-1).getDate().split("-")[1])) + 
							" AT " + fix.get(match_number-1).getTime() + "\0");
				}
				
			}
			
			data_name.clear();
		}
		
		scorebug.setLast_scorebug_promo(scorebug.getScorebug_promo());
		return scorebug;
	}
	
	public void populateFFTeams(PrintWriter print_writer,String viz_scene, List<Team> team,Match match, String session_selected_broadcaster) throws InterruptedException, IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			int row_id_1=0,row_id_2=0,row_id_3=0,row_id_4=0;
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$GroupsAll$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
					"GROUP WINNER WILL QUALIFY FOR THE SEMI-FINALS" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$GroupsAll$AllData$txt_Header*GEOM*TEXT SET " + "GROUPS" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$GroupsAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + match.getTournament() + "\0");
			
			for(int i=0;i<=team.size()-1;i++) {
				if(team.get(i).getTeamGroup().equalsIgnoreCase("A")) {
					row_id_1 = row_id_1 + 1;
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$GroupsAll$AllData$Group1$TeamData" + row_id_1 + 
							"$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + team.get(i).getTeamName1() + "\0");
				}else if(team.get(i).getTeamGroup().equalsIgnoreCase("B")) {
					row_id_2 = row_id_2 + 1;
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$GroupsAll$AllData$Group2$TeamData" + row_id_2 + 
							"$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + team.get(i).getTeamName1() + "\0");
				}else if(team.get(i).getTeamGroup().equalsIgnoreCase("C")) {
					row_id_3 = row_id_3 + 1;
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$GroupsAll$AllData$Group3$TeamData" + row_id_3 + 
							"$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + team.get(i).getTeamName1() + "\0");
				}else if(team.get(i).getTeamGroup().equalsIgnoreCase("D")) {
					row_id_4 = row_id_4 + 1;
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$GroupsAll$AllData$Group4$TeamData" + row_id_4 + 
							"$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + team.get(i).getTeamName1() + "\0");
				}
			}
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 FF_In 2.000 Groups_In 2.580 \0");
		}
	}
	public void populateMatchId(PrintWriter print_writer,String viz_scene, Match match, String session_selected_broadcaster,List<VariousText> vt) throws InterruptedException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			KabaddiFunctions.getFootballLiveDatafromAPI(KabaddiFunctions.getAccessToken());
			
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp1$img_BadgesBW"
//					+ "*TEXTURE*IMAGE SET "+ logo_bw_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp2$img_BadgesOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp3$img_BadgesOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp4$img_Badges"
//					+ "*TEXTURE*IMAGE SET "+ logo_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//			
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp1$img_BadgesBW"
//					+ "*TEXTURE*IMAGE SET "+ logo_bw_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp2$img_BadgesOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp3$img_BadgesOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp4$img_Badges"
//					+ "*TEXTURE*IMAGE SET "+ logo_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp1$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp1$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getAwayTeam().getTeamName4() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp2$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp2$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getAwayTeam().getTeamName4() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$NameGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$NameGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$SelectSeparator$txt_Score*GEOM*TEXT SET " + "VS" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$HashTag$txt_WebInfo*GEOM*TEXT SET " + "GROUP " + match.getHomeTeam().getTeamGroup() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$Header$txt_Header*GEOM*TEXT SET " + match.getTournament() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$NameGrp$txt_TeamName*GEOM*TEXT SET " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$NameGrp$txt_TeamName*GEOM*TEXT SET " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
			
			for(VariousText vartext : vt) {
				if(vartext.getVariousType().equalsIgnoreCase("MatchIdentFooter") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$NameGrp$txt_Info*GEOM*TEXT SET " + vartext.getVariousText() + "\0");
					
				}else if(vartext.getVariousType().equalsIgnoreCase("MatchIdentFooter") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$NameGrp$txt_Info*GEOM*TEXT SET " + match.getVenueName().toUpperCase() + "\0");
					
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 MatchId_In 2.600 \0");
		}
	}
	public void populateMatchPromoSingle(PrintWriter print_writer,String viz_scene, int match_number ,List<Team> team,List<Fixture> fix,List<Ground>ground,Match match, String broadcaster) throws InterruptedException, IOException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {			
			String newDate = "";
			
			String[] dateSuffix = {
					"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
					
					"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
					
					"th", "st", "nd", "rd", "th", "th", "th", "th", "th","th",
					
					"th", "st"
			};
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$Header$txt_Header*GEOM*TEXT SET " + "FIFA WORLD CUP 26 AFC QUALIFIERS - ROUND 2" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$SelectSeparator$txt_Score*GEOM*TEXT SET " + "VS" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$HashTag$txt_WebInfo*GEOM*TEXT SET " + 
					fix.get(match_number - 1).getGroupName() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$NameGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + "WHITE" + KabaddiUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$NameGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + "WHITE" + KabaddiUtil.PNG_EXTENSION + "\0");
			
			for(Team TM : team) {
				if(fix.get(match_number - 1).getHometeamid() == TM.getTeamId()) {

					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp1$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ flag_path + TM.getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp2$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ flag_path + TM.getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp3$img_BadgesOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$ImageGrp$LogoImageGrp4$img_Badges"
//							+ "*TEXTURE*IMAGE SET "+ logo_path + TM.getTeamName4().toLowerCase() + "\0");
					
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp1$NameGrp$txt_TeamName*GEOM*TEXT SET " + TM.getTeamName1().toUpperCase() + "\0");
				}
				if(fix.get(match_number - 1).getAwayteamid() == TM.getTeamId()) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$TeamBadgeGrp2$BadgeAll$img_Badge" + "*TEXTURE*IMAGE SET "+ logo_path + 
							TM.getTeamName2().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp1$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ flag_path + TM.getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp2$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ flag_path + TM.getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp3$img_BadgesOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$ImageGrp$LogoImageGrp4$img_Badges"
//							+ "*TEXTURE*IMAGE SET "+ logo_path +TM.getTeamName4().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$TeamGrp2$NameGrp$txt_TeamName*GEOM*TEXT SET " + TM.getTeamName1().toUpperCase() + "\0");	
				}
			}
			String Date = "";
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, +1);
			Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
			if(fix.get(match_number-1).getDate().equalsIgnoreCase(Date)) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$NameGrp$txt_Info*GEOM*TEXT SET " + "TOMORROW AT " + fix.get(match_number-1).getTime() 
						+ " LOCAL TIME (" + ground.get(fix.get(match_number -1).getVenue() - 1).getFullname() + ")" + "\0");
			}else {
				cal.add(Calendar.DATE, -1);
				Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
				if(fix.get(match_number-1).getDate().equalsIgnoreCase(Date)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$NameGrp$txt_Info*GEOM*TEXT SET " + "COMING UP AT " + fix.get(match_number-1).getTime() 
							+ " LOCAL TIME (" + ground.get(fix.get(match_number -1).getVenue() - 1).getFullname() + ")" + "\0");
				}else {
					newDate = fix.get(match_number-1).getDate().split("-")[0];
					if(Integer.valueOf(newDate) < 10) {
						newDate = newDate.replaceFirst("0", "");
					}
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchId$All$NameGrp$txt_Info*GEOM*TEXT SET " + newDate + dateSuffix[Integer.valueOf(newDate)] + " " + 
							Month.of(Integer.valueOf(fix.get(match_number-1).getDate().split("-")[1])) + " AT " + fix.get(match_number-1).getTime() 
							+ " LOCAL TIME (" + ground.get(fix.get(match_number -1).getVenue() - 1).getFullname() + ")" + "\0");
				}
				
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 MatchId_In 2.100 \0");	
		}
	}
	public void populateTournamentStats(PrintWriter print_writer,String viz_scene,Match match, String session_selected_broadcaster) throws InterruptedException, IOException, CsvException, SAXException, ParserConfigurationException, FactoryConfigurationError{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp1$LogoImageGrp1$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp1$LogoImageGrp2$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getHomeTeam().getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp1$LogoImageGrp3$img_BadgesOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp1$LogoImageGrp4$img_Badges"
//					+ "*TEXTURE*IMAGE SET "+ logo_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp2$LogoImageGrp1$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getAwayTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp2$LogoImageGrp2$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getAwayTeam().getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp2$LogoImageGrp3$img_BadgesOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$TeamLogoGrp2$LogoImageGrp4$img_Badges"
//					+ "*TEXTURE*IMAGE SET "+ logo_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$SponsorPosition$SponsorGrpAll$SelectSponsorType$SponsorAll$SponsorBase"
					+ "*TEXTURE*IMAGE SET "+ logo_path + "Logo1" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$HeaderDataAll$txt_Header*GEOM*TEXT SET " + 
					match.getTournament() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$HeaderDataAll$txt_SubHead*GEOM*TEXT SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$TeamDataGrp$Out$TeamData$TextGrp$ScoreAllGrp$"
					+ "txt_HomeTeamScore*GEOM*TEXT SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$TeamDataGrp$Out$TeamData$TextGrp$ScoreAllGrp$"
					+ "Separator*GEOM*TEXT SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$TeamDataGrp$Out$TeamData$TextGrp$ScoreAllGrp$"
					+ "txt_AwayTeamScore*GEOM*TEXT SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$TeamDataGrp$Out$TeamData$TextGrp$txt_HomeTeamName*GEOM*TEXT SET " + 
					match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$TeamDataGrp$Out$TeamData$TextGrp$txt_AwayTeamName*GEOM*TEXT SET " + 
					match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
			
			
			String text_to_return = "";
			ArrayList<String> TournamentStats = new ArrayList<String>();
			try (BufferedReader br = new BufferedReader(new FileReader(KabaddiUtil.KABADDI_DIRECTORY + "TournamentStats.txt"))) {
				while((text_to_return = br.readLine()) != null) {
					TournamentStats.add(text_to_return);
				}
			}
		
		    for(int i=0;i<=TournamentStats.size()-1;i++) {
		    	//System.out.println("VALUE : " + Stats.get(i));
		    	print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$StatDataAll$Row" + (i+1) + "$Out$StatAllGrp$StatDataAll"
		    			+ "$txt_HomeStatValue*GEOM*TEXT SET " + TournamentStats.get(i).split(",")[0] + "\0");
		    	print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$StatDataAll$Row" + (i+1) + "$Out$StatAllGrp$StatDataAll"
		    			+ "$txt_StatHead*GEOM*TEXT SET " + TournamentStats.get(i).split(",")[1] + "\0");
		    	print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$MatchStatsAll$AllData$DataOut$StatDataAll$Row" + (i+1) + "$Out$StatAllGrp$StatDataAll"
		    			+ "$txt_AwayStatValue*GEOM*TEXT SET " + TournamentStats.get(i).split(",")[2] + "\0");
		    }
			
		    print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 FF_In 2.000 MatchStats_In 2.200 \0");
		}
	}
	public void populateMatchStats(PrintWriter print_writer,String viz_scene,KabaddiService footballService, Match match,Clock clock, String session_selected_broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET " + "2" + "\0");
			int l = 4;
			//String Home_player="",Away_player="";
			String h1="",h2="",h3="",a1="",a2="",a3="";
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$txt_Time*GEOM*TEXT SET " + 
					" " + "\0");
			//print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$MatchScorers$Header$HeaderOut$txt_TopHeader*GEOM*TEXT SET " + match.getMatchIdent().toUpperCase() + " - " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$ScoreGtp$txt_Score*GEOM*TEXT SET " + match.getHomeTeamScore() + " - " + match.getAwayTeamScore() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_1$img_LogoBW"
					+ "*TEXTURE*IMAGE SET "+ logo_bw_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ logo_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_3$img_LogoOutline"
					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_4$img_LogoOutline"
					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_1$img_LogoBW"
					+ "*TEXTURE*IMAGE SET "+ logo_bw_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ logo_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_3$img_LogoOutline"
					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_4$img_LogoOutline"
					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$HomeLogoGrp$Ani_2$img_Logo" + "*TEXTURE*IMAGE SET "+ logo_path + 
//					match.getHomeTeam().getTeamName2().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$TeamBadgeGrp2$BadgeAll$img_Badge" + "*TEXTURE*IMAGE SET "+ logo_path + 
//					match.getAwayTeam().getTeamName2().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$txt_HomeTeam*GEOM*TEXT SET " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$txt_AwayTeam*GEOM*TEXT SET " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
			
			if(match.getClock().getMatchHalves().equalsIgnoreCase("HALF")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$SubHeader$txt_Info*GEOM*TEXT SET " + 
						match.getClock().getMatchHalves().toUpperCase() + " TIME" + "\0");
				
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("FULL")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$SubHeader$txt_Info*GEOM*TEXT SET " + 
						match.getClock().getMatchHalves().toUpperCase() + " TIME" + "\0");
				
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("FIRST")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$SubHeader$txt_Info*GEOM*TEXT SET " + "FIRST HALF" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("SECOND")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$SubHeader$txt_Info*GEOM*TEXT SET " + "SECOND HALF" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("EXTRA1")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$SubHeader$txt_Info*GEOM*TEXT SET " + "EXTRA TIME 1" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("EXTRA2")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$SubHeader$txt_Info*GEOM*TEXT SET " + "EXTRA TIME 2" + "\0");
			}
			
			List<String> home_stats = new ArrayList<String>();
			List<String> away_stats = new ArrayList<String>();
			List<Integer> plyr_ids = new ArrayList<Integer>();
			boolean plyr_exist = false;
 			String stats_txt = "",stats_txt_og = "";
			
			for(int i=0; i<=match.getMatchStats().size()-1; i++) {
				
				if((match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.GOAL) 
						|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.PENALTY))) {
					
					plyr_exist = false;
					for(Integer plyr_id : plyr_ids) {
						if(match.getMatchStats().get(i).getPlayerId() == plyr_id && 
								(match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.GOAL) 
										|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.OWN_GOAL)
										|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.PENALTY))) {
							plyr_exist = true;
							break;
						}
					}

					if(plyr_exist == false) {
						plyr_ids.add(match.getMatchStats().get(i).getPlayerId());
						stats_txt = footballService.getPlayer(KabaddiUtil.PLAYER, 
							String.valueOf(match.getMatchStats().get(i).getPlayerId())).getTicker_name().toUpperCase()+ " " + 
							KabaddiFunctions.calExtraTimeGoal(match.getMatchStats().get(i).getMatchHalves(),match.getMatchStats().get(i).getTotalMatchSeconds()) + 
								KabaddiFunctions.goal_shortname(match.getMatchStats().get(i).getStats_type());
						
						for(int j=i+1; j<=match.getMatchStats().size()-1; j++) {
							if (match.getMatchStats().get(i).getPlayerId() == match.getMatchStats().get(j).getPlayerId()
								&& (match.getMatchStats().get(j).getStats_type().equalsIgnoreCase(KabaddiUtil.GOAL)
								|| match.getMatchStats().get(j).getStats_type().equalsIgnoreCase(KabaddiUtil.PENALTY))) {

								stats_txt = stats_txt + "," + 
									KabaddiFunctions.calExtraTimeGoal(match.getMatchStats().get(j).getMatchHalves(), match.getMatchStats().get(j).getTotalMatchSeconds()) 
										+ KabaddiFunctions.goal_shortname(match.getMatchStats().get(j).getStats_type());
							}
						}
						switch (KabaddiFunctions.getPlayerSquadType(match.getMatchStats().get(i).getPlayerId(),match.getMatchStats().get(i).getStats_type() ,match)) {
						case KabaddiUtil.HOME:
							home_stats.add(stats_txt);
							break;
						case KabaddiUtil.AWAY:
							away_stats.add(stats_txt);
							break;
						}
					}
				}else if(match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.OWN_GOAL)) {
					stats_txt_og = footballService.getPlayer(KabaddiUtil.PLAYER, 
							String.valueOf(match.getMatchStats().get(i).getPlayerId())).getTicker_name().toUpperCase()+ " " + 
							KabaddiFunctions.calExtraTimeGoal(match.getMatchStats().get(i).getMatchHalves(),match.getMatchStats().get(i).getTotalMatchSeconds()) + 
								KabaddiFunctions.goal_shortname(match.getMatchStats().get(i).getStats_type());
						
						/*for(int j=i+1; j<=match.getMatchStats().size()-1; j++) {
							if (match.getMatchStats().get(i).getPlayerId() == match.getMatchStats().get(j).getPlayerId()
								&& (match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(FootballUtil.OWN_GOAL))) {

								stats_txt_og = stats_txt_og + "," + 
									FootballFunctions.calExtraTimeGoal(match.getMatchStats().get(j).getMatchHalves(), match.getMatchStats().get(j).getTotalMatchSeconds()) 
										+ FootballFunctions.goal_shortname(match.getMatchStats().get(j).getStats_type());
							}
						}*/
						switch (KabaddiFunctions.getPlayerSquadType(match.getMatchStats().get(i).getPlayerId(),match.getMatchStats().get(i).getStats_type() ,match)) {
						case KabaddiUtil.HOME:
							home_stats.add(stats_txt_og);
							break;
						case KabaddiUtil.AWAY:
							away_stats.add(stats_txt_og);
							break;
						}
				}
			}
			
			if(match.getHomeTeamScore() == 0 ) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET 0 \0");
			}else if(home_stats.size() > 0 && home_stats.size() <= 2) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET 1 \0");

			}else if(home_stats.size() > 2 && home_stats.size() <= 4) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET 2 \0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET 3 \0");
			}
			
			if(match.getAwayTeamScore() == 0 ) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET 0 \0");

			}else if(away_stats.size() > 0 && away_stats.size() <= 2) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET 1 \0");

			}else if(away_stats.size() > 2 && away_stats.size() <= 4) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET 2 \0");

			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET 3 \0");

			}
			
			for(int i=0;i<=home_stats.size()-1;i++) {
				if(i < 2) { 
					h1 = h1 + home_stats.get(i); 
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Home$First$txt_Scorer1*GEOM*TEXT SET " + h1 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 4) {
					h2 = h2 + home_stats.get(i);
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Home$Second$txt_Scorer2*GEOM*TEXT SET " + h2 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 6){
					h3 = h3 + home_stats.get(i);
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Home$Third$txt_Scorer3*GEOM*TEXT SET " + h3 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			for(int i=0;i<=away_stats.size()-1;i++) {
				if(i < 2) { 
					a1 = a1 + away_stats.get(i); 
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Away$First$txt_Scorer1*GEOM*TEXT SET " + a1 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 4) {
					a2 = a2 + away_stats.get(i);
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Away$Second$txt_Scorer2*GEOM*TEXT SET " + a2 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 6){
					a3 = a3 + away_stats.get(i);
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ScoreLine$Header$Dataall$Away$Third$txt_Scorer3*GEOM*TEXT SET " + a3 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 3.120 \0");
		}
	}
	public void populateFF_H2H(PrintWriter print_writer,String viz_scene, Match match, String session_selected_broadcaster) throws InterruptedException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			data = new FootballData();
			//EuroLeague.WinProbability(data);
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$Header$txt_Header*GEOM*TEXT SET " + "HEAD TO HEAD" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp1$ImageGrp$LogoImageGrp1$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp2$ImageGrp$LogoImageGrp1$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getAwayTeam().getTeamName4() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp1$ImageGrp$LogoImageGrp2$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp2$ImageGrp$LogoImageGrp2$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ flag_path + match.getAwayTeam().getTeamName4() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp1$NameGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp2$NameGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			
			
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$SelectSeparator$txt_Score$txt_WinValue1*GEOM*TEXT SET " + 
//					data.getHomeContestantWins() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$SelectSeparator$txt_Score$txt_WinValue2*GEOM*TEXT SET " + 
//					data.getAwayContestantWins() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$DrawGrp$TextGrp$txt_DrawValue*GEOM*TEXT SET " + data.getDraws() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$SelectSeparator$txt_Score$txt_WinValue1*GEOM*TEXT SET " + 
					"2" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$SelectSeparator$txt_Score$txt_WinValue2*GEOM*TEXT SET " + 
					"2" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$DrawGrp$TextGrp$txt_DrawValue*GEOM*TEXT SET " + "2" + "\0");
			
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp1$NameGrp$txt_TeamName*GEOM*TEXT SET " + 
					match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$TeamGrp2$NameGrp$txt_TeamName*GEOM*TEXT SET " + 
					match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HeadToHead$All$InfoGrp$txt_Info*GEOM*TEXT SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 HeadToHead_In 2.500 \0");
		}
	}
	
	public void populateBugHighlight(PrintWriter print_writer,String viz_scene, Match match,Clock clock, String session_selected_broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			//print_writer.println("-1 RENDERER*TREE*$Main$All$MatchScorers$Header$HeaderOut$txt_TopHeader*GEOM*TEXT SET " + match.getMatchIdent().toUpperCase() + " - " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$Seperator$txt_HomeScore*GEOM*TEXT SET " + match.getHomeTeamScore() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$Seperator$txt_AwayScore*GEOM*TEXT SET " + match.getAwayTeamScore() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$TeamGrp1$txt_Name*GEOM*TEXT SET " + match.getHomeTeam().getTeamName4().toUpperCase() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$TeamGrp2$txt_Name*GEOM*TEXT SET " + match.getAwayTeam().getTeamName4().toUpperCase() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$TeamGrp1$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$TeamGrp2$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			
			if(match.getClock().getMatchHalves().equalsIgnoreCase("HALF")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$txt_Clock*GEOM*TEXT SET " + 
						match.getClock().getMatchHalves().toUpperCase() + " TIME" + "\0");
				
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("FULL")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$txt_Clock*GEOM*TEXT SET " + 
						match.getClock().getMatchHalves().toUpperCase() + " TIME" + "\0");
				
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("FIRST")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$txt_Clock*GEOM*TEXT SET " + "FIRST HALF" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("SECOND")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$txt_Clock*GEOM*TEXT SET " + "SECOND HALF" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("EXTRA1")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$txt_Clock*GEOM*TEXT SET " + "EXTRA TIME 1" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase("EXTRA2")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$txt_Clock*GEOM*TEXT SET " + "EXTRA TIME 2" + "\0");
			}			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 1.500 \0");
		}
	}
	public void populateBug(PrintWriter print_writer,String viz_scene,Bugs bug, Match match,Clock clock, String session_selected_broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			if(bug.getText1() != null && bug.getText2() != null) {
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$Base$SelectLineNumber*FUNCTION*Omo*vis_con SET 1 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber*FUNCTION*Omo*vis_con SET 1 \0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber$txt_BottomInfo*GEOM*TEXT SET " + bug.getText1().toUpperCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber$txt_Text*GEOM*TEXT SET " + bug.getText2().toUpperCase() + "\0");
			}else if(bug.getText1() != null && bug.getText2() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$Base$SelectLineNumber*FUNCTION*Omo*vis_con SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber*FUNCTION*Omo*vis_con SET 0 \0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber$txt_BottomInfo*GEOM*TEXT SET " + bug.getText1().toUpperCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber$txt_Text*GEOM*TEXT SET " + " " + "\0");
			}else {
				
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$Base$SelectLineNumber*FUNCTION*Omo*vis_con SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber*FUNCTION*Omo*vis_con SET 0 \0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber$txt_BottomInfo*GEOM*TEXT SET " + bug.getText2().toUpperCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Bug$SelectLineNumber$txt_Text*GEOM*TEXT SET " + " " + "\0");
			}		
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.500 \0");
		}
	}
	
	
	public void populateLofLineUp(PrintWriter print_writer,String viz_scene, int TeamId, List<Formation> formation, List<Team> team ,List<VariousText> var,Match match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			int row_id = 0;
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic*ACTIVE SET 1\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Effective*ACTIVE SET 0\0");
			
			if(TeamId == match.getHomeTeamId()) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$HeaderGrp$txt_TeamName*GEOM*TEXT SET " + 
						match.getHomeTeam().getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$HeaderGrp$txt_Formation*GEOM*TEXT SET " + 
						formation.get(match.getHomeTeamFormationId()-1).getFormDescription() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$HeaderGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				
				for(Player hs : match.getHomeSquad()) {
					row_id = row_id + 1;
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NumberGrp$"
							+ "txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$"
							+ "Name_IconsAll$txt_Name*GEOM*TEXT SET " + hs.getTicker_name().toUpperCase() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player" 
							+ row_id + "$In$Radikal-Bold*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
					
					if(hs.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN)) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					}
					else if(hs.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					}
					else if(hs.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					}
					else {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
							+ "SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
							+ "SelectStatus*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					for(Formation form : formation) {
						if(form.getFormId() == match.getHomeTeamFormationId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
							
						}
					}
				}
			}
			else if(TeamId == match.getAwayTeamId()) {
				row_id = 0;
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$HeaderGrp$txt_TeamName*GEOM*TEXT SET " + 
						match.getAwayTeam().getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$HeaderGrp$txt_Formation*GEOM*TEXT SET " + 
						formation.get(match.getAwayTeamFormationId()-1).getFormDescription() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$HeaderGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				
				for(Player as : match.getAwaySquad()) {
					row_id = row_id + 1;
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NumberGrp$txt_Number*GEOM*TEXT SET " 
							+ as.getJersey_number() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$txt_Name*GEOM*TEXT SET " 
							+ as.getTicker_name().toUpperCase() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$TacticallNumber$Player" + row_id + 
							"$In$Radikal-Bold*GEOM*TEXT SET " + as.getJersey_number() + "\0");
					
					if(as.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN)) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					}
					else if(as.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					}
					else if(as.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					}
					else {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
								+ "SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
							+ "SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$LineUp$Row" + row_id + "$In$NameGrp$Name_IconsAll$"
							+ "SelectStatus*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					for(Formation form : formation) {
						if(form.getFormId() == match.getAwayTeamFormationId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$TextAll$Side" + which_side + "$TacticalAllGrp$Basic$TacticallNumber$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
							
						}
					}
				}
			}
		}
	}
	public String populateLofVerticalFlipper(PrintWriter print_writer,String viz_scene, int which_side, int Teamid, List<Formation> formations, List<Team> team, Match match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			int row_id = 0;
			
			if(TeamId == match.getHomeTeamId()) {
				
				if(which_side == 1) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$HeaderGrp$txt_TeamName*GEOM*TEXT SET " + 
							match.getHomeTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$HeaderGrp$txt_Formation*GEOM*TEXT SET " + 
							formations.get(match.getHomeTeamFormationId()-1).getFormDescription() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$HeaderGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
							colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
					
					formation = formations.get(match.getHomeTeamFormationId()-1).getFormDescription();
				}
				
				if(count > 0) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$"
							+ "Select_PlayerNumber*FUNCTION*Omo*vis_con SET " + formation.split("-")[count-1] + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$"
							+ "Select_PlayerNumber*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}
				
				if(count == 0) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"GOAL-KEEPER" + "\0");
					row_id = row_id + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
							+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getHomeSquad().get(0).getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
							+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getHomeSquad().get(0).getJersey_number() + "\0");
					
					if(match.getHomeSquad().get(0).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
							match.getHomeSquad().get(0).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
								+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
								+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
							+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					for(Event event : match.getEvents()) {
						if(event.getOnPlayerId() == match.getHomeSquad().get(0).getPlayerId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							break;
						}
					}
					
				}
				else if(count == 1) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"DEFENDERS" + "\0");
					for(int i = 1; i<= match.getHomeSquad().size(); i++) {
						if(i <= Integer.valueOf(formation.split("-")[count-1])) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getHomeSquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getHomeSquad().get(i).getJersey_number() + "\0");
							
							if(match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getHomeSquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 2) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"MIDFIELDERS" + "\0");
					
					for(int i = 1; i<= match.getHomeSquad().size(); i++) {
						if(i > Integer.valueOf(formation.split("-")[0]) && i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getHomeSquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getHomeSquad().get(i).getJersey_number() + "\0");
							
							if(match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getHomeSquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 3) {
					if(formation.split("-").length == 3) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
								"FORWARDS" + "\0");
					}else if(formation.split("-").length == 4) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
								"MIDFIELDERS" + "\0");
					}
					
					for(int i = 1; i<= match.getHomeSquad().size(); i++) {
						if(i > (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])) && 
								i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getHomeSquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getHomeSquad().get(i).getJersey_number() + "\0");
							
							if(match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getHomeSquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 4) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"FORWARDS" + "\0");
					
					for(int i = 1; i<= match.getHomeSquad().size(); i++) {
						if(i > (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+Integer.valueOf(formation.split("-")[2])) && 
								i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2])+
										Integer.valueOf(formation.split("-")[3]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getHomeSquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getHomeSquad().get(i).getJersey_number() + "\0");
							
							if(match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getHomeSquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 5) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"FORWARDS" + "\0");
					
					for(int i = 1; i<= match.getHomeSquad().size(); i++) {
						if(i > (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2])+
								Integer.valueOf(formation.split("-")[3])) && 
								i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2])+
										Integer.valueOf(formation.split("-")[3])+Integer.valueOf(formation.split("-")[4]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getHomeSquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getHomeSquad().get(i).getJersey_number() + "\0");
							
							if(match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getHomeSquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getHomeSquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}
			}
			else if(TeamId == match.getAwayTeamId()) {
				if(which_side == 1) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$HeaderGrp$txt_TeamName*GEOM*TEXT SET " + 
							match.getAwayTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$HeaderGrp$txt_Formation*GEOM*TEXT SET " + 
							formations.get(match.getAwayTeamFormationId()-1).getFormDescription() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$HeaderGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
							colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
					
					formation = formations.get(match.getAwayTeamFormationId()-1).getFormDescription();
				}
				
				if(count > 0) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$"
							+ "Select_PlayerNumber*FUNCTION*Omo*vis_con SET " + formation.split("-")[count-1] + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$"
							+ "Select_PlayerNumber*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}
				
				if(count == 0) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"GOAL-KEEPER" + "\0");
					row_id = row_id + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
							+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getAwaySquad().get(0).getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
							+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getAwaySquad().get(0).getJersey_number() + "\0");
					
					if(match.getAwaySquad().get(0).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
							match.getAwaySquad().get(0).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
								+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
								+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
							+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					for(Event event : match.getEvents()) {
						if(event.getOnPlayerId() == match.getAwaySquad().get(0).getPlayerId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							break;
						}
					}
				}
				else if(count == 1) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"DEFENDERS" + "\0");
					for(int i = 1; i<= match.getAwaySquad().size(); i++) {
						if(i <= Integer.valueOf(formation.split("-")[count-1])) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getAwaySquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getAwaySquad().get(i).getJersey_number() + "\0");
							
							if(match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getAwaySquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 2) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"MIDFIELDERS" + "\0");
					
					for(int i = 1; i<= match.getAwaySquad().size(); i++) {
						if(i > Integer.valueOf(formation.split("-")[0]) && i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getAwaySquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getAwaySquad().get(i).getJersey_number() + "\0");
							
							if(match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getAwaySquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 3) {
					if(formation.split("-").length == 3) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
								"FORWARDS" + "\0");
					}else if(formation.split("-").length == 4) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
								"MIDFIELDERS" + "\0");
					}
					
					for(int i = 1; i<= match.getAwaySquad().size(); i++) {
						if(i > (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])) && 
								i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getAwaySquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getAwaySquad().get(i).getJersey_number() + "\0");
							
							if(match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getAwaySquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 4) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"FORWARDS" + "\0");
					
					for(int i = 1; i<= match.getAwaySquad().size(); i++) {
						if(i > (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+Integer.valueOf(formation.split("-")[2])) && 
								i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2])+
										Integer.valueOf(formation.split("-")[3]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getAwaySquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getAwaySquad().get(i).getJersey_number() + "\0");
							
							if(match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getAwaySquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}else if(count == 5) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$txt_PositionHead*GEOM*TEXT SET " + 
							"FORWARDS" + "\0");
					
					for(int i = 1; i<= match.getAwaySquad().size(); i++) {
						if(i > (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2])+
								Integer.valueOf(formation.split("-")[3])) && 
								i <= (Integer.valueOf(formation.split("-")[0])+Integer.valueOf(formation.split("-")[1])+ Integer.valueOf(formation.split("-")[2])+
										Integer.valueOf(formation.split("-")[3])+Integer.valueOf(formation.split("-")[4]))) {
							row_id = row_id + 1;
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Name*GEOM*TEXT SET " + match.getAwaySquad().get(i).getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$txt_Number*GEOM*TEXT SET " + match.getAwaySquad().get(i).getJersey_number() + "\0");
							
							if(match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN) || 
									match.getAwaySquad().get(i).getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
										+ row_id + "$Select_Captain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							}
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
									+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							for(Event event : match.getEvents()) {
								if(event.getOnPlayerId() == match.getAwaySquad().get(i).getPlayerId()) {
									print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$PlayerGrpAll$Select_PlayerNumber$"
											+ row_id + "$Select_Sub*FUNCTION*Omo*vis_con SET " + "1" + "\0");
									break;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	public void populateLofLeaderBoard(PrintWriter print_writer,String viz_scene, LeaderBoard leaderboard, List<Team> team,Match match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			String cont = "";
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_Header*GEOM*TEXT SET " + 
					leaderboard.getHeader() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_SubHeader*GEOM*TEXT SET " + 
					leaderboard.getSubHeader() + "\0");
			
			for(int i=0;i<2;i++) {
				if(i==0) {
					cont = "Dehighlight";
				}else {
					cont = "HIghtlight";
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row1$" + cont + "$ImageGrp$img_Player*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer1().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row1$" + cont + "$ImageGrp$img_PlayerShadow*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer1().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row2$" + cont + "$ImageGrp$img_Player*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer2().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row2$" + cont + "$ImageGrp$img_PlayerShadow*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer2().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row3$" + cont + "$ImageGrp$img_Player*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer3().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row3$" + cont + "$ImageGrp$img_PlayerShadow*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer3().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row4$" + cont + "$ImageGrp$img_Player*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer4().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row4$" + cont + "$ImageGrp$img_PlayerShadow*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer4().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row5$" + cont + "$ImageGrp$img_Player*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer5().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row5$" + cont + "$ImageGrp$img_PlayerShadow*TEXTURE*IMAGE SET " + photos_path 
							+ leaderboard.getPlayer5().getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
				}
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row1$" + cont + "$StatGrp$txt_Name*GEOM*TEXT SET " + 
						leaderboard.getPlayer1().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row1$" + cont + "$StatGrp$txt_TeamName*GEOM*TEXT SET " + 
						team.get(leaderboard.getPlayer1().getTeamId() - 1).getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row1$" + cont + "$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
						leaderboard.getPlayerStats1() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row2$" + cont + "$StatGrp$txt_Name*GEOM*TEXT SET " + 
						leaderboard.getPlayer2().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row2$" + cont + "$StatGrp$txt_TeamName*GEOM*TEXT SET " + 
						team.get(leaderboard.getPlayer2().getTeamId() - 1).getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row2$" + cont + "$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
						leaderboard.getPlayerStats2() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row3$" + cont + "$StatGrp$txt_Name*GEOM*TEXT SET " + 
						leaderboard.getPlayer3().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row3$" + cont + "$StatGrp$txt_TeamName*GEOM*TEXT SET " + 
						team.get(leaderboard.getPlayer3().getTeamId() - 1).getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row3$" + cont + "$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
						leaderboard.getPlayerStats3() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row4$" + cont + "$StatGrp$txt_Name*GEOM*TEXT SET " + 
						leaderboard.getPlayer4().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row4$" + cont + "$StatGrp$txt_TeamName*GEOM*TEXT SET " + 
						team.get(leaderboard.getPlayer4().getTeamId() - 1).getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row4$" + cont + "$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
						leaderboard.getPlayerStats4() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row5$" + cont + "$StatGrp$txt_Name*GEOM*TEXT SET " + 
						leaderboard.getPlayer5().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row5$" + cont + "$StatGrp$txt_TeamName*GEOM*TEXT SET " + 
						team.get(leaderboard.getPlayer5().getTeamId() - 1).getTeamName3() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row5$" + cont + "$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
						leaderboard.getPlayerStats5() + "\0");
			}
		}
	}
	
	public void populateFF_FixtureAndResult(PrintWriter print_writer,String viz_scene, int TeamId, List<Fixture> fixtures, Match match, String session_selected_broadcaster) {
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			int row_id =0, omo=0;
			String cout = "";
			
			if(match.getHomeTeamId() == TeamId) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$txt_Header*GEOM*TEXT SET " + 
						match.getHomeTeam().getTeamName1() + " RESULTS - ROUND 2" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$ResultsAll$LogoGrp$LogoImageGrp1$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$ResultsAll$LogoGrp$LogoImageGrp2$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
			}else if(match.getAwayTeamId() == TeamId) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$txt_Header*GEOM*TEXT SET " + 
						match.getAwayTeam().getTeamName1() + " RESULTS - ROUND 2" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$ResultsAll$LogoGrp$LogoImageGrp1$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$ResultsAll$LogoGrp$LogoImageGrp2$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + 
					"FIFA WORLD CUP QUALIFIERS 2026" + "\0");
			
			for(int i=0;i <= fixtures.size()-1;i++) {
				if(fixtures.get(i).getHometeamid() == TeamId || fixtures.get(i).getAwayteamid() == TeamId) {
					if(fixtures.get(i).getMargin() == null) {
						
					}else {
						
						row_id = row_id + 1;
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber*FUNCTION*Omo*vis_con SET " + row_id + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$DateGrp$txt_Date"
								+ "*GEOM*TEXT SET " + fixtures.get(i).getDate().split("-")[0] + " " + Month.of(Integer.valueOf(fixtures.get(i).getDate().split("-")[1])) 
								+ " " + fixtures.get(i).getDate().split("-")[2] + "\0");
						
						if(match.getMatchFileName().replace(".xml", "").equalsIgnoreCase(fixtures.get(i).getMatchfilename())) {
							omo=1;
							cout="$Highlight";
						}else {
							omo=0;
							cout="$Dehighlight";
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + 
								"$SelectMatchType*FUNCTION*Omo*vis_con SET " + omo + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$SelectMatchType" 
								+ cout + "$Team1$txt_TeamName1*GEOM*TEXT SET " + fixtures.get(i).getHome_Team().getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$SelectMatchType" 
								+ cout + "$Team1$txt_TeamName2*GEOM*TEXT SET " + fixtures.get(i).getAway_Team().getTeamName1() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$SelectMatchType" 
								+ cout + "$Team1$txt_Separator*GEOM*TEXT SET " + fixtures.get(i).getMargin() + "\0");
						
						if(fixtures.get(i).getHomeScorer() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$txt_Scorer1" 
									+ "$*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$txt_Scorer1" 
									+ "$*GEOM*TEXT SET " + fixtures.get(i).getHomeScorer() + "\0");
						}
						
						if(fixtures.get(i).getAwayScorer() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$txt_Scorer2" 
									+ "$*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$Select_RowNumber$Row" + row_id + "$txt_Scorer2" 
									+ "$*GEOM*TEXT SET " + fixtures.get(i).getAwayScorer() + "\0");
						}
					}
				}
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FF_Required$HashTag*ACTIVE SET 0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$ResultsAll$AllData$FixtureData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 FF_In 2.000 Results_In 2.500 \0");
		}
	}
	
	public void populatePlayingXI(PrintWriter print_writer,String viz_scene, int TeamId,String Type,List<Formation> formation, List<Team> team ,List<VariousText> var,Match match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			int row_id = 0,row_id_sub = 0,l=100;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
			
			if(TeamId == match.getHomeTeamId()) {
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$txt_TeamName*GEOM*TEXT SET " + 
						team.get(TeamId-1).getTeamName1().toUpperCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$SubHeadGrp$txt_SubHead1*GEOM*TEXT SET " + 
						"STARTING XI" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$SubHeadGrp$txt_SubHead2*GEOM*TEXT SET " + 
						"SUBSTITUTES" + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp1$img_BadgesBW"
						+ "*TEXTURE*IMAGE SET "+ logo_bw_path + team.get(TeamId-1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp4$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ logo_path + team.get(TeamId-1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp2$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(TeamId-1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp3$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(TeamId-1).getTeamName4().toLowerCase() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player1"
						+ "$In$SelectPlayerType$GK_Jersey$img_GK_Jersey*TEXTURE*IMAGE SET "+ colors_path + match.getHomeTeamGKJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
				
				if(team.get(TeamId-1).getTeamCoach() == null) {
					if(team.get(TeamId-1).getTeamAssistantCoach() == null) {
						
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										"" + "\0");
							}
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								"" + "\0");
					}else {

						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										"ASST. COACH" + "\0");
							}
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								team.get(TeamId-1).getTeamAssistantCoach() + "\0");
					}
					
				}else {
					for(VariousText vartext : var) {
						if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									vartext.getVariousText() + "\0");
						}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									"COACH" + "\0");
						}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
							"" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
							team.get(TeamId-1).getTeamCoach() + "\0");
				}
				
				
				
				for(Player hs : match.getHomeSquad()) {
					row_id = row_id + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp"
							+ "*FUNCTION*Grid*num_row SET " + row_id + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
					
					if(hs.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hs.getFirstname().toUpperCase() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + hs.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hs.getFull_name().toUpperCase() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					
					if(hs.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(hs.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(hs.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
					//FORMATION
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player" + row_id + 
							"$Out$In$Dehighlight$Radikal-Bold*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
					
					switch(Type.toUpperCase()) {
					case "WITHOUT_IMAGE":
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
							"$In$SelectPlayerType$Jersey$img_Jersey"+ "*TEXTURE*IMAGE SET "+ colors_path + match.getHomeTeamJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						if(match.getHomeTeamJerseyColor().equalsIgnoreCase("YELLOW")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "BLACK" + KabaddiUtil.PNG_EXTENSION + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "WHITE" + KabaddiUtil.PNG_EXTENSION + "\0");
						}
						
						if(row_id == 1) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$GK_Jersey$img_GK_JerseyText$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$Jersey$img_JerseyText$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
						}
						
						
						break;
					case "WITH_IMAGE":
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$ImageAll$img_PlayerImage" + "*TEXTURE*IMAGE SET "+ photos_path + match.getHomeTeam().getTeamName4().toUpperCase() + 
									"//" + hs.getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						break;
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
							"$In$txt_PlayerName*GEOM*TEXT SET " + hs.getTicker_name().toUpperCase() + "\0");
					
					TimeUnit.MILLISECONDS.sleep(l);
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
				}
				
				for(Formation form : formation) {
					if(form.getFormId() == match.getHomeTeamFormationId()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$txt_Formation*GEOM*TEXT SET " + form.getFormDescription() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
					}
				}
				
				for(Player hsub : match.getHomeSubstitutes()) {
					row_id_sub = row_id_sub + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench"
							+ "*FUNCTION*Grid*num_row SET " + row_id_sub + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayerIn" + row_id + " SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + hsub.getJersey_number() + "\0");
					
					if(hsub.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hsub.getFirstname().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + hsub.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hsub.getFull_name().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					if(hsub.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
				}
				
				
				
			//-------------------------------------------------------------------------------------------------------------------------------------------------
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$txt_TeamName*GEOM*TEXT SET " + 
						match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$SubHeadGrp$txt_SubHead1*GEOM*TEXT SET " + 
						"STARTING XI" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$SubHeadGrp$txt_SubHead2*GEOM*TEXT SET " + 
						"SUBSTITUTES" + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp1$img_BadgesBW"
						+ "*TEXTURE*IMAGE SET "+ logo_bw_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp4$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ logo_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp2$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp3$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player1"
						+ "$In$SelectPlayerType$GK_Jersey$img_GK_Jersey*TEXTURE*IMAGE SET "+ colors_path + match.getAwayTeamGKJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
				
				if(team.get(match.getAwayTeamId()-1).getTeamCoach() == null) {
					if(team.get(match.getAwayTeamId()-1).getTeamAssistantCoach() == null) {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										" " + "\0");
							}
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								"" + "\0");
					}else {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										"ASST. COACH" + "\0");
							}
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								team.get(match.getAwayTeamId()-1).getTeamAssistantCoach() + "\0");
					}
					
				}else {
					for(VariousText vartext : var) {
						if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									vartext.getVariousText() + "\0");
						}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									"COACH" + "\0");
						}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
							"" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
							team.get(match.getAwayTeamId()-1).getTeamCoach() + "\0");
					
				}
				
				row_id = 0;
				for(Player as : match.getAwaySquad()) {
					row_id = row_id + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp"
							+ "*FUNCTION*Grid*num_row SET " + row_id + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
					
					if(as.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + as.getFirstname().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + as.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + as.getFull_name().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					
					if(as.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(as.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(as.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
					//FORMATION
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player" + row_id + 
							"$Out$In$Dehighlight$Radikal-Bold*GEOM*TEXT SET " + as.getJersey_number() + "\0");
					
					switch(Type.toUpperCase()) {
					case "WITHOUT_IMAGE":
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
							"$In$SelectPlayerType$Jersey$img_Jersey"+ "*TEXTURE*IMAGE SET "+ colors_path + match.getAwayTeamJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						if(match.getAwayTeamJerseyColor().equalsIgnoreCase("YELLOW")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "BLACK" + KabaddiUtil.PNG_EXTENSION + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "WHITE" + KabaddiUtil.PNG_EXTENSION + "\0");
						}
						
						if(row_id == 1) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$GK_Jersey$img_GK_JerseyText$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$Jersey$img_JerseyText$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
						}
						break;
					case "WITH_IMAGE":
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$ImageAll$img_PlayerImage" + "*TEXTURE*IMAGE SET "+ photos_path + match.getAwayTeam().getTeamName4().toUpperCase() + 
									"//" + as.getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						break;
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
							"$In$txt_PlayerName*GEOM*TEXT SET " + as.getTicker_name().toUpperCase() + "\0");
					
					TimeUnit.MILLISECONDS.sleep(l);
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
				}
				
				for(Formation form : formation) {
					if(form.getFormId() == match.getAwayTeamFormationId()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$txt_Formation*GEOM*TEXT SET " + form.getFormDescription() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
					}
				}
				
				row_id_sub = 0;
				for(Player asub : match.getAwaySubstitutes()) {
					row_id_sub = row_id_sub + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench"
							+ "*FUNCTION*Grid*num_row SET " + row_id_sub + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayerIn" + row_id + " SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + asub.getJersey_number() + "\0");
					
					if(asub.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + asub.getFirstname().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + asub.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + asub.getFull_name().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					if(asub.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
				}
				
			}else if(TeamId == match.getAwayTeamId()) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$txt_TeamName*GEOM*TEXT SET " + 
						team.get(TeamId-1).getTeamName1().toUpperCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$SubHeadGrp$txt_SubHead1*GEOM*TEXT SET " + 
						"STARTING XI" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$SubHeadGrp$txt_SubHead2*GEOM*TEXT SET " + 
						"SUBSTITUTES" + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp1$img_BadgesBW"
						+ "*TEXTURE*IMAGE SET "+ logo_bw_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp4$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ logo_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp2$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$LogoGrp$LogoImageGrp3$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(match.getAwayTeamId() -1).getTeamName4().toLowerCase() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player1"
						+ "$In$SelectPlayerType$GK_Jersey$img_GK_Jersey*TEXTURE*IMAGE SET "+ colors_path + match.getAwayTeamGKJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
				
				if(team.get(TeamId-1).getTeamCoach() == null) {
					if(team.get(TeamId-1).getTeamAssistantCoach() == null) {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										" " + "\0");
							}
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								"" + "\0");
					}else {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										"ASST. COACH" + "\0");
							}
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								team.get(TeamId-1).getTeamAssistantCoach() + "\0");
					}
					
				}else {
					for(VariousText vartext : var) {
						if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									vartext.getVariousText() + "\0");
						}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONAWAYCOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									"COACH" + "\0");
						}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
							"" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
							team.get(TeamId-1).getTeamCoach() + "\0");
					
				}
				row_id = 0;
				for(Player as : match.getAwaySquad()) {
					row_id = row_id + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp"
							+ "*FUNCTION*Grid*num_row SET " + row_id + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
					
					if(as.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + as.getFirstname().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + as.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + as.getTicker_name().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					if(as.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(as.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(as.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
					
					//FORMATION
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player" + row_id + 
							"$Out$In$Dehighlight$Radikal-Bold*GEOM*TEXT SET " + as.getJersey_number() + "\0");
					
					switch(Type.toUpperCase()) {
					case "WITHOUT_IMAGE":
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
							"$In$SelectPlayerType$Jersey$img_Jersey"+ "*TEXTURE*IMAGE SET "+ colors_path + match.getAwayTeamJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						if(match.getAwayTeamJerseyColor().equalsIgnoreCase("WHITE")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "BLACK" + KabaddiUtil.PNG_EXTENSION + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "WHITE" + KabaddiUtil.PNG_EXTENSION + "\0");
						}
						
						if(row_id == 1) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$GK_Jersey$img_GK_JerseyText$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$Jersey$img_JerseyText$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
						}
						break;
					case "WITH_IMAGE":
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$ImageAll$img_PlayerImage" + "*TEXTURE*IMAGE SET "+ photos_path + match.getAwayTeam().getTeamName4().toUpperCase() + 
									"//" + as.getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						break;
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
							"$In$txt_PlayerName*GEOM*TEXT SET " + as.getTicker_name().toUpperCase() + "\0");
					
					TimeUnit.MILLISECONDS.sleep(l);
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					
				}
				
				for(Formation form : formation) {
					if(form.getFormId() == match.getAwayTeamFormationId()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$txt_Formation*GEOM*TEXT SET " + form.getFormDescription() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
					}
				}
				
				row_id_sub = 0;
				for(Player asub : match.getAwaySubstitutes()) {
					row_id_sub = row_id_sub + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench"
							+ "*FUNCTION*Grid*num_row SET " + row_id_sub + "\0");

					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + asub.getJersey_number() + "\0");
					
					if(asub.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + asub.getFirstname().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + asub.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + asub.getFull_name().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					if(asub.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team1$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
				}
				
				
				//---------------------------------------------------------------------------------------------------------------------------------------------------
				
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$txt_TeamName*GEOM*TEXT SET " + 
						match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$SubHeadGrp$txt_SubHead1*GEOM*TEXT SET " + 
						"STARTING XI" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$SubHeadGrp$txt_SubHead2*GEOM*TEXT SET " + 
						"SUBSTITUTES" + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp1$img_BadgesBW"
						+ "*TEXTURE*IMAGE SET "+ logo_bw_path + team.get(match.getHomeTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp4$img_Badges"
						+ "*TEXTURE*IMAGE SET "+ logo_path + team.get(match.getHomeTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp2$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(match.getHomeTeamId() -1).getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$LogoGrp$LogoImageGrp3$img_BadgesOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(match.getHomeTeamId() -1).getTeamName4().toLowerCase() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$img_TeamColour*TEXTURE*IMAGE SET " + 
						colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player1"
						+ "$In$SelectPlayerType$GK_Jersey$img_GK_Jersey*TEXTURE*IMAGE SET "+ colors_path + match.getHomeTeamGKJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
				
				if(team.get(match.getHomeTeamId()-1).getTeamCoach() == null) {
					if(team.get(match.getHomeTeamId()-1).getTeamAssistantCoach() == null) {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										"" + "\0");
							}
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								"" + "\0");
					}else {
						for(VariousText vartext : var) {
							if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										vartext.getVariousText() + "\0");
							}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
										"ASST. COACH" + "\0");
							}
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
								"" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
								team.get(match.getHomeTeamId()-1).getTeamAssistantCoach() + "\0");
					}
					
				}else {
					for(VariousText vartext : var) {
						if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									vartext.getVariousText() + "\0");
						}else if(vartext.getVariousType().equalsIgnoreCase("FORMATIONHOMECOACH") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$txt_CoachText*GEOM*TEXT SET " + 
									"COACH" + "\0");
						}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_FirstName*GEOM*TEXT SET " + 
							"" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$CoachGrp$CoachNameGrp$MaxSize$NameAll$txt_LastName*GEOM*TEXT SET " + 
							team.get(match.getHomeTeamId()-1).getTeamCoach() + "\0");
					
				}
				
				row_id = 0;
				for(Player hs : match.getHomeSquad()) {
					row_id = row_id + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp"
							+ "*FUNCTION*Grid*num_row SET " + row_id + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
					
					if(hs.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hs.getFirstname().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + hs.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hs.getFull_name().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					if(hs.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.CAPTAIN)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(hs.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else if(hs.getCaptainGoalKeeper().equalsIgnoreCase("CAPTAIN_GOAL_KEEPER")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
								"$Out$Dehighlight$NameGrp$SelectCaptain*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
					
					//FORMATION
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player" + row_id + 
							"$Out$In$Dehighlight$Radikal-Bold*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
					
					switch(Type.toUpperCase()) {
					case "WITHOUT_IMAGE":
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
							"$In$SelectPlayerType$Jersey$img_Jersey"+ "*TEXTURE*IMAGE SET "+ colors_path + match.getHomeTeamJerseyColor().toUpperCase() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						if(match.getHomeTeamJerseyColor().equalsIgnoreCase("WHITE")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "BLACK" + KabaddiUtil.PNG_EXTENSION + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$Jersey$img_JerseyText" + "*TEXTURE*IMAGE SET "+ colors_path + "WHITE" + KabaddiUtil.PNG_EXTENSION + "\0");
						}
						
						if(row_id == 1) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$GK_Jersey$img_GK_JerseyText$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
									"$In$SelectPlayerType$Jersey$img_JerseyText$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
						}
						break;
					case "WITH_IMAGE":
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
								"$In$SelectPlayerType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player"+ row_id + 
								"$In$SelectPlayerType$ImageAll$img_PlayerImage" + "*TEXTURE*IMAGE SET "+ photos_path + match.getHomeTeam().getTeamName4().toUpperCase() + 
									"//" + hs.getPhoto() + KabaddiUtil.PNG_EXTENSION + "\0");
						
						break;
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player" + row_id + 
							"$In$txt_PlayerName*GEOM*TEXT SET " + hs.getTicker_name().toUpperCase() + "\0");
					
					TimeUnit.MILLISECONDS.sleep(l);
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$LineUp$Row" + row_id + 
							"$Out$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					
				}
				
				for(Formation form : formation) {
					if(form.getFormId() == match.getHomeTeamFormationId()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$txt_Formation*GEOM*TEXT SET " + form.getFormDescription() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticallNumber$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
						
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*X SET " + form.getFormOrds1X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player1*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds1Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*X SET " + form.getFormOrds2X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player2*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds2Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*X SET " + form.getFormOrds3X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player3*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds3Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*X SET " + form.getFormOrds4X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player4*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds4Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*X SET " + form.getFormOrds5X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player5*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds5Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*X SET " + form.getFormOrds6X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player6*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds6Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*X SET " + form.getFormOrds7X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player7*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds7Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*X SET " + form.getFormOrds8X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player8*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds8Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*X SET " + form.getFormOrds9X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player9*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds9Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*X SET " + form.getFormOrds10X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player10*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds10Y() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*X SET " + form.getFormOrds11X() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$TacticalAllGrp$TacticalImage$Player11*TRANSFORMATION*POSITION*Y SET " + form.getFormOrds11Y() + "\0");
					}
				}
				
				row_id_sub = 0;
				for(Player hsub : match.getHomeSubstitutes()) {
					row_id_sub = row_id_sub + 1;
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench"
							+ "*FUNCTION*Grid*num_row SET " + row_id_sub + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*PlayerIn" + row_id + " SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NumberGrp$txt_Number*GEOM*TEXT SET " + hsub.getJersey_number() + "\0");
					
					if(hsub.getSurname() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hsub.getFirstname().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + hsub.getSurname().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_FirstName*GEOM*TEXT SET " + hsub.getFull_name().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$NameAll$txt_LastName*GEOM*TEXT SET " + "" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
							"$Dehighlight$NameGrp$SelectCard*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					
					if(hsub.getCaptainGoalKeeper().equalsIgnoreCase(KabaddiUtil.GOAL_KEEPER)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "1" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All_FullFRames$TeamsAll$Team2$AllData$DataOut$Bench$Row" + row_id_sub + 
								"$Dehighlight$NameGrp$SelectGoalKeeper*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
				}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 FF_In 2.000 LineUp$Team1$DataIn 2.520 \0");
	}
	public void populateMatchDoublePromo(PrintWriter print_writer,String viz_scene,String day,Match match,List<Fixture> fixture,List<Team> team,List<Ground> ground, String session_selected_broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
			
			int row_id = 1 ,l=4;
			String Date = "",grou = "",newDate = "";
			Calendar cal = Calendar.getInstance();
			boolean is_date_found = false;
			
			String[] dateSuffix = {
					"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
					
					"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
					
					"th", "st", "nd", "rd", "th", "th", "th", "th", "th","th",
					
					"th", "st"
			};
			
			if(day.toUpperCase().equalsIgnoreCase("TODAY")) {
				Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$txt_Header*GEOM*TEXT SET " + "TODAY'S MATCHES" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
			}
			else if(day.toUpperCase().equalsIgnoreCase("TOMORROW")) {
				cal.add(Calendar.DATE, +1);
				Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$txt_Header*GEOM*TEXT SET " + "TOMORROW'S MATCHES" + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
			}else {
				cal.add(Calendar.DATE, +2);
				Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
				is_date_found = true;
				TimeUnit.MILLISECONDS.sleep(l);
			}
			
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + match.getTournament().toUpperCase() + "\0");
			TimeUnit.MILLISECONDS.sleep(l);
			
			
			for(int i = 0; i <= fixture.size()-1; i++) {
				if(fixture.get(i).getDate().equalsIgnoreCase(Date)) {
					for(int j = 0; j <= ground.size()-1; j++) {
						if(ground.get(j).getGroundId() == Integer.valueOf(fixture.get(i).getVenue())) {
							grou = ground.get(j).getFullname();
						}
					}
					if(is_date_found == true) {
						newDate = fixture.get(i).getDate().split("-")[0];
						if(Integer.valueOf(newDate) < 10) {
							newDate = newDate.replaceFirst("0", "");
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$txt_Header*GEOM*TEXT SET " + "MATCHES ON " + 
								newDate + dateSuffix[Integer.valueOf(newDate)] + " " + Month.of(Integer.valueOf(fixture.get(i).getDate().split("-")[1])) + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + "KALINGA STADIUM, BHUBANESWAR" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$GroupNameGrp$txt_GroupName*GEOM*TEXT SET " + 
							fixture.get(i).getGroupName() + "\0");
					
					if(day.toUpperCase().equalsIgnoreCase("TODAY")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TimeGrp$txt_Time*GEOM*TEXT SET " + 
								" " + "\0");
					}
					else if(day.toUpperCase().equalsIgnoreCase("TOMORROW")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TimeGrp$txt_Time*GEOM*TEXT SET " + 
								"KICK OFF AT " + fixture.get(i).getTime() + " LOCAL TIME" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TimeGrp$txt_Time*GEOM*TEXT SET " + 
								"KICK OFF AT " + fixture.get(i).getTime() + " LOCAL TIME" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp1$ImageGrp$LogoImageGrp1$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(fixture.get(i).getHometeamid() - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp1$ImageGrp$LogoImageGrp2$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(fixture.get(i).getHometeamid() - 1).getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp1$ImageGrp$LogoImageGrp3$img_BadgesOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(fixture.get(i).getHometeamid() - 1).getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp1$ImageGrp$LogoImageGrp4$img_Badges"
//							+ "*TEXTURE*IMAGE SET "+ logo_path + team.get(fixture.get(i).getHometeamid() - 1).getTeamName4().toLowerCase() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp2$ImageGrp$LogoImageGrp1$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(fixture.get(i).getAwayteamid() - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp2$ImageGrp$LogoImageGrp2$img_Badges"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(fixture.get(i).getAwayteamid() - 1).getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp2$ImageGrp$LogoImageGrp3$img_BadgesOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(fixture.get(i).getAwayteamid() - 1).getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp2$ImageGrp$LogoImageGrp4$img_Badges"
//							+ "*TEXTURE*IMAGE SET "+ logo_path + team.get(fixture.get(i).getAwayteamid() - 1).getTeamName4().toLowerCase() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$SeparatorGrpGrp$txt_Separator*GEOM*TEXT SET " + "VS" + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp1$TeamData1$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + 
							team.get(fixture.get(i).getHometeamid() - 1).getTeamName1().toUpperCase() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$DoubleMatchId$AllData$Group" + row_id + "$TeamGrp2$TeamData2$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + 
							team.get(fixture.get(i).getAwayteamid() - 1).getTeamName1().toUpperCase() + "\0");
					
					row_id = row_id +1;
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 FF_In 2.000 DoubleID_In 2.600 \0");
		}
	}
	public void populatePointsTable(PrintWriter print_writer,String viz_sence_path,String Group,List<LeagueTeam> point_table, List<Team> team,String session_selected_broadcaster,Match match) throws InterruptedException, IOException 
	{		
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
				"GROUP WINNER WILL QUALIFY FOR THE SEMI-FINALS" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + "" + "\0");
		
		int row_no=0,omo=0,l=4;
		String cout = "";
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$txt_Header*GEOM*TEXT SET " + "POINTS TABLE" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + match.getTournament() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style*FUNCTION*Omo*vis_con SET " + "0" + "\0");
		TimeUnit.MILLISECONDS.sleep(l);
		
		if(Group.equalsIgnoreCase("LeagueTableA")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$GroupHeadGrp1$"
					+ "txt_Group*GEOM*TEXT SET " + "GROUP A" + "\0");
		}else if(Group.equalsIgnoreCase("LeagueTableB")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$GroupHeadGrp1$txt_Group*GEOM*TEXT SET " + "GROUP B" + "\0");
		}else if(Group.equalsIgnoreCase("LeagueTableC")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$GroupHeadGrp1$txt_Group*GEOM*TEXT SET " + "GROUP C" + "\0");
		}else if(Group.equalsIgnoreCase("LeagueTableD")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$GroupHeadGrp1$txt_Group*GEOM*TEXT SET " + "GROUP D" + "\0");
		}
		
		for(int i = 0; i <= point_table.size() - 1 ; i++) {
			row_no = row_no + 1;
			
			if(match.getHomeTeam().getTeamName2().equalsIgnoreCase(point_table.get(i).getTeamName()) || 
					match.getAwayTeam().getTeamName2().equalsIgnoreCase(point_table.get(i).getTeamName())){
				omo=1;
				cout="$Highlight";
			}else {
				omo=0;
				cout="$Dehighlight";
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType*FUNCTION*Omo*vis_con SET " + omo + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType" + cout + "$Text$txt_Rank*GEOM*TEXT SET " + row_no + "\0");
			
			for(Team tm : team) {
				if(point_table.get(i).getTeamName().equalsIgnoreCase("GOA")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
							"$SelectType" + cout + "$Text$img_TeamBadge*TEXTURE*IMAGE SET "+ flag_path + "fcg" + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
					
					if(point_table.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + "FC GOA" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + "FC GOA (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					TimeUnit.MILLISECONDS.sleep(l);
					
				}else if(tm.getTeamName1().contains(point_table.get(i).getTeamName())) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
							"$SelectType" + cout + "$Text$img_TeamBadge*TEXTURE*IMAGE SET "+ flag_path + tm.getTeamName4() + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
					
					if(point_table.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1().toUpperCase() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1().toUpperCase() + " (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType" + cout + "$Text$PointsData$txt_PlayedValue*GEOM*TEXT SET " + point_table.get(i).getPlayed() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType" + cout + "$Text$PointsData$txt_WinValue*GEOM*TEXT SET " + point_table.get(i).getWon() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType" + cout + "$Text$PointsData$txt_DrawValue*GEOM*TEXT SET " + point_table.get(i).getLost() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType" + cout + "$Text$PointsData$txt_LostValue*GEOM*TEXT SET " + point_table.get(i).getDrawn() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType" + cout + "$Text$PointsData$txt_GoalDifferenceValue*GEOM*TEXT SET " + point_table.get(i).getGD() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group1$PointsData1$Row" + row_no + 
					"$SelectType" + cout + "$Text$PointsData$txt_PointsValue*GEOM*TEXT SET " + point_table.get(i).getPoints() + "\0");

		}
//		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.020 FF_In 2.000 PlayOffs_In 2.700 \0");
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.020 FF_In 2.000 PointsTable_In 2.580 \0");
		
	}
	public void populatePointsTableGrp(PrintWriter print_writer,String viz_sence_path,String Group,List<LeagueTeam> point_table1,List<LeagueTeam> point_table2, List<Team> team,String session_selected_broadcaster,Match match) throws InterruptedException, IOException 
	{		
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
		
		
		int row_no_1=0,row_no_2=0,omo=0,l=4;
		String cout = "";
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$txt_Header*GEOM*TEXT SET " + "GROUP STANDINGS" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + match.getTournament() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style*FUNCTION*Omo*vis_con SET " + "1" + "\0");
		TimeUnit.MILLISECONDS.sleep(l);
		
		if(Group.equalsIgnoreCase("SemiFinal1")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$GroupHeadGrp$txt_Group*GEOM*TEXT SET " + "GROUP A" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$GroupHeadGrp$txt_Group*GEOM*TEXT SET " + "GROUP B" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
					"GROUP A WINNER WILL PLAY GROUP B WINNER IN THE 1st SEMI-FINAL" + "\0");
			
		}else if(Group.equalsIgnoreCase("SemiFinal2")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$GroupHeadGrp$txt_Group*GEOM*TEXT SET " + "GROUP C" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$GroupHeadGrp$txt_Group*GEOM*TEXT SET " + "GROUP D" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
					"GROUP C WINNER WILL PLAY GROUP D WINNER IN THE 2nd SEMI-FINAL" + "\0");
		}
		
		for(int i = 0; i <= point_table1.size() - 1 ; i++) {
			row_no_1 = row_no_1 + 1;
			
			if(match.getHomeTeam().getTeamName2().equalsIgnoreCase(point_table1.get(i).getTeamName()) || 
					match.getAwayTeam().getTeamName2().equalsIgnoreCase(point_table1.get(i).getTeamName())){
				omo=1;
				cout="$Highlight";
			}else {
				omo=0;
				cout="$Dehighlight";
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType*FUNCTION*Omo*vis_con SET " + omo + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$txt_Rank*GEOM*TEXT SET " + row_no_1 + "\0");
			
			for(Team tm : team) {
				if(point_table1.get(i).getTeamName().equalsIgnoreCase("GOA")) {
					if(point_table1.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + "FC GOA" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + "FC GOA (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					
					TimeUnit.MILLISECONDS.sleep(l);
					
				}else if(tm.getTeamName1().contains(point_table1.get(i).getTeamName())) {
					
					if(point_table1.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1().toUpperCase() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1().toUpperCase() + " (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_PlayedValue*GEOM*TEXT SET " + point_table1.get(i).getPlayed() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_WinValue*GEOM*TEXT SET " + point_table1.get(i).getWon() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_DrawValue*GEOM*TEXT SET " + point_table1.get(i).getLost() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_LostValue*GEOM*TEXT SET " + point_table1.get(i).getDrawn() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_GoalDifferenceValue*GEOM*TEXT SET " + point_table1.get(i).getGD() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_PointsValue*GEOM*TEXT SET " + point_table1.get(i).getPoints() + "\0");

		}
		
		for(int i = 0; i <= point_table2.size() - 1 ; i++) {
			row_no_2 = row_no_2 + 1;
			
			if(match.getHomeTeam().getTeamName2().equalsIgnoreCase(point_table2.get(i).getTeamName()) || 
					match.getAwayTeam().getTeamName2().equalsIgnoreCase(point_table2.get(i).getTeamName())){
				omo=1;
				cout="$Highlight";
			}else {
				omo=0;
				cout="$Dehighlight";
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType*FUNCTION*Omo*vis_con SET " + omo + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout + "$Text$txt_Rank*GEOM*TEXT SET " + row_no_2 + "\0");
			
			for(Team tm : team) {
				if(point_table2.get(i).getTeamName().equalsIgnoreCase("GOA")) {
					if(point_table2.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + "FC GOA" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + "FC GOA (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					TimeUnit.MILLISECONDS.sleep(l);
					
				}else if(tm.getTeamName1().contains(point_table2.get(i).getTeamName())) {
					if(point_table2.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1().toUpperCase() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
								"$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1().toUpperCase() + " (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout + "$Text$PointsData$txt_PlayedValue*GEOM*TEXT SET " + point_table2.get(i).getPlayed() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout + "$Text$PointsData$txt_WinValue*GEOM*TEXT SET " + point_table2.get(i).getWon() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout + "$Text$PointsData$txt_DrawValue*GEOM*TEXT SET " + point_table2.get(i).getLost() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout + "$Text$PointsData$txt_LostValue*GEOM*TEXT SET " + point_table2.get(i).getDrawn() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout + "$Text$PointsData$txt_GoalDifferenceValue*GEOM*TEXT SET " + point_table2.get(i).getGD() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout + "$Text$PointsData$txt_PointsValue*GEOM*TEXT SET " + point_table2.get(i).getPoints() + "\0");

		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.020 FF_In 2.000 PointsTable_In 2.580 \0");
		
	}
	public void populateFixtures(PrintWriter print_writer,String viz_sence_path,String Group,String header,List<Fixture> fixture,List<Team> team,List<Ground> ground,String session_selected_broadcaster,Match match) throws InterruptedException, IOException 
	{		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
//				"GROUP WINNER WILL QUALIFY FOR THE SEMI-FINALS" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$BottomInfoGrp*ACTIVE SET 0\0");
		int row_no=0,omo=0;
		String cout = "",match_name="",new_date="";
		
		String[] dateSuffix = {
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th","th",
				
				"th", "st"
		};
				  
		
		
		match_name = match.getMatchFileName().replace(".xml", "");
		
		switch(header.toUpperCase()) {
		case "FIXTURE":
			if(Group.equalsIgnoreCase("group A")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "FIXTURES - GROUP A" + "\0");
			}else if(Group.equalsIgnoreCase("group B")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "FIXTURES - GROUP B" + "\0");
			}else if(Group.equalsIgnoreCase("group C")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "FIXTURES - GROUP C" + "\0");
			}else if(Group.equalsIgnoreCase("group D")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "FIXTURES - GROUP D" + "\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + "GROUP STAGE" + "\0");

			break;
		case "RESULT":
			if(Group.equalsIgnoreCase("group A")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "RESULTS - GROUP A" + "\0");
			}else if(Group.equalsIgnoreCase("group B")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "RESULTS - GROUP B" + "\0");
			}else if(Group.equalsIgnoreCase("group C")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "RESULTS - GROUP C" + "\0");
			}else if(Group.equalsIgnoreCase("group D")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$txt_Header*GEOM*TEXT SET " + "RESULTS - GROUP D" + "\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + "GROUP STAGE" + "\0");

			break;
		}
				
		for(int i=0;i<=fixture.size()-1;i++) {
			if(fixture.get(i).getGroupName().equalsIgnoreCase(Group.toUpperCase())) {
				row_no = row_no + 1;
				
				if(row_no <= 2) {
					new_date = fixture.get(i).getDate().split("-")[0];
					if(Integer.valueOf(new_date) < 10) {
						new_date = new_date.replaceFirst("0", "");
					}
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row1$DateGrp$txt_Date*GEOM*TEXT SET " + 
							new_date + dateSuffix[Integer.valueOf(new_date)] + " " + Month.of(Integer.valueOf(fixture.get(i).getDate().split("-")[1])) + "\0");
					
					if(match_name.equalsIgnoreCase(fixture.get(i).getMatchfilename())) {
						omo=1;
						cout="$Highlight";
					}else {
						omo=0;
						cout="$Dehighlight";
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row1$Grp" + row_no + 
							"$SelectMatchType*FUNCTION*Omo*vis_con SET " + omo + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row1$Grp" + row_no + "$SelectMatchType" + cout
							+ "$Team1$txt_TeamName1*GEOM*TEXT SET " + team.get(fixture.get(i).getHometeamid()-1).getTeamName1() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row1$Grp" + row_no + "$SelectMatchType" + cout
							+ "$Team1$txt_TeamName2*GEOM*TEXT SET " + team.get(fixture.get(i).getAwayteamid()-1).getTeamName1() + "\0");
					
					if(fixture.get(i).getMargin() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row1$Grp" + row_no + "$SelectMatchType" + cout
								+ "$Team1$txt_Separator*GEOM*TEXT SET " + "VS" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row1$Grp" + row_no + "$SelectMatchType" + cout
								+ "$Team1$txt_Separator*GEOM*TEXT SET " + fixture.get(i).getMargin() + "\0");
					}
					
					
					
				}else if(row_no > 2 && row_no <= 4) {
					new_date = fixture.get(i).getDate().split("-")[0];
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row2$DateGrp$txt_Date*GEOM*TEXT SET " + 
							new_date + dateSuffix[Integer.valueOf(new_date)] + " " + Month.of(Integer.valueOf(fixture.get(i).getDate().split("-")[1])) + "\0");
					
					if(match_name.equalsIgnoreCase(fixture.get(i).getMatchfilename())) {
						omo=1;
						cout="$Highlight";
					}else {
						omo=0;
						cout="$Dehighlight";
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row2$Grp" + row_no + 
							"$SelectMatchType*FUNCTION*Omo*vis_con SET " + omo + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row2$Grp" + row_no + "$SelectMatchType" + cout
							+ "$Team1$txt_TeamName1*GEOM*TEXT SET " + team.get(fixture.get(i).getHometeamid()-1).getTeamName1() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row2$Grp" + row_no + "$SelectMatchType" + cout
							+ "$Team1$txt_TeamName2*GEOM*TEXT SET " + team.get(fixture.get(i).getAwayteamid()-1).getTeamName1() + "\0");
					
					if(fixture.get(i).getMargin() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row2$Grp" + row_no + "$SelectMatchType" + cout
								+ "$Team1$txt_Separator*GEOM*TEXT SET " + "VS" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row2$Grp" + row_no + "$SelectMatchType" + cout
								+ "$Team1$txt_Separator*GEOM*TEXT SET " + fixture.get(i).getMargin() + "\0");
					}
					
					
				}else if(row_no > 4 && row_no <= 6) {
					new_date = fixture.get(i).getDate().split("-")[0];
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row3$DateGrp$txt_Date*GEOM*TEXT SET " + 
							new_date + dateSuffix[Integer.valueOf(new_date)] + " " + Month.of(Integer.valueOf(fixture.get(i).getDate().split("-")[1])) + "\0");
					
					if(match_name.equalsIgnoreCase(fixture.get(i).getMatchfilename())) {
						omo=1;
						cout="$Highlight";
					}else {
						omo=0;
						cout="$Dehighlight";
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row3$Grp" + row_no + 
							"$SelectMatchType*FUNCTION*Omo*vis_con SET " + omo + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row3$Grp" + row_no + "$SelectMatchType" + cout
							+ "$Team1$txt_TeamName1*GEOM*TEXT SET " + team.get(fixture.get(i).getHometeamid()-1).getTeamName1() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row3$Grp" + row_no + "$SelectMatchType" + cout
							+ "$Team1$txt_TeamName2*GEOM*TEXT SET " + team.get(fixture.get(i).getAwayteamid()-1).getTeamName1() + "\0");
					
					if(fixture.get(i).getMargin() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row3$Grp" + row_no + "$SelectMatchType" + cout
								+ "$Team1$txt_Separator*GEOM*TEXT SET " + "VS" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$Row3$Grp" + row_no + "$SelectMatchType" + cout
								+ "$Team1$txt_Separator*GEOM*TEXT SET " + fixture.get(i).getMargin() + "\0");
					}
				}
			}
		}

		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.020 FF_In 2.000 Fixtures_In 2.500 \0");
		
	}
	
	public void populateQulifiers(PrintWriter print_writer,String viz_sence_path,String session_selected_broadcaster,Match match) throws InterruptedException, IOException 
	{		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + 
				"RESULTS OF THE QUALIFIERS" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$FixturesAll$AllData$FixtureData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
				"ROUNDGLASS PUNJAB FC QUALIFIED DIRECTLY FOR THE GROUP STAGE" + "\0");
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.500 FF_In 2.000 Fixtures_In 2.500 \0");
		
	}
	
	public void populateLtPenalty(PrintWriter print_writer,String viz_scene,String valueToProcess,KabaddiService footballService,Match match,Clock clock, String session_selected_broadcaster) 
			throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			int l=200;
			int iHomeCont = 0, iAwayCont = 0;
			
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$TeamGrp1$txt_Name*GEOM*TEXT SET " + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$TeamGrp2$txt_Name*GEOM*TEXT SET " + match.getAwayTeam().getTeamName4() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$TeamGrp1$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getHomeTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$TeamGrp2$img_TeamColour*TEXTURE*IMAGE SET " + 
					colors_path + match.getAwayTeamJerseyColor() + KabaddiUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$Seperator$AllScoreGrp$txt_HomeScore*GEOM*TEXT SET " + match.getHomePenaltiesHits() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$MainScorePart$Seperator$AllScoreGrp$txt_AwayScore*GEOM*TEXT SET " + match.getAwayPenaltiesHits() + "\0");
			
			TimeUnit.MILLISECONDS.sleep(l);
			
			for(int p=1;p<=5;p++) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + p + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
						p + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + p + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
						p + "\0");
			}
			
			for(String pen : penalties)
			{
				if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
					
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					iHomeCont = iHomeCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "2" + "\0");
					
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES$" + "_" + KabaddiUtil.HIT)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.INCREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					iAwayCont = iAwayCont + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				}
				
				
				if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.HOME + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + iHomeCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					if(iHomeCont > 0) {
						iHomeCont = iHomeCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.HIT)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}else if(pen.toUpperCase().contains(KabaddiUtil.AWAY + "_" + KabaddiUtil.DECREMENT + "_" + "PENALTIES" + "_" + KabaddiUtil.MISS)) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + iAwayCont + 
							"$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
					if(iAwayCont > 0) {
						iAwayCont = iAwayCont - 1;
					}
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 1.700 \0");
		}
	}
	public void populateLtPenaltyChange(PrintWriter print_writer,Match match, String session_selected_broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			int iHomeCont = 0, iAwayCont = 0;
			int HomeTotal = 0,AwayTotal=0;
			
			iHomeCont = (match.getHomePenaltiesHits() + match.getHomePenaltiesMisses());
			iAwayCont = (match.getAwayPenaltiesHits() + match.getAwayPenaltiesMisses());
			
			HomeTotal = iHomeCont + 5;
			AwayTotal = iAwayCont + 5;
			
			if(((match.getHomePenaltiesHits()+match.getHomePenaltiesMisses())%5) == 0 && ((match.getAwayPenaltiesHits()+match.getAwayPenaltiesMisses())%5) == 0) {
				if(match.getHomePenaltiesHits() == match.getAwayPenaltiesHits()) {
					penalties = new ArrayList<String>();
						for(int p=1;p<=5;p++) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + p + "$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + p + "$SelectPenaltyType*FUNCTION*Omo*vis_con SET " + "0" + "\0");
						}
				}
			}
			
			for(int h=iHomeCont+1;h<=HomeTotal;h++) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$HomePenalties$" + (h-iHomeCont) + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
						h + "\0");
			}
			
			for(int a=iAwayCont+1;a<=AwayTotal;a++) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllOut$DataGrp$PenalyGrp$PenaltyDots$AwayPenalties$" + (a-iAwayCont) + "$SelectPenaltyType$txt_PenaltyNumber*GEOM*TEXT SET " + 
						a + "\0");
			}
			
		}
	}
	
	public void populateScoreUpdate(PrintWriter print_writer,String viz_scene,KabaddiService footballService,Match match,Clock clock, String session_selected_broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			int l=200;
			String h1="",h2="",h3="",h4="",a1="",a2="",a3="",a4="";
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET " + "2" + "\0");
			TimeUnit.MILLISECONDS.sleep(l);
			
			if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.HALF)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + 
						clock.getMatchHalves().toUpperCase() + " TIME" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.FULL)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + 
						clock.getMatchHalves().toUpperCase() + " TIME" + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_1$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_3$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_4$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_1$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_3$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_4$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$ScoreGtp$txt_Score*GEOM*TEXT SET " + match.getHomeTeamScore() + "-" + match.getAwayTeamScore() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$SubHeader$txt_Info*GEOM*TEXT SET " + match.getTournament() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_HomeTeam*GEOM*TEXT SET " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_AwayTeam*GEOM*TEXT SET " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
			
			
			List<String> home_stats = new ArrayList<String>();
			List<String> away_stats = new ArrayList<String>();
			List<Integer> plyr_ids = new ArrayList<Integer>();
			boolean plyr_exist = false;
 			String stats_txt = "",stats_txt_og = "";
 			
			for(int i=0; i<=match.getMatchStats().size()-1; i++) {
				
				if((match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.GOAL) 
						|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.PENALTY))) {
					
					plyr_exist = false;
					for(Integer plyr_id : plyr_ids) {
						if(match.getMatchStats().get(i).getPlayerId() == plyr_id && 
								(match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.GOAL) 
										|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.OWN_GOAL)
										|| match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.PENALTY))) {
							plyr_exist = true;
							break;
						}
					}

					if(plyr_exist == false) {
						plyr_ids.add(match.getMatchStats().get(i).getPlayerId());
						stats_txt = footballService.getPlayer(KabaddiUtil.PLAYER, 
							String.valueOf(match.getMatchStats().get(i).getPlayerId())).getTicker_name().toUpperCase()+ " " + 
							KabaddiFunctions.calExtraTimeGoal(match.getMatchStats().get(i).getMatchHalves(), match.getMatchStats().get(i).getTotalMatchSeconds()) + 
							KabaddiFunctions.goal_shortname(match.getMatchStats().get(i).getStats_type());
						
						for(int j=i+1; j<=match.getMatchStats().size()-1; j++) {
							if (match.getMatchStats().get(i).getPlayerId() == match.getMatchStats().get(j).getPlayerId()
								&& (match.getMatchStats().get(j).getStats_type().equalsIgnoreCase(KabaddiUtil.GOAL)
								|| match.getMatchStats().get(j).getStats_type().equalsIgnoreCase(KabaddiUtil.PENALTY))) {
								
								stats_txt = stats_txt.trim() + "," + 
								KabaddiFunctions.calExtraTimeGoal(match.getMatchStats().get(j).getMatchHalves(), match.getMatchStats().get(j).getTotalMatchSeconds()) 
										+ KabaddiFunctions.goal_shortname(match.getMatchStats().get(j).getStats_type());
							}
						}
						switch (KabaddiFunctions.getPlayerSquadType(match.getMatchStats().get(i).getPlayerId(),match.getMatchStats().get(i).getStats_type() ,match)) {
						case KabaddiUtil.HOME:
							home_stats.add(stats_txt);
							break;
						case KabaddiUtil.AWAY:
							away_stats.add(stats_txt);
							break;
						}
					}
				}else if(match.getMatchStats().get(i).getStats_type().equalsIgnoreCase(KabaddiUtil.OWN_GOAL)) {
					stats_txt_og = footballService.getPlayer(KabaddiUtil.PLAYER, 
							String.valueOf(match.getMatchStats().get(i).getPlayerId())).getTicker_name().toUpperCase()+ " " + 
							KabaddiFunctions.calExtraTimeGoal(match.getMatchStats().get(i).getMatchHalves(),match.getMatchStats().get(i).getTotalMatchSeconds()) + 
								KabaddiFunctions.goal_shortname(match.getMatchStats().get(i).getStats_type());
						
						switch (KabaddiFunctions.getPlayerSquadType(match.getMatchStats().get(i).getPlayerId(),match.getMatchStats().get(i).getStats_type() ,match)) {
						case KabaddiUtil.HOME:
							home_stats.add(stats_txt_og);
							break;
						case KabaddiUtil.AWAY:
							away_stats.add(stats_txt_og);
							break;
						}
				}
			}
			
			if(match.getHomeTeamScore() == 0 && match.getAwayTeamScore()==0) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
			}else {
				
				//System.out.println("Home:" + home_stats.size() + " Away : " + away_stats.size());
				
				if (home_stats.size() == 0) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
				}else if(home_stats.size() <= 2) { 
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}else if(home_stats.size() <= 4) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				}else if(home_stats.size() <= 6){
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "3" + "\0");
				}else if(home_stats.size() <= 8){
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "4" + "\0");
				}
				
				if (away_stats.size() == 0) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
				}else if(away_stats.size() <= 2) { 
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "1" + "\0");
				}else if(away_stats.size() <= 4) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "2" + "\0");
				}else if(away_stats.size() <= 6){
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "3" + "\0");
				}else if(away_stats.size() <= 8){
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "4" + "\0");
				}
				
			}
			
			for(int i=0;i<=home_stats.size()-1;i++) {
				if(i < 2) { 
					h1 = h1 + home_stats.get(i); 
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$First$txt_Scorer1*GEOM*TEXT SET " + h1 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 4) {
					h2 = h2 + home_stats.get(i);
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Second$txt_Scorer2*GEOM*TEXT SET " + h1 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$First$txt_Scorer1*GEOM*TEXT SET " + h2 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 6){
					h3 = h3 + home_stats.get(i);
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Third$txt_Scorer3*GEOM*TEXT SET " + h1 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Second$txt_Scorer2*GEOM*TEXT SET " + h2 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$First$txt_Scorer1*GEOM*TEXT SET " + h3 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 8){
					h4 = h4 + home_stats.get(i);
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Fourth$txt_Scorer4*GEOM*TEXT SET " + h1 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Third$txt_Scorer3*GEOM*TEXT SET " + h2 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Second$txt_Scorer2*GEOM*TEXT SET " + h3 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$First$txt_Scorer1*GEOM*TEXT SET " + h4 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			for(int i=0;i<=away_stats.size()-1;i++) {
				if(i < 2) { 
					a1 = a1 + away_stats.get(i); 
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select$First$txt_Scorer1*GEOM*TEXT SET " + a1 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 4) {
					a2 = a2 + away_stats.get(i);
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Second$txt_Scorer2*GEOM*TEXT SET " + a1 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$First$txt_Scorer1*GEOM*TEXT SET " + a2 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 6){
					a3 = a3 + away_stats.get(i);
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Third$txt_Scorer3*GEOM*TEXT SET " + a1 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Second$txt_Scorer2*GEOM*TEXT SET " + a2 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$First$txt_Scorer1*GEOM*TEXT SET " + a3 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}else if(i < 8){
					a4 = a4 + away_stats.get(i);
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Fourth$txt_Scorer4*GEOM*TEXT SET " + a1 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Third$txt_Scorer3*GEOM*TEXT SET " + a2 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$Second$txt_Scorer2*GEOM*TEXT SET " + a3 + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select$First$txt_Scorer1*GEOM*TEXT SET " + a4 + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");
		}
	}
	public void populateLtMatchId(PrintWriter print_writer,String viz_scene,KabaddiService footballService,Match match,Clock clock, String session_selected_broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET " + "2" + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_1$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_3$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_4$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_1$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_3$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_4$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
			
			if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.HALF)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + clock.getMatchHalves().toUpperCase() + " TIME" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.FULL)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + clock.getMatchHalves().toUpperCase() + " TIME" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.FIRST)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + "FIRST HALF" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.SECOND)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + "SECOND HALF" + "\0");
			}else if(match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.EXTRA1) || match.getClock().getMatchHalves().equalsIgnoreCase(KabaddiUtil.EXTRA2)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + "EXTRA TIME" + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$ScoreGtp$txt_Score*GEOM*TEXT SET " + match.getHomeTeamScore() + "-" + match.getAwayTeamScore() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_HomeTeam*GEOM*TEXT SET " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_AwayTeam*GEOM*TEXT SET " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
			
			String text_to_return = "";
			try (BufferedReader br = new BufferedReader(new FileReader(KabaddiUtil.KABADDI_DIRECTORY + "PenaltyResult.txt"))) {
				while((text_to_return = br.readLine()) != null) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$SubHeader$txt_Info*GEOM*TEXT SET " + text_to_return + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");
		}
	}
	public void populateLTMatchPromoSingle(PrintWriter print_writer,String viz_scene, int match_number ,List<Team> team,List<Fixture> fix,List<Ground>ground,Match match, String broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET " + "2" + "\0");
			
			
			for(Team TM : team) {
				if(fix.get(match_number - 1).getHometeamid() == TM.getTeamId()) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_1$img_LogoBW"
							+ "*TEXTURE*IMAGE SET "+ logo_bw_path + TM.getTeamName4().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ logo_path + TM.getTeamName4().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_3$img_LogoOutline"
							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_4$img_LogoOutline"
							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_HomeTeam*GEOM*TEXT SET " + TM.getTeamName1().toUpperCase() + "\0");
				}
				if(fix.get(match_number - 1).getAwayteamid() == TM.getTeamId()) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_1$img_LogoBW"
							+ "*TEXTURE*IMAGE SET "+ logo_bw_path + TM.getTeamName4().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ logo_path + TM.getTeamName4().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_3$img_LogoOutline"
							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_4$img_LogoOutline"
							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_AwayTeam*GEOM*TEXT SET " + TM.getTeamName1().toUpperCase() + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + fix.get(match_number - 1).getGroupName() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$ScoreGtp$txt_Score*GEOM*TEXT SET " + "VS" + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
			
			String Date = "";
			Calendar cal = Calendar.getInstance();
			Date =  new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
			if(fix.get(match_number-1).getDate().equalsIgnoreCase(Date)) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$SubHeader$txt_Info*GEOM*TEXT SET " + "COMING UP AT " + fix.get(match_number-1).getTime() 
						+ " LOCAL TIME" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$SubHeader$txt_Info*GEOM*TEXT SET " + fix.get(match_number-1).getDate() + 
						" AT " + fix.get(match_number-1).getTime() + " LOCAL TIME" + "\0");
			}
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");
		}
	}
	
	public void populateLTPlayerStats(PrintWriter print_writer,String viz_scene, int playerStatsId ,List<Team> team,List<PlayerStat> playerStats,Match match, String broadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getPlayer().getJersey_number() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Header$Dataall$TextGrp$txt_Name*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getPlayer().getFull_name() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Header$Dataall$TextGrp$txt_Info*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getSubHeader() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$LogoGrp$Ani_1$img_Logo*TEXTURE*IMAGE SET "+ Lt_flag_path + 
					team.get(playerStats.get(playerStatsId-1).getPlayer().getTeamId() - 1).getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$LogoGrp$Ani_2$img_Logo*TEXTURE*IMAGE SET "+ Lt_flag_path + 
					team.get(playerStats.get(playerStatsId-1).getPlayer().getTeamId() - 1).getTeamName4() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead1*TRANSFORMATION*POSITION*X SET " + "0" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead2*TRANSFORMATION*POSITION*X SET " + "420" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead3*TRANSFORMATION*POSITION*X SET " + "840" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead4*TRANSFORMATION*POSITION*X SET " + "1260" + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue1*TRANSFORMATION*POSITION*X SET " + "0" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue2*TRANSFORMATION*POSITION*X SET " + "420" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue3*TRANSFORMATION*POSITION*X SET " + "840" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue4*TRANSFORMATION*POSITION*X SET " + "1260" + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead1*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getHeadStats1() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead2*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getHeadStats2() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead3*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getHeadStats3() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead4*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getHeadStats4() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue1*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getValueStats1() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue2*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getValueStats2() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue3*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getValueStats3() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue4*GEOM*TEXT SET " + 
					playerStats.get(playerStatsId-1).getValueStats4() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead5*ACTIVE SET 0\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline1$InfoGrp$txt_StatHead6*ACTIVE SET 0\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue5*ACTIVE SET 0\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$NameSuperCard$Subline2$InfoGrp$txt_StatValue6*ACTIVE SET 0\0");
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.400 \0");
		}
	}
	
	public void populateStaff(PrintWriter print_writer,String viz_scene, Staff st,List<Team> team ,Match match, String selectedbroadcaster) throws InterruptedException, IOException{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET 0 \0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_1$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(st.getClubId() - 1).getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(st.getClubId() - 1).getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_3$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(st.getClubId() - 1).getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_4$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(st.getClubId() - 1).getTeamName4().toLowerCase() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + st.getName().toUpperCase() + "\0");
				
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
					st.getRole().toUpperCase() + ", " + team.get(st.getClubId() - 1).getTeamName1().toUpperCase() + "\0");
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");
		}
	}
	public void populateNameSuper(PrintWriter print_writer,String viz_scene, NameSuper ns ,Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			int l = 4;
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$Logo_Grp$Nquad$img_Badges"
					+ "*TEXTURE*IMAGE SET "+ logo2_path + "HeroTrination" + "\0");
			
			if(ns.getSponsor() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_1$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ logo_path + "Logo1" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_2$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ logo_path + "Logo1" + "\0");
//				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_3$img_LogoOutline"
//						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + "TLogo" + "\0");
//				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_4$img_LogoOutline"
//						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + "TLogo" + "\0");
			}else {
				if(ns.getSponsor().equalsIgnoreCase(KabaddiUtil.HOME)) {
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_1$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_3$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_4$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
					
				}else if(ns.getSponsor().equalsIgnoreCase(KabaddiUtil.AWAY)) {

					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_1$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_3$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_4$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
				}else {
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_1$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ logo_path + "Logo1" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ logo_path + "Logo1" + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_3$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + "TLogo" + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$LogoGrp$Ani_4$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + "TLogo" + "\0");
				}
				
			}
			
			if(ns.getFirstname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
						ns.getSurname() + "\0");
				
			}else if(ns.getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
						ns.getFirstname() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
						ns.getFirstname() + " " + ns.getSurname() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$GenericNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
					ns.getSubLine().toUpperCase() + "\0");
			TimeUnit.MILLISECONDS.sleep(l);
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");
		}
		
	}
	public void populateNameSuperPlayer(PrintWriter print_writer,String viz_scene, int TeamId, String captainGoalKeeper, int playerId, Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			String Home_or_Away="";
			int l = 4;
			
			if(captainGoalKeeper.equalsIgnoreCase("PLAYER OF THE MATCH")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET 8 \0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$BottomGrp$InfoGrp$InfoDataGrp$txt_BottomInfo*GEOM*TEXT SET " + 
						" OF THE MATCH " + "\0");
				
				if(TeamId == match.getHomeTeamId()) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_1$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_3$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_4$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
					
					Home_or_Away = match.getHomeTeam().getTeamName1().toUpperCase();
					for(Player hs : match.getHomeSquad()) {
						if(playerId == hs.getPlayerId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									hs.getFull_name().toUpperCase() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									hs.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
						}
					}
					for(Player hsub : match.getHomeSubstitutes()) {
						if(playerId == hsub.getPlayerId()) {
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									hsub.getFull_name().toUpperCase() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									hsub.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
						}
					}
				}
				else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_1$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_3$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$LogoGrp$Ani_4$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
					
					Home_or_Away = match.getAwayTeam().getTeamName1().toUpperCase();
					for(Player as : match.getAwaySquad()) {
						if(playerId == as.getPlayerId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									as.getFull_name().toUpperCase() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									as.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
						}
					}
					for(Player asub : match.getAwaySubstitutes()) {
						if(playerId == asub.getPlayerId()) {
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									asub.getFull_name().toUpperCase() + "\0");
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$Hero$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									asub.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
						}
					}
				}
				
				
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET 1 \0");
				
				if(TeamId == match.getHomeTeamId()) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_1$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_3$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_4$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
					
					Home_or_Away = match.getHomeTeam().getTeamName1().toUpperCase();
					for(Player hs : match.getHomeSquad()) {
						if(playerId == hs.getPlayerId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									hs.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									hs.getFull_name().toUpperCase() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
							if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("PLAYER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
								TimeUnit.MILLISECONDS.sleep(l);
							}else if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("Player_Role")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										hs.getRole().toUpperCase() + " , " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
							}
						}
					}
					for(Player hsub : match.getHomeSubstitutes()) {
						if(playerId == hsub.getPlayerId()) {
							
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									hsub.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									hsub.getFull_name().toUpperCase() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
							if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("PLAYER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
							}else if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("Player_Role")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										hsub.getRole().toUpperCase() + " , " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
							}
						}
					}
				}
				else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_1$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_2$img_Logo"
							+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_3$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$LogoGrp$Ani_4$img_LogoOutline"
//							+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
					TimeUnit.MILLISECONDS.sleep(l);
					
					Home_or_Away = match.getAwayTeam().getTeamName1().toUpperCase();
					for(Player as : match.getAwaySquad()) {
						if(playerId == as.getPlayerId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									as.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									as.getFull_name().toUpperCase() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
							if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("PLAYER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
							}else if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("Player_Role")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										as.getRole().toUpperCase() + " , " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
							}
						}
					}
					for(Player asub : match.getAwaySubstitutes()) {
						if(playerId == asub.getPlayerId()) {
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Number*GEOM*TEXT SET " + 
									asub.getJersey_number() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
									asub.getFull_name().toUpperCase() + "\0");
							TimeUnit.MILLISECONDS.sleep(l);
							
							if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("PLAYER")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
							}else if(captainGoalKeeper.toUpperCase().equalsIgnoreCase("Player_Role")) {
								print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
										asub.getRole().toUpperCase() + " , " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
							}
						}
					}
				}
				
				switch(captainGoalKeeper.toUpperCase())
				{
				case "CAPTAIN":
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
							captainGoalKeeper.toUpperCase() + " , " + Home_or_Away + "\0");
					break;
				/*case "PLAYER OF THE MATCH":
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
							"PLAYER OF THE MATCH " + "\0");
					break;*/
				case "KALINGA PLAYER OF THE MATCH":
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
							"KALINGA PLAYER OF THE MATCH " + "\0");
					break;	
				case "GOAL_KEEPER":
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
							"GOAL-KEEPER" + " , " + Home_or_Away + "\0");
					break;
				case "PLAYER_TODAY_GOAL":
					int player_goal_count=0;
					for(MatchStats ms : match.getMatchStats()) {
						if(ms.getStats_type().equalsIgnoreCase("goal")) {
							if(ms.getPlayerId() == playerId) {
								player_goal_count = player_goal_count + 1;
							}
						}else if(ms.getStats_type().equalsIgnoreCase("penalty")) {
							if(ms.getPlayerId() == playerId) {
								player_goal_count = player_goal_count + 1;
							}
						}
					}
					if(player_goal_count != 0) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
								"GOALS TODAY - " + player_goal_count + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
								Home_or_Away + "\0");
					}
					
					break;
				case "GOAL_SCORER":
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
							"GOAL SCORER" + " , " + Home_or_Away + "\0");
					break;
				case "CAPTAIN-GOALKEEPER":
					print_writer.println("-1 RENDERER*TREE*$Main$All$Select$PlayerNameSuper$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
							"CAPTAIN & GOAL-KEEPER" + " , " + Home_or_Away + "\0");
					break;
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");	
		}
	}
	public void populateNameSuperCard(PrintWriter print_writer,String viz_scene, int TeamId, String cardType, int playerId, Match match, String selectedbroadcaster) throws InterruptedException, IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		} else {
			
			int l = 4;
			print_writer.println("-1 RENDERER*TREE*$Main$Select*FUNCTION*Omo*vis_con SET 3 \0");
			
			if(TeamId == match.getHomeTeamId()) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_1$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_2$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getHomeTeam().getTeamName4() + "\0");
//				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_3$img_LogoOutline"
//						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
//				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_4$img_LogoOutline"
//						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getHomeTeam().getTeamName4().toLowerCase() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				
				for(Player hs : match.getHomeSquad()) {
					if(playerId == hs.getPlayerId()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Number*GEOM*TEXT SET " + hs.getJersey_number() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Name*GEOM*TEXT SET " + hs.getFull_name().toUpperCase() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
								hs.getRole().toUpperCase() + " , " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
						
					}
				}
				for(Player hsub : match.getHomeSubstitutes()) {
					if(playerId == hsub.getPlayerId()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Number*GEOM*TEXT SET " + hsub.getJersey_number() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Name*GEOM*TEXT SET " + hsub.getFull_name().toUpperCase() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
								hsub.getRole().toUpperCase() + " , " + match.getHomeTeam().getTeamName1().toUpperCase() + "\0");
					}
				}
			}
			else {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_1$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_2$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + match.getAwayTeam().getTeamName4() + "\0");
//				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_3$img_LogoOutline"
//						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
//				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$LogoGrp$Ani_4$img_LogoOutline"
//						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + match.getAwayTeam().getTeamName4().toLowerCase() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				
				for(Player as : match.getAwaySquad()) {
					if(playerId == as.getPlayerId()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Number*GEOM*TEXT SET " + as.getJersey_number() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Name*GEOM*TEXT SET " + as.getFull_name().toUpperCase() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
								as.getRole().toUpperCase() + " , " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
					}
				}
				for(Player asub : match.getAwaySubstitutes()) {
					if(playerId == asub.getPlayerId()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Number*GEOM*TEXT SET " + asub.getJersey_number() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$txt_Name*GEOM*TEXT SET " + asub.getFull_name().toUpperCase() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
						
						print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$BottomGrp$InfoGrp$txt_BottomInfo*GEOM*TEXT SET " + 
								asub.getRole().toUpperCase() + " , " + match.getAwayTeam().getTeamName1().toUpperCase() + "\0");
					}
				}
			}
			
			switch(cardType.toUpperCase())
			{
			case KabaddiUtil.YELLOW:
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$Card$Select*FUNCTION*Omo*vis_con SET 0 \0");
				TimeUnit.MILLISECONDS.sleep(l);
				break;
			case KabaddiUtil.RED:
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$Card$Select*FUNCTION*Omo*vis_con SET 1 \0");
				TimeUnit.MILLISECONDS.sleep(l);
				break;
			case "YELLOW_RED":
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$NameSuperCard$Header$Dataall$Card$Select*FUNCTION*Omo*vis_con SET 2 \0");
				TimeUnit.MILLISECONDS.sleep(l);
				break;
			}

			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");		
		}
	}
	public void populateSubstitute(PrintWriter print_writer,String viz_scene,int Team_id,String Num_Of_Subs,List<Player> plyr,List<Team> team, Match match, String session_selected_broadcaster) throws InterruptedException, IOException
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			int l = 200;
			List<Event> evnt = new ArrayList<Event>();
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET 4 \0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$LogoGrp$Ani_1$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(Team_id - 1).getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$LogoGrp$Ani_2$img_Logo"
					+ "*TEXTURE*IMAGE SET "+ Lt_flag_path + team.get(Team_id - 1).getTeamName4() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$LogoGrp$Ani_3$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(Team_id - 1).getTeamName4().toLowerCase() + "\0");
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$LogoGrp$Ani_4$img_LogoOutline"
//					+ "*TEXTURE*IMAGE SET "+ logo_outline_path + team.get(Team_id - 1).getTeamName4().toLowerCase() + "\0");
			
			for(int i = 0; i<=match.getEvents().size()-1; i++) { 
				if(match.getEvents().get(i).getEventType().equalsIgnoreCase("replace")) {
					if(Team_id ==plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
						if(match.getHomeTeamId() == plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
							evnt.add(match.getEvents().get(i)); 
						}else if(match.getAwayTeamId() == plyr.get(match.getEvents().get(i).getOnPlayerId()-1).getTeamId()) {
							evnt.add(match.getEvents().get(i)); 
						} 
					} 
				}
			}
			
			switch(Num_Of_Subs.toUpperCase())
			{
			case "SINGLE":
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$Header$Dataall$InPlayer$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$Header$Dataall$InPlayer$txt_Name*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFull_name().toUpperCase() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$Header$Dataall$OutPlayer$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$Header$Dataall$OutPlayer$txt_Name*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFull_name().toUpperCase() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$BottomGrp$InfoGrp$OutPlayer$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$BottomGrp$InfoGrp$OutPlayer$txt_Name*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getFull_name().toUpperCase() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$BottomGrp$InfoGrp$InPlayer$txt_Number*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() + "\0");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Subs$BottomGrp$InfoGrp$InPlayer$txt_Name*GEOM*TEXT SET " + 
						plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getFull_name().toUpperCase() + "\0");
				
				
				TimeUnit.MILLISECONDS.sleep(l);
				break;
			/*case "DOUBLE":
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vNumberOfIn 1;");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vNumberOfOut 1;");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber2A " + plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName2A " + plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow2A " + logo_path + "Red_Arrow" + FootballUtil.PNG_EXTENSION + ";");
			
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber1A " + plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName1A " + plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow1A " + logo_path + "Green_Arrow" + FootballUtil.PNG_EXTENSION + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber2B " + plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName2B " + plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow2B " + logo_path + "Red_Arrow" + FootballUtil.PNG_EXTENSION + ";");
			
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber1B " + plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName1B " + plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow1B " + logo_path + "Green_Arrow" + FootballUtil.PNG_EXTENSION + ";");
				
				break;
				
			case "TRIPLE":
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vNumberOfIn 2;");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vNumberOfOut 2;");
				TimeUnit.MILLISECONDS.sleep(l);
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber2A " + plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName2A " + plyr.get(evnt.get(evnt.size() - 3).getOffPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow2A " + logo_path + "Red_Arrow" + FootballUtil.PNG_EXTENSION + ";");
			
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber1A " + plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName1A " + plyr.get(evnt.get(evnt.size() - 3).getOnPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow1A " + logo_path + "Green_Arrow" + FootballUtil.PNG_EXTENSION + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber2B " + plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName2B " + plyr.get(evnt.get(evnt.size() - 2).getOffPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow2B " + logo_path + "Red_Arrow" + FootballUtil.PNG_EXTENSION + ";");
			
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber1B " + plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName1B " + plyr.get(evnt.get(evnt.size() - 2).getOnPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow1B " + logo_path + "Green_Arrow" + FootballUtil.PNG_EXTENSION + ";");
				
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber2C " + plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName2C " + plyr.get(evnt.get(evnt.size() - 1).getOffPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow2C " + logo_path + "Red_Arrow" + FootballUtil.PNG_EXTENSION + ";");
			
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerNumber1C " + plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getJersey_number() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerName1C " + plyr.get(evnt.get(evnt.size() - 1).getOnPlayerId() - 1).getTicker_name().toUpperCase() +";");
				print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgArrow1C " + logo_path + "Green_Arrow" + FootballUtil.PNG_EXTENSION + ";");
				
				break;*/
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");	
		}
	}
	public void populateOfficials(PrintWriter print_writer,String viz_scene,List<Officials> officials,Match match, String session_selected_broadcaster) throws InterruptedException, IOException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET 5 \0");

			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Officials$Header$Dataall$txt_Name*GEOM*TEXT SET " + 
					officials.get(0).getReferee()  + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Officials$BottomGrp$InfoGrp$txt_Name*GEOM*TEXT SET " + 
					officials.get(0).getAssistantReferee1() + " & " + officials.get(0).getAssistantReferee2() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$LT_Officials$BottomGrp$Dataall2$txt_Name*GEOM*TEXT SET " + 
					officials.get(0).getFourthOfficial() + "\0");
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");	
		}
	}
	public void populateHeatMapPeakDistance(PrintWriter print_writer,String viz_scene,int TeamId,String Value,int Playerid,List<Player> plyr,Match match, String session_selected_broadcaster) throws InterruptedException, IOException, SAXException, ParserConfigurationException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			int team_number=0;
			String team_name="";
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET 7 \0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$HeatMap$HeatMap$Header$Dataall$txt_Name$txt_Number*GEOM*TEXT SET " + 
					plyr.get(Playerid-1).getJersey_number()  + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$HeatMap$HeatMap$Header$Dataall$txt_Name$txt_PlayerName*GEOM*TEXT SET " + 
					plyr.get(Playerid-1).getTicker_name()  + "\0");
			
			if(match.getHomeTeamId() == TeamId) {
				team_number = 0;
				team_name = match.getHomeTeam().getTeamName4();
			}else if(match.getAwayTeamId() == TeamId){
				team_number = 1;
				team_name = match.getAwayTeam().getTeamName4();
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$HeatMap$HeatMap$Header$Dataall$txt_Name$txt_TeamName*GEOM*TEXT SET " + 
					team_name  + "\0");
			
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$HeatMapAll$HeatMapGrp$Logo_Grp$Nquad$img_Badges" + "*TEXTURE*IMAGE SET "+ photos_path + 
//					team_name + "\\" + plyr.get(Playerid-1).getPhoto() + FootballUtil.PNG_EXTENSION + "\0");
			
//			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$HeatMapAll$HeatMapGrp$BandAll$NameDataGrp$Field$Football_Pitch*ACTIVE SET 0 \0");
			
			switch(Value.toUpperCase()) {
			case "HEATMAP":
				TimeUnit.SECONDS.sleep(2);
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$HeatMap$HeatMap$DataAll$DataOut$Ground$img_HeatMap*TEXTURE*IMAGE SET "+ image_path + 
						"playerheatmap" + team_number + "_" + plyr.get(Playerid-1).getJersey_number() + ".jpg" + "\0");
				break;
			case "PEAKDISTANCE":
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$HeatMap$HeatMap$DataAll$DataOut$Ground$img_HeatMap*TEXTURE*IMAGE SET "+ image_path + 
						"playerpeakdistancegraph" + team_number + "_" + plyr.get(Playerid-1).getJersey_number() + ".jpg" + "\0");
				break;
			}
			  
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");	
		}
	}
	public void populateTopStats(PrintWriter print_writer,String viz_scene,String Value,List<TeamStats>teamStats,List<Player> player,List<Team> team,Match match, String session_selected_broadcaster) throws InterruptedException, IOException, SAXException, ParserConfigurationException 
	{
		if (match == null) {
			this.status = "ERROR: Match is null";
		}else {
			
			String team_name="";
			int row = 0;
			
			List<PlayerStats> top_stats = new ArrayList<PlayerStats>();
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$Out$Select_EventLogo*FUNCTION*Omo*vis_con SET " + "1" + "\0");
			
			if(Value.toUpperCase().equalsIgnoreCase("TEAM TOP SPEED")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_Header*GEOM*TEXT SET " + 
						"TOP SPEED" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_SubHeader*GEOM*TEXT SET " + 
						"(KPH)" + "\0");
			}else if(Value.toUpperCase().equalsIgnoreCase("HIGHEST DISTANCE")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_Header*GEOM*TEXT SET " + 
						"HIGHEST DISTANCE" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_SubHeader*GEOM*TEXT SET " + 
						"(KMS)" + "\0");
			}else if(Value.toUpperCase().equalsIgnoreCase("BEST RUNNER")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_Header*GEOM*TEXT SET " + 
						"TOP RUNNER" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_SubHeader*GEOM*TEXT SET " + 
						"(KPH)" + "\0");
			}else if(Value.toUpperCase().equalsIgnoreCase("BEST SPRINTER")) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_Header*GEOM*TEXT SET " + 
						"TOP SPRINTS" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$HeaderGrp$txt_SubHeader*GEOM*TEXT SET " + 
						"" + "\0");
			}
			
			
	        for(int i=0;i<= teamStats.size()-1;i++) {
        		for(int j=0;j<= teamStats.get(i).getTopStats().size()-1;j++) {
        			if(teamStats.get(i).getTopStats().get(j).getHeader().equalsIgnoreCase(Value)) {
        				//System.out.println(teamStats.get(i).getTopStats().toString());
        				for(PlayerStats ps : teamStats.get(i).getTopStats().get(j).getPlayersStats()) {
        					top_stats.add(ps);
        				}
		        	}
        		}
	        }
	        
	        Collections.sort(top_stats,new KabaddiFunctions.PlayerStatsComparator());
	        
	        for(int m=0; m<= top_stats.size() - 1; m++) {
	        	row = row + 1;
	        	if(row <= 5) {
					for(Team tm : team) {
						if(top_stats.get(m).getTeam_name().equalsIgnoreCase(tm.getTeamName5())) {
							team_name = tm.getTeamName3();
						}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row" + row + "$Dehighlight$StatGrp$"
							+ "txt_Name*GEOM*TEXT SET " + top_stats.get(m).getFirst_name().toUpperCase() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row" + row + "$Dehighlight$StatGrp$"
							+ "txt_TeamName*GEOM*TEXT SET " + team_name + "\0");
					
					if(Value.toUpperCase().equalsIgnoreCase("HIGHEST DISTANCE")) {
						DecimalFormat df_bo = new DecimalFormat("0.0");
						double df = Double.valueOf(top_stats.get(m).getValue())/1000;
						df_bo.setRoundingMode(RoundingMode.UP); 
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row" + row + "$Dehighlight$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
								df_bo.format(df) + "\0");
					}else if(Value.toUpperCase().equalsIgnoreCase("TEAM TOP SPEED")) {
						DecimalFormat df_ts = new DecimalFormat("0.0");
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row" + row + "$Dehighlight$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
								df_ts.format(Double.valueOf(top_stats.get(m).getValue())) + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main$All$AllDataGrp$DataAll$Side" + which_side + "$LineUp$Row" + row + "$Dehighlight$StatGrp$txt_StatValue*GEOM*TEXT SET " + 
								top_stats.get(m).getValue() + "\0");
					}
	        	}
			}
//	        print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.340 \0");
		}
	}
	
	public void populateTeamFixture(PrintWriter print_writer,String viz_scene,int TeamId,Match match, String broadcaster,List<Team> team, List<Fixture> fixture) throws InterruptedException, IOException 
	{

		int row_id = 0;
		String newDate = "";
		
		String[] dateSuffix = {
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th","th",
				
				"th", "st"
		};
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$Header$Dataall$sUBgRP$txt_Sub*GEOM*"
				+ "TEXT SET " + "FIXTURES"  + "\0");
		
		for(Fixture fix : fixture) {
			if(fix.getHometeamid() == TeamId) {
				row_id = row_id + 1; 
				newDate = fix.getDate().split("-")[0];
				if(Integer.valueOf(newDate) < 10) {
					newDate = newDate.replaceFirst("0", "");
				}
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$NameGrp$txt_Fixture*GEOM*"
						+ "TEXT SET " + " " + "\0");
				
				for(Team tm : team) {
					if(tm.getTeamId() == fix.getHometeamid()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$Header$Dataall$txt_Name*GEOM*"
								+ "TEXT SET " + tm.getTeamName1()  + "\0");
					}
					
					if(tm.getTeamId() == fix.getAwayteamid()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$NameGrp$txt_Name*GEOM*"
								+ "TEXT SET " + "vs " + tm.getTeamName1()  + "\0");
					}
				}
				
				if(fix.getMargin() != null && !fix.getMargin().isEmpty()) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$StatValueGrp$txt_Value*GEOM*"
							+ "TEXT SET " + fix.getMargin()  + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$StatValueGrp$txt_Value*GEOM*"
							+ "TEXT SET " + newDate + dateSuffix[Integer.valueOf(newDate)] + " " + Month.of(Integer.valueOf(fix.getDate().split("-")[1]))  + "\0");
				}
				
			}else if(fix.getAwayteamid() == TeamId) {
				row_id = row_id + 1; 
				newDate = fix.getDate().split("-")[0];
				if(Integer.valueOf(newDate) < 10) {
					newDate = newDate.replaceFirst("0", "");
				}
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$NameGrp$txt_Fixture*GEOM*"
						+ "TEXT SET " + " " + "\0");
				
				for(Team tm : team) {
					
					if(tm.getTeamId() == fix.getAwayteamid()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$Header$Dataall$txt_Name*GEOM*"
								+ "TEXT SET " + tm.getTeamName1()  + "\0");
					}
					
					if(tm.getTeamId() == fix.getHometeamid()) {
						print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$NameGrp$txt_Name*GEOM*"
								+ "TEXT SET " + "vs " + tm.getTeamName1()  + "\0");
					}
				}
				if(fix.getMargin() != null && !fix.getMargin().isEmpty()) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$StatValueGrp$txt_Value*GEOM*"
							+ "TEXT SET " + fix.getMargin()  + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$Top5All$DataAll$ScorrerTeam1$Row" + row_id + "$StatGrp$StatValueGrp$txt_Value*GEOM*"
							+ "TEXT SET " + newDate + dateSuffix[Integer.valueOf(newDate)] + " " + Month.of(Integer.valueOf(fix.getDate().split("-")[1]))  + "\0");
				}
			}
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.320 \0");
		
	}
	public void populatePlayoffs(PrintWriter print_writer,String viz_scene,List<Playoff> playoffs,List<Team> team,List<VariousText> vt, String broadcaster,Match match) throws InterruptedException, IOException 
	{
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$txt_Header*GEOM*TEXT SET " + "ROAD TO THE FINAL"  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + "KNOCKOUT STAGE"  + "\0");
		for(VariousText vartext : vt) {
			if(vartext.getVariousType().equalsIgnoreCase("PLAYOFFSFOOTER") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.YES)) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + vartext.getVariousText()  + "\0");
			}else if(vartext.getVariousType().equalsIgnoreCase("PLAYOFFSFOOTER") && vartext.getUseThis().toUpperCase().equalsIgnoreCase(KabaddiUtil.NO)) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
							""  + "\0");
			}
		}
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
				""  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$GroupNameGrp$txt_MatchNumber*GEOM*TEXT SET " + "SEMI-FINAL 1"  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$GroupNameGrp$txt_MatchNumber*GEOM*TEXT SET " + "SEMI-FINAL 2"  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$GroupNameGrp$txt_MatchNumber*GEOM*TEXT SET " + "FINAL"  + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$txt_Date*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$txt_Date*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$txt_Date*ACTIVE SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$txt_Date*GEOM*TEXT SET " + ""  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$txt_Date*GEOM*TEXT SET " + ""  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$txt_Date*GEOM*TEXT SET " + ""  + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$TeamData1$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + playoffs.get(0).getTeam1().toUpperCase()  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$TeamData2$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + playoffs.get(0).getTeam2().toUpperCase()  + "\0");
		
		if(playoffs.get(0).getMargin() != null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$TeamData1$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + playoffs.get(0).getMargin().split("-")[0]  + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$TeamData2$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + playoffs.get(0).getMargin().split("-")[1]  + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$TeamData1$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + ""  + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group1$TeamData2$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + ""  + "\0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$TeamData1$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + playoffs.get(1).getTeam1().toUpperCase()  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$TeamData2$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + playoffs.get(1).getTeam2().toUpperCase()  + "\0");
		
		if(playoffs.get(1).getMargin() != null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$TeamData1$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + playoffs.get(1).getMargin().split("-")[0]  + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$TeamData2$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + playoffs.get(1).getMargin().split("-")[1]  + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$TeamData1$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + ""  + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group2$TeamData2$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + ""  + "\0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$TeamData1$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + playoffs.get(2).getTeam1().toUpperCase()  + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$TeamData2$TeamAll$Team$txt_TeamName*GEOM*TEXT SET " + playoffs.get(2).getTeam2().toUpperCase()  + "\0");
		
		if(playoffs.get(2).getMargin() != null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$TeamData1$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + playoffs.get(2).getMargin().split("-")[0]  + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$TeamData2$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + playoffs.get(2).getMargin().split("-")[1]  + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$TeamData1$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + ""  + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PlayOffs$AllData$Group3$TeamData2$TeamAll$Team$txt_TeamScore*GEOM*TEXT SET " + ""  + "\0");
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 0.020 FF_In 2.000 PlayOffs_In 2.700 \0");
		
	}
	public void populateMatchResult(PrintWriter print_writer,String viz_sence_path,int match_number,List<Fixture> fix,List<Team> team,List<Ground> ground,String session_selected_broadcaster,Match match) throws InterruptedException, IOException 
	{		
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$Select*FUNCTION*Omo*vis_con SET " + "2" + "\0");
		
		for(Team TM : team) {
			if(fix.get(match_number - 1).getHometeamid() == TM.getTeamId()) {

				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_1$img_LogoBW"
						+ "*TEXTURE*IMAGE SET "+ logo_bw_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_2$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ logo_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_3$img_LogoOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HomeLogoGrp$Ani_4$img_LogoOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_HomeTeam*GEOM*TEXT SET " + TM.getTeamName1().toUpperCase() + "\0");
			}
			if(fix.get(match_number - 1).getAwayteamid() == TM.getTeamId()) {
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_1$img_LogoBW"
						+ "*TEXTURE*IMAGE SET "+ logo_bw_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_2$img_Logo"
						+ "*TEXTURE*IMAGE SET "+ logo_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_3$img_LogoOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$AwayLogoGrp$Ani_4$img_LogoOutline"
						+ "*TEXTURE*IMAGE SET "+ logo_outline_path + TM.getTeamName4().toLowerCase() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$txt_AwayTeam*GEOM*TEXT SET " + TM.getTeamName1().toUpperCase() + "\0");
			}
		}
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$txt_Time*GEOM*TEXT SET " + "" + "\0");
		
		if(fix.get(match_number - 1).getMargin() != null) {
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$ScoreGtp$txt_Score*GEOM*TEXT SET " + fix.get(match_number - 1).getMargin() + "\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$ScoreGtp$txt_Score*GEOM*TEXT SET " + "" + "\0");
		}
		
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Home$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$Header$Dataall$Scorers$Away$Select*FUNCTION*Omo*vis_con SET " + "0" + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$Select$ScoreLine$HeaderAll$SubHeader$txt_Info*GEOM*TEXT SET " + ground.get(fix.get(match_number -1).getVenue() - 1).getFullname() + "\0");
		
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.340 \0");
		
	}
	public void populateRoadToFinal(PrintWriter print_writer,String viz_sence_path,List<LeagueTeam> point_table1,List<LeagueTeam> point_table2, List<Team> team,String session_selected_broadcaster,Match match) throws InterruptedException, IOException 
	{		
		
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$All_FF_Required$HashTag*ACTIVE SET 0 \0");
		
		
		int row_no_1=0,row_no_2=0,omo=0,omo1=0,l=4;
		String cout = "",cout1="";
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$txt_Header*GEOM*TEXT SET " + "ROAD TO THE FINAL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$SubHeadGrp$txt_SubHead*GEOM*TEXT SET " + "GROUP STAGE" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style*FUNCTION*Omo*vis_con SET " + "1" + "\0");
		TimeUnit.MILLISECONDS.sleep(l);
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$"
				+ "GroupHeadGrp$txt_Group*GEOM*TEXT SET " + "GROUP " + match.getHomeTeam().getTeamGroup() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$"
				+ "GroupHeadGrp$txt_Group*GEOM*TEXT SET " + "GROUP " + match.getAwayTeam().getTeamGroup() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$BottomInfoGrp$txt_Info*GEOM*TEXT SET " + 
				"GROUP WINNERS QUALIFIED FOR THE SEMI-FINALS" + "\0");
		
		for(int i = 0; i <= point_table1.size() - 1 ; i++) {
			row_no_1 = row_no_1 + 1;
			
			if(match.getHomeTeam().getTeamName2().equalsIgnoreCase(point_table1.get(i).getTeamName())){
				omo=1;
				cout="$Highlight";
			}else {
				omo=0;
				cout="$Dehighlight";
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 +
					"$SelectType*FUNCTION*Omo*vis_con SET " + omo + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$txt_Rank*GEOM*TEXT SET " + row_no_1 + "\0");
			
			for(Team tm : team) {
				if(tm.getTeamName2().equalsIgnoreCase(point_table1.get(i).getTeamName())) {
					if(point_table1.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" 
								+ row_no_1 + "$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" 
								+ row_no_1 + "$SelectType" + cout + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1() + " (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
					TimeUnit.MILLISECONDS.sleep(l);
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_PlayedValue*GEOM*TEXT SET " + point_table1.get(i).getPlayed() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_WinValue*GEOM*TEXT SET " + point_table1.get(i).getWon() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_DrawValue*GEOM*TEXT SET " + point_table1.get(i).getLost() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_LostValue*GEOM*TEXT SET " + point_table1.get(i).getDrawn() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_GoalDifferenceValue*GEOM*TEXT SET " + point_table1.get(i).getGD() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData1$Row" + row_no_1 + 
					"$SelectType" + cout + "$Text$PointsData$txt_PointsValue*GEOM*TEXT SET " + point_table1.get(i).getPoints() + "\0");
			
		}
		
		for(int i = 0; i <= point_table2.size() - 1 ; i++) {
			row_no_2 = row_no_2 + 1;
			
			if(match.getAwayTeam().getTeamName2().equalsIgnoreCase(point_table2.get(i).getTeamName())){
				omo1=1;
				cout1="$Highlight";
			}else {
				omo1=0;
				cout1="$Dehighlight";
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 +
					"$SelectType*FUNCTION*Omo*vis_con SET " + omo1 + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout1 + "$Text$txt_Rank*GEOM*TEXT SET " + row_no_2 + "\0");
			
			for(Team tm : team) {
				if(tm.getTeamName2().equalsIgnoreCase(point_table2.get(i).getTeamName())) {
					
					if(point_table2.get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row"
								+ row_no_2 + "$SelectType" + cout1 + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" 
								+ row_no_2 + "$SelectType" + cout1 + "$Text$txt_TeamName*GEOM*TEXT SET " + tm.getTeamName1() + " (Q)" + "\0");
						TimeUnit.MILLISECONDS.sleep(l);
					}
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout1 + "$Text$PointsData$txt_PlayedValue*GEOM*TEXT SET " + point_table2.get(i).getPlayed() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout1 + "$Text$PointsData$txt_WinValue*GEOM*TEXT SET " + point_table2.get(i).getWon() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout1 + "$Text$PointsData$txt_DrawValue*GEOM*TEXT SET " + point_table2.get(i).getLost() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout1 + "$Text$PointsData$txt_LostValue*GEOM*TEXT SET " + point_table2.get(i).getDrawn() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout1 + "$Text$PointsData$txt_GoalDifferenceValue*GEOM*TEXT SET " + point_table2.get(i).getGD() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$All_FullFRames$PointsTableAll$AllData$Select_PointsData_style$Group2$PointsData2$Row" + row_no_2 + 
					"$SelectType" + cout1 + "$Text$PointsData$txt_PointsValue*GEOM*TEXT SET " + point_table2.get(i).getPoints() + "\0");
			

		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.020 FF_In 2.000 PointsTable_In 2.580 \0");
		
	}
}