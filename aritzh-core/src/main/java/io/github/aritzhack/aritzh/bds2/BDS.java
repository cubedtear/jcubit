package io.github.aritzhack.aritzh.bds2;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.github.aritzhack.aritzh.collections.CollectionUtil;
import io.github.aritzhack.aritzh.util.NotNull;
import io.github.aritzhack.aritzh.util.Set2;

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

	private final static byte[] SIGNATURE = ".BDS\r\n".getBytes(Charsets.UTF_8);
	private final static byte[] NEW_LINE = "\r\n".getBytes(Charsets.UTF_8);

	private String name;
	private Set<String> takenNames = Sets.newHashSet();
	private Map<String, String> strings = Maps.newHashMap();
	private Map<String, Integer> ints = Maps.newHashMap();
	private Map<String, Byte> bytes = Maps.newHashMap();
	private Map<String, Long> longs = Maps.newHashMap();
	private Map<String, Short> shorts = Maps.newHashMap();
	private Map<String, Float> floats = Maps.newHashMap();
	private Map<String, Double> doubles = Maps.newHashMap();
	private Map<String, BDS> bdss = Maps.newHashMap();

	private BDS(String name) {
		this.name = name;
	}

	public static BDS createEmpty(String name) {
		return new BDS(name);
	}

	public static BDS createEmpty() {
		return new BDS("");
	}

	public static BDS loadFromFile(File f) throws IOException {
		return BDS.load(com.google.common.io.Files.toByteArray(f));
	}

	public static BDS loadFromFile(Path p) throws IOException {
		return BDS.load(java.nio.file.Files.readAllBytes(p));
	}

	public static BDS load(byte[] data) {
		if (data.length < SIGNATURE.length + 2)
			throw new IllegalArgumentException("Data is too short, it is not in the correct format!");

		if (!CollectionUtil.equals(data, SIGNATURE, SIGNATURE.length))
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		return parseBDS(data, SIGNATURE.length).getT();
	}

	@NotNull
	private static Set2<BDS, Integer> parseBDS(byte[] data, int offset) {
		if (data[offset] != BDSType.BDS.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		BDS bds = BDS.createEmpty();

		Set2<String, Integer> name = parseString(data, offset+1);
		bds.setName(name.getT());

		int i = name.getU();

		A:
		while(i < data.length) {
			switch (BDSType.fromSignature(data[i])) {
				case BYTE:
					i = bds.parseByte(data, i);
					break;
				case SHORT:
					i = bds.parseShort(data, i);
					break;
				case INT:
					i = bds.parseInt(data, i);
					break;
				case LONG:
					i = bds.parseLong(data, i);
					break;
				case FLOAT:
					i = bds.parseFloat(data, i);
					break;
				case DOUBLE:
					i = bds.parseDouble(data, i);
					break;
				case STRING:
					i = bds.parseBDSString(data, i);
					break;
				case BDS:
					Set2<BDS, Integer> bds1 = parseBDS(data, i);
					bds.addBDS(bds1.getT());
					i = bds1.getU();
					break;
				case END:
					break A;
				default:
					throw new IllegalArgumentException("Unknown data type found, with byte: " + data[i]);
			}
		}
		return Set2.of(bds, i);
	}

	private static Set2<String, Integer> parseString(byte[] data, int offset) {
		byte top = data[offset++];
		byte bot = data[offset++];
		int length = top << 8 | bot;
		if(length == 0) return Set2.of("", offset);
		return Set2.of(new String(data, offset, length, Charsets.UTF_8), length + offset);
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

	private int parseByte(byte[] data, int offset) {
		if (data[offset] != BDSType.BYTE.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		Set2<String, Integer> name = parseString(data, offset+1);
		byte v = data[name.getU()];
		this.addByte(name.getT(), v);
		return name.getU();
	}

	private int parseShort(byte[] data, int offset) {
		if (data[offset] != BDSType.SHORT.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		Set2<String, Integer> name = parseString(data, offset+1);
		offset = name.getU();
		short v = (short) (data[offset++] << 8 | data[offset++]);
		this.addShort(name.getT(), v);
		return offset;
	}

	private int parseInt(byte[] data, int offset) {
		if (data[offset] != BDSType.INT.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		Set2<String, Integer> name = parseString(data, offset+1);
		offset = name.getU();
		int v = Ints.fromBytes(data[offset++], data[offset++], data[offset++], data[offset++]);
		this.addInt(name.getT(), v);
		return offset;
	}

	private int parseLong(byte[] data, int offset) {
		if (data[offset] != BDSType.LONG.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		Set2<String, Integer> name = parseString(data, offset+1);
		offset = name.getU();
		byte[] longData = new byte[8];
		System.arraycopy(data, offset, longData, 0, 8);
		long l = Longs.fromByteArray(longData);
		this.addLong(name.getT(), l);
		return offset + Longs.BYTES;
	}

	private int parseFloat(byte[] data, int offset) {
		if (data[offset] != BDSType.FLOAT.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		Set2<String, Integer> name = parseString(data, offset+1);
		offset = name.getU();
		float f = Float.intBitsToFloat(Ints.fromBytes(data[offset++], data[offset++], data[offset++], data[offset++]));
		this.addFloat(name.getT(), f);
		return offset;
	}

	private int parseDouble(byte[] data, int offset) {
		if (data[offset] != BDSType.DOUBLE.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		Set2<String, Integer> name = parseString(data, offset+1);
		offset = name.getU();

		byte[] longData = new byte[8];
		System.arraycopy(data, offset, longData, 0, 8);
		long l = Longs.fromByteArray(longData);
		double d = Double.longBitsToDouble(l);
		this.addDouble(name.getT(), d);
		return offset + Doubles.BYTES;
	}

	private int parseBDSString(byte[] data, int offset) {
		if (data[offset] != BDSType.STRING.signature)
			throw new IllegalArgumentException("Given data is not in the appropriate format!");

		Set2<String, Integer> name = parseString(data, offset+1);
		Set2<String, Integer> str = parseString(data, name.getU());
		this.addString(name.getT(), str.getT());
		return str.getU();
	}

	public void writeToFile(File f) {
		try (FileOutputStream fos = new FileOutputStream(f)) {
			fos.write(writeFinal());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] write() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		baos.write(BDSType.BDS.signature);

		addString(this.name, baos);

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

		for (Map.Entry<String, Float> flot : floats.entrySet()) {
			baos.write(BDSType.FLOAT.signature);
			addString(flot.getKey(), baos);
			int i = Float.floatToIntBits(flot.getValue());
			for (byte b : Ints.toByteArray(i)) baos.write(b);
		}

		for (Map.Entry<String, Double> doble : doubles.entrySet()) {
			baos.write(BDSType.DOUBLE.signature);
			addString(doble.getKey(), baos);
			long loong = Double.doubleToLongBits(doble.getValue());
			for (byte b : Longs.toByteArray(loong)) baos.write(b);
		}

		for (Map.Entry<String, String> str : strings.entrySet()) {
			baos.write(BDSType.STRING.signature);
			addString(str.getKey(), baos);
			addString(str.getValue(), baos);
		}

		for (Map.Entry<String, BDS> bds : bdss.entrySet()) {
			for (byte b : bds.getValue().write()) baos.write(b);
		}

		baos.write(BDSType.END.signature);

		return baos.toByteArray();
	}

	public byte[] writeFinal() {
		byte[] data = write();
		byte[] newData = new byte[data.length + SIGNATURE.length + NEW_LINE.length];
		System.arraycopy(SIGNATURE, 0, newData, 0, SIGNATURE.length);
		System.arraycopy(data, 0, newData, SIGNATURE.length, data.length);
		System.arraycopy(NEW_LINE, 0, newData, SIGNATURE.length + data.length, NEW_LINE.length);
		return newData;
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

	public boolean addFloat(String name, float value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		floats.put(name, value);
		return true;
	}

	public boolean addDouble(String name, double value) {
		if (takenNames.contains(name)) return false;
		takenNames.add(name);
		doubles.put(name, value);
		return true;
	}

	public boolean addBDS(BDS value) {
		if (takenNames.contains(value.name)) return false;
		takenNames.add(value.name);
		bdss.put(value.name, value);
		return true;
	}

	public Byte getByte(String name) {
		return this.bytes.get(name);
	}

	public Short getShort(String name) {
		return this.shorts.get(name);
	}

	public Integer getInt(String name) {
		return this.ints.get(name);
	}

	public Long getLong(String name) {
		return this.longs.get(name);
	}

	public Float getFloat(String name) {
		return this.floats.get(name);
	}

	public Double getDouble(String name) {
		return this.doubles.get(name);
	}

	public String getString(String name) {
		return this.strings.get(name);
	}

	public BDS getBDS(String name) {
		return this.bdss.get(name);
	}

	private enum BDSType {
		BYTE((byte) 1),
		SHORT((byte) 2),
		INT((byte) 3),
		LONG((byte) 4),
		FLOAT((byte) 5),
		DOUBLE((byte) 6),
		STRING((byte) 7),
		BDS((byte) 8),
		END((byte) 9),
		ERROR((byte) -1);

		private static final Map<Byte, BDSType> TYPE_MAP;

		static {
			Map<Byte, BDSType> types = Maps.newHashMap();
			for (BDSType t : BDSType.values()) {
				if (t != ERROR) types.put(t.signature, t);
			}
			TYPE_MAP = ImmutableMap.copyOf(types);
		}

		private final byte signature;

		BDSType(byte signature) {
			this.signature = signature;
		}

		private static BDSType fromSignature(byte signature) {
			return TYPE_MAP.containsKey(signature) ? TYPE_MAP.get(signature) : ERROR;
		}
	}
}
