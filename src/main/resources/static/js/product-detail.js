import {fetchReadProduct, fetchCreateCart, fetchReadReview, fetchReadImage} from './fetch.js';

document.addEventListener("DOMContentLoaded", function () {
    const pathArray = window.location.pathname.split('/');
    const productId = pathArray[pathArray.length - 1];
    if (productId) {
        fetchReadProduct(productId).then(data => {
            console.log(data);
            const div = document.getElementById('product');
            const price = data.price.toLocaleString();
            div.innerHTML = `
                <div class="productDiv">
                    <div class="content_prod_image">
                        <img src="/uploadPath/${data.images[0]}" alt="이미지 없음1"
                             onerror="this.onerror=null; this.src='/images/image01.png';">
                    </div>
                    <div class="content_prod_info">
                        <h2>${data.pName}</h2><hr>
                        <div>${price}원</div><hr>
                        <div>재고 수량 : ${data.stock}개</div><hr>
                        <div class="add_cart_div">
                            <div class="quantity_div">
                                <input type="text" id="quantity" value="1" readonly>
                                <div>
                                    <button class="quantity-button increase" >▲</button>
                                    <button class="quantity-button decrease" >▼</button>
                                </div>   
                            </div>
                            <button class="add_cart_button">장바구니 담기</button>
                        </div>
                    </div>
         
                </div>
                <div class="product_description">${data.description}</div><hr>
                <div class="review-div"></div>
            `;
            const increaseButton = div.querySelector('.quantity-button.increase');
            const decreaseButton = div.querySelector('.quantity-button.decrease');
            const addCartButton =  div.querySelector('.add_cart_button');
            const reviewContainer = div.querySelector('.review-div');

            increaseButton.addEventListener('click', () => changeQuantity(1));
            decreaseButton.addEventListener('click', () => changeQuantity(-1));
            addCartButton.addEventListener('click', () => {
                const quantity = document.getElementById('quantity').value;
                if(quantity > data.stock) {
                    alert('재고 부족!!');
                    window.location.href = `/app/product/${productId}`;
                } else {
                    fetchCreateCart(tokenMemberId,productId, quantity);
                }
            })
            fetchReadReview(data.productId).then(data => {
                data.content.forEach(review => {
                    const row = document.createElement('div');
                    row.innerHTML = `
                        <div>
                            <div id="review-${review.memberId}" class="image-td">${review.memberId} - ${review.star}점</div>
                        </div>
                        <div>
                            <div>${review.content}</div>
                        </div>
                    `;
                    fetchReadImage(review.memberId)
                        .then(imgTag => {
                            const memberCell = row.querySelector(`#review-${review.memberId}`);
                            memberCell.innerHTML = `${imgTag} ${review.memberId} - ${review.star}점`;
                        })
                        .catch(error => {
                            console.error('Error loading image:', error);
                        });
                    reviewContainer.appendChild(row);
                });
            });

        });
    }
});

function changeQuantity(amount) {
    const quantityInput = document.getElementById('quantity');
    let currentQuantity = parseInt(quantityInput.value);

    currentQuantity += amount;

    if (currentQuantity < 1) {
        currentQuantity = 1;
    }

    quantityInput.value = currentQuantity;
}

