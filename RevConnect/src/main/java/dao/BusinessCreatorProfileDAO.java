package dao;


import model.BusinessCreatorProfile;

public interface BusinessCreatorProfileDAO {

    BusinessCreatorProfile getProfileByUserId(int userId);

    boolean createOrUpdateProfile(BusinessCreatorProfile profile);
}

