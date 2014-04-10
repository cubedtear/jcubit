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

package io.github.aritzhack.aritzh;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.github.aritzhack.aritzh.logging.ILogger;
import io.github.aritzhack.aritzh.logging.SLF4JLogger;

import java.io.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kind of {@link java.util.Properties}, but with category names, and returning {@link String}, not
 * {@link Object}
 *
 * @author Aritz Lopez
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
public class Configuration {

    public static final Pattern CATEGORY_REGEX = Pattern.compile("^\\s*\\[(?:\\s*(?=\\w+))(.+)\\s*\\]\\s*$");// Category is trimmed, but keeps spaces in between
    public static final Pattern PROP_REGEX = Pattern.compile("^\\s*(\\w+)\\s*=(?:\\s*(?=\\w+))(.*)\\s*$");
    public static final Pattern SKIP_REGEX = Pattern.compile("(^\\s*$)|(^\\s*#.*$)"); // Empty line, or: Spaces or not, followed by # and followed by anything
    public static final ILogger logger = new SLF4JLogger(Configuration.class);

    private final File configFile;
    private final boolean compressedSpaces;
    private final Map<String, LinkedHashMap<String, String>> categories = Maps.newLinkedHashMap();

    private Configuration(File configFile, boolean compressedSpaces) {
        this.configFile = configFile;
        this.compressedSpaces = compressedSpaces;
    }

    /**
     * Loads the configuration file <br>
     * If the config file did not exist, it will be created
     * <p>
     * Equivalent to calling {@code Configuration.loadConfig(configFile, false)}
     *
     * @param configFile File to read the configuration from
     * @return A new configuration object, already parsed from {@code configFile}
     * @see Configuration#loadConfig(java.io.File, boolean)
     */
    public static Configuration loadConfig(File configFile) {
        return Configuration.loadConfig(configFile, false);
    }

    /**
     * Loads the configuration file, specifying if spaces should be compressed or not ({@code currentLine.replaceAll("\\s+", " ")})
     * If the config file did not exist, it will be created
     * Equivalent to calling {@code Configuration.loadConfig(configFile, compressedSpaces, false)}
     *
     * @param configFile       File to read the configuration from
     * @param compressedSpaces If true subsequent whitespaces will be replaced with a single one (defaults to false)
     * @return A new configuration object, already parsed, from {@code configFile}
     * @see Configuration#loadConfig(java.io.File, boolean, boolean)
     */
    public static Configuration loadConfig(File configFile, boolean compressedSpaces) {
        return Configuration.loadConfig(configFile, compressedSpaces, false);
    }

    /**
     * Loads the configuration file, specifying if spaces should be compressed or not ({@code currentLine.replaceAll("\\s+", " ")})
     * If the config file did not exist, it will be created
     *
     * @param configFile     File to read the configuration from
     * @param compressSpaces If true subsequent whitespaces will be replaced with a single one (defaults to {@code false})
     * @param verbose        Whether to log unrecognized lines or not (defaults to {@code false})
     * @return A new configuration object, already parsed, from {@code configFile}
     */
    public static Configuration loadConfig(File configFile, boolean compressSpaces, boolean verbose) {
        if (!configFile.exists()) {
            return Configuration.newConfig(configFile);
        }
        Configuration config = new Configuration(configFile, compressSpaces);
        String currentLine;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)))) {

            String currentCategory = "";

            while ((currentLine = reader.readLine()) != null) {
                currentLine = compressSpaces ? currentLine.replaceAll("\\s+", " ").trim() : currentLine;

                Matcher m;

                if ((SKIP_REGEX.matcher(currentLine)).matches()) {
                    continue;
                } else if ((m = CATEGORY_REGEX.matcher(currentLine)).matches()) {
                    currentCategory = config.addCategory(m);
                } else if ((m = PROP_REGEX.matcher(currentLine)).matches()) {
                    config.addProperty(m, currentCategory);
                } else if (verbose) {
                    Configuration.logger.d("Unknown-line: {}", currentLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    /**
     * Creates a new empty configuration object for the specified file
     *
     * @param configFile the config file (doesn't need to exist)
     * @return A new empty configuration object
     */
    public static Configuration newConfig(File configFile) {
        return new Configuration(configFile, false);
    }

    private String addCategory(Matcher m) {
        String cat = m.group(1);
        this.categories.put(cat, Maps.<String, String>newLinkedHashMap());
        return cat;
    }

    private void addProperty(Matcher m, String category) {
        String key = m.group(1);
        String val = m.group(2);

        LinkedHashMap<String, String> currCat = this.categories.get(category);
        currCat.put(key, val);
        this.categories.put(category, currCat);
    }

    /**
     * Sets a property in the configuration
     *
     * @param category The category of the property
     * @param key      The key to identify the property
     * @param value    The value associated with it
     */
    public void setProperty(String category, String key, Object value) {
        this.setProperty(category, key, value.toString());
    }

    /**
     * Sets a property in the configuration
     *
     * @param category The category of the property
     * @param key      The key to identify the property
     * @param value    The value associated with it
     */
    public void setProperty(String category, String key, String value) {
        category = (this.compressedSpaces ? category.replaceAll("\\s+", " ") : category).trim();
        if (category.equals("")) category = "Main";
        key = (this.compressedSpaces ? key.replaceAll("\\s+", " ") : key).trim().replace(" ", "_");
        value = (this.compressedSpaces ? value.replaceAll("\\s+", " ") : value).trim();

        if (!this.categories.containsKey(category))
            this.categories.put(category, Maps.<String, String>newLinkedHashMap());
        LinkedHashMap<String, String> currCat = this.categories.get(category);
        currCat.put(key, value);
        this.categories.put(category, currCat);
    }

    public void setDefault(String category, String key, Object value) {
        this.setDefault(category, key, String.valueOf(value));
    }

    /**
     * Sets the property, unless it already had a value
     *
     * @param category The category of the property
     * @param key      The identifier of the property
     * @param value    The value to set to the property
     */
    public void setDefault(String category, String key, String value) {
        if (this.hasProperty(category, key)) return;
        this.setProperty(category, key, value);
    }

    /**
     * Checks whether the specified key is present in the specified category
     *
     * @param category The category to look into for the key
     * @param key      The key to look for
     * @return {@code true} if the key was found in the category, {@code false} otherwise
     */
    public boolean hasProperty(String category, String key) {
        return this.categories.containsKey(category) && this.categories.get(category).containsKey(key);
    }

    /**
     * Same as {@link Configuration#getProperty(String, String)}, but a boolean is parsed.
     *
     * @param category The category of the property
     * @param key      The key (identifier) of the property
     * @return {@code true} if the property can be parsed to boolean, or equals (ignoring case) {@code "on"}
     */
    public boolean getBoolean(String category, String key) {
        String value = this.getProperty(category, key);
        return Boolean.parseBoolean(value) || value.equalsIgnoreCase("on");
    }

    /**
     * Gets a property with an empty string as default value. <br>
     * Equivalent to calling {@code config.getProperty(category, key, "")}
     *
     * @param category The category in which the property is
     * @param key      The key of the property
     * @return The value associated with {@code key} in {@code category}, or {@code ""} (an empty string) if not found
     */
    public String getProperty(String category, String key) {
        return this.getProperty(category, key, "");
    }

    /**
     * Gets a property
     *
     * @param category     The category in which the property is
     * @param key          The key of the property
     * @param defaultValue If the property couldn't be found, this will be returned
     * @return The value associated with {@code key} in {@code category}, or {@code defaultValue} if not found
     */
    public String getProperty(String category, String key, String defaultValue) {
        category = (this.compressedSpaces ? category.replaceAll("\\s+", " ") : category).trim();
        if (category.equals("")) category = "Main";
        key = (this.compressedSpaces ? key.replaceAll("\\s+", " ") : key).trim().replace(" ", "_");
        try {
            return this.categories.get(category).get(key).replace("_", " ");
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * Same as {@link Configuration#getProperty(String, String)}, but a integer is parsed.
     *
     * @param category The category of the property
     * @param key      The key (identifier) of the property
     * @return the integer value parsed from the property
     */
    public int getInt(String category, String key) {
        String value = this.getProperty(category, key).toLowerCase().trim();
        return Integer.parseInt(value);
    }

    /**
     * Same as {@link Configuration#getProperty(String, String)}, but a double is parsed.
     *
     * @param category The category of the property
     * @param key      The key (identifier) of the property
     * @return the double value parsed from the property
     */
    public double getDouble(String category, String key) {
        String value = this.getProperty(category, key).toLowerCase().trim();
        return Double.parseDouble(value);
    }

    /**
     * Saves the configuration to the file it was created with <br>
     * Equivalent to calling {@code config.save(configFile)}, if {@code config} was created with {@code configFile}
     */
    public void save() throws IOException {
        this.save(configFile);
    }

    /**
     * Saves the configuration to {@code configFile}
     *
     * @param configFile The file to save the configuration to
     */
    private void save(File configFile) throws IOException {
        Preconditions.checkNotNull(configFile, "Cannot save to null file!!");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)))) {
            writer.write("# Last edit: " + new Date().toString());
            writer.newLine();
            for (Map.Entry<String, LinkedHashMap<String, String>> e1 : this.categories.entrySet()) {
                writer.write("[" + e1.getKey() + "]");
                writer.newLine();
                for (Map.Entry<String, String> e2 : e1.getValue().entrySet()) {
                    writer.write(e2.getKey() + "=" + e2.getValue());
                    writer.newLine();
                }
                writer.newLine();
            }
            writer.flush();
        }
    }
}
