package service;


import dao.BlockDAO;
import dao.impl.BlockDAOImpl;

import java.util.List;

public class BlockService {

    private BlockDAO blockDAO = new BlockDAOImpl();

    public boolean blockUser(int blockerId, int blockedId) {
        return blockDAO.blockUser(blockerId, blockedId);
    }

    public boolean unblockUser(int blockerId, int blockedId) {
        return blockDAO.unblockUser(blockerId, blockedId);
    }

    public boolean isBlocked(int blockerId, int blockedId) {
        return blockDAO.isBlocked(blockerId, blockedId);
    }

    public List<Integer> getBlockedUsers(int blockerId) {
        return blockDAO.getBlockedUsers(blockerId);
    }
}
