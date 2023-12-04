package janullq.griefpreventionsizelimitter;

import me.ryanhamshire.GriefPrevention.events.ClaimChangeEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimInspectionEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    private FileConfiguration config;
    @Override
    public void onEnable() {
        getLogger().info("GriefPreventionAreaSizeLimitter is Loaded.サイズ制限プラグイン読み込み");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this,this);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        getLogger().info("参加！");
        Player player = event.getPlayer();
        player.sendMessage("ようこそ！"+"§a"+player.getName()+"さん！");
    }

    @EventHandler
    public void onClaimChanged(ClaimChangeEvent event)
    {
		int areaSize = event.getTo().getArea();
        int previousAreaSize = event.getFrom().getArea();

        getLogger().info("Claimの変更！サイズ" + String.valueOf(previousAreaSize) + "->" +  String.valueOf(areaSize));
        getLogger().info("calcelするで！");
        event.setCancelled(true);

    }

    @EventHandler
    public void onClaimInspect(ClaimInspectionEvent event)
    {
        getLogger().info("ClaimのInspect！");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
