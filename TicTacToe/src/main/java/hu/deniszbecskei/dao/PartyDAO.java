package hu.deniszbecskei.dao;

import java.util.List;

public interface PartyDAO {
    List<String> getAllKeys();
    Party save(Party party);
}
