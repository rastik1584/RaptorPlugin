package rtc;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import rtc.listeners.RaptorTestCommandListener;
import rtc.listeners.RaptorTestPlayerListener;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RaptorTest extends JavaPlugin {

	public File CommandLog;
	public Logger log = Logger.getLogger("Minecraft");
	public MySQL db;
	public CommandLog cl;
	public RaptorTestCommandListener commandListener = new RaptorTestCommandListener(this);
	public RaptorTestPlayerListener loginListener = new RaptorTestPlayerListener(this);
	public PermissionManager permissionsEx;
	private static final boolean host = true;

	public void onEnable(){
		getLogger().info("Raptor plugin zapnuty");
		this.setupPermissions();
		getServer().getPluginManager().registerEvents(loginListener, this);
		this.getCommand("trol").setExecutor(commandListener);
		this.getCommand("c").setExecutor(commandListener);
		this.getCommand("warn").setExecutor(commandListener);
		this.getCommand("rtcinfo").setExecutor(commandListener);
		this.getCommand("freeze").setExecutor(commandListener);
		
		CommandLog = new File("cmd.log");
		try {
			if(!CommandLog.exists()) {
				CommandLog.createNewFile();
				Logger.getLogger("Subor vytvoreny");
			} else {
				Logger.getLogger("Subor ne-vytvoreny");
			}
		} catch (IOException e) {}
		cl = new CommandLog(this);
		if(!host) {
			db = new MySQL(log, "[RaptorDb]", "localhost", "3306", "139138_mysql_db", "139138_mysql_db", "Kristinka");
		} else {
			db = new MySQL(log, "[RaptorDb]", "localhost", "3306", "139138_mysql_db", "139138_mysql_db", "Kristinka");
		}
		db.open();
		if(db.open() != null ){
			getLogger().info("MySQL pripojene");
		}else{
			getLogger().info("MySQL nepripojena");
		}
	}
	
	
	public void onDisable(){
		db.close();
		cl.close();
		getLogger().info("Raptor plugin vypnuty");
	}
	
	
	
	
	private void setupPermissions() {
		if (this.permissionsEx == null) {
			if (this.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
				this.permissionsEx = PermissionsEx.getPermissionManager();
				getLogger().info("[RaptorCraft] permissions (PermissionsEx-Plugin) successfully loaded");
				return;
			}
		} else {
			getLogger().info("[RaptorCraft] permission system not detected, defaulting to OP");
			return;
		}
	}

	public boolean hasPermission(Player player, String permission) {
		if (permissionsEx != null) {
			return (permissionsEx.has(player, permission));
		} else {
			return player.isOp();
		}
	}

	public void PrivateMessage(Player player, String message) {
		player.sendMessage(ChatColor.RED+"[RTC] "+ChatColor.BLACK+" >> "+ChatColor.WHITE + message);
	}
	
	public String getIP(Player player) {
		String RawAddress = player.getAddress().toString();
		RawAddress.replace("/", "");
		return RawAddress.substring(1).split(":")[0];
	}
	
	

}
	
	
	
	
	
	
	
	
	
	
	
	

