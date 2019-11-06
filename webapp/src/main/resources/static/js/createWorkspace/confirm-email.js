import {CreateWorkspacePaginationService} from './rest/entities-rest-pagination.js'

const  createWorkspace_service = new CreateWorkspacePaginationService();
let result =[];
$("input").each(function() {
   let $this = $(this);
    $(this).data("maxlength", $this.prop("maxlength"));
    $(this).removeAttr("maxlength");
}).bind("input", function() {
    let $this = $(this);
    setTimeout(function() { spill($this, $this.val()); },0);
    result.push($this.val());
    if(checkIfEmpty() !== false && result.length === 6) {
        send();
    } else if (result[0].length === 6) {
        send();
    }
});

function spill($this, val) {
    let maxlength = $this.data("maxlength");
    if ( val.length >= maxlength ) {
        $this.val(val.substring(0, maxlength));
        let next = $this.next("input").focus();
        spill(next, val.substring(maxlength));
    }
    else
        $this.val(val);
}

function checkIfEmpty() {
    let arrayInputs = $('input').map(function () {
        return this.value;
    }).get()

    for (let i; i < arrayInputs.length; i++) {
        if (arrayInputs[i] === "") {
            return false;
        }
    }
}

function send() {
        let arrayInputs = $('input').map(function () {
            return this.value;
        }).get();
        arrayInputs = arrayInputs.join("");
       // let result =  sendCode(arrayInputs);

       const result = createWorkspace_service.sendCode(arrayInputs);
       result.then(value => { //После того как ответ будет получен, начнется выполнение этого блока
           if (value === null) {
               alert("ne tot kod :)")
           } else {
               setTimeout(function () {
                   window.location.href = "/workspacename";
               }, 1000);

           }
       });
}







