package rtc.player;

import java.util.List;
import java.util.LinkedList;

import org.bukkit.entity.Player;

public class RaptorPlayer {

		public Player player;
		protected boolean freezed;
		
		public static List<RaptorPlayer> players = new LinkedList<RaptorPlayer>();
		
		public RaptorPlayer(Player player){
			this.player = player;
			players.add(this);
		}
		
		public boolean isFreezed(){
			return freezed;
		}
		
		public void freeze(){
			freezed = true;
		}
		
		public void unfreeze(){
			freezed = false;
		}
		
		public void remove(){
			players.remove(this);
		}
		
		public static RaptorPlayer getRaptorPlayer(Player player){
			RaptorPlayer returning = null;
			for(RaptorPlayer rp : players){
				if(rp.player.equals(player)){
					returning = rp;
					break;
				}
			}
			
			if(returning == null) {
				return new RaptorPlayer(player);
			} else {
				return returning;
			}
		}
}
