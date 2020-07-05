package com.github.schottky.zener.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
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
}