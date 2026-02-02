package dao;

import java.util.List;


public interface BlockDAO {

    boolean blockUser(int blockerId, int blockedId);

    boolean unblockUser(int blockerId, int blockedId);

    boolean isBlocked(int blockerId, int blockedId);

    List<Integer> getBlockedUsers(int blockerId);
}

