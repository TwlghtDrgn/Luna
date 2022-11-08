package net.twlghtdrgn.luna.Listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.twlghtdrgn.luna.Luna;

public class AudioEventListener extends AudioEventAdapter {
    private final Luna luna;

    public AudioEventListener(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
        luna.getLogger().info("Player was paused..?");
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
        luna.getLogger().info("Player was resumed");
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // A track started playing
        luna.getLogger().info("Started new track");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
//        if (endReason.mayStartNext) {
//            // Start next track
//        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
        luna.getLogger().info("Track was ended, idk why");
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be received separately)
        luna.getLogger().error(exception.getMessage());
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
        luna.getLogger().error("Track is stuck!11");
    }
}
