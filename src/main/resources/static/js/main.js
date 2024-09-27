const toggleBtn = document.querySelector('.navbar__toogleBtn');
const menu = document.querySelector('.navbar__menu');
const icons = document.querySelector('.navbar__menu2');

toggleBtn.addEventListener('click', () => {
    menu.classList.toggle('active');
    icons.classList.toggle('active');
});

document.addEventListener("DOMContentLoaded", function () {
    fetchCartItems();
});

function fetchCartItems() {
    fetch('/cc/cart')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('cart-list');
            tbody.innerHTML = ''; // 기존 내용을 비움
            data.forEach((item, index) => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <th scope="row">${index + 1}</th>
                    <td>${item.productName}</td>
                    <td>${item.category}</td>
                    <td>${item.price}</td>
                    <td>${item.quantity}</td>
                    <td>${new Date(item.createdAt).toLocaleString()}</td>
                    <td>${new Date(item.updatedAt).toLocaleString()}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching cart items:', error));
}