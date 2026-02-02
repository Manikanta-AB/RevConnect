package dao.impl;



import config.DBConnection;
import dao.BusinessCreatorProfileDAO;
import model.BusinessCreatorProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BusinessCreatorProfileDAOImpl implements BusinessCreatorProfileDAO {

    @Override
    public BusinessCreatorProfile getProfileByUserId(int userId) {

        String sql = "SELECT * FROM business_creator_profile WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                BusinessCreatorProfile profile = new BusinessCreatorProfile();
                profile.setProfileId(rs.getInt("profile_id"));
                profile.setUserId(rs.getInt("user_id"));
                profile.setBusinessOrCreatorName(rs.getString("business_or_creator_name"));
                profile.setCategory(rs.getString("category"));
                profile.setIndustry(rs.getString("industry"));
                profile.setDetailedBio(rs.getString("detailed_bio"));
                profile.setBusinessAddress(rs.getString("business_address"));
                profile.setContactInformation(rs.getString("contact_information"));
                profile.setWebsiteLinks(rs.getString("website_links"));
                profile.setSocialMediaLinks(rs.getString("social_media_links"));
                profile.setBusinessHours(rs.getString("business_hours"));
                return profile;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createOrUpdateProfile(BusinessCreatorProfile profile) {

        String sql = """
            INSERT INTO business_creator_profile
            (user_id, business_or_creator_name, category, industry, detailed_bio,
             business_address, contact_information, website_links, social_media_links, business_hours)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            business_or_creator_name = VALUES(business_or_creator_name),
            category = VALUES(category),
            industry = VALUES(industry),
            detailed_bio = VALUES(detailed_bio),
            business_address = VALUES(business_address),
            contact_information = VALUES(contact_information),
            website_links = VALUES(website_links),
            social_media_links = VALUES(social_media_links),
            business_hours = VALUES(business_hours)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getBusinessOrCreatorName());
            ps.setString(3, profile.getCategory());
            ps.setString(4, profile.getIndustry());
            ps.setString(5, profile.getDetailedBio());
            ps.setString(6, profile.getBusinessAddress());
            ps.setString(7, profile.getContactInformation());
            ps.setString(8, profile.getWebsiteLinks());
            ps.setString(9, profile.getSocialMediaLinks());
            ps.setString(10, profile.getBusinessHours());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
