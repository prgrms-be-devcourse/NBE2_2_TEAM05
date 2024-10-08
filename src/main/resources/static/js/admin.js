import {
    fetchUpdateOrder,
    fetchReadOrders,
    fetchCreateBoard,
    fetchUpdateBoard,
    fetchDlBoardImage,
    fetchDeleteProduct,
    fetchDeleteProductImage,
    fetchUploadProductImage,
    fetchCreateProduct,
    fetchReadProduct,
    fetchReadProductPage,
    fetchUpdateProduct,
    fetchUpBoardImage, fetchReadBoard, fetchDeleteBoard, fetchReadBoards, fetchReadImage
} from './fetch.js';

function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

let tokenMemberId;

document.addEventListener("DOMContentLoaded", function() {
    const jwtToken = localStorage.getItem("jwtToken");

    if (jwtToken) {
        const decodedToken = parseJwt(jwtToken);
        tokenMemberId = decodedToken.memberId;
        const role = decodedToken.role;

        console.log("Member ID:", tokenMemberId);
        console.log("Roles:", role);

        if (!role.includes("ROLE_ADMIN")) {
            alert("관리자가 아닙니다.");
            window.location.href = "/app";
        }
    } else {
        window.location.href = "/login";
    }
});
const logoutBtn = document.getElementById('logout-btn');
logoutBtn.addEventListener('click', function (event) {
    event.preventDefault();
    localStorage.removeItem('jwtToken');
    window.location.href ='/login';
});


const dashboardMenu = document.querySelector('#nav_dashboard');
const memberMenu = document.querySelector('#nav_member');
const productMenu = document.querySelector('#nav_product');
const orderMenu = document.querySelector('#nav_order');
const boardMenu = document.querySelector('#nav_board');

dashboardMenu.addEventListener('click', () => {
    addDashboard();
});

// memberMenu.addEventListener('click', () => {
//     addMember();
// });

productMenu.addEventListener('click', () => {
    fetchReadProductPage().then(data => {
        addProduct(data);
    });
});

orderMenu.addEventListener('click', () => {
    fetchReadOrders().then(data => {
        addOrder(data);
    })
});

boardMenu.addEventListener('click', () => {
    fetchReadBoards().then(data => {
        addBoard(data);
    });
});

document.addEventListener("DOMContentLoaded", function () {
    addDashboard();
});

function addDashboard() {
    const adminContent = document.getElementById('admin_content');
    const activeMenu = document.getElementsByClassName('hover-active');
    const navDashboard = document.getElementById('nav_dashboard');

    for (let i = 0; i < activeMenu.length; i++) {
        activeMenu[i].classList.remove('hover-active');
    }

    navDashboard.classList.add('hover-active');

    adminContent.innerHTML = `
        <div class="admin_board_div1">
            <h3>Dashboard</h3>
        </div>
        <div class="admin_dashboard">
            <div>대시보드가 들어갈 예정입니다...</div>
            
        </div>
    `;
}

function addMember() {
    const adminContent = document.getElementById('admin_content');
    const activeMenu = document.getElementsByClassName('hover-active');
    const navMember = document.getElementById('nav_member');

    for (let i = 0; i < activeMenu.length; i++) {
        activeMenu[i].classList.remove('hover-active');
    }

    navMember.classList.add('hover-active');

    adminContent.innerHTML = `
        <h3>멤버관리</h3>
        <div>멤버관리가 들어갈 예정 입니다!!</div>
    `;
}

function addProduct(data) {
    const adminContent = document.getElementById('admin_content');
    const activeMenu = document.getElementsByClassName('hover-active');
    const navProduct = document.getElementById('nav_product');

    for (let i = 0; i < activeMenu.length; i++) {
        activeMenu[i].classList.remove('hover-active');
    }

    navProduct.classList.add('hover-active');

    adminContent.innerHTML = `
        <div class="admin_product_div1">
            <h3>상품 관리</h3>
        </div>
        <table class="table1">
            <thead id="admin-product-thead">
            </thead>
            <tbody id="admin-product-tbody">
            </tbody>
        </table>
        <div id="pagination"></div>
    `;
    const thead = document.getElementById('admin-product-thead');
    thead.innerHTML = '';
    const row2 = document.createElement('tr');
    row2.innerHTML = `
        <th>productId</th>
        <th>상품명</th>
        <th>가격</th>
        <th>재고수량</th>
        <th id=""><button class="add-product-button">상품 추가</button></th>
    `;
    const addProductBtn = row2.querySelector('.add-product-button');
    addProductBtn.addEventListener('click', () => createProduct());
    thead.appendChild(row2);

    const tbody= document.getElementById('admin-product-tbody');
    tbody.innerHTML = '';
    data.content.forEach(product => {
        const row = document.createElement('tr');
        row.id = `product-${product.productId}`;
        row.innerHTML =`
                <td>${product.productId}</td>
                <td>${product.pName}</td>
                <td>${product.price}</td>
                <td>${product.stock}</td>
                <td>
                    <button class="detail-button">상세</button>
                    <button class="delete-product-btn">삭제</button>
                </td>
        `;
        const detailButton = row.querySelector('.detail-button');
        const deleteProdBtn = row.querySelector('.delete-product-btn');
        detailButton.addEventListener('click', () => detailProduct(product.productId));
        deleteProdBtn.addEventListener('click', () => fetchDeleteProduct(product.productId));

        tbody.appendChild(row);
    });
    renderPaginationProduct(data);
}

function detailProduct(id) {
    fetchReadProduct(id)
        .then(data =>{
            const adminContent = document.getElementById('admin_content');
            adminContent.innerHTML='';

            const row = document.createElement('div');
            row.className = 'detail-product';

            row.innerHTML = `                
                    <div >
                        <div><h3>상품 상세 보기</h3></div>
                        <div class="detail-product-hDiv">productId: ${data.productId}</div>
                    </div><hr>
                    <div>
                        <label>상품명</label>
                        <input type="text" id="productName" value="${data.pName}">
                    </div><hr>
                    <div>
                        <label>가격</label>
                        <input type="text" id="productPrice" value="${data.price}">
                    </div><hr>
                    <div>
                        <label>재고 수량</label>
                        <input type="text" id="productStock" value="${data.stock}">
                    </div><hr>
                    <div>
                        <label>상품 설명</label>
                        <textarea id="productDescription" class="description-input">${data.description}</textarea>
                    </div><hr>
                    <div>
                        <label>이미지</label>
                        <div class="d-p-d-i" id="imageContainer-${data.productId}"></div>
                    </div>
                    <div>
                        <label></label>
                        <div class="d-p-d-i">
                            <div><input type="file" id="update-image"></div>
                        </div>
                    </div><hr>
                    <div>
                        <label>생성 일자</label>
                        <div class="d-p-d-d">${data.createdAt}</div>
                    </div><hr>
                    <div>
                        <label>수정 일자</label>
                        <div class="d-p-d-d">${data.updatedAt}</div>
                    </div><hr>
                    <div class="detail-product-buttonDiv">
                        <button class="product-update-button">수정</button>
                        <button class="update-cancel">뒤로</button>
                    </div>
            `;

            adminContent.appendChild(row);

            const imageContainer = document.getElementById(`imageContainer-${data.productId}`);
            if(data.images.length > 0 ) {
                data.images.forEach(image => {
                    const imgDiv = document.createElement('div');
                    imgDiv.textContent = image;
                    imageContainer.appendChild(imgDiv);
                });
            }

            const productUpdate = row.querySelector('.product-update-button');
            const updateCancel = row.querySelector('.update-cancel');

            productUpdate.addEventListener('click', () => {
                const images = document.getElementById('update-image').files;
                const product = {
                    productId: data.productId,
                    pName: document.getElementById('productName').value,
                    price: document.getElementById('productPrice').value,
                    stock: document.getElementById('productStock').value,
                    description: document.getElementById('productDescription').value
                };

                fetchUpdateProduct(product)
                    .then(() => {
                        if(images.length > 0) {
                            const deletePromises = data.images.map(() => fetchDeleteProductImage(data.productId));
                            return Promise.all(deletePromises);
                        } else{
                            return Promise.resolve();
                        }
                    })
                    .then(() => {
                        if(images.length>0){
                            return fetchUploadProductImage(data.productId, images);
                        }
                    })
                    .then(()=> {
                        return fetchReadProductPage();
                    })
                    .then(updatedData => {
                        // const productButton = document.querySelector('#nav_product');
                        // productButton.click();
                        // // addProduct(updatedData);
                    });
            });

            updateCancel.addEventListener('click', () => {
                const productButton = document.querySelector('#nav_product');
                productButton.click();
            });

            // adminContent.appendChild(row);

        })
        .catch(error => error);
}

function createProduct() {
    const adminContent = document.getElementById('admin_content');
    adminContent.innerHTML='';

    const row = document.createElement('div');
    row.className = 'detail-product';

    row.innerHTML=`
        <div >
            <div><h3>상품 등록</h3></div>
            <div class="detail-product-hDiv"></div>
        </div><hr>
        <div>
            <label>상품명</label>
            <input type="text" id="productName">
        </div><hr>
        <div>
            <label>가격</label>
            <input type="text" id="productPrice">
        </div><hr>
        <div>
            <label>재고 수량</label>
            <input type="text" id="productStock">
        </div><hr>
        <div>
            <label>상품 설명</label>
            <textarea id="productDescription" class="description-input"></textarea>
        </div><hr>
        <div>
            <label>이미지 등록</label>
            <input type="file" id="productImage">
        </div><hr>
        <div class="detail-product-buttonDiv">
            <button class="product-add-button">등록</button>
            <button class="add-cancel">취소</button>
        </div>
    `;

    const productCreate = row.querySelector('.product-add-button');
    const createCancel = row.querySelector('.add-cancel');

    productCreate.addEventListener('click', () => {
        const product = {
            pName: document.getElementById('productName').value,
            price: document.getElementById('productPrice').value,
            stock: document.getElementById('productStock').value,
            description: document.getElementById('productDescription').value
        };

        const images = document.getElementById('productImage').files;


        fetchCreateProduct(product).then(data => {
            console.log('data : ', data);
            if (images.length > 0) {
                return fetchDeleteProductImage(data.productId) // 이미지 삭제
                    .then(() => {
                        return fetchUploadProductImage(data.productId, images);
                    });
            }
            return Promise.resolve();
        }).then(() => {
            return fetchReadProductPage();
        }).then(updatedData => {
            alert('상품등록 완료');
            const productButton = document.querySelector('#nav_product');
            productButton.click();
            // addProduct(updatedData);
        }).catch(error => {
            console.error('Error calling fetchCreateProduct:', error);
        });

    });

    createCancel.addEventListener('click', () => {
        const productButton = document.querySelector('#nav_product');
        productButton.click();
    });

    adminContent.appendChild(row);
}

function addOrder(data) {
    const adminContent = document.getElementById('admin_content');
    const activeMenu = document.getElementsByClassName('hover-active');
    const navOrder = document.getElementById('nav_order');

    for (let i = 0; i < activeMenu.length; i++) {
        activeMenu[i].classList.remove('hover-active');
    }

    navOrder.classList.add('hover-active');

    adminContent.innerHTML = `
        <div class="admin_board_div1">
            <h3>주문 관리</h3>
        </div>
        <table class="table1">
            <thead id="admin-order-thead">
            </thead>
            <tbody id="admin-order-tbody">
            </tbody>
        </table>
    `;
    const thead = document.getElementById('admin-order-thead');
    thead.innerHTML = '';
    const row2 = document.createElement('tr');
    row2.innerHTML = `
        <th>주문번호</th>
        <th>이름</th>
        <th>배송지</th>
        <th>주문일자</th>
        <th>주문상태</th>
        <th></th>
    `;
    thead.appendChild(row2);

    const tbody= document.getElementById('admin-order-tbody');
    tbody.innerHTML = '';
    data.forEach(order => {
        const row = document.createElement('tr');
        row.id = `order-${order.orderId}`;
        const createdAt = formatLocalDateTime(order.createdAt);
        row.innerHTML = `
                <td>${order.orderId}</td>
                <td>${order.name}</td>
                <td>${order.address}</td>
                <td>${createdAt}</td>
                <td>${order.status}</td>
                <td>
                    <select id="status-select">
                        <option value="" disabled selected hidden>주문 상태 선택</option>
                        <option value="APROVED">APROVED</option>
                        <option value="CANCELLED">CANCELLED</option>
                        <option value="DELIVERED">DELIVERED</option>
                        <option value="SHIPPED">SHIPPED</option>
                    </select>
                    <button id="change-status-btn">변경</button>
                </td>
        `;
        const changeStBtn = row.querySelector('#change-status-btn');
        changeStBtn.addEventListener('click', () => {
            const selectElement = row.querySelector('#status-select');
            const selectedStatus = selectElement.value;
            if (selectedStatus !== "") {
                const changeData = {
                    orderId: order.orderId,
                    orderStatus: selectedStatus
                };
                console.log(changeData);
                fetchUpdateOrder(changeData).then( () => {
                    alert('수정 완료');
                    orderMenu.click();
                });
            } else {
                alert('주문 상태를 선택해 주세요.');
            }

        });

        tbody.appendChild(row);
    });
}

function addBoard(data) {
    const adminContent = document.getElementById('admin_content');
    const activeMenu = document.getElementsByClassName('hover-active');
    const navBoard = document.getElementById('nav_board');

    for (let i = 0; i < activeMenu.length; i++) {
        activeMenu[i].classList.remove('hover-active');
    }

    navBoard.classList.add('hover-active');

    adminContent.innerHTML = `
        <div class="admin_board_div1">
            <h3>게시판 관리</h3>
        </div>
        <table class="table1">
            <thead id="admin-board-thead">
            </thead>
            <tbody id="admin-board-tbody">
            </tbody>
        </table>
        <div id="pagination"></div>
    `;
    const thead = document.getElementById('admin-board-thead');
    thead.innerHTML = '';
    const row2 = document.createElement('tr');
    row2.innerHTML = `
        <th>번호</th>
        <th>카테고리</th>
        <th>제목</th>
        <th>작성자</th>
        <th id=""><button class="add-board-button">게시글 추가</button></th>
    `;
    const addBoardBtn = row2.querySelector('.add-board-button');
    addBoardBtn.addEventListener('click', () => createBoard());
    thead.appendChild(row2);

    const tbody= document.getElementById('admin-board-tbody');
    tbody.innerHTML = '';
    data.content.forEach(board => {
        const row = document.createElement('tr');
        row.id = `board-${board.boardId}`;
        row.innerHTML =`
                <td>${board.boardId}</td>
                <td>${board.category}</td>
                <td>${board.title}</td>
                <td>${board.memberId}</td>
                <td>
                    <button class="detail-button">상세</button>
                    <button class="delete-board-btn">삭제</button>
                </td>
        `;
        const detailButton = row.querySelector('.detail-button');
        const deleteBoardBtn = row.querySelector('.delete-board-btn');
        detailButton.addEventListener('click', () => detailBoard(board.boardId));
        deleteBoardBtn.addEventListener('click', () => {
            fetchDeleteBoard(board.boardId).then(()=>{
                return fetchReadBoards();
            }).then(updatedData => {
                alert('삭제완료');
                addBoard(updatedData);
            }).catch(error => {
                console.error('Error:', error);
            });
        });

        tbody.appendChild(row);
    });
    renderPaginationBoard(data);
}

function renderPaginationBoard(data) {
    const paginationDiv = document.getElementById('pagination');
    paginationDiv.innerHTML = '';

    const currentPage = data.number + 1;
    const totalPages = data.totalPages;

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        if (i === currentPage) {
            pageButton.disabled = true;
        }
        pageButton.addEventListener('click', () => {
            fetchReadBoards(i).then(data => {
                addBoard(data);
                renderPaginationBoard(data);
            });
        });
        paginationDiv.appendChild(pageButton);
    }
}

function renderPaginationProduct(data) {
    const paginationDiv = document.getElementById('pagination');
    paginationDiv.innerHTML = '';

    const currentPage = data.number + 1;
    const totalPages = data.totalPages;

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        if (i === currentPage) {
            pageButton.disabled = true;
        }
        pageButton.addEventListener('click', () => {
            fetchReadProductPage(i).then(data => {
                addProduct(data);
                renderPaginationProduct(data);
            });
        });
        paginationDiv.appendChild(pageButton);
    }
}

function createBoard(){
    const adminContent = document.getElementById('admin_content');
    adminContent.innerHTML='';

    const row = document.createElement('div');
    row.className = 'detail-board';

    row.innerHTML=`
        <div >
            <div><h3>게시글 등록</h3></div>
            <div class="detail-product-hDiv"></div>
        </div><hr>
        <div>
            <label>카테고리</label>
            <select id="boardCategory">
                <option value="TIP">TIP</option>
                <option value="NOTICE">NOTICE</option>
                <option value="GENERAL">GENERAL</option>
            </select>
        </div><hr>
        <div>
            <label>제목</label>
            <input type="text" id="boardTitle">
        </div><hr>
        <div>
            <label>내용</label>
            <textarea id="boardDescription" class="description-input"></textarea>
        </div><hr>
        <div>
            <label>이미지 등록</label>
            <input type="file" id="boardImage" multiple>
        </div><hr>
        <div class="detail-board-buttonDiv">
            <button class="board-add-button">등록</button>
            <button class="board-add-cancel">취소</button>
        </div>
    `;

    const boardCreate = row.querySelector('.board-add-button');
    const createCancel = row.querySelector('.board-add-cancel');

    boardCreate.addEventListener('click', () => {
        const board = {
            memberId: tokenMemberId,
            title: document.getElementById('boardTitle').value,
            description: document.getElementById('boardDescription').value,
            category: document.getElementById('boardCategory').value

        };

        const images = document.getElementById('boardImage').files;

        fetchCreateBoard(board).then( data=> {
            if (images.length > 0) {
                return fetchUpBoardImage(data.boardId, images);
            }
            return Promise.resolve();
        }).then(()=>{
            return fetchReadBoards();
        }).then(updatedData => {
            addBoard(updatedData);
        }).catch(error => {
            console.error('Error:', error);
        });


    });

    createCancel.addEventListener('click', () => {
        const boardButton = document.querySelector('#nav_board');
        boardButton.click();
    });

    adminContent.appendChild(row);
}

function detailBoard(id){
    fetchReadBoard(id)
        .then(data =>{
            const adminContent = document.getElementById('admin_content');
            adminContent.innerHTML='';

            const row = document.createElement('div');
            row.className = 'detail-board';

            row.innerHTML = `                
                    <div >
                        <div><h3>게시글 상세 보기</h3></div>
                        <div class="detail-board-hDiv">boardId: ${data.boardId}</div>
                    </div><hr>
                    <div>
                        <label>작성자</label>
                        <div>${data.memberId}</div>
                    </div><hr>
                    <div>
                        <label>카테고리</label>
                        <div>${data.category}</div>
                    </div><hr>
                    <div>
                        <label>제목</label>
                        <input type="text" id="boardTitle" value="${data.title}">
                    </div><hr>
                    <div>
                        <label>설명</label>
                        <textarea id="boardDescription" class="description-input">${data.description}</textarea>
                    </div><hr>
                    <div>
                        <label>이미지</label>
                        <div class="d-p-d-i" id="imageContainer-${data.boardId}"></div>
                    </div>
                    <div>
                        <label></label>
                        <div class="d-p-d-i">
                            <div><input type="file" id="update-image" multiple></div>
                        </div>
                    </div><hr>
                    <div>
                        <label>작성 일자</label>
                        <div class="d-p-d-d">${data.createdAt}</div>
                    </div><hr>
                    <div>
                        <label>수정 일자</label>
                        <div class="d-p-d-d">${data.updatedAt}</div>
                    </div><hr>
                    <div>
                        <label>댓글</label>
                        <div id="reply-div"></div>
                    </div><hr>
                    <div class="detail-board-buttonDiv">
                        <button class="board-update-button">수정</button>
                        <button class="board-update-cancel">취소</button>
                    </div>
            `;

            adminContent.appendChild(row);

            const replyContainer = document.getElementById('reply-div');
            if(data.replies.length) {
                data.replies.forEach(reply=>{
                    const replyContent = document.createElement('div');
                    replyContent.innerHTML = `${reply.memberId} - ${reply.content}`;
                    replyContainer.appendChild(replyContent);
                });
            } else {
                replyContainer.innerHTML = "<label></label><div>댓글 없음</div>";
            }
            const imageContainer = document.getElementById(`imageContainer-${data.boardId}`);

            if(data.imageFilenames.length > 0 ) {
                data.imageFilenames.forEach(image => {
                    const imgDiv = document.createElement('div');
                    imgDiv.textContent = image;
                    const removeBtn = document.createElement('button');
                    removeBtn.textContent = 'X';
                    removeBtn.addEventListener('click', ()=>{
                        fetchDlBoardImage(data.boardId, image);
                        alert('이미지 삭제 완료');
                        imageContainer.removeChild(imgDiv);
                    });
                    imgDiv.appendChild(removeBtn);
                    imageContainer.appendChild(imgDiv);
                });
            }

            const boardUpdate = row.querySelector('.board-update-button');
            const updateCancel = row.querySelector('.board-update-cancel');

            boardUpdate.addEventListener('click', () => {
                const images = document.getElementById('update-image').files;
                const board = {
                    boardId: data.boardId,
                    title: document.getElementById('boardTitle').value,
                    description: document.getElementById('boardDescription').value,
                    category: data.category
                };
                console.log(data);
                fetchUpdateBoard(board).then(() => {
                        if(images.length > 0) {
                            const deletePromises = data.imageFilenames.map((filename)=> fetchDlBoardImage(data.boardId,filename));
                            return Promise.all(deletePromises);
                        } else{
                            return Promise.resolve();
                        }
                    })
                    .then(() => {
                        if(images.length > 0) {
                            return fetchUpBoardImage(data.boardId, images);
                        }
                    })
                    .then(()=> {
                        alert('수정완료');
                        const boardButton = document.querySelector('#nav_board');
                        boardButton.click();
                    });
            });

            updateCancel.addEventListener('click', () => {
                const boardButton = document.querySelector('#nav_board');
                boardButton.click();
            });


        })
        .catch(error => error);
}


function clickProduct() {
    productMenu.click();
}

function formatLocalDateTime(dateTimeString) {
    const date = new Date(dateTimeString);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`;
}


history.pushState(null, null, window.location.href);

window.addEventListener('popstate', function(event) {
    // 뒤로 가기를 했을 때 /admin 페이지로 리다이렉트
    window.location.href = '/app/admin';
});

