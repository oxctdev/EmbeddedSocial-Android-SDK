/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.ui.activity;

import com.microsoft.embeddedsocial.ui.activity.base.BaseActivity;
import com.microsoft.embeddedsocial.ui.fragment.FeedViewMenuFragment;
import com.microsoft.embeddedsocial.ui.fragment.RecentActivityFragmentTabs;
import com.microsoft.embeddedsocial.sdk.R;

/**
 * Shows recent activity.
 */
public class RecentActivityActivity extends BaseActivity {
	public static final String NAME = "ActivityFeed";

	public RecentActivityActivity() {
		super(R.id.es_navigationActivity);
	}

	@Override
	protected void setupFragments() {
		setActivityContent(new RecentActivityFragmentTabs());
		super.setupFragments();
		getSupportFragmentManager().beginTransaction().add(new FeedViewMenuFragment(), FeedViewMenuFragment.TAG).commit();
	}

	@Override
	protected boolean isAuthorizationRequired() {
		return true;
	}

	@Override
	protected String getName() {
		return NAME;
	}
}
