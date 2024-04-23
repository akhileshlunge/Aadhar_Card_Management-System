$(document).ready(function() {
  	 $('#mySwitch1').change(function() {
       var baseurl = document.location.origin + "/updateauthentication/";
       var userId =  $('#userId').val();
          if(this.checked){
          	 if(confirm("Are you sure you want to enable this?")){
			    $.ajax({
			        url: baseurl+userId+"/true",
			        type: 'GET',
			        success: function(res) {
			            console.log(res);
			               },
			        error: function(res){
			       		 console.log(res);
			        }
			    });
          	 } else{
          	 	 $('#mySwitch1').prop('checked', false);
          	 }
          
          } else {
          	if(confirm("Are you sure you want to disable this?")){
          	 	$.ajax({
			        url: baseurl+userId+"/false",
			        type: 'GET',
			        success: function(res) {
			            console.log(res);
			               },
			        error: function(res){
			       		 console.log(res);
			        }
			    });
          	 } else{
          	 	 $('#mySwitch1').prop('checked', true);
          	 }
          	
          }
                     
    });
  	 
  	 
  	$("#btnChange").click(function(){
		 var baseurl1 = document.location.origin + "/updatepassword";
		 var userId =  $('#userId').val();
		 
		 var pass1 = $("#passInput1").val();
		 var pass2 = $("#passInput2").val();
		 $("#changemsg").text("");
		if(pass1.length == 0 && pass2.length == 0){
			$("#changemsg").text("Please enter the password.");
		} else if(pass1 != pass2){
			$("#changemsg").text("Password missmath.");
		} else if(pass1 == pass2){
			var str = {
					id:userId,
					password:pass1
			};
			
			$.ajax({
			    type:"post",
			    data:str,
			    url:baseurl1,
			    dataType: "json",
			    success: function(result){
			    	$("#changemsg").text("Password change successfully.");
			    },
			    error: function (result) {
			    	$("#changemsg").text("Password change failed.");
				}
			});
			
			
		} else {
			$("#changemsg").text("Something went wrong.");
		}
		
	
	});
  	 
  	 
  	 
  	 
  	 
  	  	 
  	$("#cmd").click(function () {
  	    $("#imagetocreate").print();
  	});
    
});

 function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#blah').attr('src', e.target.result).width(120).height(
							100);
				};

				reader.readAsDataURL(input.files[0]);
			}
		}
 
 