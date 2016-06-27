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

package io.github.cubedtear.jcubit.config;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.util.NotNull;
import io.github.cubedtear.jcubit.util.OneOrOther;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kind of {@link java.util.Properties}, but with category names, and returning {@link String}, not
 * {@link Object}.
 *
 * @author Aritz Lopez
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
public class Configuration {

	public static final Pattern CATEGORY_REGEX = Pattern.compile("^\\s*\\[(?:\\s*(?=[a-zA-Z_0-9{}]+))(.+)\\s*\\]\\s*$");// Category is trimmed, but keeps spaces in between
	public static final Pattern PROP_REGEX = Pattern.compile("^\\s*(\\w+)\\s*=(?:\\s*(?=\\w+))(.*)\\s*$");
	public static final Pattern SKIP_REGEX = Pattern.compile("(^\\s*$)|(^\\s*#.*$)"); // Empty line, or: Spaces or not, followed by # and followed by anything

	private final OneOrOther<File, Path> configFile;
	private final boolean compressedSpaces;
	private final Map<String, LinkedHashMap<String, String>> categories = Maps.newLinkedHashMap();

	private Configuration(boolean compressedSpaces) {
		this.configFile = null;
		this.compressedSpaces = compressedSpaces;
	}

	private Configuration(OneOrOther<File, Path> configFile, boolean compressedSpaces) {
		this.configFile = configFile;
		this.compressedSpaces = compressedSpaces;
	}

	/**
	 * Loads the configuration file. <br>
	 * If the config file did not exist, it will be created.
	 * <p>
	 * Equivalent to calling {@code Configuration.loadConfig(configFile, false)}.
	 * </p>
	 *
	 * @param configFile File to read the configuration from.
	 * @return A new configuration object, already parsed from {@code configFile}.
	 * @see Configuration#loadConfig(java.io.File, boolean)
	 */
	public static Configuration loadConfig(File configFile) {
		return Configuration.loadConfig(configFile, false);
	}

	/**
	 * Loads the configuration file, specifying if spaces should be compressed or not ({@code currentLine.replaceAll("\\s+", " ")}).
	 * If the config file did not exist, it will be created.
	 *
	 * @param configFile     File to read the configuration from.
	 * @param compressSpaces If true subsequent whitespaces will be replaced with a single one (defaults to {@code false}).
	 * @return A new configuration object, already parsed, from {@code configFile}.
	 */
	public static Configuration loadConfig(File configFile, boolean compressSpaces) {
		if (!configFile.exists()) {
			return Configuration.newConfig(OneOrOther.<File, Path>ofOne(configFile));
		}

		Configuration config = new Configuration(OneOrOther.<File, Path>ofOne(configFile), compressSpaces);

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)))) {
			loadConfig(config, reader, compressSpaces);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return config;
	}

	public static Configuration loadConfig(InputStream is, boolean compressSpaces) {
		if (is == null) {
			throw new IllegalArgumentException("InputStream is null!");
		}

		Configuration config = new Configuration(compressSpaces);

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			loadConfig(config, reader, compressSpaces);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return config;
	}

	private static Configuration newConfig(OneOrOther<File, Path> configFile) {
		return new Configuration(configFile, false);
	}

	private static void loadConfig(final Configuration config, final BufferedReader reader, final boolean compressSpaces) {
		String line;
		String currCategory = "";
		try {
			while ((line = reader.readLine()) != null) {
				line = compressSpaces ? line.replaceAll("\\s+", " ").trim() : line;

				currCategory = (compressSpaces ? currCategory.replaceAll("\\s+", " ") : currCategory).trim();
				if (Strings.isNullOrEmpty(currCategory)) {
					currCategory = "Main";
					if (!config.categories.containsKey(currCategory)) config.addCategory(currCategory);
				}

				Matcher m;

				if ((SKIP_REGEX.matcher(line)).matches()) continue;

				if ((m = CATEGORY_REGEX.matcher(line)).matches()) {
					currCategory = config.addCategory(m.group(1));
				} else if ((m = PROP_REGEX.matcher(line)).matches()) {
					config.addProperty(m, currCategory);
				}
			}
		} catch (IOException e) {
			throw new ParseException("Error parsing config", e);
		}
	}

	/**
	 * Loads the configuration from the given path <br>.
	 * If the config path did not exist, it will be created.
	 * <p>
	 * Equivalent to calling {@code Configuration.loadConfig(configFile, false)}.
	 * </p>
	 *
	 * @param configFile Path to read the configuration from.
	 * @return A new configuration object, already parsed from {@code configFile}.
	 * @see Configuration#loadConfig(java.nio.file.Path, boolean)
	 */
	public static Configuration loadConfig(Path configFile) {
		return Configuration.loadConfig(configFile, false);
	}

	/**
	 * Loads the configuration path, specifying if spaces should be compressed or not ({@code currentLine.replaceAll("\\s+", " ")}).
	 * If the config path did not exist, it will be created.
	 *
	 * @param configFile     Path to read the configuration from.
	 * @param compressSpaces If true subsequent whitespaces will be replaced with a single one (defaults to {@code false}).
	 * @return A new configuration object, already parsed, from {@code configFile}.
	 */
	public static Configuration loadConfig(Path configFile, boolean compressSpaces) {
		if (Files.notExists(configFile)) {
			return Configuration.newConfig(OneOrOther.<File, Path>ofOther(configFile));
		}

		Configuration config = new Configuration(OneOrOther.<File, Path>ofOther(configFile), compressSpaces);

		try (BufferedReader reader = Files.newBufferedReader(configFile, Charset.defaultCharset())) {
			loadConfig(config, reader, compressSpaces);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}

	/**
	 * Creates a new empty configuration object for the specified file.
	 *
	 * @param configFile the config file (doesn't need to exist).
	 * @return A new empty configuration object.
	 */
	public static Configuration newConfig(@NotNull File configFile) {
		return new Configuration(OneOrOther.<File, Path>ofOne(configFile), false);
	}

	/**
	 * Creates a new empty configuration object for the specified file.
	 *
	 * @param configFile the config file (doesn't need to exist).
	 * @return A new empty configuration object.
	 */
	public static Configuration newConfig(@NotNull Path configFile) {
		return new Configuration(OneOrOther.<File, Path>ofOther(configFile), false);
	}

	private static BufferedWriter getFileWriter(File file) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
	}

	public static BufferedWriter getPathWriter(Path path) throws IOException {
		return Files.newBufferedWriter(path, Charset.defaultCharset(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	}

	private String addCategory(String cat) {
		if (!this.categories.containsKey(cat)) this.categories.put(cat, Maps.<String, String>newLinkedHashMap());
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
	 * Sets a property in the configuration.
	 *
	 * @param category The category of the property.
	 * @param key      The key to identify the property.
	 * @param value    The value associated with it.
	 */
	public void setProperty(String category, String key, Object value) {
		this.setProperty(category, key, value.toString());
	}

	/**
	 * Sets a property in the configuration.
	 *
	 * @param category The category of the property.
	 * @param key      The key to identify the property.
	 * @param value    The value associated with it.
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
	 * Sets the property, unless it already had a value.
	 *
	 * @param category The category of the property.
	 * @param key      The identifier of the property.
	 * @param value    The value to set to the property.
	 */
	public void setDefault(String category, String key, String value) {
		if (this.hasProperty(category, key)) return;
		this.setProperty(category, key, value);
	}

	/**
	 * Checks whether the specified key is present in the specified category.
	 *
	 * @param category The category to look into for the key.
	 * @param key      The key to look for.
	 * @return {@code true} if the key was found in the category, {@code false} otherwise.
	 */
	public boolean hasProperty(String category, String key) {
		return this.categories.containsKey(category) && this.categories.get(category).containsKey(key);
	}

	/**
	 * Same as {@link Configuration#getProperty(String, String)}, but a boolean is parsed.
	 *
	 * @param category The category of the property.
	 * @param key      The key (identifier) of the property.
	 * @return {@code true} if the property can be parsed to boolean, or equals (ignoring case) {@code "on"}.
	 */
	public boolean getBoolean(String category, String key) {
		String value = this.getProperty(category, key);
		return Boolean.parseBoolean(value) || "on".equalsIgnoreCase(value);
	}

	/**
	 * Gets a property with an empty string as default value. <br>
	 * Equivalent to calling {@code config.getProperty(category, key, "")}.
	 *
	 * @param category The category in which the property is.
	 * @param key      The key of the property.
	 * @return The value associated with {@code key} in {@code category}, or {@code ""} (an empty string) if not found.
	 */
	public String getProperty(String category, String key) {
		return this.getProperty(category, key, "");
	}

	/**
	 * Gets a property.
	 *
	 * @param category     The category in which the property is.
	 * @param key          The key of the property.
	 * @param defaultValue If the property couldn't be found, this will be returned.
	 * @return The value associated with {@code key} in {@code category}, or {@code defaultValue} if not found.
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
	 * @param category The category of the property.
	 * @param key      The key (identifier) of the property.
	 * @return the integer value parsed from the property.
	 */
	public int getInt(String category, String key) {
		String value = this.getProperty(category, key).toLowerCase().trim();
		return Integer.parseInt(value);
	}

	/**
	 * Same as {@link Configuration#getProperty(String, String)}, but a double is parsed.
	 *
	 * @param category The category of the property.
	 * @param key      The key (identifier) of the property.
	 * @return the double value parsed from the property.
	 */
	public double getDouble(String category, String key) {
		String value = this.getProperty(category, key).toLowerCase().trim();
		return Double.parseDouble(value);
	}

	/**
	 * Saves the configuration to the file it was created with. <br>
	 * Equivalent to calling {@code config.save(configFile)}, if {@code config} was created with {@code configFile}
	 *
	 * @throws IOException if an I/O error occurs.
	 */
	public void save() throws IOException {
		if(this.configFile == null) throw new IllegalStateException("Cannot save if constructed without a file!");
		this.save(configFile);
	}

	/**
	 * Saves the configuration to {@code f}.
	 * @param f The file to save the configuration to.
	 * @throws IOException if an I/O error occurs.
	 */
	public void save(File f) throws IOException {
		this.saveToWriter(getFileWriter(f));
	}

	/**
	 * Saves the configuration to {@code p}.
	 * @param p The file to save the configuration to.
	 * @throws IOException if an I/O error occurs.
	 */
	public void save(Path p) throws IOException {
		this.saveToWriter(getPathWriter(p));
	}

	/**
	 * Saves the configuration to {@code os}.
	 * @param os The {@link OutputStream} to save the configuration to.
	 * @throws IOException if an I/O error occurs.
	 */
	public void save(OutputStream os) throws IOException {
		this.saveToWriter(new BufferedWriter(new OutputStreamWriter(os)));
	}

	/**
	 * Saves the configuration to {@code configFile}.
	 *
	 * @param configFile The file to save the configuration to.
	 * @throws IOException if an I/O error occurs.
	 */
	private void save(OneOrOther<File, Path> configFile) throws IOException {
		if (configFile.isOne()) save(configFile.getOne());
		else save(configFile.getOther());
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
		writer.close();
	}
}
