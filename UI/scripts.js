(function () {
    'use strict';
    let historyList = [{'name': 'Victor', 'message': 'Hello!Do you know how has Chelsea played with Norwich?', 'id': uniqueId(), 'time': '12:35:44'},
                         {'name': 'Anonymous', 'message': 'Hi!Chelsea won 2-1', 'id': uniqueId(), 'time': '12:38:12'},
                         {'name': 'Victor', 'message': 'Oh, thanks a lot!', 'id': uniqueId(), 'time': '12:41:03'}];
    let currentUser = 'Victor';
    let editFlag = false;
    window.addEventListener('load', ()=> {
        loadHistory();
        let loginButton = document.querySelector('.changeUsername');
        loginButton.addEventListener('click', changeUsername);
        let sendButton = document.querySelector('#send');
        sendButton.addEventListener('click', sendMessage);
    });

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

    function deleteMessage() {
        let deleteButtons = [].slice.call(document.querySelectorAll('.delButton'));
        deleteButtons
            .forEach((button) => button
                .addEventListener('click', (event) => {
                    let delOne = event.target.parentElement;
                    for (let i = 0; i < historyList.length; i++) {
                        if (historyList[i].id == delOne.id) {
                            historyList.splice(i, 1);
                        }
                    }
                    delOne.parentElement.remove();
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
        var elId = editOne.parentElement.id;
        for (let i = 0; i < historyList.length; i++) {
            if (historyList[i].id == elId) {
                historyList[i].message = editOne.innerText;
            }
        }
    }

    function sendMessage() {
        let inputMessage = document.querySelector('#msg-input');
        if (inputMessage.value != '') {
            let data = createMessage(currentUser, inputMessage.value);
            historyList.push(data);
            inputMessage.value = '';
            let addMessage = document.createElement('div');
            addMessage.className = 'myMessage';
            addMessage.innerHTML = formMyMessage(data);
            let chat = document.querySelector('.chat-messages');
            chat.appendChild(addMessage);
            deleteMessage();
            editMessage();
        }
    }

    function createMessage(name_, msg_) {
        return {
            name: name_,
            message: msg_,
            id: uniqueId(),
            time: new Date().toLocaleTimeString()
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
        let chat = document.querySelector('.chat-messages');
        for (let i = 0; i < historyList.length; i++) {
            let addMessage = document.createElement('div');
            if (historyList[i].name == currentUser) {
                addMessage.className = 'myMessage';
                addMessage.innerHTML = formMyMessage(historyList[i]);
            }
            else {
                addMessage.className = 'alienMessage';
                addMessage.innerHTML = `<div class ="alien-info">${historyList[i].name} ${historyList[i].time}
                                        </div><i class ="message"> ${historyList[i].message}</i>`;
            }
            chat.appendChild(addMessage);
        }
        deleteMessage();
        editMessage();
    }
}());