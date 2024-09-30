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

document.addEventListener("DOMContentLoaded", function () {
    fetchProducts();
});

function fetchCartItems() {
    fetch('/cc/cart')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const tbody = document.getElementById('cart-list');
            tbody.innerHTML = ''; // 기존 내용을 비움
            data.forEach((item, index) => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${item.cartId}</td>
                    <td>${item.memberId}</td>
                    <td>
                        <li th:each="cartitem : ${item.cartItems}">
                            <a href="" th:text="${cartitem.productId}"></a>
                            <a href="" th:text="${cartitem.quantity}"></a>
                        </li>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching cart items:', error));
}

function fetchProducts() {
    fetch('/cc/product/list')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const div = document.getElementById('product-list');
            div.innerHTML = ''; // 기존 내용을 비움
            data.forEach((item, index) => {
                const productDiv = document.createElement('div');
                productDiv.setAttribute('onclick', `getProduct(${item.productId})`); // getProduct 함수 호출시 id 전달

                const productImg = document.createElement('img');
                productImg.src = `/images/${item.image}`; // 서버에서 제공한 이미지 경로
                productImg.alt = '이미지 없음';

                productImg.onerror = function() {
                    this.src = `/images/image01.png`
                }

                const productName = document.createElement('p');
                productName.textContent = `${item.pname}`; // 서버에서 가져온 상품 이름

                const hr = document.createElement('hr');

                const productPrice = document.createElement('p');
                productPrice.textContent = `${item.price}원`; // 서버에서 가져온 상품 가격
                productDiv.appendChild(productImg);
                productDiv.appendChild(productName);
                productDiv.appendChild(hr);
                productDiv.appendChild(productPrice);

                div.appendChild(productDiv);

            });
        })
        .catch(error => console.error('Error fetching products:', error));
}

function getProduct() {
    window.location.href="/app"
}