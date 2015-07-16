package io.github.aritzhack.aritzh.awt.gameEngine.test;

import io.github.aritzhack.aritzh.awt.gameEngine.CanvasEngine;
import io.github.aritzhack.aritzh.awt.render.BufferedImageRenderer;
import io.github.aritzhack.aritzh.awt.render.IRender;
import io.github.aritzhack.aritzh.awt.render.Sprite;
import io.github.aritzhack.aritzh.gameEngine.IGame;
import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.NullLogger;
import io.github.aritzhack.aritzh.util.Nullable;

import java.util.Map;

/**
 * Simple engine that can be used to test things quickly. Uses {@link io.github.aritzhack.aritzh.awt.gameEngine.CanvasEngine} internally
 *
 * @author Aritz Lopez
 */
public class TestEngine implements IGame {

	private final ILogger LOGGER;
	private final Map<String, Sprite> sprites;
	private final CanvasEngine engine;
	private final IRender render;
	private final AbstractGame game;
	private final int height, width;

	/**
	 * Creates a TestEngine with the given game and size.
	 *
	 * @param game   The implementation of the game.
	 * @param width  The width of the game.
	 * @param height The height of the game.
	 */
	public TestEngine(AbstractGame game, int width, int height) {
		this(game, width, height, null, null);
	}

	/**
	 * Creates a TestEngine with the given game and size.
	 *
	 * @param game    The implementation of the game.
	 * @param width   The width of the game.
	 * @param height  The height of the game.
	 * @param logger  The logger that the {@link io.github.aritzhack.aritzh.awt.gameEngine.CanvasEngine} will use.
	 * @param sprites The sprites that the renderer will use.
	 */
	public TestEngine(AbstractGame game, int width, int height, @Nullable ILogger logger, @Nullable Map<String, Sprite> sprites) {
		this.game = game;
		this.sprites = sprites;
		this.width = width;
		this.height = height;
		this.LOGGER = NullLogger.getLogger(logger);
		this.engine = new CanvasEngine(this, this.width, this.height, false, LOGGER);

		if (this.sprites != null) this.render = new BufferedImageRenderer(this.width, this.height, this.sprites);
		else this.render = new BufferedImageRenderer(this.width, this.height);

		this.engine.start();
	}

	public void start() {
		this.engine.start();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void onStart() {
		this.game.onStart();
	}

	@Override
	public void onStop() {
		this.game.onStart();
	}

	@Override
	public void onRender() {
		this.render.clear();

		this.game.onRender();

		this.engine.getGraphics().drawImage(this.render.getImage(), 0, 0, width, height, null);

		this.game.onPostRender(this.engine.getGraphics());
	}

	@Override
	public void onUpdate() {
		this.game.onUpdate();
	}

	@Override
	public String getGameName() {
		return this.game.getGameName();
	}

	@Override
	public void onUpdatePS() {
		this.game.onUpdatePS();
	}

	public IRender getRender() {
		return render;
	}

	public CanvasEngine getEngine() {
		return engine;
	}
}
