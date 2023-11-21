package hu.deniszbecskei.dao;

import hu.deniszbecskei.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class PartyDAOImpl implements PartyDAO {
    private final String INSERT_PARTY = "INSERT INTO parties(id, turns, table_size, played_on) VALUES (?, ?, ?, ?)";
    private final String GET_ALL_KEYS = "SELECT id FROM parties";

    private final String connectionURL;

    public PartyDAOImpl() {
        connectionURL = AppConfig.getValue("db.url");
    }

    public boolean isInDB(String id) {
        List<String> ids = getAllKeys();
        return ids.contains(id);
    }

    @Override
    public List<String> getAllKeys() {
        List<String> ids = new ArrayList<>();

        try(Connection c = DriverManager.getConnection(connectionURL);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(GET_ALL_KEYS);
        ){
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return ids;
    }

    @Override
    public Party save(Party party) {
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = c.prepareStatement(INSERT_PARTY, Statement.RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1, party.getId());
            stmt.setString(2, party.getTurnsIntoDB());
            stmt.setInt(3, party.getTableSize());
            stmt.setObject(4, party.getPlayedOn());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return null;
            }
            System.out.println("Saved to database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return party;
    }

}
