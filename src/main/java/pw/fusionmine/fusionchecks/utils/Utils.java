package pw.fusionmine.fusionchecks.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class Utils {

    public void connectServer(ProxiedPlayer player, String name) {
        ServerInfo server = ProxyServer.getInstance().getServerInfo(name);
        player.connect(server);
    }

    public void sendBanCommand(ProxiedPlayer target, String command) {
        command = command.replaceAll("%target%", target.getName());
        ProxyServer.getInstance().getConsole().sendMessage(command);
    }

}
