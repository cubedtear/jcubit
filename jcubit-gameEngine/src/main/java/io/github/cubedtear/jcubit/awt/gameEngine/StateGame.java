package io.github.cubedtear.jcubit.awt.gameEngine;

import io.github.cubedtear.jcubit.awt.render.BufferedImageRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 * @author Aritz Lopez
 */
public class StateGame {

    private final Dimension size;
    private final JFrame frame;
    private final Canvas canvas;
    private final Thread thread;
    private final BufferedImageRenderer render;
    private final int targetUps;

    private IGameState state;
    private String title;
    private boolean running;
    private int fps, ups;

    public StateGame(int width, int height, IGameState initialState, String title, int targetUps) {
        this.targetUps = targetUps;
        this.size = new Dimension(width, height);
        this.state = initialState;
        this.title = title;

        this.frame = new JFrame(title);
        this.canvas = new Canvas();
        this.render = new BufferedImageRenderer(width, height);

        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StateGame.this.run();
            }
        }, "GameThread");

        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.getContentPane().setMinimumSize(this.size);
        this.frame.getContentPane().setPreferredSize(this.size);
        this.frame.getContentPane().setSize(this.size);
        this.frame.getContentPane().setMaximumSize(this.size);
        this.frame.add(canvas);
        this.frame.pack();

        this.frame.setLocationRelativeTo(null);

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                StateGame.this.stop();
            }
        });

        this.frame.setVisible(false);

        // TODO Class that converts AWT events, to "Event" events.
        InputAdapter inputAdapter = new InputAdapter(this);

        canvas.addKeyListener(inputAdapter);
        canvas.addFocusListener(inputAdapter);
        canvas.addMouseListener(inputAdapter);
        canvas.addMouseMotionListener(inputAdapter);
        canvas.addMouseWheelListener(inputAdapter);
    }

    public synchronized void start() {
        this.running = true;
        this.thread.start();
    }

    public synchronized void stop() {
        // TODO Add onClose Event, and send it to the current state.
    }

    private void run() {
        this.frame.setVisible(true);
        this.fps = this.ups = 0;

        double delta = 0;

        long lastNano = System.nanoTime();
        long lastMillis = System.currentTimeMillis();

        // TODO Post onStart event.

        this.running = true;

        while (this.running) {

            double NSPerTick = 1000000000.0 / this.targetUps;

            long now = System.nanoTime();
            delta += (now - lastNano) / NSPerTick;
            lastNano = now;

            if (delta >= 1 || this.targetUps < 0) {
                this.state.update(this);
                this.ups++;
                delta--;
            }

            this.render();
            this.fps++;

            if (System.currentTimeMillis() - lastMillis >= 1000) {
                lastMillis += 1000;
                this.fps = this.ups = 0;
            }
        }
    }

    private void render() {
        if (!running) return;

        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.size.width, this.size.height);

        this.state.draw(render);

        g.drawImage(this.render.getImage(), 0, 0, size.width, size.height, null);

        g.dispose();
        bs.show();
    }

    public void setState(IGameState state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Dimension getSize() {
        return size;
    }

    public IGameState getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public int getFps() {
        return fps;
    }
}
