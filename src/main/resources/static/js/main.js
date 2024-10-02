import { fetchProduct, fetchProducts } from "./product.js";
import { displayCartData } from "./cart.js";
const toggleBtn = document.querySelector('.navbar__toogleBtn');
const menu = document.querySelector('.navbar__menu');
const icons = document.querySelector('.navbar__menu2');

toggleBtn.addEventListener('click', () => {
    menu.classList.toggle('active');
    icons.classList.toggle('active');
});

document.addEventListener("DOMContentLoaded", function () {
    fetchProducts();
});

// document.addEventListener("DOMContentLoaded", function () {
//     fetchProductList(1, 10);
// });


document.addEventListener("DOMContentLoaded", function () {
    const pathArray = window.location.pathname.split('/');
    const productId = pathArray[pathArray.length - 1];
    if (productId) {
        fetchProduct(productId);
    }
});

document.getElementById('fetch-cart-btn').addEventListener('click', function() {
    const memberId = document.getElementById('memberId').value; // 입력 필드의 값을 가져옴

    // 서버에 fetch 요청을 보냄
    fetch(`/cc/cart/${memberId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('카트 정보를 불러오지 못했습니다.');
            }
            return response.json(); // 서버 응답을 JSON으로 변환
        })
        .then(data => {
            // 데이터를 화면에 표시
            console.log(data)
            displayCartData(data);
        })
        .catch(error => {
            console.error('Error fetching cart:', error);
            alert('카트 정보를 불러오는 중 오류가 발생했습니다.');
        });
});




