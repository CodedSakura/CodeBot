package codebot.commands.music.handle;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    boolean pause(boolean result) {
        if (player.getPlayingTrack() != null) player.setPaused(result);
        return player.isPaused();
    }
    boolean pause() {
        return pause(!player.isPaused());
    }

    Object[] currentInfo() {
        AudioTrack current = player.getPlayingTrack();
        if (current != null) {
            return new Object[] {
                    current.getInfo(),
                    current.getPosition()
            };
        }
        return null;
    }

    AudioTrackInfo[] queueInfo() {
        if (queue.isEmpty()) {
            Object[] currentInfo = currentInfo();
            return new AudioTrackInfo[] {currentInfo == null ? null : (AudioTrackInfo) currentInfo[0]};
        }
        AudioTrack[] queue = new AudioTrack[0];
        queue = this.queue.toArray(queue);
        int queueSize = queue.length;
        AudioTrackInfo currentInfo = (AudioTrackInfo) currentInfo()[0];
        AudioTrackInfo[] out = new AudioTrackInfo[queueSize + (currentInfo == null ? 0 : 1)];
        if (currentInfo != null) out[0] = currentInfo;
        for (int i = 0; i < queueSize; i++) out[i + (currentInfo == null ? 0 : 1)] = queue[i].getInfo();
        return out;
    }

    void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    void shuffle() {
        Collections.shuffle((List<?>) queue);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}