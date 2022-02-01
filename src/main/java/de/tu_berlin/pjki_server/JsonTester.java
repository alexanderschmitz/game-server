package de.tu_berlin.pjki_server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.Manager;

public class JsonTester {

	public static void main(String[] args) {
		TicTacToeExample ttt = new TicTacToeExample();
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(AbstractGame.class, ttt);
		Gson customGson = gsonBuilder.create();
		
		System.out.println(customGson.toJson(ttt));
		
		Manager manager = Manager.getManager();
		manager.addGameType(TicTacToeExample.class);
		for (int i = 0; i< 5; i++) {
			try {
				manager.addGameToLobby("TicTacToeExample");
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		System.out.println(manager.getLobby().size());
		
		System.out.println(customGson.toJson(manager.getLobby()));
	}
}
