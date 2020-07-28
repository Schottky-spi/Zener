package com.github.schottky.zener.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PersistentDataTypes {

    private PersistentDataTypes() {}

    public static final PersistentDataType<byte[],UUID> UUID = new PersistentDataType<byte[], java.util.UUID>() {

        @Override
        public @NotNull Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public @NotNull Class<java.util.UUID> getComplexType() {
            return java.util.UUID.class;
        }

        @Override
        public byte @NotNull [] toPrimitive(
                java.util.@NotNull UUID uuid,
                @NotNull PersistentDataAdapterContext persistentDataAdapterContext)
        {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
            byteBuffer.putLong(uuid.getMostSignificantBits());
            byteBuffer.putLong(uuid.getLeastSignificantBits());
            return byteBuffer.array();
        }

        @NotNull
        @Override
        public java.util.UUID fromPrimitive(
                byte @NotNull [] bytes,
                @NotNull PersistentDataAdapterContext persistentDataAdapterContext)
        {
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            long high = byteBuffer.getLong();
            long low = byteBuffer.getLong();
            return new UUID(high, low);
        }
    };

    public static final PersistentDataType<Byte,Boolean> BOOLEAN = new PersistentDataType<Byte, Boolean>() {
        @Override
        public @NotNull Class<Byte> getPrimitiveType() {
            return byte.class;
        }

        @Override
        public @NotNull Class<Boolean> getComplexType() {
            return boolean.class;
        }

        @NotNull
        @Override
        public Byte toPrimitive(
                @NotNull Boolean bool,
                @NotNull PersistentDataAdapterContext persistentDataAdapterContext)
        {
            return bool ? (byte) 1 : 0;
        }

        @NotNull
        @Override
        public Boolean fromPrimitive(
                @NotNull Byte aByte,
                @NotNull PersistentDataAdapterContext persistentDataAdapterContext)
        {
            return aByte != 0;
        }
    };

    public static class StringList extends AbstractList<String> {

        private final List<String> l;
        StringList(List<String> l)              {this.l = l;}

        public String get(int index)            {return l.get(index);}
        public int size()                       {return l.size();}
        public ArrayList<String> asArrayList()  {return new ArrayList<>(l);}
    }

    public static final PersistentDataType<byte[], StringList> STRING_LIST = new PersistentDataType<byte[], StringList>() {

        public @NotNull Class<byte[]> getPrimitiveType()    {return byte[].class;}
        public @NotNull Class<StringList> getComplexType()  {return StringList.class;}

        @Override
        public byte @NotNull [] toPrimitive(@NotNull StringList strings, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (String s: strings) {
                for (char c: s.toCharArray()) {
                    outputStream.write(c);
                }
                outputStream.write(0);
            }
            return outputStream.toByteArray();
        }

        @NotNull
        @Override
        public StringList fromPrimitive(byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            StringBuilder builder = new StringBuilder();
            final List<String> strings = new ArrayList<>();
            for (byte b: bytes) {
                if (b == 0) {
                    strings.add(builder.toString());
                    builder = new StringBuilder();
                } else {
                    builder.append((char) b);
                }
            }
            return new StringList(strings);
        }
    };

}