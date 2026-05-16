package pw.fusionmine.fusionchecks;

import co.aikar.commands.BungeeCommandManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import pw.fusionmine.fusionchecks.commands.CheckCommand;
import pw.fusionmine.fusionchecks.commands.ContactCommand;
import pw.fusionmine.fusionchecks.listeners.CheckListener;
import pw.fusionmine.fusionchecks.managers.CheckManager;

import java.io.File;

public class FusionChecks extends Plugin {

    private CheckManager checkManager = new CheckManager(this);

    @Override
    public void onEnable() {
        reloadSettings();
        registerListeners();
        registerCommands();
    }

    public void reloadSettings() {
        File settingsFile = new File(this.getDataFolder(), "settings.yml");
        Settings.IMP.reload(settingsFile, Settings.IMP.PREFIX);
    }

    private void registerListeners() {
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new CheckListener(this.checkManager));
    }

    private void registerCommands() {
        BungeeCommandManager manager = new BungeeCommandManager(this);
        manager.registerCommand(new CheckCommand(this, this.checkManager));
        manager.registerCommand(new ContactCommand(this.checkManager));
    }

}
