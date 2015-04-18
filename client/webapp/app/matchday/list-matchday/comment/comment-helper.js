(function() {
	"use strict";

	/*
	 * This ccontroller only can be used on List-Matchday Controller
	 */
	angular
		.module("app.matchday")
		.controller("commentctrl", CommentCtrl);

	function CommentCtrl(dataservice, commenthelper, $modal, $scope) {
		var maxCommentLength = 500;
		var remainingChars = null; // Will be change when user start typing
		var deferred = null;
		var allComments = [];
		var modalInstance = null;

		// Modal dialog for post comment {new comment / reply}
		var titleDialog = null;
		var postTextBtn = null;
		var placeHolder = null;
		var parentComment = null;

		var service = {
			initNewComment: initNewComment,
			userTypeNewComment: userTypeNewComment,
			postNewComment: postNewComment,
			getRemainingChars: getRemainingChars,
			fetchComments: fetchComments,
			getAllComments: getAllComments,
			getTitleDialog: getTitleDialog,
			getPostTextBtn: getPostTextBtn,
			getPlaceHolder: getPlaceHolder,
			getParentComment: getParentComment,
			openCommentDialog: openCommentDialog,
			closeCommentDialog: closeCommentDialog,
			postNewComment: postNewComment
		}
		angular.extend(this, service);
		this.newComment = "";

		function closeCommentDialog() {
			modalInstance.dismiss();
		}

		function openCommentDialog(parentComment) {
			if (this && this.newComment)
				this.newComment = "";

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

		function fetchComments(matchdayId) {
			allComments = null;
			deferred = $.Deferred();
			dataservice.fetchComments(matchdayId).then(afterFetchComments);
			return deferred;
		}

		function afterFetchComments(resp) {
			if (200 === resp.status) {
				allComments = resp.data.comments;
				_.each(allComments, function(c) {
					// Set timeDiff on all comment
					c.timeDiff = commenthelper.findTimeDiff(c.created);
					if (c.subComment && c.subComment.length > 0 ) {
						_.each(c.subComment, function(s) {
							// Set timeDIff on all children comment
							s.timeDiff = commenthelper.findTimeDiff(s.created);
						});
					}
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