package io.github.aritzhack.aritzh.bds2;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
@SuppressWarnings("unused")
public class BDS {

	private final byte[] SIGNATURE = ".BDS\r\n".getBytes(Charsets.UTF_8);
	private Set<String> takenNames = Sets.newHashSet();
	private Map<String, String> strings = Maps.newHashMap();
	private Map<String, Integer> ints = Maps.newHashMap();
	private Map<String, Byte> bytes = Maps.newHashMap();
	private Map<String, Long> longs = Maps.newHashMap();
	private Map<String, Short> shorts = Maps.newHashMap();
	private Map<String, BDS> bdss = Maps.newHashMap();

	private BDS() {
	}

	public static BDS createEmpty() {
		return new BDS();
	}

	public static BDS loadFromFile(File f) throws IOException {
		return BDS.load(com.google.common.io.Files.toByteArray(f));
	}

	public static BDS loadFromFile(Path p) throws IOException {
		return BDS.load(java.nio.file.Files.readAllBytes(p));
	}

	public static BDS load(byte[] data) {
		// TODO Implement
		return null;
	}

	private static void addString(String nameStr, ByteArrayOutputStream baos) {
		byte[] name = nameStr.getBytes(Charsets.UTF_8);
		if (name.length > Math.pow(2, 9)) {
			throw new ArrayIndexOutOfBoundsException("Tag name is too long!");
		}
		baos.write((byte) (name.length >> 8));
		baos.write((byte) name.length);
		for (byte b : name) baos.write(b);
	}

	public void writeToFile(File f) {
		try (FileOutputStream fos = new FileOutputStream(f)) {
			fos.write(SIGNATURE);
			fos.write(write());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] write() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		baos.write(BDSType.BDS.signature);

		for (Map.Entry<String, Byte> bite : bytes.entrySet()) {
			baos.write(BDSType.BYTE.signature);
			addString(bite.getKey(), baos);
			baos.write(bite.getValue());
		}

		for (Map.Entry<String, Short> sort : shorts.entrySet()) {
			baos.write(BDSType.SHORT.signature);
			addString(sort.getKey(), baos);
			short s = sort.getValue();
			baos.write((byte) (s >> 8));
			baos.write((byte) s);
		}

		for (Map.Entry<String, Integer> integer : ints.entrySet()) {
			baos.write(BDSType.INT.signature);
			addString(integer.getKey(), baos);
			for (byte b : Ints.toByteArray(integer.getValue())) baos.write(b);
		}

		for (Map.Entry<String, Long> loong : longs.entrySet()) {
			baos.write(BDSType.LONG.signature);
			addString(loong.getKey(), baos);
			for (byte b : Longs.toByteArray(loong.getValue())) baos.write(b);
		}

		for (Map.Entry<String, String> str : strings.entrySet()) {
			baos.write(BDSType.STRING.signature);
			addString(str.getKey(), baos);
			addString(str.getValue(), baos);
		}

		for (Map.Entry<String, BDS> bds : bdss.entrySet()) {
			baos.write(BDSType.BDS.signature);
			addString(bds.getKey(), baos);
			for (byte b : bds.getValue().write()) baos.write(b);
		}

		baos.write(BDSType.END.signature);

		return baos.toByteArray();
	}

	public boolean addString(String name, String value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		strings.put(name, value);
		return true;
	}

	public boolean addByte(String name, byte value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		bytes.put(name, value);
		return true;
	}

	public boolean addInt(String name, int value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		ints.put(name, value);
		return true;
	}

	public boolean addShort(String name, short value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		shorts.put(name, value);
		return true;
	}

	public boolean addLong(String name, long value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		longs.put(name, value);
		return true;
	}

	public boolean addBDS(String name, BDS value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		bdss.put(name, value);
		return true;
	}

	private enum BDSType {
		BYTE((byte) 1),
		STRING((byte) 2),
		INT((byte) 3),
		LONG((byte) 4),
		SHORT((byte) 5),
		BDS((byte) 6),
		END((byte) 7);

		private static final Map<Byte, BDSType> TYPE_MAP;

		static {
			Map<Byte, BDSType> types = Maps.newHashMap();
			for (BDSType t : BDSType.values()) {
				types.put(t.signature, t);
			}
			TYPE_MAP = ImmutableMap.copyOf(types);
		}

		private final byte signature;

		BDSType(byte signature) {
			this.signature = signature;
		}
	}
}
