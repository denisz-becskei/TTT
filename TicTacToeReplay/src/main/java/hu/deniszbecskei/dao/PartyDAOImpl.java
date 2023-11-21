package hu.deniszbecskei.dao;
import hu.deniszbecskei.AppConfig;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PartyDAOImpl implements PartyDAO {
    private final String GET_ALL_PARTIES = "SELECT * FROM parties ORDER BY played_on DESC, id";
    private final String GET_SPECIFIC_PARTY = "SELECT * FROM parties WHERE id = ?";
    private final String REMOVE_PARTY = "DELETE FROM parties WHERE id = ?";

    private final String connectionURL;

    public PartyDAOImpl() {
        connectionURL = AppConfig.getValue("db.url");
    }

    private Integer[][] stringToIntArr(int tableSize, String str) {
        Integer[][] turns = new Integer[tableSize][tableSize];
        String[] turnsSplit = str.split(" ");
        int turnIterationCount = 0;
        for (int i = 0; i < tableSize; i++) {
            for (int j = 0; j < tableSize; j++) {
                turns[i][j] = Integer.parseInt(turnsSplit[turnIterationCount]);
                turnIterationCount++;
            }
        }
        return turns;
    }

    @Override
    public List<Party> getParties() {
        List<Party> parties = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(connectionURL);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(GET_ALL_PARTIES);
        ) {
            while (rs.next()) {
                Party party = new Party();
                party.setId(rs.getString("id"));
                party.setTableSize(rs.getInt("table_size"));
                party.setMoves(stringToIntArr(party.getTableSize(), rs.getString("turns")));
                party.setPlayedOn((LocalDate.parse(rs.getString("played_on"))));
                parties.add(party);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return parties;
    }

    @Override
    public Party getParty(String partyId) {
        Party party = new Party();
        try (Connection c = DriverManager.getConnection(connectionURL);
             PreparedStatement stmt = c.prepareStatement(GET_SPECIFIC_PARTY, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, partyId);
            ResultSet rs = stmt.executeQuery();

            party.setId(rs.getString("id"));
            party.setTableSize(rs.getInt("table_size"));
            party.setPlayedOn(LocalDate.parse(rs.getString("played_on")));
            party.setMoves(stringToIntArr(party.getTableSize(), rs.getString("turns")));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return party;
    }

    @Override
    public void delete(String partyId) {
        try (Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = c.prepareStatement(REMOVE_PARTY, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, partyId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
