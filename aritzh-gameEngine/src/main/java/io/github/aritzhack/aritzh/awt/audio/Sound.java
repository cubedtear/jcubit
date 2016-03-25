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

	/**
	 * Load a Sound from a URL.
	 *
	 * @param url The URL from which the sound will be loaded.
	 */
	public Sound(URL url) {
		Preconditions.checkArgument(url != null, "Audio Stream cannot be null!");
		try {
			this.clip = AudioSystem.getClip();
			this.clip.open(AudioSystem.getAudioInputStream(url));
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			throw new IllegalArgumentException("Could not load sound!", e);
		}
	}

	/**
	 * Start playing the sound.
	 * Note: Starts from the beginning each time. In order to keep playing from where it stopped, use {@link Sound#keepGoing()}.
	 *
	 * @see Sound#keepGoing()
	 */
	public void play() {
		this.clip.setMicrosecondPosition(0);
		this.clip.start();
	}

	/**
	 * True iff it is playing.
	 *
	 * @return true if it is playing.
	 */
	public boolean isPlaying() {
		return this.clip.isRunning();
	}

	/**
	 * Stops playing the sound. Will keep the position so that the playback can be continued with {@link Sound#keepGoing()}.
	 *
	 * @see Sound#keepGoing()
	 */
	public void stop() {
		this.clip.stop();
	}

	/**
	 * Plays the sound from where it was stopped, or from the beginning if it was just loaded.
	 */
	public void keepGoing() {
		this.clip.start();
	}
}
