package me.example.autosfunlock;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoSlimefunUnlock extends JavaPlugin implements Listener {
    private static final long DELAY_TICKS = 20L * 60L * 3L; // 3 minutes

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("[AutoSlimefunUnlock] Enabled. Delay ticks=" + DELAY_TICKS);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean hasPlayedBefore = player.hasPlayedBefore();
        getLogger().info("[AutoSlimefunUnlock] Join: " + player.getName()
                + " hasPlayedBefore=" + hasPlayedBefore
                + " isOnline=" + player.isOnline());
        if (hasPlayedBefore) {
            return;
        }

        long scheduledAt = System.currentTimeMillis();
        getLogger().info("[AutoSlimefunUnlock] Scheduling unlock for " + player.getName()
                + " after " + DELAY_TICKS + " ticks.");

        Bukkit.getScheduler().runTaskLater(this, () -> {
            long now = System.currentTimeMillis();
            long elapsedMs = now - scheduledAt;
            getLogger().info("[AutoSlimefunUnlock] Task fired for " + player.getName()
                    + " elapsedMs=" + elapsedMs
                    + " isOnline=" + player.isOnline());

            if (!player.isOnline()) {
                getLogger().info("[AutoSlimefunUnlock] Skip unlock: player offline: " + player.getName());
                return;
            }

            String command = "sf research " + player.getName() + " all";
            boolean dispatched = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

            Logger logger = getLogger();
            logger.info("[AutoSlimefunUnlock] Dispatched command='" + command + "' success=" + dispatched);
            logger.info("[AutoSlimefunUnlock] Unlocked Slimefun researches for " + player.getName());
        }, DELAY_TICKS);
    }
}
