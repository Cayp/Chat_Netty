getLoginData();
document.querySelector('.chat[data-chat=person1]').classList.add('active-chat');
document.querySelector('.person[data-chat=person1]').classList.add('active');
var friends;
var chat;
initFriends();



function initclick() {
        friends = {
            list: document.querySelector('ul.people'),
            all: document.querySelectorAll('.left .person'),
            name: ''
        };

        chat = {
            container: document.querySelector('.container .right'),
            current: null,
            person: null,
            name: document.querySelector('.container .right .top .name') };


    friends.all.forEach(function (f) {
        f.addEventListener('mousedown', function () {
            f.classList.contains('active') || setAciveChat(f);
        });
    });
}

function setAciveChat(f) {
  friends.list.querySelector('.active').classList.remove('active');
  f.classList.add('active');
  chat.current = chat.container.querySelector('.active-chat');
  chat.person = f.getAttribute('data-chat');
  chat.current.classList.remove('active-chat');
  chat.container.querySelector('[data-chat="' + chat.person + '"]').classList.add('active-chat');
  friends.name = f.querySelector('.name').innerText;
  chat.name.innerHTML = friends.name;
}
function initFriends() {
    $.ajax({
            type: "GET",
            dataType: "json",
            url: "user/getFriends",
            success: function (result) {
                var code = result.code;
                if (code == 20000) {
                   var userlist = result.dataList;
                   for(var i=0;i<userlist.length;i++){
                       var account = userlist[i].account;
                       var name = userlist[i].name;
                       document.getElementById("people").innerHTML += "<li class=\"person\" data-chat=\""+account+"\"><span class=\'name\'>"+name+"</span><span  class=\"time\" style=\"color: #ff0000\"></span></li>"
                       document.getElementById("rightC").innerHTML += "<div id=\""+account+"\" class=\"chat\" data-chat=\""+account+"\"></div>"
                   }
                   initclick();
                   initUnRead();
                } else {
                    alert(result.message)
                }
            },
            error: function () {
                alert("fail!")
            }
        }
    );
}

function getLoginData() {
    var url = decodeURI(window.location.href);
    var data = url.split("?")[1].split("&");
    var account = data[0].split("=")[1];
    var name = data[1].split("=")[1];
    document.getElementById("account").text = account;
    document.getElementById("name").text = name;
}
function initUnRead() {
    $.ajax({
      type:"GET",
      dataType:"json",
      url:"record/unRead/getUnRead",
      success:function (result) {
          var code = result.code;
          if(code == 20000){
              var userlist = result.dataList;
              for(var i=0;i<userlist.length;i++){
                  var fromid= userlist[i].fromid;
                  var text = userlist[i].message;
                  document.getElementById(""+fromid).innerHTML += "<div class=\"bubble you\">"+text+"</div>"
              }
          }
      }
    })
}