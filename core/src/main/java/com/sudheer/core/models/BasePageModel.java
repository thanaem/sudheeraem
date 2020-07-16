package com.sudheer.core.models;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

@Model(adaptables = {Page.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BasePageModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = "jcr:content/author")
    private String author;

    @ValueMapValue(name = "jcr:content/publicationDate")
    private Calendar publicationDate;

    @ValueMapValue(name = "jcr:content/displayDate")
    private boolean isDisplayDate;

    @ValueMapValue(name = "jcr:content/jcr:title")
    private String jcrTitle;

    @ValueMapValue(name = "jcr:content/pageTitle")
    private String pageTitle;

    @ValueMapValue(name = "jcr:content/navTitle")
    private String navigationTitle;

    
    private String title;

    private String path;

    
    @ValueMapValue(name = "jcr:content/subTitle")
    private String subTitle;

    
    @ValueMapValue(name = "jcr:content/breadCrumbTitle")
    private String breadCrumbTitle;

    
    @ValueMapValue(name = "jcr:content/description")
    private String description;

    @ValueMapValue(name = "jcr:content/cq:lastModified")
    private Calendar lastModified;

    @ValueMapValue(name = "jcr:content/cq:lastReplicated")
    private Calendar replicatedDate;

    @ValueMapValue(name = "jcr:content/jcr:created")
    private Calendar createdDate;

    @ValueMapValue(name = "jcr:content/onTime")
    private Calendar onTime;

    @ValueMapValue(name = "jcr:content/offTime")
    private Calendar offTime;

    @ValueMapValue(name = "jcr:content/hideInNav")
    private boolean isHideInNav;

    @ValueMapValue(name = "jcr:content/linkTarget")
    private String linkTarget;

    @ValueMapValue(name = "jcr:content/contentType")
    private String contentType;

    @ValueMapValue(name = "jcr:content/breadcrumbHide")
    private boolean isHideFromBreadCrumb;

    @ValueMapValue(name = "jcr:content/hideBreadcrumb")
    private boolean isHideBreadCrumb;

    private String contentTitle;

    @ValueMapValue(name = "jcr:content/cq:redirectTarget")
    private String redirectURL;

    @Self
    private Page page;

    @Self
    private Resource resource;

    @PostConstruct
    public void init() {
        if (StringUtils.isNotBlank(getNavigationTitle())) {
            title = getNavigationTitle();
        } else if (StringUtils.isNotBlank(getPageTitle())) {
            title = getPageTitle();
        } else {
            title = getJcrTitle();
        }
        final TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);

        if (StringUtils.isNotBlank(contentType)) {
            contentTitle = tagManager.resolve(contentType).getName();
        }

        path = page.getPath();
    }

    public String getAuthor() {
        return author;
    }


    public Calendar getPublicationDate() {
        return publicationDate != null ? publicationDate : getReplicatedDate();
    }

    public boolean isDisplayDate() {
        return isDisplayDate;
    }

    public String getJcrTitle() {
        return jcrTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getNavigationTitle() {
        return navigationTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public Calendar getReplicatedDate() {
        return replicatedDate;
    }

    public Calendar getOnTime() {
        return onTime;
    }

    public Calendar getOffTime() {
        return offTime;
    }


    public String getLinkTarget() {
        return linkTarget;
    }

    public String getContentType() {
        return contentType;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public boolean isHideInNav() {
        return isHideInNav;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public boolean isHideFromBreadCrumb() {
        return isHideFromBreadCrumb;
    }

    public boolean isHideBreadCrumb() {
        return isHideBreadCrumb;
    }
    
    public String getBreadCrumbTitle(){
       return StringUtils.isNotEmpty(breadCrumbTitle) ? breadCrumbTitle : getTitle();
    }
}
