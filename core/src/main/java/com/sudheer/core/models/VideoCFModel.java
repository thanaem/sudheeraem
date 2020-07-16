package com.sudheer.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VideoCFModel {

    public static final String LINK = "link";
    public static final String LINK_TYPE = "linkType";
    public static final String TEXT = "text";
    public static final String NAME = "name";
    public static final String ALT = "altText";
    public static final String IMAGE = "image";
    public static final String TAGS = "tags";

    @Self
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    private Optional<ContentFragment> contentFragment;

    private String link;
    private String linkType;
    private String name;
    private String image;
    private String altText;
    private String text;
    private List<String> tags;

    @PostConstruct
    private void init() {
        this.contentFragment = Optional.ofNullable(this.resource.adaptTo(ContentFragment.class));
    }

    private String getFromContentFragmentElement(String elementName) {
        return this.contentFragment
                .map(contentFragment -> contentFragment.getElement(elementName))
                .map(ContentElement::getContent)
                .orElse(StringUtils.EMPTY);
    }

    private List<String> getListFromContentFragmentElement(String elementName) {
        return (List<String>) this.contentFragment
                .map(contentFragment -> contentFragment.getElement(elementName))
                .map(ContentElement::getValue)
                .map(element -> element.getValue())
                .orElse(Collections.emptyList());
    }

    public String getVideoLink() {
        if (this.link == null) {
            this.link = getFromContentFragmentElement(LINK);
        }
        return this.link;
    }

    public String getVideoType() {
        if (this.linkType == null) {
            this.linkType = getFromContentFragmentElement(LINK_TYPE);
        }
        return this.linkType;
    }

    public String getName() {
        if (this.name == null) {
            this.name = getFromContentFragmentElement(NAME);
        }
        return this.name;
    }

    public String getText() {
        if (this.text == null) {
            this.text = getFromContentFragmentElement(TEXT);
        }
        return this.text;
    }

    public String getFileReference() {
        if (this.image == null) {
            this.image = getFromContentFragmentElement(IMAGE);
        }
        return this.image;
    }

    public String getAlt() {
        if (this.altText == null) {
            this.altText = getFromContentFragmentElement(ALT);
        }
        return this.altText;
    }

    public List<String> getTags() {
        if (this.tags == null) {
            this.tags = getListFromContentFragmentElement(TAGS);
        }
        return new ArrayList<>(this.tags);
    }
}
