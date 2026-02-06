package me.example.autosfunlock;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoSlimefunUnlock extends JavaPlugin implements Listener {
    private static final long DELAY_TICKS = 20L * 90L; // 1 min 30 sec
    private static final long REPEAT_INTERVAL_TICKS = 20L * 5L; // 5 seconds
    private static final int REPEAT_COUNT = 3;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("[AutoSlimefunUnlock] Enabled. Delay ticks=" + DELAY_TICKS
                + " repeatIntervalTicks=" + REPEAT_INTERVAL_TICKS
                + " repeatCount=" + REPEAT_COUNT);
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
        getLogger().info("[AutoSlimefunUnlock] Scheduling unlocks for " + player.getName()
                + " after " + DELAY_TICKS + " ticks. Will repeat "
                + REPEAT_COUNT + " time(s) every " + REPEAT_INTERVAL_TICKS + " ticks.");

        Bukkit.getScheduler().runTaskLater(this, () -> {
            scheduleRepeatedUnlocks(player, scheduledAt);
        }, DELAY_TICKS);
    }

    private void scheduleRepeatedUnlocks(Player player, long scheduledAt) {
        for (int i = 0; i < REPEAT_COUNT; i++) {
            long delay = REPEAT_INTERVAL_TICKS * i;
            int attempt = i + 1;
            Bukkit.getScheduler().runTaskLater(this, () -> {
                long now = System.currentTimeMillis();
                long elapsedMs = now - scheduledAt;
                getLogger().info("[AutoSlimefunUnlock] Task fired for " + player.getName()
                        + " attempt=" + attempt
                        + " elapsedMs=" + elapsedMs
                        + " isOnline=" + player.isOnline());

                if (!player.isOnline()) {
                    getLogger().info("[AutoSlimefunUnlock] Skip unlock: player offline: " + player.getName()
                            + " attempt=" + attempt);
                    return;
                }

                String command = "sf research " + player.getName() + " all";
                boolean dispatched = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

                Logger logger = getLogger();
                logger.info("[AutoSlimefunUnlock] Dispatched command='" + command + "' success=" + dispatched
                        + " attempt=" + attempt);
                logger.info("[AutoSlimefunUnlock] Unlocked Slimefun researches for " + player.getName()
                        + " attempt=" + attempt);
            }, delay);
        }
    }
}
