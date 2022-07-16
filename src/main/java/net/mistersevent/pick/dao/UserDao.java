package net.mistersevent.pick.dao;

import net.mistersevent.pick.Main;
import net.mistersevent.pick.adapter.UserAdapter;
import net.mistersevent.pick.model.UserModel;

import java.sql.*;

public class UserDao {
    private final Connection connection;
    private final UserAdapter adapter;

    public UserDao(final Connection connection) {
        this.connection = connection;
        this.adapter = new UserAdapter();
        this.createTable();
    }

    public void createTable() {
        try (final PreparedStatement statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS `pick_users` (id VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16) NOT NULL, count DOUBLE NOT NULL, xp DOUBLE NOT NULL, enchantments TEXT NOT NULL);")) {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        try (PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM `pick_users`;")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Main.getInstance().getUserCache().addElement(this.adapter.read(resultSet));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertOrUpdate(final UserModel model) {
        try (PreparedStatement statement = this.connection.prepareStatement("INSERT INTO `pick_users` VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE count=?, xp=?, enchantments=?;")) {
            this.adapter.write(statement, model);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
