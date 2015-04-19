(function() {
	"use strict";

	/*
	 * This ccontroller only can be used on List-Matchday Controller
	 */
	angular
		.module("app.matchday")
		.controller("commentctrl", CommentCtrl);

	function CommentCtrl(dataservice, commenthelper, $modal, $scope) {
		var matchdayId = null;
		var maxCommentLength = 500;
		var remainingChars = null; // Will be change when user start typing
		var deferred = null;
		var allComments = [];
		var modalInstance = null;
		var totalComment = 0;
		var commentLoaded = 0;

		// Modal dialog for post comment {new comment / reply}
		var titleDialog = null;
		var postTextBtn = null;
		var placeHolder = null;
		var parentComment = null;

		var textInfoComment = "Load More Comment... ";
		var stillDoAsc = false;

		var service = {
			initNewComment: initNewComment,
			userTypeNewComment: userTypeNewComment,
			postNewComment: postNewComment,
			getRemainingChars: getRemainingChars,
			fetchComments: fetchComments,
			afterFetchComments: afterFetchComments,
			loadMoreComment: loadMoreComment,
			afterLoadComment: afterLoadComment,
			getAllComments: getAllComments,
			getTitleDialog: getTitleDialog,
			getPostTextBtn: getPostTextBtn,
			getPlaceHolder: getPlaceHolder,
			getParentComment: getParentComment,
			openCommentDialog: openCommentDialog,
			closeCommentDialog: closeCommentDialog,
			postNewComment: postNewComment,
			loadMoreReplies: loadMoreReplies, 
			afterLoadReplies: afterLoadReplies,
			getTotalComment: getTotalComment,
			getCommentLoaded: getCommentLoaded,
			getTextInfoComment: getTextInfoComment,
			isStillDoAsc: isStillDoAsc
		}
		angular.extend(this, service);
		this.newComment = "";

		function isStillDoAsc() {
			return stillDoAsc;
		}

		function getTextInfoComment() {
			return textInfoComment;
		}

		function getCommentLoaded() {
			return commentLoaded;
		}

		function getTotalComment() {
			return totalComment;
		}

		function loadMoreReplies(c) {
			stillDoAsc = true;
			c.textInfoSubCmt = "Loading...";
			dataservice.fetchSubComments(c.id, c.subCommentLoaded)
				.then(afterLoadReplies);
		}

		function afterLoadReplies(resp) {
			if (200 === resp.status) {
				var comments = resp.data.comments;
				_.each(comments, function(c) {
					var parent = commenthelper.findParentById(c.parentId, allComments);

					if (parent) {
							parent.subComment.push(c);
							parent.offset++;
							parent.subCommentLoaded++;
							parent.textInfoSubCmt = "Load More Replies...";
					}
				});
			}
			stillDoAsc = false;
		}

		function closeCommentDialog() {
			modalInstance.dismiss();
		}

		function openCommentDialog(parentComment) {
			if (this && this.newComment)
				this.newComment = "";

			remainingChars = maxCommentLength;

			initNewComment(parentComment);

			modalInstance = $modal.open({
				templateUrl: "newComment.html",
				size: "lg",
				scope: $scope
			});
		}

		function getParentComment() {
			return parentComment;
		}

		function getPlaceHolder() {
			return placeHolder;
		}

		function getPostTextBtn() {
			return postTextBtn;
		}

		function getTitleDialog() {
			return titleDialog;
		}

		function getAllComments() {
			return allComments;
		}

		function loadMoreComment() {
			stillDoAsc = true;
			textInfoComment = "Loading...";
			deferred = $.Deferred();
			dataservice.fetchComments(matchdayId, commentLoaded, false)
				.then(afterLoadComment);
			return deferred;			
		}

		function afterLoadComment(resp) {
			textInfoComment = "Load More Comment... ";
			
			if (200 === resp.status) {
				var allC = resp.data.comments;
				totalComment = resp.data.totalComment;
				_.each(allC, function(c) {
					commentLoaded++;
					commenthelper.initCommentObj(c);
					allComments.push(c);
				});
			}
			
			deferred.resolve(resp);
			stillDoAsc = false;
		}

		function fetchComments(mID) {
			matchdayId = mID;
			allComments = null;
			deferred = $.Deferred();
			dataservice.fetchComments(matchdayId, 0, true)
				.then(afterFetchComments);
			return deferred;
		}

		function afterFetchComments(resp) {
			if (200 === resp.status) {
				allComments = resp.data.comments;
				totalComment = resp.data.totalComment;
				commentLoaded = 0;
				_.each(allComments, function(c) {
					commentLoaded++;
					commenthelper.initCommentObj(c);
				});
			}
			deferred.resolve(resp);
		}

		function getRemainingChars() {
			return remainingChars;
		}

		function postNewComment() {
			console.log("this : ", this.newComment);
			closeCommentDialog();
		}

		function userTypeNewComment() {
			var newComment = this.newComment;
			if (newComment) {
				var newComment = newComment;
				var length = (newComment && newComment.length) 
					? newComment.length
					: 0;
				remainingChars = maxCommentLength - length;
			}
		}

		function initNewComment(pc) {
			// !pc is new comment, and comment is reply a comment it self
			titleDialog = !pc ? "New Comment" : "Reply Comment";
			postTextBtn = !pc ? "Post" : "Reply";
			placeHolder = !pc ? "Write comments...": "Reply comments...";
			parentComment = pc;
		}
	}
})();