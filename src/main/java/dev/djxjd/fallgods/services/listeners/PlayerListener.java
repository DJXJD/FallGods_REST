package dev.djxjd.fallgods.services.listeners;

import dev.djxjd.fallgods.beans.Player;
import jakarta.persistence.PostLoad;

public class PlayerListener {
	
	@PostLoad
	private void updateTransientData(Player p) {
		
	}

}
