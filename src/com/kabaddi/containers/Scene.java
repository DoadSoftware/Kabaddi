package com.kabaddi.containers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import com.kabaddi.util.KabaddiUtil;

public class Scene {
	
	private String scene_path;
	private String broadcaster;
	private String which_layer;
	
	public Scene() {
		super();
	}

	public Scene(String scene_path, String which_layer) {
		super();
		this.scene_path = scene_path;
		this.which_layer = which_layer;
	}
	
	public String getScene_path() {
		return scene_path;
	}

	public void setScene_path(String scene_path) {
		this.scene_path = scene_path;
	}
	
	public String getBroadcaster() {
		return broadcaster;
	}

	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
	}

	public String getWhich_layer() {
		return which_layer;
	}

	public void setWhich_layer(String which_layer) {
		this.which_layer = which_layer;
	}

	public void scene_load(PrintWriter print_writer, String broadcaster) throws InterruptedException, IOException
	{
		switch (broadcaster.toUpperCase()) {
		
		case KabaddiUtil.KABADDI:
			switch(this.which_layer.toUpperCase()) {
			case KabaddiUtil.FRONT_LAYER:
				print_writer.println("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*" + this.scene_path + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE \0");
				TimeUnit.MILLISECONDS.sleep(500);
				break;
			case KabaddiUtil.MIDDLE_LAYER:
				print_writer.println("-1 RENDERER SET_OBJECT SCENE*" + this.scene_path + "\0");
				print_writer.println("-1 RENDERER*STAGE SHOW 0.0\0");
				print_writer.println("-1 RENDERER*SCENE_DATA INITIALIZE \0");
				TimeUnit.MILLISECONDS.sleep(500);
				break;
			case KabaddiUtil.BACK_LAYER:
				print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*" + this.scene_path + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
				TimeUnit.MILLISECONDS.sleep(500);
				break;
			}
			break;
		}
	}
}
