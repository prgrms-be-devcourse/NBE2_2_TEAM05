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
        const role = decodedToken.role;
        if (role.includes("ROLE_ADMIN")) {
            window.location.href = "/app/admin";
        }
    } else {
        window.location.href = "/login";
    }
});

function formatLocalDateTime(dateTimeString) {
    const date = new Date(dateTimeString);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`;
}





