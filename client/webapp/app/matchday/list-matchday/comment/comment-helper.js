(function() {
	"use strict";

	/*
	 * This controller only can be used on List-Matchday Controller
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
		var myComments = [];
		var myPoints = [];
		var modalInstance = null;
		var totalComment = 0;
		var commentLoaded = 0;

		// Modal dialog for post comment {new comment / reply}
		var titleDialog = null;
		var postTextBtn = null;
		var placeHolder = null;
		var actualParentId = null;
		var parentComment = null;

		var textInfoComment = "Load More Comment... ";
		var stillDoAsc = false;
		var latestPointValue = null;

		var service = {
			userTypeNewComment: userTypeNewComment,
			getRemainingChars: getRemainingChars,
			fetchComments: fetchComments,
			afterFetchComments: afterFetchComments,
			loadMoreComment: loadMoreComment,
			afterLoadComment: afterLoadComment,
			getAllComments: getAllComments,
			getMyComments: getMyComments,
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
			isStillDoAsc: isStillDoAsc,
			updatePoint: updatePoint
		}
		angular.extend(this, service);
		this.newComment = "";

		function updatePoint(commentId, isUp) {
			latestPointValue = null;
			var point = _.find(myPoints, function(p) {
				return p.commentId === commentId;
			});
			if (point)
				latestPointValue = point.isUp;
			
			dataservice.updatePoint(commentId, isUp)
				.then(afterUpdatePoint);
		}

		function afterUpdatePoint(resp) {
			if (200 === resp.status) {
				var newPoint = resp.data;
				var oldPoint = null;

				_.each(myPoints, function(p) {
					if (p.id === newPoint.id) {
						oldPoint = {};
						oldPoint.commentId = p.commentId
						oldPoint.isUp = p.isUp;

						p.isUp = newPoint.isUp;
					}
				});

				if (oldPoint === null) {
					myPoints.push(newPoint);
				} 
				
				iterateCommentList(myComments, newPoint, oldPoint);

				iterateCommentList(allComments, newPoint, oldPoint);
			}
		}

		function iterateCommentList(commentList, newPoint, oldPoint) {
			_.each(commentList, function(c) {
				commenthelper.updateCommentAttr(c, myPoints);
				updateCommentPoints(c, newPoint, oldPoint);

				_.each(c.subComment, function(s) {
					commenthelper.updateCommentAttr(s, myPoints);
					updateCommentPoints(s, newPoint, oldPoint);
				});
			});
		}

		function updateCommentPoints(comment, newPoint, oldPoint) {
			if (oldPoint == null) {
				if (newPoint.commentId === comment.id) {
					var latestPoint = comment.points;
					newPoint.isUp ? latestPoint++ : latestPoint--;
					comment.points = latestPoint;
					comment.points = comment.points < 0 ? 0 : comment.points;
				}
			} else if (oldPoint.commentId === comment.id) {
				if (oldPoint.isUp && !comment.isUp) {
					comment.points--;
					comment.points = comment.points < 0 ? 0 : comment.points;
				} else if (!oldPoint.isUp && comment.isUp) {
					comment.points++;
				}				
			}
		}

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

					commenthelper.initCommentObj(c, myPoints);

					var parent = commenthelper.findParentById(c.parentId, myComments);
					if (!parent)
						parent = commenthelper.findParentById(c.parentId, allComments);

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

		function openCommentDialog(parentComment, parentId) {
			this.newComment = "";
			if (parentComment) {
				this.newComment = "@" + parentComment.username + " ";
			}

			initNewComment(parentComment, parentId);

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

		function getMyComments() {
			return myComments;
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
					commenthelper.initCommentObj(c, myPoints);

					// Chek if it is a user comment
					var myComment = _.find(myComments, function(mc) {
						return c.id === mc.id;
					});
					if (myComment) {
						c.hideThis = true;
					} else {
						allComments.push(c);
					}
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
				myComments = resp.data.myComments;
				myPoints = resp.data.myPoints;

				_.each(myComments, function(c) {
					c.myReplies = [];
					commenthelper.initCommentObj(c, myPoints);
				});				

				commentLoaded = 0;
				_.each(allComments, function(c) {
					commentLoaded++;
					c.myReplies = [];
					commenthelper.initCommentObj(c, myPoints);

					// Chek if it is a user comment
					var myComment = _.find(myComments, function(mc) {
						return c.id === mc.id;
					});
					if (myComment) {
						c.hideThis = true;
					}
				});
			}
			deferred.resolve(resp);
		}

		function getRemainingChars() {
			return remainingChars;
		}

		function postNewComment() {
			closeCommentDialog();

			dataservice.createNewComment(matchdayId ,this.newComment, actualParentId)
				.then(afterPostComment);
		}

		function afterPostComment(resp) {
			if (200 === resp.status) {

				var newComment = resp.data;
				commenthelper.initCommentObj(newComment);
				if (actualParentId) {

					var parent = commenthelper.findParentById(actualParentId, myComments);
					if (!parent)
						parent = commenthelper.findParentById(actualParentId, allComments);

					if (!parent.myReplies) 
						parent.myReplies = [];
					parent.myReplies.push(newComment);
				} else {
					newComment.myReplies = [];
					allComments.unshift(newComment);
				}
			}
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

		function initNewComment(pc, parentId) {
			// Set require param to default
			parentComment = null;
			actualParentId = null;
			remainingChars = maxCommentLength;

			parentComment = pc;
			if (parentId)
				actualParentId = parentId;

			// !pc is new comment, and comment is reply a comment it self
			titleDialog = !pc ? "New Comment" : "Reply Comment";
			postTextBtn = !pc ? "Post" : "Reply";
			placeHolder = !pc ? "Write comments...": "Reply comments...";
		}
	}
})();