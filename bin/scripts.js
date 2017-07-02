$(document).ready(function() {
	var username;
	var token;
	var name;
	var signBtns = $(".sign-btns");
	var loginForm = $(".login-form");
	var signupForm = $(".signup-form");

	// Toggle forms

	$(".btn-login-form").click(function() {
		signBtns.hide();
		loginForm.show();
	});

	$(".btn-signup-form").click(function() {
		signBtns.hide();
		signupForm.show();
	});

	$(".btn-cancel").click(function() {
		loginForm.hide();
		signupForm.hide();
		signBtns.show();
		$(".info-lbl").html("");
	});

	// Log In

	$("#login-form").submit(function( event ) {
		event.preventDefault();
		let un = $("#username1").val();
		let ps = $("#password1").val();

    	$.ajax({
			method: "POST",
			url: "/login",
			data: {username: un, password: ps},
			success: function (data) {
	            console.log(data)
	            if(data === "LOGIN FAILED")
	            	$(".info-lbl").html("Username or password are incorrect");
	            else {
	            	let json = JSON.parse(data);
	            	username = un;
	            	token = json.token;
	            	name = json.name;

	            	$(".main").hide();
	            	$("#welcome-lbl").html("Welcome " + name);
	            	$(".home").show();
	            }
	        }
		});
	    return false;
	});

	// Sign Up

	$("#signup-form").submit(function( event ) {
		event.preventDefault();
		let un = $("#username").val();
		let ps = $("#password").val();
		let na = $("#name").val();
		let em = $("#email").val();

    	$.ajax({
			method: "POST",
			url: "/signup",
			data: {username: un, password: ps, name: na, email: em},
			success: function (data) {
	            console.log(data)
	            if(data === "REG FAILED")
	            	$(".info-lbl").html("Sign up failed!");
	            else {
	            	loginForm.hide();
					signupForm.hide();
					signBtns.show();
					$(".info-lbl").html("");
	            }
	        }
		});
	    return false;
	});

	// leaderboard

	$("#gross-profit").click(function() {
		$.ajax({
			method: "POST",
			url: "/leaderboard",
			data: {id: "CASH", username: username, token: token},
			success: function (data) {
	            leaderFunc(data);
	        }
		});
	});

	$("#highest-cash").click(function() {
		$.ajax({
			method: "POST",
			url: "/leaderboard",
			data: {id: "WIN", username: username, token: token},
			success: function(data) {
				leaderFunc(data);
			}
		})
	});

	$("#games-played").click(function() {
		$.ajax({
			method: "POST",
			url: "/leaderboard",
			data: {id: "PLAY", username: username, token: token},
			success: function(data) {
				leaderFunc(data);
			}
		})
	});

	var leaderFunc = function(data) {
		$(".leaders-tbl").html("");
		$(".stats-row").hide();
		$(".leaders-row").show();
		console.log(data);
        let json = JSON.parse(data);
        let leaders = json.leaders;

        let th = "<tr><th>#</th><th>Name</th><th>Email</th><th>Email</th><th>Total Gross Profit</th>"
        th += "<th>Highest Win</th><th>Total Games Played</th><th>League</th></tr>";
        $(".leaders-tbl").html(th);
        for(let i = 0; i < leaders.length; i++) {
        	let name = leaders[i].name;
            let email = leaders[i].email;
            let totalCash = leaders[i].totalCash;
            let highestWin = leaders[i].highestWin;
            let gamesPlayed = leaders[i].gamesPlayed;
            let league = leaders[i].league;

            $(".leaders-tbl").append("<tr>");
            $(".leaders-tbl").append("<td>" + name + "</td>");
            $(".leaders-tbl").append("<td>" + email + "</td>");
            $(".leaders-tbl").append("<td>" + totalCash + "</td>");
            $(".leaders-tbl").append("<td>" + highestWin + "</td>");
            $(".leaders-tbl").append("<td>" + gamesPlayed + "</td>");
            $(".leaders-tbl").append("<td>" + league + "</td>");
            $(".leaders-tbl").append("</tr>");
        }
	};

	$(".btn-signout").click(function() {
    	$.ajax({
			method: "POST",
			url: "/signout",
			data: {username: username, token: token},
			success: function (data) {
	            if(data.split(" ")[1] === "FAILED")
	            	window.alert(data.split(" ")[2]);
	            else {
	            	console.log(data);
	            	username = undefined;
	            	token = undefined;
	            	name = undefined;

	            	$("#welcome-lbl").html("");
	            	$(".home").hide();
	            	$(".main").show();
	            }
	        }
		});
	});

	$(".btn-winstat").click(function() {
		let un = $("#username-stat").val();
		if(un.length === 0 || un === null || un === undefined)
			window.alert("Please enter a username first");
		else {
			$.ajax({
				method: "POST",
				url: "/stats",
				data: {id: "WIN", userstats: un, username: username, token: token},
				success: function (data) {
		            statsFunc(data);
		        }
			});
		}
	});

	$(".btn-grossstat").click(function() {
		let un = $("#username-stat").val();
		if(un.length === 0 || un === null || un === undefined)
			window.alert("Please enter a username first");
		else {
			$.ajax({
				method: "POST",
				url: "/stats",
				data: {id: "GROSS", username: username, token: token},
				success: function (data) {
		            statsFunc(data);
		        }
			});
		}
	});

	var statsFunc = function(data) {

	};
});