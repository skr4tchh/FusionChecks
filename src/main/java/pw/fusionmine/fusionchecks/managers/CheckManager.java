package pw.fusionmine.fusionchecks.managers;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import pw.fusionmine.fusionchecks.FusionChecks;
import pw.fusionmine.fusionchecks.Settings;
import pw.fusionmine.fusionchecks.utils.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CheckManager {

    private FusionChecks plugin;

    private ScheduledTask globalTimerHandle;

    private final Map<UUID, CheckData> activeChecks = new ConcurrentHashMap<>(8);

    private final Map<UUID, UUID> checkerToTarget = new ConcurrentHashMap<>(8);

    public CheckManager(FusionChecks plugin) {
        this.plugin = plugin;
    }

    public boolean isChecking(ProxiedPlayer p) {
        return this.activeChecks.containsKey(p.getUniqueId());
    }

    public ProxiedPlayer getTargetByChecker(UUID checkerUUID) {
        UUID targetUUID = this.checkerToTarget.get(checkerUUID);
        return (targetUUID != null) ? plugin.getProxy().getPlayer(targetUUID) : null;
    }

    public void startCheck(ProxiedPlayer target, ProxiedPlayer checker) {
        UUID targetUUID = target.getUniqueId();
        if (this.activeChecks.containsKey(targetUUID))
            return;
        int totalSeconds = Settings.IMP.CHECK.TIME * 60;
        ServerInfo prevServer = target.getServer().getInfo();
        CheckData data = new CheckData(targetUUID, checker.getUniqueId(), prevServer, totalSeconds);
        Utils.connectServer(target, Settings.IMP.CHECK.SERVER);
        this.activeChecks.put(targetUUID, data);
        this.checkerToTarget.put(checker.getUniqueId(), targetUUID);
        ensureGlobalTimer();
    }

    public void endCheck(ProxiedPlayer target) {
        CheckData data = this.activeChecks.remove(target.getUniqueId());
        if (data == null)
            return;
        this.checkerToTarget.remove(data.checkerUUID);
        if(target.isConnected())
            target.connect(data.prevServer);
        stopGlobalTimerIfEmpty();
    }

    private void ensureGlobalTimer() {
        if (this.globalTimerHandle != null)
            return;
        this.globalTimerHandle = this.plugin.getProxy().getScheduler().schedule(plugin, this::tickAllChecks, 1, TimeUnit.SECONDS);
    }

    private void stopGlobalTimerIfEmpty() {
        if (this.activeChecks.isEmpty() && this.globalTimerHandle != null) {
            this.plugin.getProxy().getScheduler().cancel(this.globalTimerHandle);
            this.globalTimerHandle = null;
        }
    }

    private void tickAllChecks() {
        Iterator<Map.Entry<UUID, CheckData>> it = this.activeChecks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, CheckData> entry = it.next();
            CheckData data = entry.getValue();
            ProxiedPlayer target = plugin.getProxy().getPlayer(data.targetUUID);
            if (data.timeLeft <= 0) {
                it.remove();
                this.checkerToTarget.remove(data.checkerUUID);
                Utils.sendBanCommand(target, Settings.IMP.CHECK.BAN_COMMAND);
                continue;
            }
            data.timeLeft--;
        }
        stopGlobalTimerIfEmpty();
    }

    static class CheckData {
        final UUID targetUUID;

        final UUID checkerUUID;

        volatile int timeLeft;

        final ServerInfo prevServer;

        volatile int totalSeconds;

        CheckData(UUID target, UUID checker, ServerInfo prevServer, int totalSeconds) {
            this.targetUUID = target;
            this.checkerUUID = checker;
            this.timeLeft = totalSeconds;
            this.prevServer = prevServer;
            this.totalSeconds = totalSeconds;
        }

    }

}
