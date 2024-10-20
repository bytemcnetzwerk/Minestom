package net.bytemc.minestom.bootstrap;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.velocity.VelocityProxy;

import java.util.List;
import java.util.Objects;

public class MinestomBootstrap {

    public static void main(String[] arguments) {
        MinecraftServer server = MinecraftServer.init();
        var args = List.of(arguments);

        if (args.contains("--velocity") || System.getenv("forwarding_secret") != null) {
            String forwardingSecret;

            if (args.contains("--velocity")) {
                forwardingSecret = "";

                for (int i = 0; i < args.size(); i++) {
                    if (args.get(i).equals("--velocity")) {
                        forwardingSecret = args.get(i + 1);
                    }
                }
            } else {
                forwardingSecret = System.getenv("forwarding_secret");
            }

            VelocityProxy.enable(forwardingSecret);
            MinecraftServer.LOGGER.info("Velocity will be enabled...");
        } else {
            if (args.contains("--disableMojangAuth")) {
                MinecraftServer.LOGGER.info("Disable MojangAuth...");
            } else {
                MojangAuth.init();
            }
        }

        var host = "0.0.0.0";
        var port = 25565;

        if (System.getenv("hostname") != null) {
            host = System.getenv("hostname");
        } else if (args.contains("--host")) {
            for (int i = 0; i < args.size(); i++) {
                if (Objects.equals(args.get(i), "--host")) {
                    host = args.get(i + 1);
                }
            }
        }

        if (System.getenv("port") != null) {
            port = Integer.parseInt(System.getenv("port"));
        } else if (args.contains("--port")) {
            for (int i = 0; i < args.size(); i++) {
                if (Objects.equals(args.get(i), "--port")) {
                    port = Integer.parseInt(args.get(i + 1));
                }
            }
        }

        server.start(host, port);
        MinecraftServer.LOGGER.info("Minestom server was started on {}:{}", host, port);
    }

}
