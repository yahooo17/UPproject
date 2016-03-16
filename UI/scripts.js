(function () {
    'use strict';
    let mes = restore();
    let historyList = [];
    let currentUser = 'Victor';
    let editFlag = false;
    let chat;
    createHistoryList();
    window.addEventListener('load', ()=> {
        loadHistory();
        let loginButton = document.querySelector('.changeUsername');
        loginButton.addEventListener('click', changeUsername);
        let sendButton = document.querySelector('#send');
        sendButton.addEventListener('click', sendMessage);
    });

    function createHistoryList() {
        if (mes != null) {
            for (let i = 0; i < mes.length; i++) {
                let data = createMessage(mes[i].name, mes[i].message, mes[i].edit);
                historyList.push(data);
            }
        }
    }

    function changeUsername() {
        let userInput = document.querySelector('.edit-name');
        currentUser = userInput.value;
        updateUser();
        userInput.value = '';
    }

    function updateUser() {
        if (!currentUser) {
            return;
        }
        else {
            let userName = document.querySelector(".userHolder");
            userName.innerText = currentUser;
            let clearChat = document.querySelector('.chat-messages');
            while (clearChat.hasChildNodes()) {
                clearChat.removeChild(clearChat.lastChild);
            }
            loadHistory();
        }
    }

    function updateScroll() {
        chat.scrollTop = chat.scrollHeight;
    }

    function deleteMessage() {
        let deleteButtons = [].slice.call(document.querySelectorAll('.delButton'));
        deleteButtons
            .forEach((button) => button
                .addEventListener('click', (event) => {
                    let delOne = event.target.parentElement;
                    for (let i = 0; i < historyList.length; i++) {
                        if (historyList[i].id == delOne.id) {
                            historyList[i].message = "DELETED";
                        }
                    }
                    delOne.parentElement.remove();
                    store(historyList);
                    updateScroll();
                }));
    }

    function editMessage() {
        let inputMessage = document.querySelector('#msg-input');
        let editButtons = [].slice.call(document.querySelectorAll('.editButton'));
        editButtons
            .forEach((button) => button
                .addEventListener('click', (event) => {
                    if (editFlag == false) {
                        editFlag = true;
                        let editOne = event.target.parentElement.parentElement.getElementsByClassName('message')[0];
                        inputMessage.value = editOne.innerText;
                        let saveChanges = createTempButtons();
                        saveChanges.addEventListener('click', () => saveEdit(editOne));
                    }
                }));
    }

    function createTempButtons() {
        let sendButton = document.querySelector('#send');
        sendButton.disabled = true;
        let saveChanges = document.createElement('button');
        saveChanges.innerText = 'save';
        saveChanges.className = 'tempButton';
        let chat = document.querySelector('.need');
        chat.appendChild(saveChanges);
        let cancel = document.createElement('button');
        cancel.innerText = 'cancel';
        cancel.className = 'tempButton';
        chat.appendChild(cancel);
        cancel.addEventListener('click', cancelChanges);
        return saveChanges;
    }

    function cancelChanges() {
        let editData = document.querySelector('#msg-input');
        editData.value = '';
        let sendButton = document.querySelector('#send');
        sendButton.disabled = false;
        let destroyButtons = [].slice.call(document.querySelectorAll('.tempButton'));
        destroyButtons.forEach((button) => button.remove());
        editFlag = false;
    }

    function saveEdit(editOne) {
        let editData = document.querySelector('#msg-input');
        editOne.innerText = editData.value;
        cancelChanges();
        for (let i = 0; i < historyList.length; i++) {
            if (historyList[i].id == editOne.parentElement.id) {
                historyList[i].message = editOne.innerText;
                historyList[i].edit = "was edited";
            }
        }
        store(historyList);
        updateScroll();
    }

    function sendMessage() {
        let inputMessage = document.querySelector('#msg-input');
        if (inputMessage.value != '') {
            let data = createMessage(currentUser, inputMessage.value, '');
            historyList.push(data);
            inputMessage.value = '';
            let addMessage = document.createElement('div');
            addMessage.className = 'myMessage';
            addMessage.innerHTML = formMyMessage(data);
            let chat = document.querySelector('.chat-messages');
            chat.appendChild(addMessage);
            deleteMessage();
            editMessage();
            store(historyList);
            updateScroll();
        }
    }

    function createMessage(name_, msg_, _edit) {
        return {
            name: name_,
            message: msg_,
            id: uniqueId(),
            time: new Date().toLocaleTimeString(),
            edit: _edit
        }
    }

    function uniqueId() {
        let date = Date.now();
        let random = Math.random() * Math.random();
        return Math.floor(date * random).toString();
    }

    function formMyMessage(data) {
        return `<div id=${data.id}>${data.name} ${data.time}
                <button class="delButton"></button>
                <button class="editButton"></button>
                <div class="message">${data.message}</div></div>`;
    }

    function loadHistory() {
        chat = document.querySelector('.chat-messages');
        for (let i = 0; i < historyList.length; i++) {
            let addMessage = document.createElement('div');
            if (historyList[i].name == currentUser && historyList[i].message != "DELETED") {
                addMessage.className = 'myMessage';
                addMessage.innerHTML = formMyMessage(historyList[i]);
            }
            else if (historyList[i].name != currentUser) {
                addMessage.className = 'alienMessage';
                addMessage.innerHTML = `<div class ="alien-info">${historyList[i].name} ${historyList[i].time} ${historyList[i].edit}
                                        </div><i class ="message"> ${historyList[i].message}</i>`;
            }
            chat.appendChild(addMessage);
        }
        updateScroll();
        deleteMessage();
        editMessage();
    }

    function store(listToSave) {
        if (typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
        }
        localStorage.setItem("HistoryList", JSON.stringify(listToSave));
    }

    function restore() {
        if (typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
        }
        let item = localStorage.getItem("HistoryList");
        return item && JSON.parse(item);
    }
}());