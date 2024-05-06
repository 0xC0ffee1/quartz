package net.c0ffee1.quartz.api.packet;

public interface PacketReceiver<T> {
    void sendPacket(T type);
}
