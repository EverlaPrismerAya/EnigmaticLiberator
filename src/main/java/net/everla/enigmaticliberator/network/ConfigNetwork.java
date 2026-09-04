package net.everla.enigmaticliberator.network;

import net.everla.enigmaticliberator.EnigmaticLiberator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ConfigNetwork {
    private static final String PROTOCOL = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            EnigmaticLiberator.id("config"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
    private static int packetId;

    private ConfigNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(packetId++, ConfigRequestPacket.class, ConfigRequestPacket::encode,
                ConfigRequestPacket::decode, ConfigRequestPacket::handle);
        CHANNEL.registerMessage(packetId++, ConfigSyncPacket.class, ConfigSyncPacket::encode,
                ConfigSyncPacket::decode, ConfigSyncPacket::handle);
    }

    public static void request(String key, String value) {
        CHANNEL.sendToServer(new ConfigRequestPacket(key, value));
    }

    public static boolean updateFromScreen(Object configValue, String value, boolean multiplayer) {
        String key = ConfigValues.keyFor(configValue);
        if (key == null) {
            return false;
        }
        if (multiplayer) {
            request(key, value);
        } else {
            ConfigValues.apply(key, value);
            saveConfigs();
        }
        return true;
    }

    public static void sync(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ConfigSyncPacket(ConfigValues.snapshot()));
    }

    public static void syncAll() {
        CHANNEL.send(PacketDistributor.ALL.noArg(), new ConfigSyncPacket(ConfigValues.snapshot()));
    }

    private record ConfigRequestPacket(String key, String value) {
        private static void encode(ConfigRequestPacket packet, FriendlyByteBuf buffer) {
            buffer.writeUtf(packet.key, 128);
            buffer.writeUtf(packet.value, 32);
        }

        private static ConfigRequestPacket decode(FriendlyByteBuf buffer) {
            return new ConfigRequestPacket(buffer.readUtf(128), buffer.readUtf(32));
        }

        private static void handle(ConfigRequestPacket packet, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player != null && player.hasPermissions(2) && ConfigValues.apply(packet.key, packet.value)) {
                    saveConfigs();
                    ConfigNetwork.syncAll();
                }
            });
            context.setPacketHandled(true);
        }
    }

    private record ConfigSyncPacket(Map<String, String> values) {
        private static void encode(ConfigSyncPacket packet, FriendlyByteBuf buffer) {
            buffer.writeVarInt(packet.values.size());
            packet.values.forEach((key, value) -> {
                buffer.writeUtf(key, 128);
                buffer.writeUtf(value, 32);
            });
        }

        private static ConfigSyncPacket decode(FriendlyByteBuf buffer) {
            Map<String, String> values = new HashMap<>();
            int count = buffer.readVarInt();
            for (int i = 0; i < count; i++) {
                values.put(buffer.readUtf(128), buffer.readUtf(32));
            }
            return new ConfigSyncPacket(values);
        }

        private static void handle(ConfigSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                packet.values.forEach(ConfigValues::apply);
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        refreshClientScreen());
            });
            context.setPacketHandled(true);
        }

        private static void refreshClientScreen() {
            net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
            if (minecraft.screen instanceof net.everla.enigmaticliberator.gui.ConfigScreen screen) {
                screen.refreshFromNetwork();
            }
        }
    }

    private static void saveConfigs() {
        // Forge owns the config files; saving each spec persists the server-side change.
        net.minecraftforge.common.ForgeConfigSpec[] specs = {
                net.everla.enigmaticliberator.config.CurseConfig.SPEC,
                net.everla.enigmaticliberator.config.BlessingConfig.SPEC,
                net.everla.enigmaticliberator.config.ExtraConfig.SPEC
        };
        for (net.minecraftforge.common.ForgeConfigSpec spec : specs) {
            spec.save();
        }
    }
}
