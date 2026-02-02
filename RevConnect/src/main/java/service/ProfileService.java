package service;



import dao.BusinessCreatorProfileDAO;
import dao.ProfileDAO;
import dao.impl.BusinessCreatorProfileDAOImpl;
import dao.impl.ProfileDAOImpl;
import model.BusinessCreatorProfile;
import model.User;

public class ProfileService {

    private ProfileDAO profileDAO = new ProfileDAOImpl();
    private BusinessCreatorProfileDAO bcDAO = new BusinessCreatorProfileDAOImpl();

    public User viewOwnProfile(int userId) {
        return profileDAO.getUserProfile(userId);
    }

    public boolean updateProfile(User user) {
        return profileDAO.updateUserProfile(user);
    }

    public User searchUser(String username) {
        return profileDAO.getUserByUsername(username);
    }

    public BusinessCreatorProfile viewBusinessCreatorProfile(int userId) {
        return bcDAO.getProfileByUserId(userId);
    }

    public boolean updateBusinessCreatorProfile(BusinessCreatorProfile profile) {
        return bcDAO.createOrUpdateProfile(profile);
    }
    public User getUserByUsername(String username) {
        return profileDAO.getUserByUsername(username);
    }

}
