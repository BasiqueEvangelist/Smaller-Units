package com.tfc.smallerunits.utils.world;

import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;

public class ClientServerScoreboard extends ServerScoreboard {
	public ClientServerScoreboard(MinecraftServer mcServer) {
		super(mcServer);
	}
}
