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

package io.github.cubedtear.jcubit.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.swing.*;
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

    private static final FileFilter acceptAll = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return true;
        }
    };

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
     * @return The chosen file, or {@code null} if none was chosen.
     */
    @Nullable
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
     * Open a {@link javax.swing.JFileChooser}, and return the selected file, or null if closed or cancelled.
     *
     * @param title      The title of the file-chooser window.
     * @param currentDir The root directory. If null, {@code new File(".")} will be used.
     * @param filter     The file filter to use.
     * @return The chosen file, or {@code null} if none was chosen.
     */
    @Nullable
    public static File chooseFile(String title, File currentDir, javax.swing.filechooser.FileFilter filter) {
        if (currentDir == null) currentDir = new File(".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDir);
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(filter);
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
     * @return The chosen file(s), or {@code null} if none was chosen.
     */
    @Nullable
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
     * Open a {@link javax.swing.JFileChooser} with multi-selection enabled, and return the selected file(s), or null if closed or cancelled.
     *
     * @param title      The title of the file-chooser window.
     * @param currentDir The root directory. If null, {@code new File(".")} will be used.
     * @param filter     The file filter to use.
     * @return The chosen file(s), or {@code null} if none was chosen.
     */
    @Nullable
    public static File[] chooseFiles(String title, File currentDir, javax.swing.filechooser.FileFilter filter) {
        if (currentDir == null) currentDir = new File(".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDir);
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(filter);
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
     * @return The chosen folder, or {@code null} if none was chosen.
     */
    @Nullable
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
     * Open a {@link javax.swing.JFileChooser} which can be used to save a new file, and return the path to the new file, or null if closed or cancelled.
     *
     * @param title      The title of the file-chooser window.
     * @param currentDir The root directory. If null, {@code new File(".")} will be used.
     * @return The new file, or {@code null} if none was chosen.
     */
    public static File saveFile(String title, File currentDir) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDir);
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Open a {@link javax.swing.JFileChooser} which can be used to save a new file, and return the path to the new file, or null if closed or cancelled.
     *
     * @param title      The title of the file-chooser window.
     * @param currentDir The root directory. If null, {@code new File(".")} will be used.
     * @param filter     The file filter to use.
     * @return The new file, or {@code null} if none was chosen.
     */
    public static File saveFile(String title, File currentDir, javax.swing.filechooser.FileFilter filter) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDir);
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
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
