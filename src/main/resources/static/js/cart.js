// export function fetchCartItems() {
//     fetch('/cc/cart')
//         .then(response => response.json())
//         .then(data => {
//             console.log(data);
//             const tbody = document.getElementById('cart-list');
//             tbody.innerHTML = '';
//             data.forEach((item, index) => {
//                 const row = document.createElement('tr');
//
//                 row.innerHTML = `
//                     <td>${item.cartId}</td>
//                     <td>${item.memberId}</td>
//                     <td>
//                         <li th:each="cartitem : ${item.cartItems}">
//                             <a href="" th:text="${cartitem.productId}"></a>
//                             <a href="" th:text="${cartitem.quantity}"></a>
//                         </li>
//                     </td>
//                 `;
//                 tbody.appendChild(row);
//             });
//         })
//         .catch(error => console.error('Error fetching cart items:', error));
// }

export function displayCartData(cartData) {
    const cartContainer = document.getElementById('cart-container');
    cartContainer.innerHTML = '';

    if (!cartData || cartData.length === 0) {
        cartContainer.innerHTML = '<p>카트에 담긴 상품이 없습니다.</p>';
        return;
    }

    const cartTable = document.createElement('table');
    cartTable.className = 'cart_table';

    cartTable.innerHTML = `
        <tr>
            <th><input type="checkbox" name="option1" value="Option1"></th>
            <th>이미지</th>
            <th>상품명</th>
            <th>판매가</th>
            <th>수량</th>
            <th>합계</th>
            <th></th>
        </tr>
    `;

    cartData.cartItems.forEach(item => {
        const row = document.createElement('tr');
        row.id = `cart-item-${item.cartItemId}`;

        const price = item.productPrice.toLocaleString();
        const quantity = item.quantity;
        const totalPrice = (item.productPrice * quantity).toLocaleString();

        row.innerHTML = `
            <td><input type="checkbox" name="option1" value="Option1"></td>
            <td><img src="/uploadPath/${item.filename}" alt="이미지 없음1" class="cart_img"
                     onerror="this.onerror=null; this.src='/images/image01.png';"></td>
            <td>${item.productName}</td>
            <td>${price}원</td>
            <td>
                <div class="change_quantity_div">
                    <div class="quantity_div">
                        <input type="text" id='${item.cartItemId}' value='${quantity}' readonly>
                        <div>
                            <button class="quantity2-button increase" >▲</button>
                            <button class="quantity2-button decrease" >▼</button>
                        </div>   
                    </div>
                    <button class="cart_change_button">변경</button>
                </div>
               
            </td>
            <td>${totalPrice}원</td>
            <td><button class="remove_button">제거</button></td>
        `;
        const increaseButton = row.querySelector('.quantity2-button.increase');
        const decreaseButton = row.querySelector('.quantity2-button.decrease');
        const quantityInputId = item.cartItemId;
        const changeCartButton =  row.querySelector('.cart_change_button');
        const removeCartButton = row.querySelector('.remove_button');

        increaseButton.addEventListener('click', () => changeQuantity2(1, quantityInputId));
        decreaseButton.addEventListener('click', () => changeQuantity2(-1, quantityInputId));
        changeCartButton.addEventListener('click', () => {
            let quantity = document.getElementById(quantityInputId).value;
            changeCart(quantityInputId, quantity);
        })
        removeCartButton.addEventListener('click', () => removeCartItem(quantityInputId));


        cartTable.appendChild(row);

    });

    cartContainer.appendChild(cartTable);

    const totalDiv = document.createElement('div');
    totalDiv.className = 'totalDiv';

    const totalPrice = cartData.totalPrice.toLocaleString();

    totalDiv.innerHTML = `
        <div>총 상품구매금액 : ${totalPrice}원</div>
        <div><button>전체상품주문</button></div>
    `;

    cartContainer.appendChild(totalDiv);

}

function changeQuantity2(amount, id) {
    const quantityInput2 = document.getElementById(id);
    let currentQuantity = parseInt(quantityInput2.value);

    currentQuantity += amount;

    if (currentQuantity < 1) {
        currentQuantity = 1;
    }

    quantityInput2.value = currentQuantity;
}

function changeCart(id, quantity){
    const data = {
        cartItemId : id,
        quantity: quantity
    };

    fetch(`/cc/cart/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if(!response.ok) {
            throw new Error('에러발생!!')
        }
        return response.json();
    }).then(responseData => {
        alert('카트 수정 완료 되었습니다!!')

    }).catch(error => {
        console.error('Error updating cart:', error);
    });
}

function removeCartItem(id) {
    fetch(`/cc/cart/cartItem/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            alert("삭제 완료!!");
            const cartItemRow = document.getElementById(`cart-item-${id}`); // 각 아이템을 구분할 ID 설정 필요
            if (cartItemRow) {
                cartItemRow.remove();
            }
        })
        .catch(error => {
            console.error('Error removing cart item:', error);
        });
}
