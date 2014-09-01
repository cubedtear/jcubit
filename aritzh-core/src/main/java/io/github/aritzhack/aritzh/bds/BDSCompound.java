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

package io.github.aritzhack.aritzh.bds;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Special BDS that can store different BDSs inside of it
 *
 * @author Aritz Lopez
 */
@SuppressWarnings("UnusedDeclaration")
public class BDSCompound extends BDS {

    private final List<BDS> items = new ArrayList<>();

    /**
     * Creates an empty BDSCompound
     *
     * @param name The name of this BDS
     */
    public BDSCompound(String name) {
        this.name = name;
    }

    /**
     * Reads the BDSCompound from a file
     *
     * @param file The file to read from
     */
    public BDSCompound(File file) throws IOException {
        this(Files.toByteArray(file));
    }

    /**
     * Parses a BDSCompound from a byte array
     *
     * @param data The byte array to parse this BDSCompound from
     */
    public BDSCompound(byte[] data) {
        this(data, true);
    }

    private BDSCompound(byte[] data, boolean compressed) {
        data = (compressed ? BDSCompound.decompress(data) : data);
        this.parse(ByteStreams.newDataInput(data), true);
    }

    public static byte[] decompress(byte[] data) {
        try {
            GZIPInputStream stream = new GZIPInputStream(new ByteArrayInputStream(data));
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nRead;
            byte[] buffer = new byte[16384];
            while ((nRead = stream.read(buffer, 0, buffer.length)) != -1) {
                byteStream.write(buffer, 0, nRead);
            }
            byteStream.flush();
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not decompress");
        }
    }

    private void parse(ByteArrayDataInput input, boolean withType) {
        if (input == null) {
            this.name = "";
            return;
        }
        try {
            if (withType) input.readByte();
            this.name = input.readUTF();

            for (byte typeB = input.readByte(); typeB < BDSType.values().length && typeB >= 0; typeB = input.readByte()) {
                BDSType type = BDSType.values()[typeB];
                switch (type) {
                    case BDS_COMPEND:
                        return;
                    case BDS_BYTE:
                        this.items.add(new BDSByte(input));
                        break;
                    case BDS_COMPOUND:
                        this.items.add(new BDSCompound(input, false));
                        break;
                    case BDS_INT:
                        this.items.add(new BDSInt(input));
                        break;
                    case BDS_SHORT:
                        this.items.add(new BDSShort(input));
                        break;
                    case BDS_STRING:
                        this.items.add(new BDSString(input));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown type byte in BDSCompound: " + type);
                }

            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Could not parse BDSCompound", e);
        }
    }

    /**
     * Constructs a BDS with the single specified BDS inside
     *
     * @param item The BDS this BDSCompound will contain
     * @param name The name of this BDS
     */
    public BDSCompound(BDS item, String name) {
        this(new BDS[]{item}, name);
    }

    /**
     * Constructs a BDSCompound with the BDSs inside of the array
     *
     * @param items The array of BDSs to construct this with
     */
    public BDSCompound(BDS[] items, String name) {
        this(Arrays.asList(items), name);
    }

    /**
     * Constructs a BDSCompound with specified BDSs
     *
     * @param items The list of BDSs to construct this with
     * @param name  The name of this BDS
     */
    public BDSCompound(List<BDS> items, String name) {
        this.items.addAll(items);
        this.name = name;
    }

    private BDSCompound(ByteArrayDataInput input, boolean withType) {
        this.parse(input, withType);
    }

    public static byte[] compress(byte[] data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            gzos.write(data);
            gzos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Getters for each type

    /**
     * Add an element to this BDS
     *
     * @param bds The element to be added
     * @return {@code this}. Eases builder pattern
     */
    public BDSCompound add(BDS bds) {
        checkArgument(bds != this, "Cannot add itself as a sub-element!");
        this.items.add(bds);
        return this;
    }

    /**
     * @param coll Adds all elements in the collection to this BDS
     * @return {@code this}. Eases builder pattern
     */
    public BDSCompound addAll(Collection<? extends BDS> coll) {
        this.items.addAll(coll);
        return this;
    }

    /**
     * Returns the {@code offset}'th BDSString from the list
     *
     * @param offset The number of BDSStrings to skip
     * @return The {@code offset}'th BDSString from the list
     */
    public BDSString getString(String name, int offset) {
        return this.getAllStrings().get(offset);
    }

    /**
     * Returns a list with all {@link BDSString}s inside this Compound
     *
     * @return a list with all {@link BDSString}s inside this Compound
     */
    public List<BDSString> getAllStrings() {
        List<BDSString> ret = Lists.newArrayList();
        for (BDS b : this.items) {
            if (b instanceof BDSString) ret.add((BDSString) b);
        }
        return ret;
    }

    /**
     * Returns the {@code offset}'th BDSByte from the list
     *
     * @param offset The number of BDSBytes to skip
     * @return The {@code offset}'th BDSByte from the list
     */
    public BDSByte getByte(String name, int offset) {
        return this.getAllBytes().get(offset);
    }

    /**
     * Returns a list with all {@link BDSByte}s inside this Compound
     *
     * @return a list with all {@link BDSByte}s inside this Compound
     */
    public List<BDSByte> getAllBytes() {
        List<BDSByte> ret = Lists.newArrayList();
        for (BDS b : this.items) {
            if (b instanceof BDSByte) ret.add((BDSByte) b);
        }
        return ret;
    }

    /**
     * Returns the {@code offset}'th BDSShort from the list
     *
     * @param offset The number of BDSShorts to skip
     * @return The {@code offset}'th BDSShort from the list
     */
    public BDSShort getShort(String name, int offset) {
        return this.getAllShorts().get(offset);
    }

    /**
     * Returns a list with all {@link BDSShort}s inside this Compound
     *
     * @return a list with all {@link BDSShort}s inside this Compound
     */
    public List<BDSShort> getAllShorts() {
        List<BDSShort> ret = Lists.newArrayList();
        for (BDS b : this.items) {
            if (b instanceof BDSShort) ret.add((BDSShort) b);
        }
        return ret;
    }

    /**
     * Returns the {@code offset}'th BDSInt from the list
     *
     * @param offset The number of BDSInts to skip
     * @return The {@code offset}'th BDSInt from the list
     */
    public BDSInt getInt(String name, int offset) {
        return this.getAllInts().get(offset);
    }

    /**
     * Returns a list with all {@link BDSInt}s inside this Compound
     *
     * @return a list with all {@link BDSInt}s inside this Compound
     */
    public List<BDSInt> getAllInts() {
        List<BDSInt> ret = Lists.newArrayList();
        for (BDS b : this.items) {
            if (b instanceof BDSInt) ret.add((BDSInt) b);
        }
        return ret;
    }

    /**
     * Returns the {@code offset}th BDSCompound from the list
     *
     * @param offset The number of BDSCompounds to skip
     * @return The {@code offset}th BDSCompound from the list
     */
    public BDSCompound getComp(String name, int offset) {
        return this.getAllCompounds().get(offset);
    }

    /**
     * Returns a list with all {@link BDSCompound}s inside this Compound
     *
     * @return a list with all {@link BDSCompound}s inside this Compound
     */
    public List<BDSCompound> getAllCompounds() {
        List<BDSCompound> ret = Lists.newArrayList();
        for (BDS b : this.items) {
            if (b instanceof BDSCompound) ret.add((BDSCompound) b);
        }
        return ret;
    }

    /**
     * Remove the element from the list
     *
     * @param bds The element to be removed
     * @return {@code true} if this BDSCompound contained the element
     */
    public boolean remove(BDS bds) {
        return this.items.remove(bds);
    }

    private byte[] getUncompressedBytes() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeByte(this.getType().toByte());
        output.writeUTF(this.name);
        for (BDS bds : this.items) {
            if (bds instanceof BDSCompound) {
                output.write(((BDSCompound) bds).getUncompressedBytes());
            } else output.write(bds.getBytes());
        }
        output.write(new BDSCompEnd().getBytes());
        return output.toByteArray();
    }

    /**
     * Stores the data from this BDSCompound into a byte array, so that
     * it can be easy and efficiently saved. Gzip is used to compress the data
     *
     * @return The byte array identifying this BDS
     * @see #getUncompressedBytes() Uncompressed counterpart
     */
    public byte[] getBytes() {
        return compress(this.getUncompressedBytes());
    }

    /**
     * Writes this BDSCompound to the specified file
     *
     * @param f The file to write this Compound to
     * @throws java.io.IOException Caused by opening the file or writing to it
     */
    public void writeToFile(File f) throws IOException {
        Preconditions.checkArgument(f != null, "File cannot be null");
        if (!f.exists())
            if (!f.createNewFile()) {
                throw new IOException("Could not write BDSCompound to file " + f.getAbsolutePath());
            }
        try (FileOutputStream fos = new FileOutputStream(f)) {
            this.writeToStream(fos);
        }
    }

    /**
     * Write this BDSCompound to an {@link java.io.OutputStream}
     *
     * @param s The stream this Compound will be written to.
     */
    public void writeToStream(OutputStream s) throws IOException {
        Preconditions.checkArgument(s != null, "Stream cannot be null");
        s.write(this.getBytes());
    }

    public BDSCompound copyOf(BDSCompound comp) {
        return new BDSCompound(comp.getBytes());
    }

    private String pretty(final int level) {
        StringBuilder builder = new StringBuilder();
        int cLevel = level;

        builder.append(Strings.repeat(" ", cLevel * 4))
            .append("[")
            .append(this.getType())
            .append(":")
            .append(this.getName())
            .append("]\n")
            .append(Strings.repeat(" ", cLevel * 4))
            .append("{");
        cLevel++;
        boolean some = false;
        for (BDS b : this.items) {
            some = true;
            if (b instanceof BDSCompEnd) continue;
            if (b instanceof BDSCompound) {
                builder.append("\n").append(((BDSCompound) b).pretty(cLevel));
                continue;
            }
            builder.append("\n").append(Strings.repeat(" ", cLevel * 4)).append(b);
        }
        if (!some) builder.append("\n").append(Strings.repeat(" ", cLevel * 4)).append("[EMPTY COMPOUND]");

        cLevel--;
        builder.append("\n").append(Strings.repeat(" ", cLevel * 4)).append("}");
        return builder.toString();
    }

    private static class BDSCompEnd extends BDS {

        public BDSCompEnd() {
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{this.getType().toByte()};
        }

        @Override
        public BDSType getType() {
            return BDSType.BDS_COMPEND;
        }

        @Override
        public Object getData() {
            return null;
        }
    }

    @Override
    public BDSType getType() {
        return BDSType.BDS_COMPOUND;
    }


    /**
     * Returns a multi-line string representing the tree structure of this Compound
     *
     * @return a multi-line string representing the tree structure of this Compound
     */
    @Override
    public String toString() {
        return this.pretty(0);
    }


    @Override
    public Object getData() {
        return this.items;
    }


}
