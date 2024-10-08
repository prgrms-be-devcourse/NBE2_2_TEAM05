document.addEventListener('DOMContentLoaded', function () {
    const toggleBtn = document.querySelector('.navbar__toogleBtn');
    const menu = document.querySelector('.navbar__menu');
    const icons = document.querySelector('.navbar__menu2');

    const jwtToken = localStorage.getItem('jwtToken');

    if (jwtToken) {
        icons.innerHTML = `
            <li>${tokenMemberId}</li>
            <li><a href="/app/cart">장바구니</a></li>
            <li><a href="#" id="logout-btn">로그아웃</a></li>
        `;

        const logoutBtn = document.getElementById('logout-btn');
        logoutBtn.addEventListener('click', function (event) {
            event.preventDefault();
            localStorage.removeItem('jwtToken');
            window.location.href ='/login';
        });
    } else {
        icons.innerHTML = `
            <li><a href="/login">로그인</a></li>
            <li><a href="/signup">회원가입</a></li>
        `;
    }

    if (toggleBtn && menu && icons) {
        toggleBtn.addEventListener('click', () => {
            menu.classList.toggle('active');
            icons.classList.toggle('active');
            myMenu.classList.toggle('active');
        });
    } else {
        console.error('Navbar elements not found.');
    }
});
