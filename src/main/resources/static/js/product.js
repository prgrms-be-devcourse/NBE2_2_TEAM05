
export function fetchProducts() {
    fetch('/cc/product/list')
        .then(response => response.json())
        .then(data => {
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
                productName.textContent = `${item.pname}`;

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
        })
        .catch(error => console.error('Error fetching products:', error));
}

export function fetchProduct(productId) {
    fetch(`/cc/product/${productId}`)
        .then(response => response.json())
        .then(data => {
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
                        <h2>${data.pname}</h2><hr>
                        <div>${price}원</div><hr>
                        <div>재고 수량 : ${data.stock}개</div><hr>
                        <div>
                            <p>로그인 구현 전 임시 input</p>
                            <input type="text" id="memberId" placeholder="MemberId 입력">
                        </div><hr>
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
                <div class="product_description">${data.description}</div>
            `;
            const increaseButton = div.querySelector('.quantity-button.increase');
            const decreaseButton = div.querySelector('.quantity-button.decrease');
            const addCartButton =  div.querySelector('.add_cart_button');

            increaseButton.addEventListener('click', () => changeQuantity(1));
            decreaseButton.addEventListener('click', () => changeQuantity(-1));
            addCartButton.addEventListener('click', () => {
                const memberId = document.getElementById('memberId').value;
                const quantity = document.getElementById('quantity').value;
                addCart(memberId,productId, quantity);
            })

        })
        .catch(error => console.error('Error fetching products:', error));
}

function changeQuantity(amount) {
    const quantityInput = document.getElementById('quantity');
    let currentQuantity = parseInt(quantityInput.value);

    currentQuantity += amount;

    if (currentQuantity < 1) {
        currentQuantity = 1;
    }

    quantityInput.value = currentQuantity;
}

function getProduct(productId) {
    window.location.href=`/app/product/${productId}`
}

function addCart(memberId, productId, quantity) {
    fetch('/cc/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            memberId: memberId,
            cartItems: [
                { productId: productId, quantity: quantity}
            ]
        })
    })
        .then(response => {
            if (!response.ok) {
                // 응답이 실패일 경우, 서버에서 반환된 메시지를 파싱하여 출력
                return response.json().then(errorData => {
                    throw new Error(`${response.status}: ${errorData.message}`);
                });
            }
            return response.json();
        })
        .then(data => {
            alert('카트 등록 완료 되었습니다!!');
        })
        .catch(error => {
            alert(`에러발생 : ${error.message}`);
        });
}