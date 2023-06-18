
const products = document.querySelectorAll(".product-list-single");
const itemsPerPage = 1; // Số sản phẩm mỗi trang
const numPages = Math.ceil(products.length / itemsPerPage); // Tổng số trang

// Hiển thị danh sách sản phẩm cho trang đầu tiên
showPage(1);

// Tạo các nút liên kết phân trang
const pagination = document.querySelector(".pagination");
for (let i = 1; i <= numPages; i++) {
    const li = document.createElement("li");
    const link = document.createElement("a");
    link.textContent = i;
    link.href = "#";
    if (i === 1) {
        li.classList.add("active-page");
    }

    li.appendChild(link);
    pagination.appendChild(li);

    // Xử lý sự kiện click cho từng nút liên kết
    link.addEventListener("click", function(event) {
        event.preventDefault();
        // Xóa class active của tất cả các nút liên kết khác
        const activeLink = pagination.querySelector("li.active-page");
        if (activeLink) activeLink.classList.remove("active-page");
        // Thêm class active vào nút liên kết được click
        li.classList.add("active-page");

        showPage(i);
    });
}
const liNext = document.createElement("li");
const linkNext = document.createElement("a");
linkNext.textContent = "Next";
linkNext.href = "#";
// add onclick(nextPage()) to linkNext
linkNext.addEventListener("click", function(event) {
    event.preventDefault();
    nextPage();
});
liNext.appendChild(linkNext);
pagination.appendChild(liNext);
document.getElementById("previous-page").addEventListener("click", function(event) {
    event.preventDefault();
    previousPage();
});
// Hiển thị sản phẩm cho trang được chọn
function showPage(pageNumber) {
    const startIndex = (pageNumber - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    for (let i = 0; i < products.length; i++) {
        if (i >= startIndex && i < endIndex) {
            products[i].style.display = "block";
        } else {
            products[i].style.display = "none";
        }
    }
}
// function previous is page in front of current page if current page is 1 notification page is first page
function previousPage() {
    var currentPage = document.querySelector(".active-page");
    if (currentPage.textContent == 1) {
        Swal.fire({
            title: 'Bạn đang ở trang đầu tiên!',
            icon: 'error',
            showConfirmButton: false,
            timer: 1500
        });
    } else {
        currentPage.classList.remove("active-page");
        currentPage.previousElementSibling.classList.add("active-page");
        showPage(currentPage.previousElementSibling.textContent);
    }
}

function nextPage() {
    var currentPage = document.querySelector(".active-page");
    if (currentPage.textContent == numPages) {
        Swal.fire({
            title: 'Bạn đang ở trang cuối cùng!',
            icon: 'error',
            showConfirmButton: false,
            timer: 1500
        });
    } else {
        currentPage.classList.remove("active-page");
        currentPage.nextElementSibling.classList.add("active-page");
        showPage(currentPage.nextElementSibling.textContent);
    }
}
