package com.sudheer.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class, resourceType = "sudheer/components/content/breadcrumb")
public class Breadcrumb {

	protected static final boolean PROP_SHOW_HIDDEN_DEFAULT = false;
	protected static final boolean PROP_HIDE_CURRENT_DEFAULT = false;
	protected static final int PROP_START_LEVEL_DEFAULT = 2;
	protected static final String PROP_TARGET_DEFAULT = "_self";
	private static final String START_LEVEL = "startLevel";
	private static final String TARGET = "target";
	private static final String SHOW_HIDDEN = "showHidden";
	private static final String HIDE_CURRENT = "hideCurrent";

	@ScriptVariable
	private Style currentStyle;

	@ScriptVariable
	private Page currentPage;

	@Self
	private SlingHttpServletRequest request;

	@ScriptVariable
	private ValueMap properties;

	private boolean showHidden;
	private boolean hideCurrent;
	private int startLevel;
	private String target;
	private List<BasePageModel> items;

	public String getTarget() {
		return this.target;
	}

	@PostConstruct
	private void init() {
		this.startLevel = this.properties.get(START_LEVEL,
				this.currentStyle.get(START_LEVEL, PROP_START_LEVEL_DEFAULT));
		this.showHidden = this.properties.get(SHOW_HIDDEN,
				this.currentStyle.get(SHOW_HIDDEN, PROP_SHOW_HIDDEN_DEFAULT));
		this.hideCurrent = this.properties.get(HIDE_CURRENT,
				this.currentStyle.get(HIDE_CURRENT, PROP_HIDE_CURRENT_DEFAULT));
		this.target = this.properties.getOrDefault(TARGET, this.currentStyle.get(TARGET, PROP_TARGET_DEFAULT))
				.toString();
	}

	public Collection<BasePageModel> getItems() {
		if (this.items == null) {
			this.createItems();
		}
		return new ArrayList<>(this.items);
	}

	public String getExportedType() {
		return this.request.getResource().getResourceType();
	}

	private void createItems() {
		items=new ArrayList<BasePageModel>();
		BasePageModel basePage = currentPage.adaptTo(BasePageModel.class);
		if(basePage != null && !basePage.isHideBreadCrumb()) {
			for (final int currentLevel = this.currentPage.getDepth(); this.startLevel < currentLevel; ++this.startLevel) {
				final Page page = this.currentPage.getAbsoluteParent(this.startLevel);
				if (page != null) {
					final boolean isActivePage = page.equals(this.currentPage);
					if (!(isActivePage && this.hideCurrent) && (this.checkIfNotHidden(page))) {
						items.add(page.adaptTo(BasePageModel.class));
					}
				}
			}
		}
	}

	private boolean checkIfNotHidden(final Page page) {
		BasePageModel basePage = page.adaptTo(BasePageModel.class);
		return (!basePage.isHideInNav() && !basePage.isHideFromBreadCrumb()) || this.showHidden;
	}
}
