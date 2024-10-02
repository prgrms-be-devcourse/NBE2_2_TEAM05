import { fetchDeleteProduct, fetchDeleteProductImage, fetchUploadProductImage, fetchCreateProduct, fetchReadProduct, fetchReadProducts, fetchUpdateProduct } from './fetch.js';

const dashboardMenu = document.querySelector('#nav_dashboard');
const memberMenu = document.querySelector('#nav_member');
const productMenu = document.querySelector('#nav_product');
const orderMenu = document.querySelector('#nav_order');
const boardMenu = document.querySelector('#nav_board');

dashboardMenu.addEventListener('click', () => {
    addDashboard();
});

memberMenu.addEventListener('click', () => {
    addMember();
});

productMenu.addEventListener('click', () => {
    fetchReadProducts().then(data => {
        addProduct(data);
    });
});

orderMenu.addEventListener('click', () => {
    addOrder();
});

boardMenu.addEventListener('click', () => {
    addBoard();
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
        <h3>Dashboard</h3>
        <div>대시보드가 들어갈 예정 입니다!!</div>
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
    data.forEach(product => {
        const row = document.createElement('tr');
        row.id = `product-${product.productId}`;
        row.innerHTML =`
                <td>${product.productId}</td>
                <td>${product.pname}</td>
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
                        <input type="text" id="productName" value="${data.pname}">
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
                        <button class="update-cancel">취소</button>
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
                    pname: document.getElementById('productName').value,
                    price: document.getElementById('productPrice').value,
                    stock: document.getElementById('productStock').value,
                    description: document.getElementById('productDescription').value
                };

                fetchUpdateProduct(product)
                    .then(() => {
                        if(images.length > 0) {
                            // ino 정보가 필요함!!!!!!!
                            console.log('삭제할 이미지 갯수 : ', data.images.length);
                            for(let i = 0; i<data.images.length; i++){
                                fetchDeleteProductImage(data.productId, 0).then();
                            }
                        }
                        return Promise.resolve();
                        // const deleteRequests = data.images.map((_, i) => fetchDeleteProductImage(data.productId, i));
                        // return Promise.all(deleteRequests);
                    })
                    .then(() => {
                        return fetchUploadProductImage(data.productId, images);
                    })
                    .then(()=> {
                        return fetchReadProducts();
                    })
                    .then(updatedData => {
                        addProduct(updatedData);
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
            pname: document.getElementById('productName').value,
            price: document.getElementById('productPrice').value,
            stock: document.getElementById('productStock').value,
            description: document.getElementById('productDescription').value
        };

        const images = document.getElementById('productImage').files;

        fetchCreateProduct(product).then(data => {
            console.log('data : ', data);
            if (images.length > 0) {
                return fetchDeleteProductImage(data.productId, 0) // 이미지 삭제
                    .then(() => {
                        // 삭제가 완료된 후 업로드 진행
                        return fetchUploadProductImage(data.productId, images);
                    });
            }
            return Promise.resolve(); // 이미지가 없으면 빈 Promise 반환
        }).then(() => {
            // 모든 작업이 완료된 후 제품 목록을 다시 읽습니다.
            return fetchReadProducts();
        }).then(updatedData => {
            addProduct(updatedData);
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

function addOrder() {
    const adminContent = document.getElementById('admin_content');
    const activeMenu = document.getElementsByClassName('hover-active');
    const navOrder = document.getElementById('nav_order');

    for (let i = 0; i < activeMenu.length; i++) {
        activeMenu[i].classList.remove('hover-active');
    }

    navOrder.classList.add('hover-active');

    adminContent.innerHTML = `
        <h3>주문관리</h3>
        <div>주문관리가 들어갈 예정 입니다!!</div>
    `;
}

function addBoard() {
    const adminContent = document.getElementById('admin_content');
    const activeMenu = document.getElementsByClassName('hover-active');
    const navBoard = document.getElementById('nav_board');

    for (let i = 0; i < activeMenu.length; i++) {
        activeMenu[i].classList.remove('hover-active');
    }

    navBoard.classList.add('hover-active');

    adminContent.innerHTML = `
        <h3>게시판 관리</h3>
        <div>게시판 관리가 들어갈 예정 입니다!!</div>
    `;
}


function clickProduct() {
    productMenu.click();
}

history.pushState(null, null, window.location.href);

window.addEventListener('popstate', function(event) {
    // 뒤로 가기를 했을 때 /admin 페이지로 리다이렉트
    window.location.href = '/admin';
});