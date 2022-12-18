package net.twlghtdrgn.luna;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.twlghtdrgn.luna.Commands.CommandManager;
import net.twlghtdrgn.luna.Listeners.AudioEventListener;
import net.twlghtdrgn.luna.Listeners.BirthdayListener;
import net.twlghtdrgn.luna.Listeners.EventListener;
import net.twlghtdrgn.luna.Listeners.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Luna {
    private final Dotenv config;
    private final ShardManager shardManager;
    final static Logger logger = LoggerFactory.getLogger(Luna.class);
    public Luna() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.IDLE);
        builder.setActivity(Activity.playing("with your dreams"));
        builder.enableIntents(GatewayIntent.getIntents(3276799));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        shardManager = builder.build();

        // Listeners
        logger.info("Initializing listeners...");
        shardManager.addEventListener(new MessageListener(this));
        shardManager.addEventListener(new BirthdayListener(this));
        shardManager.addEventListener(new CommandManager(this));
        shardManager.addEventListener(new EventListener(this));
        new AudioEventListener(this);
    }

    /**
     * Returns shard manager
     * @return the ShardManager instance
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    /**
     * Returns config values
     * @return config
     */
    public Dotenv getConfig() {
        return config;
    }

    /**
     * Returns logger instance
     * @return logger
     */
    public Logger getLogger() {
        return logger;
    }


    /**
     * Main class, y'know
     */
    public static void main(String[] args) {
        try {
            Luna luna = new Luna();
        } catch (LoginException e) {
            logger.error("Incorrect bot token");
        }
    }
}