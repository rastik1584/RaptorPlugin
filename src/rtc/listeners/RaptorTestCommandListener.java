package rtc.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rtc.RaptorTest;

public class RaptorTestCommandListener implements CommandExecutor {
	
	private final RaptorTest plugin;
	
	public RaptorTestCommandListener(RaptorTest instance) {
		plugin = instance;
		
	}

	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if(sender instanceof Player) {
			for(int i = 0; i<args.length; i++) {
			}
		
		}
		
		
		if(label.equalsIgnoreCase("trol")){ // If the player typed /basic then do the following...
			plugin.getServer().broadcastMessage(ChatColor.GREEN+"[RaptorCraft] " + ChatColor.DARK_GREEN + "Trolololololo");
			return true;
		}
		
		if(label.equalsIgnoreCase("c")){
			Player pSender = (Player) sender;
			Location position = pSender.getLocation();
			double x,y,z;
			x = position.getX();
			y = position.getY();
			z = position.getZ();
			pSender.sendMessage(ChatColor.RED + "Tvoja pozícia je : X:" + ChatColor.GREEN + x + " Y:" + ChatColor.GREEN + y + " Z:" + ChatColor.GREEN + z);
			return true;
		}
		
		if(label.equalsIgnoreCase("warn")){
			if(!plugin.hasPermission((Player) sender, "raptor.warn")){
				plugin.PrivateMessage((Player) sender, "&cNemas povolenia!");
			}else{
				Player p = plugin.getServer().getPlayer(args[0]);
				p.sendMessage(ChatColor.RED + "Bol si varovany ADMINOM z porusovania pravidiel " + ChatColor.GREEN + "pravidla najdes na spawne " + ChatColor.RED + "tak ich DODRZUJ!!!");
				plugin.getServer().broadcastMessage(ChatColor.AQUA + "Hrac " + ChatColor.RED + p.getName() + ChatColor.AQUA + " bol varovany z porusovania pravidiel" );
				return true;
			}
			
			
		}
		
		if(label.equalsIgnoreCase("freeze")){
			if(!plugin.hasPermission((Player) sender, "raptor.freeze")){
				plugin.PrivateMessage((Player) sender , "&cNemas povolenia!");
			}else{
				Player k = plugin.getServer().getPlayerExact(args[0]);
				k.getLocation();
				
				k.sendMessage("Bol si zamrazený ! ");
			}
		}
		
		if(label.equalsIgnoreCase("rtcinfo")){
			Player p = (Player) sender;
			ResultSet x = plugin.db.query("SELECT * FROM rtcplayers WHERE meno = '"+ p.getName() +"'");
			int m = 0;
			int i = 0;
			p.sendMessage(ChatColor.AQUA + "Na svojom nicku "+ p.getName() +" mas : ");
			try{
				while(x.next()) {
					m = x.getInt("mute");
					
				}
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
			if(m != 0){
				p.sendMessage(ChatColor.AQUA + "spam bodov: " + m  );
			}
			ResultSet y = plugin.db.query("SELECT * FROM iConomy WHERE meno = '"+ p.getName() +"'");
			try{
				while(y.next()){
					i = y.getInt("balance");
				}
			}catch(SQLException e1){
				e1.printStackTrace();
			}
			p.sendMessage(ChatColor.AQUA + "Penazi: " + i);
			
		}
		
		if(label.equalsIgnoreCase("setspawn")){
			if(!plugin.hasPermission((Player) sender, "rtc.setspawn")){
				plugin.PrivateMessage((Player) sender, "&cNemas povolenia!");
			}else{
				Player h = (Player) sender;
				Location l = h.getLocation();
				double x,y,z;
				x = l.getX();
				y = l.getY();
				z = l.getZ();
				ResultSet i = plugin.db.query("SELECT * FROM spawn");
				String nn=null;
				try {
		              while(i.next()) {
		                   nn = i.getString("nick");
		              }
		          } catch (SQLException e1) {
		              e1.printStackTrace();
		          }
				if(nn != null){
					plugin.db.query("UPDATE spawn SET x = '"+ x +"' , y = '"+ y +"' , z = '"+ z +"' nick = '"+ h.getName() +"'");
					h.sendMessage(ChatColor.RED + "RTC>>>" + ChatColor.AQUA +"Spawn bol nastaveny");
				}else{
					plugin.db.query("INSERT INTO spawn (`x`,`y`,`z`,`nick`) VALUES ('"+ x +"','"+ y +"','"+ z +"','"+ h.getName() +"')");
					h.sendMessage(ChatColor.RED + "RTC>>>" + ChatColor.AQUA +"Spawn bol nastaveny");
				}
			}
			
		}
		
		if(label.equalsIgnoreCase("spawn")){
			Player p = (Player) sender;	
			ResultSet k = plugin.db.query("SELECT * FROM spawn");
			double x,y,z;
			try {
	              while(k.next()) {
	                   x = k.getDouble("x");
	                   y = k.getDouble("y");
	                   z = k.getDouble("z");
	              }
	          } catch (SQLException e1) {
	              e1.printStackTrace();
	          }
			
		}
		
		return false; 
	}
	
	

	public RaptorTest getPlugin() {
		return plugin;
	}

}
