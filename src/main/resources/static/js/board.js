import {
    fetchReadImage,
    fetchUpBoardImage,
    fetchDlBoardImage,
    fetchDeleteBoard,
    fetchUpdateBoard,
    fetchReadBoard,
    fetchReadBoards,
    fetchCreateBoard,
    fetchCreateReply, fetchReadReply
} from './fetch.js'

history.pushState(null, null, window.location.href);

window.addEventListener('popstate', function(event) {
    // 뒤로 가기를 했을 때 /admin 페이지로 리다이렉트
    window.location.href = '/app/board';
});



document.addEventListener('DOMContentLoaded', () => {
    const paginationDiv = document.getElementById('pagination');

    fetchReadBoards().then(data => {
        const category = 'All';
        boardTable(data.content, category);
        renderPagination(data);
    });

    const allBtn = document.getElementById('board-category-all');
    allBtn.addEventListener('click', () => {
        window.location.href = "/app/board";
    });
    const noticeBtn = document.getElementById('board-category-notice');
    noticeBtn.addEventListener('click', () => {
        fetchReadBoards().then(data => {
            paginationDiv.style.display = 'none';
            const category = 'NOTICE';
            boardTable(data.content, category);
            renderPagination(data);
        });
    });
    const tipBtn = document.getElementById('board-category-tip');
    tipBtn.addEventListener('click', () => {
        fetchReadBoards().then(data => {
            paginationDiv.style.display = 'none';
            const category = 'TIP';
            boardTable(data.content, category);
            renderPagination(data);
        });
    });
    const generalBtn = document.getElementById('board-category-general');
    generalBtn.addEventListener('click', () => {
        fetchReadBoards().then(data => {
            paginationDiv.style.display = 'none';
            const category = 'GENERAL';
            boardTable(data.content, category);
            renderPagination(data);
        });
    });

});

function boardTable(boards, category) {
    const tbody = document.querySelector('#board-table tbody');
    tbody.innerHTML = ``;
    boards.forEach(board => {
        if(category === 'All') {
            const row = document.createElement('tr');
            row.className = 'board-body-tr';
            const createdAt = formatLocalDateTime(board.createdAt);
            row.innerHTML = `
                <td>${board.boardId}</td>
                <td>${board.category}</td>
                <td>${board.title}</td>
                <td id="member-${board.memberId}" class="image-td">${board.memberId}</td>
                <td>${createdAt}</td>
            `;
            fetchReadImage(board.memberId)
                .then(imgTag => {
                    const memberCell = row.querySelector(`#member-${board.memberId}`);
                    memberCell.innerHTML = `<div>${imgTag} ${board.memberId}</div>`;
                })
                .catch(error => {
                    console.error('Error loading image:', error);
                });



            row.addEventListener('click', () => {
                detailBoard(board.boardId);
            });
            tbody.appendChild(row);
        } else {
            if(board.category === category) {
                const row = document.createElement('tr');
                row.className = 'board-body-tr';
                const createdAt = formatLocalDateTime(board.createdAt);
                row.innerHTML = `
                <td>${board.boardId}</td>
                <td>${board.category}</td>
                <td>${board.title}</td>
                <td id="member-${board.memberId}" class="image-td">${board.memberId}</td>
                <td>${createdAt}</td>
                `;
                fetchReadImage(board.memberId)
                    .then(imgTag => {
                        const memberCell = row.querySelector(`#member-${board.memberId}`);
                        memberCell.innerHTML = `${imgTag} ${board.memberId}`;
                    })
                    .catch(error => {
                        console.error('Error loading image:', error);
                    });


                row.addEventListener('click', () => {
                    detailBoard(board.boardId);
                });
                tbody.appendChild(row);
            }
        }
    });
}

function renderPagination(pageData) {
    const paginationContainer = document.querySelector('#pagination');
    paginationContainer.innerHTML = ''; // 기존 페이지 버튼 초기화

    const totalPages = pageData.totalPages;
    const currentPage = pageData.number + 1;

    // 페이지 번호 버튼 추가
    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        if (i === currentPage) {
            pageButton.disabled = true; // 현재 페이지는 비활성화
        }
        pageButton.addEventListener('click', () => {
            fetchReadBoards(i).then(data => {
                boardTable(data.content, 'All');
                renderPagination(data);
            });
        });
        paginationContainer.appendChild(pageButton);
    }
}

function detailBoard(id) {

    const boardMain = document.querySelector('.board-main');
    boardMain.innerHTML = ``;

    const row = document.createElement('div');

    fetchReadBoard(id).then(data => {
        let imagesHtml = '';

        if (data.imageFilenames && data.imageFilenames.length > 0) {
            data.imageFilenames.forEach(filename => {
                imagesHtml += `<img class="board-img" src="/uploadPath/${filename}" alt="Image" />`;
            });
        }
        const createdAt = formatLocalDateTime(data.createdAt);

        let updateHtml = '';
        if (data.memberId === tokenMemberId) {
            updateHtml += `
                <button class="board-update-btn">수정</button>
                <button class="board-delete-btn">삭제</button>
            `;
        }
        row.innerHTML = `
            <div class="board-detail-div">
                <div>
                    <div>${data.category}</div>
                    <div>${data.title}</div>
                </div>
                <div>
                    <div id="member-${data.memberId}" class="image-td">${data.memberId}</div>
                    <div>${createdAt}</div>
                </div>
                <div class="board-detail-desc">
                    <div>${imagesHtml}</div>
                    <div>${data.description}</div>
                </div>
                <div>
                    ${updateHtml}
                </div>
            </div>
            <div>
                <h3 id="totalReply"></h3>
            </div>
            <div id="reply-div">
                
            </div><hr>
            <div id="reply-div2">
                <div>댓글 작성자 : ${tokenMemberId}</div>
                <div>
                    <textarea id="reply-content"></textarea>
                </div>
            </div>
            <div>
                <button class="reply-create-btn">작성</button>
            </div>
        `;
        fetchReadImage(data.memberId)
            .then(imgTag => {
                const memberCell = row.querySelector(`#member-${data.memberId}`);
                memberCell.innerHTML = `${imgTag} ${data.memberId}`;
            })
            .catch(error => {
                console.error('Error loading image:', error);
            });

        if (data.memberId === tokenMemberId) {
            const updateBtn = row.querySelector('.board-update-btn');
            const deleteBtn = row.querySelector('.board-delete-btn');

            updateBtn.addEventListener('click', () => {
                updateBoard(data);
            });
            deleteBtn.addEventListener('click', () => {
                fetchDeleteBoard(id).then(()=>{
                    alert('삭제완료');
                    window.location.href = "/app/board";
                });
            });
        }
        if(data.replies.length > 0) {
            const replyDiv = row.querySelector('#reply-div');
            const totalReplyDiv = row.querySelector('#totalReply');
            totalReplyDiv.innerHTML = `댓글 [${data.replies.length}]개`;

            data.replies.forEach(reply => {
                const replyContent = document.createElement('div');
                replyContent.innerHTML = `
                    <div id="reply-${reply.memberId}" class="image-td">${reply.memberId}</div>
                    <div>${reply.content}</div>
                `;

                fetchReadImage(reply.memberId)
                    .then(imgTag => {
                        const memberCell = row.querySelector(`#reply-${reply.memberId}`);
                        memberCell.innerHTML = `${imgTag} ${reply.memberId}`;
                    })
                    .catch(error => {
                        console.error('Error loading image:', error);
                    });
                replyDiv.appendChild(replyContent);
            });
        }
        // fetchReadReply(data.boardId).then(data => {
        //     const replyDiv = row.querySelector('#reply-div');
        //
        //     if (Array.isArray(data) && data.length > 0) {
        //         const totalReplyDiv = row.querySelector('#totalReply');
        //         totalReplyDiv.innerHTML = `댓글 [${data.length}]개`
        //         data.forEach(reply => {
        //             const replyContent = document.createElement('div');
        //             replyContent.innerHTML = `
        //                 <div>${reply.memberId}</div>
        //                 <div>${reply.content}</div>
        //             `;
        //             replyDiv.appendChild(replyContent);
        //         });
        //     }
        // }).catch();

        const replyCreate = row.querySelector('.reply-create-btn');
        replyCreate.addEventListener('click', () => {

            const reply = {
                content: row.querySelector('#reply-content').value,
                memberId: tokenMemberId,
                boardId: data.boardId
            };
            fetchCreateReply(reply).then(()=> {
                alert('댓글작성완료');
                detailBoard(id);
            });
        });

    });
    boardMain.appendChild(row);
}

function updateBoard(data) {
    const boardMain = document.querySelector('.board-main');
    boardMain.innerHTML = ``;
    const row = document.createElement('div');
    row.className = 'board-input-div';
    row.innerHTML = `
        <div>
            <h3>게시글 수정</h3>
        </div>
        <div>
            <label>제목</label>
            <input type="text" id="input-title" value="${data.title}">
        </div>
        <div>
            <label>내용</label>
            <textarea id="input-description">${data.description}</textarea>
        </div>
        <div>
            <label>이미지</label>
            <div id="imageContainer"></div>
         </div>
        <div>
            <label></label>
            <input id="input-files" type="file" multiple>
        </div>
        <div>
            <label></label>
            <button id="update-btn">수정</button>
            <button id="cancel-btn">취소</button>
        </div>
    `;
    const imageContainer = row.querySelector('#imageContainer');
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
    const updateBtn = row.querySelector('#update-btn');
    const cancelBtn = row.querySelector('#cancel-btn');

    updateBtn.addEventListener('click', () => {
        const images = row.querySelector('#input-files').files;
        const updateData = {
            boardId: data.boardId,
            title: document.getElementById('input-title').value,
            description: document.getElementById('input-description').value,
            category: 'GENERAL'
        }
        fetchUpdateBoard(updateData).then(() => {
            if(images.length > 0) {
                const deletePromises = data.imageFilenames.map((filename) => fetchDlBoardImage(data.boardId,filename));
                return Promise.all(deletePromises);
            } else{
                return Promise.resolve();
            }
        }).then(() => {
            if( images.length > 0 ) {
                return fetchUpBoardImage(data.boardId, images);
            }
        }).then(() => {
            alert('수정완료');
            window.location.href = "/app/board";
        });
    });
    cancelBtn.addEventListener('click', () =>{
        window.location.href = "/app/board";
    });
    boardMain.appendChild(row);
}

const addBoardBtn = document.querySelector('#add-board-btn');

addBoardBtn.addEventListener('click', () => {
    const boardMain = document.querySelector('.board-main');
    boardMain.innerHTML = ``;

    const row = document.createElement('div');
    row.className = 'board-input-div';
    row.innerHTML = `
        <div>
            <h3>자유 게시글 작성</h3>
        </div>
        <div>
            <label>제목</label>
            <input type="text" id="input-title">
        </div>
        <div>
            <label>내용</label>
            <textarea id="input-description"></textarea>
        </div>
        <div>
            <label>이미지</label>
            <input id="input-files" type="file" multiple>
        </div>
        <div>
            <label></label>
            <button id="create-btn">등록</button>
            <button id="cancel-btn">취소</button>
        </div>
    `;
    boardMain.appendChild(row);

    const createBtn = row.querySelector('#create-btn');
    createBtn.addEventListener('click', () => {
       const board = {
           memberId: tokenMemberId,
           title: document.getElementById('input-title').value,
           description: document.getElementById('input-description').value,
           category: 'GENERAL'
       };
        const images = document.getElementById('input-files').files;

       fetchCreateBoard(board).then( data=> {
           if (images.length > 0) {
               return fetchUpBoardImage(data.boardId, images);
           }
           return Promise.resolve();
       }).then(()=>{
           alert('등록완료');
           window.location.href = "/app/board";
       });
    });

    const cancelBtn = row.querySelector('#cancel-btn');
    cancelBtn.addEventListener('click', () => {
        window.location.href = "/app/board";
    });
});