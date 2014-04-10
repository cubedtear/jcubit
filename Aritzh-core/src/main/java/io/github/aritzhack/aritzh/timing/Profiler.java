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

package io.github.aritzhack.aritzh.timing;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Stack;

/**
 * @author Aritz Lopez
 */
@SuppressWarnings("UnusedDeclaration")
public class Profiler {

    private static final Profiler DEFAULT;
    private static final Map<String, Profiler> profilers = Maps.newHashMap();

    static {
        DEFAULT = new Profiler(false);
        Profiler.profilers.put("DEFAULT", Profiler.DEFAULT);
    }

    private Map<String, Long> startTime = Maps.newHashMap();
    private Map<String, Long> elapsedTime = Maps.newHashMap();
    private Stack<String> trace = new Stack<>();
    private boolean verbose;

    private Profiler(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Returns the default profiler.
     * Equivalent to calling {@link Profiler#getInstance(String) getInstance("DEFAULT")}
     * The default profiler has verbosity turned off
     *
     * @return The default profiler
     * @see Profiler#getInstance(String, boolean)
     */
    public static Profiler getInstance() {
        return Profiler.DEFAULT;
    }

    /**
     * Returns a specific instance of profiler, with no verbosity <br>
     * <font color="#191919">
     * NOTE: Non-verbosity is not guaranteed. since loggers are cached, if there was another
     * profiler with the specified name and it was verbose, that instance will be
     * returned. To set the verbosity afterwards see {@link Profiler#setVerbose(boolean)}
     * </font>
     *
     * @param name The name of the profiler
     * @return A profiler with the specified name
     */
    public static Profiler getInstance(String name) {
        return Profiler.getInstance(name, false);
    }

    /**
     * Returns a specific instance of profiler, specifying verbosity
     * <br>
     * <font color="red">
     * NOTE: Verbosity is not guaranteed. since loggers are cached, if there was another
     * profiler with the specified name and it was not verbose, that instance will be
     * returned. To set the verbosity afterwards see {@link Profiler#setVerbose(boolean)}
     * </font>
     *
     * @param name    The name of the profiler
     * @param verbose Whether to print to {@link System#out} when ending a section
     * @return A profiler identified by the specified name.
     */
    public static Profiler getInstance(String name, boolean verbose) {
        if (Profiler.profilers.containsKey(name)) return Profiler.profilers.get(name);
        else {
            Profiler p = new Profiler(verbose);
            Profiler.profilers.put(name, p);
            return p;
        }
    }

    /**
     * Starts a profiling section.
     *
     * @param section The name of the section
     */
    public synchronized void startSection(String section) {
        section = section.toLowerCase();

        if (this.startTime.containsKey(section))
            throw new IllegalArgumentException("Section \"" + section + "\" had already been started!");

        this.startTime.put(section, System.nanoTime());
        this.trace.push(section);
    }

    /**
     * Ends the last started section.
     * Time elapsed from the call to {@link Profiler#startSection(String)} is stored
     * for later use
     */
    public synchronized void endSection() {
        if (this.trace.empty()) throw new IllegalStateException("There are no open sections to close!");
        this.endSection(this.trace.pop());
    }

    /**
     * Ends the specified section.
     * Time elapsed from the call to {@link Profiler#startSection(String)} is stored
     *
     * @param section The name of the section
     */
    public synchronized void endSection(String section) {
        long now = System.nanoTime();
        section = section.toLowerCase();
        if (!this.startTime.containsKey(section))
            throw new IllegalArgumentException("Section \"" + section + "\" hasn't been started!");
        long elapsed = now - this.startTime.remove(section);
        this.elapsedTime.put(section, elapsed);
        if (this.verbose) System.out.println("Section " + section + " lasted " + elapsed + "ns");
    }

    /**
     * Gets the time elapsed, from calling {@link Profiler#startSection(String) startSection(section)}
     * to calling {@link Profiler#endSection(String) endSection(section)} in milliseconds. <br>
     * If the elapsed section time was not profiled, returns -1.
     *
     * @param section The name of the section of which the elapsed time to request
     * @return The elapsed section time if the section was profiled, or {@code -1} otherwise
     */
    public synchronized long getSectionTime(String section) {
        section = section.toLowerCase();
        if (!this.elapsedTime.containsKey(section)) return -1;
        return this.elapsedTime.get(section);
    }

    /**
     * Whether this profiler will print to {@link System#out} when ending a section
     *
     * @return {@code true} if this profiler will print to {@link System#out} when ending a section, {@code false} otherwise
     */
    public boolean isVerbose() {
        return this.verbose;
    }

    /**
     * Sets the verbosity of this profiler <br>
     *
     * @param verbose The value to which the verbosity of this profiler will be set
     */
    public synchronized Profiler setVerbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }
}
