var paramObject = {};
 
function 	initialize(params) {
		this.paramObject = params; 
		if (getCookie("username") != null) {
			$("j_username").value = getCookie("username");
			$("j_password").focus();
		} else {
				$("j_username").focus();
		}
}
	
function saveUsername(theForm) {
	    var expires = new Date();
	    expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.        
	    KS.setCookie("username",$("j_username").value,expires, paramObject.url);
}
	
function validateForm(form) {                                                               
	    return validateRequired(form); 
} 
	
function passwordHint() {
		if ($("j_username").value.length == 0) {
		  alert(this.paramObject.requiredUsername);
		  $("j_username").focus();
		} else {
		    location.href=this.paramObject.passwordHintLink + "/" + $("j_username").value;      
		}
}

function required() {   
	    this.aa = new Array("j_username", paramObject.requiredUsername, new Function ("varName", " return this[varName];"));
	    this.ab = new Array("j_password", paramObject.requiredPassword, new Function ("varName", " return this[varName];"));
} 

/* ActiveLabel */

function initOverLabels () {
  if (!document.getElementById) return;

  var labels, id, field;

  // Set focus and blur handlers to hide and show
  // LABELs with 'overlabel' class names.
  labels = document.getElementsByTagName('label');
  for (var i = 0; i < labels.length; i++) {

    if (labels[i].className == 'overlabel') {

      // Skip labels that do not have a named association
      // with another field.
      id = labels[i].htmlFor || labels[i].getAttribute('for');
      if (!id || !(field = document.getElementById(id))) {
        continue;
      }

      // Change the applied class to hover the label
      // over the form field.
      labels[i].className = 'overlabel-apply';

      // Hide any fields having an initial value.
      if (field.value !== '') {
        hideLabel(field.getAttribute('id'), true);
      }

      // Set handlers to show and hide labels.
      field.onfocus = function () {
        hideLabel(this.getAttribute('id'), true);
      };
      field.onblur = function () {
        if (this.value === '') {
          hideLabel(this.getAttribute('id'), false);
        }
      };

      // Handle clicks to LABEL elements (for Safari).
      labels[i].onclick = function () {
        var id, field;
        id = this.getAttribute('for');
        if (id && (field = document.getElementById(id))) {
          field.focus();
        }
      };

    }
  }
};

function hideLabel (field_id, hide) {
  var field_for;
  var labels = document.getElementsByTagName('label');
  for (var i = 0; i < labels.length; i++) {
    field_for = labels[i].htmlFor || labels[i].getAttribute('for');
    if (field_for == field_id) {
      labels[i].style.display = (hide) ? 'none' : 'block';
      return true;
    }
  }
}

window.onload = function () {
  setTimeout(initOverLabels, 50);
};


