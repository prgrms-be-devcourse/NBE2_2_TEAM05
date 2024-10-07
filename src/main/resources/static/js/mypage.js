import { fetchReadOrderMember } from './fetch.js';

document.addEventListener("DOMContentLoaded", function () {
    const menuButtons = document.querySelectorAll('#my-page-menu .mp-menu-btn');
    const firstMenuButton = menuButtons[0];
    menuButtons.forEach(button => button.classList.remove('active'));
    firstMenuButton.classList.add('active');

    const div = document.getElementById('my-page-content');
    const orderList = document.createElement('h3');
    orderList.innerHTML = '주문목록';
    const hrTag = document.createElement('hr');
    div.appendChild(orderList);
    div.appendChild(hrTag);

    let currentPage = 1;
    const itemsPerPage = 3;

    let paginationDiv;

    fetchReadOrderMember(tokenMemberId).then(data => {
        let totalPages = Math.ceil(data.length / itemsPerPage);

        function updatePage(page) {
            currentPage = page;

            const existingOrderDivs = document.querySelectorAll('.order-div');
            existingOrderDivs.forEach(div => div.remove());

            const start = (page - 1) * itemsPerPage;
            const end = start + itemsPerPage;
            const currentItems = data.slice(start, end);

            currentItems.forEach(item => {
                const row = document.createElement('div');
                row.className = 'order-div';

                let date = new Date(item.createdAt);
                let year = date.getFullYear();
                let month = String(date.getMonth() + 1).padStart(2, '0');
                let day = String(date.getDate()).padStart(2, '0');
                let formattedDate = `${year}.${month}.${day}`;

                row.innerHTML = `
                    <div>
                        <div class="order-header">
                            <div>${formattedDate}</div>
                            <div>주문상태 : ${item.status}</div>
                        </div><hr>
                        <div class="order-main">
                            <div>배송지 : ${item.address}</div>
                            <div>연락처 : ${item.phoneNumber}</div>
                        </div><hr>
                        <div id="order-items-${item.orderId}"></div><hr>
                        <div id="order-total-${item.orderId}"></div>
                    <div>
                `;

                let totalPrice = 0;

                const orderItem = row.querySelector(`#order-items-${item.orderId}`);
                item.orderItems.forEach(orderItemData => {
                    const row2 = document.createElement('div');
                    row2.className = 'order-item-products';
                    row2.innerHTML = `
                        <div>${orderItemData.product.pName}</div>
                        <div>${orderItemData.product.stock}개</div>
                        <div><button>리뷰작성</button></div>
                    `;
                    totalPrice += orderItemData.product.price * orderItemData.product.stock;
                    orderItem.appendChild(row2);
                });

                const totalDiv = row.querySelector(`#order-total-${item.orderId}`);
                totalDiv.innerHTML = `<div>총가격 : ${totalPrice}원</div>`;

                div.appendChild(row);
            });

            if (paginationDiv) {
                paginationDiv.remove();
            }

            updatePagination(totalPages);
        }

        function updatePagination(totalPages) {
            paginationDiv = document.createElement('div');
            paginationDiv.id = 'pagination';

            for (let i = 1; i <= totalPages; i++) {
                const pageBtn = document.createElement('button');
                pageBtn.innerHTML = i;
                if (i === currentPage) {
                    pageBtn.disabled = true; // 현재 페이지 버튼 비활성화
                }
                pageBtn.addEventListener('click', () => updatePage(i));
                paginationDiv.appendChild(pageBtn);
            }

            div.appendChild(paginationDiv);
        }

        updatePage(currentPage);
    });
});

// document.addEventListener("DOMContentLoaded", function () {
//     const menuButtons = document.querySelectorAll('#my-page-menu .mp-menu-btn');
//     const firstMenuButton = menuButtons[0];
//     menuButtons.forEach(button => button.classList.remove('active'));
//     firstMenuButton.classList.add('active');
//
//
//     const div = document.getElementById('my-page-content');
//     const orderList = document.createElement('h3');
//     orderList.innerHTML = '주문목록';
//     const hrTag = document.createElement('hr');
//     div.appendChild(orderList);
//     div.appendChild(hrTag);
//
//     fetchReadOrderMember(tokenMemberId).then(data => {
//         data.forEach(item => {
//             const row = document.createElement('div');
//             row.className = 'order-div';
//
//             let date = new Date(item.createdAt);
//             let year = date.getFullYear();
//             let month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
//             let day = String(date.getDate()).padStart(2, '0');
//             let formattedDate = `${year}.${month}.${day}`;
//
//             row.innerHTML = `
//                 <div>
//                     <div class="order-header">
//                         <div>${formattedDate}</div>
//                         <div>주문상태 : ${item.status}</div>
//                     </div><hr>
//                     <div class="order-main">
//                         <div>배송지 : ${item.address}</div>
//                         <div>연락처 : ${item.phoneNumber}</div>
//                     </div><hr>
//                     <div id="order-items-${item.orderId}">
//                     </div><hr>
//                     <div id="order-total-${item.orderId}">
//                     </div>
//                 <div>
//             `;
//             let totalPrice = 0;
//
//             const orderItem = row.querySelector(`#order-items-${item.orderId}`);
//             item.orderItems.forEach(item => {
//                 const row2 = document.createElement('div');
//
//                 row2.className = 'order-item-products'
//                 row2.innerHTML = `
//                     <div>
//                         ${item.product.pName}
//                     </div>
//                     <div>
//                         ${item.product.stock}개
//                     </div>
//                     <div>
//                         <button>리뷰작성</button>
//                     </div>
//                 `;
//                 totalPrice += item.product.price * item.product.stock;
//                 orderItem.appendChild(row2);
//             })
//             const totalDiv = row.querySelector(`#order-total-${item.orderId}`);
//             totalDiv.innerHTML = `<div>총가격 : ${totalPrice}원</div>`;
//
//
//             div.appendChild(row);
//         });
//     })
// })