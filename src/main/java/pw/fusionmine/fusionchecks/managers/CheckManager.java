package pw.fusionmine.fusionchecks.managers;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import pw.fusionmine.fusionchecks.FusionChecks;
import pw.fusionmine.fusionchecks.Settings;
import pw.fusionmine.fusionchecks.utils.Utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CheckManager {

    private FusionChecks plugin;

    private final Map<UUID, CheckData> activeChecks = new ConcurrentHashMap<>(8);

    private final Map<UUID, UUID> checkerToTarget = new ConcurrentHashMap<>(8);

    public CheckManager(FusionChecks plugin) {
        this.plugin = plugin;
    }

    public boolean isChecking(ProxiedPlayer p) {
        return this.activeChecks.containsKey(p.getUniqueId());
    }

    public boolean isCheckerActive(ProxiedPlayer p) {
        return this.getTargetByChecker(p.getUniqueId()) != null;
    }

    public ProxiedPlayer getTargetByChecker(UUID checkerUUID) {
        UUID targetUUID = this.checkerToTarget.get(checkerUUID);
        return (targetUUID != null) ? plugin.getProxy().getPlayer(targetUUID) : null;
    }

    public ProxiedPlayer getCheckerByTarget(ProxiedPlayer target) {
        CheckData data = this.activeChecks.get(target.getUniqueId());
        return (data != null) ? plugin.getProxy().getPlayer(data.checkerUUID) : null;
    }

    public void startCheck(ProxiedPlayer target, ProxiedPlayer checker) {
        UUID targetUUID = target.getUniqueId();
        if (this.activeChecks.containsKey(targetUUID))
            return;
        ServerInfo prevServer = target.getServer().getInfo();
        ScheduledTask messageTask = plugin.getProxy().getScheduler().schedule(plugin, () -> sendInfo(target, checker), 1, 30, TimeUnit.SECONDS);
        CheckData data = new CheckData(targetUUID, checker.getUniqueId(), prevServer, messageTask);
        Utils.connectServer(target, Settings.IMP.CHECK.SERVER);
        this.activeChecks.put(targetUUID, data);
        this.checkerToTarget.put(checker.getUniqueId(), targetUUID);
    }

    public void endCheck(ProxiedPlayer target) {
        CheckData data = this.activeChecks.remove(target.getUniqueId());
        if (data == null)
            return;
        data.messageTask.cancel();
        this.checkerToTarget.remove(data.checkerUUID);
        if(target.isConnected())
            target.connect(data.prevServer);
    }

    public void sendInfo(ProxiedPlayer target, ProxiedPlayer checker) {
        Utils.sendTitle(target, Settings.IMP.TITLE.TITLE, Settings.IMP.TITLE.SUBTITLE, 600, 10, 20);
        Utils.sendMessage(target, Settings.IMP.MESSAGES.CHECK.INFO.replaceAll("%checker%", checker.getName()));
    }

    static class CheckData {

        final UUID targetUUID;

        final UUID checkerUUID;

        final ServerInfo prevServer;

        final ScheduledTask messageTask;

        CheckData(UUID target, UUID checker, ServerInfo prevServer, ScheduledTask messageTask) {
            this.targetUUID = target;
            this.checkerUUID = checker;
            this.prevServer = prevServer;
            this.messageTask = messageTask;
        }

    }

}
