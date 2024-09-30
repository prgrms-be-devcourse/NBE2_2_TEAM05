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
        </tr>
    `;

    cartData.cartItems.forEach(item => {
        const row = document.createElement('tr');
        const price = item.productPrice.toLocaleString();
        const quantity = item.quantity;
        const totalPrice = (item.productPrice * quantity).toLocaleString();
        row.innerHTML = `
            <td><input type="checkbox" name="option1" value="Option1"></td>
            <td><img src="/uploadPath/${item.filename}" alt="이미지 없음1" class="cart_img"
                     onerror="this.onerror=null; this.src='/images/image01.png';"></td>
            <td>${item.productName}</td>
            <td>${price}원</td>
            <td><input type="text" value='${quantity}'><button>변경</button></td>
            <td>${totalPrice}원</td>
        `;
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
