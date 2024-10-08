import { fetchCreateOrder, fetchRemoveCartItem, fetchReadCart, fetchUpdateCart, fetchReadMember} from './fetch.js';


document.addEventListener('DOMContentLoaded', function () {
    fetchReadCart(tokenMemberId).then(data => { displayCartData(data)});
})

function displayCartData(cartData) {
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
        const product = item.product;

        const price = product.price.toLocaleString();
        const quantity = item.quantity;
        const totalPrice = (product.price * quantity).toLocaleString();

        row.innerHTML = `
            <td><input type="checkbox" name="option1" value="Option1"></td>
            <td><img src="/uploadPath/${product.images[0]}" alt="이미지 없음1" class="cart_img"
                     onerror="this.onerror=null; this.src='/images/image01.png';"></td>
            <td>${product.pName}</td>
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
            const changeCartData = {
                cartItemId : quantityInputId,
                quantity: quantity
            };
            fetchUpdateCart(changeCartData).then(() =>{
               window.location.href="/app/cart";
            });
        })
        removeCartButton.addEventListener('click', () => fetchRemoveCartItem(quantityInputId).then(()=>window.location.href="/app/cart"));


        cartTable.appendChild(row);

    });

    cartContainer.appendChild(cartTable);

    const totalDiv = document.createElement('div');
    totalDiv.className = 'totalDiv';

    const totalPrice = cartData.totalPrice.toLocaleString();

    totalDiv.innerHTML = `
        <div>총 상품구매금액 : ${totalPrice}원</div>
        <div><button class="add-order-btn">전체상품주문</button></div>
    `;
    const addOrderBtn = totalDiv.querySelector('.add-order-btn');
    addOrderBtn.addEventListener('click', () => {

        cartToOrder(cartData.memberId, cartData.cartItems);
    });

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

function cartToOrder(memberId, cartItems) {
    fetchReadMember(memberId).then(data => {
        const orderData = [];
        cartItems.forEach(item => {
            orderData.push({
                productId: item.product.productId,
                quantity: item.quantity
            });
        });
        const addOrderData = {
            memberId : data.memberId,
            email: data.email,
            address: data.address,
            phoneNumber: data.phoneNumber,
            orderItems: orderData
        };
        fetchCreateOrder(addOrderData).then(()=>{
            const removePromises = cartItems.map(item => fetchRemoveCartItem(item.cartItemId));

            Promise.all(removePromises).then(() => {
                alert('주문완료');
                window.location.href = "/app/mypage";
            });
        });
    });
}
