import { fetchReadProducts } from './fetch.js';

document.addEventListener("DOMContentLoaded", function () {
    fetchReadProducts().then(data => {
        console.log(data);
        const div = document.getElementById('product-list');
        div.innerHTML = '';
        data.forEach((item, index) => {
            const productDiv = document.createElement('div');

            productDiv.addEventListener('click', () => getProduct(item.productId));

            const productImg = document.createElement('img');
            productImg.src = `/uploadPath/${item.images[0]}`;
            productImg.alt = '이미지 없음';

            productImg.onerror = function() {
                this.src = `/images/image01.png`

            }

            const productName = document.createElement('p');
            productName.textContent = `${item.pName}`;

            const hr = document.createElement('hr');

            const productPrice = document.createElement('p');
            const price = item.price.toLocaleString();
            productPrice.textContent = `${price}원`;
            productDiv.appendChild(productImg);
            productDiv.appendChild(productName);
            productDiv.appendChild(hr);
            productDiv.appendChild(productPrice);

            div.appendChild(productDiv);

        });
    });
});

function getProduct(productId) {
    window.location.href=`/app/product/${productId}`
}