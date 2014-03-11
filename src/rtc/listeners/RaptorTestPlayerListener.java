package rtc.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import rtc.RaptorTest;
import rtc.player.RaptorPlayer;




public class RaptorTestPlayerListener implements Listener {
	private final RaptorTest plugin;
	public RaptorTestPlayerListener(RaptorTest iplugin) {
		plugin = iplugin;
	}
	
	 private Map<Player, Location> deaths = new HashMap<Player, Location>();
	 
		
		private static String getDateTime() {
		    DateFormat dfFormat = new SimpleDateFormat("yyyyy-mm-dd HH:mm:ss");
		    Date dNow = new Date();
		    return dfFormat.format(dNow);
		}
	 
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent e){
		new RaptorPlayer(e.getPlayer());
		
		Player player = e.getPlayer();
		String pl = player.getName();
		player.sendMessage(ChatColor.GREEN + "Vitaj na serveri RaptorCraft " + pl);
		ResultSet x = plugin.db.query("SELECT * FROM rtcplayers WHERE meno = '"+ pl + "';");
		String pla = null;
		  try {
              while(x.next()) {
                   pla = x.getString("meno");
              }
          } catch (SQLException e1) {
              e1.printStackTrace();
          }
          
		if(pla != null){
			plugin.db.query("UPDATE `rtcplayers` SET `lastloginip` = '" + plugin.getIP(player) +"' , lastlogindate = '"+ getDateTime() +"' WHERE meno = '" + pl +"';");
		}else{
			plugin.db.query("INSERT INTO rtcplayers (`meno`,`mute`,`iconomy`,`lastloginip`,`lastlogindate`) VALUES ('"+ pl +"','20','0','"+ plugin.getIP(player) +"','"+ getDateTime() +"') ");
		}
	}
	
 
	protected Player oldP;
	protected String oldM;
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(final AsyncPlayerChatEvent e){
		if(e.getMessage().equalsIgnoreCase(oldM) && oldP == e.getPlayer()) {
			Player p = e.getPlayer();
			e.getMessage();
			p.sendMessage(ChatColor.GREEN + "[AntiSpam] " + ChatColor.RED + "Nespamuj ! " + ChatColor.AQUA + "Z tvojho konta bolo odcitanych 5 spam bodov ! ");
			ResultSet a = plugin.db.query("SELECT * FROM rtcplayers WHERE meno = '"+ p.getName() +"'");
			int s = 0;
			try{
				while(a.next()) {
					s = a.getInt("mute");
				}
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
			int xs = s -5;
			plugin.db.query("UPDATE rtcplayers SET mute = '"+ xs +"' WHERE meno = '"+ p.getName() +"'");
			p.sendMessage(ChatColor.AQUA + "Na tvojom konte ostalo " + xs + " spam bodov.");
			if(xs == 10){
				p.sendMessage(ChatColor.RED + "Ak dosiahnes 0 bodov dostanes kick ! NESPAMUJ !!");
				
				
				}else{
					if(xs <= 0){
						//e.getPlayer().kickPlayer("Bol si kicknuty za SPAM ! ");
						plugin.db.query("UPDATE rtcplayers SET mute = '20' WHERE meno = '"+ p.getName() +"'");
						plugin.getServer().broadcastMessage(ChatColor.GREEN + "[AntiSpam] " + ChatColor.AQUA + "Hrac " + ChatColor.RED + p.getName() + ChatColor.AQUA + " bol kicknuty za spam !" );
					}
				}
				
			}
					
		oldM = e.getMessage();
		oldP = e.getPlayer();
	}
	

	
	
	public Location getPlayerDeathLoc(Player p) {
        return deaths.get(p);
    }
	 
		
	
	
	public void onMove(PlayerMoveEvent x){
	Player p = x.getPlayer();
		RaptorPlayer.getRaptorPlayer(p);
		
	}
	
	public RaptorTest getPlugin() {
		return plugin;
	}

}
