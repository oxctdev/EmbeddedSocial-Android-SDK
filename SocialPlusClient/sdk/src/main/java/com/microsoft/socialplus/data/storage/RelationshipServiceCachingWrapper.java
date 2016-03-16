/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.socialplus.data.storage;

import com.microsoft.socialplus.data.storage.request.wrapper.relationship.BlockedUserFeedRequestWrapper;
import com.microsoft.socialplus.data.storage.request.wrapper.relationship.FollowerFeedRequestWrapper;
import com.microsoft.socialplus.data.storage.request.wrapper.relationship.FollowingFeedRequestWrapper;
import com.microsoft.socialplus.data.storage.request.wrapper.relationship.PendingUserFeedRequestWrapper;
import com.microsoft.socialplus.server.IRelationshipService;
import com.microsoft.socialplus.server.exception.NetworkRequestException;
import com.microsoft.socialplus.server.model.UsersListResponse;
import com.microsoft.socialplus.server.model.relationship.AcceptFollowRequest;
import com.microsoft.socialplus.server.model.relationship.BlockUserRequest;
import com.microsoft.socialplus.server.model.relationship.FollowUserRequest;
import com.microsoft.socialplus.server.model.relationship.FollowUserResponse;
import com.microsoft.socialplus.server.model.relationship.GetBlockedUsersRequest;
import com.microsoft.socialplus.server.model.relationship.GetFollowerFeedRequest;
import com.microsoft.socialplus.server.model.relationship.GetFollowingFeedRequest;
import com.microsoft.socialplus.server.model.relationship.GetPendingUsersRequest;
import com.microsoft.socialplus.server.model.relationship.RejectFollowRequest;
import com.microsoft.socialplus.server.model.relationship.UnblockUserRequest;
import com.microsoft.socialplus.server.model.relationship.UnfollowUserRequest;

import retrofit2.Response;

/**
 * Provides transparent cache functionality on top of {@linkplain IRelationshipService}.
 */
public class RelationshipServiceCachingWrapper implements IRelationshipService {

	private final BlockedUserFeedRequestWrapper blockedUsersWrapper;
	private final PendingUserFeedRequestWrapper pendingUsersWrapper;
	private final FollowerFeedRequestWrapper userFollowerFeedWrapper;
	private final FollowingFeedRequestWrapper userFollowingWrapper;

	private final IRelationshipService wrappedService;

	/**
	 * Creates an instance.
	 *
	 * @param wrappedService service to wrap
	 */
	public RelationshipServiceCachingWrapper(IRelationshipService wrappedService) {
		this.wrappedService = wrappedService;
		UserCache userCache = new UserCache();
		userFollowingWrapper = new FollowingFeedRequestWrapper(
			wrappedService::getUserFollowingFeed,
			userCache,
			UserCache.UserFeedType.FOLLOWING
		);
		userFollowerFeedWrapper = new FollowerFeedRequestWrapper(
			wrappedService::getUserFollowerFeed,
			userCache,
			UserCache.UserFeedType.FOLLOWER
		);
		blockedUsersWrapper = new BlockedUserFeedRequestWrapper(
			wrappedService::getUserBlockedFeed,
			userCache,
			UserCache.UserFeedType.BLOCKED
		);
		pendingUsersWrapper = new PendingUserFeedRequestWrapper(
			wrappedService::getUserPendingFeed,
			userCache,
			UserCache.UserFeedType.PENDING
		);
	}

	@Override
	public Response acceptUser(AcceptFollowRequest request) throws NetworkRequestException {
		return wrappedService.acceptUser(request);
	}

	@Override
	public Response blockUser(BlockUserRequest request) throws NetworkRequestException {
		return wrappedService.blockUser(request);
	}

	@Override
	public FollowUserResponse followUser(FollowUserRequest request) throws NetworkRequestException {
		return wrappedService.followUser(request);
	}

	@Override
	public UsersListResponse getUserBlockedFeed(GetBlockedUsersRequest request) throws NetworkRequestException {
		return blockedUsersWrapper.getResponse(request);
	}

	@Override
	public UsersListResponse getUserFollowerFeed(GetFollowerFeedRequest request) throws NetworkRequestException {
		return userFollowerFeedWrapper.getResponse(request);
	}

	@Override
	public UsersListResponse getUserFollowingFeed(GetFollowingFeedRequest request) throws NetworkRequestException {
		return userFollowingWrapper.getResponse(request);
	}

	@Override
	public UsersListResponse getUserPendingFeed(GetPendingUsersRequest request) throws NetworkRequestException {
		return pendingUsersWrapper.getResponse(request);
	}

	@Override
	public Response rejectUser(RejectFollowRequest request) throws NetworkRequestException {
		return wrappedService.rejectUser(request);
	}

	@Override
	public Response unblockUser(UnblockUserRequest request) throws NetworkRequestException {
		return wrappedService.unblockUser(request);
	}

	@Override
	public Response unfollowUser(UnfollowUserRequest request) throws NetworkRequestException {
		return wrappedService.unfollowUser(request);
	}
}
