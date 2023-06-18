var dataTable;

$(document).ready(function () {
    loadDataTable();
});

function loadDataTable() {
    dataTable = $('#tblData').DataTable({
        "ajax": {
            url: 'http://localhost:8080/api/v2/users',
            dataSrc: ''
        },
        "columns": [
            { data: 'id', width: '5%' },
            { data: 'userName', width: '15%' },
            { data: 'email', width: '10%' },
            { data: 'phone', width: '15%' },
            { data: 'provider', width: '10%',
                render: function (data) {
                    if (data == null) {
                        return '';
                    }
                    return data;
                }
            },
            { data: 'roleName', width: '10%' },
            {
                data: 'userName',
                render: function (data) {
                    return '<div class="w-75 btn-group" role="group">' +
                        '<a onclick="openEditUser(\'' + data + '\')" data-bs-target="#exampleModalCenter" data-bs-toggle="modal" class="btn btn-primary mx-2"><i class="bi bi-search"></i>Detail</a>' +
                        '<a onclick="lockUser(\'' + data + '\')" class="btn btn-danger mx-2"><i class="bi bi-lock-fill"></i>Lock</a>' +
                        '</div>';
                },
                width: '35%'
            }
        ]
    });
}

function lockUser(username) {
    Swal.fire({
        title: "Are you sure?",
        text: "Once locked, you will not be able to recover this user!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, lock it!",
    }).then(function (result) {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/v1/users/lock/${username}`,
                type: "PUT",
                success: function (response) {
                    Swal.fire("Success", "User locked successfully!", "success");
                    dataTable.ajax.reload();
                },
                error: function (xhr, status, error) {
                    Swal.fire("Error", "Failed to lock user.", "error");
                }
            });
        } else {
            Swal.fire("Cancelled", "User is safe :)", "error");
        }
    });
}
function openEditUser(username) {
    $.ajax({
        url: 'http://localhost:8080/api/v2/users/' + username,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            $('#exampleModalCenter .modal-body').html('');
            let html = '';
            html += '<div class="form-group row">' +
                '<label for="userName" class="col-sm-3 col-form-label">Username</label>' +
                '<div class="col-sm-9">' +
                '<input type="text" class="form-control" id="userName" value="' + data.userName + '" readonly>' +
                '</div>' +
                '</div>';
            html += '<div class="form-group row">' +
                '<label for="email" class="col-sm-3 col-form-label">Email</label>' +
                '<div class="col-sm-9">' +
                '<input type="text" class="form-control" id="email" value="' + data.email + '" readonly>' +
                '</div>' +
                '</div>';
            html += '<div class="form-group row">' +
                '<label for="roleName" class="col-sm-3 col-form-label">Role</label>' +
                '<div class="col-sm-9">' +
                '<input type="text" class="form-control" id="roleName" value="' + data.roleName + '" readonly>' +
                '</div>' +
                '</div>';
            html += '<div class="form-group row">' +
                '<label for="provider" class="col-sm-3 col-form-label">Provider</label>' +
                '<div class="col-sm-9">' +
                '<input type="text" class="form-control" id="provider" value="' + data.provider + '" readonly>' +
                '</div>' +
                '</div>';
            $('#exampleModalCenter .modal-body').html(html);
            $('#exampleModalCenter').modal('show');
        },
        error: function (xhr, status, error) {
            Swal.fire("Error", "Failed to get user.", "error");
        }
    });
}
