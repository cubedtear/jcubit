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
import com.google.common.collect.Lists;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aritz Lopez
 */
@SuppressWarnings("UnusedDeclaration")
public class IOUtil {

    private static final FileFilter acceptAll = pathname -> true;

    /**
     * Get all files matching the {@link java.io.FileFilter} under the specified folder, including those in sub-folders, sub-sub-folders, etc.
     *
     * @param folder The root folder to look for files from
     * @param filter The filter the files must match in order to be accepted
     * @return A list of all files matching the filter under the folder
     */
    public static List<File> getSubFiles(File folder, FileFilter filter) {
        Preconditions.checkArgument(folder.isDirectory(), "Argument \"folder\" must be a directory!");

        ArrayList<File> ret = Lists.newArrayList();

        for (File f : folder.listFiles(filter)) {
            if (f.isFile()) ret.add(f);
            else if (f.isDirectory()) ret.addAll(IOUtil.getSubFiles(f, filter));
        }
        return ret;
    }

    /**
     * Get all files under the specified folder, including those inside sub-folders, sub-sub-folders, etc.
     *
     * @param folder The root folder to look for files from
     * @return A list of all files under the folder
     */
    public static List<File> getSubFiles(File folder) {
        return IOUtil.getSubFiles(folder, acceptAll);
    }

    /**
     * Open a {@link javax.swing.JFileChooser}, and return the selected file, or null if closed or cancelled.
     *
     * @param title      The title of the file-chooser window.
     * @param currentDir The root directory. If null, {@code new File(".")} will be used.
     * @return The chosen file, or null if none was chosen.
     */
    public static File chooseFile(String title, File currentDir) {
        if (currentDir == null) currentDir = new File(".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDir);
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Open a {@link javax.swing.JFileChooser} with multi-selection enabled, and return the selected file(s), or null if closed or cancelled.
     *
     * @param title      The title of the file-chooser window.
     * @param currentDir The root directory. If null, {@code new File(".")} will be used.
     * @return The chosen file(s), or null if none was chosen.
     */
    public static File[] chooseFiles(String title, File currentDir) {
        if (currentDir == null) currentDir = new File(".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDir);
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFiles();
        }
        return null;
    }

    /**
     * Open a {@link javax.swing.JFileChooser} which can only select folders, and return the selected folder, or null if closed or cancelled.
     *
     * @param title      The title of the file-chooser window.
     * @param currentDir The root directory. If null, {@code new File(".")} will be used.
     * @return The chosen folder, or null if none was chosen.
     */
    public static File chooseFolder(String title, File currentDir) {
        if (currentDir == null) currentDir = new File(".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDir);
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Gets a folder to store application data, such as save or config files.
     * It will be located under:
     * <ul>
     * <li>In Windows: {@code %APPDATA%\name}, and if it fails, under {@code System.getProperty("user.home")\name}.</li>
     * <li>In OSX: under {@code System.getProperty("user.home")/Library/Application Support/name}.</li>
     * <li>In any other case: under {@code System.getProperty("user.home")/name}.</li>
     * </ul>
     *
     * @param name The name of the folder (usually same as the name of the application).
     * @return A folder where you should save configs, saves, etc.
     * @throws java.io.IOException If folder did not exist, and could not be created.
     */
    public static File getAppDir(String name) throws IOException {
        String s1 = System.getProperty("user.home", ".");
        File file1;

        switch (OSUtil.getOs()) {
            case WINDOWS:
                String s2 = System.getenv("APPDATA");

                if (s2 != null) {
                    file1 = new File(s2, name + '/');
                } else {
                    file1 = new File(s1, name + '/');
                }

                break;
            case MACOS:
                file1 = new File(s1, "Library/Application Support/" + name);
                break;
            default:
                file1 = new File(s1, name + '/');
        }

        if (!file1.exists() && !file1.mkdirs()) {
            throw new IOException("The working directory could not be created: " + file1);
        } else {
            return file1;
        }
    }

    /**
     * Deletes a file, and if it is a folder, it deletes all its contents (including sub-folders), so that it can be deleted without problem
     *
     * @param f The file or folder to delete
     * @return the return value of {@link java.io.File#delete()}
     */
    public static boolean delete(File f) {
        if (f == null || !f.exists()) return true;
        if (f.isDirectory()) {
            File[] subFiles = f.listFiles();
            if (subFiles != null) {
                for (File sf : subFiles) {
                    IOUtil.delete(sf);
                }
            }
        }
        return f.delete();
    }
}
