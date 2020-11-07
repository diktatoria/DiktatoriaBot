package io.github.diktatoria.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Utils {
    public static boolean hasRole(User user, long id, Server server) {
        AtomicBoolean b = new AtomicBoolean(false);
        user.getRoles(server).forEach(role -> {
            if (role.getId() == id)
                b.set(true);
        });
        return b.get();
    }

    public static boolean hasRole(long userID, long roleID, Server server) {
        try {
            User user = server.getApi().getUserById(userID).get();
            AtomicBoolean b = new AtomicBoolean(false);
            user.getRoles(server).forEach(role -> {
                if (role.getId() == roleID)
                    b.set(true);
            });
            return b.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean hasAllRoles(User user, long[] rules, Server server) {
        List<Boolean> list = new ArrayList<>();
        for (long l : rules)
            list.add(hasRole(user, l, server));
        boolean ret = true;
        for (Boolean b : list)
            if (!b) {
                ret = false;
                break;
            }
        return ret;
    }

    public static boolean hasOneRole(User user, long[] rules, Server server) {
        List<Boolean> list = new ArrayList<>();
        for (long l : rules)
            list.add(hasRole(user, l, server));
        boolean ret = false;
        for (Boolean b : list)
            if (b) {
                ret = true;
                break;
            }
        return ret;
    }


    public static class ShutdownHook extends Thread {
        public DiscordApi api;

        public ShutdownHook(DiscordApi api) {
            this.api = api;
        }

        @Override
        public void run() {
            api.disconnect();
        }
    }
}
