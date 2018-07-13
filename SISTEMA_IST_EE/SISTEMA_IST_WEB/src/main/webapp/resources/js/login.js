function logar(){
	
	$.ajax({
	    url: "${pageContext.request.contextPath}/j_spring_security_check",
	    type: "POST",
	    data: $("#formMenu").serialize(),
	    beforeSend: function (xhr) {
	        xhr.setRequestHeader("X-Ajax-call", "true");
	    },
	    complete: function(jqXHR, textStatus){
			if (jqXHR.statusText == "OK"){
				var formulario = document.getElementById("formMenu");
				
			   formulario.action = "${pageContext.request.contextPath}/index.jsf";
			   formulario.submit();

			}else{
				
				location.href="${pageContext.request.contextPath}/loginFail.jsp";
			}
		}
	});
}