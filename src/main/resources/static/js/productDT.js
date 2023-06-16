var dataTable;

$(document).ready(function () {
    loadDataTable();
});

function loadDataTable() {
    dataTable = $('#tblData').DataTable({
        "ajax": {
            url: 'http://localhost:8080/api/v1/products',
            dataSrc: ''
        },
        "columns": [
            { data: 'id', width: '5%' },
            // show cover
            { data: 'cover', width: '10%',
                render: function (data) {
                    return '<img src="' + data + '" alt="Cover" class="img-thumbnail" width="50px" height="50px" />';
                }
            },
            { data: 'title', width: '15%' },
            { data: 'price', width: '10%' },
            { data: 'category', width: '25%' },
            {
                data: 'id',
                render: function (data) {
                    return '<div class="w-75 btn-group" role="group">' +
                        '<a href="/products/edit/' + data + '" class="btn btn-primary mx-2"><i class="bi bi-pencil-square"></i>Edit</a>' +
                        '<a onclick="Delete(\'' + data + '\')" class="btn btn-danger mx-2"><i class="bi bi-trash-fill"></i>Delete</a>' +
                        '</div>';
                },
                width: '35%'
            }
        ]
    });
}

function Delete(id) {
    Swal.fire({
        title: "Are you sure?",
        text: "Once deleted, you will not be able to recover this product!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
    }).then(function (result) {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/v1/products/${id}`,
                type: "DELETE",
                success: function (response) {
                    Swal.fire("Success", "Product deleted successfully!", "success");
                    dataTable.ajax.reload();
                },
                error: function (xhr, status, error) {
                    Swal.fire("Error", "Failed to delete product.", "error");
                },
            });
        } else {
            Swal.fire("Cancelled", "Product deletion was cancelled.", "info");
        }
    });
}
function openEditProduct(id) {
    $.ajax({
        url: 'http://localhost:8080/api/v1/products/' + id,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let formEdit = '';
            formEdit += '<form id="formEdit" method="PUT">';
            formEdit += '<div class="mb-3">';
            formEdit += '<label for="title" class="form-label">Title</label>';
            formEdit += '<input type="text" class="form-control" id="title" name="title" value="' + data.title + '">';
            formEdit += '</div>';
            formEdit += '<div class="mb-3">';
            formEdit += '<label for="price" class="form-label">Price</label>';
            formEdit += '<input type="number" class="form-control" id="price" name="price" value="' + data.price + '">';
            formEdit += '</div>';
            formEdit += '<div class="mb-3">';
            formEdit += '<label for="author" class="form-label">Author</label>';
            formEdit += '<input type="text" class="form-control" id="author" name="author" value="' + data.author + '">';
            formEdit += '</div>';
            formEdit += '<div class="mb-3">';
            formEdit += '<label for="cover" class="form-label">Cover</label>';
            formEdit += '<input type="text" class="form-control" id="cover" name="cover" value="' + data.cover + '">';
            formEdit += '</div>';
            formEdit += '<div class="mb-3">';
            formEdit += '<label for="category" class="form-label">Category</label>';
            formEdit += '<select class="form-select" id="category" name="category">';
            formEdit += '<option value="' + data.category.id + '">' + data.category.name + '</option>';
            formEdit += '</select>';
            formEdit += '</div>';
            formEdit += '<button type="submit" class="btn btn-primary">Submit</button>';
            formEdit += '</form>';
            $('#formEdit').html(formEdit);
            // $('#formEdit').submit(function (e) {
            //     e.preventDefault();
            //     var data = {
            //         id: $('#id').val(),
            //         title: $('#title').val(),
            //         price: $('#price').val(),
            //
            // }
            $('#id').val(data.id);
            $('#title').val(data.title);
            $('#price').val(data.price);
            $('#author').val(data.author);
            $('.covers').html('');
            data.cover.forEach(function (cover) {
                $('.covers').append('<img src="' + cover + '" alt="Cover" class="img-thumbnail" width="50px" height="50px" />');
            });
            $('#category').val(data.category.id);
        }
    });
}
