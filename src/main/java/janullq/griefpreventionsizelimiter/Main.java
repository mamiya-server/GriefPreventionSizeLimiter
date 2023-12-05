package janullq.griefpreventionsizelimiter;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
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
        this.config_message_of_claimLimit = config.getString("MessageOfClaimLimit", "§dThis claim is too large! Claims must be {0} blocks or less. (This claim is {1} Blocks)");
        outConfig.set("MessageOfClaimLimit", this.config_message_of_claimLimit);
        try {
            outConfig.save(configFilePath);
        } catch (IOException exception) {
            getLogger().info("Unable to write to the configuration file at \"" + configFilePath + "\"");
        }
    }
    @Override
    public void onEnable() {
        getLogger().info("GriefPreventionAreaSizeLimiter is Loaded.サイズ制限プラグイン読み込み");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this,this);
        loadAndUpdateConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        getLogger().info("参加！");
        Player player = event.getPlayer();
        player.sendMessage("ようこそ！"+"§a"+player.getName()+"さん！");
    }

    @EventHandler
    public void onClaimCreated(ClaimCreatedEvent event) {
        // 保護作成時の面積チェック
        Claim claim = event.getClaim();
        CommandSender player = event.getCreator();
        int areaSize = claim.getArea();
        String worldName = claim.getLesserBoundaryCorner().getWorld().getName(); //作成した保護のあるワールド名
        int maxAreaSizeOfWorld = config_claim_size_limits.get(worldName);
        if (maxAreaSizeOfWorld != -1 && areaSize > maxAreaSizeOfWorld) {
            event.setCancelled(true);
            if (player != null) {
                player.sendMessage(this.config_message_of_claimLimit.replace("{0}", String.valueOf(maxAreaSizeOfWorld)).replace("{1}", String.valueOf(areaSize)));
            }
        }
    }
    @EventHandler
    public void onClaimResized(ClaimResizeEvent event) {
        // 保護サイズ変更時の面積チェック
        Claim claim = event.getTo();
        CommandSender player = event.getModifier();
        int areaSize = claim.getArea();
        String worldName = claim.getLesserBoundaryCorner().getWorld().getName(); //作成した保護のあるワールド名
        int maxAreaSizeOfWorld = config_claim_size_limits.get(worldName);
        if (maxAreaSizeOfWorld != -1 && areaSize > maxAreaSizeOfWorld) {
            event.setCancelled(true);
            if (player != null) {
                player.sendMessage(this.config_message_of_claimLimit.replace("{0}", String.valueOf(maxAreaSizeOfWorld)).replace("{1}", String.valueOf(areaSize)));
            }
        }
    }
}
