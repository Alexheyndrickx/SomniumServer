package com.osroyale.game.world.entity.mob.player.profile;

import com.osroyale.game.world.entity.mob.player.PlayerRight;

import java.util.List;
import java.util.Set;

public class Profile {
	
	private final String name;
	
	private final String password;
	
	private final String lastHost;
	
	private final Set<String> host;
	
	private final PlayerRight rank;
	
	public Profile(String name, String password, String lastHost, Set<String> host, PlayerRight rank) {
		this.name = name;
		this.password = password;
		this.lastHost = lastHost;
		this.host = host;
		this.rank = rank;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getLastHost() {
		return lastHost;
	}
	
	public Set<String> getHost() {
		return host;
	}
	
	public PlayerRight getRank() {
		return rank;
	}
}
