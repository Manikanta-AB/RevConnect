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
                    MERGE INTO business_creator_profile target
                    USING (SELECT ? AS user_id, ? AS business_name, ? AS cat, ? AS ind, ? AS bio,
                                  ? AS addr, ? AS contact, ? AS web, ? AS social, ? AS hours FROM dual) source
                    ON (target.user_id = source.user_id)
                    WHEN MATCHED THEN
                    UPDATE SET business_or_creator_name = source.business_name,
                               category = source.cat,
                               industry = source.ind,
                               detailed_bio = source.bio,
                               business_address = source.addr,
                               contact_information = source.contact,
                               website_links = source.web,
                               social_media_links = source.social,
                               business_hours = source.hours
                    WHEN NOT MATCHED THEN
                    INSERT (user_id, business_or_creator_name, category, industry, detailed_bio,
                            business_address, contact_information, website_links, social_media_links, business_hours)
                    VALUES (source.user_id, source.business_name, source.cat, source.ind, source.bio,
                            source.addr, source.contact, source.web, source.social, source.hours)
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
            // Oracle PreparedStatement parameters repeat for INSERT part in MERGE if
            // handled this way
            // But here I'm using source columns which are defined once in USING.
            // Wait, I used 10 parameters in the SELECT. So I need to set 10.

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
