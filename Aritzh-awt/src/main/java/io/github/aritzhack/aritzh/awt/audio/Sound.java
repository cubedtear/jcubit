/*
 * Copyright 2014 Aritz Lopez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.aritzhack.aritzh.awt.audio;

import com.google.common.base.Preconditions;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

/**
 * Binder for WAV sound files
 *
 * @author Aritz Lopez
 */
public class Sound {

    private final Clip clip;

    public Sound(URL url) {
        Preconditions.checkArgument(url != null, "Audio Stream cannot be null!");
        try {
            this.clip = AudioSystem.getClip();
            this.clip.open(AudioSystem.getAudioInputStream(url));
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new IllegalArgumentException("Could not load sound!", e);
        }
    }

    public void play() {
        this.clip.setMicrosecondPosition(0);
        this.clip.start();
    }

    public boolean isPlaying() {
        return this.clip.isRunning();
    }

    public void stop() {
        this.clip.stop();
    }

    public void keepGoing() {
        this.clip.start();
    }
}
