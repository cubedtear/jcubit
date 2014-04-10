/*
 * Copyright (c) 2014 Aritzh (Aritz Lopez)
 *
 * This file is part of AritzhUtil
 *
 * AritzhUtil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * AritzhUtil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with AritzhUtil.
 * If not, see http://www.gnu.org/licenses/.
 */

package io.github.aritzhack.aritzh.gameEngine;

import com.google.common.base.Preconditions;
import io.github.aritzhack.aritzh.logging.ILogger;
import io.github.aritzhack.aritzh.gameEngine.input.InputHandler;
import io.github.aritzhack.aritzh.logging.NullLogger;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 * @author Aritz Lopez
 */
public class CanvasGame extends Canvas implements IGameEngine {

    private final InputHandler inputHandler;
    private final IGame gameHandler;
    private final Thread thread;
    private final Dimension size;
    private final ILogger logger;
    private JFrame frame;
    private boolean running;
    private boolean noFrame;
    private int ups, fps;

    public CanvasGame(IGame game, int width, int height) {
        this(game, width, height, false, null);
    }

    public CanvasGame(IGame game, int width, int height, boolean noFrame, ILogger logger) {
        Preconditions.checkArgument(game != null, "Game cannot be null!");
        Preconditions.checkArgument(width > 0 && height > 0, "Game sizes canot be negative!");

        if (logger == null) logger = new NullLogger();

        this.noFrame = noFrame;
        this.gameHandler = game;
        this.size = new Dimension(width, height);
        this.thread = new Thread(this, game.getGameName() + "-Thread");
        this.inputHandler = new InputHandler();
        this.logger = logger;

        this.addKeyListener(this.inputHandler);
        this.addFocusListener(this.inputHandler);
        this.addMouseListener(this.inputHandler);
        this.addMouseMotionListener(this.inputHandler);
        if (!this.noFrame) this.createFrame();
    }

    private void createFrame() {
        this.frame = new JFrame(this.gameHandler.getGameName());

        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.getContentPane().setMinimumSize(this.size);
        this.frame.getContentPane().setPreferredSize(this.size);
        this.frame.getContentPane().setSize(this.size);
        this.frame.getContentPane().setMaximumSize(this.size);
        this.frame.add(this);
        this.frame.pack();

        this.frame.setLocationRelativeTo(null);

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CanvasGame.this.stop();
            }
        });
    }

    @Override
    public synchronized void start() {
        this.running = true;
        this.frame.setVisible(true);
        this.thread.start();
    }

    @Override
    public synchronized void stop() {
        this.logger.d("Stopping...");
        this.gameHandler.onStop();
        this.running = false;
        if (!this.noFrame) this.frame.dispose();
        try {
            this.thread.join(3000);
        } catch (InterruptedException e) {
            this.logger.e("Error exiting!", e);
            System.exit(-1);
        }
        this.logger.d("Stopped.");
        System.exit(0);
    }

    @Override
    public void update() {
        this.gameHandler.onUpdate();
        //this.inputHandler.clearMouseEvents();
    }

    @Override
    public void render() {

        if (!running) return;

        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.size.width, this.size.height);

        this.gameHandler.onRender(g);

        g.dispose();
        bs.show();
    }

    @Override
    public void updatePS() {
        this.gameHandler.onUpdatePS();
    }

    @Override
    public IGame getGame() {
        return this.gameHandler;
    }

    @Override
    public int getFPS() {
        return fps;
    }

    @Override
    public int getUPS() {
        return ups;
    }

    @Override
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    @Override
    public void run() {

        final double NSPerTick = 1000000000.0 / 60.0;

        double delta = 0;

        long lastNano = System.nanoTime();
        long lastMillis = System.currentTimeMillis();

        this.gameHandler.onStart();

        while (this.running) {
            long now = System.nanoTime();
            delta += (now - lastNano) / NSPerTick;
            lastNano = now;

            if (delta >= 1) {
                this.update();
                this.ups++;
                delta--;
            }

            this.render();
            this.fps++;

            if (System.currentTimeMillis() - lastMillis >= 1000) {
                lastMillis += 1000;
                this.updatePS();
                this.fps = this.ups = 0;
            }
        }
    }
}
