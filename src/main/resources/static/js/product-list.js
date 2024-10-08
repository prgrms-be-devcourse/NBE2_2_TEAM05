import { fetchReadProducts, fetchReadProductPage, fetchReadProductSearch } from './fetch.js';

// document.addEventListener("DOMContentLoaded", function () {
//     fetchReadProductPage().then(data => {
//         const div = document.getElementById('product-list');
//         div.innerHTML = '';
//         data.content.forEach((item, index) => {
//             const productDiv = document.createElement('div');
//
//             productDiv.addEventListener('click', () => getProduct(item.productId));
//
//             const productImg = document.createElement('img');
//             productImg.src = `/uploadPath/${item.pimage}`;
//             productImg.alt = '이미지 없음';
//
//             productImg.onerror = function() {
//                 this.src = `/images/image01.png`
//
//             }
//
//             const productName = document.createElement('p');
//             productName.textContent = `${item.pName}`;
//
//             const hr = document.createElement('hr');
//
//             const productPrice = document.createElement('p');
//             const price = item.price.toLocaleString();
//             productPrice.textContent = `${price}원`;
//             productDiv.appendChild(productImg);
//             productDiv.appendChild(productName);
//             productDiv.appendChild(hr);
//             productDiv.appendChild(productPrice);
//
//             div.appendChild(productDiv);
//
//         });
//     });
// });
document.addEventListener("DOMContentLoaded", function () {
    let currentPage = 1;  // 기본 1페이지로 설정

    // 페이지 버튼 생성 함수
    function createPagination(totalPages, currentPage) {
        const paginationDiv = document.getElementById('pagination');
        paginationDiv.innerHTML = ''; // 기존 버튼들 제거

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.classList.add('page-button');
            if (i === currentPage) {
                pageButton.classList.add('active'); // 현재 페이지는 강조
            }

            pageButton.addEventListener('click', () => {
                loadPage(i);
            });

            paginationDiv.appendChild(pageButton);
        }
    }

    // 페이지 로드 함수
    function loadPage(page) {
        fetchReadProductPage(page).then(data => {
            const div = document.getElementById('product-list');
            div.innerHTML = '';
            data.content.forEach((item) => {
                const productDiv = document.createElement('div');
                productDiv.addEventListener('click', () => getProduct(item.productId));

                const productImg = document.createElement('img');
                productImg.src = `/uploadPath/${item.pimage}`;
                productImg.alt = '이미지 없음';
                productImg.onerror = function() {
                    this.src = `/images/image01.png`;
                };

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

            // API에서 받아온 totalPages로 페이지네이션 버튼 생성
            createPagination(data.totalPages, page);
        });
    }

    // 초기 1페이지 로드
    loadPage(currentPage);
});

const searchBtn = document.getElementById('search-btn');
searchBtn.addEventListener('click', () => {
    const searchString = document.getElementById('search-input').value;
    const paginationDiv = document.getElementById('pagination');

    if (searchString) {
        paginationDiv.style.display = 'none';
        fetchReadProductSearch(searchString).then( data => {
            const div = document.getElementById('product-list');
            div.innerHTML = '';
            data.forEach((item) => {
                const productDiv = document.createElement('div');
                productDiv.addEventListener('click', () => getProduct(item.productId));

                const productImg = document.createElement('img');
                productImg.src = `/uploadPath/${item.pimage}`;
                productImg.alt = '이미지 없음';
                productImg.onerror = function() {
                    this.src = `/images/image01.png`;
                };

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
    } else {
        window.location.href=`/app/product`;
    }
});

function getProduct(productId) {
    window.location.href=`/app/product/${productId}`;
}