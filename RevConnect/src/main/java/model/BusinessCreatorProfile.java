package model;


public class BusinessCreatorProfile {

    private int profileId;
    private int userId;
    private String businessOrCreatorName;
    private String category;
    private String industry;
    private String detailedBio;
    private String businessAddress;
    private String contactInformation;
    private String websiteLinks;
    private String socialMediaLinks;
    private String businessHours;

    public BusinessCreatorProfile() {}

    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getBusinessOrCreatorName() { return businessOrCreatorName; }
    public void setBusinessOrCreatorName(String businessOrCreatorName) { this.businessOrCreatorName = businessOrCreatorName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getDetailedBio() { return detailedBio; }
    public void setDetailedBio(String detailedBio) { this.detailedBio = detailedBio; }

    public String getBusinessAddress() { return businessAddress; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }

    public String getContactInformation() { return contactInformation; }
    public void setContactInformation(String contactInformation) { this.contactInformation = contactInformation; }

    public String getWebsiteLinks() { return websiteLinks; }
    public void setWebsiteLinks(String websiteLinks) { this.websiteLinks = websiteLinks; }

    public String getSocialMediaLinks() { return socialMediaLinks; }
    public void setSocialMediaLinks(String socialMediaLinks) { this.socialMediaLinks = socialMediaLinks; }

    public String getBusinessHours() { return businessHours; }
    public void setBusinessHours(String businessHours) { this.businessHours = businessHours; }
}
