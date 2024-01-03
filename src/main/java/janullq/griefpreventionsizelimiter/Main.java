package janullq.griefpreventionsizelimiter;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Main extends JavaPlugin implements Listener {
    final static String dataLayerFolderPath = "plugins" + File.separator + "GriefPreventionSizeLimiter";
    final static String configFilePath = dataLayerFolderPath + File.separator + "config.yml";
    public HashMap<String, Integer> config_claim_size_limits;
    public String config_message_of_claimLimit;
    public boolean config_ignore_if_admin_claim;
    public List<ClaimRuleZone> config_claim_rule_zone_list;

    private void loadAndUpdateConfig() {
        //configが存在すれば読む
        FileConfiguration config = YamlConfiguration.loadConfiguration((new File(configFilePath)));
        FileConfiguration outConfig = new YamlConfiguration();

        List<World> worlds = this.getServer().getWorlds();
        this.config_claim_size_limits = new HashMap<>();
        this.config_claim_rule_zone_list = new ArrayList<>();

        for (World world : worlds) {
            int maxArea = config.getInt("ClaimSizeLimits." + world.getName(), -1);
            outConfig.set("ClaimSizeLimits." + world.getName(), maxArea);
            this.config_claim_size_limits.put(world.getName(), maxArea);
        }
        this.config_message_of_claimLimit = config.getString("MessageOfClaimLimit", "§dThis claim is too large! Claims must be {0} blocks or less. (This claim is {1} Blocks)");
        this.config_ignore_if_admin_claim = config.getBoolean("IgnoreIfAdminClaim", true);
        outConfig.set("MessageOfClaimLimit", this.config_message_of_claimLimit);
        outConfig.set("IgnoreIfAdminClaim", this.config_ignore_if_admin_claim);
        try {
            outConfig.save(configFilePath);
        } catch (IOException exception) {
            getLogger().info("Unable to write to the configuration file at \"" + configFilePath + "\"");
        }

        if (config.contains("ClaimRuleZone")) {
            List<Map<?, ?>> configClaimRuleZoneList = config.getMapList("ClaimRuleZone");
            for (Map<?, ?> configClaimRuleZone : configClaimRuleZoneList) {
                // 設定値の存在確認
                if (configClaimRuleZone.containsKey("world") &&
                        configClaimRuleZone.containsKey("x1") && configClaimRuleZone.containsKey("x2") &&
                        configClaimRuleZone.containsKey("z1") && configClaimRuleZone.containsKey("z2") &&
                        configClaimRuleZone.containsKey("ClaimSizeLimit")) {
                    // 設定値の型確認
                    if (configClaimRuleZone.get("world") instanceof String &&
                            configClaimRuleZone.get("x1") instanceof Integer &&
                            configClaimRuleZone.get("x2") instanceof Integer &&
                            configClaimRuleZone.get("z1") instanceof Integer &&
                            configClaimRuleZone.get("z2") instanceof Integer &&
                            configClaimRuleZone.get("ClaimSizeLimit") instanceof Integer) {
                        String worldName = (String) configClaimRuleZone.get("world");
                        int x1 = (int) configClaimRuleZone.get("x1");
                        int x2 = (int) configClaimRuleZone.get("x2");
                        int z1 = (int) configClaimRuleZone.get("z1");
                        int z2 = (int) configClaimRuleZone.get("z2");
                        int claimSizeLimit = (int) configClaimRuleZone.get("ClaimSizeLimit");
                        // 指定したワールドが存在すれば、config_claim_rule_zone_listにルールを追加する
                        for (World world : worlds) {
                            if (world.getName().equals(worldName)) {
                                ClaimRuleZone claimRuleZone = new ClaimRuleZone(world, x1, z1, x2, z2, claimSizeLimit);
                                if (configClaimRuleZone.containsKey("message")) {
                                    claimRuleZone.message = (String) configClaimRuleZone.get("message");
                                }
                                config_claim_rule_zone_list.add(claimRuleZone);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onEnable() {
        getLogger().info("[GP Limiter] GriefPreventionAreaSizeLimiter is Loaded.");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this,this);
        loadAndUpdateConfig();
    }

    @EventHandler
    public void onClaimCreated(ClaimCreatedEvent event) {
        // 保護作成時
        Claim claim = event.getClaim();
        CommandSender player = event.getCreator();
        int areaSize = claim.getArea();
        String worldName = claim.getLesserBoundaryCorner().getWorld().getName(); //作成した保護のあるワールド名
        int maxAreaSizeOfWorld = config_claim_size_limits.get(worldName);
        // admin保護無視オプションが有効、かつadmin保護なら無視
        if (this.config_ignore_if_admin_claim && claim.isAdminClaim()) {
            return;
        }
        // 面積判定
        if (maxAreaSizeOfWorld != -1 && areaSize > maxAreaSizeOfWorld) {
            event.setCancelled(true);
            if (player != null) {
                player.sendMessage(this.config_message_of_claimLimit.replace("{0}", String.valueOf(maxAreaSizeOfWorld)).replace("{1}", String.valueOf(areaSize)));
            }
        }
    }
    @EventHandler
    public void onClaimResized(ClaimResizeEvent event) {
        // 保護サイズ変更時
        Claim claim = event.getTo();
        CommandSender player = event.getModifier();
        int areaSize = claim.getArea();
        String worldName = claim.getLesserBoundaryCorner().getWorld().getName(); //作成した保護のあるワールド名
        int maxAreaSizeOfWorld = config_claim_size_limits.get(worldName);
        // admin保護無視オプションが有効、かつadmin保護なら無視
        if (this.config_ignore_if_admin_claim && claim.isAdminClaim()) {
            return;
        }
        // 面積判定
        if (maxAreaSizeOfWorld != -1 && areaSize > maxAreaSizeOfWorld) {
            event.setCancelled(true);
            if (player != null) {
                player.sendMessage(this.config_message_of_claimLimit.replace("{0}", String.valueOf(maxAreaSizeOfWorld)).replace("{1}", String.valueOf(areaSize)));
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, @NotNull String[] args) {
        // /reloadGpLimiterコマンド実行時
        if (cmd.getName().equalsIgnoreCase("reloadGpLimiter") || cmd.getName().equalsIgnoreCase("reloadgpl")) {
            this.loadAndUpdateConfig();
            getLogger().info("[GP Limiter] GriefPreventionSizeLimiter is reloaded.");
            if((sender instanceof Player)) sender.sendMessage("[GP Limiter] GriefPreventionSizeLimiter is reloaded.");
            return true;
        }
        return false;
    }
}
