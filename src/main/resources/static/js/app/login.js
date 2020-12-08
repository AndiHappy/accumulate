$("#logInbtn").click(
function(event){
	 $(".login-loading").removeClass("d-none");
     $.ajax({
         type: "POST",
         url: "/passport/signin",
         data: $("#inputPasswordm"),$("#inputEmail")
         dataType: "json",
         success: function (json) {
             $(".login-loading").addClass("hide");
             if (json.status == 200) {
                 window.location.href = "/";
             }else{
                 $.alert.error(json.message);
                 $("#img-kaptcha").attr("src", '/getKaptcha?time=' + new Date().getTime());
             }
         }
     });
 });