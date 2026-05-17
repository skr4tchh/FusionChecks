package pw.fusionmine.fusionchecks.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import static co.aikar.commands.ACFBungeeUtil.color;

@UtilityClass
public class Utils {

    public void connectServer(ProxiedPlayer player, String name) {
        ServerInfo server = ProxyServer.getInstance().getServerInfo(name);
        player.connect(server);
    }

    public void sendBanCommand(ProxiedPlayer target, String command) {
        command = command.replaceAll("%target%", target.getName());
        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), command);
    }

    public void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(getTextComponent(message));
    }

    public TextComponent getTextComponent(String text) {
        return new TextComponent(color(text));
    }

    public void sendTitle(ProxiedPlayer player, String title, String subtitle, int stay, int fadeIn, int fadeOut) {
        TextComponent titleComponent = getTextComponent(title);
        TextComponent subTitleComponent = getTextComponent(subtitle);
        Title t = ProxyServer.getInstance().createTitle()
                .title(titleComponent)
                .subTitle(subTitleComponent)
                .stay(stay)
                .fadeIn(fadeIn)
                .fadeOut(fadeOut);
        player.sendTitle(t);
    }

}
