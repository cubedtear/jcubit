# Binary Data Storage v2

Binary Data Storage v2 (BDSv2) is a serialization format that allows serializing compound objects of any depth without loops (i.e. trees).

## Format

A full serialized entity is called a **BDS file**,
which is a BDS structure with some metadata. A BDS structure is itself a compound object, made of primitive types, Strings, and other BDSs.

The format specified onwards is given with a description of each value, together with the values, specified in binary.

In addition, all strings will be serialized in _UTF-8_ format, including the signatures, and all
numbers will be stored in big endian, that is, the first byte will be the most significant byte.

Lastly, all number types are of the standard java size: Integers 4 bytes, Shorts 2 bytes, and so on.

## BDS File

- Signature: "_.BDSv2\r\n_": `2E` `42` `44` `53` `76` `32` `0D` `0A`
- BDS type signature: See below
- BDS Element

### BDS Element

- Length: Number of bytes from after this integer to the end of the file: Integer
- Zero or more _Elements_: See below

### Type signatures

Each type storable in BDS has a signature of type
Byte. These are the values:

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

### Element structure

- Type signature: See above
- Serialized name of the element inside the parent BDS: See below.
- Value: Variable size, depending on type. Java primitives have the same size as in Java, Strings are serialized as stated below. Nested BDSs are BDS elements.

### Strings

- Length in bytes: Integer
- String in UTF-8
