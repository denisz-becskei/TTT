package hu.deniszbecskei.dao;

import java.util.List;

public interface PartyDAO {
    List<Party> getParties();
    Party getParty(String partyId);
    void delete(String partyId);
}
