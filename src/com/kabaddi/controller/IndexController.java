package com.kabaddi.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kabaddi.broadcaster.Kabaddi;
import com.kabaddi.containers.Scene;
import com.kabaddi.containers.ScoreBug;
import com.kabaddi.model.Clock;
import com.kabaddi.model.Configurations;
import com.kabaddi.model.Event;
import com.kabaddi.model.EventFile;
import com.kabaddi.model.LiveMatchData;
import com.kabaddi.model.Match;
import com.kabaddi.model.MatchStats;
import com.kabaddi.model.Player;
import com.kabaddi.service.KabaddiService;
import com.kabaddi.util.KabaddiFunctions;
import com.kabaddi.util.KabaddiUtil;
import com.opencsv.exceptions.CsvException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class IndexController 
{
	@Autowired
	KabaddiService kabaddiService;
	
	public static String expiry_date = "2024-12-31";
	public static String current_date = "";
	public static String error_message = "";
	public static Clock session_clock = new Clock();
	public static Configurations session_configurations;
	public static List<PrintWriter> print_writers;
	public static Match session_match;
	public static EventFile session_event;
	public static String session_selected_broadcaster;
	public static Socket session_socket;
	public static Kabaddi session_kabaddi;
	public static List<Scene> session_selected_scenes;
	
	public static String access_token = "";
	public static String wtp = "",vtp = "";
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) 
		throws IOException, JAXBException 
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = KabaddiFunctions.getOnlineCurrentDate();
		}
		model.addAttribute("session_viz_scenes", new File(KabaddiUtil.KABADDI_DIRECTORY + 
				KabaddiUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".via") && pathname.isFile();
		    }
		}));

		model.addAttribute("match_files", new File(KabaddiUtil.KABADDI_DIRECTORY 
				+ KabaddiUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".xml") && pathname.isFile();
		    }
		}));
		
		model.addAttribute("configuration_files", new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CONFIGURATIONS_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".xml") && pathname.isFile();
		    }
		}));
		
		if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CONFIGURATIONS_DIRECTORY + KabaddiUtil.OUTPUT_XML).exists()) {
			session_configurations = (Configurations)JAXBContext.newInstance(
					Configurations.class).createUnmarshaller().unmarshal(
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CONFIGURATIONS_DIRECTORY 
					+ KabaddiUtil.OUTPUT_XML));
		} else {
			session_configurations = new Configurations();
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_configurations, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CONFIGURATIONS_DIRECTORY + 
					KabaddiUtil.OUTPUT_XML));
		}
		
		model.addAttribute("session_configurations",session_configurations);
	
		return "initialise";
	}
	
	@RequestMapping(value = {"/setup"}, method = RequestMethod.POST)
	public String setupPage(ModelMap model) throws JAXBException, IllegalAccessException, 
		InvocationTargetException, IOException, ParseException  
	{
		model.addAttribute("match_files", new File(KabaddiUtil.KABADDI_DIRECTORY + 
				KabaddiUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".xml") && pathname.isFile();
		    }
		}));
		model.addAttribute("session_match", session_match);
		model.addAttribute("teams", kabaddiService.getTeams());
		model.addAttribute("formations", kabaddiService.getFormations());
		model.addAttribute("teamcolor", kabaddiService.getTeamColors());
		model.addAttribute("grounds", kabaddiService.getGrounds());
		model.addAttribute("licence_expiry_message",
				"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
				new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));

		return "setup";
	}

	@RequestMapping(value = {"/match"}, method = {RequestMethod.POST,RequestMethod.GET})
	public String kabaddiMatchPage(ModelMap model,
		@RequestParam(value = "selectedBroadcaster", required = false, defaultValue = "") String selectedBroadcaster,
		@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddresss,
		@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber,
		@RequestParam(value = "vizSecondaryIPAddress", required = false, defaultValue = "") String vizSecondaryIPAddress,
		@RequestParam(value = "vizSecondaryPortNumber", required = false, defaultValue = "") int vizSecondaryPortNumber,
		@RequestParam(value = "vizScene", required = false, defaultValue = "") String vizScene)
			throws IOException, ParseException, JAXBException, InterruptedException, SAXException, ParserConfigurationException, FactoryConfigurationError  
	{
		if(current_date == null || current_date.isEmpty()) {
		
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
			
			session_configurations = new Configurations(selectedBroadcaster, vizIPAddresss, vizPortNumber, vizSecondaryIPAddress, vizSecondaryPortNumber);
			if(vizPortNumber != 0) {
				session_configurations.setPortNumber(Integer.valueOf(vizPortNumber));
			}

			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_configurations, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CONFIGURATIONS_DIRECTORY + 
					KabaddiUtil.OUTPUT_XML));
			
			print_writers = KabaddiFunctions.processPrintWriter(session_configurations);

			session_selected_broadcaster = selectedBroadcaster;
			session_selected_scenes = new ArrayList<Scene>();
			if(!vizIPAddresss.trim().isEmpty() && vizPortNumber != 0) {
				//System.out.println("Broad : " + session_selected_broadcaster + " Port : " + vizPortNumber);
				session_socket = new Socket(vizIPAddresss, Integer.valueOf(vizPortNumber));
				switch (session_selected_broadcaster.toUpperCase()) {
				case KabaddiUtil.KABADDI:
					session_selected_scenes.add(new Scene(KabaddiUtil.SUPER_CUP_SCORE_BUG_SCENE_PATH,KabaddiUtil.FRONT_LAYER)); // Front layer
					session_selected_scenes.add(new Scene(KabaddiUtil.SUPER_CUP_FF_SCENE_PATH,KabaddiUtil.BACK_LAYER)); // Back layer
					session_selected_scenes.add(new Scene("",KabaddiUtil.MIDDLE_LAYER));
					
					session_selected_scenes.get(0).scene_load(print_writers.get(0), session_selected_broadcaster);
					session_selected_scenes.get(1).scene_load(print_writers.get(0), session_selected_broadcaster);
					
					session_kabaddi = new Kabaddi();
					session_kabaddi.scorebug = new ScoreBug();
					
					break;
					
				}
			}
			//System.out.println(session_selected_scenes);
			model.addAttribute("match_files", new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".xml") && pathname.isFile();
			    }
			}));

			model.addAttribute("licence_expiry_message",
				"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
				new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));
			
			session_match = new Match();
			session_event = new EventFile();
			if(session_event.getEvents() == null || session_event.getEvents().size() <= 0)
				session_event.setEvents(new ArrayList<Event>());
			if(session_match.getMatchStats() == null || session_match.getMatchStats().size() <= 0) 
				session_match.setMatchStats(new ArrayList<MatchStats>());
			if(session_match.getClock() == null) 
				session_match.setClock(new Clock());
			
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
			model.addAttribute("session_match", session_match);
			model.addAttribute("session_event", session_event);
			model.addAttribute("session_configurations", session_configurations);
			model.addAttribute("session_socket", session_socket);
			model.addAttribute("session_selected_scenes", session_selected_scenes);
			
			return "match";
		}
	}
	
	@RequestMapping(value = {"/back_to_match"}, method = RequestMethod.POST)
	public String backToMatchPage(ModelMap model) throws ParseException
	{
		if(current_date == null || current_date.isEmpty()) {
		
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
		
			model.addAttribute("match_files", new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".xml") && pathname.isFile();
			    }
			}));
			model.addAttribute("licence_expiry_message",
				"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
				new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));
			
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
			model.addAttribute("session_match", session_match);

			return "match";
		
		}
	}	
	
	@RequestMapping(value = {"/upload_match_setup_data", "/reset_and_upload_match_setup_data"}
		,method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String uploadFormDataToSessionObjects(MultipartHttpServletRequest request) 
			throws IllegalAccessException, InvocationTargetException, JAXBException, IOException
	{
		if (request.getRequestURI().contains("upload_match_setup_data") 
				|| request.getRequestURI().contains("reset_and_upload_match_setup_data")) {
			
			List<Player> home_squad = new ArrayList<Player>(); List<Player> away_squad = new ArrayList<Player>();
			List<Player> home_substitutes = new ArrayList<Player>(); List<Player> away_substitutes = new ArrayList<Player>();

	   		boolean reset_all_variables = false;
			if(request.getRequestURI().contains("reset_and_upload_match_setup_data")) {
				reset_all_variables = true;
			} else if(request.getRequestURI().contains("upload_match_setup_data")) {
				for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
					if(entry.getKey().equalsIgnoreCase("select_existing_kabaddi_matches") && entry.getValue()[0].equalsIgnoreCase("new_match")) {
						reset_all_variables = true;
						break;
					}
				}
			}
			if(reset_all_variables == true) {
				session_match = new Match(); 
				session_event = new EventFile();
				session_event.setEvents(new ArrayList<Event>());
				session_match.setMatchStats(new ArrayList<MatchStats>());
			}
			
			for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
	   			if(entry.getKey().contains("_")) {
   					if(entry.getKey().split("_")[0].equalsIgnoreCase(KabaddiUtil.HOME + KabaddiUtil.PLAYER)) {
   						switch (Integer.parseInt(entry.getKey().split("_")[1])) {
   						case 1: case 2: case 3: case 4: case 5: case 6: case 7:
   		   					home_squad.add(new Player(Integer.parseInt(entry.getValue()[0]), 
   		   							Integer.parseInt(entry.getKey().split("_")[1]), KabaddiUtil.PLAYER));
   							break;
   						default:
   		   					home_substitutes.add(new Player(Integer.parseInt(entry.getValue()[0]), 
   		   							Integer.parseInt(entry.getKey().split("_")[1]), KabaddiUtil.SUBSTITUTE));
   							break;
   						}
   					} else if(entry.getKey().split("_")[0].equalsIgnoreCase(KabaddiUtil.AWAY + KabaddiUtil.PLAYER)) {
   						switch (Integer.parseInt(entry.getKey().split("_")[1])) {
   						case 1: case 2: case 3: case 4: case 5: case 6: case 7:
   		   					away_squad.add(new Player(Integer.parseInt(entry.getValue()[0]), 
   		   							Integer.parseInt(entry.getKey().split("_")[1]), KabaddiUtil.PLAYER));
   							break;
   						default:
   		   					away_substitutes.add(new Player(Integer.parseInt(entry.getValue()[0]), 
   		   							Integer.parseInt(entry.getKey().split("_")[1]), KabaddiUtil.SUBSTITUTE));
   							break;
   						}
   					}
	   			} else {
	   				BeanUtils.setProperty(session_match, entry.getKey(), entry.getValue()[0]);
	   			}
	   		}
			
			for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
	   			if(entry.getKey().contains("_")) {
	   				if(entry.getKey().split("_")[0].equalsIgnoreCase(KabaddiUtil.HOME + KabaddiUtil.CAPTAIN 
	   						+ KabaddiUtil.GOAL_KEEPER.replace("_", ""))) {
	   					for(Player plyr:home_squad) {
	   						if(plyr.getPlayerPosition() == Integer.parseInt(entry.getKey().split("_")[1])) {
	   							plyr.setCaptainGoalKeeper(entry.getValue()[0]);
	   						}
	   					}
	   					for(Player plyr:home_substitutes) {
	   						if(plyr.getPlayerPosition() == Integer.parseInt(entry.getKey().split("_")[1])) {
	   							plyr.setCaptainGoalKeeper(entry.getValue()[0]);
	   						}
	   					}
	   				} else if(entry.getKey().split("_")[0].equalsIgnoreCase(KabaddiUtil.AWAY + KabaddiUtil.CAPTAIN 
	   						+ KabaddiUtil.GOAL_KEEPER.replace("_", ""))) {
	   					for(Player plyr:away_squad) {
	   						if(plyr.getPlayerPosition() == Integer.parseInt(entry.getKey().split("_")[1])) {
	   							plyr.setCaptainGoalKeeper(entry.getValue()[0]);
	   						}
	   					}
	   					for(Player plyr:away_substitutes) {
	   						if(plyr.getPlayerPosition() == Integer.parseInt(entry.getKey().split("_")[1])) {
	   							plyr.setCaptainGoalKeeper(entry.getValue()[0]);
	   						}
	   					}
   					}
	   			}
	   		}

			session_match.setHomeSquad(home_squad);
			session_match.setAwaySquad(away_squad);
			
			Collections.sort(session_match.getHomeSquad());
			Collections.sort(session_match.getAwaySquad());

			session_match.setHomeSubstitutes(home_substitutes);
			session_match.setAwaySubstitutes(away_substitutes);
			
			Collections.sort(session_match.getHomeSubstitutes());
			Collections.sort(session_match.getAwaySubstitutes());
			
			session_match.setHomeOtherSquad(KabaddiFunctions.getPlayersFromDB(kabaddiService, KabaddiUtil.HOME, session_match));
			session_match.setAwayOtherSquad(KabaddiFunctions.getPlayersFromDB(kabaddiService, KabaddiUtil.AWAY, session_match));

			new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()).createNewFile();
			new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()).createNewFile();
			
			session_match = KabaddiFunctions.populateMatchVariables(kabaddiService, session_match);

			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));

			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));

		}
		session_match.setEvents(session_event.getEvents());
		return JSONObject.fromObject(session_match).toString();
	}
	
	@RequestMapping(value = {"/processKabaddiProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processKabaddiProcedures(
			@ModelAttribute("session_configurations") Configurations session_configurations,
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess)
					throws JAXBException, IllegalAccessException, InvocationTargetException, IOException, NumberFormatException, InterruptedException, 
						CsvException, SAXException, ParserConfigurationException
	{	
		System.out.println("whatToProcess = " + whatToProcess);
		System.out.println("valueToProcess = " + valueToProcess);
		Event this_event = new Event();
		if(session_selected_broadcaster != null && !session_selected_broadcaster.equalsIgnoreCase(KabaddiUtil.EURO_LEAGUE)) {
			if(!whatToProcess.equalsIgnoreCase(KabaddiUtil.LOAD_TEAMS)) {
				if(valueToProcess.contains(",")) {
					if(session_match.getMatchFileName() == null || session_match.getMatchFileName().isEmpty()) {
						session_match = (Match) JAXBContext.newInstance(Match.class).createUnmarshaller().unmarshal(
								new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + valueToProcess.split(",")[0]));
						
						session_event = (EventFile) JAXBContext.newInstance(EventFile.class).createUnmarshaller().unmarshal(
								new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + valueToProcess.split(",")[0]));
						
						session_match.setEvents(session_event.getEvents());
						session_match = KabaddiFunctions.populateMatchVariables(kabaddiService,session_match);
					}
				}
			}
		}
		switch (whatToProcess.toUpperCase()) {
		case "GET-CONFIG-DATA":
			session_configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
				new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CONFIGURATIONS_DIRECTORY 
						+ valueToProcess));
			
			return JSONObject.fromObject(session_configurations).toString();
		case KabaddiUtil.LOG_STAT:

			if(valueToProcess.toUpperCase().contains(KabaddiUtil.PENALTIES)) {
				if(valueToProcess.split(",")[1].split("_")[1].toUpperCase().contains(KabaddiUtil.INCREMENT)) {
					if(valueToProcess.split(",")[1].split("_")[0].toUpperCase().contains(KabaddiUtil.HOME)) {
						if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.HIT)) {
							session_match.setHomePenaltiesHits(session_match.getHomePenaltiesHits() + 1);
						}else if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.MISS)) {
							session_match.setHomePenaltiesMisses(session_match.getHomePenaltiesMisses() + 1);
						}
					}else if(valueToProcess.split(",")[1].split("_")[0].toUpperCase().contains(KabaddiUtil.AWAY)) {
						if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.HIT)) {
							session_match.setAwayPenaltiesHits(session_match.getAwayPenaltiesHits() + 1);
						}else if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.MISS)) {
							session_match.setAwayPenaltiesMisses(session_match.getAwayPenaltiesMisses() + 1);
						}
					}
				}else if(valueToProcess.split(",")[1].split("_")[1].toUpperCase().contains(KabaddiUtil.DECREMENT)) {
					if(valueToProcess.split(",")[1].split("_")[0].toUpperCase().contains(KabaddiUtil.HOME)) {
						if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.HIT)) {
							if(session_match.getHomePenaltiesHits() > 0) {
								session_match.setHomePenaltiesHits(session_match.getHomePenaltiesHits() - 1);
							}
						}else if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.MISS)) {
							if(session_match.getHomePenaltiesMisses() > 0) {
								session_match.setHomePenaltiesMisses(session_match.getHomePenaltiesMisses() - 1);
							}
						}
					}else if(valueToProcess.split(",")[1].split("_")[0].toUpperCase().contains(KabaddiUtil.AWAY)) {
						if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.HIT)) {
							if(session_match.getAwayPenaltiesHits() > 0) {
								session_match.setAwayPenaltiesHits(session_match.getAwayPenaltiesHits() - 1);
							}
						}else if(valueToProcess.split(",")[1].split("_")[3].toUpperCase().contains(KabaddiUtil.MISS)) {
							if(session_match.getAwayPenaltiesMisses() > 0) {
								session_match.setAwayPenaltiesMisses(session_match.getAwayPenaltiesMisses() - 1);
							}
						}
					}
				}
			}
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			switch (session_selected_broadcaster) {
			case KabaddiUtil.KABADDI:
				session_kabaddi.ProcessGraphicOption(print_writers.get(0),whatToProcess, session_match, session_clock, kabaddiService, 
						session_selected_scenes, valueToProcess);
				break;
			}
			//session_i_league.ProcessGraphicOption(whatToProcess,session_match, session_clock,footballService,session_socket, session_selected_scenes, valueToProcess);
			return JSONObject.fromObject(session_match).toString();
			
		case "RESET_PENALTY":
			
			session_match.setHomePenaltiesHits(0);
			session_match.setHomePenaltiesMisses(0);
			session_match.setAwayPenaltiesHits(0);
			session_match.setAwayPenaltiesMisses(0);

			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			return JSONObject.fromObject(session_match).toString();
		
		case "NAMESUPER_GRAPHICS-OPTIONS": case "BUG_DB_GRAPHICS-OPTIONS": case "STAFF_GRAPHICS-OPTIONS": case "PROMO_GRAPHICS-OPTIONS": case "LTPROMO_GRAPHICS-OPTIONS":
		case "SCOREBUGPROMO_GRAPHICS-OPTIONS":	case "RESULT_PROMO_GRAPHICS-OPTIONS": case "TEAMFIXTURE_GRAPHICS-OPTIONS": case "DB_GRAPHICS":
		case "LEADERBOARD_GRAPHICS-OPTIONS": case "LT_PLAYER_STATS_GRAPHICS-OPTIONS":
			switch (session_selected_broadcaster) {
			case KabaddiUtil.KABADDI:
				return session_kabaddi.ProcessGraphicOption(print_writers.get(0),whatToProcess, session_match, session_clock, 
						kabaddiService, session_selected_scenes, valueToProcess).toString();
			}
		case "POPULATE_RAIDER":
			//System.out.println("Size = " + valueToProcess.split(",").length);
			
			if(valueToProcess.split(",").length == 3) {
				if(valueToProcess.split(",")[2].equalsIgnoreCase("RAIDER")) {
					session_match.getMatchStats().add(new MatchStats(session_match.getMatchStats().size() + 1, Integer.valueOf(valueToProcess.split(",")[1]), 
							session_match.getClock().getMatchHalves(),"RAIDER", 0, session_match.getClock().getMatchTotalSeconds(),0,"YES"));
				}
			}else if(valueToProcess.split(",").length == 2) {
				session_match.getMatchStats().add(new MatchStats(session_match.getMatchStats().size() + 1, Integer.valueOf(valueToProcess.split(",")[1]), 
						session_match.getClock().getMatchHalves(),"RAIDER", 0, session_match.getClock().getMatchTotalSeconds(),0,"NO"));
			}
			
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			session_match.setEvents(session_event.getEvents());
			
			return JSONObject.fromObject(session_match).toString();
			
		case KabaddiUtil.REPLACE:
			
			Player store_player = new Player();
			for(int i=0 ; i<= session_match.getHomeSquad().size()-1;i++) {
				
				if(session_match.getHomeSquad().get(i).getPlayerId() == Integer.valueOf(valueToProcess.split(",")[1])) {
					store_player = session_match.getHomeSquad().get(i);
					session_match.getHomeSquad().remove(i);
					for(int j=0 ; j<= session_match.getHomeSubstitutes().size()-1;j++) {
						if(session_match.getHomeSubstitutes().get(j).getPlayerId() == Integer.valueOf(valueToProcess.split(",")[2])) {
							session_match.getHomeSquad().add(i, session_match.getHomeSubstitutes().get(j));
							session_match.getHomeSubstitutes().remove(j);
							session_match.getHomeSubstitutes().add(j, store_player);
						}
					}
				}
			}
			for(int i=0 ; i<= session_match.getAwaySquad().size()-1;i++) {
				
				if(session_match.getAwaySquad().get(i).getPlayerId() == Integer.valueOf(valueToProcess.split(",")[1])) {
					store_player = session_match.getAwaySquad().get(i);
					session_match.getAwaySquad().remove(i);
					for(int j=0 ; j<= session_match.getAwaySubstitutes().size()-1;j++) {
						if(session_match.getAwaySubstitutes().get(j).getPlayerId() == Integer.valueOf(valueToProcess.split(",")[2])) {
							session_match.getAwaySquad().add(i, session_match.getAwaySubstitutes().get(j));
							session_match.getAwaySubstitutes().remove(j);
							session_match.getAwaySubstitutes().add(j, store_player);
						}
					}
				}
			}
			
			session_event.getEvents().add(new Event(session_event.getEvents().size() + 1, 0, session_match.getClock().getMatchHalves(), 
					0,whatToProcess, "replace", Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[2]),0));
			
			session_match.getMatchStats().add(new MatchStats(session_match.getMatchStats().size() + 1, session_match.getClock().getMatchHalves(), 
					"REPLACE",session_match.getClock().getMatchTotalSeconds(), Integer.valueOf(valueToProcess.split(",")[1]), 
					Integer.valueOf(valueToProcess.split(",")[2]),Integer.valueOf(valueToProcess.split(",")[2])));
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			session_match.setEvents(session_event.getEvents());
			
			return JSONObject.fromObject(session_match).toString();
				
		case KabaddiUtil.LOG_EVENT:
			
			if(!valueToProcess.trim().isEmpty() && valueToProcess.contains(",") == true) {
				
				if(session_match.getMatchStats() == null || session_match.getMatchStats().size() <= 0) 
					session_match.setMatchStats(new ArrayList<MatchStats>());
				if(session_match.getEvents() == null || session_match.getEvents().size() <= 0) 
					session_match.setEvents(new ArrayList<Event>());
				
				session_match.getMatchStats().add(new MatchStats(session_match.getMatchStats().size() + 1, Integer.valueOf(valueToProcess.split(",")[2]), 
						session_match.getClock().getMatchHalves(),"POINTS", Integer.valueOf(valueToProcess.split(",")[1]), 
						session_match.getClock().getMatchTotalSeconds(),Integer.valueOf(valueToProcess.split(",")[2]),"NO"));
				
				if(session_match.getHomeTeamId() == Integer.valueOf(valueToProcess.split(",")[2])) {
					session_match.setHomeTeamScore(session_match.getHomeTeamScore() + Integer.valueOf(valueToProcess.split(",")[1]));	
				}
				
				if(session_match.getAwayTeamId() == Integer.valueOf(valueToProcess.split(",")[2])) {
					session_match.setAwayTeamScore(session_match.getAwayTeamScore() + Integer.valueOf(valueToProcess.split(",")[1]));
				}
				
				if(session_event.getEvents() == null || session_event.getEvents().size() <= 0) 
					session_event.setEvents(new ArrayList<Event>());
				
				session_event.getEvents().add(new Event(session_event.getEvents().size() + 1, Integer.valueOf(valueToProcess.split(",")[2]), 
						session_match.getClock().getMatchHalves(), session_match.getMatchStats().size(),whatToProcess, "POINTS", 0,0,
						Integer.valueOf(valueToProcess.split(",")[1])));
				
			}

			session_match = KabaddiFunctions.populateMatchVariables(kabaddiService, session_match);
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			session_match.setEvents(session_event.getEvents());
			
			return JSONObject.fromObject(session_match).toString();

		case "HOME_GOAL":
			
			if(session_match.getMatchStats() == null || session_match.getMatchStats().size() <= 0) 
				session_match.setMatchStats(new ArrayList<MatchStats>());
			if(session_match.getEvents() == null || session_match.getEvents().size() <= 0) 
				session_match.setEvents(new ArrayList<Event>());
			
			session_match.getMatchStats().add(new MatchStats(session_match.getMatchStats().size() + 1, session_match.getHomeSquad().get(0).getPlayerId(), 
					session_match.getClock().getMatchHalves(),"Home_Goal", 1, session_match.getClock().getMatchTotalSeconds(),
					session_match.getHomeSquad().get(0).getPlayerId(),"NO"));
			
			session_match.setHomeTeamScore(session_match.getHomeTeamScore() + 1);
			
			if(session_event.getEvents() == null || session_event.getEvents().size() <= 0) 
				session_event.setEvents(new ArrayList<Event>());
			
			session_event.getEvents().add(new Event(session_event.getEvents().size() + 1, session_match.getHomeSquad().get(0).getPlayerId(), 
					session_match.getClock().getMatchHalves(), session_match.getMatchStats().size(),whatToProcess, "Home_Goal", 0,0,1));

			session_match = KabaddiFunctions.populateMatchVariables(kabaddiService, session_match);
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			session_match.setEvents(session_event.getEvents());
			
			return JSONObject.fromObject(session_match).toString();
			
		case "AWAY_GOAL":
			
			if(session_match.getMatchStats() == null || session_match.getMatchStats().size() <= 0) 
				session_match.setMatchStats(new ArrayList<MatchStats>());
			if(session_match.getEvents() == null || session_match.getEvents().size() <= 0) 
				session_match.setEvents(new ArrayList<Event>());
			
			session_match.getMatchStats().add(new MatchStats(session_match.getMatchStats().size() + 1, session_match.getAwaySquad().get(0).getPlayerId(), 
					session_match.getClock().getMatchHalves(),"AWAY_GOAL", 1, session_match.getClock().getMatchTotalSeconds(),
					session_match.getAwaySquad().get(0).getPlayerId(),"NO"));
			
			session_match.setAwayTeamScore(session_match.getAwayTeamScore() + 1);
			
			if(session_event.getEvents() == null || session_event.getEvents().size() <= 0) 
				session_event.setEvents(new ArrayList<Event>());
			
			session_event.getEvents().add(new Event(session_event.getEvents().size() + 1, session_match.getAwaySquad().get(0).getPlayerId(), 
					session_match.getClock().getMatchHalves(), session_match.getMatchStats().size(),whatToProcess, "AWAY_GOAL", 0,0,1));

			session_match = KabaddiFunctions.populateMatchVariables(kabaddiService, session_match);
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
		
			session_match.setEvents(session_event.getEvents());
			
			return JSONObject.fromObject(session_match).toString();	

		case "LOG_OVERWRITE_MATCH_SUBS":
			//System.out.println(valueToProcess);
			if(valueToProcess.contains(",")) {
				int overwrite_palyer_off_id = 0,overwrite_palyer_on_id = 0;
				Player sub_store_player = new Player();
				if(session_event.getEvents() != null) {
					for(Event evnt : session_event.getEvents()) {
						if(evnt.getEventNumber() == Integer.valueOf(valueToProcess.split(",")[1])) {
							if(Integer.valueOf(valueToProcess.split(",")[3]) > 0 && Integer.valueOf(valueToProcess.split(",")[2]) == 0) {
								overwrite_palyer_off_id = evnt.getOnPlayerId();
								overwrite_palyer_on_id = Integer.valueOf(valueToProcess.split(",")[3]);
								
								evnt.setOnPlayerId(overwrite_palyer_on_id);
								
							}else if(Integer.valueOf(valueToProcess.split(",")[3]) == 0 && Integer.valueOf(valueToProcess.split(",")[2]) > 0) {
								overwrite_palyer_off_id = Integer.valueOf(valueToProcess.split(",")[2]);
								overwrite_palyer_on_id = evnt.getOffPlayerId();
								
								evnt.setOffPlayerId(overwrite_palyer_off_id);
								
							}else if(Integer.valueOf(valueToProcess.split(",")[3]) > 0 && Integer.valueOf(valueToProcess.split(",")[2]) > 0) {
								overwrite_palyer_off_id = Integer.valueOf(valueToProcess.split(",")[2]);
								overwrite_palyer_on_id = Integer.valueOf(valueToProcess.split(",")[3]);
								
								evnt.setOnPlayerId(overwrite_palyer_on_id);
								evnt.setOffPlayerId(overwrite_palyer_off_id);
								
							}
							
						}
					}
				}
				//System.out.println("ON - " + overwrite_palyer_on_id + " OFF - " + overwrite_palyer_off_id);
				for(int i=0 ; i<= session_match.getHomeSquad().size()-1;i++) {
					if(session_match.getHomeSquad().get(i).getPlayerId() == overwrite_palyer_off_id) {
						sub_store_player = session_match.getHomeSquad().get(i);
						session_match.getHomeSquad().remove(i);
						for(int j=0 ; j<= session_match.getHomeSubstitutes().size()-1;j++) {
							if(session_match.getHomeSubstitutes().get(j).getPlayerId() == overwrite_palyer_on_id) {
								session_match.getHomeSquad().add(i, session_match.getHomeSubstitutes().get(j));
								session_match.getHomeSubstitutes().remove(j);
								session_match.getHomeSubstitutes().add(j, sub_store_player);
							}
						}
					}
				}
				for(int i=0 ; i<= session_match.getAwaySquad().size()-1;i++) {
					if(session_match.getAwaySquad().get(i).getPlayerId() == overwrite_palyer_off_id) {
						sub_store_player = session_match.getAwaySquad().get(i);
						session_match.getAwaySquad().remove(i);
						for(int j=0 ; j<= session_match.getAwaySubstitutes().size()-1;j++) {
							if(session_match.getAwaySubstitutes().get(j).getPlayerId() == overwrite_palyer_on_id) {
								session_match.getAwaySquad().add(i, session_match.getAwaySubstitutes().get(j));
								session_match.getAwaySubstitutes().remove(j);
								session_match.getAwaySubstitutes().add(j, sub_store_player);
							}
						}
					}
				}
			}
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			return JSONObject.fromObject(session_match).toString();
		case "LOG_OVERWRITE_MATCH_STATS":
			int home_points = 0,away_points=0;
			if(valueToProcess.contains(",")) {
				if(session_match.getMatchStats() != null) {
					for(MatchStats ms : session_match.getMatchStats()) {
						if(ms.getStatsId() == Integer.valueOf(valueToProcess.split(",")[1])) {
							ms.setStatsCount(Integer.valueOf(valueToProcess.split(",")[2]));
							ms.setTotalMatchSeconds(Long.valueOf(valueToProcess.split(",")[3]));
						}
						if(ms.getTeamId() == session_match.getHomeTeamId()) {
							home_points = home_points + ms.getStatsCount();
							session_match.setHomeTeamScore(home_points);
						}else if(ms.getTeamId() == session_match.getAwayTeamId()) {
							away_points = away_points + ms.getStatsCount();
							session_match.setAwayTeamScore(away_points);
						}
					}
				}
				if(session_event.getEvents() != null) {
					for(Event evnt : session_event.getEvents()) {
						if(evnt.getStatsId() == Integer.valueOf(valueToProcess.split(",")[1])) {
							evnt.setEventScore(Integer.valueOf(valueToProcess.split(",")[2]));
							evnt.setEventLog("LOG_EVENT");
						}
					}
				}
			}

			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			session_match = KabaddiFunctions.populateMatchVariables(kabaddiService, session_match);
			session_match.setEvents(session_event.getEvents());

			return JSONObject.fromObject(session_match).toString();
		
		case KabaddiUtil.LOG_OVERWRITE_TEAM_SCORE: 
			
			session_match.setHomeTeamScore(Integer.valueOf(valueToProcess.split(",")[1]));
			session_match.setAwayTeamScore(Integer.valueOf(valueToProcess.split(",")[2]));

			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));

			return JSONObject.fromObject(session_match).toString();
		
		case "HOME_UNDO":
		
			if(session_event.getEvents() != null && 1 <= session_event.getEvents().size()) {
				
				for(int jUndo=1;jUndo<=1;jUndo++) {

					this_event = session_event.getEvents().get(session_event.getEvents().size() - 1);
					switch (this_event.getEventLog().toUpperCase()) {
					case "HOME_GOAL":
						switch (this_event.getEventType().toUpperCase()) {
						case "HOME_GOAL":
							this_event = session_event.getEvents().get(session_event.getEvents().size() - 1);
							for(Player plyr : session_match.getHomeSquad()) {
								if(plyr.getPlayerId() == this_event.getEventPlayerId()) {
									switch (this_event.getEventType().toUpperCase()) {
									case "HOME_GOAL":
										session_match.setHomeTeamScore(session_match.getHomeTeamScore() - 1);
										session_event.getEvents().remove(this_event);
										session_match.getMatchStats().remove(session_match.getMatchStats().get(session_match.getMatchStats().size() - 1));
										break;
									}
								}
							}
							for(Player plyr : session_match.getAwaySquad()) {
								if(plyr.getPlayerId() == this_event.getEventPlayerId()) {
									switch (this_event.getEventType().toUpperCase()) {
									case "HOME_GOAL":
										session_match.setAwayTeamScore(session_match.getAwayTeamScore() - 1);
										session_event.getEvents().remove(this_event);
										session_match.getMatchStats().remove(session_match.getMatchStats().get(session_match.getMatchStats().size() - 1));
										break;
									}
								}
							}
							
							break;
						}
						break;
					}
				}
			}
			
	
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
		
			session_match.setEvents(session_event.getEvents());
			return JSONObject.fromObject(session_match).toString();
			
		case "AWAY_UNDO":

			if(session_event.getEvents() != null && 1 <= session_event.getEvents().size()) {
				
				for(int jUndo=1;jUndo<=1;jUndo++) {

					this_event = session_event.getEvents().get(session_event.getEvents().size() - 1);
					
					switch (this_event.getEventLog().toUpperCase()) {
					case "AWAY_GOAL":
						switch (this_event.getEventType().toUpperCase()) {
						case "AWAY_GOAL":
							this_event = session_event.getEvents().get(session_event.getEvents().size() - 1);
							for(Player plyr : session_match.getHomeSquad()) {
								if(plyr.getPlayerId() == this_event.getEventPlayerId()) {
									switch (this_event.getEventType().toUpperCase()) {
									case "AWAY_GOAL":
										session_match.setHomeTeamScore(session_match.getHomeTeamScore() - 1);
										session_event.getEvents().remove(this_event);
										session_match.getMatchStats().remove(session_match.getMatchStats().get(session_match.getMatchStats().size() - 1));
										break;
									}
								}
							}
							for(Player plyr : session_match.getAwaySquad()) {
								if(plyr.getPlayerId() == this_event.getEventPlayerId()) {
									switch (this_event.getEventType().toUpperCase()) {
									case "AWAY_GOAL":
										session_match.setAwayTeamScore(session_match.getAwayTeamScore() - 1);
										session_event.getEvents().remove(this_event);
										session_match.getMatchStats().remove(session_match.getMatchStats().get(session_match.getMatchStats().size() - 1));
										break;
									}
								}
							}
							break;
						}
						break;
					}
				}
			}
			
	
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));
		
			session_match.setEvents(session_event.getEvents());
			return JSONObject.fromObject(session_match).toString();	
			
		case KabaddiUtil.UNDO:

			if(valueToProcess.contains(",")) {
				if(session_event.getEvents() != null && Integer.valueOf(valueToProcess.split(",")[1]) <= session_event.getEvents().size()) {
					for(int iUndo=1;iUndo<=Integer.valueOf(valueToProcess.split(",")[1]);iUndo++) {

						this_event = session_event.getEvents().get(session_event.getEvents().size() - 1);
						
						switch (this_event.getEventLog().toUpperCase()) {
						case KabaddiUtil.LOG_EVENT:
							switch (this_event.getEventType().toUpperCase()) {
							case "POINTS":
								this_event = session_event.getEvents().get(session_event.getEvents().size() - 1);
								session_match.getMatchStats().remove(session_match.getMatchStats().get(session_match.getMatchStats().size() - 1));
								if(session_match.getHomeTeamId() == this_event.getEventTeamId()) {
									switch (this_event.getEventType().toUpperCase()) {
									case "POINTS":
										session_match.setHomeTeamScore(session_match.getHomeTeamScore() - this_event.getEventScore());
										break;
									}
								}
								if(session_match.getAwayTeamId() == this_event.getEventTeamId()) {
									switch (this_event.getEventType().toUpperCase()) {
									case "POINTS":
										session_match.setAwayTeamScore(session_match.getAwayTeamScore() - this_event.getEventScore());
										break;
									}
								}
								break;
							case KabaddiUtil.GOAL: case KabaddiUtil.OWN_GOAL: case KabaddiUtil.PENALTY: case KabaddiUtil.YELLOW: case KabaddiUtil.RED:
							case KabaddiUtil.ASSISTS: case KabaddiUtil.SHOTS: case KabaddiUtil.SHOTS_ON_TARGET: case KabaddiUtil.OFF_SIDE: case KabaddiUtil.FOULS:
							case KabaddiUtil.CORNERS_CONVERTED: case KabaddiUtil.CORNERS: case KabaddiUtil.SAVES:
								this_event = session_event.getEvents().get(session_event.getEvents().size() - 1);
								session_match.getMatchStats().remove(session_match.getMatchStats().get(session_match.getMatchStats().size() - 1));
								for(Player plyr : session_match.getHomeSquad()) {
									if(plyr.getPlayerId() == this_event.getEventPlayerId()) {
										switch (this_event.getEventType().toUpperCase()) {
										case KabaddiUtil.GOAL: case KabaddiUtil.PENALTY:
											session_match.setHomeTeamScore(session_match.getHomeTeamScore() - 1);
											break;
										case KabaddiUtil.OWN_GOAL: 
											session_match.setAwayTeamScore(session_match.getAwayTeamScore() - 1);
											break;
										}
									}
								}
								for(Player plyr : session_match.getAwaySquad()) {
									if(plyr.getPlayerId() == this_event.getEventPlayerId()) {
										switch (this_event.getEventType().toUpperCase()) {
										case KabaddiUtil.GOAL: case KabaddiUtil.PENALTY:
											session_match.setAwayTeamScore(session_match.getAwayTeamScore() - 1);
											break;
										case KabaddiUtil.OWN_GOAL: 
											session_match.setHomeTeamScore(session_match.getHomeTeamScore() - 1);
											break;
										}
									}
								}
								break;
							}
							break;
						case KabaddiUtil.REPLACE:
							ArrayList<Player> undo_store_player = new ArrayList<Player>();
							for(int i=0 ; i<= session_match.getHomeSquad().size()-1;i++) {
								if(session_match.getHomeSquad().get(i).getPlayerId() == this_event.getOnPlayerId()) {
									undo_store_player.add(session_match.getHomeSquad().get(i));
									session_match.getHomeSquad().remove(i);
									for(int j=0 ; j<= session_match.getHomeSubstitutes().size()-1;j++) {
										if(session_match.getHomeSubstitutes().get(j).getPlayerId() == this_event.getOffPlayerId()) {
											session_match.getHomeSquad().add(i, session_match.getHomeSubstitutes().get(j));
											session_match.getHomeSubstitutes().remove(j);
											session_match.getHomeSubstitutes().add(j, undo_store_player.get(0));
											undo_store_player.remove(0);
										}
									}
								}
							}
							for(int i=0 ; i<= session_match.getAwaySquad().size()-1;i++) {
								if(session_match.getAwaySquad().get(i).getPlayerId() == this_event.getOnPlayerId()) {
									undo_store_player.add(session_match.getAwaySquad().get(i));
									session_match.getAwaySquad().remove(i);
									for(int j=0 ; j<= session_match.getAwaySubstitutes().size()-1;j++) {
										if(session_match.getAwaySubstitutes().get(j).getPlayerId() == this_event.getOffPlayerId()) {
											session_match.getAwaySquad().add(i, session_match.getAwaySubstitutes().get(j));
											session_match.getAwaySubstitutes().remove(j);
											session_match.getAwaySubstitutes().add(j, undo_store_player.get(0));
											undo_store_player.remove(0);
										}
									}
								}
							}
							break;
						}
						session_event.getEvents().remove(this_event);
					}
				}
			}
			
			
			JAXBContext.newInstance(EventFile.class).createMarshaller().marshal(session_event, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + session_match.getMatchFileName()));
			
			JAXBContext.newInstance(Match.class).createMarshaller().marshal(session_match, 
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()));

			session_match.setEvents(session_event.getEvents());
			return JSONObject.fromObject(session_match).toString();
			
		case KabaddiUtil.LOAD_TEAMS:
			if(!valueToProcess.trim().isEmpty()) {
				
				session_match.setHomeTeam(kabaddiService.getTeam(KabaddiUtil.TEAM, valueToProcess.split(",")[0]));
				session_match.setAwayTeam(kabaddiService.getTeam(KabaddiUtil.TEAM, valueToProcess.split(",")[1]));
				
				session_match.setHomeSquad(kabaddiService.getPlayers(KabaddiUtil.TEAM, valueToProcess.split(",")[0]));
				session_match.setAwaySquad(kabaddiService.getPlayers(KabaddiUtil.TEAM, valueToProcess.split(",")[1]));
			}
			
			return JSONObject.fromObject(session_match).toString();

		case KabaddiUtil.LOAD_MATCH: case KabaddiUtil.LOAD_SETUP:

			session_match = (Match) JAXBContext.newInstance(Match.class).createUnmarshaller().unmarshal(
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.MATCHES_DIRECTORY + valueToProcess));
			
			switch (whatToProcess.toUpperCase()) {
			case KabaddiUtil.LOAD_MATCH:
				
				if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + valueToProcess).exists()) {
					session_event = (EventFile) JAXBContext.newInstance(EventFile.class).createUnmarshaller().unmarshal(
							new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + valueToProcess));
				} else {
					session_event = new EventFile();
					new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.EVENT_DIRECTORY + valueToProcess).createNewFile();
				}

				if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CLOCK_XML).exists()) {
					session_match.setClock((Clock) JAXBContext.newInstance(Clock.class).createUnmarshaller().unmarshal(
							new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CLOCK_XML)));
				} else {
					session_match.setClock(new Clock());
				}
				break;
				
			}
			
			switch (whatToProcess.toUpperCase()) {
			case KabaddiUtil.LOAD_SETUP:
				session_match.setHomeOtherSquad(KabaddiFunctions.getPlayersFromDB(kabaddiService, KabaddiUtil.HOME, session_match));
				session_match.setAwayOtherSquad(KabaddiFunctions.getPlayersFromDB(kabaddiService, KabaddiUtil.AWAY, session_match));
				break;
			}
			session_match = KabaddiFunctions.populateMatchVariables(kabaddiService,session_match);

			session_match.setEvents(session_event.getEvents());

			return JSONObject.fromObject(session_match).toString();			
		
		case "READ_CLOCK":
			
			if(session_match != null) {
				if(new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CLOCK_XML).exists()) {
					session_clock = (Clock) JAXBContext.newInstance(Clock.class).createUnmarshaller().unmarshal(
							new File(KabaddiUtil.KABADDI_DIRECTORY + KabaddiUtil.CLOCK_XML));
					session_match.setClock(session_clock);
					
					switch (session_selected_broadcaster) {
					case KabaddiUtil.KABADDI:
						session_kabaddi.updateScoreBug(print_writers.get(0),session_selected_scenes, session_match,kabaddiService);
						break;
					}
				}
			}
			return JSONObject.fromObject(session_match).toString();
		
		default:
			switch (session_selected_broadcaster) {
			case KabaddiUtil.KABADDI:
				session_kabaddi.ProcessGraphicOption(print_writers.get(0),whatToProcess, session_match, session_clock, kabaddiService, 
						session_selected_scenes, valueToProcess);
				return JSONObject.fromObject(session_kabaddi).toString();
			}
			return JSONObject.fromObject(session_match).toString();
		}
	}
}