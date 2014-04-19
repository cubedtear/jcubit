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

package io.github.aritzhack.aritzh;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.github.aritzhack.aritzh.logging.ILogger;
import io.github.aritzhack.aritzh.logging.SLF4JLogger;
import io.github.aritzhack.aritzh.util.NotNull;
import io.github.aritzhack.aritzh.util.OneOrOther;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
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

    private final OneOrOther<File, Path> configFile;
    private final boolean compressedSpaces;
    private final Map<String, LinkedHashMap<String, String>> categories = Maps.newLinkedHashMap();

    private Configuration(OneOrOther<File, Path> configFile, boolean compressedSpaces) {
        this.configFile = configFile;
        this.compressedSpaces = compressedSpaces;
    }

    /**
     * Loads the configuration file <br>
     * If the config file did not exist, it will be created
     * <p/>
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
     * Loads the configuration from the given path <br>
     * If the config path did not exist, it will be created
     * <p/>
     * Equivalent to calling {@code Configuration.loadConfig(configFile, false)}
     *
     * @param configFile Path to read the configuration from
     * @return A new configuration object, already parsed from {@code configFile}
     * @see Configuration#loadConfig(java.nio.file.Path, boolean)
     */
    public static Configuration loadConfig(Path configFile) {
        return Configuration.loadConfig(configFile, false);
    }

    /**
     * Loads the configuration from the given path, specifying if spaces should be compressed or not ({@code currentLine.replaceAll("\\s+", " ")})
     * If the config path did not exist, it will be created
     * Equivalent to calling {@code Configuration.loadConfig(configFile, compressedSpaces, false)}
     *
     * @param configFile       Path to read the configuration from
     * @param compressedSpaces If true subsequent whitespaces will be replaced with a single one (defaults to false)
     * @return A new configuration object, already parsed, from {@code configFile}
     * @see Configuration#loadConfig(java.nio.file.Path, boolean, boolean)
     */
    private static Configuration loadConfig(Path configFile, boolean compressedSpaces) {
        return Configuration.loadConfig(configFile, compressedSpaces, false);
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
            return Configuration.newConfig(OneOrOther.ofOne(configFile));
        }

        Configuration config = new Configuration(OneOrOther.ofOne(configFile), compressSpaces);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)))) {
            loadConfig(config, reader, compressSpaces, verbose);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    /**
     * Loads the configuration path, specifying if spaces should be compressed or not ({@code currentLine.replaceAll("\\s+", " ")})
     * If the config path did not exist, it will be created
     *
     * @param configFile     Path to read the configuration from
     * @param compressSpaces If true subsequent whitespaces will be replaced with a single one (defaults to {@code false})
     * @param verbose        Whether to log unrecognized lines or not (defaults to {@code false})
     * @return A new configuration object, already parsed, from {@code configFile}
     */
    public static Configuration loadConfig(Path configFile, boolean compressSpaces, boolean verbose) {
        if (Files.notExists(configFile)) {
            return Configuration.newConfig(OneOrOther.ofOther(configFile));
        }

        Configuration config = new Configuration(OneOrOther.ofOther(configFile), compressSpaces);

        try {
            loadConfig(config, Files.newBufferedReader(configFile), compressSpaces, verbose);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    private static void loadConfig(final Configuration config, final BufferedReader reader, final boolean compressSpaces, final boolean verbose) {
        reader.lines().sequential().forEachOrdered(new Consumer<String>() {
            String currentCategory = "";

            @Override
            public void accept(String line) {
                line = compressSpaces ? line.replaceAll("\\s+", " ").trim() : line;

                Matcher m;

                if ((SKIP_REGEX.matcher(line)).matches()) {
                    // Do nothing
                } else if ((m = CATEGORY_REGEX.matcher(line)).matches()) {
                    currentCategory = config.addCategory(m);
                } else if ((m = PROP_REGEX.matcher(line)).matches()) {
                    config.addProperty(m, currentCategory);
                } else if (verbose) {
                    Configuration.logger.d("Unknown-line: {}", line);
                }
            }
        });
    }

    private static Configuration newConfig(OneOrOther<File, Path> configFile) {
        return new Configuration(configFile, false);
    }

    /**
     * Creates a new empty configuration object for the specified file
     *
     * @param configFile the config file (doesn't need to exist)
     * @return A new empty configuration object
     */
    public static Configuration newConfig(@NotNull File configFile) {
        return new Configuration(OneOrOther.ofOne(configFile), false);
    }

    /**
     * Creates a new empty configuration object for the specified file
     *
     * @param configFile the config file (doesn't need to exist)
     * @return A new empty configuration object
     */
    public static Configuration newConfig(@NotNull Path configFile) {
        return new Configuration(OneOrOther.ofOther(configFile), false);
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
        if (Strings.isNullOrEmpty(category)) category = "Main";
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
        return Boolean.parseBoolean(value) || "on".equalsIgnoreCase(value);
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
        if (Strings.isNullOrEmpty(category)) category = "Main";
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
    private void save(OneOrOther<File, Path> configFile) throws IOException {
        this.saveToWriter(configFile.map(Configuration::getWriter, Configuration::getWriter));
    }

    private void saveToWriter(BufferedWriter writer) throws IOException {
        if (writer == null) return;
        writer.write("# Last edit: " + new Date());
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

    private static BufferedWriter getWriter(File file) {
        try {
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedWriter getWriter(Path path) {
        try {
            return Files.newBufferedWriter(path, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
