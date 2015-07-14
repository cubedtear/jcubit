# BDS Data Structure

## BDS File

A **BDS** file will contain as the first 6 bytes the following string, encoded in *UTF-8*: **`.BDS\r\n`**. In practice, then, the first 6 bytes will be: `46 66 68 83 13 10`, or, in hex, `0x2E 0x42 0x44 0x53 0x0D 0x0A`. This will be the file signature. It is made this way so that if a BDS file is opened in a text editor, `.BDS` will stand on its own line, even though the rest will be binary data, and the format will be able to be recognised.

This signature will be followed by a [**BDS** section](#bds), explained in detail below.

Finally, the file will have a final `\r\n` added to it (bytes `13 10` or `0x0D 0x0A`), in order to have a blank line at the end of the file.

## String encoding

Encoded strings (do not mistake them with string sections. This is just how any and all string will be included in the binary file, including, but not limited to string sections) have the following format: First two bytes make up a short, determining the length of the following string. The first byte will be the most significant bits, and the second the least ones. Then the string will be followed, encoded in *UTF-8*. 

## Sections

Each section will have the following structure: First, there will be the section signature, made up of **one byte**, followed by the name of that section ([See section strings](#string-encoding)), and the content of that section.

As of now, there are 8 different sections:

- [Byte](#Byte)
- [Short](#Short)
- [Integer](#Integer)
- [Long](#Long)
- [Float](#Float)
- [Double](#Double)
- [String](#String)
- [BDS](#BDS)


Let's see now how the content of each of them is encoded.

### Byte

Since it is always a single byte, the value will be the only content.

### Short

Shorts take up two bytes, so the content will be made up of two bytes: the first one will contain the most significant bits, and the second the least ones.

###  Integer

Integers take up four bytes, so, following the same logic, it will take up four bytes and will be ordered in big endian: first byte will be 32-25 bits, second 24-17, third 16-9, and last 8-1.

### Long

Longs take up eight bytes, and will be stored in big endian order.

### Float

Floats take up four bytes, and will be stored in big endian order. So, first bit of the first byte will be the sign, followed by part of the exponent, and then the rest of the exponent, and part of the mantissa, and the rest of the mantissa split between the last two bytes.

For example, if we have `0.15625`, it's binary representation will be:

| Sign | Exponent | Mantissa |
|:---:|:---:|:---:|
| **`0`** | `0  1  1  1  1  1  0  0` | **`0  1  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0`** |

And the byte partitioning will be as follows:

| First byte | Second byte | Third byte | Fourth byte |
|:---:|:---:|:---:|:---:|
| **`0`** `0  1  1  1  1  1  0` | `0` **`0  1  0  0  0  0  0`** | **`0  0  0  0  0  0  0  0`** | **`0  0  0  0  0  0  0  0`** |

### Double

Doubles take up eight bytes, and will be stored following the same logic as the floats (i.e. their binary representation split into eight bytes, in big endian order).

### String

String take up a variable length, depending on the string. As the content, they will have a string in [the format we already know](#string-encoding). Therefore, the whole string section will be made up of two strings: the section name, and the value.

### BDS

A *BDS* section is a special one. It is the only one than may have subsections. Moreover, it can have a variable number of them. Therefore, after its signature and name, as the content it will contain several other section (maybe even other BDS sections, or no sections at all), and will be finished by a special signature, called the ***END*** signature (like other section signatures it will be one byte too, and will be where any other section signature could be).

## Example

To make things easy to understand, lets try to translate the following JSON into BDS:

```yaml
{
	"Main":
	{
		"floatTest": (float) 0.25,
		"stringTest": "Hello, world!",
		"bdsTest":
		{
			"byteTest": (byte) 5,
			"intTest": 25688
		}
	}
}
```

>Note: the `(float)` and `(byte)` tags are just to represent the type of the values, and not mistake them 
with doubles or ints.

For this example we will suppose the sections signatures are the following (these numbers might change since the format is still in development):

| Section | Signature |
|---------|-----------|
| Byte    | 1         |
| Short   | 2         |
| Int     | 3         |
| Long    | 4         |
| Float   | 5         |
| Double  | 6         |
| String  | 7         |
| BDS     | 8         |
| END     | 9         |

First, we can see the topmost section is the `Main` section, so that will be the name of our main BDS section.
Inside of it we have a float and a string, followed by a nested BDS section containing a byte and an int.

This will be the code we use to generate the output:

```java
BDS bds2 = BDS.loadFromFile(f);
assertEquals(value, bds2.getBDS("C").getDouble("A"), 0.0d);

BDS test = BDS.createEmpty("Main");
test.addFloat("floatTest", 0.25f);
test.addString("stringTest", "Hello, World!");

BDS nested2 = BDS.createEmpty("bdsTest");
test.addBDS(nested2);
nested2.addByte("byteTest", (byte) 5);
nested2.addInt("intTest", 25688);
byte[] dataa = test.writeFinal();
```

And this will be the generated output:

|      |      |      |      |      |      |      |      |
|------|------|------|------|------|------|------|------|
| 0x2E | 0x42 | 0x44 | 0x53 | 0x0D | 0x0A | 0x08 | 0x00 |
| 0x04 | 0x4D | 0x61 | 0x69 | 0x6E | 0x05 | 0x00 | 0x09 |
| 0x66 | 0x6C | 0x6F | 0x61 | 0x74 | 0x54 | 0x65 | 0x73 |
| 0x74 | 0x3E | 0x80 | 0x00 | 0x00 | 0x07 | 0x00 | 0x0A |
| 0x73 | 0x74 | 0x72 | 0x69 | 0x6E | 0x67 | 0x54 | 0x65 |
| 0x73 | 0x74 | 0x00 | 0x0D | 0x48 | 0x65 | 0x6C | 0x6C |
| 0x6F | 0x2C | 0x20 | 0x57 | 0x6F | 0x72 | 0x6C | 0x64 |
| 0x21 | 0x08 | 0x00 | 0x07 | 0x62 | 0x64 | 0x73 | 0x54 |
| 0x65 | 0x73 | 0x74 | 0x01 | 0x00 | 0x08 | 0x62 | 0x79 |
| 0x74 | 0x65 | 0x54 | 0x65 | 0x73 | 0x74 | 0x05 | 0x03 |
| 0x00 | 0x07 | 0x69 | 0x6E | 0x74 | 0x54 | 0x65 | 0x73 |
| 0x74 | 0x00 | 0x00 | 0x64 | 0x58 | 0x09 | 0x09 | 0x0D |
| 0x0A |


Let's break it down and see what each one means.

The first six bytes (`0x2E 0x42 0x44 0x53 0x0D 0x0A`) are the file signature, and the ASCII equivalent of `.BDS\r\n`, where the `\r\n` is the carriage return followed by a new line.

The next byte (`0x08`) is the BDS section signature. Therefore, the following must be the name of the section.
The next two bytes (`0x00 0x04`) are the length of the string, in this case 4 bytes. The next four bytes must then be the name, encoded in UTF-8. The bytes `0x4D 0x61 0x69 0x6E` correspond to the *Main* string in UTF-8.

Next we must have another section signature. In this case we have the byte `0x05`, so the section must be a float. The same way as with the previous section, we see that the name has a length of 9 bytes, and that decoded it is "floatTest". It is followed by 4 bytes that represent the float `0.25`.

Next section will be a string, because of the section signature byte `0x07`. The first string is the name: "stringTest" and the second one the value: "Hello, World!".

Next we find a BDS section signature (`0x08`), so we must be going inside of a nested section. After the section name ("bdsTest") we find a byte named "byteTest" with value 5, and an int named "intTest" with value 25688. 

After those we find a END signature (`0x09`), meaning we no longer are in the nested BDS, and we are back in the main one (if we were nested several levels below the main one, we would only go up one level).

Then we find another END signature, meaning we have finished reading the file. After that, the two remaining bytes are the "\r\n" line-break.

This is how it would be broken down:

```java
0x2E 0x42 0x44 0x53 0xD 0xA // The file signatue: ".BDS\r\n"

0x8 // Main BDS section
	0x0 0x4 // The Name length: 4 bytes
	0x4D 0x61 0x69 0x6E // Name: "Main", encoded in UTF-8

	0x5 // Float section
		0x0	0x9 // Name length: 9 bytes
		0x66 0x6C 0x6F 0x61 0x74 0x54 0x65 0x73 0x74 // Name: "floatTest"
		0x3E 0x80 0x0 0x0 // Value: 0.25f
		
	0x7 // String section
		0x0 0xA // Name length: 10 bytes
		0x73 0x74 0x72 0x69 0x6E 0x67 0x54 0x65 0x73 0x74 // Name: "stringTest"
		0x0 0xD // Value length: 13 bytes
		0x48 0x65 0x6C 0x6C 0x6F 0x2C 0x20 0x57 0x6F 0x72 0x6C 0x64 0x21 // Value: "Hello, World!"
		
	0x8 // Nested BDS section
		0x0 0x7 // Name length: 7 bytes
		0x62 0x64 0x73 0x54 0x65 0x73 0x74 // Name: "bdsTest"
		
		0x1 // Byte section
			0x0 0x8 // Name length: 8 bytes
			0x62 0x79 0x74 0x65 0x54 0x65 0x73 0x74 // Name: "byteTest"
			0x5 // Value: 5
			
		0x3 // Int section
			0x0 0x7 // Name length: 7 bytes
			0x69 0x6E 0x74 0x54 0x65 0x73 0x74 // Name: "intTest"
			0x0 0x0 0x64 0x58 // Value: 25688
		0x9 // END signature
0x9 // END signature

0xD 0xA // "\r\n"
```