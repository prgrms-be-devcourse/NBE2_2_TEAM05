//상품(List) 반환하는 함수
export function fetchReadProducts() {
    return fetch(`/cc/product/list`)
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

export function fetchReadProduct(id) {
    return fetch(`/cc/product/${id}`)
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

export function fetchUpdateProduct(product) {
    console.log(product);
    return fetch(`/cc/product/${product.productId}`, {
        method: 'PUT',
        headers: {
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

export function fetchCreateProduct(product) {
    console.log('product : ', product);
    return fetch(`/cc/product`, {
        method: 'POST',
        headers: {
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

export function  fetchUploadProductImage(id, images) {
    const formData = new FormData();
    for (let i = 0; i < images.length; i++) {
        formData.append('files', images[i]);
    }
    fetch(`/cc/productImage/upload/${id}`, {
        method: 'POST',
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

export function fetchDeleteProductImage(productId, ino) {
    console.log('삭제할 productId : ', productId);
    console.log('삭제할 ino : ', ino);
    return fetch(`/cc/productImage/${productId}/${ino}`, {
        method: 'DELETE',
        headers: {
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

export function fetchDeleteProduct(id) {
    fetch(`/cc/product/${id}`, {
        method: 'DELETE',
        headers: {
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