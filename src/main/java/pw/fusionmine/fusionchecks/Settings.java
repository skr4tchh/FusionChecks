package pw.fusionmine.fusionchecks;

import net.elytrium.commons.config.YamlConfig;

public class Settings extends YamlConfig {

    @Ignore
    public static final Settings IMP = new Settings();

    public String PREFIX = "&6Проверка &7» &f";

    @Create
    public CHECK CHECK;

    public static class CHECK {

        @Comment("В минутах")
        public int TIME = 15;

        public String SERVER = "auth";

        public String BAN_COMMAND = "banip %target% 30d 3.1";

        public String LEAVE_BAN_COMMAND = "banip %target% 30d 3.1.2 - Уход от проверки";

        public String DENY_BAN_COMMAND = "banip %target% 30d 3.1.2 - Признание в читах";

    }

    @Create
    public MESSAGES MESSAGES;

    public static class MESSAGES {



    }

}
