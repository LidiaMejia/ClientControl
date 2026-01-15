document.addEventListener("DOMContentLoaded", () => {
    document.addEventListener("submit", function(event) {
        const form = event.target;  

        if(form.matches("#clientForm")) {
            event.preventDefault();

            fetch(form.action, {
                method: "POST",
                body: new FormData(form)
            })
                .then(res => res.text()
                .then(html => {
                    console.log(res.headers.get("X-Has-Errors"));

                    const modalId = "clientModal";
                    const hasErrors = res.headers.get("X-Has-Errors") === "true";

                    closeModal(modalId);

                    if(hasErrors) {
                        showModalData(modalId, "clientModalContent", html);
                    }
                    else {
                        location.reload();
                    }
                }));
        }
    });    
});

function deleteConfirmation(fullName, confirmText) {
    if (!confirm(confirmText + " " + fullName + "?")) {
        event.preventDefault();
    }
}

function openClientModal(element) {
    let path = element.getAttribute("data-path");
    const id = element.getAttribute("data-id");

    if(id !== null) {
        path = path + id;
    }

    fetch(path)
        .then(res => res.text())
        .then(html => {
            showModalData("clientModal", "clientModalContent", html);
    });
}

    function showModalData(modalId, modalContentId, html) {
    if(modalContentId !== null && html !== null) {
        document.getElementById(modalContentId).innerHTML = html;
    }

    new bootstrap.Modal(document.getElementById(modalId)).show();    
}

function closeModal(modalId) {
    let modalElement = document.getElementById(modalId);

    let modalInstance = bootstrap.Modal.getInstance(modalElement);

    if(modalInstance !== null) {
        modalInstance.hide();
    }
}