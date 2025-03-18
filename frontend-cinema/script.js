const API_BASE_URL = "http://localhost:8080/reviews"; // Altere conforme necessário

// Carregar reviews
function loadReviews(page) {
    fetch(`${API_BASE_URL}/reviews?page=${page}`)
        .then(response => response.json())
        .then(data => {
            const reviewList = document.getElementById("reviews-list");
            reviewList.innerHTML = '';

            data.content.forEach(review => { // 'content' é o padrão do Spring Boot para paginação
                const reviewCard = document.createElement('div');
                reviewCard.classList.add('review-card');

                const reviewTitle = document.createElement('h3');
                reviewTitle.innerText = review.title;
                reviewCard.appendChild(reviewTitle);

                const reviewContent = document.createElement('p');
                reviewContent.innerText = review.content;
                reviewCard.appendChild(reviewContent);

                const viewCommentsButton = document.createElement('button');
                viewCommentsButton.classList.add('view-comments-btn');
                viewCommentsButton.innerText = 'Ver Comentários';
                viewCommentsButton.onclick = () => loadComments(review.id);
                reviewCard.appendChild(viewCommentsButton);

                reviewList.appendChild(reviewCard);
            });

        })
        .catch(error => {
            console.error("Erro ao carregar as reviews:", error);
        });
}

// Carregar comentários
function loadComments(reviewId) {
    fetch(`${API_BASE_URL}/reviews/${reviewId}/comments`)
        .then(response => response.json())
        .then(comments => {
            const commentsList = document.getElementById("comments-list");
            commentsList.innerHTML = ''; // Limpar os comentários anteriores

            comments.forEach(comment => {
                const commentItem = document.createElement('li');
                commentItem.innerHTML = `<strong>${comment.name}</strong>: ${comment.text}`;
                commentsList.appendChild(commentItem);
            });

            // Exibir o modal com os comentários
            document.getElementById("comments-modal").style.display = "block";
        })
        .catch(error => {
            console.error("Erro ao carregar os comentários:", error);
        });
}

// Fechar modal de comentários
function closeModal() {
    document.getElementById("comments-modal").style.display = "none";
}

// Iniciar carregamento ao carregar a página
window.onload = function () {
    loadReviews(1);
};
