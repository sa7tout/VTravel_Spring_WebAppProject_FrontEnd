function bookVoyage() {
    // Check if the user is signed in
    var sessionToken = sessionStorage.getItem('sessionToken') || localStorage.getItem('sessionToken');
    if (sessionToken) {
        // Validate the session token
        $.ajax({
            url: 'http://localhost:8080/api/customers/validate-session',
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + sessionToken
            },
            success: function(response) {
                // Session is valid, proceed with booking
                document.getElementById('sessionToken').value=sessionToken;
                document.getElementById('bookForm').submit();
            },
            error: function(xhr, status, error) {
                $('#loginOverlay').toggle();
            }
        });
    } else {
        $('#loginOverlay').toggle();
    }
}