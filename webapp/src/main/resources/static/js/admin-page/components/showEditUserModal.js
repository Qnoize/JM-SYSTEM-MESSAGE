export class ShowEditUserModal {

    constructor(element, users) {
        this.element=element;
        this.users=users;
    }

    showEditUserModal(){
        const userId = this.element.getAttribute("data-user_id");
        const user = this.users.find(u => u.id === parseInt(userId));

        document.getElementById("input_login").value = user.login;
        document.getElementById("input_name").value = user.name;
        document.getElementById("input_lastName").value = user.lastName;
        document.getElementById("input_email").value = user.email;
        document.getElementById("user_edit_submit").setAttribute("data-user_id", userId);

        $("#modalEditMemberInfo").modal('show');
    }

}