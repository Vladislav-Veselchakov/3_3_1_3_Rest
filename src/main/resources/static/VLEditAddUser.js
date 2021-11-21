//////////////////////////////////////VL add user//////////////////////////////////////////////////////////////////
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
}

let bUpdate = document.getElementById("bUpdate");
bUpdate.onclick = function () {



    // let myModal = new bootstrap.Modal(document.getElementById('editUser'));
    // myModal.hide();

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
    $("#editUser").modal('hide');

}

/////////////////////////////// с пом. fetch() //////////////////
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
        window.location.replace('/admin');
        // let tabUserTable = document.getElementById("Users_table-tab");
        // tabUserTable.classList.add("active");
        // let tabNewUser = document.getElementById("NewUser-tab");
        // tabNewUser.classList.remove("active");
        // document.getElementById("Users_table").classList.add("show", "active");
        // document.getElementById("NewUser").classList.remove("show", "active");

    } else {
        alert("Ошибка HTTP: " + response.status);
    }
} // async function postParams(pbody)

async function getEdit(userID) {
    let response = {};
    try {
        let request = "http://localhost:8080/admin/edit?id=" + userID;
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

///////////////////////// end of c пом fetch() /////////////////////////////////////

////////////////////////////// modal window - load data /////////////////////////////////////////////
var editUserModal = document.getElementById('editUser')
// editUserModal.addEventListener('shown.bs.modal', function (event) {
editUserModal.addEventListener('show.bs.modal', async function (event) {
    let button = event.relatedTarget;
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
////////////////////////////// END of modal window - load data /////////////////////////////////////////////



let panel2 = document.getElementById("panel2");
let vTime = new Date();
panel2.innerHTML = vTime.getHours() + ":" + vTime.getMinutes() + ":" + vTime.getSeconds() + " user name here from File";

let mUser = document.getElementById("v-pills-user-tab");
mUser.onclick = function (elem) {
    alert("not realized yet");

}
