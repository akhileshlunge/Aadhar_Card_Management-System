$(document).ready(function() {
	var path = window.location.href;
	var arr = path.split("/");
	
	if (arr[arr.length - 1] == "dashboard") {
		$("#dash").addClass("active");
	}
	if (arr[arr.length - 1] == "addaadharpage") {
		$("#agency1,#agency11").addClass("active");
		$("#collapseTwo").addClass("show");
	}
	if (arr[arr.length - 1] == "updateaadharpage" || arr[arr.length - 2] == "updateaadhar") {
		$("#agency1,#agency12").addClass("active");
		$("#collapseTwo").addClass("show");
	}
	if (arr[arr.length - 1] == "deleteaadharpage") {
		$("#agency1,#agency13").addClass("active");
		$("#collapseTwo").addClass("show");
	}
	if(arr[arr.length - 1] == "aadharlist"){
		$("#agencylist").addClass("active");
	}	
	if(arr[arr.length - 1] == "policylist"){
		$("#policylist").addClass("active");
	}
	if(arr[arr.length - 1] == "userupdatelist" ||  arr[arr.length - 2] == "updateuser"){
		$("#manageUser").addClass("active");
	}
});