/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.8.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2014
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.web.mvc.viewbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hoteia.qalingo.core.Constants;
import org.hoteia.qalingo.core.util.CoreUtil;

public class RetailerViewBean extends AbstractViewBean {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -5387549593050575454L;

	// ENTITY
	protected int version;
	protected String code;
    protected String name;
    protected String description;

    protected String i18nName;
    protected String i18nDescription;
    protected String i18nShortDescription;

	protected boolean isDefault;
	protected boolean isOfficialRetailer;

	protected boolean isBrand;
	protected boolean isEcommerce;
	protected boolean isCorner;

	protected int qualityOfService = 0;
	protected int priceScore = 0;
	protected int ratioQualityPrice = 0;

	protected int reviewCount = 0;
	protected String reviewCountLabel;

	protected RetailerAddressViewBean defaultAddress = new RetailerAddressViewBean();

    protected List<AssetViewBean> assets = new ArrayList<AssetViewBean>();

	protected List<RetailerCustomerCommentViewBean> comments = new ArrayList<RetailerCustomerCommentViewBean>();
	protected List<RetailerTagViewBean> tags = new ArrayList<RetailerTagViewBean>();

	protected Map<String, List<SocialNetworkFeedItemViewBean>> socialNetworkFeed = new HashMap<String, List<SocialNetworkFeedItemViewBean>>();

	protected List<StoreViewBean> stores = new ArrayList<StoreViewBean>();

	protected List<ShareOptionViewBean> shareOptions = new ArrayList<ShareOptionViewBean>();

    protected String detailsUrl;
    protected String editUrl;
    protected String storeListUrl;
    
	// MENU
	protected String img;
	protected String changeContextUrl;
	protected String homeUrl;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getI18nName() {
        if(StringUtils.isNotEmpty(i18nName)){
            return i18nName;
        }
        return name;
    }
    
    public void setI18nName(String i18nName) {
        this.i18nName = i18nName;
    }
    
    public String getI18nDescription() {
        if(StringUtils.isNotEmpty(i18nDescription)){
            return i18nDescription;
        }
        return description;
    }
    
    public void setI18nDescription(String i18nDescription) {
        this.i18nDescription = i18nDescription;
    }
    
    public String getI18nShortDescription() {
        return i18nShortDescription;
    }
    
    public void setI18nShortDescription(String i18nShortDescription) {
        this.i18nShortDescription = i18nShortDescription;
    }
    
    public String getI18nTruncatedDescription() {
        if(StringUtils.isNotEmpty(getI18nShortDescription())){
            if(getI18nShortDescription().length() >= 150){
                return CoreUtil.handleTruncatedDescription(getI18nShortDescription());
            } else {
                return getI18nShortDescription();
            }
        } else if (StringUtils.isNotEmpty(getI18nDescription())){
            if(getI18nDescription().length() >= 150){
                return CoreUtil.handleTruncatedDescription(getI18nDescription());
            } else {
                return getI18nDescription();
            }
        }
        return "";
    }

	public String getShortDescription() {
		String shortDescription = getDescription();
		if (StringUtils.isNotEmpty(shortDescription)) {
			shortDescription = removeHtml(shortDescription);
			if (shortDescription.length() > Constants.SHORT_DESCRIPTION_MAX_LENGTH) {
				shortDescription = shortDescription.substring(0,
						Constants.SHORT_DESCRIPTION_MAX_LENGTH) + "...";
			}
		}
		return shortDescription;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isOfficialRetailer() {
		return isOfficialRetailer;
	}

	public void setOfficialRetailer(boolean isOfficialRetailer) {
		this.isOfficialRetailer = isOfficialRetailer;
	}

	public boolean isBrand() {
		return isBrand;
	}

	public void setBrand(boolean isBrand) {
		this.isBrand = isBrand;
	}

	public boolean isEcommerce() {
		return isEcommerce;
	}

	public void setEcommerce(boolean isEcommerce) {
		this.isEcommerce = isEcommerce;
	}

	public boolean isCorner() {
		return isCorner;
	}

	public void setCorner(boolean isCorner) {
		this.isCorner = isCorner;
	}

	public int getQualityOfService() {
		return qualityOfService;
	}

	public void setQualityOfService(int qualityOfService) {
		this.qualityOfService = qualityOfService;
	}

	public int getPriceScore() {
		return priceScore;
	}

	public void setPriceScore(int priceScore) {
		this.priceScore = priceScore;
	}

	public int getRatioQualityPrice() {
		return ratioQualityPrice;
	}

	public void setRatioQualityPrice(int ratioQualityPrice) {
		this.ratioQualityPrice = ratioQualityPrice;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getReviewCountLabel() {
		return reviewCountLabel;
	}

	public void setReviewCountLabel(String reviewCountLabel) {
		this.reviewCountLabel = reviewCountLabel;
	}

	public RetailerAddressViewBean getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(RetailerAddressViewBean defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

    public List<AssetViewBean> getAssets() {
        return assets;
    }
    
    public List<AssetViewBean> getAssets(String type) {
        List<AssetViewBean> assetsByType = new ArrayList<AssetViewBean>();
        for (Iterator<AssetViewBean> iterator = assets.iterator(); iterator.hasNext();) {
            AssetViewBean assetViewBean = (AssetViewBean) iterator.next();
            if(assetViewBean.getType().equals(type)){
                assetsByType.add(assetViewBean);
            }
        }
        if(assetsByType.size() == 0){
            assetsByType.add(getDefaultAsset());
        }
        return assetsByType;
    }
    
    public String getAssetPath(String type) {
        for (Iterator<AssetViewBean> iterator = assets.iterator(); iterator.hasNext();) {
            AssetViewBean assetViewBean = (AssetViewBean) iterator.next();
            if(assetViewBean.getType().equals(type)){
                return assetViewBean.getPath();
            }
        }
        AssetViewBean assetViewBean = getDefaultAsset();
        if(assetViewBean != null){
            return assetViewBean.getPath();
        }
        return null;
    }
    
    public String getAssetAbsoluteWebPath(String type) {
        for (Iterator<AssetViewBean> iterator = assets.iterator(); iterator.hasNext();) {
            AssetViewBean assetViewBean = (AssetViewBean) iterator.next();
            if(assetViewBean.getType().equals(type)){
                return assetViewBean.getAbsoluteWebPath();
            }
        }
        AssetViewBean assetViewBean = getDefaultAsset();
        if(assetViewBean != null){
            return assetViewBean.getAbsoluteWebPath();
        }
        return null;
    }
    
    public String getAssetRelativeWebPath(String type) {
        for (Iterator<AssetViewBean> iterator = assets.iterator(); iterator.hasNext();) {
            AssetViewBean assetViewBean = (AssetViewBean) iterator.next();
            if(assetViewBean.getType().equals(type)){
                return assetViewBean.getRelativeWebPath();
            }
        }
        AssetViewBean assetViewBean = getDefaultAsset();
        if(assetViewBean != null){
            return assetViewBean.getRelativeWebPath();
        }
        return null;
    }

    public AssetViewBean getDefaultAsset() {
        for (Iterator<AssetViewBean> iterator = assets.iterator(); iterator.hasNext();) {
            AssetViewBean assetViewBean = (AssetViewBean) iterator.next();
            if("default".equals(assetViewBean.getType())){
                return assetViewBean;
            }
        }
        return null;
    }
    
    public void setAssets(List<AssetViewBean> assets) {
        this.assets = assets;
    }
    
	public List<RetailerCustomerCommentViewBean> getComments() {
		return comments;
	}

	public void setComments(List<RetailerCustomerCommentViewBean> comments) {
		this.comments = comments;
	}

	public List<RetailerTagViewBean> getTags() {
		return tags;
	}

	public void setTags(List<RetailerTagViewBean> tags) {
		this.tags = tags;
	}

	public Map<String, List<SocialNetworkFeedItemViewBean>> getSocialNetworkFeed() {
		return socialNetworkFeed;
	}

	public void setSocialNetworkFeed(
			Map<String, List<SocialNetworkFeedItemViewBean>> socialNetworkFeed) {
		this.socialNetworkFeed = socialNetworkFeed;
	}

	public List<StoreViewBean> getStores() {
		return stores;
	}

	public void setStores(List<StoreViewBean> stores) {
		this.stores = stores;
	}

	public List<ShareOptionViewBean> getShareOptions() {
		return shareOptions;
	}

	public void setShareOptions(List<ShareOptionViewBean> shareOptions) {
		this.shareOptions = shareOptions;
	}

	public String getMetaShareTitle() {
		String metaShareTitle = getName();
		metaShareTitle = encodeQuote(metaShareTitle);
		return metaShareTitle;
	}

	public String getMetaShareDescription() {
		String metaShareDescription = getDescription();
		metaShareDescription = encodeQuote(metaShareDescription);
		if (StringUtils.isNotEmpty(metaShareDescription)) {
			metaShareDescription = removeHtml(metaShareDescription);
			metaShareDescription = encodeQuote(metaShareDescription);
			if (metaShareDescription.length() > Constants.SHARE_META_DESCRIPTION_MAX_LENGTH) {
				metaShareDescription = metaShareDescription.substring(0,
						Constants.SHARE_META_DESCRIPTION_MAX_LENGTH) + "...";
			}
		}
		return metaShareDescription;
	}

	public String getMetaShareImage() {
		return getImg();
	}

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }
    
    public String getStoreListUrl() {
        return storeListUrl;
    }
    
    public void setStoreListUrl(String storeListUrl) {
        this.storeListUrl = storeListUrl;
    }

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getChangeContextUrl() {
		return changeContextUrl;
	}

	public void setChangeContextUrl(String changeContextUrl) {
		this.changeContextUrl = changeContextUrl;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

}