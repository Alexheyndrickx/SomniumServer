package com.osroyale.util.tools;

import com.jcabi.jdbc.JdbcSession;
import com.osroyale.Config;
import com.osroyale.game.world.items.ItemDefinition;

import java.sql.Connection;
import java.sql.DriverManager;

public class ItemDBdefUpdate {

    public static void main(String[] args) {
        String query = "INSERT INTO lookup.item_lookup(id, name, value) VALUES (?, ?, ?) ON CONFLICT (id) DO UPDATE SET id = excluded.id, name = excluded.name, value = excluded.value";

        ItemDefinition.createParser().run();

        try {
            Connection connection = DriverManager.getConnection(Config.POSTGRE_URL, "ENTER_SUPERUSER_HERE", "ENTER_PASSWORD_HERE");
            JdbcSession session = new JdbcSession(connection).autocommit(false);
            System.out.println("Starting");

            for (int index = 21708; index < ItemDefinition.DEFINITIONS.length; index++) {
                ItemDefinition definition = ItemDefinition.get(index);

                if (definition.getName() != null && !definition.getName().equals("null")) {
                    continue;
                }

                session
                    .sql(query)
                    .set(definition.getId())
                    .set(definition.getName())
                    .set(definition.getValue())
                .execute();
            }

            session.commit();
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
