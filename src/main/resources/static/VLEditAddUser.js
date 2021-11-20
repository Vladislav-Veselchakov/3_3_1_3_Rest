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

/////////////////////////////// с пом. fetch() //////////////////
async function postParams(pbody) {
    let response = {};
    try {
        response = await fetch("/admin/addUser", {
            credentials: 'include',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(pbody)
        });
    } catch (ex) {
        console.log(ex.message);
    }
    if (response.ok) { // если HTTP-статус в диапазоне 200-299 получаем тело ответа
        window.location.replace('http://localhost:8080/admin');
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


let panel2 = document.getElementById("panel2");
let vTime = new Date();
panel2.innerHTML = vTime.getHours() + ":" + vTime.getMinutes() + ":" + vTime.getSeconds() + " user name here from File";

let mUser = document.getElementById("v-pills-user-tab");
mUser.onclick = function (elem) {
    alert("not realized yet");

}
