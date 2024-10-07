function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

let tokenMemberId;

document.addEventListener("DOMContentLoaded", function() {
    const jwtToken = localStorage.getItem("jwtToken");

    if (jwtToken) {
        const decodedToken = parseJwt(jwtToken);
        tokenMemberId = decodedToken.memberId; //
        console.log("Member ID:", tokenMemberId);
    } else {
        window.location.href = "/login";
    }
});





