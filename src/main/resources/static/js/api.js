$(document).ready(function () {
    $.ajax({
        url: 'http://localhost:8080/api/v1/products',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let trHTML = '';
            $.each(data, function (i, item) {
                trHTML = trHTML + '<tr id="product-' + item.id + '">' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.title + '</td>' +
                    '<td>' + item.author + '</td>' +
                    '<td>' + item.price + '</td>' +
                    '<td>' + item.category + '</td>' +
                    '<td colspan="3">' +
                    '<a class="btn btn-primary" ' +
                    'sec:authorize="hasAnyAuthority(\'ADMIN\')" ' +
                    'href="/products/edit/' + item.id + '">Edit</a>' +
                    '<a class="btn btn-danger" href="/books" ' + item.id + '  ' +
                    'onclick="apiDeleteProduct(' + item.id + ', this); return false;" ' +
                    'sec:authorize="hasAnyAuthority(\'ADMIN\')">Delete</a>' +
                    '<form th:action="@{/products/add-to-cart}" method="post" class="d-inline">' +
                    '<input type="hidden" name="id" value="' + item.id + '"/>' +
                    '<input type="hidden" name="name" value="' + item.title + '"/>' +
                    '<input type="hidden" name="price" value="' + item.price + '"/>' +
                    '<button type="submit" class="btn btn-success" ' +
                    'onclick="return confirm(\'Are you sure you want to add this book to cart?\')">Add to cart</button>' +
                    '</td>' +
                    '</tr>';
            });
            $('#book-table-body').append(trHTML);
        }
    });
    $.ajax({
        url: 'http://localhost:8080/api/v1/categories',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let trHTML = '';
            $.each(data, function (i, item) {
                trHTML = trHTML + '<tr id="category-' + item.id + '">' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.name + '</td>' +
                    '<td colspan="3">' +
                    '<a class="btn btn-primary" ' +
                    'sec:authorize="hasAnyAuthority(\'ADMIN\')" ' +
                    'onclick="openEditCategory(' + item.id + ', \'' + item.name + '\')" ' +
                    ' data-bs-target="#exampleModalCenter" data-bs-toggle="modal" ' +
                    '>Edit</a>' +
                    '<a class="btn btn-danger" href="/categories" ' + item.id + '  ' +
                    'onclick="apiDeleteCategory(' + item.id + ', this); return false;" ' +
                    'sec:authorize="hasAnyAuthority(\'ADMIN\')">Delete</a>' +
                    '</td>' +
                    '</tr>';
            });
            $('#category-table-body').append(trHTML);
        }
    });
});

function apiDeleteCategory(id) {
    if (confirm('Are you sure you want to delete this category?')) {
        $.ajax({
            url: 'http://localhost:8080/api/v1/categories/' + id,
            type: 'DELETE',
            success: function (result) {
                alert('Deleted successfully')
                $('#category-' + id).remove();
            }
        });
    }
}

function apiDeleteProduct(id) {
    if (confirm('Are you sure you want to delete this book?')) {
        $.ajax({
            url: 'http://localhost:8080/api/v1/products/' + id,
            type: 'DELETE',
            success: function (result) {
                alert('Product deleted successfully')
                $('#product-' + id).remove();
            }
        });
    }
}
