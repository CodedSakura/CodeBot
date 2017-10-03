package codebot.commands.music.handle;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

import static codebot.Command.send;


public class Handler {
    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private final GuildMusicManager musicManager;
    private final Guild guild;
    private final TrackScheduler trackScheduler;

    public Handler(Guild guild) {
        this.guild = guild;

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();
        trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);

        musicManager = new GuildMusicManager(playerManager);
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
    }

    public String establishConnection(VoiceChannel voiceChannel, TextChannel channel, String id) {
        AudioManager audioManager = guild.getAudioManager();
        if (voiceChannel == null) {
            return send(channel, id, "ERR: voice channel not found");
        } else {
            audioManager.openAudioConnection(voiceChannel);
            return send(channel, id, "Connected to voice channel \"" + voiceChannel.getName() + "\"");
        }
    }

    public String loadAndPlay(String url, TextChannel channel, String id) {
        String data = send(channel, id, "processing...");
        playerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                send(channel, data, "Adding to queue \"" + track.getInfo().title + "\"");
                play(track);
            }
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) firstTrack = playlist.getTracks().get(0);
                send(channel, data, "Adding to queue " + playlist.getTracks().size() + " songs from playlist/search \"" + playlist.getName() + "\"");
                play(firstTrack);
            }
            @Override
            public void noMatches() {
                send(channel, data, "ERR: nothing found by \"" + url + "\"");
            }
            @Override
            public void loadFailed(FriendlyException exception) {
                send(channel, data,"ERR: could not play: " + exception.getMessage());
            }
        });
        return data;
    }

    private void play(AudioTrack track) {
        AudioManager audioManager = guild.getAudioManager();
        if (audioManager.isConnected() || audioManager.isAttemptingToConnect()) {
            musicManager.scheduler.queue(track);
        }
    }

    public void pause(boolean result) {
        trackScheduler.pause(result);
    }
    public boolean pause() {
        return trackScheduler.pause();
    }

    public Object[] currentInfo() {
        return trackScheduler.currentInfo();
    }
    public AudioTrackInfo[] queueInfo() {
        return trackScheduler.queueInfo();
    }

    public void shuffle() {
        trackScheduler.shuffle();
    }

    public String skipTrack(TextChannel channel, String id) {
        musicManager.scheduler.nextTrack();
        return send(channel, id, "Skipped to the next track");
    }
}
