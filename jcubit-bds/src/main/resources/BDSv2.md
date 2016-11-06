# Binary Data Storage v2<a name="header"></a>

Binary Data Storage v2 (BDSv2) is a serialization format that allows serializing compound objects of any depth without loops (i.e. trees).

## Format<a name="format"></a>

A full serialized entity is called a [**BDS file**](#bds-file), which is a [BDS element](#bds-element) with some metadata. A BDS element is itself a compound object, made of primitive types, Strings, and other BDS elements.

Notes:

- Strings will be serialized in _UTF-8_ format.
- Numbers will be stored in big endian, that is, the first byte will be the most significant byte.
- All number types are of the standard java size: Integers 4 bytes, Shorts 2 bytes, and so on.

## BDS File<a name="bds-file"></a>

- Signature: "_.BDSv2\r\n_": `2E` `42` `44` `53` `76` `32` `0D` `0A`
- BDS Element

### BDS Element<a name="bds-element"></a>

- Length: Number of bytes from after this integer to the end of this BDS Element: Integer
- Zero or more _Elements_: [See below](#element-structure)

### Type signatures<a name="type-signatures"></a>

Each type storable in BDS has a signature of type Byte. These are the values:

| Type   | Value  |
| ------ | ------ |
| Byte   | `0x01` |
| Char   | `0x02` |
| Short  | `0x03` |
| Int    | `0x04` |
| Long   | `0x05` |
| Float  | `0x06` |
| Double | `0x07` |
| BDS    | `0x08` |
| String | `0x09` |

The signature of arrays of each type will be the result of *or*-ing the signature of the type, and `0x20` (e.g. Int arrays will be `0x04 | 0x20 = 0x24`)

### Element structure<a name="element-structure"></a>

- Type signature: [See above](#type-signatures)
- Serialized name of the element inside the parent BDS: [See below](#strings).
- Value: Variable size, depending on type. Java primitives have the same size as in Java, Strings are serialized as stated below. Nested BDSs are [BDS elements](#bds-element).

### Array structure<a name="element-structure"></a>

- Type signature [See above](#type-signature)
- Serialized name of the element inside the parent BDS: [See below](#strings)
- Number of elements in the array: Integer
- Zero or more values: Variable size, depending on type.

### Strings<a name="strings"></a>

- Length in bytes: Integer
- String in UTF-8
