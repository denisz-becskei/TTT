package hu.deniszbecskei.dao;
import java.time.LocalDate;
import java.util.Random;

public class Party {
    private String id;
    private final int tableSize;
    private final Integer[][] turns;
    private final LocalDate playedOn = LocalDate.now( );

    public Party(int tableSize, Integer[][] turns) {
        Random random = new Random();

        PartyDAOImpl dao = new PartyDAOImpl();

        do {
            this.id = "";
            for (int i = 0; i < 6; i++) {
                if (i % 2 == 0) {
                    int randomnum = random.nextInt(25) + 65;
                    this.id += (char) randomnum;
                } else {
                    int randomnum = random.nextInt(10);
                    this.id += randomnum;
                }
            }
        } while (dao.isInDB(this.id));

        this.tableSize = tableSize;
        this.turns = turns;
    }

    public String getId() {
        return id;
    }

    public int getTableSize() {
        return tableSize;
    }

    public String getTurnsIntoDB() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < tableSize; i++) {
            for (int j = 0; j < tableSize; j++) {
                output.append(turns[i][j]).append(" ");
            }
        }

        return output.toString();
    }

    public LocalDate getPlayedOn() {
        return playedOn;
    }
}
