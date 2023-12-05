package janullq.griefpreventionsizelimitter;

import me.ryanhamshire.GriefPrevention.events.ClaimChangeEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimInspectionEvent;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin implements Listener {
    final static String dataLayerFolderPath = "plugins" + File.separator + "GriefPreventionSizeLimiter";
    final static String configFilePath = dataLayerFolderPath + File.separator + "config.yml";
    public HashMap<String, Integer> config_claim_size_limits;
    public String config_message_of_claimLimit;

    private void loadAndUpdateConfig() {
        //configが存在すれば読む
        FileConfiguration config = YamlConfiguration.loadConfiguration((new File(configFilePath)));
        FileConfiguration outConfig = new YamlConfiguration();

        List<World> worlds = this.getServer().getWorlds();
        this.config_claim_size_limits = new HashMap<>();
        for (World world : worlds) {
            int maxArea = config.getInt("ClaimSizeLimits." + world.getName(), -1);
            outConfig.set("ClaimSizeLimits." + world.getName(), maxArea);
            this.config_claim_size_limits.put(world.getName(), maxArea);
        }
        this.config_message_of_claimLimit = config.getString("MessageOfClaimLimit", "This claim is too large! Claims must be {0} blocks or less.");
        outConfig.set("MessageOfClaimLimit", this.config_message_of_claimLimit);
        try {
            outConfig.save(configFilePath);
        } catch (IOException exception) {
            getLogger().info("Unable to write to the configuration file at \"" + configFilePath + "\"");
        }
    }
    @Override
    public void onEnable() {
        getLogger().info("GriefPreventionAreaSizeLimitter is Loaded.サイズ制限プラグイン読み込み");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this,this);
        loadAndUpdateConfig();
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
