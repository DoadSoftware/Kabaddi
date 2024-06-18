package com.kabaddi.containers;

public class Lband {
	
	private boolean Lband_on_screen;
	private boolean start_pause;
	private String which_graphics_onscreen ;
	private String sponsor_on_screen;
	private String bottom_sponsor_on_screen;
	private String which_Right_LBand_onscreen;
	private String which_Last_Bottom_LBand_onscreen;
	private String which_Last_Right_LBand_onscreen;
	private String which_Bottom_LBand_onscreen;
	private String playing_XI_Team;
	
	public Lband() {
		super();
		this.Lband_on_screen = false;
		this.which_Bottom_LBand_onscreen = "";
		this.which_Right_LBand_onscreen = "";
		this.which_Last_Bottom_LBand_onscreen = "";
		this.which_Last_Right_LBand_onscreen = "";
		this.which_graphics_onscreen="";
	}
	
	public boolean isLband_on_screen() {
		return Lband_on_screen;
	}
	
	public void setLband_on_screen(boolean lband_on_screen) {
		Lband_on_screen = lband_on_screen;
	}
	public boolean isStart_pause() {
		return start_pause;
	}
	public void setStart_pause(boolean start_pause) {
		this.start_pause = start_pause;
	}
	public String getWhich_graphics_onscreen() {
		return which_graphics_onscreen;
	}
	public void setWhich_graphics_onscreen(String which_graphics_onscreen) {
		this.which_graphics_onscreen = which_graphics_onscreen;
	}
	public String getSponsor_on_screen() {
		return sponsor_on_screen;
	}
	public void setSponsor_on_screen(String sponsor_on_screen) {
		this.sponsor_on_screen = sponsor_on_screen;
	}
	public String getBottom_sponsor_on_screen() {
		return bottom_sponsor_on_screen;
	}
	public void setBottom_sponsor_on_screen(String bottom_sponsor_on_screen) {
		this.bottom_sponsor_on_screen = bottom_sponsor_on_screen;
	}
	
	public String getWhich_Right_LBand_onscreen() {
		return which_Right_LBand_onscreen;
	}
	public void setWhich_Right_LBand_onscreen(String which_Right_LBand_onscreen) {
		this.which_Right_LBand_onscreen = which_Right_LBand_onscreen;
	}
	public String getWhich_Bottom_LBand_onscreen() {
		return which_Bottom_LBand_onscreen;
	}
	public void setWhich_Bottom_LBand_onscreen(String which_Bottom_LBand_onscreen) {
		this.which_Bottom_LBand_onscreen = which_Bottom_LBand_onscreen;
	}
	
	public String getWhich_Last_Bottom_LBand_onscreen() {
		return which_Last_Bottom_LBand_onscreen;
	}
	public void setWhich_Last_Bottom_LBand_onscreen(String which_Last_Bottom_LBand_onscreen) {
		this.which_Last_Bottom_LBand_onscreen = which_Last_Bottom_LBand_onscreen;
	}
	public String getWhich_Last_Right_LBand_onscreen() {
		return which_Last_Right_LBand_onscreen;
	}
	public void setWhich_Last_Right_LBand_onscreen(String which_Last_Right_LBand_onscreen) {
		this.which_Last_Right_LBand_onscreen = which_Last_Right_LBand_onscreen;
	}
	
	public String getPlaying_XI_Team() {
		return playing_XI_Team;
	}
	public void setPlaying_XI_Team(String playing_XI_Team) {
		this.playing_XI_Team = playing_XI_Team;
	}
	
	
}
