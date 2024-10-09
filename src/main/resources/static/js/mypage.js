import { fetchCreateReview, fetchReadOrderMember, fetchReadMember } from './fetch.js';

document.addEventListener("DOMContentLoaded", function () {
    myOrder();
    const menuButtons = document.querySelectorAll('#my-page-menu .mp-menu-btn');
    const firstMenuButton = menuButtons[0];
    firstMenuButton.addEventListener('click', () => { myOrder() });
    const secondMenuButton = menuButtons[1];
    const thirdMenuButton = menuButtons[2];
    thirdMenuButton.addEventListener('click', () => { myInfo() });
});
function myInfo() {
    const menuButtons = document.querySelectorAll('#my-page-menu .mp-menu-btn');
    const thirdMenuButton = menuButtons[2];
    menuButtons.forEach(button => button.classList.remove('active'));
    thirdMenuButton.classList.add('active');

    const div = document.getElementById('my-page-content');
    fetchReadMember(tokenMemberId).then(data => {
        div.innerHTML = `
            <div id="myInfo-div">
            <div><h3>개인 정보 확인/수정</h3></div>
            <div>
                <label class="my-label">아이디</label>
                <input id="inputMemberId" type="text" value="${data.memberId}">
            </div>
            <div>
                <label>비밀번호</label>
                <input id="inputPassword" type="password"">
            </div>
            <div>
                <label>이름</label>
                <input id="inputName" type="text" value="${data.name}">
            </div>
            <div>
                <label>성별</label>
                <input id="inputSex" type="text" value="${data.sex}">
            </div>
            <div>
                <label>주소</label>
                <input id="inputAddress" type="text" value="${data.address}">
            </div>
            <div>
                <label>연락처</label>
                <input id="inputPhoneNumber" type="text" value="${data.phoneNumber}">
            </div>
            <div>
                <label>이메일</label>
                <input id="inputEmail" type="text" value="${data.email}">
            </div>
            <div>
                <label>이미지</label>
                 <img class="mypage-img" src="/uploadPath/${data.image}" alt="이미지 없음1"
                             onerror="this.onerror=null; this.src='/images/image01.png';">
            </div>
            <div>
                <label></label>
                <input id="inputImage" type="file" multiple>
            </div>
            <div>
                <button id="update-member">수정</button>
            </div>
            </div>
        `;




    });

}
function myOrder() {
    const menuButtons = document.querySelectorAll('#my-page-menu .mp-menu-btn');
    const firstMenuButton = menuButtons[0];
    menuButtons.forEach(button => button.classList.remove('active'));
    firstMenuButton.classList.add('active');

    const div = document.getElementById('my-page-content');
    div.innerHTML = '';
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
                        <div>${orderItemData.quantity}개</div>
                        <div><button class="add-review-btn">리뷰작성</button></div>
                    `;
                    totalPrice += orderItemData.product.price * orderItemData.quantity;
                    const addReview = row2.querySelector('.add-review-btn');
                    addReview.addEventListener('click', () => {
                        createReview(orderItemData.product);
                    });
                    orderItem.appendChild(row2);
                });

                const totalDiv = row.querySelector(`#order-total-${item.orderId}`);
                const total = totalPrice.toLocaleString();
                totalDiv.innerHTML = `<div>총가격 : ${total}원</div>`;

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
}

function createReview(product) {
    const div = document.getElementById('my-page-content');
    div.innerHTML = '';
    const orderList = document.createElement('h3');
    orderList.innerHTML = '리뷰작성';
    div.appendChild(orderList);

    const reviewDiv = document.createElement('div');
    reviewDiv.className = 'board-input-div';
    reviewDiv.innerHTML = `
        <div>
            <label>상품번호</label>
            <div>${product.productId}</div>
        </div>
        <div>
            <label>상품명</label>
            <div>${product.pName}</div>
        </div>
        <div>
            <label>별점</label>
            <select id="star-select">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
                <option value="9">9</option>
                <option value="10">10</option>
            </select>
        </div>
        <div>
            <label>리뷰</label>
            <textarea id="review-text"></textarea>
        </div>
        <div>
            <label></label>
            <button id="add-review-btn">작성</button>
            <button id="cancel-review-btn">취소</button>
        </div>
    `;

    const addReview = reviewDiv.querySelector('#add-review-btn');
    addReview.addEventListener('click', () => {
        const selectElement = reviewDiv.querySelector('#star-select');
        const selectedStar = selectElement.value;

        const reviewData = {
            content: document.getElementById('review-text').value,
            star: selectedStar,
            memberId: tokenMemberId,
            productId: product.productId
        };
        fetchCreateReview(reviewData).then(()=>{
            alert('리뷰 등록 완료');
            window.location.href="/app/mypage";
        });
    });

    div.appendChild(reviewDiv);



}