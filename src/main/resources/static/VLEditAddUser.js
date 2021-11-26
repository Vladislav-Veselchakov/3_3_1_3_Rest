/////////////////////////////// загружаем и рисуем таблицу user'ов //////////////////////////////////////////////
//$(document).ready(function() {
window.onload = updateUsersTable;
//})/;
async function updateUsersTable() {
    let jUsers = await getUsersTable();
    $("#usersTable tbody").empty();
    for (i = 0; i < jUsers.length; i++) {
        $('#usersTable tbody').append(`<tr>
            <td> ${jUsers[i].id} </td>
            <td> ${jUsers[i].firstName} </td>
            <td> ${jUsers[i].lastName} </td>
            <td> ${jUsers[i].email} </td>
            <td> ${jUsers[i].password} </td>
            <td> ${jUsers[i].created} </td>
            <td> ${jUsers[i].modified} </td>
            <td class="vl_EditButton"> <input type="button" value="Edit" class="btn btn-success" id="'bEdit' + ${jUsers[i].id}" data-bs-toggle="modal" data-bs-target="#editUser" UserID="${jUsers[i].id}"/> </td>
            <td class="vl_DeleteButton"> <input type="button" value="Delete" class="btn btn-danger" id="'bDelete' + ${jUsers[i].id}" data-bs-toggle="modal" data-bs-target="#editUser" UserID="${jUsers[i].id}"/> </td>
        </tr>`);
    }

    let bEditArr = document.querySelectorAll("td.vl_EditButton input.btn.btn-success");
    for (i = 0; i < bEditArr.length; i++) {
        bEditArr[i].onclick = bEditOnClick;
    }

    let bDeleteArr = document.querySelectorAll("td.vl_DeleteButton input.btn.btn-danger");
    for (i = 0; i < bDeleteArr.length; i++) {
        bDeleteArr[i].onclick = bDeleteOnClick;
    }
};


///////////////////////// кнопка "Add new user" на закладке New User /////////////////////////////////////
let bAddUser = document.getElementById("bAddUser");
bAddUser.onclick = function() {
    let body = {}
    body.firstName = document.getElementById("firstName").value;
    body.lastName = document.getElementById("lastName").value;
    body.email = document.getElementById("email").value;
    body.password = document.getElementById("password").value;
    body.roles = []

    let selectedRoles = selectTag.selectedOptions;
    for(let i = 0; i < selectedRoles.length; i++) {
        body.roles[i] = {
            checked : true,
            id : selectedRoles[i].value,
            name : selectedRoles[i].text
        };
    }
    postParams(body)

    this.classList.remove('active');
    let bUsersTable = $("#Users_table-tab");
    bUsersTable.addClass("active");

    $("#NewUser").removeClass("show active");
    $("#Users_table").addClass("show active");
}

/////////////////////////// кнопка Edit в таблице ///////////////////////////////////////////////////
//let bEdit = document.getElementById("bEdit");
let bEditOnClick = function (pElem) {
    $("#staticBackdropLabel").text("Edit User");
    $("#firstNameMod").prop("disabled", false);
    $("#lastNameMod").prop("disabled", false);
    $("#emailMod").prop("disabled", false);
    $("#passwordMod").prop("disabled", false);
    $("#selectTagMod").prop("disabled", false);
    let button = $("#bEditDelete");
    if (button.hasClass("btn-danger")) {
        button.removeClass("btn-danger");
        button.addClass("btn-primary")
    }
    $("#bEditDelete").text("Edit");
   // $("#bEditDelete").click();
}

/////////////////////////// кнопка Delete в таблице ///////////////////////////////////////////////////
// let bDelete = document.getElementById("bDelete");
let bDeleteOnClick = function (pElem) {
    $("#staticBackdropLabel").text("Delete User");
    $("#firstNameMod").prop("disabled", true);
    $("#lastNameMod").prop("disabled", true);
    $("#emailMod").prop("disabled", true);
    $("#passwordMod").prop("disabled", true);
    $("#selectTagMod").prop("disabled", true);
    let button = $("#bEditDelete");
    if (button.hasClass("btn-primary")) {
        button.removeClass("btn-primary");
        button.addClass("btn-danger")
    }
    $("#bEditDelete").text("Delete");


}

//////////////////////////// при открытии мод. окна: //////////////////////////////////////////////
let editUserModal = document.getElementById('editUser')
editUserModal.addEventListener('show.bs.modal', async function (event) {
    let button = event.relatedTarget; // кнопка, которая открыла окно
    let userID = button.getAttribute('UserID');
    let response = await getEdit(userID);

    $("#idMod").val(response.user.id);
    $("#createdMod").val(response.user.created);
    $("#firstNameMod").val(response.user.firstName);
    $("#lastNameMod").val(response.user.lastName);
    $("#emailMod").val(response.user.email);
    $("#passwordMod").val(response.user.password);
    let selectTag = document.getElementById("selectTagMod");
    selectTag.options.length = 0;

    for (var i = 0; i<response.roles.length; i++){
        let opt = document.createElement('option');
        opt.value = response.roles[i].id;
        opt.text = response.roles[i].name;
        if(response.roles[i].checked)
            opt.selected = true;
        selectTag.appendChild(opt);
    }

})

/////////////////////////////////// кнопка "Edit/Delete" на модальном окне нажатие //////////////////////
let bEditDelete = document.getElementById("bEditDelete");
bEditDelete.onclick = function () {
    // let myModal = new bootstrap.Modal(document.getElementById('editUser'));
    // myModal.hide();

    if (this.classList.contains("btn-primary")) {
        let lvBody = {};
        lvBody.id = $("#idMod").val();
        lvBody.created = $("#createdMod").val();
        lvBody.firstName = $("#firstNameMod").val();
        lvBody.lastName = $("#lastNameMod").val();
        lvBody.email = $("#emailMod").val();
        lvBody.password = $("#passwordMod").val();
        lvBody.roles = [];
        selectTag = document.getElementById("selectTagMod");
        let selectedRoles = selectTag.selectedOptions;
        for(let i = 0; i < selectedRoles.length; i++) {
            lvBody.roles[i] = {
                checked : true,
                id : selectedRoles[i].value,
                name : selectedRoles[i].text
            };
        }
        postParams(lvBody);
    }

    if (this.classList.contains("btn-danger")) {
        getDeleteUser($("#idMod").val());
    }

    $("#editUser").modal('hide');
}

///////////////////////////////////////// POST: Add/update user -//////////////////////////////////////
async function postParams(pbody) {
    let response = {};
    try {
        if(pbody.id) { // если отредактированный юзер сюда попал:
            response = await fetch("/admin/editUser", {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(pbody)
            });

        } else { // если новый юзер:
            response = await fetch("/admin/addUser", {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(pbody)
            });
        }
    } catch (ex) {
        console.log(ex.message);
    }
    if (response.ok) { // если HTTP-статус в диапазоне 200-299 получаем тело ответа
        updateUsersTable();
    } else {
        alert("Ошибка HTTP: " + response.status);
    }
} // async function postParams(pbody)

//////////////////////////// После откр. мод. окна (кн. edit/delete). Возвращает json с user'ом ///////////
async function getEdit(userID) {
    let response = {};
    try {
        let request = "/admin/edit?id=" + userID;
        response = await fetch(request, {
            credentials: 'include',
            method: 'GET'
            ,
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            }
            // ,
            // body: JSON.stringify(pbody)
        });
    } catch (ex) {
        console.log(ex.message);
    }
    if (response.ok) { // если HTTP-статус в диапазоне 200-299 получаем тело ответа
        let json = await response.json();
        return json;

    } else {
        alert("Ошибка HTTP: " + response.status);
    }
} // async function getEdit(pbody)

////////////////////////////////// удалить user'а метод GET ///////////////////////////////////////////////
async function getDeleteUser(userID) {
    try {
        let request = "/admin/delete?id=" + userID;
        response = await fetch(request, {
            credentials: 'include', method: 'GET',
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        });
    } catch (ex) {
        console.log(ex.message);
    }
    if (response.ok) { // если HTTP-статус в диапазоне 200-299 получаем тело ответа
        // window.location.href = "/admin";
        $("#result001").html("Deleted at " + new Date());
        updateUsersTable();
    } else {
        alert("Ошибка HTTP: " + response.status);
    }

}

////////////////////////////// получить user'ов //////////////////////////////////////////////////
async function getUsersTable(userID) {
    try {
        response = await fetch("/admin/getUsersTable", {
            credentials: 'include', method: 'GET',
            headers: {'Content-Type': 'application/json;charset=utf-8'}
        });
    } catch (ex) {
        console.log(ex.message);
    }
    if (response.ok) { // если HTTP-статус в диапазоне 200-299 получаем тело ответа
        let json = await response.json();
        $("#result001").html("table updated at " + new Date());
        return json;
    } else {
        alert("Ошибка HTTP: " + response.status);
    }
}

//////////////////////////////////////// разное удалить ///////////////////////////////////////////////////////////////
let panel2 = document.getElementById("panel2");
let vTime = new Date().toLocaleString();
// panel2.innerHTML = vTime.getHours() + ":" + vTime.getMinutes() + ":" + vTime.getSeconds() + " user name here from File";
panel2.innerHTML = vTime + " user name here from File";


