package com.kabaddi.broadcaster;

import com.kabaddi.containers.FootballData;
import com.kabaddi.containers.Scene;
import com.kabaddi.util.KabaddiUtil;

public class Kabaddi extends Scene{
	
	public String session_selected_broadcaster = KabaddiUtil.KABADDI;
	
	private String status;
	
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

}