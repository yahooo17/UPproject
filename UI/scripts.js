(function () {
    'use strict';
    var Application = {
        mainUrl : 'http://localhost:8080/chat',
        messageList : [],
        token : 'TN11EN',
        isConnected : null
    };
    let currentUser = loadUsername();
    let editFlag = false;
    let chat;
    window.addEventListener('load', ()=> {
        createHistoryList();
        document.querySelector(".userHolder").innerText = currentUser;
        let loginButton = document.querySelector('.changeUsername');
        loginButton.addEventListener('click', changeUsername);
        let sendButton = document.querySelector('#send');
        sendButton.addEventListener('click', sendMessage);
    });

    function createHistoryList() {
        var url = Application.mainUrl + '?token=' + Application.token;
        ajax('GET', url, null, function(responseText){
            var json = JSON.parse(responseText);
            Application.messageList = json.messages;
            loadHistory();
            updateScroll();
            Connect();
        });

        if (Application.messageList == null) {
            Application.messageList = [];
            store(Application.messageList);
        }
    }

    function loadUsername(){
        let name = restoreUsername();
        if (name != null){
            return name;
        }
        else {
            return '';
        }
    }

    function changeUsername() {
        let userInput = document.querySelector('.edit-name');
        currentUser = userInput.value;
        updateUser();
        userInput.value = '';
        storeUsername();
    }

    function updateUser() {
        if (!currentUser) {

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
                    for (let i = 0; i < Application.messageList.length; i++) {
                        if (Application.messageList[i].id == delOne.id) {
                            Application.messageList[i].text = "DELETED";
                            var mesToDelete = {
                                id: Application.messageList[i].id
                            };
                            ajax('DELETE', Application.mainUrl,JSON.stringify(mesToDelete), function(){
                                loadHistory();
                                updateScroll();
                            });
                        }
                    }
                    store(Application.messageList);
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
        updateScroll();
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
        updateScroll();
    }

    function saveEdit(editOne) {
        let editData = document.querySelector('#msg-input');
        editOne.innerText = editData.value;
        cancelChanges();
        for (let i = 0; i < Application.messageList.length; i++) {
            if (Application.messageList[i].id == editOne.parentElement.id) {
                Application.messageList[i].text = editOne.innerText;
                Application.messageList[i].isEdit = "was edited";
                ajax('PUT', Application.mainUrl, JSON.stringify(Application.messageList[i]), function(){
                    loadHistory();
                    updateScroll();
                });
            }
        }
        store(Application.messageList);
    }

    function sendMessage() {
        let inputMessage = document.querySelector('#msg-input');
        if (inputMessage.value != '') {
            let data = createMessage(currentUser, inputMessage.value, '');
            Application.messageList.push(data);
            inputMessage.value = '';
            let addMessage = document.createElement('div');
            addMessage.className = 'myMessage';
            addMessage.innerHTML = formMyMessage(data);
            ajax('POST', Application.mainUrl, JSON.stringify(data), function(){
                loadHistory();
                updateScroll();
            });
            deleteMessage();
            editMessage();
            store(Application.messageList);
        }
    }

    function createMessage(name_, msg_, _edit) {
        return {
            author: name_,
            text: msg_,
            id: uniqueId(),
            timestamp: new Date().getTime(),
            isEdit: _edit
        }
    }

    function uniqueId() {
        let date = Date.now();
        let random = Math.random() * Math.random();
        return Math.floor(date * random).toString();
    }

    function formMyMessage(data) {
        return `<div id=${data.id}>${data.author} ${new Date(data.timestamp).toLocaleString()} <div class="edit">${data.isEdit}</div>
                <button class="delButton"></button>
                <button class="editButton"></button>
                <div class="message">${data.text}</div></div>`;
    }

    function formDeletedMessage(data){
        return `<div id=${data.id}>${data.author} ${new Date(data.timestamp).toLocaleString()}
                <div class="message">${data.text}</div></div>`;
    }

    function loadHistory() {
        chat = document.querySelector('.chat-messages');
        var children = chat.children;
        while (children.length > 0) {
            chat.removeChild(children[0]);
        }
        for (let i = 0; i < Application.messageList.length; i++) {
            let addMessage = document.createElement('div');
            if (Application.messageList[i].author == currentUser) {
                addMessage.className = 'myMessage';
                if (Application.messageList[i].text != "DELETED") {
                    addMessage.innerHTML = formMyMessage(Application.messageList[i]);
                }
                else if (Application.messageList[i].text == "DELETED"){
                    addMessage.innerHTML = formDeletedMessage(Application.messageList[i]);
                }
            }
            else if (Application.messageList[i].author != currentUser) {
                addMessage.className = 'alienMessage';
                addMessage.innerHTML = `<div class ="alien-info">${Application.messageList[i].author} ${new Date(Application.messageList[i].timestamp).toLocaleString()}
                                    ${Application.messageList[i].isEdit}</div><i class ="message"> ${Application.messageList[i].text}</i>`;
            }
            chat.appendChild(addMessage);
        }
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

    function restoreUsername() {
        if (typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
        }
        return localStorage.getItem("Username");
    }

    function storeUsername() {
        if (typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
        }
        localStorage.setItem("Username", currentUser);
    }
    function ajax(method, url, data, continueWith) {
        var xhr = new XMLHttpRequest();

        xhr.open(method || 'GET', url, true);

        xhr.onload = function () {
            if (xhr.readyState !== 4)
                return;

            if(xhr.status != 200) {
                defaultErrorHandler('Error on the server side, response ' + xhr.status);
                return;
            }

            if(isError(xhr.responseText)) {
                defaultErrorHandler('Error on the server side, response ' + xhr.responseText);
                return;
            }

            continueWith(xhr.responseText);
            Application.isConnected = true;
        };

        xhr.ontimeout = function () {
            ServerError();
        };

        xhr.onerror = function () {
            ServerError();
        };

        xhr.send(data);
    }

    function defaultErrorHandler(message) {
        console.error(message);
    }

    function isError(text) {
        if(text == "")
            return false;

        try {
            var obj = JSON.parse(text);
        } catch(ex) {
            return true;
        }

        return !!obj.error;
    }

    function ServerError(){
        let errorServer = document.getElementsByClassName('ServerError')[0];
        errorServer.innerHTML = `<img class="alarm" src="images/warning.png" alt="Connection problems">`;
    }

    function Connect() {
        if(Application.isConnected)
            return;

        function whileConnected() {
            Application.isConnected = setTimeout(function () {
                ajax('GET', Application.mainUrl + '?token=' + Application.token, null,function (serverResponse) {
                    if (Application.isConnected) {
                        var json = JSON.parse(serverResponse);
                        Application.messageList = json.messages;
                        loadHistory();
                        whileConnected();
                    }
                });
            }, Math.round(1000));
        }

        whileConnected();
    }
}());