//수정완료
export function fetchReadProducts() {
    const jwtToken = localStorage.getItem('jwtToken');
    return fetch(`/cc/product/list`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,  // JWT 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if(!response.ok) {
                throw new Error('상품 정보를 불러오지 못했습니다!');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error => console.error('Error fetching products:', error));
}
//수정완료
export function fetchReadProductPage(pageNumber = 1) {
    const jwtToken = localStorage.getItem('jwtToken');
    return fetch(`/cc/product?page=${pageNumber}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,  // JWT 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if(!response.ok) {
                throw new Error('상품 정보를 불러오지 못했습니다!');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error => console.error('Error fetching products:', error));

}
//수정완료
export function fetchReadProductSearch(name){
    const jwtToken = localStorage.getItem('jwtToken');
    return fetch(`/cc/product/listByPName/${name}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,  // JWT 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if(!response.ok) {
                throw new Error('상품 정보를 불러오지 못했습니다!');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error => console.error('Error fetching products:', error));
}
//수정완료
export function fetchReadProduct(id) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/product/${id}`,{
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,  // JWT 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('상품 정보를 불러오지 못했습니다!')
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error => console.error('Error fetching product:', error))
}
//수정완료(관리자)
export function fetchUpdateProduct(product) {
    const jwtToken = localStorage.getItem('jwtToken');
    console.log(product);
    return fetch(`/cc/admin/product/${product.productId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(product)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log(responseData);
            alert('상품 수정 완료!!');
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}
//수정완료(관리자)
export function fetchCreateProduct(product) {
    const jwtToken = localStorage.getItem('jwtToken');
    console.log('product : ', product);
    return fetch(`/cc/admin/product`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(product)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error => {
            console.error('Fetch error :', error);
        });
}
//수정완료
export function  fetchUploadProductImage(id, images) {
    const jwtToken = localStorage.getItem('jwtToken');
    const formData = new FormData();
    for (let i = 0; i < images.length; i++) {
        formData.append('files', images[i]);
    }
    fetch(`/cc/productImage/upload/${id}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`  // JWT 토큰 추가
        },
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log(responseData);
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}
//수정완료
export function fetchDeleteProductImage(productId) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/productImage/${productId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if(!response.ok) {
            throw new Error('에러발생!!');
        }
        return response.json();
    }).then(responseData => {
        console.log(responseData);
    }).catch(error => {
        console.error('Fetch error :', error);
    });
}
//수정완료(관리자)
export function fetchDeleteProduct(id) {
    const jwtToken = localStorage.getItem('jwtToken');
    fetch(`/cc/admin/product/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if(!response.ok){
            throw new Error('에러발생');
        }
        return response.json();
    }).then(responseData => {
        alert("상품 삭제 완료!");
        const productRow = document.getElementById(`product-${id}`);
        if (productRow) {
            productRow.remove();
        }
    }).catch(error => {
        console.error('Fetch error', error);
    });
}
//수정완료
export function fetchCreateReview(data) {
    const jwtToken = localStorage.getItem('jwtToken');
    return fetch(`/cc/mypage/review`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (!response.ok) {
            throw new Error('에러발생!!');
        }
        return response.json();
    }).then(responseData => {
        console.log('responseData : ', responseData);
        return responseData;
    }).catch(error => {
        console.error('Fetch error :', error);
    });
}
//수정완료
export function fetchReadReview(id) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/review/product/${id}`,{
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,  // JWT 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('상품 정보를 불러오지 못했습니다!')
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error => console.error('Error fetching product:', error))
}
//
export function fetchCreateMember(member) {
    const jwtToken = localStorage.getItem('jwtToken');
    fetch(`/cc/member`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(product)
    }).then(response => {
            if (!response.ok) {
                throw new Error('에러발생!!');
            }
            return resonse.json();
    }).then(responseData => {
            console.log('responseData: ', responseData);
            return responseData;
    }).catch(error => {
            console.error('Fetch error :', error);
    });
}
//수정완료
export function fetchReadMember(memberId){
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/mypage/member/${memberId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if(!response.ok) {
            throw new Error('회원 정보를 불러오지 못했습니다!')
        }
        return response.json();
    }).then(data => {
        console.log(data);
        return data;
    }).catch(error =>
        console.error('Error fetching member:', error)
    )
}
//수정완료
export function fetchCreateCart(memberId, productId, quantity){
    const jwtToken = localStorage.getItem('jwtToken');

    fetch(`/cc/cart`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            memberId: memberId,
            cartItems: [
                { productId: productId, quantity: quantity}
            ]
        })
    }).then(response => {
        if (!response.ok) {
            // 응답이 실패일 경우, 서버에서 반환된 메시지를 파싱하여 출력
            return response.json().then(errorData => {
                throw new Error(`${response.status}: ${errorData.message}`);
            });
        }
        return response.json();
    }).then(data => {
            alert('카트 등록 완료 되었습니다!!');
        window.location.href = "/app/product";
    }).catch(error => {
            alert(`에러발생 : ${error.message}`);
    });
}
//수정완료
export function fetchReadCart(memberId) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/mypage/cart/${memberId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if(!response.ok) {
            throw new Error('카트 정보를 불러오지 못했습니다!')
        }
        return response.json();
    }).then(data => {
        console.log(data);
        return data;
    }).catch(error =>
        console.error('Error fetching product:', error)
    )
}
//수정완료
export function fetchUpdateCart(data) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/mypage/cart/${data.cartItemId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
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
//수정완료
export function fetchRemoveCartItem(id) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/mypage/cartitem/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    }).then(data => {
            const cartItemRow = document.getElementById(`cart-item-${id}`);
            if (cartItemRow) {
                cartItemRow.remove();
            }
    }).catch(error => {
            console.error('Error removing cart item:', error);
    });
}
//수정완료
export function fetchCreateOrder(data){
    const jwtToken = localStorage.getItem('jwtToken');
    console.log('order 데이터 : ', data);
    return fetch(`/cc/mypage/order`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (!response.ok) {
            throw new Error('에러발생!!');
        }
        return response.json();
    }).then(responseData => {
            console.log('responseData : ', responseData);
            return responseData;
    }).catch(error => {
            console.error('Fetch error :', error);
    });
}
//수정완료
export function fetchReadOrderMember(id) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/mypage/order/list/${id}`,{
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,  // JWT 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('주문 정보를 불러오지 못했습니다!')
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error =>
            console.error('Error fetching product:', error)
        );
}
//수정완료
export function fetchReadOrders() {
    const jwtToken = localStorage.getItem('jwtToken');
    return fetch(`/cc/admin/order`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,  // JWT 토큰 추가
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if(!response.ok) {
                throw new Error('주문 정보를 불러오지 못했습니다!');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            return data;
        })
        .catch(error => console.error('Error fetching products:', error));

}
//수정완료
export function fetchUpdateOrder(data) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/admin/order/${data.orderId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {

            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error =>  {
            console.error('Error error:', error);
        });
}

export function fetchCreateBoard(board) {
    const jwtToken = localStorage.getItem('jwtToken');
    console.log(board);
    return fetch(`/cc/board/`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(board)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error => {
            console.error('Fetch error :', error);
        });
}

export function fetchReadBoards(pageNumber = 1) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/board?page=${pageNumber}`,{
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error =>  {
            console.error('Error error:', error);
        });
}

export function fetchReadBoard(id) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/board/${id}`,{
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error =>  {
            console.error('Error error:', error);
        });
}

export function fetchUpdateBoard(data) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/board/${data.boardId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {

            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error =>  {
            console.error('Error error:', error);
        });
}

export function fetchDeleteBoard(id) {
    const jwtToken = localStorage.getItem('jwtToken');
    return fetch(`/cc/board/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {

            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error =>  {
            console.error('Error error:', error);
        });

}

export function fetchUpBoardImage(id ,images) {
    const jwtToken = localStorage.getItem('jwtToken');
    const formData = new FormData();
    for (let i = 0; i < images.length; i++) {
        formData.append('files', images[i]);
    }
    fetch(`/cc/boardImage/upload/${id}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`  // JWT 토큰 추가
        },
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log(responseData);
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}

export function fetchDlBoardImage(id, filename) {
    const jwtToken = localStorage.getItem('jwtToken');
    fetch(`/cc/boardImage/${id}/${filename}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if(!response.ok){
            throw new Error('에러발생');
        }
        return response.json();
    }).then(responseData => {

    }).catch(error => {
        console.error('Fetch error', error);
    });
}

export function fetchCreateReply(reply) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/reply/`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reply)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {
            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error => {
            console.error('Fetch error :', error);
        });
}

export function fetchReadReply(boardId) {
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/reply/listByBoard/${boardId}`,{
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(!response.ok) {
                throw new Error('에러발생!!');
            }
            return response.json();
        })
        .then(responseData => {

            console.log('responseData : ', responseData);
            return responseData;
        })
        .catch(error =>  {
            console.error('Error error:', error);
        });
}

export function fetchReadImage(memberId){
    const jwtToken = localStorage.getItem('jwtToken');

    return fetch(`/cc/mypage/member/${memberId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if(!response.ok) {
            throw new Error('회원 정보를 불러오지 못했습니다!')
        }
        return response.json();
    }).then(data => {
        const image = data.image;
        return `
            <img class="thum-img" src="/uploadPath/${data.image}" alt="이미지 없음1" onerror="this.onerror=null; this.src='/images/image01.png';">
        `;
    }).catch(error =>
        console.error('Error fetching member:', error)
    )
}
