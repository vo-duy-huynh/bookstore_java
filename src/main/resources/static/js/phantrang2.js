
const product1s = document.querySelectorAll(".product-default-single-item");
const itemsPerPage1 = 1; // Số sản phẩm mỗi trang
const numPages1 = Math.ceil(product1s.length / itemsPerPage1); // Tổng số trang

// Hiển thị danh sách sản phẩm cho trang đầu tiên
showPage1(1);

// Tạo các nút liên kết phân trang
const pagination1 = document.querySelector(".pagination1");
for (let i = 1; i <= numPages1; i++) {
    const li = document.createElement("li");
    const link = document.createElement("a");
    link.textContent = i;
    link.href = "#";
    if (i === 1) {
        li.classList.add("active-page1");
    }

    li.appendChild(link);
    pagination1.appendChild(li);

    // Xử lý sự kiện click cho từng nút liên kết
    link.addEventListener("click", function(event) {
        event.preventDefault();
        // Xóa class active của tất cả các nút liên kết khác
        const activeLink = pagination1.querySelector("li.active-page1");
        if (activeLink) activeLink.classList.remove("active-page1");
        // Thêm class active vào nút liên kết được click
        li.classList.add("active-page1");

        showPage1(i);
    });
}
const liNext1 = document.createElement("li");
const linkNext1 = document.createElement("a");
linkNext1.textContent = "Next";
linkNext1.href = "#";

linkNext1.classList.add("next-page1");
liNext1.appendChild(linkNext1);
pagination1.appendChild(liNext1);
// get element by class next-page1 and add event click
document.querySelector(".next-page1").addEventListener("click", function(event) {
    event.preventDefault();
    nextPage1();
});

document.getElementById("previous-page1").addEventListener("click", function(event) {
    event.preventDefault();
    previousPage1();
});
// Hiển thị sản phẩm cho trang được chọn
function showPage1(pageNumber1) {
    const startIndex1 = (pageNumber1 - 1) * itemsPerPage1;
    const endIndex1 = startIndex1 + itemsPerPage1;
    for (let i = 0; i < product1s.length; i++) {
        if (i >= startIndex1 && i < endIndex1) {
            product1s[i].style.display = "block";
        } else {
            product1s[i].style.display = "none";
        }
    }
}
// function previous is page in front of current page if current page is 1 notification page is first page
function previousPage1() {
    var currentPage1 = document.querySelector(".active-page1");
    if (currentPage1.textContent == 1) {
        Swal.fire({
            title: 'Bạn đang ở trang đầu tiên!',
            icon: 'error',
            showConfirmButton: false,
            timer: 1500
        });
    } else {
        currentPage1.classList.remove("active-page1");
        currentPage1.previousElementSibling.classList.add("active-page1");
        showPage1(currentPage1.previousElementSibling.textContent);
    }
}

function nextPage1() {
    var currentPage1 = document.querySelector(".active-page1");
    if (currentPage1.textContent == numPages1) {
        Swal.fire({
            title: 'Bạn đang ở trang cuối cùng!',
            icon: 'error',
            showConfirmButton: false,
            timer: 1500
        });
    } else {
        currentPage1.classList.remove("active-page1");
        currentPage1.nextElementSibling.classList.add("active-page1");
        showPage1(currentPage1.nextElementSibling.textContent);
    }
}